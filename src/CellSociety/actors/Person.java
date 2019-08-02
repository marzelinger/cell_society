/**
 * @author mpz5
 */

package CellSociety.actors;

import CellSociety.grids.Grid;
import CellSociety.simulations.Simulation;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class Person extends Actor {

    public static final String PERSON_A_NAME = "Person Type A";
    public static final String PERSON_B_NAME = "Person Type B";
    private boolean isTypeA;
    private double numSameNeighbors;
    private double totalNeighbors;
    private List<int[]> emptyGridLocations;
    private String colorA;
    private String colorB;

    public Person(double s, int r, int c, Grid grid, boolean isTypeA, Simulation simulation, String A_color, String B_color){
        super(s, r, c, grid, simulation);
        this.isTypeA = isTypeA;
        colorA = A_color;
        colorB = B_color;
    }

    public String getName(){
        return isTypeA ? PERSON_A_NAME : PERSON_B_NAME;
    }

    public Paint getColor(){
        return isTypeA ? Color.valueOf(colorA) : Color.valueOf(colorB);
    }

    public boolean getIsTypeA(){
        return isTypeA;
    }

    @Override
    public void saveCurrentNeighborStates() {
        numSameNeighbors = 0;
        for(Actor n : getNeighborActors()){
            if(((Person) n).getIsTypeA() == isTypeA){
                numSameNeighbors++;
            }
        }
        totalNeighbors = getNeighborActors().size();
        emptyGridLocations = getGrid().getEmptyLocations();
    }

    public void act(){
        if(numSameNeighbors/totalNeighbors >= getSim().getThreshold()){
            return;
        }
        else{
            int[] loc = getRandomEmptyLocation(emptyGridLocations);
            if(loc[0] >= 0){
                move(loc[0], loc[1]);
            }
        }
    }
}
