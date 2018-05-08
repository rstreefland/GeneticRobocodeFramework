package uk.co.streefland.rhys.garobocode.genetic.mutation;

import uk.co.streefland.rhys.garobocode.genetic.Bot;
import uk.co.streefland.rhys.garobocode.genetic.GeneticAlgorithm;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Creep mutation algorithm
 */
public class CreepMutation implements Mutation {

    public void mutate(Bot bot) {

        // For every gene
        for (int i = 0; i < bot.size(); i++) {

            if (Math.random() <= GeneticAlgorithm.MUTATION_RATE) {

                // Generate creep value and add it to the gene
                double creepValue = ThreadLocalRandom.current().nextDouble(-100, 100);

                // Don't allow the new gene value to break the 0-1000 range
                while (bot.getGene(i) + creepValue > 1000 || bot.getGene(i) + creepValue < 0) {
                    creepValue = ThreadLocalRandom.current().nextDouble(-100, 100);
                }

                double newGene = bot.getGene(i) + creepValue;

                bot.setGene(i, newGene);
            }
        }
    }
}
