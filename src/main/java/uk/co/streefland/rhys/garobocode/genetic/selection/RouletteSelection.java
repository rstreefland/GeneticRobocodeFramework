package uk.co.streefland.rhys.garobocode.genetic.selection;

import uk.co.streefland.rhys.garobocode.genetic.Population;

import java.util.Random;

/**
 * Roulette selection algorithm
 */
public class RouletteSelection implements Selection {

    public Population select(Population oldPopulation) {

        int populationSize = oldPopulation.size();
        Population newPopulation = new Population(populationSize, false);

        double averageFitness = 0;

        for (int i = 0; i < populationSize; i++) {
            averageFitness += oldPopulation.getBot(i).getFitness();
        }

        // Calculate average fitness
        averageFitness /= populationSize;

        double[] adjustedFitness = new double[populationSize];

        for (int i = 0; i < populationSize; i++) {
            adjustedFitness[i] = oldPopulation.getBot(i).getFitness() / averageFitness;
        }

        // Calculate the total adjusted fitness value of individuals of the population
        double total = 0;
        for (int i = 0; i < populationSize; i++) {
            total += adjustedFitness[i];
        }

        // Perform selection
        for (int i = 0; i < populationSize; i++) {

            int random = new Random().nextInt((int) total);
            int j = 0;

            double sum = adjustedFitness[j];
            while (sum < random) {
                j++;
                sum += adjustedFitness[j];
            }

            newPopulation.setBot(i, oldPopulation.getBots()[j]);
        }

        return newPopulation;
    }
}
