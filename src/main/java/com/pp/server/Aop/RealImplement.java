package com.pp.server.Aop;

public class RealImplement implements InterfaceA {
    public RealImplement()
    {
        System.out.println("---------------RealImplement----------------");
        exec();
    }

    @Override
    public void exec() {
        System.out.println("real Impl");
    }

    @Override
    public void select() {
        System.out.println("real select");
    }
}
