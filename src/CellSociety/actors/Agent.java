/**
 * @author mpz5
 */
package CellSociety.actors;

import CellSociety.grids.Grid;
import CellSociety.simulations.Simulation;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.*;

public class Agent extends Actor {
    public static final String AGENT_NAME = "agent";
    private int sugar;
    private int sugarMetabolism;
    private int vision;
    private SugarPatch currPatch;
    private SugarPatch nextPatch;

    public Agent(double s, int r, int c, Grid grid, Simulation simulation, int visionDistance, int metabolism, int initialSugar, SugarPatch location){
        super(s, r, c, grid, simulation);
        removeSelfFromGrid();
        location.addSelfToGrid(r, c);
        vision = visionDistance;
        sugarMetabolism = metabolism;
        sugar = initialSugar;
        currPatch = location;
    }

    @Override
    public String getName() {
        return AGENT_NAME;
    }

    @Override
    public Paint getColor() {
        return Color.BLACK;
    }

    @Override
    public void saveCurrentNeighborStates() {
        //not needed, don't update patches until after we update all agents.
    }

    public void determineNextPatch(){
        List<int[]> patchLocations = getGrid().getNeighborLocations(currPatch.getLocation()[0], currPatch.getLocation()[1], vision);
        int minDistance = Integer.MAX_VALUE;
        int maxSugar = Integer.MIN_VALUE;
        for(int[] loc : patchLocations){
            SugarPatch potentialPatch = (SugarPatch)getGrid().getActor(loc[0], loc[1]);
            int distance = currPatch.getDistance(loc);
            int patchSugar = potentialPatch.getAmountOfSugar();
            if(potentialPatch.checkEmpty() && ((patchSugar > maxSugar) || (patchSugar == maxSugar && distance < minDistance))){
                maxSugar = patchSugar;
                minDistance = distance;
                nextPatch = potentialPatch;
            }
        }
    }
    @Override
    public void act() {
        determineNextPatch();
        currPatch.removeAgent();
        nextPatch.addAgent(this);
        currPatch = nextPatch;
        sugar = sugar + nextPatch.takeSugar() - sugarMetabolism;
        if(sugar <= 0){
            currPatch.removeAgent();
        }
    }
}
