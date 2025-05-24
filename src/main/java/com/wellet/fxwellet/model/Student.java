package com.wellet.fxwellet.model;

/**
 * Student class demonstrating inheritance and polymorphism
 */
public class Student extends Person {
    private String studentId;
    private String major;
    private double gpa;
    
    public Student(String name, int age, String email, String studentId, String major, double gpa) {
        super(name, age, email); // Call parent constructor
        this.studentId = studentId;
        this.major = major;
        this.gpa = gpa;
    }
    
    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getMajor() {
        return major;
    }
    
    public void setMajor(String major) {
        this.major = major;
    }
    
    public double getGpa() {
        return gpa;
    }
    
    public void setGpa(double gpa) {
        if (gpa >= 0.0 && gpa <= 4.0) {
            this.gpa = gpa;
        }
    }
    
    // Implement abstract methods (Polymorphism)
    @Override
    public String getRole() {
        return "Student";
    }
    
    @Override
    public String getDetails() {
        return String.format("Student ID: %s, Major: %s, GPA: %.2f", studentId, major, gpa);
    }
    
    // Additional method specific to Student
    public String getGradeLevel() {
        if (gpa >= 3.5) return "Honor Student";
        else if (gpa >= 3.0) return "Good Standing";
        else if (gpa >= 2.0) return "Satisfactory";
        else return "Needs Improvement";
    }
}
