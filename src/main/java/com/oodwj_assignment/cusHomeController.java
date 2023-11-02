package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Stores;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class cusHomeController {

    @FXML private GridPane allRestaurantGrid;
    @FXML private GridPane asianRestaurantGrid;
    @FXML private GridPane chineseRestaurantGrid;
    @FXML private GridPane westernRestaurantGrid;

    public void initialize() throws IOException {
        Map<String, Object> query = Map.of();
        Response<ArrayList<Stores>> response = DaoFactory.getStoreDao().read(query);

        if (response.isSuccess()) {
            ArrayList<Stores> stores = response.getData();

            List<Stores> westernStores = filterByCategory(stores, Stores.storeCategory.Western);
            List<Stores> chineseStores = filterByCategory(stores, Stores.storeCategory.Chinese);
            List<Stores> asianStores = filterByCategory(stores, Stores.storeCategory.Asian);

            displayStores(westernStores, westernRestaurantGrid);
            displayStores(chineseStores, chineseRestaurantGrid);
            displayStores(asianStores, asianRestaurantGrid);
            displayStores(stores, allRestaurantGrid);
        } else {
            System.out.println("Failed to read Stores: " + response.getMessage());
        }
    }

    // Filter Stores by category
    private List<Stores> filterByCategory(List<Stores> stores, Stores.storeCategory category) {
        return stores.stream()
                .filter(vendor -> category.equals(vendor.getCategory()))
                .collect(Collectors.toList());
    }

    private void displayStores(List<Stores> stores, GridPane gridPane) throws IOException {
        int row = 1;
        int column = 0;

        for (Stores store : stores) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cusRestaurantPane.fxml"));
            VBox restaurantPane = fxmlLoader.load();
            cusRestaurantPaneController restaurantPaneController = fxmlLoader.getController();
            restaurantPaneController.setCusHomeController(this);
            restaurantPaneController.populateVendorData(store);
            restaurantPane.setUserData(fxmlLoader);

            if (column == 2) {
                column = 0;
                row++;
            }

            gridPane.add(restaurantPane, column++, row);
            GridPane.setMargin(restaurantPane, new Insets(2));
        }
    }

    public void setOrderInfo(String name) throws IOException {
        cusMenuController menuController = new cusMenuController(name);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cusMenu.fxml"));
        fxmlLoader.setController(menuController);
        Parent menuRoot = fxmlLoader.load();

        Stage menu = new Stage();
        menu.setTitle("Menu Page");
        menu.initStyle(StageStyle.UNDECORATED);
        menu.setScene(new Scene(menuRoot));
        menu.initModality(Modality.APPLICATION_MODAL);
        menu.showAndWait();
    }
}
