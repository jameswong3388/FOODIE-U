package com.oodwj_assignment;


import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

public class cusReviewController {

    @FXML private GridPane historyGrid;
    @FXML private ComboBox<String> modelComboBox;
    @FXML private Label modelId;
    @FXML private Label time;
    @FXML private Label cost;
    @FXML private Label modelIdLabel;
    @FXML private Label timeLabel;
    @FXML private Label costLabel;
    @FXML private Button submitButton;
    @FXML private Button clearButton;
    @FXML private Button firstRatingButton;
    @FXML private Button secondRatingButton;
    @FXML private Button thirdRatingButton;
    @FXML private Button fourthRatingButton;
    @FXML private Button fifthRatingButton;
    @FXML private ImageView firstRatingIcon;
    @FXML private ImageView secondRatingIcon;
    @FXML private ImageView thirdRatingIcon;
    @FXML private ImageView fourthRatingIcon;
    @FXML private ImageView fifthRatingIcon;
    @FXML private ImageView modelProfile;
    @FXML private TextArea contentTextArea;
    @FXML private Pane reviewPane;
    private UUID orderId;
    private UUID storeId;
    private UUID userId = cusMainController.userId;
    private Reviews.reviewRating rating;
    private cusHistoryPaneController historyPaneController;

    public void initialize() throws IOException {
        reviewPane.setVisible(false);
        // Create a list to hold the order panes
        List<VBox> historyPanes = new ArrayList<>();

        Map<String, Object> query = Map.of("userId", userId, "status", Orders.orderStatus.Completed);
        //Might need to add task status for delivery order
        Response<ArrayList<Orders>> response = DaoFactory.getOrderDao().read(query);

        if (response.isSuccess()) {
            ArrayList<Orders> completedOrders = response.getData();

            for (Orders order : completedOrders) {
                // Decide whether the order is completed based on its type and status
                if (isCompletedOrder(order)) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cusHistoryPane.fxml"));
                    VBox historyPane = fxmlLoader.load();
                    cusHistoryPaneController historyPaneController = fxmlLoader.getController();
                    this.historyPaneController = historyPaneController;
                    historyPaneController.setCusReviewController(this);
                    historyPaneController.populateOrderData(order);
                    historyPane.setUserData(fxmlLoader);
                    historyPanes.add(historyPane); // Add the history pane to the list
                }
            }
        }

        // Add the order panes to the grid
        int row = 0;
        for (VBox historyPane : historyPanes) {
            historyGrid.add(historyPane, 0, row);
            row++;
            GridPane.setMargin(historyPane, new Insets(2));
        }

        modelComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            updateLabels(newValue);
        });
        contentTextArea.setWrapText(true);
    }

    public void setReviewInfo(UUID orderId) {
        this.orderId = orderId;
        this.storeId = historyPaneController.getStoreId(orderId);
        reviewPane.setVisible(true);

        Map<String, Object> orderQuery = Map.of("Id", orderId);
        Response<ArrayList<Orders>> orderResponse = DaoFactory.getOrderDao().read(orderQuery);

        if (orderResponse.isSuccess()) {
            ArrayList<Orders> ordersInfo = orderResponse.getData();
            Orders order = ordersInfo.get(0);

            // Update ComboBox items based on order type
            if (order.getType().equals(Orders.orderType.Delivery)) {
                modelComboBox.getItems().setAll("Order", "Task");
            } else {
                modelComboBox.getItems().setAll("Order");
            }
            modelComboBox.getSelectionModel().selectFirst();
            updateLabels(modelComboBox.getSelectionModel().getSelectedItem());
        } else {
            System.out.println("Failed to fetch review info");
        }
    }

    public void submitButtonClicked(ActionEvent event) {
        String content = contentTextArea.getText();
        UUID modelId = UUID.fromString(modelIdLabel.getText());
        // Check if the rating is not provided
        if (rating == null) {
            cusMainController.showAlert("Rating Needed", "Please provide a rating to continue.");
            return;
        }

        // Check if the review content is empty
        if (content.isEmpty()) {
            cusMainController.showAlert("Review Content Required", "Please share your thoughts in the review field.");
            return;
        }
        
        String model = null;
        if (modelComboBox.getSelectionModel().getSelectedItem().equals("Order")){
            model = "/models/Order";
        } else if (modelComboBox.getSelectionModel().getSelectedItem().equals("Task")) {
            model = "/models/Task";
        } 
        
        Reviews review = new Reviews(UUID.randomUUID(), model, modelId, userId, content, rating, LocalDateTime.now(), LocalDateTime.now());
        Response<UUID> newReview = DaoFactory.getReviewDao().create(review);

        if (newReview.isSuccess()) {
            cusMainController.showAlert("Success", "Your review submitted successfully.");
            notificationController notificationController = new notificationController();
            notificationController.sendNotification(modelId, "Customer has write a review to you. Do check it out!", Notifications.notificationType.Information);
            updateLabels(modelComboBox.getSelectionModel().getSelectedItem());
        } else {
            cusMainController.showAlert("Error", "Failed to submit review: " + newReview.getMessage());
            clearFields();
        }
    }

    public void clearButtonClicked(ActionEvent event) {
        clearFields();
    }

    public void firstRatingButtonClicked(ActionEvent event) {
        updateRatingIcons(1);
        rating = Reviews.reviewRating.One;
    }

    public void secondRatingButtonClicked(ActionEvent event) {
        updateRatingIcons(2);
        rating = Reviews.reviewRating.Two;
    }

    public void thirdRatingButtonClicked(ActionEvent event) {
        updateRatingIcons(3);
        rating = Reviews.reviewRating.Three;
    }

    public void fourthRatingButtonClicked(ActionEvent event) {
        updateRatingIcons(4);
        rating = Reviews.reviewRating.Four;
    }

    public void fifthRatingButtonClicked(ActionEvent event) {
        updateRatingIcons(5);
        rating = Reviews.reviewRating.Five;
    }

    private void clearFields(){
        rating = null;
        contentTextArea.clear();
        firstRatingIcon.setImage(new Image(getClass().getResourceAsStream("/images/rating-null.png")));
        secondRatingIcon.setImage(new Image(getClass().getResourceAsStream("/images/rating-null.png")));
        thirdRatingIcon.setImage(new Image(getClass().getResourceAsStream("/images/rating-null.png")));
        fourthRatingIcon.setImage(new Image(getClass().getResourceAsStream("/images/rating-null.png")));
        fifthRatingIcon.setImage(new Image(getClass().getResourceAsStream("/images/rating-null.png")));
    }

    private boolean isCompletedOrder(Orders order) {
        if (order.getType() == Orders.orderType.Delivery) {
            Map<String, Object> taskQuery = Map.of("orderId", order.getId());
            Response<ArrayList<Tasks>> taskResponse = DaoFactory.getTaskDao().read(taskQuery);

            if (taskResponse.isSuccess() && !taskResponse.getData().isEmpty()) {
                Tasks task = taskResponse.getData().get(0);
                return order.getStatus() == Orders.orderStatus.Completed && task.getStatus() == Tasks.taskStatus.Delivered;
            }
            return false; // If there's no task found or any error, consider it as not completed
        }
        return order.getStatus() == Orders.orderStatus.Completed; // For non-delivery orders, completion is based on order status alone
    }

    private void updateLabels(String selection) {
        clearFields();
        if (selection != null){
            if (selection.equals("Order")) {
                modelId.setText("Order ID:");
                time.setText("Completion Time:");
                cost.setText("Order Cost:");
                // Fetch order information
                Map<String, Object> orderQuery = Map.of("Id", orderId);
                Response<ArrayList<Orders>> orderResponse = DaoFactory.getOrderDao().read(orderQuery);

                if (orderResponse.isSuccess()) {
                    ArrayList<Orders> ordersInfo = orderResponse.getData();

                    DecimalFormat currencyFormat = new DecimalFormat("RM #,##0.00");
                    modelIdLabel.setText(orderId.toString());
                    timeLabel.setText(ordersInfo.get(0).getUpdatedAt().withNano(0).toString());
                    costLabel.setText(currencyFormat.format(ordersInfo.get(0).getTotalPrice()));
                    setModelProfile(storeId, ProfileType.Store);
                } else {
                    reviewPane.setVisible(false);
                    cusMainController.showAlert("Error", "Failed to read review: " + orderResponse.getMessage());
                }
            } else if (selection.equals("Task")) {
                modelId.setText("Task ID:");
                time.setText("Delivered Time:");
                cost.setText("Delivery Fee:");
                // Fetch runner information
                Map<String, Object> taskQuery = Map.of("orderId", orderId);
                Response<ArrayList<Tasks>> taskResponse = DaoFactory.getTaskDao().read(taskQuery);

                if (taskResponse.isSuccess()) {
                    ArrayList<Tasks> tasksInfo = taskResponse.getData();

                    DecimalFormat currencyFormat = new DecimalFormat("RM #,##0.00");
                    modelIdLabel.setText(tasksInfo.get(0).getId().toString());
                    timeLabel.setText(tasksInfo.get(0).getUpdatedAt().withNano(0).toString());
                    costLabel.setText(currencyFormat.format(tasksInfo.get(0).getDeliveryFee()));
                    setModelProfile(tasksInfo.get(0).getRunnerId(), ProfileType.Runner);
                } else {
                    reviewPane.setVisible(false);
                    cusMainController.showAlert("Error", "Failed to read review: " + taskResponse.getMessage());
                }
            }
        }
        loadPreviousReview(UUID.fromString(modelIdLabel.getText()), selection);
    }

    private void loadPreviousReview(UUID modelId, String modelType) {
        Map<String, Object> query = Map.of("modelUUID", modelId, "model", "/models/" + modelType);
        Response<ArrayList<Reviews>> reviewResponse = DaoFactory.getReviewDao().read(query);

        if (reviewResponse.isSuccess()) {
            Reviews review = reviewResponse.getData().get(0);
            String content = review.getReviewContent();
            Reviews.reviewRating rating = review.getReviewRating();
            contentTextArea.setText(content);

            // Create a map to associate ratings with images
            Map<Reviews.reviewRating, ImageView> ratingImages = new HashMap<>();
            ratingImages.put(Reviews.reviewRating.One, firstRatingIcon);
            ratingImages.put(Reviews.reviewRating.Two, secondRatingIcon);
            ratingImages.put(Reviews.reviewRating.Three, thirdRatingIcon);
            ratingImages.put(Reviews.reviewRating.Four, fourthRatingIcon);
            ratingImages.put(Reviews.reviewRating.Five, fifthRatingIcon);

            // Set rating images
            for (Map.Entry<Reviews.reviewRating, ImageView> entry : ratingImages.entrySet()) {
                Reviews.reviewRating ratingValue = entry.getKey();
                ImageView ratingImage = entry.getValue();

                if (ratingValue.ordinal() <= rating.ordinal()) {
                    ratingImage.setImage(new Image(getClass().getResourceAsStream("/images/rating.png")));
                } else {
                    ratingImage.setImage(new Image(getClass().getResourceAsStream("/images/rating-null.png")));
                }
            }
            disableReviewEditing();
        } else {
            enableReviewEditing();
        }
    }

    private void disableReviewEditing() {
        contentTextArea.setEditable(false);
        firstRatingButton.setDisable(true);
        secondRatingButton.setDisable(true);
        thirdRatingButton.setDisable(true);
        fourthRatingButton.setDisable(true);
        fifthRatingButton.setDisable(true);
        submitButton.setDisable(true);
        clearButton.setDisable(true);
    }

    private void enableReviewEditing() {
        contentTextArea.setEditable(true);
        firstRatingButton.setDisable(false);
        secondRatingButton.setDisable(false);
        thirdRatingButton.setDisable(false);
        fourthRatingButton.setDisable(false);
        fifthRatingButton.setDisable(false);
        submitButton.setDisable(false);
        clearButton.setDisable(false);
    }

    private void updateRatingIcons(int rating) {
        Image ratedImage = new Image(getClass().getResourceAsStream("/images/rating.png"));
        Image unratedImage = new Image(getClass().getResourceAsStream("/images/rating-null.png"));

        ImageView[] ratingIcons = {firstRatingIcon, secondRatingIcon, thirdRatingIcon, fourthRatingIcon, fifthRatingIcon};

        for (int i = 0; i < ratingIcons.length; i++) {
            if (i < rating) {
                ratingIcons[i].setImage(ratedImage);
            } else {
                ratingIcons[i].setImage(unratedImage);
            }
        }
    }

    private void setModelProfile(UUID modelId, ProfileType profileType) {
        if (profileType == ProfileType.Store) {
            // Fetch and set the store profile image
            Map<String, Object> query = Map.of("Id", modelId);
            Response<ArrayList<Stores>> storeResponse = DaoFactory.getStoreDao().read(query);

            if(storeResponse.isSuccess()){
                UUID vendorId = storeResponse.getData().get(0).getVendorId();
                Response<String> mediaResponse = DaoFactory.getUserDao().getFirstMedia(vendorId);

                if (mediaResponse.isSuccess()) {
                    File imageFile = new File(mediaResponse.getData());
                    if (imageFile.exists()) {
                        Image image = new Image(imageFile.toURI().toString());
                        modelProfile.setImage(image);
                    } else {
                        System.err.println("Image file not found: " + imageFile.getAbsolutePath());
                    }
                }
            }
        } else if (profileType == ProfileType.Runner) {
            // Fetch and set the runner profile image
            Response<String> mediaResponse = DaoFactory.getUserDao().getFirstMedia(modelId);

            if (mediaResponse.isSuccess()) {
                File imageFile = new File(mediaResponse.getData());
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    modelProfile.setImage(image);
                } else {
                    System.err.println("Image file not found: " + imageFile.getAbsolutePath());
                }
            }
        }
    }

    private enum ProfileType {
        Store,
        Runner
    }
}

