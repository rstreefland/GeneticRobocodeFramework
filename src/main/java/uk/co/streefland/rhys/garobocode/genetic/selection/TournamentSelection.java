package uk.co.streefland.rhys.garobocode.genetic.selection;

import uk.co.streefland.rhys.garobocode.genetic.Population;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Tournament selection algorithm
 */
public class TournamentSelection implements Selection {

    private static final int TOURNAMENT_SIZE = 5;

    public Population select(Population oldPopulation) {

        Population newPopulation = new Population(oldPopulation.size(), false);

        for (int i = 0; i < oldPopulation.size(); i++) {

            Population tournament = new Population(TOURNAMENT_SIZE, false);

            // Get a random bot for each place in the tournament
            for (int j = 0; j < TOURNAMENT_SIZE; j++) {
                int randomId = ThreadLocalRandom.current().nextInt(0, oldPopulation.size());
                tournament.setBot(j, oldPopulation.getBot(randomId));
            }

            // Add the fittest from the tournament to the new population
            newPopulation.setBot(i, tournament.getFittest());
        }

        return newPopulation;
    }
}
