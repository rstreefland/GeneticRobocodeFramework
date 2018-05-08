package uk.co.streefland.rhys.garobocode;

import uk.co.streefland.rhys.garobocode.battlerunner.BattleConfiguration;
import uk.co.streefland.rhys.garobocode.battlerunner.BotWriter;
import uk.co.streefland.rhys.garobocode.genetic.Bot;
import uk.co.streefland.rhys.garobocode.genetic.GeneticAlgorithm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controls the overall execution of the genetic algorithm and Robocode battles.
 * Distributes work to worker processes using TCP sockets
 */
public class Controller implements Runnable {

    private boolean running = true;

    private String id;
    private BattleConfiguration bc;
    private GeneticAlgorithm ga;
    private Statistics stats;

    private int workers;
    private String[] hosts;
    private int[] ports;
    private Socket[] sockets;

    public Controller(List<String> hostnames, int populationSize, int numberRounds, String opponents) throws IOException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        id = dateFormat.format(new Date());

        this.bc = new BattleConfiguration(numberRounds, opponents);
        this.ga = new GeneticAlgorithm(populationSize);
        this.stats = new Statistics(id);

        this.workers = hostnames.size() * 4;
        this.hosts = new String[workers];
        this.ports = new int[workers];
        this.sockets = new Socket[workers];

        int pos = 0;
        for (int i = 0; i < hostnames.size(); i++) {
            hosts[pos] = hostnames.get(i);
            ports[pos++] = 15001;

            hosts[pos] = hostnames.get(i);
            ports[pos++] = 15002;

            hosts[pos] = hostnames.get(i);
            ports[pos++] = 15003;

            hosts[pos] = hostnames.get(i);
            ports[pos++] = 15004;
        }
    }

    @Override
    public void run() {
        try {

            // Send the configuration to each worker
            for (int i = 0; i < workers; i++) {
                System.out.println("Connecting to " + hosts[i] + ":" + ports[i]);
                sockets[i] = new Socket(hosts[i], ports[i]);
                sendConfiguration(sockets[i], bc);
            }

            System.out.println("Connected to all workers");

            // Terminate if average fitness hasn't increased for 20 generations
            while (running && stats.getGensSinceLastAvgFitInc() <= 20) {

                // Start the timer
                stats.startTimer();

                // Index the bots
                ga.getPopulation().indexBots();

                // Divide the bots into an array for each worker
                Bot[][] bots = divideBots(ga.getPopulation().getBots());

                // Send the bot array to each worker
                for (int i = 0; i < workers; i++) {
                    sockets[i] = new Socket(hosts[i], ports[i]);
                    sendBots(sockets[i], trim(bots[i]));
                }

                // Receive the results from all workers and modify population with results
                for (int i = 0; i < workers; i++) {
                    Bot[] results = receiveBots(sockets[i]);

                    for (int j = 0; j < results.length; j++) {
                        ga.getPopulation().setBot(results[j].getIndex(), results[j]);
                    }

                    System.out.println("Received results from " + sockets[i].getRemoteSocketAddress());
                }

                // Update the statistics with the current run
                stats.update(ga.getPopulation());

                // Write the fittest bot's DNA to file
                BotWriter.writeFittest(ga.getPopulation().getFittest(), id, stats.getCurrentGeneration());

                // Evolve the population
                ga.evolve();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Divides a population among x worker nodes
     *
     * @param source
     * @return
     */
    private Bot[][] divideBots(Bot[] source) {

        int chunkSize = (int) Math.floor(source.length / workers);
        int remainder = 0;
        Bot[][] ret;

        if (chunkSize * workers < source.length) {
            remainder = source.length - ((chunkSize * workers) - chunkSize);
            ret = new Bot[workers][remainder];
        } else {
            ret = new Bot[workers][chunkSize];
        }

        int start = 0;

        for (int i = 0; i < ret.length - 1; i++) {
            System.arraycopy(source, start, ret[i], 0, chunkSize);
            start += chunkSize;
        }

        if (remainder != 0) {
            System.arraycopy(source, start, ret[ret.length - 1], 0, remainder);
        } else {
            System.arraycopy(source, start, ret[ret.length - 1], 0, chunkSize);
        }

        return ret;
    }

    /**
     * Trims an array with null values
     *
     * @param input
     * @return
     */
    private Bot[] trim(final Bot[] input) {

        ArrayList<Bot> temp = new ArrayList<>();

        for (int i = 0; i < input.length; i++) {
            if (input[i] != null) {
                if (input[i].getGenome() != null) {
                    temp.add(input[i]);
                }
            }
        }

        Bot[] ret = new Bot[temp.size()];

        for (int i = 0; i < temp.size(); i++) {
            ret[i] = temp.get(i);
        }

        return ret;
    }

    /**
     * Sends a BattleConfiguration object to a worker
     */
    private void sendConfiguration(Socket clientSocket, BattleConfiguration bc) throws IOException {

        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        out.writeObject(bc);
    }

    /**
     * Sends an array of bots to a worker
     */
    private void sendBots(Socket clientSocket, Bot[] bots) throws IOException {

        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        out.writeObject(bots);
    }

    /**
     * Receives an array of bots from a worker (with fitness values)
     */
    private Bot[] receiveBots(Socket clientSocket) throws IOException, ClassNotFoundException {

        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        return (Bot[]) in.readObject();
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Statistics getStats() {
        return stats;
    }
}
