package com.pp.server.spring;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class MyMethodBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] objects, Object target) throws Throwable {
        System.out.println("前置增强" + method.getName());
        if (null != objects && objects.length > 0)
        {
            for (int i = 0; i< objects.length; i++)
            {
               System.out.println("第"+i+"参数为"+objects[i]);
            }
        }
        System.out.println("target class is " + target.toString());
    }
}
