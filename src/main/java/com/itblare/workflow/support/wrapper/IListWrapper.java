package com.itblare.workflow.support.wrapper;

import java.util.List;

/**
 * 列表查转换基类
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/12/2 22:17
 */
public interface IListWrapper<T, U> {

    /**
     * 执行列表转换
     *
     * @param list 待转换列表
     * @return {@link List<T>}
     * @author Blare
     */
    List<T> execute(List<U> list);

    /**
     * 执行对象转换
     *
     * @param u 待转换对象
     * @return {@link T}
     * @author Blare
     */
    T execute(U u);

}