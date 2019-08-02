/**
 * Implementation for shark class. Assumes that at each step every shark can attempt to either eat OR move
 * if a shark attempts to eat, and finds all neighboring fish have been eaten then it acts as if it "shared"
 * the fish with a neighboring shark, meaning it does not then move. Potential Situations:
 * 1) Shark finds a neighbor fish and decides to eat it
 *      a) That fish has already acted and therefore this is a valid eat. (i.e if the fish is before the shark in
 *      the iteration of the array (from top left to bottom right).
 *      b) The fish has not already acted and therefore could move.
 *          i) The fish acts and stays where it is. Therefore the fish should be eaten (removed from grid)
 *          ii) The fish acts and moves to an empty neighbor space. There is then no fish in the location.
 *          The shark will then not get to eat anything. Nothing will change.
 *          iii) The fish acts and moves to a new location. It then breeds and the new fish it breeds ends up in the
 *          original fish's location. Meaning there is now a new fish in the location that the shark had planned on eating
 *          the original fish from.
 *              * In this case, assume that the new fish will NOT be eaten because the fish would not have existed in the
 *              state of the grid when the shark was making the eat/move decision.
 *          iv) The fish acts and moves to a new location. A different fish moves into the location of the old fish. That new fish
 *          was marked to be eaten by a different shark.
 *              * In this case the fish will not be eaten because it was not the original fish that the shark tried to eat.
 *          v) The fish acts and moves to a new location. A different fish moves into the location of the old fish. That fish
 *          was not marked as eaten by any shark.
 *              * This will be treated the same way as the situation above.
 * 2) Shark Breeds:
 *      a) Shark finds an empty spot and breeds a new shark into it. The steps left to breed are reset to the breeding period.
 *      b) Shark finds an empty spot and PLANS to breed a new shark into it, however a prior animal has either already
 *      placed a baby there or has moved there. In this case, the shark will attempt to breed again the next step.
 *
 * @author mpz5
 */

package CellSociety.actors;

import CellSociety.grids.Grid;
import CellSociety.simulations.Simulation;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class Shark extends Animal {
    public static final String SHARK_NAME = "Shark";
    private String sharkColor;

    private List<int[]> edibleNeighborLocations = new ArrayList<>();

    public Shark(double s, int r, int c, Grid grid, Simulation simulation,String color){
        super(s, r, c, grid, simulation, false);
        sharkColor = color;
    }

    public String getName(){
        return SHARK_NAME;
    }

    @Override
    protected Animal createChild(int row, int col) {
        return new Shark(getSize(), row, col, getGrid(), getSim(),sharkColor);
    }

    @Override
    public void saveCurrentNeighborStates() {
        edibleNeighborLocations.clear();
        for(Actor n : getNeighborActors()){
            if(((Animal)n).checkEdible()){
               edibleNeighborLocations.add(n.getLocation());
            }
        }
    }

    public void act(){
        if(!edibleNeighborLocations.isEmpty()){
            eatNeighbor();
        }
        else {
            moveToEmptyNeighbor();
        }
        breed();
    }

    /**
     * Shark will try to find a fish neighbor that has not already been set to be eaten.
     */
    private void eatNeighbor(){
        for(int[] loc : edibleNeighborLocations){
            Animal neighbor = (Animal) getGrid().getActor(loc[0], loc[1]);
            if(neighbor != null && neighbor.checkEdible() && !neighbor.checkEaten()){
                neighbor.setEaten(true);
                // Check to see if the fish has already acted
                if(neighbor.getLocation()[1] < getLocation()[1] || (neighbor.getLocation()[1] == getLocation()[1] && neighbor.getLocation()[0] < neighbor.getLocation()[0])){
                    neighbor.removeSelfFromGrid();
                }
            }
        }
    }

    public Paint getColor(){
        return Color.valueOf(sharkColor);
    }

}
