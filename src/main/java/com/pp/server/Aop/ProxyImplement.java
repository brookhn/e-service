package com.pp.server.Aop;

public class ProxyImplement implements InterfaceA {
    private InterfaceA interfaceA;

    public ProxyImplement()
    {
        interfaceA = new RealImplement();
    }

    @Override
    public void exec() {
        System.out.println("begin Do something");
        interfaceA.exec();
        System.out.println("end Do something");
    }

    @Override
    public void select() {

    }
}
