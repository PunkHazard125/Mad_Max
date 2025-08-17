package com.madmax;

import com.madmax.Controllers.MainMenuController;
import com.madmax.Management.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    private Database database;

    public App() {

        this.database = new Database();

    }

    @Override
    public void start(Stage primaryStage) {

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/madmax/MainMenu.fxml"));
            Parent root = loader.load();

            MainMenuController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            Image icon  = new Image(getClass().getResourceAsStream("/images/icon.jpg"));
            primaryStage.getIcons().add(icon);
            primaryStage.setTitle("Main Menu");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}
