### Part 1
* What is an implementation decision that your design is encapsulating (i.e., hiding) for other areas of the program?
    * Each type of cell encapsulates the logic needed to update itself to its future state
    * Finding groups of cells within our grid class
        * grid encapsulates all adding and removing and moving in grid.
    * Simulation class handles game setup --> has access to the params from the XML file and decides where actors are actually placed

* What inheritance hierarchies are you intending to build within your area and what behavior are they based around?
    * Actor hierarchy has subclasses that are divided by the additional information they need to update to future state as well as the way they act.
    * Right now there is also an Animal abstract class that is a subclass of Actor and the parent class of Shark and Fish.
    
    * Simulation Class that has subclasses separated by how the grid needs to be filled up, what kinds of probabilities are needed and what actors are being used.

    * Grid class that abstracts the functions that grid needs to encapsulate and then a Rectangular grid class that implements using a 2D array. This way if we change the data structure holding our actors, no other classes will need to change (except for simulation currently, but the filling of cells will be moved into grid soon)
    
* What parts within your area are you trying to make closed and what parts open to take advantage of this polymorphism you are creating?
    * Everything about cells is closed except for the draw and act method. 
    * Open to extension because adding a simulation would only require creating an actor class for the simulation and a simulation class to fill the grid.
    * simulation class talks to actors but actors don't talk back.
        * actor asks grid for information but grid doesn't know anything about it's actors other than that it is holding actors in locations.
   
* What exceptions (error cases) might occur in your area and how will you handle them (or not, by throwing)?
    * A cell trying to move itself to a place in the grid that is already full or a cell removing itself from the wrong location (could cause an error if you try to remove something from an empty location)
    * Put checks into the grid file and print helpful warnngs.

* Why do you think your design is good (also define what your measure of good is)?
    * It's open to extension and closed to modification (I was able to get three simulations working in a matter of days just by adding new actor classes and writing simulation files)
    * Helpful inheritance hierarchy
    * Good = following the design principles discussed in class.


### Part 2
* How is your area linked to/dependent on other areas of the project?
    * The simulation classes use the information parsed form the XML files
    * The GUI determines how fast the simulation runs, when it starts/stops/steps/resets
    * My area provides a root group to display to the GUI.
  
* Are these dependencies based on the other class's behavior or implementation?
    * My area needs to receive a map of the XML information, but doesn't care how the creation of the map is implemented.
    * My visualization depends on my actors to have a draw method that returns a view, but doesn't care how I display that view.                
    
* How can you minimize these dependencies?
    * Keep the implementation of each area encapsulated and access the necessary linked information through function calls.
    
* Go over one pair of super/sub classes in detail to see if there is room for improvement. 
    * Actor and subActors 
        * Actor: row, col, list of neighbor locations, list of empty neighborlocations, list of neighborActors. 
            * methods: getters for variables, movement around grid
            
         * SubActor: getColor, act, saveNeighborStatus, threshold values, probabilities, booleans of state (isBurning etc)
     
* Focus on what things they have in common (these go in the superclass) and what about them varies (these go in the subclass).
        * Vary in the information they need to act and how they act as well as what color they are.
        
### Part 3
* Come up with at least five use cases for your part (most likely these will be useful for both teams).
    * Creating and filling a grid with actors for a fire simulation 
        * Border of empty cells
        * Place trees and determine if they're on fire based on probabilities
    * Determine if a center tree cell that isn't currently on fire should become on fire
    * Move a segregation simulation actor to a random empty location when unsatisfied
    * Determine if a corner cell of the segregation class is unsatisfied
    * Breed a new fish from a fish cell after determining the cell has completed the breeding period.
    
* What feature/design problem are you most excited to work on?
    * Getting the last simulation working! Fish!
    
* What feature/design problem are you most worried about working on?
    * Determining how the fish and shark will move/breed and eat each other while not impacting the states of the neighbors