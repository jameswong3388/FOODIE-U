package com.oodwj_assignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class cusPaymentController {

    @FXML
    private Button btnCancel;
    @FXML
    void backOrder(ActionEvent event) {
        Stage paymentStage = (Stage) btnCancel.getScene().getWindow();
        paymentStage.close();
    }
}
