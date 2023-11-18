package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Notifications;
import com.oodwj_assignment.models.Users;
import com.oodwj_assignment.states.AppState;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.swing.text.html.parser.Parser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.UUID;

public class registerController implements Initializable {
    public javafx.scene.control.Label messageLabel;
    @FXML
    private PasswordField passwordfield;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField phonenumTextField;
    @FXML
    private ChoiceBox<String> roleChoiceBox;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Button registerButton;
    private String[] role = {"User", "Vendor", "Runner"};
    public void initialize(URL arg0, ResourceBundle arg1){
        roleChoiceBox.getItems().addAll(role);

    }

    public void getRole(ActionEvent event) throws IOException {
        String myrole = roleChoiceBox.getValue();
    }
    public void registerButtonAction(ActionEvent event){
        String name = nameTextField.getText();
        String phonenum = phonenumTextField.getText();
        String email = emailTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordfield.getText();
        String myrole = roleChoiceBox.getValue();

        //add login navigation button
        //make sure to enter all values

        switch (myrole) {
            case "User":
                Users users = new Users(UUID.randomUUID(), username, password, Users.Role.Customer, name, phonenum, email, Users.AccountStatus.Pending, LocalDateTime.now(), LocalDateTime.now());
                Response <UUID> res = DaoFactory.getUserDao().create(users);
                if (!res.isSuccess()){
                    System.out.println(res.getMessage());
                }
            case "Vendor":
                Users users2 = new Users(UUID.randomUUID(), username, password, Users.Role.Vendor, name, phonenum, email, Users.AccountStatus.Pending, LocalDateTime.now(), LocalDateTime.now());
                Response <UUID> res2 = DaoFactory.getUserDao().create(users2);
                if (!res2.isSuccess()){
                    System.out.println(res2.getMessage());
                }
            case "Runner":
                Users users3 = new Users(UUID.randomUUID(), username, password, Users.Role.Delivery_Runner, name, phonenum, email, Users.AccountStatus.Pending, LocalDateTime.now(), LocalDateTime.now());
                Response <UUID> res3 = DaoFactory.getUserDao().create(users3);
                if (!res3.isSuccess()){
                    System.out.println(res3.getMessage());
                }
        }
        //send notification to admin 8c5dbefc-cad2-4c81-a5a6-cc95bf506610
        DaoFactory.getNotificationDao().create(new Notifications(null, "new register user", Notifications.notificationStatus.Unread, Notifications.notificationType.Information, UUID.fromString("8c5dbefc-cad2-4c81-a5a6-cc95bf506610"), LocalDateTime.now(), LocalDateTime.now()));
        messageLabel.setText("Kindly review the registration form again to confirm your registration status.");



    }
}
