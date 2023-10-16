package com.oodwj_assignment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class main extends Application {
    @Override
    public void start(Stage primarystage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        Scene sc = new Scene(root);
        //primarystage.initStyle(StageStyle.UNDECORATED);
        primarystage.setTitle("Login Page");
        primarystage.setScene(sc);
        primarystage.show();
    }

    public static void main(String[] args) {launch();}
}