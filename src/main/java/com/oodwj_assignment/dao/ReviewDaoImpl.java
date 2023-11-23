package com.oodwj_assignment.dao;

import com.oodwj_assignment.dao.base.AbstractDao;

import com.oodwj_assignment.models.Reviews;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewDaoImpl extends AbstractDao<Reviews> implements ReviewDao {

    private static final File FILE = new File("database/reviews.dat");

    public ReviewDaoImpl() {
        super(FILE);
    }

    public Reviews parse(String[] parts) {
        try {
            UUID reviewId = UUID.fromString(parts[0]);
            String model = parts[1];
            UUID modelUUID = UUID.fromString(parts[2]);
            UUID userId = UUID.fromString(parts[3]);
            String reviewContent = parts[4];
            Reviews.reviewRating reviewRating = Reviews.reviewRating.valueOf(parts[5]);
            LocalDateTime updatedAt = LocalDateTime.parse(parts[6]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[7]);

            return new Reviews(reviewId, model, modelUUID, userId, reviewContent, reviewRating, updatedAt, createdAt);
        } catch (Exception e) {
            return null;
        }
    }


}
