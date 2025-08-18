package com.madmax.Controllers;

import com.madmax.Management.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainMenuController {

    private Database database;
    private Stage primaryStage;

    public void setDatabase(Database database) {
        this.database = database;
    }

    public void setPrimaryStage(Stage primaryStage) {

        this.primaryStage = primaryStage;
        Image icon  = new Image(getClass().getResourceAsStream("/images/icon.png"));
        primaryStage.getIcons().add(icon);

    }

    @FXML
    private void handleStart() {

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/madmax/JourneyMenu.fxml"));
            Parent root = loader.load();

            JourneyMenuController controller = loader.getController();
            controller.setDatabase(database);
            controller.setPrimaryStage(primaryStage);
            Image icon  = new Image(getClass().getResourceAsStream("/images/icon.png"));
            primaryStage.getIcons().add(icon);
            primaryStage.setTitle("Journey Menu");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            primaryStage.setResizable(false);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    @FXML
    private void handleExit() {

        primaryStage.close();

    }

}
