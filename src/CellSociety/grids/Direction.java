/**
 * @author mpz5
 */
package CellSociety.grids;

public enum Direction {
    NORTH (0, -1),
    NORTH_EAST (1, -1),
    EAST (1, 0),
    SOUTH_EAST (1, 1),
    SOUTH (0, 1),
    SOUTH_WEST (-1, 1),
    WEST (-1, 0),
    NORTH_WEST (-1, -1),
    DOUBLE_EAST(2, 0),
    DOUBLE_WEST(-2, 0),
    NORTH_DOUBLE_EAST (2, 1),
    NORTH_DOUBLE_WEST (-2, 1),
    SOUTH_DOUBLE_EAST (2, -1),
    SOUTH_DOUBLE_WEST (-2, -1);

    private int x;
    private int y;

    Direction(int xloc, int yloc){
        x = xloc;
        y = yloc;
    }

    public int addDistanceX(int distance){
        return x >= 0 ? x + 1 * distance : x - 1 * distance;
    }

    public int addDistanceY(int distance){
        return y >= 0 ? y+ 1* distance : y - 1*distance;
    }


}
