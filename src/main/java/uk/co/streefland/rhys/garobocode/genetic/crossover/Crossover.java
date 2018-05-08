package uk.co.streefland.rhys.garobocode.genetic.crossover;

import uk.co.streefland.rhys.garobocode.genetic.Bot;

/**
 * Interface for each crossover algorithm
 */
public interface Crossover {

    /**
     * Performs the crossover of two parent bots' DNA to produce one offspring bot
     *
     * @return The offspring bot
     */
    Bot crossover(Bot bot1, Bot bot2);
}
