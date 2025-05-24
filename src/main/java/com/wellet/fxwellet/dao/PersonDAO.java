package com.wellet.fxwellet.dao;

import com.wellet.fxwellet.database.DatabaseManager;
import com.wellet.fxwellet.model.Person;
import com.wellet.fxwellet.model.Student;
import com.wellet.fxwellet.model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {
    private Connection connection;

    public PersonDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public void savePerson(Person person) {
        String sql = """
            INSERT OR REPLACE INTO person 
            (name, age, email, person_type, student_id, major, gpa, teacher_id, department, salary)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, person.getName());
            pstmt.setInt(2, person.getAge());
            pstmt.setString(3, person.getEmail());

            if (person instanceof Student) {
                Student student = (Student) person;
                pstmt.setString(4, "STUDENT");
                pstmt.setString(5, student.getStudentId());
                pstmt.setString(6, student.getMajor());
                pstmt.setDouble(7, student.getGpa());
                pstmt.setNull(8, Types.VARCHAR);
                pstmt.setNull(9, Types.VARCHAR);
                pstmt.setNull(10, Types.REAL);
            } else if (person instanceof Teacher) {
                Teacher teacher = (Teacher) person;
                pstmt.setString(4, "TEACHER");
                pstmt.setNull(5, Types.VARCHAR);
                pstmt.setNull(6, Types.VARCHAR);
                pstmt.setNull(7, Types.REAL);
                pstmt.setString(8, teacher.getEmployeeId());
                pstmt.setString(9, teacher.getDepartment());
                pstmt.setDouble(10, teacher.getSalary());

                // Save subjects separately
                saveTeacherSubjects(teacher);
            }

            pstmt.executeUpdate();
            System.out.println("Person saved: " + person.getName());
        } catch (SQLException e) {
            System.err.println("Error saving person: " + e.getMessage());
        }
    }

    private void saveTeacherSubjects(Teacher teacher) {
        // First delete existing subjects
        String deleteSql = "DELETE FROM teacher_subjects WHERE teacher_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSql)) {
            pstmt.setString(1, teacher.getEmployeeId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting teacher subjects: " + e.getMessage());
        }

        // Insert new subjects
        String insertSql = "INSERT INTO teacher_subjects (teacher_id, subject) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
            for (String subject : teacher.getSubjects()) {
                pstmt.setString(1, teacher.getEmployeeId());
                pstmt.setString(2, subject);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error saving teacher subjects: " + e.getMessage());
        }
    }

    public List<Person> getAllPeople() {
        List<Person> people = new ArrayList<>();
        String sql = "SELECT * FROM person";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String personType = rs.getString("person_type");
                
                if ("STUDENT".equals(personType)) {
                    Student student = new Student(
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getString("student_id"),
                        rs.getString("major"),
                        rs.getDouble("gpa")
                    );
                    people.add(student);
                } else if ("TEACHER".equals(personType)) {
                    Teacher teacher = new Teacher(
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getString("teacher_id"),
                        rs.getString("department"),
                        rs.getDouble("salary")
                    );
                    
                    // Load subjects
                    loadTeacherSubjects(teacher);
                    people.add(teacher);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading people: " + e.getMessage());
        }

        return people;
    }

    private void loadTeacherSubjects(Teacher teacher) {
        String sql = "SELECT subject FROM teacher_subjects WHERE teacher_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, teacher.getEmployeeId());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                teacher.addSubject(rs.getString("subject"));
            }
        } catch (SQLException e) {
            System.err.println("Error loading teacher subjects: " + e.getMessage());
        }
    }

    public void deletePerson(String email) {
        // First get the person to check if it's a teacher (to delete subjects)
        String selectSql = "SELECT person_type, teacher_id FROM person WHERE email = ?";
        try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
            selectStmt.setString(1, email);
            ResultSet rs = selectStmt.executeQuery();
            
            if (rs.next() && "TEACHER".equals(rs.getString("person_type"))) {
                String teacherId = rs.getString("teacher_id");
                // Delete teacher subjects first
                String deleteSubjectsSql = "DELETE FROM teacher_subjects WHERE teacher_id = ?";
                try (PreparedStatement deleteSubjectsStmt = connection.prepareStatement(deleteSubjectsSql)) {
                    deleteSubjectsStmt.setString(1, teacherId);
                    deleteSubjectsStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking person type for deletion: " + e.getMessage());
        }
        
        // Delete the person
        String deleteSql = "DELETE FROM person WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSql)) {
            pstmt.setString(1, email);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Person deleted: " + email);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting person: " + e.getMessage());
        }
    }
}
