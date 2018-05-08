package gabot;

import robocode.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * The genetic bot class. Loads its behaviour from the code.ser file in its EvoTank_RhysStreefland.data directory
 */
public class EvoTank_RhysStreefland extends AdvancedRobot {

    public static final int CHROMOSOME_COUNT = 2;
    public static final int GENE_COUNT = 21;

    private final String name = "code.ser";
    private double[][] genome = new double[CHROMOSOME_COUNT][];

    public void run() {
        double[] temp = null;

        try {
            temp = readGenes();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Split the array into its genome representation
        genome = divideArray(temp);

        while (true) {
            processAction(genome[0][0], genome[1][0]);
            processAction(genome[0][1], genome[1][1]);
            processAction(genome[0][2], genome[1][2]);
            execute();
        }
    }

    /**
     * Performs some action based on the given gene
     *
     * @param gene
     * @param parameter
     * @param eventValue
     */
    public void processAction(double gene, double parameter) {

        int geneInt = (int) scale(gene, 0, 8);

        switch (geneInt) {
            case 0:
                setFire(parameter);
                break;
            case 1:
                setAhead(parameter);
                break;
            case 2:
                setTurnRight(parameter);
                break;
            case 3:
                setTurnGunRight(parameter);
                break;
            case 4:
                setTurnRadarRight(parameter);
                break;
            case 5:
                setMaxVelocity(parameter);
                break;
            case 6:
                setMaxTurnRate(parameter);
                break;
            case 7:
                execute();
                break;
            case 8:
                break;
            default:
                break;
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {

        processAction(genome[0][3], genome[1][3]);
        processAction(genome[0][4], genome[1][4]);
        processAction(genome[0][5], genome[1][5]);
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {

        processAction(genome[0][6], genome[1][6]);
        processAction(genome[0][7], genome[1][7]);
        processAction(genome[0][8], genome[1][8]);

    }

    @Override
    public void onHitByBullet(HitByBulletEvent event) {

        processAction(genome[0][9], genome[1][9]);
        processAction(genome[0][10], genome[1][10]);
        processAction(genome[0][11], genome[1][11]);
    }

    @Override
    public void onBulletHit(BulletHitEvent event) {

        processAction(genome[0][12], genome[1][12]);
        processAction(genome[0][13], genome[1][13]);
        processAction(genome[0][14], genome[1][14]);
    }

    @Override
    public void onBulletMissed(BulletMissedEvent event) {

        processAction(genome[0][15], genome[1][15]);
        processAction(genome[0][16], genome[1][16]);
        processAction(genome[0][17], genome[1][17]);
    }

    @Override
    public void onHitWall(HitWallEvent event) {

        processAction(genome[0][18], genome[1][18]);
        processAction(genome[0][19], genome[1][19]);
        processAction(genome[0][20], genome[1][20]);
    }

    @Override
    public void setFire(double power) {
        power = scale(power, Rules.MIN_BULLET_POWER, Rules.MAX_BULLET_POWER);
        super.setFire(power);
    }

    @Override
    public void setAhead(double distance) {
        distance = scale(distance, -800, 800);
        super.setAhead(distance);
    }

    @Override
    public void setTurnRight(double degrees) {
        degrees = scale(degrees, -360, 360);
        super.setTurnRight(degrees);
    }

    @Override
    public void setTurnGunRight(double degrees) {
        degrees = scale(degrees, -360, 360);
        super.setTurnGunRight(degrees);
    }

    @Override
    public void setTurnRadarRight(double degrees) {
        degrees = scale(degrees, -360, 360);
        super.setTurnRadarRight(degrees);
    }

    @Override
    public void setMaxVelocity(double newMaxVelocity) {

        newMaxVelocity = scale(newMaxVelocity, 1, Rules.MAX_VELOCITY);
        super.setMaxVelocity(newMaxVelocity);
    }

    @Override
    public void setMaxTurnRate(double newMaxTurnRate) {

        newMaxTurnRate = scale(newMaxTurnRate, 1, Rules.MAX_TURN_RATE);
        super.setMaxVelocity(newMaxTurnRate);
    }

    /**
     * Scales a value to its representation within a given range
     *
     * @param oldValue The value to scale
     * @param newMin   The new minimum
     * @param newMax   The new maximum
     * @return The scaled value
     */
    private double scale(double oldValue, double newMin, double newMax) {
        return (oldValue / ((1000) / (newMax - newMin))) + newMin;
    }

    /**
     * Loads the genome from the code.ser file
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private double[] readGenes() throws IOException, ClassNotFoundException {

        FileInputStream fis = new FileInputStream(getDataFile(name));
        ObjectInputStream in = new ObjectInputStream(fis);
        double[] genes = (double[]) in.readObject();
        in.close();

        return genes;
    }

    /**
     * Divides the 1D DNA array into its double[chromosome][gene] representation
     *
     * @param source The 1D DNA array
     * @return 2D array of format double[chromosome][gene]
     */
    private double[][] divideArray(double[] source) {

        int chunkSize = (int) Math.floor(source.length / (double) CHROMOSOME_COUNT);
        double[][] divided = new double[CHROMOSOME_COUNT][chunkSize];
        int start = 0;

        for (int i = 0; i < divided.length; i++) {
            if (start + chunkSize > source.length) {
                System.arraycopy(source, start, divided[i], 0, source.length - start);
            } else {
                System.arraycopy(source, start, divided[i], 0, chunkSize);
            }
            start += chunkSize;
        }

        return divided;
    }
}
