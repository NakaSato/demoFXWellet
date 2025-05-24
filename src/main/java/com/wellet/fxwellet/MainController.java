package com.wellet.fxwellet;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

import java.net.URL;
import java.util.ResourceBundle;

import com.wellet.fxwellet.model.Person;
import com.wellet.fxwellet.model.Student;
import com.wellet.fxwellet.model.Teacher;
import com.wellet.fxwellet.service.PersonManager;

public class MainController implements Initializable {
    @FXML private ListView<Person> personListView;
    @FXML private TextArea detailsArea;
    @FXML private Label statsLabel;
    
    // Form fields for adding new people
    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> roleComboBox;
    
    // Student specific fields
    @FXML private TextField studentIdField;
    @FXML private TextField majorField;
    @FXML private TextField gpaField;
    
    // Teacher specific fields
    @FXML private TextField employeeIdField;
    @FXML private TextField departmentField;
    @FXML private TextField salaryField;
    @FXML private TextField subjectField;
    
    // Filter buttons
    @FXML private RadioButton allRadio;
    @FXML private RadioButton studentsRadio;
    @FXML private RadioButton teachersRadio;
    
    private PersonManager personManager;
    private ToggleGroup filterGroup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        personManager = new PersonManager();
        
        // Initialize filter radio buttons
        filterGroup = new ToggleGroup();
        allRadio.setToggleGroup(filterGroup);
        studentsRadio.setToggleGroup(filterGroup);
        teachersRadio.setToggleGroup(filterGroup);
        allRadio.setSelected(true);
        
        // Setup role combo box
        roleComboBox.setItems(FXCollections.observableArrayList("Student", "Teacher"));
        roleComboBox.setValue("Student");
        
        // Setup listeners
        setupListeners();
        
        // Load initial data
        refreshPersonList();
        updateStats();
    }
    
    private void setupListeners() {
        // Person selection listener
        personListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    showPersonDetails(newValue);
                }
            }
        );
        
        // Filter radio button listeners
        filterGroup.selectedToggleProperty().addListener(
            (observable, oldValue, newValue) -> refreshPersonList()
        );
        
        // Role combo box listener
        roleComboBox.setOnAction(e -> toggleFormFields());
    }
    
    private void refreshPersonList() {
        if (allRadio.isSelected()) {
            personListView.setItems(personManager.getPeople());
        } else if (studentsRadio.isSelected()) {
            personListView.setItems(personManager.getStudents());
        } else if (teachersRadio.isSelected()) {
            personListView.setItems(personManager.getTeachers());
        }
    }
    
    private void showPersonDetails(Person person) {
        StringBuilder details = new StringBuilder();
        details.append("=== PERSON DETAILS ===\n\n");
        details.append("Basic Information:\n");
        details.append(person.getBasicInfo()).append("\n\n");
        details.append("Role: ").append(person.getRole()).append("\n\n");
        details.append("Specific Details:\n");
        details.append(person.getDetails()).append("\n\n");
        
        // Demonstrate polymorphism
        if (person instanceof Student) {
            Student student = (Student) person;
            details.append("Academic Status: ").append(student.getGradeLevel()).append("\n");
        } else if (person instanceof Teacher) {
            Teacher teacher = (Teacher) person;
            details.append("Experience Level: ").append(teacher.getExperienceLevel()).append("\n");
            details.append("Number of Subjects: ").append(teacher.getSubjectCount()).append("\n");
        }
        
        detailsArea.setText(details.toString());
    }
    
    private void updateStats() {
        StringBuilder stats = new StringBuilder();
        stats.append(String.format("Total People: %d | ", personManager.getPersonCount()));
        stats.append(String.format("Students: %d | ", personManager.getStudentCount()));
        stats.append(String.format("Teachers: %d\n", personManager.getTeacherCount()));
        stats.append(String.format("Average GPA: %.2f | ", personManager.getAverageGPA()));
        stats.append(String.format("Average Salary: $%.2f", personManager.getAverageSalary()));
        
        statsLabel.setText(stats.toString());
    }
    
    @FXML
    private void onAddPersonClick() {
        try {
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String email = emailField.getText().trim();
            String role = roleComboBox.getValue();
            
            if (name.isEmpty() || email.isEmpty()) {
                showAlert("Error", "Name and email are required!");
                return;
            }
            
            Person newPerson;
            
            if ("Student".equals(role)) {
                String studentId = studentIdField.getText().trim();
                String major = majorField.getText().trim();
                double gpa = Double.parseDouble(gpaField.getText().trim());
                
                newPerson = new Student(name, age, email, studentId, major, gpa);
            } else {
                String employeeId = employeeIdField.getText().trim();
                String department = departmentField.getText().trim();
                double salary = Double.parseDouble(salaryField.getText().trim());
                
                Teacher teacher = new Teacher(name, age, email, employeeId, department, salary);
                String subject = subjectField.getText().trim();
                if (!subject.isEmpty()) {
                    teacher.addSubject(subject);
                }
                newPerson = teacher;
            }
            
            personManager.addPerson(newPerson);
            refreshPersonList();
            updateStats();
            clearForm();
            
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers for age, GPA, and salary!");
        }
    }
    
    @FXML
    private void onDeletePersonClick() {
        Person selectedPerson = personListView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            personManager.removePerson(selectedPerson);
            refreshPersonList();
            updateStats();
            detailsArea.clear();
        } else {
            showAlert("Warning", "Please select a person to delete!");
        }
    }
    
    @FXML
    private void onClearFormClick() {
        clearForm();
    }
    
    private void toggleFormFields() {
        boolean isStudent = "Student".equals(roleComboBox.getValue());
        
        // Student fields
        studentIdField.setVisible(isStudent);
        majorField.setVisible(isStudent);
        gpaField.setVisible(isStudent);
        
        // Teacher fields
        employeeIdField.setVisible(!isStudent);
        departmentField.setVisible(!isStudent);
        salaryField.setVisible(!isStudent);
        subjectField.setVisible(!isStudent);
    }
    
    private void clearForm() {
        nameField.clear();
        ageField.clear();
        emailField.clear();
        studentIdField.clear();
        majorField.clear();
        gpaField.clear();
        employeeIdField.clear();
        departmentField.clear();
        salaryField.clear();
        subjectField.clear();
        roleComboBox.setValue("Student");
        toggleFormFields();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}