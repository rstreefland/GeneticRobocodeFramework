package uk.co.streefland.rhys.garobocode.genetic;

/**
 * Represents a population of Bots
 */
public class Population {

    private Bot[] bots;

    public Population(int populationSize, boolean initialise) {
        bots = new Bot[populationSize];

        if (initialise) {

            // Create and initialise all individuals
            for (int i = 0; i < size(); i++) {
                Bot bot = new Bot(true);
                setBot(i, bot);
            }
        }
    }

    public Bot getBot(int index) {
        return bots[index];
    }

    public void setBot(int index, Bot bot) {
        bots[index] = bot;
    }

    /**
     * Returns the fittest bot of the population
     */
    public Bot getFittest() {
        Bot fittest = bots[0];

        for (int i = 0; i < bots.length; i++) {
            if (fittest.getFitness() <= getBot(i).getFitness()) {
                fittest = getBot(i);
            }
        }
        return fittest;
    }

    /**
     * Returns the average fitness of all bots in the population
     */
    public double getAverageFitness() {
        double totalFitness = 0;

        for (Bot bot : bots) {
            totalFitness += bot.getFitness();
        }

        return totalFitness / bots.length;
    }

    public Bot[] getBots() {
        return bots;
    }

    /**
     * Sets the index of each bot in the population.
     */
    public void indexBots() {
        for (int i = 0; i < size(); i++) {
            bots[i].setIndex(i);
        }
    }

    public int size() {
        return bots.length;
    }
}

