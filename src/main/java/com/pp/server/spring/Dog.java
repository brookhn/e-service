package com.pp.server.spring;

public class Dog implements Animal{
    private String name;
    private int age;

    public Dog()
    {

    }

    public Dog(String name, int age)
    {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public void sayHello(String userName, int age) {
        System.out.println("dog sayHello"+name+" "+age+"= say hello ====");
    }

    @Override
    public void sayException(String userName, int age) {
        System.out.println("dog sayException"+name+" "+age+"= say hello ====");
    }
}
