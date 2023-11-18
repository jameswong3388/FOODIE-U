package com.oodwj_assignment;

import com.oodwj_assignment.models.OrderFoods;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class cusOrderMethodController {

    @FXML private Button closeButton;
    private ObservableList<OrderFoods> orderFoodsList;

    public void closeButtonClicked(ActionEvent event) {
        closePage();
    }

    public void deliveryButtonClicked(ActionEvent event) throws IOException {
        openPaymentPage(true);
        closePage();
    }

    public void dineInButtonClicked(ActionEvent event) throws IOException {
        openPaymentPage(false);
        closePage();
    }

    public void takeAwayButtonClicked(ActionEvent event) throws IOException {
        openPaymentPage(false);
        closePage();
    }

    private void openPaymentPage(boolean isDelivery) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("cusPayment.fxml"));
        Parent paymentRoot = loader.load();
        cusPaymentController paymentController = loader.getController();
        paymentController.setIsDelivery(isDelivery);
        paymentController.setOrderFoodsData(orderFoodsList);
        Stage payment = new Stage();
        payment.setTitle("Payment Page");
        payment.initStyle(StageStyle.UNDECORATED);
        payment.setScene(new Scene(paymentRoot));
        payment.initModality(Modality.APPLICATION_MODAL);
        payment.showAndWait();
    }

    private void closePage(){
        Stage methodStage = (Stage) closeButton.getScene().getWindow();
        methodStage.close();
    }

    public void getOrderFoodsList(ObservableList<OrderFoods> orderFoodsList) {
        this.orderFoodsList = orderFoodsList;
    }
}
