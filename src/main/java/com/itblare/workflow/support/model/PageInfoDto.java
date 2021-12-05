package com.itblare.workflow.support.model;

import org.flowable.common.engine.impl.Direction;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 自定义分页结果
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/7/14 10:35
 */
public class PageInfoDto<T> implements Serializable {

    private static final long serialVersionUID = -4199585888369482440L;

    /**
     * 总数
     */
    private long total = 0;

    /**
     * 每页显示条数，默认 10
     */
    private int pageSize = 10;

    /**
     * 当前页
     */
    private int pageNo = 1;

    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();

    /**
     * 排序信息
     */
    private List<Order> orders;

    public PageInfoDto(long total, int size, int current, List<T> records, List<Order> orders) {
        this(size, current, orders);
        this.pageNo = current;
        this.records = records;
    }

    public PageInfoDto(int size, int current, List<Order> orders) {
        this(size, current);
        this.orders = orders;
    }

    public PageInfoDto(int size, int current) {
        this.pageSize = size;
        this.pageNo = current;
    }

    public static <T> PageInfoDto<T> withFlowable(int size, int current, List<Order> orders) {
        return new PageInfoDto<>(size, current < 1 ? 1 : (current - 1), orders);
    }

    public static <T> PageInfoDto<T> withFlowable(int size, int current) {
        return new PageInfoDto<>(size, current < 1 ? 1 : (current - 1));
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public int getOffset() {
        return pageNo * pageSize;
    }

    /**
     * 排序对象
     *
     * @author Blare
     */
    public static class Order implements Serializable {

        private static final long serialVersionUID = 678449194337269790L;

        /**
         * 排序字段
         */
        private String property;

        /**
         * 排序方向
         */
        private Direction direction;

        public Order(String property, Direction direction) {
            super();
            this.property = property;
            this.direction = direction;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public Direction getDirection() {
            return direction;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }
    }

    // /**
    //  * 升降排序枚举
    //  *
    //  * @author Blare
    //  */
    // public enum Direction {
    //
    //     ASC, DESC;
    //
    //     public boolean isAscending() {
    //         return this.equals(ASC);
    //     }
    //
    //     public boolean isDescending() {
    //         return this.equals(DESC);
    //     }
    //
    //     public static Direction fromString(String value) {
    //
    //         try {
    //             return Direction.valueOf(value.toUpperCase(Locale.US));
    //         } catch (Exception e) {
    //             throw new IllegalArgumentException(String.format("给定排序方向的值“%s”无效！ 必须是“desc”或“asc”（不区分大小写）！", value), e);
    //         }
    //     }
    //
    //     public static Optional<Direction> fromOptionalString(String value) {
    //         try {
    //             return Optional.of(fromString(value));
    //         } catch (IllegalArgumentException e) {
    //             return Optional.empty();
    //         }
    //     }
    // }
}