package com.madmax.Controllers;

import com.madmax.Management.Database;
import com.madmax.Management.JourneyUtils;
import com.madmax.Models.Item;
import com.madmax.Models.Outpost;
import com.madmax.Models.Step;
import com.madmax.Models.Vehicle;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Optional;

public class JourneyMenuController {

    private Database database;
    private Stage primaryStage;
    private ArrayList<Outpost> route;
    private ArrayList<Step> steps;
    private int totalDist;

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
    @FXML
    private ProgressBar progressBar = new ProgressBar(0.00);
    @FXML
    private Label fuelStat;
    @FXML
    private Label creditStat;
    @FXML
    private Label profitStat;
    @FXML
    private ListView<String> cargoList;
    @FXML
    private AnchorPane statsPane;
    @FXML
    private Label notification;

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

        JourneyUtils.dijkstra(src, dest, database.getAdjList(), database.getOutposts(), route, steps, database.getWarRig());

        currentPath.getItems().setAll(steps);

        progressBar.setProgress(0.00);
        totalDist = steps.getLast().getFuelCost();

    }

    public void travelToOutpost(Step selectedStep) {

        database.getWarRig().consumeFuel(selectedStep.getFuelCost());

        int nextLoc = selectedStep.getOutpost().getId();
        database.getWarRig().setLocationId(nextLoc);
        database.getWarRig().refuel(selectedStep.getOutpost().getFuelSupply());

        route = new ArrayList<>();
        steps = new ArrayList<>();

        JourneyUtils.dijkstra(nextLoc, 50, database.getAdjList(), database.getOutposts(), route, steps, database.getWarRig());

        currentPath.getItems().setAll(steps);

        double progress = 1 - ((double)steps.getLast().getFuelCost() / totalDist);
        progressBar.setProgress(progress);

    }

    public void handleDetour(String str) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention!");
        alert.setHeaderText(null);
        alert.setContentText("Detour required! " + str + " Searching for nearest outpost...");
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
            errorAlert.setContentText("No reachable outpost! Unfortunately the journey ends here.");
            errorAlert.showAndWait();
            handleExit();
            return;

        }

        loadingPane.setVisible(true);

        currentPath.setDisable(true);
        continueJourney.setDisable(true);
        endJourney.setDisable(true);
        showStats.setDisable(true);
        progressBar.setDisable(true);

        PauseTransition delay = new PauseTransition(Duration.seconds(2.0));

        delay.setOnFinished(event -> {

            route = new ArrayList<>();
            steps = new ArrayList<>();
            JourneyUtils.dijkstra(newLoc, 50, database.getAdjList(), database.getOutposts(), route, steps, database.getWarRig());

            currentPath.getItems().setAll(steps);

            loadingPane.setVisible(false);

            currentPath.setDisable(false);
            continueJourney.setDisable(false);
            endJourney.setDisable(false);
            showStats.setDisable(false);
            progressBar.setDisable(false);

            Platform.runLater(() -> {

                Vehicle warRig = database.getWarRig();
                int prevCredits = warRig.getCredits();
                int prevFuel = warRig.getFuel();

                boolean ambushStatus = JourneyUtils.simulateEvents(
                        database.getOutposts().get(database.getWarRig().getLocationId()),
                        database.getWarRig()
                );

                if (ambushStatus) {

                    handleDetour("Ambush encountered!");

                }
                else {

                    JourneyUtils.handleCargo(database.getOutposts().get(database.getWarRig().getLocationId()),
                            database.getWarRig());

                    int newCredits = warRig.getCredits();
                    int newFuel = warRig.getFuel();

                    if (newFuel < prevFuel) {
                        int fuelLost = prevFuel - newFuel;
                        showNotification(notification, "Fuel lost during travel: " + fuelLost + "L");
                    }
                    else if (newFuel > prevFuel) {
                        int bonusFuel = newFuel - prevFuel;
                        showNotification(notification, "Bonus fuel found: " + bonusFuel + "L");
                    }
                    else if (newCredits > prevCredits) {
                        int lootGained = newCredits - prevCredits;
                        showNotification(notification, "Loot gained: " + lootGained + "C");
                    }

                }

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

        Step selectedStep = currentPath.getSelectionModel().getSelectedItem();
        Step nextStep = currentPath.getItems().get(1);
        int fuelCost = nextStep.getFuelCost();
        int currFuel = database.getWarRig().getFuel();

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

        if (fuelCost > currFuel) {

            handleDetour("Insufficient fuel.");
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
        progressBar.setDisable(true);

        PauseTransition delay = new PauseTransition(Duration.seconds(2.0));

        delay.setOnFinished(event -> {

            travelToOutpost(selectedStep);

            loadingPane.setVisible(false);

            currentPath.setDisable(false);
            continueJourney.setDisable(false);
            endJourney.setDisable(false);
            showStats.setDisable(false);
            progressBar.setDisable(false);

            Platform.runLater(() -> {

                Vehicle warRig = database.getWarRig();
                int prevCredits = warRig.getCredits();
                int prevFuel = warRig.getFuel();

                boolean ambushStatus = JourneyUtils.simulateEvents(
                        database.getOutposts().get(database.getWarRig().getLocationId()),
                        database.getWarRig()
                );

                if (ambushStatus) {

                    handleDetour("Ambush encountered!");

                }
                else {

                    JourneyUtils.handleCargo(database.getOutposts().get(database.getWarRig().getLocationId()),
                            database.getWarRig());

                    int newCredits = warRig.getCredits();
                    int newFuel = warRig.getFuel();

                    if (newFuel < prevFuel) {
                        int fuelLost = prevFuel - newFuel;
                        showNotification(notification, "Fuel drained: " + fuelLost + "L");
                    }
                    else if (newFuel > prevFuel) {
                        int bonusFuel = newFuel - prevFuel;
                        showNotification(notification, "Found extra fuel: " + bonusFuel + "L");
                    }
                    else if (newCredits > prevCredits) {
                        int lootGained = newCredits - prevCredits;
                        showNotification(notification, "Loot gained: " + lootGained + "C");
                    }

                }

            });

        });

        delay.play();

    }

    @FXML
    public void showStats() {

        statsPane.setVisible(true);

        currentPath.setDisable(true);
        continueJourney.setDisable(true);
        endJourney.setDisable(true);
        showStats.setDisable(true);
        progressBar.setDisable(true);

        fuelStat.setText("Fuel Tank: " + database.getWarRig().getFuel() + "L / " + database.getWarRig().getFuelCapacity() + "L");
        profitStat.setText("Profit: " + database.getWarRig().getProfit() + "C");
        creditStat.setText("Stash: " + database.getWarRig().getCredits() + "C");

        cargoList.getItems().clear();

        for (Item item : database.getWarRig().getCargo()) {
            cargoList.getItems().add(item.getName() + " | Value: " + item.getValue() + " Credit(s)");
        }

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(400), statsPane);
        slideIn.setFromX(-statsPane.getWidth());
        slideIn.setToX(0);
        slideIn.play();

    }

    @FXML
    public void closeStats() {

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(400), statsPane);
        slideOut.setFromX(0);
        slideOut.setToX(-statsPane.getWidth());
        slideOut.setOnFinished(e -> statsPane.setVisible(false));
        slideOut.play();

        currentPath.setDisable(false);
        continueJourney.setDisable(false);
        endJourney.setDisable(false);
        showStats.setDisable(false);
        progressBar.setDisable(false);

    }

    public void showNotification(Label notification, String message) {

        notification.setText(message);
        notification.setOpacity(0);
        notification.setVisible(true);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), notification);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(1000), notification);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setDelay(Duration.seconds(2));

        SequentialTransition seq = new SequentialTransition(fadeIn, fadeOut);
        seq.setOnFinished(e -> notification.setVisible(false));
        seq.play();

    }

    @FXML
    public void handleExit() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation!");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to end the journey here?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            primaryStage.close();
        }

    }

}
