package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.CustomerProfile;
import com.oodwj_assignment.models.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class cusProfileController {

    @FXML private TextField emailTextField;
    @FXML private TextField cusNameTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private Pane cusInfoPane;
    @FXML private Pane loginInfoPane;
    @FXML private Label phoneNumberFormatLabel;
    @FXML private Label cusNameLabel;
    @FXML private ImageView oldPassEyeIcon;
    @FXML private ImageView newPassEyeIcon;
    @FXML private ImageView confirmPassEyeIcon;
    @FXML private PasswordField oldPassField;
    @FXML private PasswordField newPassField;
    @FXML private PasswordField confirmPassField;
    @FXML private TextField oldPassTextField;
    @FXML private TextField newPassTextField;
    @FXML private TextField confirmPassTextField;
    @FXML private TextField usernameTextField;
    @FXML private ImageView profilePic;
    @FXML private RadioButton maleRadButton;
    @FXML private RadioButton femaleRadButton;
    @FXML private DatePicker dobDatePicker;
    private UUID userId = cusMainController.userId;
    private String currentPassword;
    private boolean isOldHidden;
    private boolean isNewHidden;
    private boolean isConfirmHidden;
    public cusMainController mainController;
    private cusProfileController cusProfileController;

    public void initialize(){
        phoneNumberFormatLabel.setVisible(false);
        cusInfoPane.toFront();
        phoneNumberTextField.setTextFormatter(phoneNumberFormatter());
        // Show the label when the text field gains focus
        phoneNumberTextField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            phoneNumberFormatLabel.setVisible(newVal);
        });
        loadInfo();
    }

    public void loadInfo(){
        loadProfilePic();

        Map<String, Object> userQuery = Map.of("Id", userId);
        Response<ArrayList<Users>> userResponse = DaoFactory.getUserDao().read(userQuery);

        if (userResponse.isSuccess()){
            Users users = userResponse.getData().get(0);
            String name = users.getName();
            String phoneNumber = users.getPhoneNumber();
            String email = users.getEmail();
            String username = users.getUsername();
            currentPassword = users.getPassword();

            cusNameLabel.setText(name);
            cusNameTextField.setText(name);
            phoneNumberTextField.setText(phoneNumber);
            emailTextField.setText(email);
            usernameTextField.setText(username);
        }

        Map<String, Object> profileQuery = Map.of("userId", userId);
        Response<ArrayList<CustomerProfile>> profileResponse = DaoFactory.getCustomerProfileDao().read(profileQuery);

        if (profileResponse.isSuccess()){
            CustomerProfile customerProfile = profileResponse.getData().get(0);
            CustomerProfile.Gender gender = customerProfile.getGender();
            LocalDate dob = customerProfile.getDob();

            if (gender.equals(CustomerProfile.Gender.Male)){
                maleRadButton.setSelected(true);
                femaleRadButton.setSelected(false);
            } else if (gender.equals(CustomerProfile.Gender.Female)){
                maleRadButton.setSelected(false);
                femaleRadButton.setSelected(true);
            }
            dobDatePicker.setValue(dob);
        }

        isOldHidden = true;
        isNewHidden = true;
        isConfirmHidden = true;
        updatePasswordVisibility(isOldHidden, oldPassField, oldPassTextField, oldPassEyeIcon);
        updatePasswordVisibility(isNewHidden, newPassField, newPassTextField, newPassEyeIcon);
        updatePasswordVisibility(isConfirmHidden, confirmPassField, confirmPassTextField, confirmPassEyeIcon);
    }

    public void loadProfilePic(){
        Response<String> mediaResponse = DaoFactory.getUserDao().getFirstMedia(userId);
        if (mediaResponse.isSuccess()){
            String imagePath = mediaResponse.getData();
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                profilePic.setImage(image);
            } else {
                System.err.println("Image file not found: " + imageFile.getAbsolutePath());
            }
        }

        if (mainController != null){
            mainController.setCusMainController(mainController);
            mainController.loadData();
        }
    }

    public void editButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("cusProfilePic.fxml"));
        Parent profilePicRoot = loader.load();
        cusProfilePicController profilePicController = loader.getController();
        profilePicController.setCusProfileController(this);
        Stage profile = new Stage();
        profile.setTitle("Profile Picture");
        profile.initStyle(StageStyle.UNDECORATED);
        profile.setScene(new Scene(profilePicRoot));
        profile.initModality(Modality.APPLICATION_MODAL);
        profile.showAndWait();
    }

    public void cusInfoButtonClicked(ActionEvent event) {
        cusInfoPane.toFront();
    }

    public void confidentialInfoButtonClicked(ActionEvent event) {
        loginInfoPane.toFront();
    }

    public void discardCusInfoButtonClicked(ActionEvent event) {
        loadInfo();
    }

    public void saveCusInfoButtonClicked(ActionEvent event) throws IOException {
        String name = cusNameTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String email = emailTextField.getText();
        LocalDate dob = dobDatePicker.getValue();
        CustomerProfile.Gender gender = null;

        // Check if Male or Female RadioButton is selected
        if (maleRadButton.isSelected()) {
            gender = CustomerProfile.Gender.Male;
        } else if (femaleRadButton.isSelected()) {
            gender = CustomerProfile.Gender.Female;
        }

        // Perform data validation
        if (name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || dob == null || gender == null) {
            cusMainController.showAlert("Validation Error", "Please fill in all required fields.");
            return;
        }

        // Validate email format
        if (!email.matches(".+@.+\\..+")) {
            cusMainController.showAlert("Validation Error", "Invalid email format.");
            return;
        }

        // Check if Customer Profile exists
        Map<String, Object> cusQuery = Map.of("userId", userId);
        Response<ArrayList<CustomerProfile>> customerResponse = DaoFactory.getCustomerProfileDao().read(cusQuery);

        if (customerResponse.isSuccess()){
            // Update existing customer profile
            Map<String, Object> cusNewValue = Map.of("gender", gender, "dob", dob, "updatedAt", LocalDateTime.now());
            Response<Void> cusResponse = DaoFactory.getCustomerProfileDao().update(cusQuery, cusNewValue);
            if (!cusResponse.isSuccess()) {
                cusMainController.showAlert("Update Error", "Failed to update customer profile information.");
            }
        } else {
            // Create new customer profile
            CustomerProfile newCusProfile = new CustomerProfile(UUID.randomUUID(), userId, gender, dob, LocalDateTime.now(), LocalDateTime.now());
            Response<UUID> cusResponse = DaoFactory.getCustomerProfileDao().create(newCusProfile);
            if (!cusResponse.isSuccess()) {
                cusMainController.showAlert("Update Error", "Failed to update customer profile information.");
            }
        }

        // Update user information
        Map<String, Object> userQuery = Map.of("Id", userId);
        Map<String, Object> userNewValue = Map.of("name", name, "phoneNumber", phoneNumber, "email", email, "updatedAt", LocalDateTime.now());
        Response<Void> userResponse = DaoFactory.getUserDao().update(userQuery, userNewValue);

        if (!userResponse.isSuccess()) {
            cusMainController.showAlert("Update Error", "Failed to update user information.");
        }

        cusMainController.showAlert("Success", "Information updated successfully.");

        if (mainController != null){
            mainController.setCusMainController(mainController);
            mainController.loadData();
        }
        loadInfo();
    }

    public void discardLoginInfoButtonClicked(ActionEvent event) {
        loadInfo();
    }

    public void saveLoginInfoButtonClicked(ActionEvent event) {
        // Get user input
        String oldPassword = isOldHidden? oldPassField.getText() : oldPassTextField.getText();
        String newPassword = isNewHidden? newPassField.getText() : newPassTextField.getText();
        String confirmPassword = isConfirmHidden? confirmPassField.getText() : confirmPassTextField.getText();

        // Perform data validation
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            cusMainController.showAlert("Validation Error", "Please fill in all required fields.");
            return;
        }

        if (!oldPassword.equals(currentPassword)) {
            cusMainController.showAlert("Validation Error", "Incorrect old password.");
            return;
        }

        if (!newPassword.equals(confirmPassword)){
            cusMainController.showAlert("Validation Error", "Please ensure both entered passwords are the same.");
            return;
        }

        // Update login information
        Map<String, Object> userQuery = Map.of("Id", userId);
        Map<String, Object> userNewValue = Map.of("password", newPassword, "updatedAt", LocalDateTime.now());
        Response<Void> userResponse = DaoFactory.getUserDao().update(userQuery, userNewValue);

        if (!userResponse.isSuccess()) {
            cusMainController.showAlert("Update Error", "Failed to update login information.");
        }

        cusMainController.showAlert("Success", "Information updated successfully.");
        loadInfo();
    }

    public void oldEyeslashClicked() {
        isOldHidden = !isOldHidden;
        togglePasswordVisibility(isOldHidden, oldPassField, oldPassTextField, oldPassEyeIcon);
    }

    public void newEyeslashClicked() {
        isNewHidden = !isNewHidden;
        togglePasswordVisibility(isNewHidden, newPassField, newPassTextField, newPassEyeIcon);
    }

    public void confirmEyeslashClicked() {
        isConfirmHidden = !isConfirmHidden;
        togglePasswordVisibility(isConfirmHidden, confirmPassField, confirmPassTextField, confirmPassEyeIcon);
    }

    private void togglePasswordVisibility(boolean isHidden, PasswordField passwordField, TextField textField, ImageView icon) {
        if (isHidden) {
            passwordField.setVisible(true);
            textField.setVisible(false);
            passwordField.setText(textField.getText());
        } else {
            textField.setVisible(true);
            passwordField.setVisible(false);
            textField.setText(passwordField.getText());
        }

        // Update the eye icon based on the visibility state
        String iconImage = isHidden ? "/images/hide.png" : "/images/show.png";
        Image image = new Image(getClass().getResourceAsStream(iconImage));
        icon.setImage(image);
    }

    private void updatePasswordVisibility(boolean isHidden, PasswordField passwordField, TextField textField, ImageView icon) {
        passwordField.clear();
        textField.clear();
        if (isHidden) {
            passwordField.setVisible(true);
            textField.setVisible(false);
        } else {
            textField.setVisible(true);
            passwordField.setVisible(false);
        }

        // Update the eye icon based on the visibility state
        String iconImage = isHidden ? "/images/hide.png" : "/images/show.png";
        Image image = new Image(getClass().getResourceAsStream(iconImage));
        icon.setImage(image);
    }


    private TextFormatter<String> phoneNumberFormatter() {
        StringConverter<String> converter = new DefaultStringConverter() {
            @Override
            public String fromString(String string) {
                // Filter non-numeric characters and allow only one hyphen
                return string.replaceAll("[^0-9-]", "").replaceAll("(-.*?)-", "$1");
            }
        };

        return new TextFormatter<>(converter, null, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^\\d{0,3}(-\\d{0,7})?$")) {
                return change;
            }
            return null;
        });
    }

    public void setCusProfileController(cusProfileController controller) {
        this.cusProfileController = controller;
    }
    public void setCusMainController(cusMainController controller) { mainController = controller; }
}
