/**
 * Class to implement the tree actor. This class assumes that the grid will be set up properly i.e there
 * will be a boundary made up of the outermost row of the grid that is entirely empty.
 * @author mpz5
 */

package CellSociety.actors;

import CellSociety.grids.Grid;
import CellSociety.simulations.Simulation;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Random;

public class Tree extends Actor {
//    private static final Paint NOT_BURNING_COLOR = Color.GREEN;
//    private static final Paint BURNING_COLOR = Color.RED;
    public static final String NOT_BURNING_NAME = "Alive Tree";
    public static final String BURNING_NAME= "Burning Tree";

    private boolean isBurning;
    private int numBurningNeighbors;
    private String colorBurn;
    private String colorAlive;

    public Tree(double s, int r, int c, Grid grid, boolean isBurning, Simulation simulation,String burnColor, String aliveColor){
        super(s, r, c, grid, simulation);
        this.isBurning = isBurning;
        colorBurn = burnColor;
        colorAlive = aliveColor;
    }

    public String getName(){
        return isBurning ? BURNING_NAME : NOT_BURNING_NAME;
    }

    /**
     * public getter for neighboring trees to determine if this tree is burning or not when saving their
     * neighbor states.
     * @return isBurning value
     */
    public boolean checkBurning(){
        return isBurning;
    }

    /**
     * Saves the number of neighboring trees (in the North, East, South, West)
     */
    @Override
    public void saveCurrentNeighborStates() {
        numBurningNeighbors = 0;
        for(Actor n : getNeighborActors()){
            //Only look at neighbors that are directly above/below or to left/right. (NESW)
            if((n.getLocation()[0] == getLocation()[0] || n.getLocation()[1] == getLocation()[1]) && ((Tree) n).checkBurning()){
                numBurningNeighbors++;
            }
        }

    }

    @Override
    public Paint getColor() {
        return isBurning ? Color.valueOf(colorBurn): Color.valueOf(colorAlive);
    }

    @Override
    public void act() {
        if(isBurning){
            // burning trees leave an empty array after 1 step;
            removeSelfFromGrid();
        }
        else{
            Random spread  = new Random();
            if(numBurningNeighbors > 0 && spread.nextDouble() < getSim().getThreshold()){
                isBurning = true;
            }
        }
    }
}



