package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.models.Users;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import javafx.stage.FileChooser;

public class FileChooser_1 extends Application {

    // launch the application
    public void start(Stage stage)
    {

        try {

            // set title for the stage
            stage.setTitle("FileChooser");

            // create a File chooser
            FileChooser fil_chooser = new FileChooser();

            // create a Label
            Label label = new Label("no files selected");

            // create a Button
            Button button = new Button("choose file");

            // create an Event Handler
            EventHandler<ActionEvent> event =
                    new EventHandler<ActionEvent>() {

                        public void handle(ActionEvent e)
                        {

                            // get the file selected
                            File file = fil_chooser.showOpenDialog(stage);
                            fil_chooser.setInitialDirectory(file);

                            if (file != null) {

                                label.setText(file.getAbsolutePath()
                                        + " selected");
                            }
                        }
                    };

            button.setOnAction(event);

            // create a Button
            Button button1 = new Button("upload");

            // create an Event Handler
            EventHandler<ActionEvent> event1 =
                    new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e)
                        {
                            File file = fil_chooser.getInitialDirectory();
                            Users jamesProfile = DaoFactory.getUserDao().read(Map.of("username", "james")).getData().get(0);
                            DaoFactory.getUserDao().addMedia(file, "pdf", jamesProfile.getId());
                            label.setText("uploaded");
                        }
                    };

            button1.setOnAction(event1);

            // create a VBox
            VBox vbox = new VBox(30, label, button, button1);

            // set Alignment
            vbox.setAlignment(Pos.CENTER);

            // create a scene
            Scene scene = new Scene(vbox, 800, 500);

            // set the scene
            stage.setScene(scene);

            stage.show();
        }

        catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    // Main Method
    public static void main(String args[])
    {
        // launch the application
        launch(args);
    }
}
