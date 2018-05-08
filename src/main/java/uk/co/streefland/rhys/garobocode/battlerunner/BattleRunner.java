package uk.co.streefland.rhys.garobocode.battlerunner;

import robocode.BattleResults;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;
import uk.co.streefland.rhys.garobocode.genetic.Bot;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BattleRunner {

    private static RobocodeEngine engine;
    private static BattlefieldSpecification battlefield;
    private static BattleListener battleListener;
    private static BattleConfiguration bc;

    private static int port;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {

        // Initialise the Robocode engine with standard parameters
        engine = new RobocodeEngine(new File("robocode/"));
        battleListener = new BattleListener();
        engine.addBattleListener(battleListener);
        engine.setVisible(false);
        battlefield = new BattlefieldSpecification(800, 600);

        // Set the port based on the first argument
        port = Integer.parseInt(args[0]);

        setup();
    }


    /**
     * Sets up the TCP socket and reads the configuration from the controller
     */
    private static void setup() {

        try {
            serverSocket = new ServerSocket(port);

            System.out.println("Worker started - waiting for configuration on port " + port);

            // Read the configuration from the controller
            Socket initialConnection = serverSocket.accept();
            ObjectInputStream inInitial = new ObjectInputStream(initialConnection.getInputStream());
            bc = (BattleConfiguration) inInitial.readObject();
            initialConnection.close();

            // Create the bot class from the byte array
            bc.bytesToClass();

            System.out.println("Configuration received and read successfully");
            System.out.println("Number of rounds: " + bc.getRounds());
            System.out.println("Opponents: " + bc.getOpponents());
            System.out.println("Waiting for jobs from controller");

            // Process incoming jobs
            while (true) processJob();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Controller disconnected - resetting");
            reset();
        }
    }

    /**
     * Processes an incoming job containing x number of bots
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void processJob() throws IOException, ClassNotFoundException {

        // Read bot array from controller
        Socket connection = serverSocket.accept();
        ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
        Bot[] bots = (Bot[]) in.readObject();

        System.out.println("Received job from controller");
        System.out.println("Running battles");

        // Run a battle for each bot
        for (int i = 0; i < bots.length; i++) {
            System.out.println("Running battle for bot: " + i);
            BotWriter.writeForExecution(bots[i]);
            bots[i].setFitness(runBattle());
        }

        System.out.println("Completed all battles");

        // Send the results back to the controller
        ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
        out.writeObject(bots);
        connection.close();

        System.out.println("Results sent back to controller");
    }

    /**
     * Runs a battle for one bot
     *
     * @return The fitness of the bot
     * @throws IOException
     */
    private static double runBattle() throws IOException {

        String me = "gabot.EvoTank_RhysStreefland*";  // don't ask why * is needed; Took hours to figure this out

        // Set up and run the battle
        RobotSpecification[] selectedBots = engine.getLocalRepository(me + "," + bc.getOpponents());
        BattleSpecification battleSpec = new BattleSpecification(bc.getRounds(), battlefield, selectedBots);
        engine.runBattle(battleSpec, true);

        BattleResults[] br = battleListener.getResults();

        double myScore = 0;
        double opponentTotalScore = 0;
        int numberOfOpponents = br.length - 1;

        // Get the score of the genetic bot and its opponents
        for (BattleResults result : br) {
            if (result.getTeamLeaderName().equals(me)) {
                myScore = result.getScore();
            } else {
                opponentTotalScore += result.getScore();
            }
        }

        double totalScore = myScore + opponentTotalScore;

        // Penalty if no bots score any point (i.e. all opponents are sitting ducks)
        if (totalScore == 0) {
            totalScore = numberOfOpponents * 5000;
        }

        // Calculate and return the fitness (score of genetic bot as a percentage of the total score)
        return ((myScore + 1) / (totalScore + 1)) * 100;
    }

    /**
     * Reset the TCP socket
     */
    private static void reset() {
        try {
            serverSocket.close();

            while (!serverSocket.isClosed()) {
                Thread.sleep(500);
            }

            setup();
        } catch (IOException | InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
