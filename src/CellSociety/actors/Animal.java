/**
 * @author mpz5
 */

package CellSociety.actors;

import CellSociety.grids.Grid;
import CellSociety.simulations.Simulation;

public abstract class Animal extends Actor{
    private int stepsToBreed;
    private boolean isEdible;
    private boolean isEaten;

    public Animal(double s, int r, int c, Grid grid, Simulation simulation, boolean edible){
        super(s, r, c, grid, simulation);
        stepsToBreed = (int)simulation.getThreshold();
        isEdible = edible;
        isEaten = false;
    }

    protected boolean checkEdible(){
        return isEdible;
    }

    protected void breed(){
        if(stepsToBreed == 0){
            // If all of the empty locations get taken by neighbor animal breeding, stepsToBreed remains
            // zero and the animal will attempt to breed again next step.
            int[] loc = getRandomEmptyLocation(getEmptyNeighborLocations());
            if(loc[0] >= 0){
                createChild(loc[0], loc[1]);
                stepsToBreed = (int)getSim().getThreshold();
            }
        }
        else{
            stepsToBreed--;
        }
    }

    protected boolean moveToEmptyNeighbor(){
        int[] loc = getRandomEmptyLocation(getEmptyNeighborLocations());
        if(loc[0] >= 0){
            addEmptyLocation(getLocation());
            move(loc[0], loc[1]);
            return true;
        }
        return false;
    }

    public void setEaten(boolean value){
        isEaten = value;
    }

    public boolean checkEaten(){
        return isEaten;
    }



    protected abstract Animal createChild(int row, int col);

}
