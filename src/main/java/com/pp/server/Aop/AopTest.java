package com.pp.server.Aop;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

public class AopTest {
    public static void main(String args[])
    {
        /***************************************/
//        InterfaceA interfaceA = new ProxyImplement();
//        interfaceA.exec();
        /*********************cglib test*******************/
        RealProxy realProxy = new RealProxy();
        AnotherRealProxy anotherRealProxy = new AnotherRealProxy();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(RealImplement.class);
        //enhancer.setCallback(anotherRealProxy);
        /*****************************************************
         * 带Filter过滤
         ****************************************************/
         enhancer.setCallbacks(new Callback[]{realProxy, anotherRealProxy, NoOp.INSTANCE});
         enhancer.setCallbackFilter(new ProxyFilter());
         //enhancer.setInterceptDuringConstruction(false);
        /*********************************************
         * 具体调用方法
         */
        RealImplement realImplement = (RealImplement) enhancer.create();
        realImplement.exec();
        realImplement.select();
    }
}
