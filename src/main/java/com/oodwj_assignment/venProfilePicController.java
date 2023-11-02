package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Medias;
import com.oodwj_assignment.models.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class venProfilePicController {

    @FXML private Label pathLabel;
    @FXML private Button backButton;
    @FXML private ImageView test;
    private File selectedFile;

    public void initialize(){
        pathLabel.setText("");
        //Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("")));
        //test.setImage(image);
    }

    public void backButtonClicked(ActionEvent event) {
        Stage profileStage = (Stage) backButton.getScene().getWindow();
        profileStage.close();
    }

    public void browseButtonClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");
        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            pathLabel.setText(selectedFile.getAbsolutePath() + " selected");
        }
    }

    public void cancelButtonClicked(ActionEvent event) {

    }

    public void uploadButtonClicked(ActionEvent event) {
        if (selectedFile != null) {
            String extension = DaoFactory.getMediaDao().getExtensionByStringHandling(selectedFile.getName());
            UUID userId = venMainController.vendorId;

            // Upload under user model (replace 'james' with the actual username)
            Users profile = DaoFactory.getUserDao().read(Map.of("Id", userId)).getData().get(0);
            Medias media = new Medias(
                    null,
                    null,
                    profile.getId(),
                    "default",
                    selectedFile.getName(),
                    extension,
                    "local",
                    null,
                    null,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            Response<Void> res = DaoFactory.getUserDao().addMedia(selectedFile, media);
            System.out.println(res.getMessage());
            System.out.println(res.getData());

            // Retrieve
            Response<String> res2 = DaoFactory.getUserDao().getFirstMedia(UUID.fromString(profile.getId().toString()));
            System.out.println(res2.getMessage());
            System.out.println(res2.getData());

            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(res2.getData())));
            test.setImage(image);
            pathLabel.setText("Uploaded");
        }
    }
}
