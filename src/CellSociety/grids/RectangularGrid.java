/**
 * @author ral30, mpz5
 *
 */


package CellSociety.grids;

import CellSociety.EnumParams;
import CellSociety.ShapeMaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RectangularGrid extends Grid {
    private static final Direction[] VN_NEIGHBORS = new Direction[]{Direction.NORTH, Direction.WEST, Direction.EAST, Direction.SOUTH};
    private static final Direction[] CORNER_NEIGHBORS = new Direction[]{Direction.NORTH_EAST, Direction.NORTH_WEST, Direction.SOUTH_EAST, Direction.SOUTH_WEST};
    private static final Direction[] TOTAL_NEIGHBORS = new Direction[]{Direction.NORTH,Direction.NORTH_EAST, Direction.NORTH_WEST, Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.SOUTH_EAST, Direction.SOUTH_WEST};

    private static final Map<EnumParams.GridNeighborsTypes, Direction[]> NEIGHBOR_TYPES = Map.of(EnumParams.GridNeighborsTypes.Total, TOTAL_NEIGHBORS, EnumParams.GridNeighborsTypes.Corners, CORNER_NEIGHBORS, EnumParams.GridNeighborsTypes.VN, VN_NEIGHBORS);

    public RectangularGrid(int gridRows, int gridCols,double gridSize, String neighborsTypes, String edgeTypes){
        super(gridRows, gridCols, gridSize, neighborsTypes, edgeTypes);
    }

    @Override
    public List<int[]> getNeighborLocations(int row, int col, int distance, EnumParams.GridNeighborsTypes type) {
        ArrayList<int[]> neighborLocations = new ArrayList<>();
        getNeighborLocationsHelper(neighborLocations, row, col, NEIGHBOR_TYPES.get(type), distance);
        return neighborLocations;
    }

    @Override
    public String getType(){ return ShapeMaker.RECTANGLE_GRID; }
}
