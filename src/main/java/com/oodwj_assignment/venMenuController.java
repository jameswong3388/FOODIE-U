package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Foods;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.util.*;

public class venMenuController {

    @FXML private TableView<Foods> menuTableView;
    @FXML private TableColumn<Foods, UUID> itemIdColumn;
    @FXML private TableColumn<Foods, String> itemNameColumn;
    @FXML private TableColumn<Foods, Foods.foodType> itemTypeColumn;
    @FXML private TableColumn<Foods, String> itemPriceColumn;
    @FXML private TableColumn<Foods, LocalDateTime> createdDateColumn;
    @FXML private TextField addItemName;
    @FXML private TextField addItemPrice;
    @FXML private ComboBox<Foods.foodType> addItemType;
    @FXML private ComboBox<UUID> modifyItemId;
    @FXML private TextField modifyItemName;
    @FXML private TextField modifyItemPrice;
    @FXML private ComboBox<Foods.foodType> modifyItemType;
    private UUID storeId;

    public void initialize(){
        storeId = venMainController.storeId;
        itemIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        itemNameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFoodName()));
        itemPriceColumn.setCellValueFactory(cellData -> {
            double price = cellData.getValue().getFoodPrice();
            String formattedPrice = String.format("RM %.2f", price);
            return new SimpleStringProperty(formattedPrice);
        });
        itemTypeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFoodType()));
        createdDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCreatedAt()));

        ObservableList<Foods.foodType> foodTypesList = FXCollections.observableArrayList(Foods.foodType.values());
        addItemType.setItems(foodTypesList);
        modifyItemType.setItems(foodTypesList);
        loadAllOrders();
    }

    private void loadAllOrders() {
        Map<String, Object> query = Map.of("storeId", storeId);
        Response<ArrayList<Foods>> response = DaoFactory.getFoodDao().read(query);

        if (response.isSuccess()) {
            ArrayList<Foods> foods = response.getData();
            menuTableView.getItems().setAll(foods);
            modifyItemId.getItems().clear();
            modifyItemId.getItems().addAll(foods.stream().map(Foods::getId).toList());
        } else {
            System.out.println("Failed to read orders: " + response.getMessage());
        }
    }

    public void addButtonClicked(ActionEvent event) {
        String name = addItemName.getText();
        Foods.foodType type = addItemType.getValue();
        String unformattedPrice = addItemPrice.getText();

        if (name.isEmpty() || type == null) {
            venMainController.showAlert("Missing Information", "Please enter values for all fields.");
            return;
        }

        Double price = validateAndFormatPrice(unformattedPrice);

        if (price == null) {
            venMainController.showAlert("Invalid Price", "Invalid price format. Please use 'RM 7.00' or '7.00' format.");
            return;
        }

        Foods food = new Foods(UUID.randomUUID(), storeId, name, type, price, LocalDateTime.now(), LocalDateTime.now());
        Response<UUID> newFood = DaoFactory.getFoodDao().create(food);

        if (newFood.isSuccess()) {
            venMainController.showAlert("Success", "Food added successfully.");
            loadAllOrders();
            clearFields();
        } else {
            venMainController.showAlert("Error", "Failed to add food: " + newFood.getMessage());
        }
    }

    public void clearButtonClicked(ActionEvent event) { clearFields(); }

    public void updateButtonClicked(ActionEvent event) {
        UUID foodId = modifyItemId.getValue();
        String name = modifyItemName.getText();
        Foods.foodType type = modifyItemType.getValue();
        String unformattedPrice = modifyItemPrice.getText();

        if (name.isEmpty() || type == null) {
            venMainController.showAlert("Missing Information", "Please enter values for all fields.");
            return;
        }

        Double price = validateAndFormatPrice(unformattedPrice);

        if (price == null) {
            venMainController.showAlert("Invalid Price", "Invalid price format. Please use 'RM 7.00' or '7.00' format.");
            return;
        }

        Map<String, Object> query = Map.of("Id", foodId);
        Map<String, Object> newValue = Map.of("foodName", name, "foodType", type, "foodPrice", price);
        Response<Void> response = DaoFactory.getFoodDao().update(query, newValue);

        if (response.isSuccess()) {
            loadAllOrders();
            clearFields();
            venMainController.showAlert("Success", "Food item updated successfully.");
        } else {
            venMainController.showAlert("Update Error", "Failed to update food item: " + response.getMessage());
        }
    }

    public void deleteButtonClicked(ActionEvent event) {
        UUID foodId = modifyItemId.getValue();

        if (foodId != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Delete Food Item");
            confirmation.setContentText("Are you sure you want to delete this food item?");
            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Map<String, Object> query = Map.of("Id", foodId);
                Response<Void> response = DaoFactory.getFoodDao().deleteOne(query);

                if (response.isSuccess()) {
                    venMainController.showAlert("Success", "Food item deleted successfully.");
                    clearFields();
                    loadAllOrders();
                } else {
                    venMainController.showAlert("Error", "Failed to delete food item: " + response.getMessage());
                }
            }
        } else {
            venMainController.showAlert("Error", "Please select a food item to delete.");
        }
    }

    public void itemIdSelected(ActionEvent event) {
        UUID selectedFoodId = modifyItemId.getValue();
        if (selectedFoodId != null) {
            fetchFoodDetails(selectedFoodId);
        }
    }

    private void fetchFoodDetails(UUID foodId) {
        Map<String, Object> query = Map.of("Id", foodId);
        Response<ArrayList<Foods>> response = DaoFactory.getFoodDao().read(query);

        if (response.isSuccess()) {
            ArrayList<Foods> food = response.getData();
            modifyItemName.setText(food.get(0).getFoodName());
            modifyItemType.setValue(food.get(0).getFoodType());
            modifyItemPrice.setText(String.format("%.2f", food.get(0).getFoodPrice()));
        } else {
            venMainController.showAlert("Error", "Failed to fetch food details: " + response.getMessage());
        }
    }

    private Double validateAndFormatPrice(String input) {
        String formattedInput = input.replaceAll("\\s", "").toLowerCase();
        String regexRM = "^(rm)?(\\d+(\\.\\d{0,2})?)$";
        String regexRMWithSpace = "^(rm\\s)?(\\d+(\\.\\d{0,2})?)$";

        if (formattedInput.matches(regexRM) || formattedInput.matches(regexRMWithSpace)) {
            formattedInput = formattedInput.replaceAll("[^0-9.]+", "");
            if (formattedInput.isEmpty()) {
                return 0.00; // Handle invalid inputs as 0.00
            }
            return Double.parseDouble(formattedInput);
        } else {
            return null; // Invalid input, can't be formatted to a Double
        }
    }

    private void clearFields() {
        addItemName.clear();
        addItemType.setValue(null);
        addItemPrice.clear();
        modifyItemId.setValue(null);
        modifyItemName.clear();
        modifyItemType.setValue(null);
        modifyItemPrice.clear();
    }
}
