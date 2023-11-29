package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Notifications;
import com.oodwj_assignment.models.Stores;
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
import java.util.Map;
import java.util.UUID;

public class admRegisterPaneController {

    @FXML private Button closeButton;
    @FXML private TextField usernameTextField;
    @FXML private TextField passTextField;
    @FXML private TextField nameTextField;
    @FXML private ComboBox<Users.Role> roleComboBox;
    @FXML private TextField phoneNumberTextField;
    @FXML private TextField emailTextField;
    @FXML private Label phoneNumberFormatLabel;
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

    public void closeButtonClicked(ActionEvent event) {
        closePage();
    }

    public void discardButtonClicked(ActionEvent event) {
        usernameTextField.clear();
        passTextField.clear();
        nameTextField.clear();
        roleComboBox.setValue(null);
        phoneNumberTextField.clear();
        emailTextField.clear();
    }

    public void registerButtonClicked(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passTextField.getText();
        Users.Role role = roleComboBox.getValue();
        String name = nameTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String email = emailTextField.getText();

        // Perform data validation
        if (username.isEmpty() || password.isEmpty() || role == null || name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
            admMainController.showAlert("Validation Error", "Please fill in all required fields.");
            return;
        }

        // Validate email format
        if (!email.matches(".+@.+\\..+")) {
            admMainController.showAlert("Validation Error", "Invalid email format.");
            return;
        }

        // Add User
        Users user = new Users(UUID.randomUUID(), username, password, role, name, phoneNumber, email, Users.AccountStatus.Approved, LocalDateTime.now(), LocalDateTime.now());
        Response<UUID> newUser = DaoFactory.getUserDao().create(user);

        if (newUser.isSuccess()) {
            UUID newUserId = newUser.getData();
            //Check if it is vendor then assign a store for vendor
            admHomeController homeController = new admHomeController();
            homeController.createStoreIfVendor(newUserId);
            //Check if it is customer then assign wallet
            homeController.createWalletIfCustomer(newUserId);
            admMainController.showAlert("Success", "New user has been created successfully: " + newUserId);
            notificationController notificationController = new notificationController();
            notificationController.sendNotification(newUserId, "Your account has been created. Welcome to FoodieU!", Notifications.notificationType.Information);
        } else {
            admMainController.showAlert("Error", "Failed to add user: " + newUser.getMessage());
        }

        closePage();

        if (registerController != null){
            registerController.loadUsers();
        }
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
