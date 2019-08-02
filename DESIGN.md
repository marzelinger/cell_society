Cell Society Design
====

#### High-Level Design Goals:

From a high level, our design goals were to create a module that could be easily extended without a need for extensive modifications of the existing pieces. By creating three main abstract classes to run the simulations (Actor, Grid, and Simulation) that could easily interface with the GUI and Configuration, we made subclasses much easier to implement and add. Another goal of ours was to determine an effective and logical way for each aspect of our project to integrate with the others. When determining our advanced cell society plan, we identified all of these contact points and implemented them in such a way that each area of the project integrated with but did not depend on a specific implimentation of the other aspects of the project. We aimed to stay as close to our original plan as we could, which we did with a great level of success.

#### How To Add New Features

**How do add a new simulation:**
* A new XML file would be created that designates the new type of simulation within the <type> tags. For example: 
    ```html
    <?xml version="1.0" encoding="UTF-8" ?>
 
    <data sim = "sim_test">
        <!--Simulation Name-->
        <type>NewSimulation</type>
        <!--Grid Parameters-->
        <rows>50</rows>
        <cols>50</cols
        <GridShape>Rectangle</GridShape>
        <GridNeighbors>Total</GridNeighbors>
        <GridEdge>Toroidal</GridEdge>
        <GridOutlined>No</GridOutlined>
    
        <!--Actor Parameters-->
        <Threshold>0.5</Threshold>
    
        <!--Color Parameters-->
        <ActorColor1>RED</ActorColor1>
        <ActorColor2>GREEN</ActorColor2>
        <ActorColor3>BLUE</ActorColor3>
        <EmptyColor>YELLOW</EmptyColor>
    
        <!--Initialization Parameters-->
        <InitType>Probabilistic</InitType>
        <EmptyProbability>0</EmptyProbability>
        <Probability>0.7</Probability>
    
        <RadiusNeighbors>4</RadiusNeighbors>
        <Description>This is a fire simulation</Description>

    </data>

    ```
* Next, another if statement would be added to the prepareSImulation() function in the CellSociety class so that it looked like this:
    ```java
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
            else if(myArgs.get(XMLParser.TYPE_KEY).equals(EnumParams.SimulationNames.NewSimulation.name())){
                         mySimulation = new NewSimulation(myArgs);
            }
            layoutSimulationSpecificGUI();
            myRoot.getChildren().addAll(myFixedLayoutRoot);
            myRoot.getChildren().addAll(mySimulationSpecificRoot);
            mySimulation.addAllViews(myRoot);
        }
    ```
* In order for this to work, NewSimulation would need to be added as a possible SimulationName in the EnumParams class.
* Finally, the NewSimulation class would be created and the necessary abstract functions from the Simulation class would need to be implemented and the actor classes for that simulation need to be created. 
* Actor classes will include all of the necessary functions from the abstract Actor class and will determine what state they need to save from their neighbors and how to act based on that state.

**How to Add a new GUI element:**
To add a new GUI element, add in the main Cellsociety class the element substance and its lambda function. Then depending on the GUI element, create the logic to add it into either myFixedLayoutRoot, which are the GUI elements that are there for any type of simulations, or mySimulationSpecificRoot, which pertain only to a specific simulation. 
Given below is an example of the threshold slider:
``` java
    public static final double THRESHOLD_SLIDER_TEXT_X = 20;
    public static final double THRESHOLD_SLIDER_Y = 440;
    public static final double THRESHOLD_SLIDER_TEXT_Y = THRESHOLD_SLIDER_Y + 17;
    public static final double THRESHOLD_SLIDER_X = 100;
    
    private void layoutSimulationSpecificGUI(){
        System.out.println(mySimulation);
        mySimulationSpecificRoot = new Group();
        myGraph = new Graph(mySimulation.getMyGrid().getMaxPopulation(), GRAPH_X, GRAPH_Y, mySimulation);
        mySimulationSpecificRoot.getChildren().addAll(myGraph.getView());
        if (mySimulation.getThresholdRange()[0] != -1)
            createThresholdSlider(mySimulation.getThresholdRange(), mySimulation.getThreshold());
    }
    
    private Slider createThresholdSlider(double[] range, double value){
        Text t = new Text(THRESHOLD_SLIDER_TEXT_X, THRESHOLD_SLIDER_TEXT_Y, 
        myResources.getString(THRESHOLD_TEXT_KEY));
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
``` 

