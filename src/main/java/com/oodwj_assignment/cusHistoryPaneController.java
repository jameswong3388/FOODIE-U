package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Foods;
import com.oodwj_assignment.models.OrderFoods;
import com.oodwj_assignment.models.Orders;
import com.oodwj_assignment.models.Stores;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class cusHistoryPaneController {
    @FXML private ImageView restaurantProfile;
    @FXML private Label nameLabel;
    @FXML private Label dateLabel;
    @FXML private Label priceLabel;
    private UUID orderId;

    private cusHistoryController cusHistoryController;
    private cusReviewController cusReviewController;

    public void populateOrderData(Orders orders){
        orderId = orders.getId();
        UUID storeId = getStoreId(orderId);
        priceLabel.setText(String.format("RM %.2f", orders.getTotalPrice()));
        dateLabel.setText(String.valueOf(orders.getCreatedAt().withNano(0)));
        setRestaurantName(storeId);
        setRestaurantImage(storeId);
    }

    public void viewButtonClicked(ActionEvent event) throws IOException {
        String restaurant = nameLabel.getText();
        if (cusHistoryController != null) {
            cusHistoryController.setOrderInfo(orderId, restaurant);
        }
        if (cusReviewController != null){
            cusReviewController.setReviewInfo(orderId);
        }
    }

    public UUID getStoreId(UUID orderId) {
        // Fetch all OrderFoods entries for the given orderId
        Map<String, Object> orderFoodQuery = Map.of("orderId", orderId);
        Response<ArrayList<OrderFoods>> orderFoodsResponse = DaoFactory.getOrderFoodDao().read(orderFoodQuery);

        if (orderFoodsResponse.isSuccess()) {
            List<OrderFoods> orderFoods = orderFoodsResponse.getData();

            if (!orderFoods.isEmpty()) {
                // Get the foodId from the first OrderFoods object
                UUID foodId = orderFoods.get(0).getFoodId();

                // Fetch the Foods object for this foodId
                Map<String, Object> foodQuery = Map.of("Id", foodId);
                Response<ArrayList<Foods>> foodResponse = DaoFactory.getFoodDao().read(foodQuery);

                if (foodResponse.isSuccess()) {
                    return foodResponse.getData().get(0).getStoreId();
                }
            }
        } else {
            System.err.println("Failed to retrieve storeId from the order: " + orderFoodsResponse.getMessage());
        }
        return null;
    }


    private void setRestaurantImage(UUID storeId) {
        Map<String, Object> query = Map.of("Id", storeId);
        Response<ArrayList<Stores>> storeResponse = DaoFactory.getStoreDao().read(query);

        if(storeResponse.isSuccess()){
            UUID vendorId = storeResponse.getData().get(0).getVendorId();
            Response<String> mediaResponse = DaoFactory.getUserDao().getFirstMedia(vendorId);

            if (mediaResponse.isSuccess()) {
                File imageFile = new File(mediaResponse.getData());
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    restaurantProfile.setImage(image);
                } else {
                    System.err.println("Image file not found: " + imageFile.getAbsolutePath());
                }
            }
        }
    }

    private void setRestaurantName(UUID storeId) {
        Map<String, Object> query = Map.of("Id", storeId);
        Response<ArrayList<Stores>> storeResponse = DaoFactory.getStoreDao().read(query);

        if(storeResponse.isSuccess()){
            nameLabel.setText(storeResponse.getData().get(0).getName());
        }
    }

    // Set the parent cusHistoryController for communication
    public void setCusHistoryController(cusHistoryController controller) {
        this.cusHistoryController = controller;
    }

    public void setCusReviewController(cusReviewController controller) { this.cusReviewController = controller; }

    public UUID getOrderId() {
        return orderId;
    }
}
