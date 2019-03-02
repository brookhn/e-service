package com.pp.server.spi;

import javassist.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ServiceLoader;

public class JavaSpiTest {

    static void testJavaSPI()
    {
        ServiceLoader<Robot> serviceLoader = ServiceLoader.load(Robot.class);
        System.out.println("JAVA SPI");
        serviceLoader.forEach(Robot::sayHello);
    }

    static void testReflect()
    {
        try {
            Class<?> targetClass =  Class.forName("com.pp.server.spi.BumbleeRobot");
            AnnoationTest annoationTest = targetClass.getAnnotation(AnnoationTest.class);
            System.out.println("annoationTest:"+annoationTest.value());
            Method[] methods = targetClass.getMethods();
            for (Method method: methods)
            {
//                method.getAnnotation()
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void testJavasist()
    {
        try {
            CtClass bumbleeRobot = ClassPool.getDefault().get("com.pp.server.spi.BumbleeRobot");
            CtMethod method = bumbleeRobot.getDeclaredMethod("sayHello");
            method.insertBefore("{System.out.println(\"god bles\"); }");
            bumbleeRobot.writeFile();
            Class bumbleeRobotClass = bumbleeRobot.toClass();
            Method sayHello = bumbleeRobotClass.getMethod("sayHello");
            Constructor<?> con = bumbleeRobotClass.getConstructor();
            sayHello.invoke(con.newInstance());
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException ce) {
            ce.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    static void  testJavasistForConstructor()
    {

    }

    public static void main(String args[])
    {
        JavaSpiTest.testJavasist();
//
//        JavaSpiTest.testReflect();
//        JavaSpiTest.testJavaSPI();
    }
}
