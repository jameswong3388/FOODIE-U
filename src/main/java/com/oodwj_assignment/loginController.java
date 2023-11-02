package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class loginController {

    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordTextField;
    @FXML private ImageView eyeIcon;

    private boolean isImageHidden = true;

    public void loginButtonAction(ActionEvent event) throws IOException {
        String username = usernameTextField.getText();
        String password;
        if (isImageHidden){
            password = passwordField.getText();
        }
        else{
            password = passwordTextField.getText();
        }

        Response<UUID> loginResponse = DaoFactory.getAuthDao().login(username, password);

        System.out.println(loginResponse.getData());

        if (loginResponse.isSuccess()) {
            Map<String, Object> query = Map.of("username", username);
            Response<ArrayList<Users>> usersResponse = DaoFactory.getUserDao().read(query);
            ArrayList<Users> users = usersResponse.getData();
            Users.Role userRole = users.get(0).getRole();

            String fxmlPath = getFxmlPathForRole(userRole);
            if (fxmlPath != null) {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
                Stage stage = new Stage();
                stage.setTitle(getTitleForRole(userRole));
                stage.setScene(new Scene(root));
                stage.show();

                Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                loginStage.close();
            } else {
                System.out.println("Page not found");
            }

        } else {
            String errorMessage = loginResponse.getMessage();
            System.out.println("Login failed: " + errorMessage);
        }
    }

    private String getFxmlPathForRole(Users.Role role) {
        switch (role) {
            case Admin:
                return "admMain.fxml";
            case Delivery_Runner:
                return "runMain.fxml";
            case Vendor:
                return "venMain.fxml";
            case Customer:
                return "cusMain.fxml";
            default:
                return null;
        }
    }

    private String getTitleForRole(Users.Role role) {
        switch (role) {
            case Admin:
                return "Admin Page";
            case Delivery_Runner:
                return "Runner Page";
            case Vendor:
                return "Vendor Page";
            case Customer:
                return "Customer Page";
            default:
                return "User Page";
        }
    }

    public void eyeslashClicked() {
        if (isImageHidden) {
            passwordTextField.setText(passwordField.getText());
            passwordTextField.setVisible(true);
            passwordField.setVisible(false);
            Image showImage = new Image(getClass().getResourceAsStream("/images/show.png"));
            eyeIcon.setImage(showImage);
        } else {
            passwordField.setText(passwordTextField.getText());
            passwordField.setVisible(true);
            passwordTextField.setVisible(false);
            Image hideImage = new Image(getClass().getResourceAsStream("/images/hide.png"));
            eyeIcon.setImage(hideImage);
        }

        // Toggle the image state
        isImageHidden = !isImageHidden;
    }
}
