package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Foods;
import com.oodwj_assignment.models.OrderFoods;
import com.oodwj_assignment.models.Reviews;
import com.oodwj_assignment.models.Stores;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class cusRestaurantPaneController {

    @FXML private Label nameLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label ratingLabel;
    @FXML private ImageView restaurantIProfile;
    private cusHomeController cusHomeController;

    public void viewButtonClicked(ActionEvent event) throws IOException {
        String name = nameLabel.getText();
        if (cusHomeController != null) {
            cusHomeController.setOrderInfo(name);
        }
    }

    public void populateVendorData(Stores stores) {
        double averageRating = 0.0;
        nameLabel.setText(stores.getName());
        descriptionLabel.setText(stores.getDescription());
        List<UUID> orderIds = getOrderIds(stores.getId());
        if (!orderIds.isEmpty()){
            averageRating = calculateAverageRating(orderIds);
        }
        ratingLabel.setText(String.format("%.1f", averageRating));
        setRestaurantImage(stores.getVendorId());
    }

    private List<UUID> getOrderIds(UUID storeId) {
        Set<UUID> storeOrderIds = new HashSet<>();

        Map<String, Object> foodQuery = Map.of("storeId", storeId);
        Response<ArrayList<Foods>> foodResponse = DaoFactory.getFoodDao().read(foodQuery);

        if (foodResponse.isSuccess()) {
            List<UUID> foodIds = foodResponse.getData().stream()
                    .map(Foods::getId)
                    .toList();

            for (UUID foodId : foodIds) {
                Map<String, Object> orderFoodQuery = Map.of("foodId", foodId);
                Response<ArrayList<OrderFoods>> orderFoodsResponse = DaoFactory.getOrderFoodDao().read(orderFoodQuery);

                if (orderFoodsResponse.isSuccess()) {
                    List<UUID> orderIds = orderFoodsResponse.getData().stream()
                            .map(OrderFoods::getOrderId)
                            .distinct()
                            .toList();

                    storeOrderIds.addAll(orderIds);
                }
            }
        }
        return new ArrayList<>(storeOrderIds);
    }

    public void setCusHomeController(cusHomeController controller) {
        this.cusHomeController = controller;
    }

    private double calculateAverageRating(List<UUID> orderIds) {
        Map<UUID, Double> orderAverageRatings = new HashMap<>();

        for (UUID orderId : orderIds) {
            Map<String, Object> query = Map.of("modelUUID", orderId, "model", "/models/Order");
            Response<ArrayList<Reviews>> response = DaoFactory.getReviewDao().read(query);

            if (response.isSuccess()) {
                ArrayList<Reviews> reviews = response.getData();
                double totalRatings = reviews.stream()
                        .mapToDouble(review -> convertRatingToNumber(review.getReviewRating()))
                        .sum();
                double averageRating = (!reviews.isEmpty()) ? totalRatings / reviews.size() : 0.0;
                orderAverageRatings.put(orderId, averageRating);
            }
        }

        double defaultRating = 0.0;
        return orderAverageRatings.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(defaultRating);
    }

    private int convertRatingToNumber(Reviews.reviewRating rating) {
        switch (rating) {
            case One:
                return 1;
            case Two:
                return 2;
            case Three:
                return 3;
            case Four:
                return 4;
            case Five:
                return 5;
            default:
                return 0;
        }
    }

    private void setRestaurantImage(UUID vendorId) {
        Response<String> mediaResponse = DaoFactory.getUserDao().getFirstMedia(vendorId);

        if (mediaResponse.isSuccess()) {
            File imageFile = new File(mediaResponse.getData());
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                restaurantIProfile.setImage(image);
            } else {
                System.err.println("Image file not found: " + imageFile.getAbsolutePath());
            }
        }
    }
}
