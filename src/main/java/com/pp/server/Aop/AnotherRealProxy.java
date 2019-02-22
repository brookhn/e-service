package com.pp.server.Aop;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;

public class AnotherRealProxy implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("[START TIME:"+ System.currentTimeMillis()+"]");
        methodProxy.invokeSuper(o, objects);
        System.out.println("[END TIME]"+ System.currentTimeMillis()+"]");
        return o;
    }
}
