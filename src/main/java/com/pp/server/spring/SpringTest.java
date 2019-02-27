package com.pp.server.spring;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTest {
    public static void testBeforeInterceptor()
    {
        Animal animal = new Dog();
        MethodBeforeAdvice beforeAdvice = new MyMethodBeforeAdvice();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(animal);
        proxyFactory.addAdvice(beforeAdvice);
        Animal animalProxy = (Animal) proxyFactory.getProxy();
        animalProxy.sayHello("二哈", 123);
    }

    public static void testAfterInterceptor()
    {

    }

    public static void main(String args[])
    {
        SpringTest.testBeforeInterceptor();
//        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
//        LifeCycleBean cycleBean = context.getBean("myLifeCycleBean", LifeCycleBean.class);
//        ((ClassPathXmlApplicationContext)context).destroy();
    }
}
