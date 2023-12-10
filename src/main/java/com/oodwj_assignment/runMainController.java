package com.oodwj_assignment;

import com.oodwj_assignment.dao.SessionDaoImpl;
import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Foods;
import com.oodwj_assignment.models.OrderFoods;
import com.oodwj_assignment.models.Tasks;
import com.oodwj_assignment.states.AppState;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class runMainController {

    @FXML private BorderPane borderpane;
    @FXML private ImageView homeIcon;
    @FXML private ImageView taskIcon;
    @FXML private ImageView revenueIcon;
    @FXML private ImageView reviewIcon;
    @FXML private ImageView profileIcon;
    @FXML private ImageView logoutIcon;
    @FXML private ImageView notificationIcon;
    @FXML private Label nameLabel;
    @FXML private Circle profilePic;

    public static UUID runnerId;
    private runMainController mainController;

    public void initialize() throws IOException {
        Image home = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/home-orange.png")));
        homeIcon.setImage(home);
    }

    public void defaultSettings() {
        // Load an image file and set it to the ImageView
        Image home = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/home-grey.png")));
        homeIcon.setImage(home);
        Image task = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/task-grey.png")));
        taskIcon.setImage(task);
        Image revenue = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/revenue-grey.png")));
        revenueIcon.setImage(revenue);
        Image review = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/review-grey.png")));
        reviewIcon.setImage(review);
        Image profile = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/profile-grey.png")));
        profileIcon.setImage(profile);
        Image logout = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logout-grey.png")));
        logoutIcon.setImage(logout);
        Image notification= new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/notification-grey.png")));
        notificationIcon.setImage(notification);
    }

    public void btnHomeClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("runHome.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image home = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/home-orange.png")));
        homeIcon.setImage(home);
    }

    public void btnTaskClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("runTask.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image task= new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/task-orange.png")));
        taskIcon.setImage(task);
    }

    public void btnRevenueClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("runRevenue.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image wallet = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/revenue-orange.png")));
        revenueIcon.setImage(wallet);
    }

    public void btnReviewClicked(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("runReview.fxml")));
        borderpane.setCenter(view);
        defaultSettings();
        Image review = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/review-orange.png")));
        reviewIcon.setImage(review);
    }

    public void btnProfileClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("runProfile.fxml"));
        AnchorPane view = fxmlLoader.load();
        runProfileController profileController = fxmlLoader.getController();
        profileController.setRunMainController(this);
        borderpane.setCenter(view);
        defaultSettings();
        Image profile = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/profile-orange.png")));
        profileIcon.setImage(profile);
    }

    public void btnLogoutClicked(ActionEvent event) throws IOException {
        defaultSettings();
        Image logout = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logout-orange.png")));
        logoutIcon.setImage(logout);

        Stage runStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Alert.AlertType type = Alert.AlertType.CONFIRMATION;
        Alert alert = new Alert(type, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(runStage);
        alert.getDialogPane().setHeaderText("Are you sure you want to logout?");

        // Customize the button text
        ButtonType logoutButton = new ButtonType("Logout", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(logoutButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == logoutButton) {
            // Perform logout
            SessionDaoImpl sessionDao = new SessionDaoImpl();
            Response<Void> logoutResponse = sessionDao.logout(AppState.getSessionToken());

            if (logoutResponse.isSuccess()) {
                // Logout successful, navigate to login screen
                Parent loginRoot = FXMLLoader.load(getClass().getResource("login.fxml"));
                Stage loginStage = new Stage();
                loginStage.setTitle("Login Page");
                loginStage.setScene(new Scene(loginRoot));
                loginStage.setResizable(false);
                loginStage.show();
                runStage.close();
            } else {
                runMainController.showAlert(String.valueOf(Alert.AlertType.ERROR), logoutResponse.getMessage());
            }
        }
    }
    public void btnNotificationClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("notification.fxml"));
        AnchorPane view = fxmlLoader.load();
        notificationController notificationController = fxmlLoader.getController();
        notificationController.fetchAndFilterNotifications(runnerId, null);
        borderpane.setCenter(view);
        defaultSettings();
        Image notification = new Image(getClass().getResourceAsStream("/images/notification-orange.png"));
        notificationIcon.setImage(notification);
    }

    public static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static List<UUID> getTaskId() {
        Set<UUID> storeRunnerId = new HashSet<>();

        Map<String, Object> taskQuery = Map.of("runnerId", runnerId);
        Response<ArrayList<Tasks>> taskResponse = DaoFactory.getTaskDao().read(taskQuery);

        if (taskResponse.isSuccess()) {
            List<UUID> taskIds = taskResponse.getData().stream()
                    .map(Tasks::getId)
                    .toList();

            for (UUID taskId : taskIds) {
                Map<String, Object> runnerQuery = Map.of("taskId", taskId);
                Response<ArrayList<Tasks>> runnerResponse = DaoFactory.getTaskDao().read(runnerQuery);

                if (taskResponse.isSuccess()) {
                    List<UUID> runnerIds = runnerResponse.getData().stream()
                            .map(Tasks::getRunnerId)
                            .distinct()
                            .toList();

                    storeRunnerId.addAll(taskIds);
                }
            }
        }
        return new ArrayList<>(storeRunnerId);
    }

    public void loadData() {
        String name = DaoFactory.getUserDao().read(Map.of("Id", runnerId)).getData().get(0).getName();
        nameLabel.setText(name);

        Response<String> mediaResponse = DaoFactory.getUserDao().getFirstMedia(runnerId);
        if (mediaResponse.isSuccess()){
            String imagePath = mediaResponse.getData();
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                profilePic.setFill(new ImagePattern(image));
            }
        } else {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/sample-profile.png")));
            profilePic.setFill(new ImagePattern(image));
        }
    }
    public void setRunMainController(runMainController controller) { mainController = controller; }

    public void setUserId(UUID userId) throws IOException {
        runnerId = userId;
        loadData();

        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("runHome.fxml")));
        borderpane.setCenter(view);
    }
}
