package com.madmax.Controllers;

import com.madmax.Management.Database;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class FinalMenuController {

    private Database database;
    private Stage primaryStage;

    @FXML
    private Button exitButton;
    @FXML
    private Label missionStatus;
    @FXML
    private Label fuelConsumption;
    @FXML
    private Label itemCount;
    @FXML
    private Label totalProfit;
    @FXML
    private Label eventCount;
    @FXML
    private Label currLocation;

    public void setDatabase(Database database) {

        this.database = database;

        boolean ambush = database.getWarRig().isAmbushed();
        int fuel = database.getWarRig().getFuelConsumption();
        int item = ambush ? 0 : database.getWarRig().getCargo().size();
        int event = database.getWarRig().getEventCount();
        int profit = ambush ? 0 : database.getWarRig().getCredits() + database.getWarRig().getProfit();
        int id = database.getWarRig().getLocationId();
        String currLoc = database.getOutposts().get(id).getName();

        missionStatus.setText(id == 50 ? "Mission Status: Successful" : "Mission Status: Failed");

        animateCount(fuelConsumption, fuel, "Fuel Consumption: ", " Liter(s)");
        animateCount(itemCount, item, "Item Count: ", "");
        animateCount(totalProfit, profit, "Cargo Value: ", " Credit(s)");
        animateCount(eventCount, event, "Event(s) Encountered: ", "");

        currLocation.setText("Location: " + currLoc);

    }

    public void setPrimaryStage(Stage primaryStage) {

        this.primaryStage = primaryStage;
        Image icon  = new Image(getClass().getResourceAsStream("/images/icon.png"));
        primaryStage.getIcons().add(icon);

    }

    public void animateCount(Label label, int targetValue, String prefix, String suffix) {

        if (targetValue == 0) {

            label.setText(prefix + "0" + suffix);
            return;

        }

        IntegerProperty value = new SimpleIntegerProperty(0);
        value.addListener((obs, oldVal, newVal) -> label.setText(prefix + newVal + suffix));

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(value, 0)),
                new KeyFrame(Duration.seconds(1.5), new KeyValue(value, targetValue))
        );

        timeline.play();

    }

    public void saveStatsToFile() {

        String fileName = "MadMax_Stats";

        String userHome = System.getProperty("user.home");
        File downloads = new File(userHome, "Downloads");

        if (!downloads.exists()) downloads.mkdirs();

        File file = new File(downloads, fileName + ".txt");
        int counter = 1;

        while (file.exists()) {

            file = new File(downloads, fileName + "(" + counter + ").txt");
            counter++;

        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");

        try (FileWriter writer = new FileWriter(file)) {

            writer.write(missionStatus.getText() + "\n");
            writer.write(totalProfit.getText() + "\n");
            writer.write(itemCount.getText() + "\n");
            writer.write(fuelConsumption.getText() + "\n");
            writer.write(eventCount.getText() + "\n");
            writer.write(currLocation.getText() + "\n");
            writer.write("End Time of Journey: " + database.journeyEndTime.format(formatter) + "\n");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Saved!");
            alert.setHeaderText(null);
            alert.setContentText("Final stats file saved at " + userHome + File.separator + "Downloads");
            alert.showAndWait();

        }
        catch (IOException e) {

            e.printStackTrace();

        }
    }

    public void handleExit() {

        this.primaryStage.close();

    }

}
