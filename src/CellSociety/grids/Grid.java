/**
 * @author ral30, mpz5
 */

/**
 * Still not sure what future grids to create
 * Once future grids are created, the commonalities will be abstracted
 */
package CellSociety.grids;

import CellSociety.EnumParams;
import CellSociety.actors.Actor;
import CellSociety.simulations.Simulation;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.*;


public abstract class Grid {
    public static final double X_LOCATION = 20;
    public static final double Y_LOCATION = 20;
    public static final Paint GRID_COLOR = Color.LIGHTGRAY;
    public static final int PERCENTAGE_BOUND = 100;

    private Random dice = new Random();
    private Actor[][] myGrid;

    private int myRows;
    private int myCols;
    private Rectangle myView;
    private boolean isToroidal;
    EnumParams.GridNeighborsTypes neighborDirections;

    public Grid(int gridRows, int gridCols, double gridSize, String neighborsTypes, String edgeTypes){
        myGrid = new Actor[gridRows][gridCols];
        myView = new Rectangle(X_LOCATION,Y_LOCATION, gridSize, gridSize);
        myView.setFill(GRID_COLOR);
        myRows = gridRows;
        myCols = gridCols;
        isToroidal = (edgeTypes == EnumParams.GridEdgeTypes.Toroidal.name());
        neighborDirections = EnumParams.GridNeighborsTypes.valueOf(neighborsTypes);
    }

    public Map<String, Integer> getPopulations(Simulation simulation){
        Map<String, Integer> populations = new HashMap<>();
        for(String name : simulation.getActorNames()){
            populations.put(name, 0);
        }
        List<Actor> actors = getAllActors();
        for(Actor a : actors){
            if(populations.containsKey(a.getName())){
                populations.put(a.getName(), populations.get(a.getName()) + 1);
            }
        }
        return populations;
    }

    protected Random getDice(){
        return dice;
    }

    public void fillGridRandomly(double fullProbability, Simulation simulation){
        for (int i=0; i<myRows; i++) {
            for (int j=0; j<myCols; j++) {
                if(getDice().nextInt(PERCENTAGE_BOUND) < (fullProbability* PERCENTAGE_BOUND)){
                    simulation.createActor(i, j);
                }
            }
        }
    }

    public void fillGridFromArrayOfString(String initGrid,Simulation simulation){
        String[] initArr = initGrid.split(",");
        int count = 0;
        for (int i=0; i<myRows; i++){
            for(int j=0; j<myCols; j++){
                if(count<initArr.length){
                    simulation.createActor(i,j,Integer.parseInt(initArr[count]));
                    count++;
                }
                else{
                    simulation.createActor(i,j,0);

                }

            }
        }

    }

    protected int getMyRows(){
        return myRows;
    }

    protected int getMyCols(){
        return myCols;
    }

    public abstract String getType();

    public boolean isOuterBoundary(int row, int col){
        return (row == 0 || row == myRows - 1 || col == 0 || col == myCols - 1);
    }

    /**
     * Function to return all currently empty locations in the grid. Used to move actors to
     * a random empty location.
     * @return ArrayList of int[] representing empty locations
     */
    public List<int[]> getEmptyLocations(){
        ArrayList<int[]> emptyLocations = new ArrayList<>();
        for(int i = 0; i < myRows; i ++){
            for(int j = 0; j < myCols; j ++){
                if(myGrid[i][j] == null){
                    emptyLocations.add(new int[] {i, j});
                }
            }
        }
        return emptyLocations;
    }

    public Actor getActor(int row, int col){
        return myGrid[row][col];
    }

    public int getMaxPopulation(){ return myRows*myCols; }

    public List<Actor> getAllActors(){
        ArrayList<Actor> actors = new ArrayList<>();
        for (int i=0; i<myRows; i++) {
            for (int j=0; j<myCols; j++) {
                if(myGrid[i][j] != null){
                    actors.add(myGrid[i][j]);
                }
            }
        }
        return actors;
    }

    protected boolean isValidLocation(int row, int col){
        return (col >=0 && col < myCols && row >= 0 && row < myRows);
    }

    public void addActor(Actor actor, int row, int col){
        myGrid[row][col] = actor;
    }

    public void removeActor(Actor actor,int row, int col){
        myGrid[row][col] = null;
    }

    public Rectangle getView(){
        return myView;
    }

    public abstract List<int[]> getNeighborLocations(int row, int col, int distance, EnumParams.GridNeighborsTypes type);

    public List<int[]> getNeighborLocations(int row, int col, int distance){
        return getNeighborLocations(row, col, distance, neighborDirections);
    };

    // distance defaults to 1
    public List<int[]> getNeighborLocations(int row, int col){
        return getNeighborLocations(row, col, 1, neighborDirections);
    };

    protected List<int[]> getNeighborLocationsHelper(ArrayList<int[]> neighborLocations, int row, int col, Direction[] locations, int distance){
        for(int d = 0; d < distance; d++){
            for(int i = 0; i < locations.length; i ++) {
                int newRow = locations[i].addDistanceX(d) + row;
                int newCol = locations[i].addDistanceY(d) + col;
                if (isValidLocation(newRow, newCol)) {
                    neighborLocations.add(new int[]{newRow, newCol});
                }
                else if(isToroidal){
                    addToroidalNeighbor(neighborLocations, newRow, newCol);
                }
            }
        }
        return neighborLocations;
    }

    private void addToroidalNeighbor(ArrayList<int[]> neighborLocations, int neighborRow, int neighborCol){
        int tRow = neighborRow;
        int tCol = neighborCol;
        if(tRow < 0) {
            tRow = myRows + tRow;
        }
        else if(tRow > myRows - 1){
            tRow = tRow - myRows;
        }
        if(tCol < 0){
            tCol = myCols + tCol;
        }
        else if(tCol > myCols - 1){
            tCol= tCol - myCols;
        }
        if(isValidLocation(tRow, tCol) && !neighborLocations.contains(new int[] {tRow, tCol})){
            neighborLocations.add(new int[] {tRow, tCol});
        }
    }

}
