package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.Dao;
import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Reviews;
import com.oodwj_assignment.models.Users;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class admRegisterController {

    @FXML private TableView<Users> usersTableView;
    @FXML private TableColumn<Users, UUID> userIdColumn;
    @FXML private TableColumn<Users, String> usernameColumn;
    @FXML private TableColumn<Users, String> passwordColumn;
    @FXML private TableColumn<Users, Users.Role> roleColumn;
    @FXML private TableColumn<Users, String> nameColumn;
    @FXML private TableColumn<Users, String> phoneNumberColumn;
    @FXML private TableColumn<Users, String> emailColumn;
    @FXML private TableColumn<Users, LocalDateTime> createdDateColumn;
    @FXML private TableColumn<Users, Button> actionColumn;

    public void initialize(){
        // Initialize column data factories
        userIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        usernameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUsername()));
        passwordColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPassword()));
        roleColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRole()));
        nameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        phoneNumberColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPhoneNumber()));
        emailColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmail()));
        createdDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCreatedAt()));
        // Define the "Edit" button cell factory
        actionColumn.setCellFactory(col -> createEditButtonCell());

        loadUsers();
    }

    private TableCell<Users, Button> createEditButtonCell() {
        return new TableCell<Users, Button>() {
            final Button editButton = new Button("Edit");

            {
                // Define the action when the "Edit" button is clicked
                editButton.setOnAction(event -> {
                    Users user = getTableView().getItems().get(getIndex());
                    UUID userId = user.getId();
                    try {
                        openModifyPane(userId);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        };
    }

    public void loadUsers(){
        Map<String, Object> query = Map.of("accountStatus", Users.AccountStatus.Approved);
        Response<ArrayList<Users>> response = DaoFactory.getUserDao().read(query);
        if (response.isSuccess()){
            ArrayList<Users> users = response.getData();
            usersTableView.getItems().setAll(users);
        }
    }

    private void openModifyPane(UUID userId) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("admModifyPane.fxml"));
        Parent modifyRoot = loader.load();
        admModifyPaneController modifyPaneController = loader.getController();
        modifyPaneController.setAdmRegisterController(this);
        modifyPaneController.fetchUserData(userId);
        Stage modify = new Stage();
        modify.setTitle("Modify Existing User");
        modify.initStyle(StageStyle.UNDECORATED);
        modify.setScene(new Scene(modifyRoot));
        modify.initModality(Modality.APPLICATION_MODAL);
        modify.showAndWait();
    }

    public void addButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("admRegisterPane.fxml"));
        Parent registerRoot = loader.load();
        admRegisterPaneController registerPaneController = loader.getController();
        registerPaneController.setAdmRegisterController(this);
        Stage register = new Stage();
        register.setTitle("Register New User");
        register.initStyle(StageStyle.UNDECORATED);
        register.setScene(new Scene(registerRoot));
        register.initModality(Modality.APPLICATION_MODAL);
        register.showAndWait();
    }
}
