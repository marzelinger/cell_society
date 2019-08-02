/**
 * @author mpz5
 */

package CellSociety.actors;

import CellSociety.grids.Grid;
import CellSociety.simulations.Simulation;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class GameOfLifeActor extends Actor {

    public static final int MIN_LIVE_NEIGHBORS = 2;
    public static final int MAX_LIVE_NEIGHBORS = 3;
    public static final int REPRODUCING_LIVE_NEIGHBORS = 3;
    public static final String ALIVE_NAME = "Alive";
    public static final String DEAD_NAME = "Dead";
    private boolean isAlive;
    private int numAliveNeighbors;
    private String aliveColor;
    private String deadColor;

    public GameOfLifeActor(double s, int r, int c, Grid grid, boolean alive, Simulation simulation,String colorAlive, String colorDead){
        super(s, r, c, grid, simulation);
        isAlive = alive;
        aliveColor = colorAlive;
        deadColor = colorDead;

    }

    public String getName(){
        return isAlive ? ALIVE_NAME : DEAD_NAME;
    }

    public Paint getColor(){
        return isAlive ? Color.valueOf(aliveColor) : Color.valueOf(deadColor);
    }

    public boolean checkAlive(){
        return isAlive;
    }

    public void saveCurrentNeighborStates(){
        numAliveNeighbors = 0;
        for(Actor n : getNeighborActors()){
            if(((GameOfLifeActor) n).checkAlive()){
                numAliveNeighbors++;
            }
        }
    }

    @Override
    public void act(){
        if(isAlive) {
            isAlive = checkAliveRules();
        }
        else{
            isAlive = checkDeadRules();
        }
   }

    private boolean checkAliveRules(){
        return numAliveNeighbors == MIN_LIVE_NEIGHBORS || numAliveNeighbors == MAX_LIVE_NEIGHBORS;
    }

    private boolean checkDeadRules(){
        return numAliveNeighbors == REPRODUCING_LIVE_NEIGHBORS;
    }

}
