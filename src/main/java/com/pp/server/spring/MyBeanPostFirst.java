package com.pp.server.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

public class MyBeanPostFirst implements BeanPostProcessor, Ordered {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("benprocess 第" + getOrder()+"调用");
        if (bean instanceof Dog)
        {
            ((Dog) bean).setName("qiangqing");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Dog)
        {
            ((Dog) bean).setName("postBeanDog");
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
