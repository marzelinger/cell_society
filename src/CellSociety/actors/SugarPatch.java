/**
 * @author mpz5
 */

package CellSociety.actors;

import CellSociety.ShapeMaker;
import CellSociety.grids.Grid;
import CellSociety.simulations.Simulation;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

public class SugarPatch extends Actor implements Comparable  {
    public static final int PRESET_1 = 1;
    public static final int PRESET_2 = 2;
    public static final int MAX_COLOR = 255;
    public static final String SUGARPATCH_NAME = "sugar patch";

    private int amountOfSugar;
    private int maximumSugarCapacity;
    private Agent agent;
    private int sugarGrowBackRate;
    private int sugarGrowBackInterval;
    private int stepsRemaining = sugarGrowBackInterval;
    private int colorChangeInterval;

    public SugarPatch(double s, int r, int c, Grid grid, Simulation simulation, int maxSugar, int preset, int colorInterval){
        super(s, r, c, grid, simulation);
        maximumSugarCapacity = maxSugar;
        amountOfSugar = maxSugar;
        sugarGrowBackRate = 1;
        colorChangeInterval = colorInterval;
        if(preset == PRESET_1){
            sugarGrowBackInterval = 1;
        }
        else{
            sugarGrowBackInterval = 2;
        }
    }

    @Override
    public int compareTo(Object obj){
        SugarPatch patch = (SugarPatch)obj;
        return amountOfSugar - patch.getAmountOfSugar();
    }

    @Override
    public boolean equals(Object obj){
        return amountOfSugar == ((SugarPatch)obj).getAmountOfSugar();
    }

    public int getAmountOfSugar(){
        return amountOfSugar;
    }

    public boolean checkEmpty(){
        return agent ==null;
    }

    public Agent getAgent(){
        return agent;
    }

    public int getDistance(int[] otherLocation){
        return (Math.abs(otherLocation[0] - getLocation()[0]) + Math.abs(otherLocation[1] - getLocation()[1]));
    }

    public void addAgent(Agent a){
        agent = a;
    }

    public void removeAgent(){
        agent = null;
    }

    @Override
    public void saveCurrentNeighborStates() {
        //this is empty because the changes in state of each patch do not impact the state of any other patches. Therefore, there is no need to save any additional state here.
    }

    @Override
    public String getName() {
        return SUGARPATCH_NAME;
    }

    @Override
    public Paint getColor() {
        return Color.rgb(MAX_COLOR, MAX_COLOR - amountOfSugar * colorChangeInterval, amountOfSugar == 0? MAX_COLOR : 0);
    }

    @Override
    public void act() {
        if(stepsRemaining == 0){
            amountOfSugar = amountOfSugar + sugarGrowBackRate > maximumSugarCapacity ? amountOfSugar : amountOfSugar + sugarGrowBackRate;
            stepsRemaining = sugarGrowBackInterval;
        }
        else{
            stepsRemaining--;
        }
    }

    public int takeSugar(){
        int sugarTaken = amountOfSugar;
        amountOfSugar = 0;
        return sugarTaken;
    }

    @Override
    public void draw(String type, Group root){
        int row = getRow();
        int col = getCol();
        double size = getSize();
        root.getChildren().add(ShapeMaker.getInstance().drawCell(row, col, size, getColor(), Color.BLACK, type));
        if (agent != null) {
            root.getChildren().add(ShapeMaker.getInstance().drawPatchedAgent(row, col, size, type));
        }
    }
}
