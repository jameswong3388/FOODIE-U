package com.oodwj_assignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class loginController {

    @FXML
    private  Button loginButton;
    @FXML
    private ImageView eyeslash;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField textField;

    private boolean isImageHidden = true;

    public void loginButtonAction(ActionEvent event) throws IOException {
        Parent customerRoot = FXMLLoader.load(getClass().getResource("cusMain.fxml"));
        Stage customerStage = new Stage();
        customerStage.setTitle("Customer Page");
        customerStage.setScene(new Scene(customerRoot));
        customerStage.show();

        // Close the login stage (optional)
        Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loginStage.close();
    }

    private void passwordVisibility() {
        if (isImageHidden) {
            textField.setText(passwordField.getText());
            textField.setVisible(true);
            passwordField.setVisible(false);
            Image showImage = new Image(getClass().getResourceAsStream("/images/show.png"));
            eyeslash.setImage(showImage);
        } else {
            passwordField.setText(textField.getText());
            passwordField.setVisible(true);
            textField.setVisible(false);
            Image hideImage = new Image(getClass().getResourceAsStream("/images/hide.png"));
            eyeslash.setImage(hideImage);
        }

        // Toggle the image state
        isImageHidden = !isImageHidden;
    }

    public void eyeslashClicked() {
        passwordVisibility();
    }
}
