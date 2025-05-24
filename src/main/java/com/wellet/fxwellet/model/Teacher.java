package com.wellet.fxwellet.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Teacher class demonstrating inheritance and composition
 */
public class Teacher extends Person {
    private String employeeId;
    private String department;
    private double salary;
    private List<String> subjects;
    
    public Teacher(String name, int age, String email, String employeeId, String department, double salary) {
        super(name, age, email);
        this.employeeId = employeeId;
        this.department = department;
        this.salary = salary;
        this.subjects = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public double getSalary() {
        return salary;
    }
    
    public void setSalary(double salary) {
        if (salary >= 0) {
            this.salary = salary;
        }
    }
    
    public List<String> getSubjects() {
        return new ArrayList<>(subjects); // Return copy for encapsulation
    }
    
    public void addSubject(String subject) {
        if (!subjects.contains(subject)) {
            subjects.add(subject);
        }
    }
    
    public void removeSubject(String subject) {
        subjects.remove(subject);
    }
    
    // Implement abstract methods
    @Override
    public String getRole() {
        return "Teacher";
    }
    
    @Override
    public String getDetails() {
        return String.format("Employee ID: %s, Department: %s, Salary: $%.2f, Subjects: %s", 
                           employeeId, department, salary, subjects);
    }
    
    // Additional methods
    public int getSubjectCount() {
        return subjects.size();
    }
    
    public String getExperienceLevel() {
        if (subjects.size() >= 5) return "Senior Teacher";
        else if (subjects.size() >= 3) return "Experienced Teacher";
        else if (subjects.size() >= 1) return "Junior Teacher";
        else return "New Teacher";
    }
}
