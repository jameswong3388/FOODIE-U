package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Foods;
import com.oodwj_assignment.models.OrderFoods;
import com.oodwj_assignment.models.Stores;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class cusMenuController {
    
    @FXML private TableColumn<OrderFoods, String> itemColumn;
    @FXML private TableColumn<OrderFoods, String> priceColumn;
    @FXML private TableColumn<OrderFoods, Integer> quantityColumn;
    @FXML private TableColumn<OrderFoods, String> amountColumn;
    @FXML private GridPane appetizerGrid;
    @FXML private GridPane mainCourseGrid;
    @FXML private GridPane dessertGrid;
    @FXML private GridPane drinkGrid;
    @FXML private Label nameLabel;
    @FXML private Label totalLabel;
    @FXML private Button btnBack;
    private String name;
    private UUID storeId;

    public cusMenuController(String name) {
        this.name = name;
    }

    public void initialize() throws IOException {
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("foodName"));
        priceColumn.setCellValueFactory(cellData -> {double price = cellData.getValue().getFoodPrice();
            String formattedPrice = "RM " + String.format("%.2f", price);
            return new SimpleStringProperty(formattedPrice);
        });
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("foodQuantity"));
        amountColumn.setCellValueFactory(cellData -> {double amount = cellData.getValue().getAmount();
            String formattedAmount = "RM " + String.format("%.2f", amount);
            return new SimpleStringProperty(formattedAmount);
        });
        nameLabel.setText(name);

        Map<String, Object> vendorQuery = Map.of("name", name);
        Response<ArrayList<Stores>> storeResponse = DaoFactory.getStoreDao().read(vendorQuery);
        if (!storeResponse.isSuccess()) {
            System.out.println("Failed to read vendor: " + storeResponse.getMessage());
            return;
        }
        ArrayList<Stores> stores = storeResponse.getData();
        UUID storeId = stores.get(0).getId();

        Map<String, Object> query = Map.of("storeId", storeId);
        Response<ArrayList<Foods>> response = DaoFactory.getFoodDao().read(query);

        if (response.isSuccess()) {
            ArrayList<Foods> foods = response.getData();

            List<Foods> appetizer = filterByType(foods, Foods.foodType.Appetizer);
            List<Foods> mainCourse = filterByType(foods, Foods.foodType.MainCourse);
            List<Foods> dessert = filterByType(foods, Foods.foodType.Dessert);
            List<Foods> drink = filterByType(foods, Foods.foodType.Drink);

            displayFoods(appetizer, appetizerGrid);
            displayFoods(mainCourse, mainCourseGrid);
            displayFoods(dessert, dessertGrid);
            displayFoods(drink, drinkGrid);
        } else {
            System.out.println("Failed to read orders: " + response.getMessage());
        }
    }
    private List<Foods> filterByType(List<Foods> foods, Foods.foodType type) {
        return foods.stream()
                .filter(food -> type.equals(food.getFoodType()))
                .collect(Collectors.toList());
    }

    private void displayFoods(List<Foods> foods, GridPane gridPane) throws IOException {
        int row = 1;
        int column = 0;

        for (Foods food : foods) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cusFoodPane.fxml"));
            VBox foodPane = fxmlLoader.load();
            cusFoodPaneController foodPaneController = fxmlLoader.getController();
            foodPaneController.setCusMenuController(this);
            foodPaneController.populateOrderData(food);
            foodPane.setUserData(fxmlLoader);

            if (column == 3) {
                column = 0;
                row++;
            }

            gridPane.add(foodPane, column++, row);
            GridPane.setMargin(foodPane, new Insets(2));
        }
    }

    public void itemAdded(Integer quantity, String name, String price) {

    }

    public void backHome(ActionEvent event) {
        Stage menuStage = (Stage) btnBack.getScene().getWindow();
        menuStage.close();
    }

    public void placeOrder(ActionEvent event) throws IOException{
        Parent menuRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("cusPayment.fxml")));
        Stage menu = new Stage();
        menu.setTitle("Payment Page");
        menu.initStyle(StageStyle.UNDECORATED);
        menu.setScene(new Scene(menuRoot));
        menu.initModality(Modality.APPLICATION_MODAL);
        menu.showAndWait();
    }

}
