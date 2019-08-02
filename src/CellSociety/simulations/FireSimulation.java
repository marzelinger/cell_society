/**
 * @author mpz5
 */

package CellSociety.simulations;

import CellSociety.XMLParser;
import CellSociety.actors.Actor;
import CellSociety.actors.Tree;

import java.util.Map;
import java.util.Random;

public class FireSimulation extends Simulation{

    private double isBurningProbability;
    private double isTreeProbability;
    private Random dice = new Random();

    public FireSimulation(Map<String, String> args) {
        super(args);
        isBurningProbability = Double.parseDouble(getMyArgs().get(XMLParser.PROBABILITY_KEY));
        isTreeProbability = 1 - (Double.parseDouble(getMyArgs().get(XMLParser.EMPTY_PROBABILITY_KEY)));

        //create a rectangular grid that has # rows, # columns, and size a double of the side length of cell
        fillGrid();

    }

    @Override
    public double[] getThresholdRange(){
        return new double[]{0, 1, .1};
    }

    public void fillGrid(){
        getMyGrid().fillGridRandomly(isTreeProbability, this);
    }

    @Override
    public Actor createActor(int row, int col) {
        if(getMyGrid().isOuterBoundary(row, col)){
            return null;
        }
        else if(dice.nextInt(PERCENTAGE_BOUND) < (isBurningProbability * PERCENTAGE_BOUND)){
            return new Tree(getMyCellSize(), row, col, getMyGrid(), true, this,getMyArgs().get("ActorColor1"),getMyArgs().get("ActorColor2"));
        }
        else{
            return new Tree(getMyCellSize(), row, col, getMyGrid(), false, this,getMyArgs().get("ActorColor1"),getMyArgs().get("ActorColor2"));
        }
    }

    @Override
    public String[] getActorNames() {
        return new String[]{Tree.BURNING_NAME, Tree.NOT_BURNING_NAME};
    }

    @Override
    public Actor createActor(int row, int col, int whichActor){
        if(getMyGrid().isOuterBoundary(row, col)){
            return null;
        }
        if(whichActor == 1){
            return new Tree(getMyCellSize(), row, col, getMyGrid(), false, this,getMyArgs().get("ActorColor1"),getMyArgs().get("ActorColor2"));
        }
        if(whichActor == 2){
            return new Tree(getMyCellSize(), row, col, getMyGrid(), false, this,getMyArgs().get("ActorColor1"),getMyArgs().get("ActorColor2"));
        }
        return null;
    }

}

