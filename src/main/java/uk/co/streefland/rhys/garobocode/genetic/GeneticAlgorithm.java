package uk.co.streefland.rhys.garobocode.genetic;

import uk.co.streefland.rhys.garobocode.genetic.crossover.Crossover;
import uk.co.streefland.rhys.garobocode.genetic.crossover.UniformCrossover;
import uk.co.streefland.rhys.garobocode.genetic.mutation.CreepMutation;
import uk.co.streefland.rhys.garobocode.genetic.mutation.Mutation;
import uk.co.streefland.rhys.garobocode.genetic.selection.Selection;
import uk.co.streefland.rhys.garobocode.genetic.selection.TournamentSelection;

/**
 * Manages the evolution of the population for every generation
 * Ties together the process of selection, crossover, and mutation
 */
public class GeneticAlgorithm {

    public static final double MUTATION_RATE = 0.1;
    private static final boolean elitism = true;

    private Selection selection;
    private Population population;
    private Crossover crossover;
    private Mutation mutation;

    public GeneticAlgorithm(int populationSize) {
        population = new Population(populationSize, true);
        selection = new TournamentSelection();
        crossover = new UniformCrossover();
        mutation = new CreepMutation();
    }

    /**
     * Evolve the current population to form a new generation
     */
    public void evolve() {
        Population newPopulation = new Population(population.size(), false);

        int index;

        // Retain the fittest bot
        if (elitism) {
            newPopulation.setBot(0, population.getFittest());
            index = 1;
        }

        // Select two parent populations
        Population p1 = selection.select(population);
        Population p2 = selection.select(population);

        // Perform crossover on the two populations to produce offspring
        for (int i = index; i < p1.size(); i++) {
            Bot newBot = crossover.crossover(p1.getBot(i), p2.getBot(i));
            newPopulation.setBot(i, newBot);
        }

        // Perform mutation on the new population
        for (int i = index; i < newPopulation.size(); i++) {
            mutation.mutate(newPopulation.getBot(i));
        }

        population = newPopulation;
    }

    public Population getPopulation() {
        return population;
    }
}
