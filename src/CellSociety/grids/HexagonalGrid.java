/**
 * @author mpz5
 * https://www.redblobgames.com/grids/hexagons/
 */

package CellSociety.grids;

import CellSociety.EnumParams;
import CellSociety.ShapeMaker;

import java.util.ArrayList;
import java.util.List;

public class HexagonalGrid extends Grid {

    private static final Direction[] EVEN_ROW_NEIGHBORS= new Direction[] {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
    private static final Direction[] ODD_ROW_NEIGHBORS = new Direction[]{Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.WEST};

    public HexagonalGrid(int gridRows, int gridCols,double gridSize, String neighborsTypes, String edgeTypes){
        super(gridRows, gridCols, gridSize, neighborsTypes, edgeTypes);
    }

    @Override
    public List<int[]> getNeighborLocations(int row, int col, int distance, EnumParams.GridNeighborsTypes type) {
        ArrayList<int[]> neighborLocations = new ArrayList<>();
        if (row % 2 == 0) {
            getNeighborLocationsHelper(neighborLocations, row, col, EVEN_ROW_NEIGHBORS, distance);
        } else {
            getNeighborLocationsHelper(neighborLocations, row, col, ODD_ROW_NEIGHBORS, distance);
        }
        return neighborLocations;
    }

    @Override
    public String getType() { return ShapeMaker.HEXAGONAL_GRID; }

}
