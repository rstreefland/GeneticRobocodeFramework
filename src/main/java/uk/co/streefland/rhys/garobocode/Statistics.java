package uk.co.streefland.rhys.garobocode;

import uk.co.streefland.rhys.garobocode.genetic.Population;

import java.io.*;

/**
 * Calculates and stores genetic algorithm statistics for the controller
 */
public class Statistics {

    private String id;

    private int currentGeneration = 0;
    private double lastAverageFitness = 0;
    private double lastPeakFitness = 0;
    private double bestAverageFitness = 0;
    private double bestPeakFitness = 0;
    private int gensSinceLastAvgFitInc = 0;

    private long startTime = 0;
    private long endTime = 0;
    private long lastGenerationTime = 0;

    public Statistics(String id) {
        this.id = id;
    }

    /**
     * Starts a timer for a given generation
     */
    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Updates the statistics for a given generation
     *
     * @param population
     */
    public void update(Population population) {

        currentGeneration++;

        lastAverageFitness = population.getAverageFitness();
        lastPeakFitness = population.getFittest().getFitness();

        if (lastAverageFitness > bestAverageFitness) {
            bestAverageFitness = lastAverageFitness;
            gensSinceLastAvgFitInc = 0;
        } else {
            gensSinceLastAvgFitInc++;
        }

        if (lastPeakFitness > bestPeakFitness) {
            bestPeakFitness = lastPeakFitness;
        }

        endTime = System.currentTimeMillis();
        lastGenerationTime = (endTime - startTime) / 1000;

        print();
        writeToCSV();
    }

    /**
     * Prints the statistics to the console
     */
    private void print() {
        System.out.println(currentGeneration + "," + lastAverageFitness + "," + lastPeakFitness + "," + lastGenerationTime);
    }

    /**
     * Appends the statistics to the CSV file with the name id.csv
     */
    private void writeToCSV() {

        new File("output/" + id).mkdirs();

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output/" + id + "/" + id + ".csv", true)))) {
            out.println(lastAverageFitness + "," + lastPeakFitness + "," + lastGenerationTime);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public int getCurrentGeneration() {
        return currentGeneration;
    }

    public double getLastAverageFitness() {
        return lastAverageFitness;
    }

    public double getLastPeakFitness() {
        return lastPeakFitness;
    }

    public double getBestAverageFitness() {
        return bestAverageFitness;
    }

    public double getBestPeakFitness() {
        return bestPeakFitness;
    }

    public int getGensSinceLastAvgFitInc() {
        return gensSinceLastAvgFitInc;
    }

    public long getLastGenerationTime() {
        return lastGenerationTime;
    }
}
