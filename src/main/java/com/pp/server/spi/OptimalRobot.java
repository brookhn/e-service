package com.pp.server.spi;

public class OptimalRobot implements Robot {
    @Override
    @AnnoationTest(value = "sayHello")
    public void sayHello() {
        System.out.println("OptimalRobot Exception");
    }
}
