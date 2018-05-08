package uk.co.streefland.rhys.garobocode.battlerunner;

import uk.co.streefland.rhys.garobocode.genetic.Bot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Writes the DNA of bots to their respective files
 */
public class BotWriter {

    /**
     * Writes the DNA of a bot to the code.ser file ready for execution
     *
     * @param bot The bot to write to file
     */
    public static void writeForExecution(Bot bot) {

        double[] genes = bot.getGenome();

        // Make the directory if it doesn't exist
        new File("robocode/robots/gabot/EvoTank_RhysStreefland.data").mkdirs();

        try {
            FileOutputStream fos = new FileOutputStream("robocode/robots/gabot/EvoTank_RhysStreefland.data/code.ser");
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(genes);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the fittest bot of each generation to file
     *
     * @param bot        The fittest bot
     * @param id         The id of the current run (date/time)
     * @param generation The generation of the bot
     */
    public static void writeFittest(Bot bot, String id, int generation) {

        double[] genes = bot.getGenome();
        String name = "fittestBot G" + generation;

        // Make the directory if it doesn't exist
        new File("output/" + id).mkdirs();

        try {
            FileOutputStream fos = new FileOutputStream("output/" + id + "/" + name + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(genes);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
