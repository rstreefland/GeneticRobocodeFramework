package uk.co.streefland.rhys.garobocode.genetic.mutation;

import uk.co.streefland.rhys.garobocode.genetic.Bot;
import uk.co.streefland.rhys.garobocode.genetic.GeneticAlgorithm;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Random mutation algorithm
 */
public class RandomMutation implements Mutation {

    public void mutate(Bot bot) {

        // For every gene
        for (int i = 0; i < bot.size(); i++) {

            // Mutate with MUTATION_RATE probability
            if (Math.random() <= GeneticAlgorithm.MUTATION_RATE) {

                // Create random gene
                int gene = ThreadLocalRandom.current().nextInt(0, 1000);
                bot.setGene(i, gene);
            }
        }
    }
}
