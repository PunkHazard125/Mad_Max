package com.madmax.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    private Label welcomeText;

    private Stage primaryStage;

    @FXML
    protected void onHelloButtonClick() {

        welcomeText.setText("Welcome to MAD MAX!");

    }

    public void setPrimaryStage(Stage primaryStage) {

        this.primaryStage = primaryStage;

    }

}