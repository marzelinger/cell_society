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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Actor {
    private int row;
    private int col;
    private List<int[]> neighborLocations;
    private List<int[]> emptyNeighborLocations;
    private List<Actor> neighborActors;
    private Grid g;
    private double size;
    private Simulation sim;

    public Actor(double s, int r, int c, Grid grid, Simulation simulation){
        sim = simulation;
        g = grid;
        neighborLocations = grid.getNeighborLocations(r, c);
        updateNeighbors();
        row = r;
        col = c;
        size = s;
        addSelfToGrid(r, c);
    }

    protected Simulation getSim(){
        return sim;
    }

    protected Grid getGrid(){
        return g;
    }

    protected int getRow() { return row;}

    protected int getCol() { return col;}

    protected double getSize(){
        return size;
    }

    /**
     * Getter for child classes to get neighbor actors
     * @return neighborActors
     */
    protected List<Actor> getNeighborActors(){
        ArrayList<Actor> neighborActorsCopy = new ArrayList<>();
        neighborActorsCopy.addAll(neighborActors);
        return neighborActorsCopy;
    }

    /**
     * Getter for child classes to get empty neighbor locations
     * @return emptyNeighborLocations
     */
    protected List<int[]> getEmptyNeighborLocations(){
        ArrayList<int[]> emptyNeighborLocationsCopy = new ArrayList<>();
        emptyNeighborLocationsCopy.addAll(emptyNeighborLocations);
        return emptyNeighborLocationsCopy;
    }

    protected void addEmptyLocation(int[] location){
        emptyNeighborLocations.add(location);
    }

    /**
     * Adds actor to grid in designated location.
     * @param newRow new row index
     * @param newCol new col index
     */
    protected void addSelfToGrid(int newRow, int newCol){
        g.addActor(this, newRow, newCol);
    }

    /**
     * Removes actor from grid.
     */
    protected void removeSelfFromGrid(){
        g.removeActor(this, row, col);
    }

    /**
     * Used to get the name of an actor (i.e Fish or Shark) for maintaining populations to plot.
     * @return name
     */
    public abstract String getName();

    /**
     * Public getter method to access location of actor.
     * @return [row index, col index]
     */
    public int[] getLocation(){
        return new int[] {row, col};
    }

    /**
     * Calculates actor x location based on row in grid and size of each cell(actor)
     * @param c column index
     * @param size width/height of actor
     * @return x location of actor
     */
    protected double calculateXLocation(int c, double size){
        return Grid.X_LOCATION + size * c;
    }

    /**
     * Calculates actor y location based on row in grid and size of each cell(actor)
     * @param r column index
     * @param size width/height of actor
     * @return x location of actor
     */
    protected double calculateYLocation(int r, double size){
        return Grid.Y_LOCATION + size * r;
    }

    /**
     * Move actor to different location in the grid. Updates all potentially changed values (view location,
     * neighbor locations, and row/col instance variables.
     * @param newRow new row index
     * @param newCol new col index
     */
    protected void move(int newRow, int newCol){
        removeSelfFromGrid();
        addSelfToGrid(newRow, newCol);
        row = newRow;
        col = newCol;
    }

    /**
     * Get all neighboring actors from grid based on neighbor locations array.
     * @return arraylist of neighbor actors
     */
    public void updateNeighbors(){
        neighborLocations = g.getNeighborLocations(row, col);
        ArrayList<Actor> neighborActors = new ArrayList<>();
        ArrayList<int[]> emptyNeighborLocations = new ArrayList<>();
        for(int[] location : neighborLocations){
            if(g.getActor(location[0], location[1]) != null){
                neighborActors.add(g.getActor(location[0], location[1]));
            }
            else{
                emptyNeighborLocations.add(location);
            }
        }
        this.neighborActors = neighborActors;
        this.emptyNeighborLocations = emptyNeighborLocations;
    }

    /**
     * Method to update the view of the Actor to it's current state
     */
    public void draw(String type, Group root){
        root.getChildren().add(ShapeMaker.getInstance().drawCell(row, col, size, getColor(), Color.BLACK, type));
    }

    protected int[] getRandomEmptyLocation(List<int[]> locations){
        ArrayList<int[]> locs = new ArrayList<>();
        locs.addAll(locations);
        Random indexGenerator = new Random();
        while(!locs.isEmpty()){
            int index = locs.size() > 1 ? indexGenerator.nextInt(locs.size() -1) : 0;
            int[] loc = locs.get(index);
            if(getGrid().getActor(loc[0], loc[1]) == null){
                return loc;
            }
            locs.remove(index);
        }
        return new int[] {-1, -1};
    }

    /**
     * Function to save the significant states of neighbors for each type of actor. This will allow
     * the actors to move about the grid while not causing any changes in the actor's future state
     * that have not yet acted. Expected to be called on all actors before any actors act().
     */
    public abstract void saveCurrentNeighborStates();


    /**
     * Complete action. Expects grid to have updates all of the cells neighbors before beginning the step.
     * this local variable will be referred to when acting, so despite movement within the grid, the actor's
     * will not know that their neighbors have changed.
     */
    public abstract void act();

    /**
     * Used to get color from actor. This is important because sometimes the same type of actor (a GameOfLifeActor)
     * will change state (alive to dead), therefore changing its display color
     * @return
     */
    public abstract Paint getColor();

}