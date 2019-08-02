package CellSociety;

import java.util.Arrays;

public final class EnumParams {

    public enum SimulationNames{
        GameOfLife,Wator,Segregation,Fire,RockPaperScissors,Sugar;
    }

    public enum GridShapeTypes{
        Rectangle,Hexagon,Triangle;
    }
    public enum GridEdgeTypes{
        Finite,Toroidal,Infinite;
    }
    public enum InitTypes{
        Probabilistic,Number,UserSet;
    }
    public enum GridOutlinedTypes{
        Yes,No;
    }
    public enum GridNeighborsTypes{
        Total, VN, Corners;
    }
}
