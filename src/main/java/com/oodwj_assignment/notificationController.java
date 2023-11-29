package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Attachments;
import com.oodwj_assignment.models.Notifications;
import com.oodwj_assignment.models.Receipts;
import com.oodwj_assignment.models.Transactions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class notificationController {

    @FXML private GridPane notificationGrid;
    private UUID userId;
    private List<Notifications> allNotifications = new ArrayList<>();

    public void fetchAndFilterNotifications(UUID userId, Notifications.notificationStatus filterStatus) throws IOException {
        this.userId = userId;
        Map<String, Object> query = Map.of("userId", userId);
        Response<ArrayList<Notifications>> response = DaoFactory.getNotificationDao().read(query);

        if (response.isSuccess()){
            allNotifications = response.getData();
        }

        // Filter and populate the grid based on the given filter status
        List<Notifications> filteredNotifications = allNotifications;
        if (filterStatus != null) {
            filteredNotifications = allNotifications.stream()
                    .filter(notification -> notification.getStatus() == filterStatus)
                    .collect(Collectors.toList());
        }
        populateGrid(filteredNotifications);
    }

    private void populateGrid(List<Notifications> notifications) throws IOException {
        notificationGrid.getChildren().clear(); // Clear existing panes
        int row = 0;
        for (Notifications notification : notifications) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("notificationPane.fxml"));
            VBox notificationPane = fxmlLoader.load();
            notificationPaneController notificationPaneController = fxmlLoader.getController();
            notificationPaneController.setNotificationController(this);
            notificationPaneController.populateNotification(notification);
            notificationPaneController.setUserId(userId);
            notificationPane.setUserData(fxmlLoader);
            notificationGrid.add(notificationPane, 0, row);
            row++;
            GridPane.setMargin(notificationPane, new Insets(2));
        }
    }

    public void allToggleClicked(ActionEvent event) throws IOException {
        fetchAndFilterNotifications(userId, null); // null filter fetches all notifications
    }

    public void unreadToggleClicked(ActionEvent event) throws IOException {
        fetchAndFilterNotifications(userId, Notifications.notificationStatus.Unread);
    }

    public void generateReceiptAndSendNotification(UUID userId, UUID transactionId) {
        // Create a new receipt
        Receipts newReceipt = new Receipts(UUID.randomUUID(), userId, transactionId, LocalDateTime.now(), LocalDateTime.now());
        Response<UUID> receiptResponse = DaoFactory.getReceiptDao().create(newReceipt);

        if (receiptResponse.isSuccess()) {
            // Show a success message
            admMainController.showAlert("Success", "A receipt has been generated.");

            UUID receiptId = receiptResponse.getData();

            // Generate a notification for the receipt
            String notificationMessage = "Your receipt is ready. Click to view.";

            // Send the notification and get the notificationId
            UUID notificationId = sendNotification(userId, notificationMessage, Notifications.notificationType.Receipt);

            // Create an attachment with the obtained notificationId and receiptId
            createAttachment(notificationId, receiptId);
        } else {
            // Show an error message if receipt creation fails
            admMainController.showAlert("Error", "Failed to generate a receipt: " + receiptResponse.getMessage());
        }
    }

    public UUID sendNotification(UUID userId, String message, Notifications.notificationType type) {
        // Create a new notification
        Notifications newNotification = new Notifications(UUID.randomUUID(), message, Notifications.notificationStatus.Unread, type, userId, LocalDateTime.now(), LocalDateTime.now());
        Response<UUID> notificationResponse = DaoFactory.getNotificationDao().create(newNotification);

        if (notificationResponse.isSuccess()) {
            // Show a success message
            admMainController.showAlert("Success", "Notification has been sent.");
            return notificationResponse.getData(); // Return the notificationId
        } else {
            // Show an error message if notification creation fails
            admMainController.showAlert("Error", "Failed to create a notification: " + notificationResponse.getMessage());
            return null;
        }
    }

    private void createAttachment(UUID notificationId, UUID receiptId) {
        // Create a new attachment
        Attachments newAttachment = new Attachments(UUID.randomUUID(), notificationId, receiptId, LocalDateTime.now(), LocalDateTime.now());
        DaoFactory.getAttachmentDao().create(newAttachment);
    }

}
