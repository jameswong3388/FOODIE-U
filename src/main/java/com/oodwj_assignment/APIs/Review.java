package com.oodwj_assignment.APIs;

import com.oodwj_assignment.Models.Reviews;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Review {
    private static final String FILE_NAME = "src/main/java/com/oodwj_assignment/Database/reviews.txt";

    public static Response<UUID> create(Reviews review) {
        UUID reviewId = UUID.randomUUID();
        review.setReviewId(reviewId);

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(review);

            return Response.success("Review created successfully", reviewId);
        } catch (IOException e) {
            return Response.failure("Failed to create review: " + e.getMessage());
        }
    }

    public static Response<ArrayList<Reviews>> read(Map<String, Object> query) {
        ArrayList<Reviews> reviews = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length == 8) {
                    UUID reviewId = UUID.fromString(parts[0]);
                    UUID foodId = UUID.fromString(parts[1]);
                    UUID userId = UUID.fromString(parts[2]);
                    String reviewContent = parts[4];
                    Reviews.reviewRating reviewRating = Reviews.reviewRating.valueOf(parts[5]);
                    LocalDateTime updatedAt = LocalDateTime.parse(parts[6]);
                    LocalDateTime createdAt = LocalDateTime.parse(parts[7]);

                    reviews.add(new Reviews(reviewId, foodId, userId, reviewContent, reviewRating, updatedAt, createdAt));
                }
            }
        } catch (IOException e) {
            return Response.failure("Failed to read reviews: " + e.getMessage());
        }

        if (reviews.isEmpty()) {
            return Response.success("Reviews read successfully", reviews);
        }

        if (query.isEmpty()) {
            return Response.success("Reviews read successfully", reviews);
        }

        ArrayList<Reviews> matchedReviews = new ArrayList<>();

        for (Reviews review : reviews) {
            Response<Void> matchRes = match(query, review);

            if (matchRes.isSuccess()) {
                matchedReviews.add(review);
            }
        }

        return Response.success("Reviews read successfully", matchedReviews);
    }

    public static Response<Void> update(Map<String, Object> query, Map<String, Object> newValue) {
        Response<ArrayList<Reviews>> reviews = read(Map.of());

        if (reviews.isSuccess()) {
            for (Reviews review : reviews.getData()) {
                Response<Void> matchRes = match(query, review);

                if (matchRes.isSuccess()) {
                    for (Map.Entry<String, Object> entry : newValue.entrySet()) {
                        String attributeName = entry.getKey();
                        Object expectedValue = entry.getValue();

                        Response<Void> updateRes = review.setAttributeValue(attributeName, expectedValue);

                        if (!updateRes.isSuccess()) {
                            return Response.failure(updateRes.getMessage());
                        }
                    }

                    Response<Void> saveRes = saveAllReviews(reviews.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("Review updated successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }

            return Response.failure("Review not found");
        } else {
            return Response.failure(reviews.getMessage());
        }
    }

    public static Response<Void> delete(Map<String, Object> query) {
        Response<ArrayList<Reviews>> reviews = read(Map.of());

        if (reviews.isSuccess()) {
            for (Reviews review : reviews.getData()) {
                Response<Void> matchRes = match(query, review);

                if (matchRes.isSuccess()) {
                    reviews.getData().remove(review);
                    Response<Void> saveRes = saveAllReviews(reviews.getData());

                    if (saveRes.isSuccess()) {
                        return Response.success("Review deleted successfully");
                    } else {
                        return Response.failure(saveRes.getMessage());
                    }
                }
            }
            return Response.failure("Review not found");

        } else {
            return Response.failure(reviews.getMessage());
        }
    }

    public static Response<Void> deleteAll(Map<String, Object> query) {
        Response<ArrayList<Reviews>> reviews = read(Map.of());

        if (reviews.isSuccess()) {
            for (Reviews review : reviews.getData()) {
                Response<Void> matchRes = match(query, review);

                if (matchRes.isSuccess()) {
                    reviews.getData().remove(review);
                }
            }
            Response<Void> saveRes = saveAllReviews(reviews.getData());

            if (saveRes.isSuccess()) {
                return Response.success("Reviews deleted successfully");
            } else {
                return Response.failure(saveRes.getMessage());
            }
        } else {
            return Response.failure(reviews.getMessage());
        }
    }

    private static Response<Void> match(Map<String, Object> query, Reviews review) {
        boolean match = true;

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            String attributeName = entry.getKey();
            Object expectedValue = entry.getValue();

            Response<Object> actualValue = review.getAttributeValue(attributeName);

            if (actualValue.isSuccess()) {
                if (!actualValue.getData().equals(expectedValue)) {
                    match = false;
                    break;
                }
            } else {
                return Response.failure(actualValue.getMessage());
            }
        }
        if (match) {
            return Response.success("Review found");
        } else {
            return Response.failure("Review not found");
        }
    }

    private static Response<Void> saveAllReviews(ArrayList<Reviews> reviews) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Reviews review : reviews) {
                writer.println(review);
            }
            return Response.success("Reviews saved successfully");
        } catch (IOException e) {
            return Response.failure("Failed to save reviews: " + e.getMessage());
        }
    }
}
