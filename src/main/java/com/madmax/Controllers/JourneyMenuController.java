package com.madmax.Controllers;

import com.madmax.Management.Database;
import com.madmax.Management.JourneyUtils;
import com.madmax.Models.Outpost;
import com.madmax.Models.Route;
import com.madmax.Models.Step;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class JourneyMenuController {

    private Database database;
    private Stage primaryStage;
    private ArrayList<Outpost> route;
    private ArrayList<Step> steps;

    @FXML
    private ListView<Step> currentPath;
    @FXML
    private Button continueJourney;
    @FXML
    private Button endJourney;
    @FXML
    private Button showStats;
    @FXML
    private AnchorPane loadingPane;

    public void setDatabase(Database database) {
        this.database = database;
    }

    public void setPrimaryStage(Stage primaryStage) {

        this.primaryStage = primaryStage;
        Image icon  = new Image(getClass().getResourceAsStream("/images/icon.png"));
        primaryStage.getIcons().add(icon);

        loadingPane.setVisible(false);

        route = new ArrayList<>();
        steps = new ArrayList<>();

        int src = 1;
        int dest = 20;
        database.getWarRig().setLocationId(src);
        database.getWarRig().refuel(database.getOutposts().get(src).getFuelSupply());

        JourneyUtils.dijkstra(src, dest, database.getAdjList(), database.getOutposts(), route, steps);

        currentPath.getItems().setAll(steps);

    }

    public void travelToOutpost(Step selectedStep) {

        database.getWarRig().ConsumeFuel(selectedStep.getFuelCost());

        int nextLoc = selectedStep.getOutpost().getId();
        database.getWarRig().setLocationId(nextLoc);
        database.getWarRig().refuel(selectedStep.getOutpost().getFuelSupply());

        route = new ArrayList<>();
        steps = new ArrayList<>();

        JourneyUtils.dijkstra(nextLoc, 20, database.getAdjList(), database.getOutposts(), route, steps);
        currentPath.getItems().setAll(steps);

    }

    @FXML
    public void handleTravel() {

        Step selectedStep = currentPath.getSelectionModel().getSelectedItem();

        if (selectedStep == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Select an outpost!");
            alert.showAndWait();
            return;

        }

        if (selectedStep.getOutpost().getId() == database.getWarRig().getLocationId()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("You are already at this outpost! Select another one.");
            alert.showAndWait();
            return;

        }

        if (selectedStep.getFuelCost() > database.getWarRig().getFuel()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Insufficient fuel to reach this outpost! Select another one.");
            alert.showAndWait();
            return;

        }

        loadingPane.setVisible(true);

        currentPath.setDisable(true);
        continueJourney.setDisable(true);
        endJourney.setDisable(true);
        showStats.setDisable(true);

        PauseTransition delay = new PauseTransition(Duration.seconds(2.0));

        delay.setOnFinished(event -> {

            travelToOutpost(selectedStep);

            loadingPane.setVisible(false);

            currentPath.setDisable(false);
            continueJourney.setDisable(false);
            endJourney.setDisable(false);
            showStats.setDisable(false);

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Journey Update");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Reached Outpost: " + selectedStep.getOutpost().getName());
                alert.showAndWait();
            });

        });

        delay.play();

    }

    @FXML
    public void handleExit() {

        primaryStage.close();

    }

}
