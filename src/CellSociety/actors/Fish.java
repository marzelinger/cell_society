/**
 * @author mpz5
 */

package CellSociety.actors;

import CellSociety.CellSociety;
import CellSociety.grids.Grid;
import CellSociety.simulations.Simulation;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Fish extends Animal {

    public static final String FISH_NAME= "Fish";
    private String fishColor;

    public Fish(double s, int r, int c, Grid grid, Simulation simulation,String color){
        super(s, r, c, grid, simulation, true);
        fishColor = color;
    }

    public String getName(){
        return FISH_NAME;
    }

    @Override
    protected Animal createChild(int row, int col) {
        return new Fish(getSize(), row, col, getGrid(), getSim(),fishColor);
    }

    @Override
    public void saveCurrentNeighborStates() {
        return;
    }

    // Fish will attempt to move and breed. Since this is happening at "the same time" as the sharks
    // around it trying to eat it, if there is a fish in the original location of the fish after
    // the fish has attempted to move and attempted to breed, it will check if it has been set to
    // be eaten and remove itself if so.
    public void act(){
        boolean moved = moveToEmptyNeighbor();
        breed();
        if(checkEaten() && !moved){
            removeSelfFromGrid();
        }
        else{
            setEaten(false);
        }
    }

    public Paint getColor(){
        Color col = Color.valueOf(fishColor);
        return col;
    }

}
