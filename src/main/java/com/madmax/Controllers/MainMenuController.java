package com.madmax.Controllers;

import com.madmax.Management.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainMenuController {

    private Database database;
    private Stage primaryStage;

    @FXML
    private Button startButton;

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
            Stage currStage = (Stage) startButton.getScene().getWindow();
            currStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/madmax/JourneyMenu.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();

            JourneyMenuController controller = loader.getController();
            controller.setDatabase(database);
            controller.setPrimaryStage(newStage);
            Image icon  = new Image(getClass().getResourceAsStream("/images/icon.png"));
            newStage.getIcons().add(icon);
            newStage.setTitle("Journey Menu");
            newStage.setScene(new Scene(root));
            newStage.show();
            newStage.setResizable(false);

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
