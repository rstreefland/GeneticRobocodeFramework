package uk.co.streefland.rhys.garobocode.genetic.crossover;

import uk.co.streefland.rhys.garobocode.genetic.Bot;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Single point crossover algorithm
 */
public class SinglePointCrossover implements Crossover {

    public Bot crossover(Bot bot1, Bot bot2) {

        Bot newBot = new Bot(false);
        int crossoverPoint = ThreadLocalRandom.current().nextInt(0, bot1.getGenome().length);

        // Copy from first bot up until crossover point
        System.arraycopy(bot1.getGenome(), 0, newBot.getGenome(), 0, crossoverPoint);

        // Copy from second bot after crossover point
        System.arraycopy(bot1.getGenome(), crossoverPoint, newBot.getGenome(), crossoverPoint, bot1.getGenome().length - crossoverPoint);

        return newBot;
    }
}
