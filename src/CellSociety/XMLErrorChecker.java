package CellSociety;
/**
 *
 * @author: Russell Llave
 * Checks Implemented:
 * 1) Simulation name is one of the possible simulations
 * 2) All null values are checked and accounted for (only null pointer exception happens when Simulation is wrong, but alert shows up)
 * 3) All string values are checked to be appropriate values (from enumeration)
 * 4) All Probabilities are checked to be doubles between 0 and 1
 * 5) All integers are checked to be integers
 * 6) All colors are checked to be valid colors
 * 7) All doubles are checked to be doubles
 */

import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.Map;
import java.util.ResourceBundle;

public class XMLErrorChecker {
    //Parameters for each actor, grid, and initialization
    //check for null's for the variables of the appropriate actors and of the grid and initialized parameters
    protected static final String[] TREE_PARAMETERS = {"Threshold","Probability","EmptyProbability","ActorColor1","ActorColor2","EmptyColor"};
    protected static final String[] PERSON_PARAMETERS = {"Threshold","Probability","EmptyProbability","ActorColor1","ActorColor2","EmptyColor"};
    protected static final String[] ANIMAL_PARAMETERS = {"Threshold","Probability","EmptyProbability","ActorColor1","ActorColor2","EmptyColor"};
    protected static final String[] G_OF_L_ACTOR_PARAMETERS = {"Threshold","Probability","EmptyProbability","ActorColor1","ActorColor2","EmptyColor"};
    protected static final String[] RPS_ACTOR_PARAMETERS = {"Threshold","Probability","EmptyProbability","ActorColor1","ActorColor2","ActorColor3","EmptyColor"};
    protected static final String[] SUGAR_PARAMETERS = {"Threshold","Probability","EmptyProbability"};
    protected static final String[] GRID_PARAMETERS = {"rows","cols","GridShape","GridNeighbors","GridEdge","GridOutlined","RadiusNeighbors"};
    protected static final String[] INITIALIZED_PARAMETERS = {"InitType"};


    //Expected type of variable for each parameter
    protected  static final String[] STRING_PARAMETERS = {"Description"}; //other strings have enumerations
    protected static final String[] INTEGER_PARAMETERS = {"rows","cols","RadiusNeighbors"};
    protected static final String[] PROBABILITY_PARAMETERS = {"Probability","EmptyProbability"};
    protected static final String[] DOUBLE_PARAMETERS = {"Threshold","Probability","EmptyProbability"};
    protected static final String[] COLOR_PARAMETERS = {"ActorColor1","ActorColor2","ActorColor3","EmptyColor"};

    protected static final String DEFAULT_DATA_PATH = "data/default_args.xml";
    private Map<String, String> myMap;
    private Map<String, String> defaultArgs;
    private ResourceBundle myResources;
    private String[] myActorParams;

    public XMLErrorChecker(Map<String,String> mapInput){
        myMap = mapInput;
    }

    public void getDefaultValues(String data_path){
        File data_file = new File(data_path);
        defaultArgs = new XMLParser("sim").getGame(data_file);
    }

    //no default value set for Simulation Name.
    public void checkSimulationName(String simType){
        boolean nameExists = false;
        for(EnumParams.SimulationNames s: EnumParams.SimulationNames.values()){
            if(s.name().equals(simType)){nameExists = true;}
        }
        if(!nameExists){
            new Alert(Alert.AlertType.ERROR, "Simulation Name does not match possible simulations").showAndWait();
        }
    }
    public void checkGridShapes(String simType){
        boolean nameExists = false;
        for(EnumParams.GridShapeTypes s: EnumParams.GridShapeTypes.values()){
            if(s.name().equals(simType)){nameExists = true;}
        }
        if(!nameExists){
            new Alert(Alert.AlertType.ERROR, "Grid shapes does not match possible simulations").showAndWait();
            myMap.put("GridShape",defaultArgs.get("GridShape"));
        }
    }


