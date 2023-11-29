package com.oodwj_assignment;

import com.oodwj_assignment.models.OrderFoods;
import com.oodwj_assignment.models.Orders;
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
import java.util.UUID;

public class cusOrderMethodController {

    @FXML private Button closeButton;
    private ObservableList<OrderFoods> orderFoodsList;
    private UUID storeId;
    private cusMenuController menuController;

    public void closeButtonClicked(ActionEvent event) {
        closePage();
    }

    public void deliveryButtonClicked(ActionEvent event) throws IOException {
        openPaymentPage(Orders.orderType.Delivery);
        closePage();
    }

    public void dineInButtonClicked(ActionEvent event) throws IOException {
        openPaymentPage(Orders.orderType.DineIn);
        closePage();
    }

    public void takeAwayButtonClicked(ActionEvent event) throws IOException {
        openPaymentPage(Orders.orderType.TakeAway);
        closePage();
    }

    private void openPaymentPage(Orders.orderType orderType) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("cusPayment.fxml"));
        Parent paymentRoot = loader.load();
        cusPaymentController paymentController = loader.getController();
        paymentController.setOrderType(orderType);
        paymentController.setOrderFoodsData(orderFoodsList);
        paymentController.setStoreId(storeId);
        paymentController.setCusMenuController(menuController);
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

    public void setOrderFoodsList(ObservableList<OrderFoods> orderFoodsList) {
        this.orderFoodsList = orderFoodsList;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public void setCusMenuController(cusMenuController menuController){ this.menuController = menuController; }
}
