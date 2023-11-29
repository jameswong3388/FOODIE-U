package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Foods;
import com.oodwj_assignment.models.OrderFoods;
import com.oodwj_assignment.models.Stores;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class cusMenuController {

    @FXML private TableView<OrderFoods> orderFoodsTableView;
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
    @FXML private Button backButton;
    private String name;
    private UUID storeId;
    private ArrayList<OrderFoods> orderFoods;
    private cusMenuController menuController;

    public cusMenuController(String name, ArrayList<OrderFoods> orderFoods) {
        this.name = name;
        this.orderFoods = orderFoods;
    }

    public void initialize() throws IOException {
        itemColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFoodName()));
        priceColumn.setCellValueFactory(cellData -> {double price = cellData.getValue().getFoodPrice();
            String formattedPrice = "RM " + String.format("%.2f", price);
            return new SimpleStringProperty(formattedPrice);
        });
        quantityColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFoodQuantity()));
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
        storeId = stores.get(0).getId();

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

    public void itemAdded(Integer quantity, String foodName, Double price) {
        // Check if the row is existed
        for (OrderFoods orderFood : orderFoodsTableView.getItems()) {
            if (orderFood.getFoodName().equals(foodName)) {
                if (quantity == 0) {
                    orderFoodsTableView.getItems().remove(orderFood);
                } else {
                    orderFood.setFoodQuantity(quantity);
                    double newAmount = price * quantity;
                    orderFood.setAmount(newAmount);
                    orderFoodsTableView.refresh();
                }
                updateTotalLabel();
                return;
            }
        }

        if (quantity > 0) {
            Map<String, Object> query = Map.of("storeId", storeId, "foodName", foodName);
            UUID foodId = DaoFactory.getFoodDao().read(query).getData().get(0).getId();

            // If the row doesn't exist, create a new one
            OrderFoods newOrderFood = new OrderFoods(null, foodId, null, foodName, price, quantity, LocalDateTime.now(), LocalDateTime.now());
            double newAmount = price * quantity;
            newOrderFood.setAmount(newAmount);
            orderFoodsTableView.getItems().add(newOrderFood);
            updateTotalLabel();
        }
    }

    private void updateTotalLabel() {
        double total = orderFoodsTableView.getItems().stream()
                .mapToDouble(OrderFoods::getAmount)
                .sum();
        DecimalFormat currencyFormat = new DecimalFormat("RM #,##0.00");
        totalLabel.setText(currencyFormat.format(total));
    }

    public void backButtonClicked(ActionEvent event) {
        Stage menuStage = (Stage) backButton.getScene().getWindow();
        menuStage.close();
    }

    public void checkoutButtonClicked(ActionEvent event) throws IOException{
        if (!orderFoodsTableView.getItems().isEmpty()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cusOrderMethod.fxml"));
            Parent methodRoot = loader.load();
            cusOrderMethodController orderMethodController = loader.getController();
            orderMethodController.setOrderFoodsList(orderFoodsTableView.getItems());
            orderMethodController.setStoreId(storeId);
            orderMethodController.setCusMenuController(menuController);
            Stage method = new Stage();
            method.setTitle("Order Method Page");
            method.initStyle(StageStyle.UNDECORATED);
            method.setScene(new Scene(methodRoot));
            method.initModality(Modality.APPLICATION_MODAL);
            method.showAndWait();
        } else {
            cusMainController.showAlert("Checkout Fails", "No food is selected.");
        }
    }

    public void reviewButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("cusRestaurantReview.fxml"));
        Parent reviewRoot = loader.load();
        cusRestaurantReviewController restaurantReviewController = loader.getController();
        restaurantReviewController.getOrderIds(storeId);
        Stage review = new Stage();
        review.setTitle("Restaurant Review Page");
        review.initStyle(StageStyle.UNDECORATED);
        review.setScene(new Scene(reviewRoot));
        review.initModality(Modality.APPLICATION_MODAL);
        review.showAndWait();
    }

    public void cancelButtonClicked(ActionEvent event) {
        orderFoodsTableView.getItems().clear();
        updateTotalLabel();

        // Reset all spinners in each food pane
        setSpinnersInGrid(appetizerGrid, null, 0);
        setSpinnersInGrid(mainCourseGrid, null, 0);
        setSpinnersInGrid(dessertGrid, null,0);
        setSpinnersInGrid(drinkGrid, null,0);
    }

    private void setSpinnersInGrid(GridPane gridPane, UUID foodId, int value) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof VBox) {
                FXMLLoader loader = (FXMLLoader) node.getUserData();
                cusFoodPaneController controller = loader.getController();
                if (foodId == null || controller.getFoodId().equals(foodId)) {
                    controller.setSpinnerValue(value);
                    if (foodId == null) {
                        continue; // Continue resetting all spinners
                    }
                    break; // Break if a specific food ID is matched
                }
            }
        }
    }

    public void setOrderItems() {
        for (OrderFoods orderFood : orderFoods) {
            UUID foodId = orderFood.getFoodId();
            Map<String, Object> query = Map.of("Id", foodId);
            Response<ArrayList<Foods>> response = DaoFactory.getFoodDao().read(query);

            if (response.isSuccess() && !response.getData().isEmpty()) {
                Foods food = response.getData().get(0);
                int quantity = orderFood.getFoodQuantity();

                setSpinnersInGrid(appetizerGrid, foodId, quantity);
                setSpinnersInGrid(mainCourseGrid, foodId, quantity);
                setSpinnersInGrid(dessertGrid, foodId, quantity);
                setSpinnersInGrid(drinkGrid, foodId, quantity);
            } else {
                cusMainController.showAlert("Item Unavailable", "The item " + orderFood.getFoodName() + " is no longer available.");
            }
        }

        updateTotalLabel();
    }

    public void setCusMenuController(cusMenuController menuController){ this.menuController = menuController; }

}
