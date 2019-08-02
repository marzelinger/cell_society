/**
 * @author mpz5, qx26
 */


package CellSociety;

import CellSociety.simulations.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.io.File;
import java.util.Map;


public class CellSociety extends Application{
    public static final String TITLE = "Example JavaFX";
    public static final Paint BACKGROUND = Color.WHITE;
    public static final double HEIGHT = 780;
    public static final double WIDTH = 950;

    public static final int DEFAULT_SPEED_GEAR = 500;
    public static final int MAX_SPEED_GEAR = 1000;
    public static final int MIN_SPEED_GEAR = 100;
    public static final int SPEED_GEAR_INTERVAL = 100;
    public static final String SPEED_GEAR_UNIT = "ms/step";

    public static final double FILEINPUT_Y = 20;
    public static final double FILEINPUT_TEXT_X = 440;
    public static final double FILEINPUT_TEXT_Y = FILEINPUT_Y + 17;
    public static final double FILECHOOSER_X = 560;

    public static final double START_X = 440;
    public static final double START_Y = 60;
    public static final double STOP_X = 540;
    public static final double STOP_Y = 60;
    public static final double RESET_X = 640;
    public static final double RESET_Y = 60;
    public static final double STEP_X = 740;
    public static final double STEP_Y = 60;
    public static final double DES_X = 840;
    public static final double DES_Y = 60;


    public static final double SLOWER_X = 440;
    public static final double FASTER_X = 640;
    public static final double SPEED_Y = 100;
    public static final double SPEED_TEXT_X = 530;
    public static final double SPEED_TEXT_Y = SPEED_Y + 17;

    public static final double THRESHOLD_SLIDER_TEXT_X = 20;
    public static final double THRESHOLD_SLIDER_Y = 440;
    public static final double THRESHOLD_SLIDER_TEXT_Y = THRESHOLD_SLIDER_Y + 17;
    public static final double THRESHOLD_SLIDER_X = 100;

    public static final double GRAPH_X = 430;
    public static final double GRAPH_Y = 140;
    private Graph myGraph;

    // kind of data files to look for
    public static final String DATA_FILE_EXTENSION = "*.xml";

    public static final String DEFAULT_RESOURCE_PACKAGE = "resources/CellSocietyGUI";
    public static final String XML_FILE_DISPLAY_KEY = "ChooseXMLFileDisplay";

    // NOTE: generally accepted behavior that the chooser remembers where user left it last
    private FileChooser myChooser = makeChooser(DATA_FILE_EXTENSION);

    public static final double BUTTON_HEIGHT = 20;
    public static final double BUTTON_LENGTH = 80;

    public static final int FONT = 12;
    private Text mySpeedText;

    public static final String STEP_BUTTON_KEY = "StepCommand";
    public static final String STOP_BUTTON_KEY = "PauseCommand";
    public static final String START_BUTTON_KEY = "StartCommand";
    public static final String RESUME_BUTTON_KEY = "ResumeCommand";
    public static final String SPEED_BUTTON_KEY = "UnitsSpeedAnimation";
    public static final String RESET_BUTTON_KEY = "ResetCommand";
    public static final String SLOWER_BUTTON_KEY = "SlowerCommand";
    public static final String FASTER_BUTTON_KEY = "FasterCommand";
    public static final String CHOOSE_BUTTON_KEY = "ChooseCommand";
    public static final String THRESHOLD_TEXT_KEY = "ThresholdDisplay";
    public static final String DESCRIPTION_BUTTON_KEY = "Description";

    private Stage myStage;
    private Scene myScene;
    private Group myRoot;
    private Group myFixedLayoutRoot;
    private Group mySimulationSpecificRoot;
    private Timeline myAnimation;
    private int mySpeedGear;
    private Map<String, String> myArgs;
    private ResourceBundle myResources;
    private Simulation mySimulation;
    private boolean hasFile;
    private Button startButton;
    private Button stopButton;
    private Button resetButton;
    private Button stepButton;
    private Button slowerButton;
    private Button fasterButton;
    private Button fileChooserButton;
    private Button descriptionButton;

    /**
     * Initialize what will be displayed and how it will be updated. Adds the Welcome page text to the game
     */
    @Override
    public void start (Stage stage) {
        myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE);

        myStage = stage;
        stage.setTitle(TITLE);
        myRoot = new Group();
        myScene = new Scene(myRoot, WIDTH, HEIGHT, BACKGROUND);
        stage.setScene(myScene);
        stage.show();

