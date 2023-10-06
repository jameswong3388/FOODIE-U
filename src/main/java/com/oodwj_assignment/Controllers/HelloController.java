package com.oodwj_assignment.Controllers;

import com.oodwj_assignment.APIs.Response;
import com.oodwj_assignment.APIs.User;
import com.oodwj_assignment.Models.Users;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Map;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
        Response<ArrayList<Users>> res = User.read(Map.of());

        System.out.println(res.getMessage());
     }
}