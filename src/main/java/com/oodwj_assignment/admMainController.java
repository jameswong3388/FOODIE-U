package com.oodwj_assignment;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class admMainController {
        @FXML
        private BorderPane borderpane;
        @FXML
        private ImageView homeIcon;
        @FXML
        private ImageView logoutIcon;
        @FXML
        private ImageView notificationIcon;

        public void initialize() throws IOException {
            Image home = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/home-orange.png")));
            homeIcon.setImage(home);
            AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admHome.fxml")));
            borderpane.setCenter(view);
        }

        public void defaultSettings() {
            // Load an image file and set it to the ImageView
            Image home = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/home-grey.png")));
            homeIcon.setImage(home);
            Image logout = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logout-grey.png")));
            logoutIcon.setImage(logout);
            Image notification= new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/notification-grey.png")));
            notificationIcon.setImage(notification);
        }


        @FXML
        public void btnHomeClicked(ActionEvent event) throws IOException {
            AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admHome.fxml")));
            borderpane.setCenter(view);
            defaultSettings();
            Image home = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/home-orange.png")));
            homeIcon.setImage(home);
        }

        @FXML
        public void btnLogoutClicked(ActionEvent event) throws IOException {
            defaultSettings();
            Image logout = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logout-orange.png")));
            logoutIcon.setImage(logout);

            Stage cusStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Alert.AlertType type = Alert.AlertType.CONFIRMATION;
            Alert alert = new Alert(type, "");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(cusStage);
            alert.getDialogPane().setHeaderText("Are you sure you want to logout?");

            // Customize the button text
            ButtonType logoutButton = new ButtonType("Logout", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(logoutButton, cancelButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == logoutButton) {
                Parent loginRoot = FXMLLoader.load(getClass().getResource("login.fxml"));
                Stage loginStage = new Stage();
                loginStage.setTitle("Login Page");
                loginStage.setScene(new Scene(loginRoot));
                loginStage.show();

                cusStage.close();
            }
        }

        @FXML
        public void btnNotificationClicked(ActionEvent event) throws IOException {
            //AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("runNotification.fxml")));
            //borderpane.setCenter(view);
            defaultSettings();
            Image notification= new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/notification-orange.png")));
            notificationIcon.setImage(notification);
        }
        @FXML
        public void btnUserRegisterClicked(ActionEvent event) throws IOException {
            AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admApprover.fxml")));
            borderpane.setCenter(view);
            defaultSettings();
        }
        @FXML
        public void btnCreditClicked(ActionEvent event) throws IOException {
            AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admCredit.fxml")));
            borderpane.setCenter(view);
            defaultSettings();
        }
}
