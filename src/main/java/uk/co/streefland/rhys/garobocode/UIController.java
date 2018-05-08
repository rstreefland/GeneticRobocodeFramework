package uk.co.streefland.rhys.garobocode;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Controls UI updates and handles user interactions
 */
public class UIController {

    private ExecutorService executor;
    private Controller controller;

    @FXML
    private Label statusLabel;
    @FXML
    private Label bestAverageFitnessLabel;
    @FXML
    private Label bestPeakFitnessLabel;
    @FXML
    private Label currentGenerationLabel;
    @FXML
    private Label gensSinceLastAvgFitIncLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private TextField populationSizeField;
    @FXML
    private TextField numberRoundsField;
    @FXML
    private TextField opponentsField;
    @FXML
    private TextField workerList;
    @FXML
    private LineChart lineChart;

    private XYChart.Series<Number, Number> averageFitnessSeries;
    private XYChart.Series<Number, Number> peakFitnessSeries;

    private ObservableList<XYChart.Data<Number, Number>> averageFitnessData;
    private ObservableList<XYChart.Data<Number, Number>> peakFitnessData;

    private Timeline timeline;

    public void init() {

        averageFitnessSeries = new XYChart.Series<>();
        peakFitnessSeries = new XYChart.Series<>();

        averageFitnessSeries.setName("Average Fitness");
        peakFitnessSeries.setName("Peak Fitness");

        lineChart.getData().addAll(averageFitnessSeries, peakFitnessSeries);

        averageFitnessData = FXCollections.observableArrayList();
        peakFitnessData = FXCollections.observableArrayList();

        averageFitnessSeries.setData(averageFitnessData);
        peakFitnessSeries.setData(peakFitnessData);
    }

    @FXML
    private void handleStartButtonAction() throws IOException {

        String str = workerList.getText();
        List<String> hostnames = Arrays.asList(str.split(","));

        averageFitnessData.clear();
        peakFitnessData.clear();

        controller = new Controller(hostnames, Integer.parseInt(populationSizeField.getText()), Integer.parseInt(numberRoundsField.getText()), opponentsField.getText());

        executor = Executors.newSingleThreadExecutor();
        executor.submit(controller);

        // Timeline to update the stats and graph every 0.5 seconds
        timeline = new Timeline(new KeyFrame(Duration.millis(500), event -> {

            Statistics stats = controller.getStats();

            // Update the stats
            bestAverageFitnessLabel.setText("" + stats.getBestAverageFitness());
            bestPeakFitnessLabel.setText("" + stats.getBestPeakFitness());
            currentGenerationLabel.setText("" + stats.getCurrentGeneration());
            gensSinceLastAvgFitIncLabel.setText("" + stats.getGensSinceLastAvgFitInc());
            timeLabel.setText("" + stats.getLastGenerationTime());

            // Only update the graph if the data has changed
            if (averageFitnessData.size() > 0) {
                if (!averageFitnessData.get(averageFitnessData.size() - 1).getXValue().equals(stats.getCurrentGeneration())) {
                    averageFitnessData.add(new XYChart.Data<>(stats.getCurrentGeneration(), stats.getLastAverageFitness()));
                    peakFitnessData.add(new XYChart.Data<>(stats.getCurrentGeneration(), stats.getLastPeakFitness()));
                }
            } else {
                averageFitnessData.add(new XYChart.Data<>(stats.getCurrentGeneration(), stats.getLastAverageFitness()));
                peakFitnessData.add(new XYChart.Data<>(stats.getCurrentGeneration(), stats.getLastPeakFitness()));
            }

        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        statusLabel.setText("Running");
    }

    @FXML
    private void handleStopButtonAction() {

        if (executor != null) {
            controller.setRunning(false);
            executor.shutdown();
        }

        if (timeline != null) {
            timeline.stop();
        }

        statusLabel.setText("Idle");
    }
}