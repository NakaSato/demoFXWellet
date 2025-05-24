package com.wellet.fxwellet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        
        // Set window properties
        stage.setTitle("Wellet - Person Management System");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        
        // Center the window on screen
        stage.centerOnScreen();
        
        // Make window resizable but set reasonable bounds
        stage.setMaxWidth(1600);
        stage.setMaxHeight(1200);
        
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}