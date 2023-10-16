package com.oodwj_assignment;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class cusReviewController {
    @FXML
    private TextArea comment;

    @FXML
    public void clearComment(ActionEvent event) {
        comment.clear();
    }
}

