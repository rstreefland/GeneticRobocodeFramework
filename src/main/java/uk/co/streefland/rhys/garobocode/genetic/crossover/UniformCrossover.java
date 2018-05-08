package uk.co.streefland.rhys.garobocode.genetic.crossover;

import uk.co.streefland.rhys.garobocode.genetic.Bot;

/**
 * Uniform crossover algorithm
 */
public class UniformCrossover implements Crossover {

    public Bot crossover(Bot bot1, Bot bot2) {

        // Perform crossover
        Bot newBot = new Bot(false);

        // For every gene
        for (int i = 0; i < bot1.size(); i++) {

            // Uniformly select genes from each bot
            if (Math.random() <= 0.5) {
                newBot.setGene(i, bot1.getGene(i));
            } else {
                newBot.setGene(i, bot2.getGene(i));
            }
        }

        return newBot;
    }
}
