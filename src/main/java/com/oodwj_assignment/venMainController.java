package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Foods;
import com.oodwj_assignment.models.OrderFoods;
import com.oodwj_assignment.models.Orders;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class venMainController {

    @FXML private BorderPane borderpane;
    @FXML private ImageView homeIcon;
    @FXML private ImageView orderIcon;
    @FXML private ImageView menuIcon;
    @FXML private ImageView revenueIcon;
    @FXML private ImageView reviewIcon;
    @FXML private ImageView profileIcon;
    @FXML private ImageView logoutIcon;
    @FXML private ImageView notificationIcon;
    public static UUID storeId = UUID.fromString("5d6e7f8a-9b0c-1d2e-3f4a-5c6b7d8e9f0a");
    public static UUID vendorId = UUID.fromString("8a7ed604-d77a-476f-87c5-8c7e71940756");

    public void initialize() throws IOException {
        Image home = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/home-orange.png")));
        homeIcon.setImage(home);
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("venHome.fxml")));
        borderpane.setCenter(view);
    }

    public void defaultSettings() {
        // Load an image file and set it to the ImageView
        Image home = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/home-grey.png")));
        homeIcon.setImage(home);
        Image order = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/order-grey.png")));
        orderIcon.setImage(order);
        Image menu = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/menu-grey.png")));
        menuIcon.setImage(menu);
        Image revenue = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/revenue-grey.png")));
        revenueIcon.setImage(revenue);
        Image review = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/review-grey.png")));
        reviewIcon.setImage(review);
        Image profile = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/profile-grey.png")));
        profileIcon.setImage(profile);
        Image logout = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logout-grey.png")));
        logoutIcon.setImage(logout);
        Image notification = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/notification-grey.png")));
        notificationIcon.setImage(notification);
    }

    public void btnHomeClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("venHome.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image home = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/home-orange.png")));
        homeIcon.setImage(home);
    }

    public void btnOrderClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("venOrder.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image order = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/order-orange.png")));
        orderIcon.setImage(order);
    }

    public void btnMenuClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("venMenu.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image product = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/menu-orange.png")));
        menuIcon.setImage(product);
    }

    public void btnRevenueClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("venRevenue.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image wallet = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/revenue-orange.png")));
        revenueIcon.setImage(wallet);
    }

    public void btnReviewClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("venReview.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image review = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/review-orange.png")));
        reviewIcon.setImage(review);
    }

    public void btnProfileClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("venProfile.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image profile = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/profile-orange.png")));
        profileIcon.setImage(profile);
    }

    public void btnLogoutClicked(ActionEvent event) throws IOException {
        defaultSettings();
        Image logout = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logout-orange.png")));
        logoutIcon.setImage(logout);

        Stage cusStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Alert.AlertType type = Alert.AlertType.CONFIRMATION;
        Alert alert = new Alert(type, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(cusStage);
        alert.getDialogPane().setHeaderText("Are you sure you want to logout?");

        // Customize the button text
        ButtonType logoutButton = new ButtonType("Logout", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(logoutButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == logoutButton) {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("Login Page");
            loginStage.setScene(new Scene(loginRoot));
            loginStage.show();

            cusStage.close();
        }
    }

    public void btnNotificationClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("venNotification.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image notification = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/notification-orange.png")));
        notificationIcon.setImage(notification);
    }

    public static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static List<UUID> getOrderIds() {
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
        } else {
            showAlert("Error", "Failed to retrieve foodIds for the store: " + foodResponse.getMessage());
        }

        return new ArrayList<>(storeOrderIds);
    }

}