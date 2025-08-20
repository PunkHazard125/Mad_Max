package com.madmax.Controllers;

import com.madmax.Management.Database;
import com.madmax.Management.JourneyUtils;
import com.madmax.Models.Outpost;
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
        int dest = 50;
        database.getWarRig().setLocationId(src);

        JourneyUtils.dijkstra(src, dest, database.getAdjList(), database.getOutposts(), route, steps);

        currentPath.getItems().setAll(steps);

    }

    public void travelToOutpost(Step selectedStep) {

        database.getWarRig().consumeFuel(selectedStep.getFuelCost());

        int nextLoc = selectedStep.getOutpost().getId();
        database.getWarRig().setLocationId(nextLoc);
        database.getWarRig().refuel(selectedStep.getOutpost().getFuelSupply());
        System.out.println("Fuel: " + database.getWarRig().getFuel());

        route = new ArrayList<>();
        steps = new ArrayList<>();

        JourneyUtils.dijkstra(nextLoc, 50, database.getAdjList(), database.getOutposts(), route, steps);
        currentPath.getItems().setAll(steps);

    }

    public void handleDetour(String str) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Attention!");
        alert.setHeaderText(null);
        alert.setContentText("Detour required! " + str + " Searching for nearest safe outpost...");
        alert.showAndWait();

        int newLoc = JourneyUtils.detour(
                database.getWarRig().getLocationId(),
                database.getAdjList(),
                database.getOutposts(),
                database.getWarRig()
        );

        if (newLoc == -1) {

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Journey Failed");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("No reachable safe outpost! Unfortunately the journey ends here.");
            errorAlert.showAndWait();
            handleExit();
            return;

        }

        loadingPane.setVisible(true);

        currentPath.setDisable(true);
        continueJourney.setDisable(true);
        endJourney.setDisable(true);
        showStats.setDisable(true);

        PauseTransition delay = new PauseTransition(Duration.seconds(2.0));

        delay.setOnFinished(event -> {

            route = new ArrayList<>();
            steps = new ArrayList<>();
            JourneyUtils.dijkstra(newLoc, 50, database.getAdjList(), database.getOutposts(), route, steps);
            currentPath.getItems().setAll(steps);

            loadingPane.setVisible(false);

            currentPath.setDisable(false);
            continueJourney.setDisable(false);
            endJourney.setDisable(false);
            showStats.setDisable(false);

            Platform.runLater(() -> {
                Alert updateAlert = new Alert(Alert.AlertType.INFORMATION);
                updateAlert.setTitle("Journey Update");
                updateAlert.setHeaderText(null);
                updateAlert.setContentText("Successfully reached detour outpost: "
                        + database.getOutposts().get(newLoc).getName());
                updateAlert.showAndWait();
            });

        });

        delay.play();

    }

    @FXML
    public void handleTravel() {

        if (currentPath.getItems().size() <= 1) {

            handleExit();
            return;

        }

        Step nextStep = currentPath.getItems().get(1);
        int fuelCost = nextStep.getFuelCost();
        int currFuel = database.getWarRig().getFuel();

        if (fuelCost > currFuel) {

            handleDetour("Insufficient fuel.");
            return;

        }

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
