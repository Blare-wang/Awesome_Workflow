package com.itblare.workflow.support.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 构建器
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/7/14 10:06
 */
public class Builder<T> {

    /**
     * 实例化器
     */
    private final Supplier<T> instantiator;

    /**
     * 消费列表
     */
    private final List<Consumer<T>> modifiers = new ArrayList<>();

    public Builder(Supplier<T> instantiator) {
        this.instantiator = instantiator;
    }

    public static <T> Builder<T> of(Supplier<T> instantiator) {
        return new Builder<>(instantiator);
    }

    /**
     * 一个参数的构建
     *
     * @param consumer 构建执行方法对象
     * @param p1       参数1
     * @return {@link Builder<T>}
     * @author Blare
     */
    public <P1> Builder<T> with(Consumer1<T, P1> consumer, P1 p1) {
        /*final Consumer<T> c = new Consumer<T>() {
            @Override
            public void accept(T instance) {
                consumer.accept(instance, p1);
            }
        };*/
        Consumer<T> c = instance -> consumer.accept(instance, p1);
        modifiers.add(c);
        return this;
    }

    /**
     * 两个参数的构建对象
     *
     * @param consumer 构建执行方法
     * @param p1       参数1
     * @param p2       参数2
     * @return {@link Builder<T>}
     * @author Blare
     */
    public <P1, P2> Builder<T> with(Consumer2<T, P1, P2> consumer, P1 p1, P2 p2) {
        final Consumer<T> c = instance -> consumer.accept(instance, p1, p2);
        modifiers.add(c);
        return this;
    }

    /**
     * 三个参数的构建对象
     *
     * @param consumer 构建执行方法
     * @param p1       参数1
     * @param p2       参数2
     * @param p3       参数3
     * @return {@link Builder<T>}
     * @author Blare
     */
    public <P1, P2, P3> Builder<T> with(Consumer3<T, P1, P2, P3> consumer, P1 p1, P2 p2, P3 p3) {
        final Consumer<T> c = instance -> consumer.accept(instance, p1, p2, p3);
        modifiers.add(c);
        return this;
    }

    /**
     * 执行构建
     *
     * @return {@link T}
     * @author Blare
     */
    public T build() {
        final T value = instantiator.get();
        modifiers.forEach(modifier -> modifier.accept(value));
        modifiers.clear();
        return value;
    }

    /**
     * 1 个参数 Consumer accept执行
     */
    @FunctionalInterface
    public interface Consumer1<T, P1> {
        void accept(T t, P1 p1);
    }

    /**
     * 2 个参数 Consumer accept执行
     */
    @FunctionalInterface
    private interface Consumer2<T, P1, P2> {
        void accept(T t, P1 p1, P2 p2);
    }

    /**
     * 3 个参数 Consumer accept执行
     */
    @FunctionalInterface
    private interface Consumer3<T, P1, P2, P3> {
        void accept(T t, P1 p1, P2 p2, P3 p3);
    }

    public static void main(String[] args) {
        // final StorageConfig build = Builder.of(StorageConfig::new)
        //     .with((v, n) -> {
        //         v.setAliyunConfig(n);
        //     }, new StorageConfig.AliyunConfig())
        //     .build();
        //
        // final StorageConfig build1 = Builder.of(StorageConfig::new)
        //     .with(StorageConfig::setAliyunConfig, new StorageConfig.AliyunConfig())
        //     .build();
    }
}