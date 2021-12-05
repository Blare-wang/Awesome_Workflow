package com.itblare.workflow.support.spring;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring Bean获取工具
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/12/2 23:37
 */
@Component
public class SpringUtilConfig implements ApplicationContextAware {

    /**
     * 当前IOC
     */
    private static ApplicationContext applicationContext;

    /**
     * 设置当前上下文环境，此方法由spring自动装配
     */
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringUtilConfig.applicationContext = applicationContext;
    }

    /**
     * 从当前IOC获取bean
     */
    public static <T> T getBean(Class<T> c) {
        return applicationContext.getBean(c);
    }
}