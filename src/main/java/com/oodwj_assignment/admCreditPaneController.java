package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class admCreditPaneController {

    @FXML private Label transactionIdLabel;
    @FXML private Label nameLabel;
    @FXML private Label amountLabel;
    @FXML private Label dateLabel;
    private admCreditController admCreditController;
    private UUID transactionId;
    private Transactions transaction;
    private notificationController notificationController = new notificationController();
    public void populateTopUpData(Transactions transaction){
        this.transaction = transaction;
        transactionId = transaction.getId();
        transactionIdLabel.setText(String.valueOf(transactionId));
        amountLabel.setText(String.format("RM %.2f", transaction.getAmount()));
        dateLabel.setText(String.valueOf(transaction.getCreatedAt().withNano(0)));

        Response<ArrayList<Users>> response = DaoFactory.getUserDao().read(Map.of("Id", transaction.getPayeeId()));
        if (response.isSuccess()){
            nameLabel.setText(response.getData().get(0).getName());
        }
    }

    public void approveButtonClicked(ActionEvent event) throws IOException {
        updateTransactionStatus(Transactions.transactionStatus.Completed, "Top Up request has been approved.");
        UUID userId = transaction.getPayeeId();
        Double amount = transaction.getAmount();
        admCreditController.topUpToWallet(userId, amount);
        admCreditController.removeCreditPane(transactionId);
        notificationController.generateReceiptAndSendNotification(userId, transactionId);
    }

    public void rejectButtonClicked(ActionEvent event) throws IOException {
        updateTransactionStatus(Transactions.transactionStatus.Cancelled, "Top Up request has been rejected.");
        UUID userId = transaction.getPayeeId();
        admCreditController.removeCreditPane(transactionId);
        notificationController.sendNotification(userId, "Top up request has been rejected by admin.", Notifications.notificationType.Information);
    }

    private void updateTransactionStatus(Transactions.transactionStatus newStatus, String message) throws IOException {
        // Update the transaction status
        Map<String, Object> query = Map.of("Id", transactionId);
        Map<String, Object> newValue = Map.of("status", newStatus, "updatedAt", LocalDateTime.now());

        Response<Void> response = DaoFactory.getTransactionDao().update(query, newValue);
        if (response.isSuccess()) {
            admMainController.showAlert("Success", message);
        } else {
            admMainController.showAlert("Update Error", "Failed to update transaction status: " + response.getMessage());
        }
    }

    public void setAdmCreditController(admCreditController controller) { this.admCreditController = controller; }

    public UUID getTransactionId() { return transactionId; }
}
