package com.oodwj_assignment;

import com.oodwj_assignment.models.Foods;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class cusFoodPaneController {

    @FXML private Label nameLabel;
    @FXML private Label priceLabel;
    @FXML private Spinner<Integer> quantitySpinner;
    private cusMenuController cusMenuController;

    public void initialize() {
        // Initialize and configure the Spinner
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 0));
        quantitySpinner.setEditable(true);
        quantitySpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (cusMenuController != null) {
                cusMenuController.itemAdded(newValue, getName(), getPrice());
            }
        });
    }

    public void populateOrderData(Foods foods){
        nameLabel.setText(foods.getFoodName());
        priceLabel.setText(String.format("RM %.2f", foods.getFoodPrice()));
    }

    public void setCusMenuController(cusMenuController controller) {
        this.cusMenuController = controller;
    }

    public String getName() {
        return nameLabel.getText();
    }

    public String getPrice() {
        return priceLabel.getText();
    }
}
