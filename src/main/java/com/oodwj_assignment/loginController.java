package com.oodwj_assignment;

import com.oodwj_assignment.states.AppState;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;


public class loginController {

    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordTextField;
    @FXML private TextField fgtUsernameTextField;
    @FXML private TextField fgtPasswordTextField;
    @FXML private TextField fgtConfirmTextField;
    @FXML private Pane loginPane;
    @FXML private Pane forgotPane;
    @FXML private ImageView eyeIcon;

    private boolean isImageHidden = true;

    public void loginButtonClicked(ActionEvent event) throws IOException {
        String username = usernameTextField.getText();
        String password;
        if (isImageHidden){
            password = passwordField.getText();
        }
        else{
            password = passwordTextField.getText();
        }

        Response<UUID> loginResponse = DaoFactory.getAuthDao().login(username, password);

        if (loginResponse.isSuccess()) {
            AppState.setSessionToken(loginResponse.getData());

            Users user = DaoFactory.getAuthDao().geAuthenticatedUser().getData();
            Users.Role userRole = user.getRole();

            String fxmlPath = getFxmlPathForRole(userRole);
            if (fxmlPath != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent root = fxmlLoader.load();

                // Set the user ID in the appropriate controller
                Object controller = fxmlLoader.getController();
                if (controller instanceof cusMainController) {
                    cusMainController mainController = (cusMainController) controller;
                    mainController.setUserId(user.getId());
                } else if (controller instanceof admMainController) {
                    admMainController mainController = (admMainController) controller;
                    mainController.setUserId(user.getId());
                } else if (controller instanceof venMainController) {
                    venMainController mainController = (venMainController) controller;
                    mainController.setUserId(user.getId());
                } else if (controller instanceof runMainController) {
                    runMainController mainController = (runMainController) controller;
                    mainController.setUserId(user.getId());
                }

                Stage stage = new Stage();
                stage.setTitle(getTitleForRole(userRole));
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.show();

                Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                loginStage.close();
            } else {
                System.out.println("Page not found");
            }

        } else {
            String errorMessage = loginResponse.getMessage();
            admMainController.showAlert("Login failed", errorMessage);
        }
    }

    public void registerButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
        Parent registerRoot = loader.load();
        registerController registerController = loader.getController();
        Stage register = new Stage();
        register.setTitle("Register Page");
        register.setScene(new Scene(registerRoot));
        register.initModality(Modality.APPLICATION_MODAL);
        register.showAndWait();
    }

    public void forgotPasswordClicked(MouseEvent event){
        forgotPane.toFront();
        clearFields();
    }

    public void resetButtonClicked(ActionEvent event){
        // Get user input
        String username = fgtUsernameTextField.getText();
        String newPassword = fgtPasswordTextField.getText();
        String confirmPassword = fgtConfirmTextField.getText();

        // Perform data validation
        if (username.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            admMainController.showAlert("Validation Error", "Please fill in all required fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)){
            venMainController.showAlert("Validation Error", "Please ensure both entered passwords are the same.");
            return;
        }

        // Reset Password
        Map<String, Object> query = Map.of("username", username);
        Response<ArrayList<Users>> response = DaoFactory.getUserDao().read(query);
        if (response.isSuccess()){
            UUID userId = response.getData().get(0).getId();

            Map<String, Object> userQuery = Map.of("Id", userId);
            Map<String, Object> userNewValue = Map.of("password", newPassword, "updatedAt", LocalDateTime.now());
            Response<Void> userResponse = DaoFactory.getUserDao().update(userQuery, userNewValue);

            if (!userResponse.isSuccess()) {
                admMainController.showAlert("Update Error", "Failed to update login information.");
            }
            admMainController.showAlert("Success", "Information updated successfully.");
            clearFields();
            loginPane.toFront();
        } else {
            admMainController.showAlert("Error", "Failed to fetch user: " + response.getMessage());
        }
    }

    public void backButtonClicked(ActionEvent event){
        loginPane.toFront();
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

    private void clearFields(){
        usernameTextField.clear();
        passwordField.clear();
        passwordTextField.clear();
        fgtUsernameTextField.clear();
        fgtPasswordTextField.clear();
        fgtConfirmTextField.clear();
    }
}
