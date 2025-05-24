package com.wellet.fxwellet.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.stream.Collectors;

import com.wellet.fxwellet.dao.PersonDAO;
import com.wellet.fxwellet.model.Person;
import com.wellet.fxwellet.model.Student;
import com.wellet.fxwellet.model.Teacher;

/**
 * PersonManager class demonstrating composition and data management with SQLite
 */
public class PersonManager {
    private ObservableList<Person> people;
    private PersonDAO personDAO;
    
    public PersonManager() {
        this.personDAO = new PersonDAO();
        this.people = FXCollections.observableArrayList();
        loadDataFromDatabase();
        
        // Initialize sample data only if database is empty
        if (people.isEmpty()) {
            initializeSampleData();
        }
    }
    
    private void loadDataFromDatabase() {
        people.clear();
        people.addAll(personDAO.getAllPeople());
    }
    
    public ObservableList<Person> getPeople() {
        return people;
    }
    
    public void addPerson(Person person) {
        people.add(person);
        personDAO.savePerson(person);
    }
    
    public void removePerson(Person person) {
        people.remove(person);
        personDAO.deletePerson(person.getEmail());
    }
    
    public void updatePerson(Person person) {
        personDAO.savePerson(person);
        // Refresh the list to reflect changes
        loadDataFromDatabase();
    }
    
    public ObservableList<Person> getStudents() {
        return FXCollections.observableList(
            people.stream()
                .filter(p -> p instanceof Student)
                .collect(Collectors.toList())
        );
    }
    
    public ObservableList<Person> getTeachers() {
        return FXCollections.observableList(
            people.stream()
                .filter(p -> p instanceof Teacher)
                .collect(Collectors.toList())
        );
    }
    
    public int getPersonCount() {
        return people.size();
    }
    
    public int getStudentCount() {
        return (int) people.stream().filter(p -> p instanceof Student).count();
    }
    
    public int getTeacherCount() {
        return (int) people.stream().filter(p -> p instanceof Teacher).count();
    }
    
    public double getAverageGPA() {
        return people.stream()
                .filter(p -> p instanceof Student)
                .mapToDouble(p -> ((Student) p).getGpa())
                .average()
                .orElse(0.0);
    }
    
    public double getAverageSalary() {
        return people.stream()
                .filter(p -> p instanceof Teacher)
                .mapToDouble(p -> ((Teacher) p).getSalary())
                .average()
                .orElse(0.0);
    }
    
    private void initializeSampleData() {
        // Add sample students
        Student student1 = new Student("Alice Johnson", 20, "alice@email.com", "S001", "Computer Science", 3.8);
        Student student2 = new Student("Bob Smith", 22, "bob@email.com", "S002", "Mathematics", 3.5);
        Student student3 = new Student("Carol Davis", 19, "carol@email.com", "S003", "Physics", 3.9);
        
        // Add sample teachers
        Teacher teacher1 = new Teacher("Dr. John Wilson", 45, "john.wilson@email.com", "T001", "Computer Science", 75000);
        teacher1.addSubject("Java Programming");
        teacher1.addSubject("Data Structures");
        teacher1.addSubject("Software Engineering");
        
        Teacher teacher2 = new Teacher("Prof. Sarah Brown", 52, "sarah.brown@email.com", "T002", "Mathematics", 80000);
        teacher2.addSubject("Calculus");
        teacher2.addSubject("Linear Algebra");
        teacher2.addSubject("Statistics");
        teacher2.addSubject("Discrete Mathematics");
        
        // Save to database
        addPerson(student1);
        addPerson(student2);
        addPerson(student3);
        addPerson(teacher1);
        addPerson(teacher2);
    }
}
