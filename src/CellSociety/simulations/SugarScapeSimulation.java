package CellSociety.simulations;

import CellSociety.XMLParser;
import CellSociety.actors.Actor;
import CellSociety.actors.Agent;
import CellSociety.actors.SugarPatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class SugarScapeSimulation extends Simulation{
    private double hasAgent;
    private Random dice = new Random();

    public SugarScapeSimulation(Map<String, String> args) {
        super(args);
        hasAgent = Double.parseDouble(getMyArgs().get(XMLParser.PROBABILITY_KEY));
        //create a rectangular grid that has # rows, # columns, and size a double of the side length of cell
        fillGrid();
    }

    @Override
    public String[] getActorNames() {
        return new String[0];
    }

    @Override
    public double[] getThresholdRange(){
        return new double[]{1, 10, 1};
    }

    public void fillGrid(){
        getMyGrid().fillGridRandomly(1, this);
    }

    @Override
    public Actor createActor(int row, int col){
        SugarPatch patch = createSugarPatch(row, col,dice.nextInt((int)Math.ceil(getThreshold())));
        if(dice.nextInt(PERCENTAGE_BOUND) < (hasAgent * PERCENTAGE_BOUND)){
            Agent a = createAgent(row, col, patch);
            patch.addAgent(a);
        }
        return patch;
    }

    public Actor createActor(int row, int col, int whichActor){
        SugarPatch patch = createSugarPatch(row, col, row);
        if(whichActor<0){
            Agent a = createAgent(row, col, patch);
            patch.addAgent(a);
            return patch;
        }
        return patch;
    }

    public SugarPatch createSugarPatch(int row, int col, int sugar){
        return new SugarPatch(getMyCellSize(), row, col, getMyGrid(), this, sugar, 1, SugarPatch.MAX_COLOR/(int)Math.ceil(getThreshold()));
    }

    public Agent createAgent(int row, int col, SugarPatch patch){
        return new Agent(getMyCellSize(), row, col, getMyGrid(), this, dice.nextInt(getMyRow()) + 1, 1, (int)Math.ceil(getThreshold()), patch);
    }


    @Override
    protected void updateAllCells(){
        //first store
        ArrayList<SugarPatch> patches = new ArrayList<>();
        ArrayList<Agent> agents = new ArrayList<>();
        //then act
        for (Actor actor : getMyGrid().getAllActors()) {
            if(!((SugarPatch)actor).checkEmpty()){
                agents.add(((SugarPatch)actor).getAgent());
            }
            patches.add(((SugarPatch)actor));
        }
        Collections.shuffle(agents);
        for(Agent a : agents){
            a.act();
        }
        for(SugarPatch patch : patches){
            patch.act();
        }
    };
}
