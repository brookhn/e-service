package com.pp.server.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class LifeCycleBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof LifeCycleBean) {
            System.out.println("04-->调用postProcessBeforeInitialization方法, 获取到的beanName: " + beanName);
            ((LifeCycleBean) bean).setName("李四");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof LifeCycleBean) {
            System.out.println("07-->调用postProcessAfterInitialization, 获取到的beanName: " + beanName);
            ((LifeCycleBean) bean).setAge(30);
        }
        return bean;
    }

}
