package com.wellet.fxwellet.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_NAME = "wellet.db";
    private static DatabaseManager instance;
    private Connection connection;
    private String dbPath;

    private DatabaseManager() {
        // Set database path to user's home directory
        this.dbPath = getDatabasePath();
        try {
            String dbUrl = "jdbc:sqlite:" + dbPath;
            connection = DriverManager.getConnection(dbUrl);
            createTables();
            System.out.println("Database connected at: " + dbPath);
        } catch (SQLException e) {
            System.err.println("Error connecting to SQLite database: " + e.getMessage());
        }
    }

    private String getDatabasePath() {
        // Store in current project directory
        String currentDir = System.getProperty("user.dir");
        
        return currentDir + File.separator + DB_NAME;
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getDbPath() {
        return dbPath;
    }

    private void createTables() {
        String createPersonTable = """
            CREATE TABLE IF NOT EXISTS person (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                age INTEGER NOT NULL,
                email TEXT NOT NULL UNIQUE,
                person_type TEXT NOT NULL,
                student_id TEXT,
                major TEXT,
                gpa REAL,
                teacher_id TEXT,
                department TEXT,
                salary REAL
            )
        """;

        String createSubjectsTable = """
            CREATE TABLE IF NOT EXISTS teacher_subjects (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                teacher_id TEXT NOT NULL,
                subject TEXT NOT NULL,
                FOREIGN KEY (teacher_id) REFERENCES person(teacher_id)
            )
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createPersonTable);
            stmt.execute(createSubjectsTable);
            System.out.println("Tables created successfully");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
