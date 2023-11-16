package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Foods;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.UUID;

public class cusFoodPaneController {

    @FXML private Label nameLabel;
    @FXML private Label priceLabel;
    @FXML private ImageView foodImage;
    @FXML private Spinner<Integer> quantitySpinner;
    private UUID foodId;
    private cusMenuController cusMenuController;

    public void initialize() {
        // Initialize and configure the Spinner
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 0));
        quantitySpinner.setEditable(true);
        quantitySpinner.valueProperty().addListener((observable, oldQuantity, newQuantity) -> {
            if (cusMenuController != null) {
                cusMenuController.itemAdded(newQuantity, getName(), Double.parseDouble(getPrice().substring(3)));
            }
        });
    }

    public void populateOrderData(Foods foods){
        this.foodId = foods.getId();
        nameLabel.setText(foods.getFoodName());
        priceLabel.setText(String.format("RM %.2f", foods.getFoodPrice()));
        Response<String> mediaResponse = DaoFactory.getFoodDao().getFirstMedia(foods.getId());
        if (mediaResponse.isSuccess()){
            File imageFile = new File(mediaResponse.getData());
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                foodImage.setImage(image);
            } else {
                System.err.println("Image file not found: " + imageFile.getAbsolutePath());
            }
        }
    }
    public void setSpinnerValue(int value) { quantitySpinner.getValueFactory().setValue(value); }

    public void setCusMenuController(cusMenuController controller) {
        this.cusMenuController = controller;
    }

    public String getName() {
        return nameLabel.getText();
    }

    public String getPrice() {
        return priceLabel.getText();
    }

    public UUID getFoodId() {
        return foodId;
    }
}
