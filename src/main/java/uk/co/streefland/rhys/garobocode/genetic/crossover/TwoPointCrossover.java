package uk.co.streefland.rhys.garobocode.genetic.crossover;

import uk.co.streefland.rhys.garobocode.genetic.Bot;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Two point crossover algorithm
 */
public class TwoPointCrossover implements Crossover {

    public Bot crossover(Bot bot1, Bot bot2) {

        // Perform crossover
        Bot newBot = new Bot(false);

        int crossoverPoint1 = ThreadLocalRandom.current().nextInt(0, bot1.getGenome().length);
        int crossoverPoint2 = ThreadLocalRandom.current().nextInt(0, bot1.getGenome().length);

        // Need two different crossover points
        while (crossoverPoint2 == crossoverPoint1) {
            crossoverPoint2 = ThreadLocalRandom.current().nextInt(0, bot1.getGenome().length);
        }

        if (crossoverPoint1 < crossoverPoint2) {

            // Copy from bot1 from 0 up until first crossover point
            System.arraycopy(bot1.getGenome(), 0, newBot.getGenome(), 0, crossoverPoint1);

            // Copy from bot2 from first crossover point to second crossover point
            System.arraycopy(bot2.getGenome(), crossoverPoint1, newBot.getGenome(), crossoverPoint1, crossoverPoint2 - crossoverPoint1);

            // Copy from bot1 from second crossover point to array length
            System.arraycopy(bot1.getGenome(), crossoverPoint2, newBot.getGenome(), crossoverPoint2, bot1.getGenome().length - crossoverPoint2);
        } else {

            // Copy from bot1 from 0 up until first crossover point
            System.arraycopy(bot1.getGenome(), 0, newBot.getGenome(), 0, crossoverPoint2);

            // Copy from bot2 from first crossover point to second crossover point
            System.arraycopy(bot2.getGenome(), crossoverPoint2, newBot.getGenome(), crossoverPoint2, crossoverPoint1 - crossoverPoint2);

            // Copy from bot1 from second crossover point to array length
            System.arraycopy(bot1.getGenome(), crossoverPoint1, newBot.getGenome(), crossoverPoint1, bot1.getGenome().length - crossoverPoint1);
        }

        return newBot;
    }
}
