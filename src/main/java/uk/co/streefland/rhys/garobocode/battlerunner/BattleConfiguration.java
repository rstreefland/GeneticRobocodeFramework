package uk.co.streefland.rhys.garobocode.battlerunner;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Represents the battle configuration. An object of this class is sent to all workers at the beginning of each run to
 * ensure they all have the same configuration parameters.
 */
public class BattleConfiguration implements Serializable {

    private static final long serialVersionUID = 2L;

    private final int rounds;
    private final String opponents;
    private byte[] botClassBytes;

    public BattleConfiguration(int numberRounds, String opponents) throws IOException {
        this.rounds = numberRounds;
        this.opponents = opponents;

        classToBytes();
    }

    /**
     * Reads the class file and stores it as a byte array
     *
     * @throws IOException
     */
    private void classToBytes() throws IOException {

        // Read in the class file and store as a byte array
        Path path = Paths.get("target/classes/gabot/EvoTank_RhysStreefland.class");
        botClassBytes = java.nio.file.Files.readAllBytes(path);
    }

    /**
     * Writes the bot class bytes to a .class file
     *
     * @throws IOException
     */
    public void bytesToClass() throws IOException {

        // Make the directory if it doesn't already exist
        new File("robocode/robots/gabot").mkdirs();

        // Write the bot class bytes to a class file
        Path path = Paths.get("robocode/robots/gabot/EvoTank_RhysStreefland.class");
        java.nio.file.Files.write(path, botClassBytes);
    }

    public int getRounds() {
        return rounds;
    }

    public String getOpponents() {
        return opponents;
    }
}
