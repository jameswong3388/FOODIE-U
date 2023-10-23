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

public class cusMainController {

    @FXML
    private BorderPane borderpane;
    @FXML
    private ImageView homeIcon;
    @FXML
    private ImageView historyIcon;
    @FXML
    private ImageView reviewIcon;
    @FXML
    private ImageView walletIcon;
    @FXML
    private ImageView profileIcon;
    @FXML
    private ImageView logoutIcon;
    @FXML
    private ImageView notificationIcon;
    @FXML
    private ImageView cartIcon;


    public void initialize() throws IOException {
        // Load an image file and set it to the ImageView
        Image home = new Image(getClass().getResourceAsStream("/images/home-orange.png"));
        homeIcon.setImage(home);
        AnchorPane view = FXMLLoader.load(getClass().getResource("cusHome.fxml"));
        borderpane.setCenter(view);
    }

    public void defaultSettings() {
        // Load an image file and set it to the ImageView
        Image home = new Image(getClass().getResourceAsStream("/images/home-grey.png"));
        homeIcon.setImage(home);
        Image review = new Image(getClass().getResourceAsStream("/images/review-grey.png"));
        reviewIcon.setImage(review);
        Image history = new Image(getClass().getResourceAsStream("/images/history-grey.png"));
        historyIcon.setImage(history);
        Image wallet = new Image(getClass().getResourceAsStream("/images/wallet-grey.png"));
        walletIcon.setImage(wallet);
        Image profile = new Image(getClass().getResourceAsStream("/images/profile-grey.png"));
        profileIcon.setImage(profile);
        Image logout = new Image(getClass().getResourceAsStream("/images/logout-grey.png"));
        logoutIcon.setImage(logout);
        Image notification = new Image(getClass().getResourceAsStream("/images/notification-grey.png"));
        notificationIcon.setImage(notification);
        Image cart = new Image(getClass().getResourceAsStream("/images/cart-grey.png"));
        cartIcon.setImage(cart);
    }

    public void btnHomeClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("cusHome.fxml"));
        borderpane.setCenter(view);
        defaultSettings();
        Image home = new Image(getClass().getResourceAsStream("/images/home-orange.png"));
        homeIcon.setImage(home);
    }

    public void btnHistoryClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("cusHistory.fxml"));
        borderpane.setCenter(view);
        defaultSettings();
        Image history = new Image(getClass().getResourceAsStream("/images/history-orange.png"));
        historyIcon.setImage(history);
    }

    public void btnReviewClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("cusReview.fxml"));
        borderpane.setCenter(view);
        defaultSettings();
        Image review = new Image(getClass().getResourceAsStream("/images/review-orange.png"));
        reviewIcon.setImage(review);
    }

    public void btnWalletClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("cusWallet.fxml"));
        borderpane.setCenter(view);
        defaultSettings();
        Image wallet = new Image(getClass().getResourceAsStream("/images/wallet-orange.png"));
        walletIcon.setImage(wallet);
    }

    public void btnProfileClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("cusProfile.fxml"));
        borderpane.setCenter(view);
        defaultSettings();
        Image profile = new Image(getClass().getResourceAsStream("/images/profile-orange.png"));
        profileIcon.setImage(profile);
    }

    public void btnLogoutClicked(ActionEvent event) throws IOException {
        defaultSettings();
        Image logout = new Image(getClass().getResourceAsStream("/images/logout-orange.png"));
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
            loginStage.setTitle("Customer Main Page");
            loginStage.setScene(new Scene(loginRoot));
            loginStage.show();
            cusStage.close();
        }
    }
    public void btnNotificationClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("cusNotification.fxml"));
        borderpane.setCenter(view);
        defaultSettings();
        Image notification = new Image(getClass().getResourceAsStream("/images/notification-orange.png"));
        notificationIcon.setImage(notification);
    }

    public void btnCartClicked(ActionEvent event) throws IOException {
        //AnchorPane view = FXMLLoader.load(getClass().getResource("cusNotification.fxml"));
        //borderpane.setCenter(view);
        defaultSettings();
        Image cart = new Image(getClass().getResourceAsStream("/images/cart-orange.png"));
        cartIcon.setImage(cart);
    }

    public void btnProfileCliked(ActionEvent event) throws IOException  {
        AnchorPane view = FXMLLoader.load(getClass().getResource("cusProfile.fxml"));
        borderpane.setCenter(view);
        defaultSettings();
    }

}
