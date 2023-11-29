package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Attachments;
import com.oodwj_assignment.models.Receipts;
import com.oodwj_assignment.models.Transactions;
import com.oodwj_assignment.models.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class receiptController {

    @FXML private Button closeButton;
    @FXML private Label successLabel;
    @FXML private Label cancelLabel;
    @FXML private Label typeLabel;
    @FXML private Label receiptIdLabel;
    @FXML private Label transactionIdLabel;
    @FXML private Label dateLabel;
    @FXML private Label payerLabel;
    @FXML private Label payeeLabel;
    @FXML private Label amountLabel;
    private UUID notificationId;

    public void fetchTransactionData(UUID notificationId){
        this.notificationId = notificationId;
        Map<String, Object> query = Map.of("notificationId", notificationId);
        Response<ArrayList<Attachments>> response = DaoFactory.getAttachmentDao().read(query);
        if (!response.isSuccess()){
            return;
        }
        UUID receiptId = response.getData().get(0).getAttachedId();
        Map<String, Object> receiptQuery = Map.of("Id", receiptId);
        Response<ArrayList<Receipts>> receiptResponse = DaoFactory.getReceiptDao().read(receiptQuery);
        if (!receiptResponse.isSuccess()){
            return;
        }
        UUID transactionId = receiptResponse.getData().get(0).getTransactionId();
        Map<String, Object> transactionQuery = Map.of("Id", transactionId);
        Response<ArrayList<Transactions>> transactionResponse = DaoFactory.getTransactionDao().read(transactionQuery);
        if (!transactionResponse.isSuccess()){
            return;
        }
        Transactions transaction = transactionResponse.getData().get(0);
        Transactions.transactionStatus status = transaction.getStatus();
        UUID payerId = transaction.getPayerId();
        UUID payeeId = transaction.getPayeeId();
        Map<String, Object> payerQuery = Map.of("Id", payerId);
        Response<ArrayList<Users>> payerResponse = DaoFactory.getUserDao().read(payerQuery);
        if (!payerResponse.isSuccess()){
            return;
        }
        String payer = payerResponse.getData().get(0).getName();

        Map<String, Object> payeeQuery = Map.of("Id", payeeId);
        Response<ArrayList<Users>> payeeResponse = DaoFactory.getUserDao().read(payeeQuery);
        if (!payeeResponse.isSuccess()){
            return;
        }
        String payee = payeeResponse.getData().get(0).getName();

        if (status == Transactions.transactionStatus.Completed){
            successLabel.setVisible(true);
            successLabel.toFront();
        } else if (status == Transactions.transactionStatus.Cancelled){
            cancelLabel.setVisible(true);
            cancelLabel.toFront();
        } else {
            successLabel.setVisible(false);
            cancelLabel.setVisible(false);
        }
        typeLabel.setText(transaction.getType() + " Transaction");
        receiptIdLabel.setText(String.valueOf(receiptId));
        transactionIdLabel.setText(String.valueOf(transactionId));
        dateLabel.setText(String.valueOf(transaction.getCreatedAt()));
        payerLabel.setText(payer);
        payeeLabel.setText(payee);
        amountLabel.setText(String.valueOf(transaction.getAmount()));
    }

    public void closeButtonClicked(ActionEvent event) { closePage(); }

    private void closePage(){
        Stage methodStage = (Stage) closeButton.getScene().getWindow();
        methodStage.close();
    }
}