    public void checkGridEdges(String simType){
        boolean nameExists = false;
        for(EnumParams.GridEdgeTypes s: EnumParams.GridEdgeTypes.values()){
            if(s.name().equals(simType)){nameExists = true;}
        }
        if(!nameExists){
            new Alert(Alert.AlertType.ERROR, "Grid edges does not match possible simulations").showAndWait();
            myMap.put("GridEdge",defaultArgs.get("GridEdge"));

        }
    }
    public void checkInitTypes(String simType){
        boolean nameExists = false;
        for(EnumParams.InitTypes s: EnumParams.InitTypes.values()){
            if(s.name().equals(simType)){nameExists = true;}
        }
        if(!nameExists){
            new Alert(Alert.AlertType.ERROR, "InitType does not match possible simulations").showAndWait();
            myMap.put("InitType",defaultArgs.get("InitType"));

        }
    }
    public void checkGridNeighbor(String simType){
        boolean nameExists = false;
        for(EnumParams.GridNeighborsTypes s: EnumParams.GridNeighborsTypes.values()){
            if(s.name().equals(simType)){nameExists = true;}
        }
        if(!nameExists){
            new Alert(Alert.AlertType.ERROR, "Grid Neighbor does not match possible simulations").showAndWait();
            myMap.put("InitType",defaultArgs.get("InitType"));

        }
    }
    public void checkGridOutlined(String simType){
        boolean nameExists = false;
        for(EnumParams.GridOutlinedTypes s: EnumParams.GridOutlinedTypes.values()){
            if(s.name().equals(simType)){nameExists = true;}
        }
        if(!nameExists){
            new Alert(Alert.AlertType.ERROR, "Grid outlined does not match possible simulations").showAndWait();
            myMap.put("InitType",defaultArgs.get("InitType"));

        }
    }



    public void setMyActorParams(String simType) {
        if(simType.equals("GameOfLife")) {
            myActorParams = G_OF_L_ACTOR_PARAMETERS;
        }
        if(simType.equals("Wator")){
            myActorParams = ANIMAL_PARAMETERS;
        }
        if(simType.equals("Segregation")){
            myActorParams = PERSON_PARAMETERS;
        }
        if(simType.equals("Fire")){
            myActorParams = TREE_PARAMETERS;
        }
        if(simType.equals("RockPaperScissors")){
            myActorParams=RPS_ACTOR_PARAMETERS;
        }
        if(simType.equals("Sugar")){
            myActorParams=SUGAR_PARAMETERS;
        }
    }


    public void checkNullAllParams(){
        String[][] concatenatedParams = {myActorParams,GRID_PARAMETERS,INITIALIZED_PARAMETERS,STRING_PARAMETERS};
        for(String[] s_arr: concatenatedParams){
            checkNullParams(s_arr);
        }
    }

    public void checkNullParams(String[] s_arr){
        for(String s: s_arr){
            if (myMap.get(s).equals("")||myMap.get(s)==(null)){
               new Alert(Alert.AlertType.ERROR, s+" is null or empty.").showAndWait();
               myMap.put(s,defaultArgs.get(s));
           }
        }
    }

    public void checkIntegerParams(String[] s_arr){
        for(String s: s_arr){
            try{
                Integer.parseInt(myMap.get(s));
            }
            catch (NumberFormatException nfe){
                myMap.put(s,defaultArgs.get(s));
                new Alert(Alert.AlertType.ERROR, s+" should be integer.").showAndWait();

            }
        }
    }

    public void checkDoubleParams(String[] s_arr){
        for(String s: s_arr){
            try{
                Double.parseDouble(myMap.get(s));
            }
            catch (NumberFormatException nfe){
                myMap.put(s,defaultArgs.get(s));
                new Alert(Alert.AlertType.ERROR, s+" should be double.").showAndWait();

            }
        }
    }

    public void checkProbabilities(String[] s_arr){
        for(String s:s_arr){
            Double temp = Double.parseDouble(myMap.get(s));
            if(temp>1||temp<0){
                myMap.put(s,defaultArgs.get(s));
                new Alert(Alert.AlertType.ERROR, s+" should be between 0 and 1.").showAndWait();
            }
        }
    }

    public void checkValidColor(String[] s_arr){
        for(String s:s_arr){
            try{
                Color.valueOf(myMap.get(s));
            }
            catch (IllegalArgumentException e){
                myMap.put(s,defaultArgs.get(s));

                new Alert(Alert.AlertType.ERROR, s+" is invalid color.").showAndWait();

            }
        }
    }


    public Map<String,String> checkAll(){
        checkSimulationName(myMap.get("type"));
        getDefaultValues(DEFAULT_DATA_PATH);
        if(!(myMap.get("type")==(null))){
            setMyActorParams(myMap.get("type"));
            checkNullAllParams();
            checkIntegerParams(INTEGER_PARAMETERS);
            checkDoubleParams(DOUBLE_PARAMETERS);
            checkProbabilities(PROBABILITY_PARAMETERS);
            checkInitTypes(myMap.get("InitType"));
            checkGridEdges(myMap.get("GridEdge"));
            checkGridShapes(myMap.get("GridShape"));
            checkGridOutlined(myMap.get("GridOutlined"));
            checkValidColor(COLOR_PARAMETERS);
        }
        return myMap;
    }

}
