cell society
====

This project implements a cellular automata simulator.

Names: Marley Zelinger, Qingyang Xu, Russell Llave

### Timeline

Start Date: 09/16/2018 (basic) 09/24/2018 (complete)

Finish Date: 09/23/2018 (basic) 10/01/2018 (complete)

Hours Spent: ~20 total (basic), 15 each (complete)

### Primary Roles

Marley --> Simulation sub classes, Actor parent class and all actor sub classes, refactoring.

Russell --> Grid and Rectangular Sub class, XML Parser/Checker, Resource Bundle

Qingyang --> Simulation parent class, GUI, file input logic

### Resources Used
XML Parser from Professor Duvall

[Lambda Functions](https://www.geeksforgeeks.org/lambda-expressions-java-8/)

[Wator World](http://nifty.stanford.edu/2011/scott-wator-world/WatorWorld.htm)

[Game of Life](https://en.wikipedia.org/wiki/Conway's_Game_of_Life)

[Segregation](http://nifty.stanford.edu/2014/mccown-schelling-model-segregation/)

[Fire](http://nifty.stanford.edu/2007/shiflet-fire/)

[Hexagonal Grid](https://www.redblobgames.com/grids/hexagons/);

### Running the Program

Main class: CellSociety

Data files needed: XML files are located in the data folder.

Interesting data files: XML files to simulate fire, game of life, segregation and wator world are located in the data file. The sugarscape file demonstrates the use of a patch and an actor within a patch. The wator world simulation is on a triangular grid and the fire simulation uses a toroidal neighborhood.

Features implemented: 

* The ability to run 6 different simulations
* Start, pause, reset, and step button functionality. The simulation must be paused or not started to use the step button.
* Speed can be adjusted by 100 ms/step interval from 100 ms/step to 1000 ms/step while the simulation is paused or not started.
* The ability to set a neighborhood radius as well as the edge type (toroidal or finite) and the grid shape type (triangular, hexagonal, square).
* The ability to set a non totalitarian neighborhood such as edges only or corners only for rectangular grid.

* The ability to upload a different XML file, which stops the current simulation and loads in the new one with most asked parameters able to be set and loaded
* The ability to check the inputs of the XML file (valid string names, no null's for important variable, correct type of variable).
* The ability to use a user input the specific state and locations for Segregation only (though cannot reset)

Assumptions or Simplifications:
1. Person Class: assumes that if all spots that were empty at the beginning of the iteration through people for a person are now filled that the person will stay in the same place for another turn.
2. Shark/Fish Class: 
  1) Shark finds a neighbor fish and decides to eat it
       a) That fish has already acted and therefore this is a valid eat. (i.e if the fish is before the shark in
       the iteration of the array (from top left to bottom right).
       b) The fish has not already acted and therefore could move.
           i) The fish acts and stays where it is. Therefore the fish should be eaten (removed from grid)
           ii) The fish acts and moves to an empty neighbor space. There is then no fish in the location.
           The shark will then not get to eat anything. Nothing will change.
           iii) The fish acts and moves to a new location. It then breeds and the new fish it breeds ends up in the
           original fish's location. Meaning there is now a new fish in the location that the shark had planned on eating
           the original fish from.
               * In this case, assume that the new fish will NOT be eaten because the fish would not have existed in the
               state of the grid when the shark was making the eat/move decision.
           iv) The fish acts and moves to a new location. A different fish moves into the location of the old fish. That new fish
           was marked to be eaten by a different shark.
               * In this case the fish will not be eaten because it was not the original fish that the shark tried to eat.
           v) The fish acts and moves to a new location. A different fish moves into the location of the old fish. That fish
           was not marked as eaten by any shark.
               * This will be treated the same way as the situation above.
  2) Shark Breeds:
       a) Shark finds an empty spot and breeds a new shark into it. The steps left to breed are reset to the breeding period.
       b) Shark finds an empty spot and PLANS to breed a new shark into it, however a prior animal has either already
       placed a baby there or has moved there. In this case, the shark will attempt to breed again the next step.
       
Known Bugs:

Extra credit: N/A

### Notes
Left to Implement: 
* Infinite or Unbounded Neighbor Type
    * In order to implement this, I would use the isOuterBoundary function from the Grid class
    ```
    public boolean isOuterBoundary(int row, int col){
            return (row == 0 || row == myRows - 1 || col == 0 || col == myCols - 1);
        }
    ```
    If an actor moved to or was "born" on an cell that was an outerBoundary, I would ass a row and column to the grid by creating a new double array in the Grid class and then filling it with old values from the current grid. I would then switch the myGrid instance parameter to point to this new bigger grid. 
    
   * More Non totalitarian neighborhoods:
    * Since these are governed by the enum value of the 
    ```
    public enum GridNeighborsTypes{
            Total, VN, Corners;
        }
    ```
    More enum values could be added, and then corresponding Direction arrays could be associated with those values in the maps created in the Grid classes. For example, the rectangular class has:
    ```
    private static final Map<EnumParams.GridNeighborsTypes, Direction[]> NEIGHBOR_TYPES = Map.of(EnumParams.GridNeighborsTypes.Total, TOTAL_NEIGHBORS, EnumParams.GridNeighborsTypes.Corners, CORNER_NEIGHBORS, EnumParams.GridNeighborsTypes.VN, VN_NEIGHBORS);
    ```
    * Implementing more types of simulations:
        * In order to do this more easily, introducing a general patch class that could then hold an arraylist of actors (similarly to how the SugarPatch class holds the agents), would allow specific grid locations to have characteristics that could then be used to govern the actions of their actors. For example, in the foraging ants simulation being able to mark locations as "home" or "food source".
* Initialization
    *  started this step with exact state and locations lifted for Segregation and it (mostly) worked. The foundation for this step is implemented for each Simulation. All that is left is to change the fillGrid() method for the other simulations accordingly. Also, a way to find reset non-probabilitistic initialization needs to be found.   
    * to do this step with the number of states, would have an input n numbers for each n actors. Then I would create an array with the states at each location from those inputs, shuffle that array, and then perform the same method as if the exact locations of states were given. For example, if given 1 zero-state, 2 one-state, would create array [0,1,1], shuffle array [1,0,1], and then use that array as the location of states to create grid.
* Save current state of simulation
    * create array of locations and states by iterating through grid. then use Xstream to save that array to a tag <currentState> in the XML file.
    * size of each grid location
        * have tag <sidelength> in XML file. give variable to front end where object is made and displayed. Add scrolling feature to grid scene. 
### Impressions

