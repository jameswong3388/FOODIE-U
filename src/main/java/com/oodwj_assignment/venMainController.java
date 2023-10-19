package com.oodwj_assignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class venMainController {

    @FXML
    private BorderPane borderpane;
    @FXML
    private ImageView homeIcon;
    @FXML
    private ImageView menuIcon;
    @FXML
    private ImageView statisticIcon;
    @FXML
    private ImageView reviewIcon;
    @FXML
    private ImageView profileIcon;
    @FXML
    private ImageView logoutIcon;

    public void initialize() throws IOException {
        // Load an image file and set it to the ImageView
        Image home = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/home-orange.png")));
        homeIcon.setImage(home);
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("venHome.fxml")));
        borderpane.setCenter(view);
    }

    public void defaultSettings() {
        // Load an image file and set it to the ImageView
        Image home = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/home-grey.png")));
        homeIcon.setImage(home);
        Image menu = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/menu-grey.png")));
        menuIcon.setImage(menu);
        Image statistic = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/statistic-grey.png")));
        statisticIcon.setImage(statistic);
        Image review = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/review-grey.png")));
        reviewIcon.setImage(review);
        Image profile = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/profile-grey.png")));
        profileIcon.setImage(profile);
        Image logout = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logout-grey.png")));
        logoutIcon.setImage(logout);
    }

    public void btnHomeClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("venHome.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image home = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/home-orange.png")));
        homeIcon.setImage(home);
    }

    public void btnMenuClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("venMenu.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image product = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/menu-orange.png")));
        menuIcon.setImage(product);
    }

    public void btnStatisticClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("venStatistic.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image wallet = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/statistic-orange.png")));
        statisticIcon.setImage(wallet);
    }

    public void btnReviewClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("venReview.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image review = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/review-orange.png")));
        reviewIcon.setImage(review);
    }

    public void btnProfileClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("venProfile.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image profile = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/profile-orange.png")));
        profileIcon.setImage(profile);
    }

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
    public void btnNotificationClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("venNotification.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
    }


}
