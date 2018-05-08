package uk.co.streefland.rhys.garobocode.genetic;

import gabot.EvoTank_RhysStreefland;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents one genetic bot in a population
 */
public class Bot implements Serializable {

    private static final long serialVersionUID = 1L;

    private int index;
    private double[] genome = new double[EvoTank_RhysStreefland.CHROMOSOME_COUNT * EvoTank_RhysStreefland.GENE_COUNT];
    private double fitness = 0;

    public Bot(boolean initialise) {
        if (initialise) {
            initialise();
        }
    }

    /**
     * Initialises the bot's DNA with a random set of genes
     */
    private void initialise() {
        for (int i = 0; i < genome.length; i++) {
            genome[i] = ThreadLocalRandom.current().nextInt(0, 1000);
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getGene(int index) {
        return genome[index];
    }

    public void setGene(int index, double gene) {
        genome[index] = gene;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double[] getGenome() {
        return genome;
    }

    public int size() {
        return genome.length;
    }
}
