package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class admMainController {

    @FXML private BorderPane borderpane;
    @FXML private ImageView homeIcon;
    @FXML private ImageView registerIcon;
    @FXML private ImageView creditIcon;
    @FXML private ImageView profileIcon;
    @FXML private ImageView logoutIcon;
    @FXML private Label nameLabel;
    @FXML private Circle profilePic;

    public static UUID adminId;
    private admMainController mainController;

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
        Image register = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/register-grey.png")));
        registerIcon.setImage(register);
        Image credit = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/wallet-grey.png")));
        creditIcon.setImage(credit);
        Image profile = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/profile-grey.png")));
        profileIcon.setImage(profile);
        Image logout = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logout-grey.png")));
        logoutIcon.setImage(logout);
    }

    public void btnHomeClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admHome.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image home = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/home-orange.png")));
        homeIcon.setImage(home);
    }

    public void btnRegisterClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admRegister.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image register = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/register-orange.png")));
        registerIcon.setImage(register);
    }

    public void btnCreditClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admCredit.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image credit = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/wallet-orange.png")));
        creditIcon.setImage(credit);
    }

    public void btnProfileClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admProfile.fxml"));
        AnchorPane view = fxmlLoader.load();
        admProfileController profileController = fxmlLoader.getController();
        profileController.setAdmMainController(this);
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

    public static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void loadData(){
        String name = DaoFactory.getUserDao().read(Map.of("Id", adminId)).getData().get(0).getName();
        nameLabel.setText(name);

        Response<String> mediaResponse = DaoFactory.getUserDao().getFirstMedia(adminId);
        if (mediaResponse.isSuccess()){
            String imagePath = mediaResponse.getData();
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                profilePic.setFill(new ImagePattern(image));
            }
        } else {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/sample-profile.png")));
            profilePic.setFill(new ImagePattern(image));
        }
    }

    public void setAdmMainController(admMainController controller) { mainController = controller; }

    public void setUserId(UUID userId) {
        adminId = userId;
        loadData();
    }
}
