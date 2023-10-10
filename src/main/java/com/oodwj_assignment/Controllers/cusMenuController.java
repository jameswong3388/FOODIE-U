package com.oodwj_assignment.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class cusMenuController {

    @FXML
    private Button btnBack;

    @FXML
    public void backHome(ActionEvent event) {
        Stage menuStage = (Stage) btnBack.getScene().getWindow();
        menuStage.close();
    }

}
