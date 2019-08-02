package CellSociety;

import CellSociety.simulations.Simulation;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.Map;

public class Graph {
    private final int MAX_DATA_POINTS = 40;

    private int numOfSeries;
    private String[] seriesName;
    private ArrayList<XYChart.Series<Number, Number>> dataSeries;
    private int stepIndex;

    private NumberAxis myXAxis;
    private NumberAxis myYAxis;
    private LineChart<Number, Number> myChart;

    public Graph(int maxPopulation, double x, double y, Simulation sim){
        stepIndex = 0;
        seriesName = sim.getActorNames();
        myXAxis = new NumberAxis();
        myYAxis = new NumberAxis();
        myXAxis.setLabel("Step");
        myXAxis.setForceZeroInRange(false);
        myYAxis.setLabel("Population");
        myYAxis.setLowerBound(0);
        myYAxis.setUpperBound((double)maxPopulation);
        myChart = new LineChart<>(myXAxis, myYAxis);
        myChart.setTitle("Population Chart");
        myChart.setLayoutX(x);
        myChart.setLayoutY(y);
        if(seriesName.length != 0){
            numOfSeries = seriesName.length;
            dataSeries = new ArrayList<>();

            for (int i = 0; i < numOfSeries; i++){
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                series.setName(seriesName[i]);
                myChart.getData().addAll(series);
                dataSeries.add(series);
            }
        }
    }

    public void plotStep(Map<String, Integer> point){
        stepIndex ++;
        if (stepIndex >= MAX_DATA_POINTS) {
            myXAxis.setLowerBound(stepIndex - MAX_DATA_POINTS);
            myXAxis.setUpperBound(stepIndex + 1);
        }
        for (int i = 0; i < numOfSeries; i++) {
            if (stepIndex >= MAX_DATA_POINTS)
                dataSeries.get(i).getData().remove(0);

            dataSeries.get(i).getData().add(new XYChart.Data<>(stepIndex, point.get(dataSeries.get(i).getName())));
        }
    }

    public Group getView(){
        return new Group(myChart);
    }

}
