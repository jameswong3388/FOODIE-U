package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Medias;
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
import java.time.LocalDateTime;

import java.util.Map;
import java.util.UUID;

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
                            String extension = DaoFactory.getMediaDao().getExtensionByStringHandling(file.getName());
                            // Upload under user model
                            Users jamesProfile = DaoFactory.getUserDao().read(Map.of("username", "james")).getData().get(0);
                            Medias media = new Medias(null, null, jamesProfile.getId(), "default", file.getName(), extension, "local", null, null, null, LocalDateTime.now(), LocalDateTime.now());
                            Response<Void> res = DaoFactory.getUserDao().addMedia(file, media);
                            System.out.println(res.getMessage());
                            System.out.println(res.getData());

                            // Retrieve
                            Response<String> res2 = DaoFactory.getUserDao().getFirstMedia(UUID.fromString(jamesProfile.getId().toString()));
                            System.out.println(res2.getMessage());
                            System.out.println(res2.getData());

                            // Remove
//                            Response<Void> res3 = DaoFactory.getUserDao().removeMedia(UUID.fromString(jamesProfile.getId().toString()));
//                            System.out.println(res3.getMessage());

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
