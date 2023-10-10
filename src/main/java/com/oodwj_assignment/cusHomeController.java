package com.oodwj_assignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class cusHomeController {

    @FXML
    public void selectClicked(ActionEvent event) throws IOException {
        Parent menuRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("cusMenu.fxml")));
        Stage menu = new Stage();
        menu.setTitle("Menu Page");
        menu.initStyle(StageStyle.UNDECORATED);
        menu.setScene(new Scene(menuRoot));
        menu.initModality(Modality.APPLICATION_MODAL);
        menu.showAndWait();
    }

}
