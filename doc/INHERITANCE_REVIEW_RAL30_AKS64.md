INHERITANCE_REVIEW_RAL30_AKS64.md
====
### Part 1
1. What is an implementation decision that your design is encapsulating (i.e., hiding) for other areas of the program?
All of the XML parsing scripts will be held in its own class rather than in another class, minimizing the amount of code in the other class to parse the XML file. The parser will then be called by the Simulator class that will then feed the appropriate parameters to the grid or the actors. 

2. What inheritance hierarchies are you intending to build within your area and what behavior are they based around?

I do not plan on intending to build an inheritance hierarchy for the XML parser because the parsing behavior is very similar across the simulations. The only area that may be different across simulations is the number of parameters for the simulation (such as probabilities, thresholds, etc.). However, this will be avoided by adding all of the parameters as one input and then parsed accordingly.

3. What parts within your area are you trying to make closed and what parts open to take advantage of this polymorphism you are creating?

There is no polymorphism because we are not using inheritance. 


4. What exceptions (error cases) might occur in your area and how will you handle them (or not, by throwing)?

A posible error is when bad data is given such as a lack of data or the wrong type of data is given (words instead of number for a number variable). These will be handled by throwing an exception stating which input is not appropriate or missing. 

5. Why do you think your design is good (also define what your measure of good is)?

My design can be easily modified because all of the rules are contained in one area. In addition, this removes duplicate code. 


### Part 2
1. How is your area linked to/dependent on other areas of the project?

The actor class receives the parameters for the rules of the simulation. The grid class receives the size of grid. The simulation class receives the name of the simulation. 

2. Are these dependencies based on the other class's behavior or implementation?

These dependencies are based on the class's behavior. The point of the XML parser is to supply these other classs with the simulation specific information. 

3. How can you minimize these dependencies?

The dependencies can be minimized by having only one class receive the data from the XML paser and then sending that data to the rest of the appropriate classes.

4. Go over one pair of super/sub classes in detail to see if there is room for improvement.

There are no super or sub classes.
 
5. Focus on what things they have in common (these go in the superclass) and what about them varies (these go in the subclass).

There are no super or sub classes.

### Part 3
1. Come up with at least five use cases for your part (most likely these will be useful for both teams).

Change the values of each tag to different appropriate values

Change the name of simulation to not existing simulation

Change the number of parameters to the "wrong" number of parameters for the simulation

Change the parameter inputs to a string

Change the value of the parameters to negative values


2. What feature/design problem are you most excited to work on?

I am most excited to work on finding a pattern throughout the different XML files for each simulation. 

3. What feature/design problem are you most worried about working on?

I am most worried about realizing that there is no reasonable pattern for different simulations, causing me to change my design to use inheritance. 

