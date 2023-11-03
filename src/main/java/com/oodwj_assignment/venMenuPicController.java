package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Medias;
import com.oodwj_assignment.models.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class venMenuPicController {

    @FXML private Label pathLabel;
    @FXML private Button backButton;
    private File selectedFile;
    private venMenuController venMenuController;

    public void initialize(){
        pathLabel.setText("");
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
        selectedFile = null;
        pathLabel.setText("");
    }

    public void uploadButtonClicked(ActionEvent event) throws IOException {
        if (selectedFile != null) {
            String extension = DaoFactory.getMediaDao().getExtensionByStringHandling(selectedFile.getName());
            UUID userId = venMainController.vendorId;

            Users profile = DaoFactory.getUserDao().read(Map.of("Id", userId)).getData().get(0);
            Medias media = new Medias(null, null, profile.getId(), "food_pics", selectedFile.getName(),
                    extension, "menus", null, null, null, LocalDateTime.now(), LocalDateTime.now()
            );

            //remove existed user profile pic
            Response<Void> removeResponse = DaoFactory.getUserDao().removeMedia(userId);
            if (removeResponse.isSuccess()) {
                Response<Void> addResponse = DaoFactory.getUserDao().addMedia(selectedFile, media);
                pathLabel.setText("Uploaded");
                venMainController.showAlert("Success", "New profile added");

                venMenuController.setVenMenuController(venMenuController);
                venMenuController.initialize();

                Stage profileStage = (Stage) backButton.getScene().getWindow();
                profileStage.close();
            }
        }
    }

    public void setVenMenuController(com.oodwj_assignment.venMenuController controller) {
        this.venMenuController = controller;
    }
}
