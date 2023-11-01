package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Medias;
import com.oodwj_assignment.models.Users;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class admApproverController {
    @FXML
    private Button UploadImage;
    @FXML
    private Button approveUser;
    @FXML
    private Button createUser;
    @FXML
    private ToggleGroup gender;
    @FXML
    private Button rejectUser;
    @FXML
    private ComboBox<?> userSelect;

    @FXML
    public void btnApproveUserClicked(ActionEvent event) {

    }

    @FXML
    public void btnCreateUserClicked(ActionEvent event) {

    }

    @FXML
    public void btnRejectUserClicked(ActionEvent event) {

    }

    @FXML
    public void btnRemoveUserClicked(ActionEvent event) {

    }

    @FXML
    public void btnUploadImageClicked(ActionEvent event)  {
       
    }

}
