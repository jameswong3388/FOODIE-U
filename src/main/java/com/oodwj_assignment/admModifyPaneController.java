package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Notifications;
import com.oodwj_assignment.models.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class admModifyPaneController {

    @FXML private Button closeButton;
    @FXML private TextField usernameTextField;
    @FXML private TextField passTextField;
    @FXML private TextField nameTextField;
    @FXML private ComboBox<Users.Role> roleComboBox;
    @FXML private TextField phoneNumberTextField;
    @FXML private TextField emailTextField;
    @FXML private Label phoneNumberFormatLabel;
    private UUID userId;
    private admRegisterController registerController;

    public void initialize(){
        phoneNumberFormatLabel.setVisible(false);
        phoneNumberTextField.setTextFormatter(phoneNumberFormatter());
        // Show the label when the text field gains focus
        phoneNumberTextField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            phoneNumberFormatLabel.setVisible(newVal);
        });
        ObservableList<Users.Role> roleList = FXCollections.observableArrayList(Users.Role.values());
        roleComboBox.setItems(roleList);
    }

    public void fetchUserData(UUID userId){
        this.userId = userId;
        Map<String, Object> query = Map.of("Id", userId);
        Response<ArrayList<Users>> response = DaoFactory.getUserDao().read(query);
        if(response.isSuccess()){
            Users user = response.getData().get(0);
            usernameTextField.setText(user.getUsername());
            passTextField.setText(user.getPassword());
            roleComboBox.setValue(user.getRole());
            nameTextField.setText(user.getName());
            phoneNumberTextField.setText(user.getPhoneNumber());
            emailTextField.setText(user.getEmail());
        }
    }

    public void closeButtonClicked(ActionEvent event) { closePage(); }

    public void deleteButtonClicked(ActionEvent event) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Delete User");
        confirmation.setContentText("Are you sure you want to delete this user?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Map<String, Object> query = Map.of("Id", userId);
            Response<Void> response = DaoFactory.getUserDao().deleteOne(query);

            if (response.isSuccess()) {
                // Check if there's any media associated with the user
                Response<String> mediaResponse = DaoFactory.getUserDao().getFirstMedia(userId);
                if (mediaResponse.isSuccess()) {
                    // If there is media, remove it
                    Response<Void> removeMediaResponse = DaoFactory.getUserDao().removeMedia(userId);
                    if (!removeMediaResponse.isSuccess()) {
                        admMainController.showAlert("Media Error", "Failed to remove user profile: " + removeMediaResponse.getMessage());
                        return;
                    }
                }
                admMainController.showAlert("Success", "User deleted successfully.");
                closePage();

                if (registerController != null){
                    registerController.loadUsers();
                }
            } else {
                admMainController.showAlert("Error", "Failed to delete user: " + response.getMessage());
            }
        }
    }

    public Response<Object> updateButtonClicked(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passTextField.getText();
        Users.Role role = roleComboBox.getValue();
        String name = nameTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String email = emailTextField.getText();

        // Perform data validation
        if (username.isEmpty() || password.isEmpty() || role == null || name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
            admMainController.showAlert("Validation Error", "Please fill in all required fields.");
            return null;
        }

        // Validate email format
        if (!email.matches(".+@.+\\..+")) {
            admMainController.showAlert("Validation Error", "Invalid email format.");
            return null;
        }

        // Check if username taken
        if (DaoFactory.getUserDao().isUsernameTaken(username).getData()) {
            admMainController.showAlert("Validation Error", "Username is taken.");
            return null;
        }

        // Update user information
        Map<String, Object> query = Map.of("Id", userId);
        Map<String, Object> newValue = Map.of("username", username, "password", password, "role", role, "name", name, "phoneNumber", phoneNumber, "email", email, "updatedAt", LocalDateTime.now());
        Response<Void> response = DaoFactory.getUserDao().update(query, newValue);

        if (response.isSuccess()) {
            admMainController.showAlert("Success", "User information updated successfully.");
            notificationController notificationController = new notificationController();
            notificationController.sendNotification(userId, "Your account information has been updated by admin.", Notifications.notificationType.Information);
        } else {
            admMainController.showAlert("Update Error", "Failed to update user information: " + response.getMessage());
        }

        closePage();

        if (registerController != null){
            registerController.loadUsers();
        }
        return null;
    }

    private void closePage(){
        Stage methodStage = (Stage) closeButton.getScene().getWindow();
        methodStage.close();
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

    public void setAdmRegisterController(admRegisterController controller) { registerController = controller; }
}
