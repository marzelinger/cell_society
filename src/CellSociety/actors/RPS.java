/**
 * @author mpz5
 */

package CellSociety.actors;

import CellSociety.grids.Grid;
import CellSociety.simulations.Simulation;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;
import java.util.Random;

public class RPS extends Actor{
    public static final int MAX_LEVEL = 10;
    public static final int MIN_LEVEL = 0;
    public static final int ROCK = 1;
    public static final int PAPER = 2;
    public static final int SCISSOR= 3;
    public static final String ROCK_NAME = "rock";
    public static final String PAPER_NAME = "paper";
    public static final String SCISSOR_NAME = "scissor";
    public static final String WHITE_SPACE_NAME = "white space";
    private String colorRock;
    private String colorPaper;
    private String colorScissor;
    private String colorEmpty;


    private int level;
    // empty is 0, rock is 1, paper is 2, scissor is 3 replace this later with an ENUM class
    private int rps;
    //paper beats rock, rock beats scissor, scissor beats paper
    private List<int[]> currNeighborLocations;

    public RPS(double s, int r, int c, Grid grid, Simulation simulation, int type,String rockColor, String paperColor, String scissorColor, String emptyColor){
        super(s, r, c, grid, simulation);
        level = MAX_LEVEL;
        rps = type;
        colorRock = rockColor;
        colorPaper = paperColor;
        colorScissor = scissorColor;
        colorEmpty = emptyColor;
    }


    protected boolean loses(int otherRps){
        int winningRps = rps + 1 <= SCISSOR ? rps + 1 : ROCK;
        return winningRps == otherRps;
    }

    @Override
    public void saveCurrentNeighborStates() {
        currNeighborLocations = getGrid().getNeighborLocations(getLocation()[0], getLocation()[1]);
    }

    protected void loseHealth(int otherRps){
        level--;
        if(level < MIN_LEVEL){
            rps = otherRps;
        }
    }

    protected void attack(RPS neighbor){
        neighbor.loseHealth(rps);
        level = level + 1 <= MAX_LEVEL ? level + 1: level;
    }

    public boolean canSpread(){
        return rps > 0 && level > 0;
    }

    public int getRPS(){
        return rps;
    }

    public int getLevel(){
        return level;
    }

    @Override
    public void act() {
        Random chooser = new Random();
        int neighborNum = chooser.nextInt(currNeighborLocations.size());
        int[] location = currNeighborLocations.get(neighborNum);
        RPS neighbor = (RPS)getGrid().getActor(location[0], location[1]);
        if(rps > 0 && neighbor.loses(rps)) {
            attack(neighbor);
        }
        else if(rps == 0 && neighbor.canSpread()){
            rps = neighbor.getRPS();
            level = neighbor.getLevel() - 1;
        }
    }

    public String getName(){
        if(rps == 0){
            return WHITE_SPACE_NAME;
        }
        if(rps == ROCK){
            return ROCK_NAME;
        }
        else if(rps == PAPER){
            return PAPER_NAME;
        }
        else{
            return SCISSOR_NAME;
        }
    }

    @Override
    public Paint getColor() {
        if(rps == 0){
            return Color.valueOf(colorEmpty);
        }
        if(rps == ROCK){
            return Color.valueOf(colorRock);
        }
        else if(rps == SCISSOR){
            return Color.valueOf(colorScissor);
        }
        else{
            return Color.valueOf(colorPaper);
        }
    }
}
