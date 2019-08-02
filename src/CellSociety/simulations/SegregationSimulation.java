/**
 * @author mpz5
 */

package CellSociety.simulations;

import CellSociety.XMLParser;
import CellSociety.actors.Actor;
import CellSociety.actors.Person;

import java.util.Map;
import java.util.Random;

public class SegregationSimulation extends Simulation{

    private double isPersonAProbability;
    private double isPersonProbability;
    private Random dice = new Random();

    public SegregationSimulation(Map<String, String> args) {
        super(args);
        isPersonAProbability = Double.parseDouble(getMyArgs().get(XMLParser.PROBABILITY_KEY));
        isPersonProbability = 1 - Double.parseDouble(getMyArgs().get(XMLParser.EMPTY_PROBABILITY_KEY));
        //create a rectangular grid that has # rows, # columns, and size a double of the side length of cell

        fillGrid();
    }

    @Override
    public String[] getActorNames() {
        return new String[]{Person.PERSON_A_NAME, Person.PERSON_B_NAME};
    }

    @Override
    public double[] getThresholdRange(){
        return new double[]{0, 1, .1};
    }

    public void fillGrid(){
        String fillMethod = (getMyArgs().get("InitType"));

        if(fillMethod.equals("Probabilistic"))
            getMyGrid().fillGridRandomly(isPersonProbability, this);
        if(fillMethod.equals("UserSet")){
            getMyGrid().fillGridFromArrayOfString(getMyArgs().get("InitArray"),this);
            getMyArgs().put("InitType","Probabilistic");
        }
    }

    public Actor createActor(int row, int col){
        boolean isPersonA = dice.nextInt(PERCENTAGE_BOUND) < (isPersonAProbability * PERCENTAGE_BOUND);
        return new Person(getMyCellSize(), row, col, getMyGrid(), isPersonA, this, getMyArgs().get("ActorColor1"),getMyArgs().get("ActorColor2"));
    };

    public Actor createActor(int row, int col,int whichActor) {
        if (whichActor == 1) {
            return new Person(getMyCellSize(), row, col, getMyGrid(), true, this, getMyArgs().get("ActorColor1"), getMyArgs().get("ActorColor2"));
        }
        if (whichActor == 2){
            return new Person(getMyCellSize(), row, col, getMyGrid(), false, this, getMyArgs().get("ActorColor1"),getMyArgs().get("ActorColor2"));
        }
        return null;
    }

}

