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

public class admProfilePicController {

    @FXML private Label pathLabel;
    @FXML private Button backButton;
    private File selectedFile;
    private admProfileController admProfileController;

    public void initialize(){
        pathLabel.setText("");
    }

    public void backButtonClicked(ActionEvent event) {
        Stage profileStage = (Stage) backButton.getScene().getWindow();
        profileStage.close();
    }

    public void browseButtonClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image File");

        // Set the file extension filters to limit selection to image files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile == null){
            return;
        }

        if (!isImageFile(selectedFile.getName())) {
            runMainController.showAlert("Upload Error", "Invalid image file format");
            return;
        }
        pathLabel.setText(selectedFile.getAbsolutePath() + " selected");
    }

    private boolean isImageFile(String filename) {
        String extension = filename.toLowerCase();
        return extension.endsWith(".png") || extension.endsWith(".jpg")
                || extension.endsWith(".jpeg") || extension.endsWith(".gif") || extension.endsWith(".bmp");
    }

    public void cancelButtonClicked(ActionEvent event) {
        selectedFile = null;
        pathLabel.setText("");
    }

    public void uploadButtonClicked(ActionEvent event) throws IOException {
        if (selectedFile != null) {
            String extension = DaoFactory.getMediaDao().getExtensionByStringHandling(selectedFile.getName());
            UUID userId = admMainController.adminId;

            Users profile = DaoFactory.getUserDao().read(Map.of("Id", userId)).getData().get(0);
            Medias media = new Medias(null, null, profile.getId(), "profile_pics", selectedFile.getName(),
                    extension, "profiles", null, null, null, LocalDateTime.now(), LocalDateTime.now()
            );

            //for the initial upload when there is no existing data
            Response<Void> addResponse = DaoFactory.getUserDao().addMedia(selectedFile, media);
            if (addResponse.isSuccess()){
                //remove existed user profile pic
                Response<Void> removeResponse = DaoFactory.getUserDao().removeMedia(userId);
                if (removeResponse.isSuccess()) {
                    DaoFactory.getUserDao().addMedia(selectedFile, media);
                    pathLabel.setText("Uploaded");
                    admMainController.showAlert("Success", "New profile added");

                    admProfileController.setAdmProfileController(admProfileController);
                    admProfileController.loadProfilePic();

                    Stage profileStage = (Stage) backButton.getScene().getWindow();
                    profileStage.close();
                }
            }
        }
    }

    public void setAdmProfileController(admProfileController controller) {
        this.admProfileController = controller;
    }
}
