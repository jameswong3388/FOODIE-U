package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Transactions;
import com.oodwj_assignment.models.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class admRequestPaneController {

    @FXML private Label userIdLabel;
    @FXML private Label usernameLabel;
    @FXML private Label roleLabel;
    @FXML private Label dateLabel;
    private admHomeController admHomeController;
    private UUID userId;
    private Users user;
    public void populateUserData(Users user){
        this.user = user;
        userId = user.getId();
        userIdLabel.setText(String.valueOf(userId));
        usernameLabel.setText(user.getUsername());
        roleLabel.setText(String.valueOf(user.getRole()));
        dateLabel.setText(String.valueOf(user.getCreatedAt().withNano(0)));
    }

    public void viewButtonClicked(ActionEvent event) throws IOException {
        admHomeController.setUserInfo(userId);
    }

    public void setAdmHomeController(admHomeController controller) { this.admHomeController = controller; }

    public UUID getUserId() { return userId; }
}
