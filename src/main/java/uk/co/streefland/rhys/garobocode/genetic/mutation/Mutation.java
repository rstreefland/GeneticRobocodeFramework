package uk.co.streefland.rhys.garobocode.genetic.mutation;

import uk.co.streefland.rhys.garobocode.genetic.Bot;

/**
 * Interface for each mutation algorithm
 */
public interface Mutation {

    /**
     * Performs mutation for one Bot's DNA
     */
    void mutate(Bot bot);
}
