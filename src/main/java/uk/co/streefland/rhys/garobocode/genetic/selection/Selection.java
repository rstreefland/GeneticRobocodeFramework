package uk.co.streefland.rhys.garobocode.genetic.selection;

import uk.co.streefland.rhys.garobocode.genetic.Population;

/**
 * Interface for each selection algorithm
 */
public interface Selection {

    /**
     * Selects offspring from a given set of parents
     *
     * @param oldPopulation The parent population
     * @return Offspring population
     */
    Population select(Population oldPopulation);
}
