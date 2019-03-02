package com.pp.server.spi;

@AnnoationTest(value = "bumbleeRobot")
public class BumbleeRobot implements Robot {
    @Override
    public void sayHello() {
        System.out.println("BumbleeRobot hello");
    }
}
