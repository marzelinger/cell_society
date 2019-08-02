/**
* @author mpz5
 */

package CellSociety.grids;

import CellSociety.EnumParams;
import CellSociety.ShapeMaker;

import java.util.ArrayList;
import java.util.List;

public class TriangularGrid extends Grid {
    private static final Direction[] POINTING_UP_NEIGHBORS = new Direction[]{Direction.NORTH, Direction.NORTH_EAST,Direction.NORTH_WEST, Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.SOUTH_EAST, Direction.SOUTH_WEST, Direction.DOUBLE_EAST, Direction.DOUBLE_WEST, Direction.SOUTH_DOUBLE_EAST, Direction.SOUTH_DOUBLE_WEST};
    private static final Direction[] POINTING_DOWN_NEIGHBORS = new Direction[]{Direction.NORTH, Direction.NORTH_EAST,Direction.NORTH_WEST, Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.SOUTH_EAST, Direction.SOUTH_WEST, Direction.DOUBLE_EAST, Direction.DOUBLE_WEST, Direction.NORTH_DOUBLE_EAST, Direction.NORTH_DOUBLE_WEST};

    public TriangularGrid(int gridRows, int gridCols,double gridSize, String neighborsTypes, String edgeTypes){
        super(gridRows, gridCols, gridSize, neighborsTypes, edgeTypes);
    }

    @Override
    public List<int[]> getNeighborLocations(int row, int col, int distance, EnumParams.GridNeighborsTypes type) {
        //figure out if the triangle is pointing up or down
        ArrayList<int[]> neighborLocations = new ArrayList<>();
        if(determineDirection(row, col)){
            getNeighborLocationsHelper(neighborLocations, row, col, POINTING_UP_NEIGHBORS, distance);
        }
        else{
            getNeighborLocationsHelper(neighborLocations, row, col, POINTING_DOWN_NEIGHBORS, distance);
        }
        return neighborLocations;
    }

    /**
     * returns true if the triangle points up
     * @param row
     * @param col
     * @return
     */
    public boolean determineDirection(int row, int col){
        return (row%2 !=  col %2);
    }

    @Override
    public String getType(){
        return ShapeMaker.TRIANGULAR_GRID;
    }

}
