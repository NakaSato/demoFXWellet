package com.wellet.fxwellet.model;

/**
 * Base Person class demonstrating encapsulation and abstraction
 */
public abstract class Person {
    private String name;
    private int age;
    private String email;
    
    // Constructor
    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
    
    // Getters and Setters (Encapsulation)
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
        if (age >= 0) {
            this.age = age;
        }
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    // Abstract method (Abstraction)
    public abstract String getRole();
    public abstract String getDetails();
    
    // Common method
    public String getBasicInfo() {
        return String.format("Name: %s, Age: %d, Email: %s", name, age, email);
    }
    
    @Override
    public String toString() {
        return getBasicInfo() + " - " + getRole();
    }
}
