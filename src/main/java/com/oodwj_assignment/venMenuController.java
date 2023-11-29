package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Foods;
import com.oodwj_assignment.models.Medias;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
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
    @FXML private TextField addImage;
    @FXML private TextField modifyImage;
    @FXML private ComboBox<Foods.foodType> addItemType;
    @FXML private ComboBox<UUID> modifyItemId;
    @FXML private TextField modifyItemName;
    @FXML private TextField modifyItemPrice;
    @FXML private ComboBox<Foods.foodType> modifyItemType;
    private UUID storeId;
    private venMenuController venMenuController;
    private File selectedFile;
    @FXML private ImageView test;

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
        }
    }

    public void addImageClicked() throws IOException { browseImage(addImage); }

    public void modifyImageClicked(){
        browseImage(modifyImage);
    }

    public void browseImage(TextField textField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image File");

        // Set the file extension filters to limit selection to image files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile == null){
            return;
        }

        if (!isImageFile(selectedFile.getName())) {
            venMainController.showAlert("Upload Error", "Invalid image file format");
            return;
        }
        textField.setText(selectedFile.getAbsolutePath());
    }

    private boolean isImageFile(String filename) {
        String extension = filename.toLowerCase();
        return extension.endsWith(".png") || extension.endsWith(".jpg")
                || extension.endsWith(".jpeg") || extension.endsWith(".gif") || extension.endsWith(".bmp");
    }

    public void addButtonClicked(ActionEvent event) {
        String name = addItemName.getText();
        Foods.foodType type = addItemType.getValue();
        String unformattedPrice = addItemPrice.getText();
        String image = addImage.getText();

        if (name.isEmpty() || type == null || unformattedPrice.isEmpty() || image.isEmpty()) {
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
            UUID newFoodId = newFood.getData();
            if (saveImage(newFoodId)){
                venMainController.showAlert("Success", "New food added successfully.");
                loadAllOrders();
                clearFields();
            } else {
                venMainController.showAlert("Media Error", "Failed to upload food image.");
            }
        } else {
            venMainController.showAlert("Error", "Failed to add food: " + newFood.getMessage());
        }
    }

    public boolean saveImage(UUID foodId) {
        if (selectedFile != null) {
            String extension = DaoFactory.getMediaDao().getExtensionByStringHandling(selectedFile.getName());

            Foods food = DaoFactory.getFoodDao().read(Map.of("Id", foodId)).getData().get(0);
            Medias media = new Medias(null, null, food.getId(), "food_pics", selectedFile.getName(),
                    extension, "menus", null, null, null, LocalDateTime.now(), LocalDateTime.now()
            );

            //for the initial upload when there is no existing data
            Response<Void> addResponse = DaoFactory.getFoodDao().addMedia(selectedFile, media);
            if (addResponse.isSuccess()){
                //remove existed food pic
                Response<Void> removeResponse = DaoFactory.getFoodDao().removeMedia(foodId);
                if (removeResponse.isSuccess()) {
                    DaoFactory.getFoodDao().addMedia(selectedFile, media);
                    return true;
                }
            }
        }

        return false;
    }

    public void clearButtonClicked(ActionEvent event) { clearFields(); }

    public void updateButtonClicked(ActionEvent event) {
        UUID foodId = modifyItemId.getValue();
        String name = modifyItemName.getText();
        Foods.foodType type = modifyItemType.getValue();
        String unformattedPrice = modifyItemPrice.getText();
        String image = modifyImage.getText();

        if (foodId == null || name.isEmpty() || type == null || unformattedPrice.isEmpty() || image.isEmpty()) {
            venMainController.showAlert("Missing Information", "Please enter values for all fields.");
            return;
        }

        Double price = validateAndFormatPrice(unformattedPrice);

        if (price == null) {
            venMainController.showAlert("Invalid Price", "Invalid price format. Please use 'RM 7.00' or '7.00' format.");
            return;
        }

        Map<String, Object> query = Map.of("Id", foodId);
        Map<String, Object> newValue = Map.of("foodName", name, "foodType", type, "foodPrice", price, "updatedAt", LocalDateTime.now());
        Response<Void> response = DaoFactory.getFoodDao().update(query, newValue);

        if (response.isSuccess()) {
            if (saveImage(foodId) || !modifyImage.getText().isEmpty()){
                venMainController.showAlert("Success", "Food item updated successfully.");
                loadAllOrders();
                clearFields();
            } else {
                venMainController.showAlert("Media Error", "Failed to upload food image.");
            }
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
                    Response<Void> removeResponse = DaoFactory.getFoodDao().removeMedia(foodId);
                    if (removeResponse.isSuccess()) {
                        venMainController.showAlert("Success", "Food item deleted successfully.");
                        clearFields();
                        loadAllOrders();
                    } else {
                        venMainController.showAlert("Media Error", "Failed to delete food item: " + removeResponse.getMessage());
                    }
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
        Map<String, Object> mediaQuery = Map.of("ModelUUID", foodId);
        Response<ArrayList<Medias>> mediaResponse = DaoFactory.getMediaDao().read(mediaQuery);

        if (response.isSuccess()) {
            ArrayList<Foods> food = response.getData();
            modifyItemName.setText(food.get(0).getFoodName());
            modifyItemType.setValue(food.get(0).getFoodType());
            modifyItemPrice.setText(String.format("%.2f", food.get(0).getFoodPrice()));
            if (mediaResponse.isSuccess()){
                modifyImage.setText(String.valueOf(mediaResponse.getData().get(0).getId()));
            } else {
                modifyImage.clear();
            }
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
        addImage.clear();
        modifyItemId.setValue(null);
        modifyItemName.clear();
        modifyItemType.setValue(null);
        modifyItemPrice.clear();
        modifyImage.clear();
        selectedFile = null;
    }

    public void setVenMenuController(venMenuController controller) { this.venMenuController = controller; }
}
