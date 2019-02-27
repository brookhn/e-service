package com.pp.server.spring;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring bean load validation
 */
public class LifeCycleBean implements BeanNameAware,
        BeanFactoryAware,
        ApplicationContextAware,
        InitializingBean,
        DisposableBean {

    private String name;

    private Integer age;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("2 ->BeanFactoryAware 接口被调用了");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("1 ->BeanNameAware 获取 beanName:"+ name);
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("09-->DisposableBean接口被调用了");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("05-->InitializingBean接口被调用了");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("03-->ApplicationContextAware接口被调用了");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void myDestroy()
    {
        System.out.println("10-->myDestroy接口被调用了");
    }

    public void myInit()
    {
        System.out.println("06-->myInit接口被调用了");
    }


}
