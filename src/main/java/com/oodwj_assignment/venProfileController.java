package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Foods;
import com.oodwj_assignment.models.Orders;
import com.oodwj_assignment.models.Stores;
import com.oodwj_assignment.models.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import java.awt.event.KeyEvent;
import javafx.scene.image.ImageView;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.*;

public class venProfileController {

    @FXML private ComboBox<Stores.storeCategory> categoryComboBox;
    @FXML private TextField descriptionTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField restaurantNameTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private Pane restaurantInfoPane;
    @FXML private Pane loginInfoPane;
    @FXML private Label phoneNumberFormatLabel;
    @FXML private Label restaurantNameLabel;
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

    private UUID storeId = venMainController.storeId;
    private UUID vendorId = venMainController.vendorId;
    private String currentPassword;
    private boolean isOldHidden;
    private boolean isNewHidden;
    private boolean isConfirmHidden;



    public void initialize(){
        phoneNumberFormatLabel.setVisible(false);
        restaurantInfoPane.toFront();
        phoneNumberTextField.setTextFormatter(phoneNumberFormatter());
        // Show the label when the text field gains focus
        phoneNumberTextField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            phoneNumberFormatLabel.setVisible(newVal);
        });
        ObservableList<Stores.storeCategory> categoryList = FXCollections.observableArrayList(Stores.storeCategory.values());
        categoryComboBox.setItems(categoryList);
        loadInfo();
    }

    private void loadInfo(){
        Map<String, Object> storeQuery = Map.of("Id", storeId);
        Response<ArrayList<Stores>> storeResponse = DaoFactory.getStoreDao().read(storeQuery);

        if (storeResponse.isSuccess()){
            ArrayList<Stores> stores = storeResponse.getData();
            String restaurantName = stores.get(0).getName();
            String description = stores.get(0).getDescription();
            Stores.storeCategory category = stores.get(0).getCategory();

            restaurantNameLabel.setText(restaurantName);
            restaurantNameTextField.setText(restaurantName);
            descriptionTextField.setText(description);
            categoryComboBox.setValue(category);
        }

        Map<String, Object> userQuery = Map.of("Id", vendorId);
        Response<ArrayList<Users>> userResponse = DaoFactory.getUserDao().read(userQuery);

        if (userResponse.isSuccess()){
            ArrayList<Users> users = userResponse.getData();
            String name = users.get(0).getName();
            String phoneNumber = users.get(0).getPhoneNumber();
            String email = users.get(0).getEmail();
            String username = users.get(0).getUsername();
            currentPassword = users.get(0).getPassword();

            nameTextField.setText(name);
            phoneNumberTextField.setText(phoneNumber);
            emailTextField.setText(email);
            usernameTextField.setText(username);
        }
        isOldHidden = true;
        isNewHidden = true;
        isConfirmHidden = true;
        updatePasswordVisibility(isOldHidden, oldPassField, oldPassTextField, oldPassEyeIcon);
        updatePasswordVisibility(isNewHidden, newPassField, newPassTextField, newPassEyeIcon);
        updatePasswordVisibility(isConfirmHidden, confirmPassField, confirmPassTextField, confirmPassEyeIcon);
    }

    public void editButtonClicked(ActionEvent event) throws IOException {
        Parent profileRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("venProfilePic.fxml")));
        Stage profile = new Stage();
        profile.setTitle("Profile Picture");
        profile.initStyle(StageStyle.UNDECORATED);
        profile.setScene(new Scene(profileRoot));
        profile.initModality(Modality.APPLICATION_MODAL);
        profile.showAndWait();
    }

    public void restaurantInfoButtonClicked(ActionEvent event) {
        restaurantInfoPane.toFront();
    }

    public void confidentialInfoButtonClicked(ActionEvent event) {
        loginInfoPane.toFront();
    }

    public void discardResInfoButtonClicked(ActionEvent event) {
        loadInfo();
    }

    public void saveResInfoButtonClicked(ActionEvent event) {
        // Get user input
        String restaurantName = restaurantNameTextField.getText();
        String description = descriptionTextField.getText();
        Stores.storeCategory category = categoryComboBox.getValue();
        String name = nameTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String email = emailTextField.getText();

        // Perform data validation
        if (restaurantName.isEmpty() || name.isEmpty() || description.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
            venMainController.showAlert("Validation Error", "Please fill in all required fields.");
            return;
        }

        // Validate email format
        if (!email.matches(".+@.+\\..+")) {
            venMainController.showAlert("Validation Error", "Invalid email format.");
            return;
        }

        // Update store information
        Map<String, Object> storeQuery = Map.of("Id", storeId);
        Map<String, Object> storeNewValue = Map.of("name", restaurantName, "description", description, "category", category);
        Response<Void> storeResponse = DaoFactory.getStoreDao().update(storeQuery, storeNewValue);

        if (!storeResponse.isSuccess()) {
            venMainController.showAlert("Update Error", "Failed to update store information.");
        }

        // Update user information
        Map<String, Object> userQuery = Map.of("Id", vendorId);
        Map<String, Object> userNewValue = Map.of("name", name, "phoneNumber", phoneNumber, "email", email);
        Response<Void> userResponse = DaoFactory.getUserDao().update(userQuery, userNewValue);

        if (!userResponse.isSuccess()) {
            venMainController.showAlert("Update Error", "Failed to update user information.");
        }

        venMainController.showAlert("Success", "Information updated successfully.");
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
            venMainController.showAlert("Validation Error", "Please fill in all required fields.");
            return;
        }

        if (!oldPassword.equals(currentPassword)) {
            venMainController.showAlert("Validation Error", "Incorrect old password.");
            return;
        }

        if (!newPassword.equals(confirmPassword)){
            venMainController.showAlert("Validation Error", "Please ensure both entered passwords are the same.");
            return;
        }

        // Update login information
        Map<String, Object> userQuery = Map.of("Id", vendorId);
        Map<String, Object> userNewValue = Map.of("password", newPassword);
        Response<Void> userResponse = DaoFactory.getUserDao().update(userQuery, userNewValue);

        if (!userResponse.isSuccess()) {
            venMainController.showAlert("Update Error", "Failed to update login information.");
        }

        venMainController.showAlert("Success", "Information updated successfully.");
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
}
