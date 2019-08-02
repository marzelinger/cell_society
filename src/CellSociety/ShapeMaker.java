package CellSociety;

import CellSociety.grids.Grid;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.List;

import static java.util.Arrays.asList;

public class ShapeMaker {
    public static String RECTANGLE_GRID = "rectangle";
    public static String TRIANGULAR_GRID = "triangle";
    public static String HEXAGONAL_GRID = "hexagon";
    private static double COSINE_30 = Math.cos(Math.toRadians(30));
    private static double SINE_30 = 0.5;
    private static double TANGENT_30 = Math.tan(Math.toRadians(30));

    private static ShapeMaker ourInstance = new ShapeMaker();

    public static ShapeMaker getInstance() {
        return ourInstance;
    }

    private ShapeMaker() {
    }

    public Shape drawCell(int row, int col, double size, Paint color, Paint border, String type){
        if (type.equals(RECTANGLE_GRID))
            return makeRectangle(row, col, size, color, border);
        else if (type.equals(TRIANGULAR_GRID))
            return makeTriangle(row, col, size, color, border);
        else
            return makeHexagon(row, col, size, color, border);
    }

    public Shape drawPatchedAgent(int row, int col, double size, String type){
        double x, y, r;
        if (type.equals(RECTANGLE_GRID)) {
            x = Grid.X_LOCATION + row * size + 0.5 * size;
            y = Grid.Y_LOCATION + col * size + 0.5 * size;
            r = 0.5 * size;
        } else if (type.equals(TRIANGULAR_GRID)){
            x = Grid.X_LOCATION + (col + 1) * size / 2;
            double offset;
            if (row % 2 == col % 2) offset = size / 2 * TANGENT_30; else offset = size / 2 / COSINE_30;
            y = Grid.Y_LOCATION + offset + row * size * COSINE_30;
            r = size / 2 * TANGENT_30;
        } else {
            Double offset;
            if (row % 2 != 0) offset = size * COSINE_30 * 2; else offset =  size * COSINE_30;
            x = size * COSINE_30 * 2 * col + offset;
            y = 0.5 * size + size * SINE_30 + (size + size * SINE_30) * row;
            r = size * COSINE_30;
        }
        Circle circle = new Circle();
        circle.setCenterX(x + Grid.X_LOCATION);
        circle.setCenterY(y + Grid.Y_LOCATION);
        circle.setRadius(r);
        circle.setFill(Color.BLACK);
        return circle;
    }

    public Rectangle makeRectangle(int row, int col, double size, Paint color, Paint border) {
        double xLocation = Grid.X_LOCATION + row * size;
        double yLocation = Grid.Y_LOCATION + col * size;
        Rectangle r = new Rectangle(xLocation, yLocation, size, size);
        r.setFill(color);
        r.setStroke(border);
        return r;
    }

    public Polygon makeTriangle(int row, int col, double size, Paint color, Paint border) {
        List<Double> points;
        if (row % 2 == col % 2) {
            //pointing down
            double topLeftX = Grid.X_LOCATION + col * size / 2;
            double topLeftY = Grid.Y_LOCATION + row * size * COSINE_30;
            double topRightX = topLeftX + size;
            double topRightY = topLeftY;
            double bottomX = topLeftX + SINE_30 * size;
            double bottomY = topLeftY + size * COSINE_30;
            points = asList(topLeftX, topLeftY, topRightX, topRightY, bottomX, bottomY);
        } else {
            double bottomLeftX = Grid.X_LOCATION + col * size / 2;
            double bottomLeftY = Grid.Y_LOCATION + (row + 1) * size * COSINE_30;
            double bottomRightX = bottomLeftX + size;
            double bottomRightY = bottomLeftY;
            double topX = bottomLeftX + SINE_30 * size;
            double topY = bottomLeftY - size * COSINE_30;
            points = asList(topX, topY, bottomLeftX, bottomLeftY, bottomRightX, bottomRightY);
        }
        Polygon p = new Polygon();
        p.getPoints().addAll(points);
        p.setFill(color);
        p.setStroke(border);
        return p;
    }

    public Polygon makeHexagon(int row, int col, double size, Paint color, Paint border) {

        Double offset = 0.0;
        if (row % 2 != 0) offset = size * COSINE_30;
        double leftTopX = Grid.X_LOCATION + size * COSINE_30 * col * 2 + offset;
        double leftTopY = Grid.Y_LOCATION + SINE_30 * size + (1+SINE_30) * size * row;
        double leftBotX = leftTopX;
        double leftBotY = leftTopY + size;
        double topX = leftTopX + size * COSINE_30;
        double topY = leftTopY - SINE_30 * size;
        double rightTopX = topX + size * COSINE_30;
        double rightTopY = leftTopY;
        double rightBotX = rightTopX;
        double rightBotY = leftBotY;
        double botX = topX;
        double botY = leftBotY + size * SINE_30;

        List<Double> points = asList(leftTopX, leftTopY, topX, topY, rightTopX, rightTopY, rightBotX, rightBotY, botX, botY, leftBotX, leftBotY);

        Polygon p = new Polygon();
        p.getPoints().addAll(points);
        p.setFill(color);
        p.setStroke(border);
        return p;
    }




}
