package com.oodwj_assignment;

import com.oodwj_assignment.models.Stores;
import com.oodwj_assignment.models.VendorProfile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class cusRestaurantPaneController {

    @FXML private Label nameLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label deliveryFeeLabel;
    @FXML private Label ratingLabel;
    private cusHomeController cusHomeController;

    public void viewButtonClicked(ActionEvent event) throws IOException {
        String name = nameLabel.getText();
        if (cusHomeController != null) {
            cusHomeController.setOrderInfo(name);
        }
    }

    public void populateVendorData(Stores stores){
        nameLabel.setText(stores.getName());
        descriptionLabel.setText(stores.getDescription());
    }

    public void setCusHomeController(cusHomeController controller) {
        this.cusHomeController = controller;
    }

    public String  getName() {
        return nameLabel.getText();
    }
}