        mySpeedGear = DEFAULT_SPEED_GEAR;
        var frame = new KeyFrame(Duration.millis(mySpeedGear), e -> step());
        myAnimation = new Timeline();
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.getKeyFrames().add(frame);

        layoutButtons();
        myRoot.getChildren().addAll(myFixedLayoutRoot);

    }

    private void prepareSimulation(){
        if (myArgs.get(XMLParser.TYPE_KEY).equals(EnumParams.SimulationNames.GameOfLife.name())){
            mySimulation = new GameOfLifeSimulation(myArgs);
        }
        else if (myArgs.get(XMLParser.TYPE_KEY).equals(EnumParams.SimulationNames.Fire.name())){
            mySimulation = new FireSimulation(myArgs);
        }
        else if (myArgs.get(XMLParser.TYPE_KEY).equals(EnumParams.SimulationNames.Wator.name())){
            mySimulation = new WatorWorldSimulation(myArgs);
        }
        else if (myArgs.get(XMLParser.TYPE_KEY).equals(EnumParams.SimulationNames.Segregation.name())){
            mySimulation = new SegregationSimulation(myArgs);
        }
        else if(myArgs.get(XMLParser.TYPE_KEY).equals(EnumParams.SimulationNames.RockPaperScissors.name())){
            mySimulation = new RockPaperScissorsSimulation(myArgs);
        }
        else if(myArgs.get(XMLParser.TYPE_KEY).equals(EnumParams.SimulationNames.Sugar.name())){
            mySimulation = new SugarScapeSimulation(myArgs);
        }
        layoutSimulationSpecificGUI();
        myRoot.getChildren().addAll(myFixedLayoutRoot);
        myRoot.getChildren().addAll(mySimulationSpecificRoot);
        mySimulation.addAllViews(myRoot);
    }

    // prepare new animation for show, called when loading a file, or changing speed while during a stopped simulation
    public void setAnimationTempo() {
        var frame = new KeyFrame(Duration.millis(mySpeedGear), e -> step());
        myAnimation.getKeyFrames().setAll(frame);
    }

    public void handleStartEvent(ActionEvent e) {
        if (hasFile) {
            mySimulation.startPressed();
            myAnimation.play();
        }
    }

    public void handleStopEvent(ActionEvent e) {
        if (hasFile) {
            startButton.setText(myResources.getString(RESUME_BUTTON_KEY));
            mySimulation.stopPressed();
            myAnimation.stop();
        }
    }

    public void handleReset (ActionEvent e) {
        if (hasFile) {
            myAnimation.stop();
            startButton.setText(myResources.getString(START_BUTTON_KEY));
            mySimulation.resetPressed();
            myRoot.getChildren().clear();
            myRoot.getChildren().addAll(myFixedLayoutRoot);
            layoutSimulationSpecificGUI();
            myRoot.getChildren().addAll(mySimulationSpecificRoot);
            mySimulation.addAllViews(myRoot);
        }
    }

    public void handleSlower(ActionEvent e) {
        if (hasFile) {
            if (!mySimulation.getIsOn()) {
                mySpeedGear = Math.min(mySpeedGear + SPEED_GEAR_INTERVAL, MAX_SPEED_GEAR);
                mySpeedText.setText(mySpeedGear + myResources.getString(SPEED_BUTTON_KEY));
                setAnimationTempo();
            }
        }
    }

    public void handleFaster(ActionEvent e) {
        if (hasFile) {
            if (!mySimulation.getIsOn()) {
                mySpeedGear = Math.max(mySpeedGear - SPEED_GEAR_INTERVAL, MIN_SPEED_GEAR);
                mySpeedText.setText(mySpeedGear + myResources.getString(SPEED_BUTTON_KEY));
                setAnimationTempo();
            }
        }
    }

    public void handleFileChooser(ActionEvent e) {
        var dataFile = myChooser.showOpenDialog(myStage);
        try {
            myArgs = new XMLParser(XMLParser.SIM_FILE_TAG).getGame(dataFile);
        }
        catch (XMLException err) {
            new Alert(Alert.AlertType.ERROR, err.getMessage()).showAndWait();
        }
        XMLErrorChecker checker = new XMLErrorChecker(myArgs);

        myArgs = checker.checkAll();

        hasFile = true;
        startButton.setText(myResources.getString(START_BUTTON_KEY));
        myAnimation.stop();
        myRoot.getChildren().clear();
        prepareSimulation();
    }

    public void handleStep(ActionEvent e) {
        if (hasFile && !mySimulation.getIsOn()){
            step();
        }
    }

    // put button views into myRoot group
    public void layoutButtons () {
        myFixedLayoutRoot = new Group();

        startButton = createButton(START_BUTTON_KEY, START_X, START_Y);
        startButton.setOnAction( e -> handleStartEvent(e));


        stopButton = createButton(STOP_BUTTON_KEY, STOP_X, STOP_Y);
        stopButton.setOnAction( e -> handleStopEvent(e));

        resetButton = createButton(RESET_BUTTON_KEY, RESET_X, RESET_Y);
        resetButton.setOnAction( e -> handleReset(e));


        slowerButton = createButton(SLOWER_BUTTON_KEY, SLOWER_X, SPEED_Y);
        slowerButton.setOnAction(e -> handleSlower(e));

        fasterButton = createButton(FASTER_BUTTON_KEY, FASTER_X, SPEED_Y);
        fasterButton.setOnAction(e -> handleFaster(e));

        stepButton = createButton(STEP_BUTTON_KEY, STEP_X, STEP_Y);
        stepButton.setOnAction(e -> handleStep(e));

        fileChooserButton = createButton(CHOOSE_BUTTON_KEY, FILECHOOSER_X, FILEINPUT_Y);
        fileChooserButton.setOnAction(e -> handleFileChooser(e));

        descriptionButton = createButton(DESCRIPTION_BUTTON_KEY,DES_X,DES_Y);
        descriptionButton.setOnAction(e -> new Alert(Alert.AlertType.INFORMATION, myArgs.get("Description")).showAndWait());



        mySpeedText = createText(SPEED_TEXT_X, SPEED_TEXT_Y, mySpeedGear+SPEED_GEAR_UNIT);
        createText(FILEINPUT_TEXT_X, FILEINPUT_TEXT_Y, myResources.getString(XML_FILE_DISPLAY_KEY ));
    }

    private void layoutSimulationSpecificGUI(){
        System.out.println(mySimulation);
        mySimulationSpecificRoot = new Group();
        myGraph = new Graph(mySimulation.getMyGrid().getMaxPopulation(), GRAPH_X, GRAPH_Y, mySimulation);
        mySimulationSpecificRoot.getChildren().addAll(myGraph.getView());
        if (mySimulation.getThresholdRange()[0] != -1) createThresholdSlider(mySimulation.getThresholdRange(), mySimulation.getThreshold());
    }

    private Text createText(double xLocation, double yLocation, String message){
        Text t = new Text(xLocation, yLocation, message);
        t.setFont(new Font(FONT));
        myFixedLayoutRoot.getChildren().add(t);
        return t;
    }

    private Button createButton(String key, double xLocation, double yLocation){
        Button b = new Button(myResources.getString(key));
        b.setLayoutX(xLocation);
        b.setLayoutY(yLocation);
        b.setPrefSize(BUTTON_LENGTH, BUTTON_HEIGHT);
        myFixedLayoutRoot.getChildren().add(b);
        return b;
    }

    private Slider createThresholdSlider(double[] range, double value){
        Text t = new Text(THRESHOLD_SLIDER_TEXT_X, THRESHOLD_SLIDER_TEXT_Y, myResources.getString(THRESHOLD_TEXT_KEY));
        t.setFont(new Font(FONT));
        mySimulationSpecificRoot.getChildren().add(t);

        Slider s = new Slider();
        s.setLayoutX(THRESHOLD_SLIDER_X);
        s.setLayoutY(THRESHOLD_SLIDER_Y);
        s.setMin(range[0]);
        s.setMax(range[1]);
        s.setValue(value);
        s.setBlockIncrement(range[2]);
        mySimulationSpecificRoot.getChildren().add(s);
        return s;
    }

    private FileChooser makeChooser (String extensionAccepted) {
        var result = new FileChooser();
        result.setTitle("Open Data File");
        result.setInitialDirectory(new File(System.getProperty("user.dir")));
        result.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Text Files", extensionAccepted));
        return result;
    }



    public void step() {
        myRoot.getChildren().clear();
        myRoot.getChildren().addAll(myFixedLayoutRoot);
        myRoot.getChildren().addAll(mySimulationSpecificRoot);
        mySimulation.step(myRoot);

        Map<String, Integer> population = mySimulation.getMyGrid().getPopulations(mySimulation);
        myGraph.plotStep(population);
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }

}