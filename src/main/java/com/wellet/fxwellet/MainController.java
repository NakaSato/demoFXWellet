package com.wellet.fxwellet;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

import java.net.URL;
import java.util.Optional;
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
        details.append("╔══════════════════════════════════════════════════════════════╗\n");
        details.append("║                      PERSON DETAILS                         ║\n");
        details.append("╚══════════════════════════════════════════════════════════════╝\n\n");
        
        details.append("📋 Basic Information:\n");
        details.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        details.append(person.getBasicInfo()).append("\n\n");
        
        details.append("👤 Role: ").append(person.getRole()).append("\n\n");
        
        details.append("📊 Specific Details:\n");
        details.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        details.append(person.getDetails()).append("\n\n");
        
        // Demonstrate polymorphism with enhanced formatting
        if (person instanceof Student) {
            Student student = (Student) person;
            details.append("🎓 Academic Status: ").append(student.getGradeLevel()).append("\n");
            details.append("📈 Performance Level: ");
            double gpa = student.getGpa();
            if (gpa >= 3.7) {
                details.append("Excellent (").append(gpa).append("/4.0)");
            } else if (gpa >= 3.0) {
                details.append("Good (").append(gpa).append("/4.0)");
            } else if (gpa >= 2.0) {
                details.append("Satisfactory (").append(gpa).append("/4.0)");
            } else {
                details.append("Needs Improvement (").append(gpa).append("/4.0)");
            }
            details.append("\n");
        } else if (person instanceof Teacher) {
            Teacher teacher = (Teacher) person;
            details.append("💼 Experience Level: ").append(teacher.getExperienceLevel()).append("\n");
            details.append("📚 Number of Subjects: ").append(teacher.getSubjectCount()).append("\n");
            details.append("💰 Salary Range: ");
            double salary = teacher.getSalary();
            if (salary >= 80000) {
                details.append("Senior Level");
            } else if (salary >= 60000) {
                details.append("Mid Level");
            } else {
                details.append("Entry Level");
            }
            details.append(" ($").append(String.format("%,.2f", salary)).append(")\n");
        }
        
        detailsArea.setText(details.toString());
    }
    
    private void updateStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("📊 ");
        stats.append(String.format("Total: %d", personManager.getPersonCount()));
        stats.append(" | ");
        stats.append(String.format("🎓 Students: %d", personManager.getStudentCount()));
        stats.append(" | ");
        stats.append(String.format("👨‍🏫 Teachers: %d", personManager.getTeacherCount()));
        
        if (personManager.getStudentCount() > 0 || personManager.getTeacherCount() > 0) {
            stats.append(" | ");
            if (personManager.getStudentCount() > 0) {
                stats.append(String.format("📈 Avg GPA: %.2f", personManager.getAverageGPA()));
            }
            if (personManager.getStudentCount() > 0 && personManager.getTeacherCount() > 0) {
                stats.append(" | ");
            }
            if (personManager.getTeacherCount() > 0) {
                stats.append(String.format("💰 Avg Salary: $%,.0f", personManager.getAverageSalary()));
            }
        }
        
        statsLabel.setText(stats.toString());
    }
    
    @FXML
    private void onAddPersonClick() {
        try {
            String name = nameField.getText().trim();
            String ageText = ageField.getText().trim();
            String email = emailField.getText().trim();
            String role = roleComboBox.getValue();
            
            // Enhanced validation
            if (name.isEmpty()) {
                showAlert("Validation Error", "📝 Name is required!");
                nameField.requestFocus();
                return;
            }
            
            if (ageText.isEmpty()) {
                showAlert("Validation Error", "📅 Age is required!");
                ageField.requestFocus();
                return;
            }
            
            if (email.isEmpty()) {
                showAlert("Validation Error", "📧 Email is required!");
                emailField.requestFocus();
                return;
            }
            
            if (!email.contains("@")) {
                showAlert("Validation Error", "📧 Please enter a valid email address!");
                emailField.requestFocus();
                return;
            }
            
            if (role == null) {
                showAlert("Validation Error", "👤 Please select a role!");
                roleComboBox.requestFocus();
                return;
            }
            
            int age = Integer.parseInt(ageText);
            if (age <= 0 || age > 150) {
                showAlert("Validation Error", "📅 Please enter a valid age (1-150)!");
                ageField.requestFocus();
                return;
            }
            
            Person newPerson;
            
            if ("Student".equals(role)) {
                String studentId = studentIdField.getText().trim();
                String major = majorField.getText().trim();
                String gpaText = gpaField.getText().trim();
                
                if (studentId.isEmpty() || major.isEmpty() || gpaText.isEmpty()) {
                    showAlert("Validation Error", "🎓 All student fields are required!");
                    return;
                }
                
                double gpa = Double.parseDouble(gpaText);
                if (gpa < 0.0 || gpa > 4.0) {
                    showAlert("Validation Error", "📊 GPA must be between 0.0 and 4.0!");
                    gpaField.requestFocus();
                    return;
                }
                
                newPerson = new Student(name, age, email, studentId, major, gpa);
            } else {
                String employeeId = employeeIdField.getText().trim();
                String department = departmentField.getText().trim();
                String salaryText = salaryField.getText().trim();
                
                if (employeeId.isEmpty() || department.isEmpty() || salaryText.isEmpty()) {
                    showAlert("Validation Error", "👨‍🏫 All teacher fields are required!");
                    return;
                }
                
                double salary = Double.parseDouble(salaryText);
                if (salary < 0) {
                    showAlert("Validation Error", "💰 Salary must be a positive number!");
                    salaryField.requestFocus();
                    return;
                }
                
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
            
            // Show success message
            showSuccessAlert("Success", "✅ " + role + " '" + name + "' has been added successfully!");
            
        } catch (NumberFormatException e) {
            showAlert("Input Error", "🔢 Please enter valid numbers for numeric fields!");
        } catch (Exception e) {
            showAlert("Error", "❌ An unexpected error occurred: " + e.getMessage());
        }
    }
    
    @FXML
    private void onDeletePersonClick() {
        Person selectedPerson = personListView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            // Show confirmation dialog
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Deletion");
            confirmAlert.setHeaderText("Delete Person");
            confirmAlert.setContentText("Are you sure you want to delete '" + selectedPerson.getName() + "'?\n\nThis action cannot be undone.");
            
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                personManager.removePerson(selectedPerson);
                refreshPersonList();
                updateStats();
                detailsArea.clear();
                showSuccessAlert("Success", "🗑️ '" + selectedPerson.getName() + "' has been deleted successfully!");
            }
        } else {
            showAlert("Warning", "⚠️ Please select a person to delete!");
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
    
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}