/**
 * @author mpz5
 */
package CellSociety.simulations;

import CellSociety.XMLParser;
import CellSociety.actors.Actor;
import CellSociety.actors.Fish;
import CellSociety.actors.Shark;

import java.util.Map;
import java.util.Random;

public class WatorWorldSimulation extends Simulation{

    private double isFishProbability;
    private double isAnimalProbability;
    private Random dice = new Random();

    public WatorWorldSimulation(Map<String, String> args) {
        super(args);
        isFishProbability = Double.parseDouble(getMyArgs().get(XMLParser.PROBABILITY_KEY));
        isAnimalProbability = 1 - Double.parseDouble(getMyArgs().get(XMLParser.EMPTY_PROBABILITY_KEY));

        //create a rectangular grid that has # rows, # columns, and size a double of the side length of cell

        fillGrid();
    }

    @Override
    public String[] getActorNames() {
        return new String[]{Shark.SHARK_NAME, Fish.FISH_NAME};
    }

    @Override
    public double[] getThresholdRange(){
        return new double[]{1, 10, 1};
    }

    public void fillGrid(){
        getMyGrid().fillGridRandomly(isAnimalProbability, this);
    }

    @Override
    public Actor createActor(int row, int col){
        if(dice.nextInt(PERCENTAGE_BOUND) < (isFishProbability * PERCENTAGE_BOUND)){
            return new Fish(getMyCellSize(), row, col, getMyGrid(), this,getMyArgs().get("ActorColor1"));
        }
        else{
            return new Shark(getMyCellSize(), row, col, getMyGrid(), this,getMyArgs().get("ActorColor2"));
        }
    }
    @Override
    public Actor createActor(int row,int col, int whichActor){
        if(whichActor == 1){
            return new Fish(getMyCellSize(), row, col, getMyGrid(), this,getMyArgs().get("ActorColor1"));
        }
        if(whichActor == 2){
            return new Shark(getMyCellSize(), row, col, getMyGrid(), this,getMyArgs().get("ActorColor2"));
        }
        return null;
    }


}

