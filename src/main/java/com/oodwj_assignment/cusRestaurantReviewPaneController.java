package com.oodwj_assignment;

import com.oodwj_assignment.models.Reviews;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

import java.util.*;

public class cusRestaurantReviewPaneController {

    @FXML private ImageView firstRatingIcon;
    @FXML private ImageView secondRatingIcon;
    @FXML private ImageView thirdRatingIcon;
    @FXML private ImageView fourthRatingIcon;
    @FXML private ImageView fifthRatingIcon;
    @FXML private TextArea contentTextArea;
    @FXML private Label dateLabel;
    @FXML private Label nameLabel;

    public void populateOrderData(Reviews review, String name){
        nameLabel.setText(name);
        dateLabel.setText(String.valueOf(review.getCreatedAt().withNano(0)));
        contentTextArea.setText(review.getReviewContent());
        Reviews.reviewRating rating = review.getReviewRating();

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
