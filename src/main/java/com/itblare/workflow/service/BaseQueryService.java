package com.itblare.workflow.service;

import com.itblare.workflow.support.model.PageInfoDto;
import com.itblare.workflow.support.model.req.BaseQueryDto;
import com.itblare.workflow.support.spring.SpringUtilConfig;
import com.itblare.workflow.support.wrapper.IListWrapper;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.api.query.Query;
import org.flowable.common.engine.api.query.QueryProperty;
import org.flowable.common.engine.impl.Direction;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 基础查询接口
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/12/5 11:56
 */
public interface BaseQueryService<U> {

    /**
     * Flowable 分页对象
     * Flowable 内置分页采用limit offset语法进行分页查询
     *
     * @param baseQueryVo 基础查询参数
     * @return {@link PageInfoDto}
     * @author Blare
     */
    default <T> PageInfoDto<T> getFlowablePage(BaseQueryDto baseQueryVo) {
        final Integer pageNo = baseQueryVo.getPageNo();
        final Integer pageSize = baseQueryVo.getPageSize();
        Map<String, String> orderRule = baseQueryVo.getOrderRule();
        if (Objects.nonNull(orderRule) && orderRule.size() > 0) {
            final List<PageInfoDto.Order> orders = baseQueryVo.getOrderRule()
                    .entrySet()
                    .stream()
                    .map(o -> {
                        final String column = o.getKey();
                        final String sortBy = o.getValue();
                        Direction direction;
                        if (sortBy.toLowerCase(Locale.ROOT).equals(Direction.DESCENDING.getName())) {
                            direction = Direction.DESCENDING;
                        } else if (sortBy.toLowerCase(Locale.ROOT).equals(Direction.ASCENDING.getName())) {
                            direction = Direction.ASCENDING;
                        } else {
                            direction = Direction.DESCENDING;
                        }
                        return new PageInfoDto.Order(column, direction);
                    })
                    .collect(Collectors.toList());
            return PageInfoDto.withFlowable(pageSize, pageNo, orders);
        }
        return PageInfoDto.withFlowable(pageSize, pageNo);
    }

    /**
     * 获取分页信息
     *
     * @param baseQueryVo             基础查询参数
     * @param query                   查询对象
     * @param listWrapperClass        列表转换器
     * @param allowedSortProperties   排序属性
     * @param defaultDescSortProperty 默认排序属性
     * @return {@link PageInfoDto<T>}
     * @author Blare
     */
    default <T> PageInfoDto<T> pageList(BaseQueryDto baseQueryVo,
                                        Query<?, U> query,
                                        Class<? extends IListWrapper<T, U>> listWrapperClass,
                                        Map<String, QueryProperty> allowedSortProperties,
                                        QueryProperty defaultDescSortProperty) {
        return pageList(getFlowablePage(baseQueryVo), query, listWrapperClass, allowedSortProperties, defaultDescSortProperty);
    }

    /**
     * 获取分页信息
     *
     * @param pageInfoDto             分页参数
     * @param query                   查询对象
     * @param listWrapperClass        列表转换器
     * @param allowedSortProperties   排序属性
     * @param defaultDescSortProperty 默认排序属性
     * @return {@link PageInfoDto<T>}
     * @author Blare
     */
    @SuppressWarnings("unchecked")
    default <T> PageInfoDto<T> pageList(PageInfoDto<T> pageInfoDto,
                                        Query<?, U> query,
                                        Class<? extends IListWrapper<T, U>> listWrapperClass,
                                        Map<String, QueryProperty> allowedSortProperties,
                                        QueryProperty defaultDescSortProperty) {
        List<U> list;
        if (Objects.isNull(pageInfoDto)) {
            list = query.list();
            pageInfoDto = PageInfoDto.withFlowable(0, 0);
        } else {
            setQueryOrder(pageInfoDto.getOrders(), query, allowedSortProperties, defaultDescSortProperty);
            list = query.listPage(pageInfoDto.getOffset(), pageInfoDto.getPageSize());
        }
        if (Objects.nonNull(listWrapperClass)) {
            pageInfoDto.setRecords(listWrapper(listWrapperClass, list));
        } else {
            pageInfoDto.setRecords((List<T>) list);
        }
        pageInfoDto.setTotal(query.count());
        return pageInfoDto;
    }

    /**
     * 查询排序
     *
     * @param orders                  排序规则
     * @param query                   查询对象
     * @param allowedSortProperties   被允许的排序属性
     * @param defaultDescSortProperty 默认排序属性
     * @author Blare
     */
    default void setQueryOrder(List<PageInfoDto.Order> orders, Query<?, U> query, Map<String, QueryProperty> allowedSortProperties, QueryProperty defaultDescSortProperty) {
        if (Objects.nonNull(orders) && !orders.isEmpty()) {
            orders.forEach(order -> {
                final QueryProperty property = allowedSortProperties.get(order.getProperty());
                if (Objects.isNull(property)) {
                    throw new FlowableIllegalArgumentException("参数“orders”的值无效, " + order.getProperty() + " 不是有效的属性");
                }
                query.orderBy(property);
                if (order.getDirection() == Direction.DESCENDING) {
                    query.desc();
                } else {
                    query.asc();
                }
            });
        } else if (Objects.nonNull(defaultDescSortProperty)) {
            query.orderBy(defaultDescSortProperty).desc();
        }
    }

    /**
     * 列表转换
     *
     * @param listWrapperClass 转换服务类
     * @param list             待转换列表
     * @return {@link List<T>}
     * @author Blare
     */
    default <T> List<T> listWrapper(Class<? extends IListWrapper<T, U>> listWrapperClass, List<U> list) {
        IListWrapper<T, U> iListWrapper = SpringUtilConfig.getBean(listWrapperClass);
        return iListWrapper.execute(list); // 列表转换处理
    }
}