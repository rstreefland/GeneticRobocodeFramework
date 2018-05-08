package uk.co.streefland.rhys.garobocode.genetic.selection;

import uk.co.streefland.rhys.garobocode.genetic.Population;

import java.util.Random;

/**
 * Stochastic universal sampling selection algorithm
 */
public class StochasticUniversalSamplingSelection implements Selection {

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

        // Calculate the total adjusted fitness value of individuals of the population
        for (int i = 0; i < populationSize; i++) {
            adjustedFitness[i] = oldPopulation.getBot(i).getFitness() / averageFitness;
        }

        double random = new Random().nextDouble();
        double sum = 0;
        int j = 0;

        // Perform selection
        for (int i = 0; i < populationSize; i++) {
            for (sum += adjustedFitness[i]; sum > random; random++) {
                newPopulation.setBot(j++, oldPopulation.getBot(i));
            }
        }

        return newPopulation;
    }
}
