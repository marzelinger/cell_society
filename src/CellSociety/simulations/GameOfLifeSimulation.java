/**
 * @author mpz5
 */

package CellSociety.simulations;

import CellSociety.XMLParser;
import CellSociety.actors.Actor;
import CellSociety.actors.GameOfLifeActor;

import java.util.Map;
import java.util.Random;

public class GameOfLifeSimulation extends Simulation {

    private double myInitialAliveProbability;
    private Random dice = new Random();

    public GameOfLifeSimulation(Map<String, String> args) {
        super(args);

        myInitialAliveProbability = Double.parseDouble(getMyArgs().get(XMLParser.PROBABILITY_KEY));
        //create a rectangular grid that has # rows, # columns, and size a double of the side length of cell

        fillGrid();
    }

    @Override
    public String[] getActorNames() {
        return new String[]{GameOfLifeActor.ALIVE_NAME, GameOfLifeActor.DEAD_NAME};
    }

    @Override
    public double[] getThresholdRange(){
        return new double[]{-1, -1, -1};
    }

    public void fillGrid() {
        getMyGrid().fillGridRandomly(1, this);
    }

    public Actor createActor(int row, int col) {
        boolean isAlive = dice.nextInt(PERCENTAGE_BOUND) < (myInitialAliveProbability * PERCENTAGE_BOUND);
        return new GameOfLifeActor(getMyCellSize(), row, col, getMyGrid(), isAlive, this, getMyArgs().get("ActorColor1"), getMyArgs().get("ActorColor2"));
    }

    ;

    public Actor createActor(int row, int col, int whichActor) {
        if (whichActor == 1) {
            return new GameOfLifeActor(getMyCellSize(), row, col, getMyGrid(), true, this, getMyArgs().get("ActorColor1"), getMyArgs().get("ActorColor2"));
        }
        return null;
    }
}
