/**
 * @author mpz5
 */

package CellSociety.simulations;

import CellSociety.actors.Actor;
import CellSociety.actors.RPS;

import java.util.Map;
import java.util.Random;

public class RockPaperScissorsSimulation extends Simulation{
    public static final int NUM_TYPES = 4;

    private Random dice = new Random();

    public RockPaperScissorsSimulation(Map<String, String> args) {
        super(args);
        fillGrid();
    }

    @Override
    public String[] getActorNames() {
        return new String[]{RPS.ROCK_NAME, RPS.PAPER_NAME, RPS.SCISSOR_NAME, RPS.WHITE_SPACE_NAME};
    }

    @Override
    public double[] getThresholdRange(){
        return new double[]{-1, -1, -1};
    }

    public void fillGrid(){
        getMyGrid().fillGridRandomly(1, this);
    }

    public Actor createActor(int row, int col){
        System.out.println(getMyArgs().get("EmptyColor"));
        return new RPS(getMyCellSize(), row, col, getMyGrid(), this,  dice.nextInt(NUM_TYPES),
            getMyArgs().get("ActorColor1"),getMyArgs().get("ActorColor2"),getMyArgs().get("ActorColor3"),getMyArgs().get("EmptyColor"));
    };

    public Actor createActor(int row, int col, int whichActor){
        return new RPS(getMyCellSize(), row, col, getMyGrid(), this,  whichActor%2,
                getMyArgs().get("ActorColor1"),getMyArgs().get("ActorColor2"),getMyArgs().get("ActorColor3"),getMyArgs().get("EmptyColor"));

    }


}

