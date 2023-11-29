package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class registerController {

    @FXML private TextField nameTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private ComboBox<Users.Role> roleComboBox;
    @FXML private Label phoneNumberFormatLabel;

    public void initialize(){
        phoneNumberFormatLabel.setVisible(false);
        phoneNumberTextField.setTextFormatter(phoneNumberFormatter());
        // Show the label when the text field gains focus
        phoneNumberTextField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            phoneNumberFormatLabel.setVisible(newVal);
        });
        List<Users.Role> roleList = Arrays.stream(Users.Role.values())
                .filter(role -> role != Users.Role.Admin)
                .collect(Collectors.toList());
        roleComboBox.setItems(FXCollections.observableArrayList(roleList));
    }

    public void registerButtonClicked(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
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
        Users user = new Users(UUID.randomUUID(), username, password, role, name, phoneNumber, email, Users.AccountStatus.Pending, LocalDateTime.now(), LocalDateTime.now());
        Response<UUID> newUser = DaoFactory.getUserDao().create(user);

        if (newUser.isSuccess()) {
            UUID newUserId = newUser.getData();
            admMainController.showAlert("Registration Successful", "Your account request with ID" + newUserId + " has been submitted successfully. Please review and log in again to confirm the status of your registration.");
            clearFields();
        }
    }

    public void clearButtonClicked(ActionEvent event){
        clearFields();
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

    private void clearFields(){
        usernameTextField.clear();
        passwordTextField.clear();
        nameTextField.clear();
        roleComboBox.setValue(null);
        phoneNumberTextField.clear();
        emailTextField.clear();
    }
}
