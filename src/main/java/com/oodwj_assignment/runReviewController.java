package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Reviews;
import com.oodwj_assignment.models.Users;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDateTime;
import java.util.*;

public class runReviewController {

    @FXML private TableView<Reviews> reviewsTableView;
    @FXML private TableColumn<Reviews, UUID> reviewIdColumn;
    @FXML private TableColumn<Users, String> nameColumn;
    @FXML private TableColumn<Reviews, LocalDateTime> dateColumn;
    @FXML private TableColumn<Reviews, Reviews.reviewRating> ratingColumn;
    @FXML private TableColumn<Reviews, String> contentColumn;
    @FXML private TableColumn<Reviews, Button> actionColumn;
    @FXML private Label averageRatingLabel;
    @FXML private Label nameLabel;
    @FXML private Label dateLabel;
    @FXML private TextArea contentTextArea;
    @FXML private ImageView firstRatingIcon;
    @FXML private ImageView secondRatingIcon;
    @FXML private ImageView thirdRatingIcon;
    @FXML private ImageView fourthRatingIcon;
    @FXML private ImageView fifthRatingIcon;
    private Map<UUID, List<Reviews>> orderReviewsMap = new HashMap<>();

    public void initialize(){
        // Initialize column data factories
        reviewIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCreatedAt()));
        ratingColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getReviewRating()));
        contentColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getReviewContent()));
        // Define the "View" button cell factory
        actionColumn.setCellFactory(col -> createViewButtonCell());

        loadReviews();
    }

    private TableCell<Reviews, Button> createViewButtonCell() {
        return new TableCell<Reviews, Button>() {
            final Button viewButton = new Button("View");

            {
                // Define the action when the "View" button is clicked
                viewButton.setOnAction(event -> {
                    Reviews review = getTableView().getItems().get(getIndex());
                    UUID reviewId = review.getId();
                    setReviewInfo(reviewId);
                });
            }
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewButton);
                }
            }
        };
    }

    private void loadReviews() {
        // Fetch a list of order IDs
        List<UUID> taskIds = runMainController.getTaskId();

        List<String> names = new ArrayList<>();

        if (!taskIds.isEmpty()) {
            for (UUID taskId : taskIds) {
                fetchReviewsForTask(taskId, names);
            }
        }

        // Set the cell value factory for the nameColumn
        setCellValueFactoryForNameColumn(names);

        // Calculate average rating
        averageRatingLabel.setText(calculateAverageRating(taskIds));
    }

    private void fetchReviewsForTask(UUID taskId, List<String> names) {
        Map<String, Object> query = Map.of("modelUUID", taskId, "model", "/models/Task");
        Response<ArrayList<Reviews>> response = DaoFactory.getReviewDao().read(query);

        if (response.isSuccess()) {
            ArrayList<Reviews> reviews = response.getData();
            orderReviewsMap.put(taskId, reviews);

            for (Reviews review : reviews) {
                String name = getNameByUserId(review.getUserId());
                names.add(name);
            }

            reviewsTableView.getItems().addAll(reviews);
        }
    }

    private void setCellValueFactoryForNameColumn(List<String> names) {
        nameColumn.setCellValueFactory(cellData -> {
            int index = cellData.getTableView().getItems().indexOf(cellData.getValue());
            return new SimpleStringProperty(names.get(index));
        });
    }

    public String calculateAverageRating(List<UUID> taskIds) {
        int totalRatings = 0;
        int totalReviews = 0;

        for (UUID taskId : taskIds) {
            List<Reviews> reviews = orderReviewsMap.get(taskId);

            if (reviews != null) {
                for (Reviews review : reviews) {
                    int numericalRating = convertRatingToNumber(review.getReviewRating());
                    totalRatings += numericalRating;
                    totalReviews++;
                }
            }
        }

        if (totalReviews > 0) {
            double averageRating = (double) totalRatings / totalReviews;
            return String.format("%.1f", averageRating);
        } else {
            return "N/A";
        }
    }

    private String getNameByUserId(UUID userId) {
        Map<String, Object> query = Map.of("Id", userId);
        Response<ArrayList<Users>> response = DaoFactory.getUserDao().read(query);
        if (response.isSuccess()) {
            ArrayList<Users> user = response.getData();
            return user.get(0).getName();
        } else {
            System.out.println("No username found");
        }
        return "";
    }

    private void setReviewInfo(UUID reviewId){
        Map<String, Object> query = Map.of("Id", reviewId);
        Response<ArrayList<Reviews>> response = DaoFactory.getReviewDao().read(query);
        if (response.isSuccess()) {
            ArrayList<Reviews> review = response.getData();
            String content = review.get(0).getReviewContent();
            LocalDateTime date = review.get(0).getCreatedAt();
            UUID userId = review.get(0).getUserId();
            String name = getNameByUserId(userId);
            Reviews.reviewRating rating = review.get(0).getReviewRating();

            nameLabel.setText(name);
            contentTextArea.setWrapText(true);
            contentTextArea.setText(content);
            dateLabel.setText(String.valueOf(date.withNano(0)));

            // Create a map to associate ratings with images
            Map<Reviews.reviewRating, ImageView> ratingImages = new HashMap<>();
            ratingImages.put(Reviews.reviewRating.One, firstRatingIcon);
            ratingImages.put(Reviews.reviewRating.Two, secondRatingIcon);
            ratingImages.put(Reviews.reviewRating.Three, thirdRatingIcon);
            ratingImages.put(Reviews.reviewRating.Four, fourthRatingIcon);
            ratingImages.put(Reviews.reviewRating.Five, fifthRatingIcon);

            // Set rating images
            for (Map.Entry<Reviews.reviewRating, ImageView> entry : ratingImages.entrySet()) {
                Reviews.reviewRating ratingValue = entry.getKey();
                ImageView ratingImage = entry.getValue();

                if (ratingValue.ordinal() <= rating.ordinal()) {
                    ratingImage.setImage(new Image(getClass().getResourceAsStream("/images/rating.png")));
                } else {
                    ratingImage.setImage(new Image(getClass().getResourceAsStream("/images/rating-null.png")));
                }
            }
        }
    }

    private int convertRatingToNumber(Reviews.reviewRating rating) {
        // Convert the rating to a numerical value
        switch (rating) {
            case One:
                return 1;
            case Two:
                return 2;
            case Three:
                return 3;
            case Four:
                return 4;
            case Five:
                return 5;
            default:
                return 0;
        }
    }

    public void clearButtonClicked(ActionEvent event) {
        nameLabel.setText("");
        dateLabel.setText("");
        contentTextArea.clear();
        firstRatingIcon.setImage(new Image(getClass().getResourceAsStream("/images/rating-null.png")));
        secondRatingIcon.setImage(new Image(getClass().getResourceAsStream("/images/rating-null.png")));
        thirdRatingIcon.setImage(new Image(getClass().getResourceAsStream("/images/rating-null.png")));
        fourthRatingIcon.setImage(new Image(getClass().getResourceAsStream("/images/rating-null.png")));
        fifthRatingIcon.setImage(new Image(getClass().getResourceAsStream("/images/rating-null.png")));
    }
}
