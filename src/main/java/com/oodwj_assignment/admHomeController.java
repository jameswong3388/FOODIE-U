package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.Dao;
import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class admHomeController {

    @FXML private GridPane requestGrid;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label roleLabel;
    @FXML private Label nameLabel;
    @FXML private Label phoneNumberLabel;
    @FXML private Label emailLabel;
    @FXML private Pane registrationInfoPane;
    @FXML private Pane requestPane;
    private UUID userId;

    public void initialize() throws IOException {
        hidePane();
        clearLabels();
        // Create a list to hold the credit panes
        List<VBox> requestPanes = new ArrayList<>();

        Map<String, Object> query = Map.of("accountStatus", Users.AccountStatus.Pending);
        Response<ArrayList<Users>> response = DaoFactory.getUserDao().read(query);

        if (response.isSuccess()){
            ArrayList<Users> pendingUsers = response.getData();

            for (Users user : pendingUsers) {
                // Load the request pane and set up its controller
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admRequestPane.fxml"));
                VBox requestPane = fxmlLoader.load();
                admRequestPaneController requestPaneController = fxmlLoader.getController();
                requestPaneController.setAdmHomeController(this);
                requestPaneController.populateUserData(user);
                requestPane.setUserData(fxmlLoader);
                requestPanes.add(requestPane); // Add the request pane to the list
            }
        }

        // Add the request panes to the grid
        int row = 0;
        for (VBox requestPane : requestPanes) {
            requestGrid.add(requestPane, 0, row);
            row++;
            GridPane.setMargin(requestPane, new Insets(2));
        }
    }

    public void setUserInfo(UUID userId) {
        showPane();
        this.userId = userId;
        showPane();

        // Fetch user information
        Map<String, Object> userQuery = Map.of("Id", userId);
        Response<ArrayList<Users>> userResponse = DaoFactory.getUserDao().read(userQuery);

        if (userResponse.isSuccess()) {
            Users user = userResponse.getData().get(0);

            usernameLabel.setText(user.getUsername());
            passwordLabel.setText(user.getPassword());
            roleLabel.setText(String.valueOf(user.getRole()));
            nameLabel.setText(user.getName());
            phoneNumberLabel.setText(user.getPhoneNumber());
            emailLabel.setText(user.getEmail());
        }
    }

    public void showPane(){
        registrationInfoPane.setVisible(true);
        requestPane.setLayoutX(19);
    }

    public void hidePane(){
        registrationInfoPane.setVisible(false);
        requestPane.setLayoutX(220);
    }

    private void clearLabels(){
        usernameLabel.setText("");
        passwordLabel.setText("");
        roleLabel.setText("");
        nameLabel.setText("");
        phoneNumberLabel.setText("");
        emailLabel.setText("");
    }

    public void approveButtonClicked(ActionEvent event) throws IOException {
        // Update the user status
        Map<String, Object> query = Map.of("Id", userId);
        Map<String, Object> newValue = Map.of("accountStatus", Users.AccountStatus.Approved, "updatedAt", LocalDateTime.now());

        Response<Void> response = DaoFactory.getUserDao().update(query, newValue);
        if (response.isSuccess()) {
            admMainController.showAlert("Success", "User registration request has been approved.");
            notificationController notificationController = new notificationController();
            notificationController.sendNotification(userId, "Your account has been approved. Welcome to FoodieU!", Notifications.notificationType.Information);
            createStoreIfVendor(userId);
            createWalletIfCustomer(userId);
        } else {
            admMainController.showAlert("Update Error", "Failed to update user's account status: " + response.getMessage());
        }
        removeRequestPane();
        hidePane();
    }

    public void createStoreIfVendor(UUID userId){
        Map<String, Object> query = Map.of("Id", userId);
        Response<ArrayList<Users>> response = DaoFactory.getUserDao().read(query);
        if (response.isSuccess()){
            Users.Role role = response.getData().get(0).getRole();
            if (role != Users.Role.Vendor){
                return;
            }
            Stores newStore = new Stores(UUID.randomUUID(), "", userId, "", Stores.storeCategory.Default, LocalDateTime.now(), LocalDateTime.now());
            DaoFactory.getStoreDao().create(newStore);
        }
    }

    public void createWalletIfCustomer(UUID userId){
        Map<String, Object> query = Map.of("Id", userId);
        Response<ArrayList<Users>> response = DaoFactory.getUserDao().read(query);
        if (response.isSuccess()){
            Users.Role role = response.getData().get(0).getRole();
            if (role != Users.Role.Customer){
                return;
            }
            Wallets newWallet = new Wallets(UUID.randomUUID(), userId, 0.0, LocalDateTime.now(), LocalDateTime.now());
            DaoFactory.getWalletDao().create(newWallet);
        }
    }

    private void removeRequestPane() {
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : requestGrid.getChildren()) {
            if (node instanceof VBox) {
                FXMLLoader loader = (FXMLLoader) node.getUserData();
                admRequestPaneController requestPaneController = loader.getController();
                UUID requestPaneUserId = requestPaneController.getUserId();

                if (requestPaneUserId.equals(userId)) {
                    nodesToRemove.add(node);
                }
            }
        }
        requestGrid.getChildren().removeAll(nodesToRemove);
    }
}
