package com.oodwj_assignment;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class cusWalletController {
    public void btnTopUpClicked(ActionEvent s) throws IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Request Top UP");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter your name:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
        }
    }
}
