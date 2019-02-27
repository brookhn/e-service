package com.pp.server.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;

public class MyFactoryBeanFirst implements BeanFactoryPostProcessor, Ordered {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("MyFactoryBeanFirst 第" + getOrder()+"调用");
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("dog");
        if (null != beanDefinition)
        {
            MutablePropertyValues pv = beanDefinition.getPropertyValues();
            if (pv.contains("name"))
            {
                pv.addPropertyValue("name", "大强");
            }
            beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
