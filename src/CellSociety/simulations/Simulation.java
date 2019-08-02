package CellSociety.simulations;

import CellSociety.XMLParser;
import CellSociety.EnumParams;
import CellSociety.actors.Actor;
import CellSociety.grids.Grid;
import CellSociety.grids.HexagonalGrid;
import CellSociety.grids.RectangularGrid;

import CellSociety.grids.TriangularGrid;
import javafx.scene.Group;

import java.util.Map;

public abstract class Simulation {
    public static final double SIMULATION_SCREEN_SIZE = 400;
    public static final int PERCENTAGE_BOUND = 100;

    private Grid myGrid;
    private boolean isOn;
    private Map<String, String> myArgs;
    private int myRow;
    private int myColumn;
    private double myCellSize;
    private double threshold;

    public Simulation(Map<String, String> args){
        threshold = Double.parseDouble(args.get(XMLParser.THRESHOLD_KEY));
        isOn = false;
        myArgs = args;
        myRow = Integer.parseInt(myArgs.get(XMLParser.ROWS_KEY));
        myColumn = Integer.parseInt(myArgs.get(XMLParser.COLS_KEY));

        myCellSize = calculateCellSize(SIMULATION_SCREEN_SIZE, myRow, myColumn, myArgs.get(XMLParser.GRID_SHAPE_KEY));

        myGrid = createGrid();
    }

    public double calculateCellSize(double screenSize, int row, int col, String type){
        double byRow, byCol;
        if (type.equals(EnumParams.GridShapeTypes.Rectangle.name())) {
            byCol = screenSize / col;
            byRow = screenSize / row;
        } else if (type.equals(EnumParams.GridShapeTypes.Hexagon.name())) {
            byRow = screenSize / 1.5 / row;
            byCol = screenSize / Math.cos(Math.toRadians(30)) / 2 / (col+1);
        } else {
            byRow = screenSize / Math.cos(Math.toRadians(30)) / row;
            byCol = screenSize / col * 2;
        }
        return Math.min(byCol, byRow);
    }

    public abstract String[] getActorNames();

    public Grid createGrid(){
        String gridShape = myArgs.get(XMLParser.GRID_SHAPE_KEY);
        String neighborType = myArgs.get(XMLParser.GRID_NEIGHBORS_KEY);
        String edgeType =  myArgs.get(XMLParser.GRID_EDGE_KEY);
        if(gridShape.equals(EnumParams.GridShapeTypes.Rectangle.name())){
            return new RectangularGrid(myRow, myColumn, SIMULATION_SCREEN_SIZE, neighborType, edgeType);
        }
        else if(gridShape.equals(EnumParams.GridShapeTypes.Hexagon.name())){
            return new HexagonalGrid(myRow, myColumn, SIMULATION_SCREEN_SIZE, neighborType, edgeType);
        }
        else{
            return new TriangularGrid(myRow, myColumn, SIMULATION_SCREEN_SIZE, neighborType, edgeType);
        }
    }

    public abstract double[] getThresholdRange();

    public void updateThreshold(double newThreshold){
        threshold  = newThreshold;
    }

    public double getThreshold(){
        return threshold;
    }


    protected int getMyRow(){ return myRow; }
    protected int getMyColumn(){ return myColumn; }
    protected double getMyCellSize(){ return myCellSize; }
    protected Map<String, String> getMyArgs(){ return myArgs; }
    public Grid getMyGrid() {return myGrid;}

    public boolean getIsOn() { return isOn; }

    //fill myGrid with actors: called when reset a simulation, or at the very first time in constructor
    abstract void fillGrid();

    //update grid when simulation is running

    public void step(Group root) {
            updateAllCells();
            addAllViews(root);
    }

    // handle stop/start pressed
    public void stopPressed() { isOn = false; }
    public void startPressed() { isOn = true; }

    public void resetPressed() {
        isOn = false;
        myGrid = createGrid();
        fillGrid();
    }

    //GIVE THIS TO UI
    public void addAllViews(Group root){
        Group allSimulationViews = new Group();
        allSimulationViews.getChildren().add(getMyGrid().getView());
        for (Actor actor : myGrid.getAllActors()) {
            actor.draw(myGrid.getType(), allSimulationViews);
        }
        root.getChildren().addAll(allSimulationViews.getChildren());
    };

    protected void updateAllCells(){
        //first store
        for (Actor actor : myGrid.getAllActors()) {
            actor.updateNeighbors();
            actor.saveCurrentNeighborStates();
        }
        //then act
        for (Actor actor : myGrid.getAllActors()) {
            actor.act();
        }
    };

    public abstract Actor createActor(int row, int col);

    public abstract Actor createActor(int row, int col, int whichActor);

}
