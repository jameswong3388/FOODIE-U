package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

public class cusRestaurantReviewController {


    @FXML private GridPane reviewGrid;
    @FXML private Label averageRatingLabel;
    @FXML private Button backButton;
    private ArrayList<UUID> storeOrderIds = new ArrayList<>();
    private Map<UUID, List<Reviews>> orderReviewsMap = new HashMap<>();

    private void loadReview() throws IOException {
        // Create a list to hold the review panes
        int row = 1;
        int column = 0;

        if (!storeOrderIds.isEmpty()) {
            for (UUID orderId : storeOrderIds) {

                Map<String, Object> query = Map.of("modelUUID", orderId, "model", "/models/Order");
                Response<ArrayList<Reviews>> response = DaoFactory.getReviewDao().read(query);

                if (response.isSuccess()) {
                    ArrayList<Reviews> reviews = response.getData();
                    orderReviewsMap.put(orderId, reviews);

                    for (Reviews review : reviews) {
                        String name = getNameByUserId(review.getUserId());

                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cusRestaurantReviewPane.fxml"));
                        VBox reviewPane = fxmlLoader.load();
                        cusRestaurantReviewPaneController restaurantReviewPaneController = fxmlLoader.getController();
                        restaurantReviewPaneController.populateOrderData(review, name);
                        reviewPane.setUserData(fxmlLoader);

                        if (column == 2) {
                            column = 0;
                            row++;
                        }
                        reviewGrid.add(reviewPane, column++, row);
                        GridPane.setMargin(reviewPane, new Insets(2));
                    }
                }
            }
        }

        averageRatingLabel.setText(calculateAverageRating(storeOrderIds));
    }

    public void getOrderIds(UUID storeId) throws IOException {
        Map<String, Object> foodQuery = Map.of("storeId", storeId);
        Response<ArrayList<Foods>> foodResponse = DaoFactory.getFoodDao().read(foodQuery);

        if (foodResponse.isSuccess()) {
            List<UUID> foodIds = foodResponse.getData().stream()
                    .map(Foods::getId)
                    .toList();

            Set<UUID> uniqueOrderIds = new HashSet<>(storeOrderIds); // Create a Set from existing order IDs to remove duplicates

            for (UUID foodId : foodIds) {
                Map<String, Object> orderFoodQuery = Map.of("foodId", foodId);
                Response<ArrayList<OrderFoods>> orderFoodsResponse = DaoFactory.getOrderFoodDao().read(orderFoodQuery);

                if (orderFoodsResponse.isSuccess()) {
                    List<UUID> orderIds = orderFoodsResponse.getData().stream()
                            .map(OrderFoods::getOrderId)
                            .distinct()
                            .toList();

                    uniqueOrderIds.addAll(orderIds);
                }
            }

            storeOrderIds.addAll(uniqueOrderIds); // Add all unique order IDs back into the list
        } else {
            System.out.println("Fails to get order Ids");
        }

        loadReview();
    }


    public void backButtonClicked(ActionEvent event) {
        Stage menuStage = (Stage) backButton.getScene().getWindow();
        menuStage.close();
    }

    public String calculateAverageRating(List<UUID> orderIds) {
        int totalRatings = 0;
        int totalReviews = 0;

        for (UUID orderId : orderIds) {
            List<Reviews> reviews = orderReviewsMap.get(orderId);

            if (reviews != null) {
                for (Reviews review : reviews) {
                    int numericalRating = convertRatingToNumber(review.getReviewRating());
                    totalRatings += numericalRating;
                    totalReviews++;
                }
            }
        }

        if (totalReviews > 0) {
            double averageRating = (double) totalRatings / totalReviews;
            return String.format("%.1f", averageRating);
        } else {
            return "N/A";
        }
    }

    private String getNameByUserId(UUID userId) {
        Map<String, Object> query = Map.of("Id", userId);
        Response<ArrayList<Users>> response = DaoFactory.getUserDao().read(query);
        if (response.isSuccess()) {
            ArrayList<Users> user = response.getData();
            return user.get(0).getName();
        } else {
            System.out.println("No username found");
        }
        return "";
    }

    private int convertRatingToNumber(Reviews.reviewRating rating) {
        // Convert the rating to a numerical value
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
}