**How to Add a new XML Tag:**
To add a new XML Tag, the name of the tag must be added as a string to the "DATA_FIELDS" array in XML parser. For example, to add the XML tag "ActorColor1", the following can be done: 
``` java 
        public static final String ACTOR_COLOR1_KEY = "ActorColor1";
        public static final List<String> DATA_FIELDS = List.of(
            TYPE_KEY,THRESHOLD_KEY,EMPTY_PROBABILITY_KEY,PROBABILITY_KEY,EMPTY_PROBABILITY_KEY,
            ROWS_KEY,COLS_KEY,GRID_SHAPE_KEY,GRID_EDGE_KEY,GRID_NEIGHBORS_KEY,GRID_OUTLINED_KEY,
            ACTOR_COLOR1_KEY
    );
``` 
After this is completed, the tag for that new string in that XML file will be read and its value will be stored in the same "myArgs" map where all of the other XML data is stored.
#### Major Design Choices
1. A major design choice is that we decided to put the neighboring relationship conditions into a Grid class instead of keeping track of everything in simulation class, or let actors interact with each other directly. While each simulation subclass contains information specific to a type of simulation, Grid class takes care of how the cells are placed by providing a method getAllNeighbors, which returns to an Actor all the nrighboring cells. This design provides much more flexibility as different types of grids with different edges can extend the Grid class, and override these conditions.

2. We directly place our actors into the grid as opposed to having a sort of container class that we referred to as a Cell in our plan. If we had used a Cell class then our grid would have had no empty locations, instead it would have been full of Cells and each cell would have a location and be able to hold/add/and remove actors from itself. When implementing the basic simulations, this extra class was very unecessary. Since there was no state associated with a certain location in the grid, only with the actors, it seemed like the Cell class would have been extremely passive. However, after implementing the sugarscape simulation and looking more at the other advanced simulations, it seems like this cell class may have been useful. For example, the cells could have served as SugarPatches for the SugarScape simulation and could have ended up havng additional state such as amount of sugar and the ability to lose that sugar to agents as well as grow more sugar each turn. With this in mind, I think that for a basic implementation the Cell class was not needed, but that for a complex implementation it could have been useful.

#### Assumptions Made

When implementing Wator World we made several assumptions: 
 * Implementation for shark class assumes that at each step every shark can attempt to either eat OR move. If a shark attempts to eat, and finds all neighboring fish have been eaten then it acts as if it "shared" the fish with a neighboring shark, meaning it does not then move. Potential Situations:
 * Shark finds a neighbor fish and decides to eat it
     * That fish has already acted and therefore this is a valid eat. (i.e if the fish is before the shark in the iteration of the array (from top left to bottom right).
     * The fish has not already acted and therefore could move.
         * The fish acts and stays where it is. Therefore the fish should be eaten (removed from grid)
         * The fish acts and moves to an empty neighbor space. There is then no fish in the location.
         * The shark will then not get to eat anything. Nothing will change.
         * The fish acts and moves to a new location. It then breeds and the new fish it breeds ends up in the original fish's location. Meaning there is now a new fish in the location that the shark had planned on eating the original fish from. In this case, assume that the new fish will NOT be eaten because the fish would not have existed in the state of the grid when the shark was making the eat/move decision.
         * The fish acts and moves to a new location. A different fish moves into the location of the old fish. That new fish was marked to be eaten by a different shark. In this case the fish will not be eaten because it was not the original fish that the shark tried to eat.
         * The fish acts and moves to a new location. A different fish moves into the location of the old fish. That fish was not marked as eaten by any shark. This will be treated the same way as the situation above.
 * Shark Breeds:
     * Shark finds an empty spot and breeds a new shark into it. The steps left to breed are reset to the breeding period.
     * Shark finds an empty spot and PLANS to breed a new shark into it, however a prior animal has either already placed a baby there or has moved there. In this case, the shark will attempt to breed again the next step.

