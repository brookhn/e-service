package com.pp.server.Aop;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

public class ProxyFilter implements CallbackFilter {
    @Override
    public int accept(Method method) {
        if ("select".equals(method.getName()))
        {
            return 0;
        }
        return 1;
    }
}
