package com.mycompany.app.PetriNet.HighLevelPetriNet;

import com.mycompany.app.PetriNet.ColoredPetriNet.ColoredPetriNet;

import java.util.ArrayList;
import java.util.HashMap;

public class HighLevelPetriNet extends ColoredPetriNet {
    private ArrayList<HighLevelEdge> highLevelEdges = new ArrayList<>();
    private HashMap<String, HighLevelPlace> highLevelPlaceHashMap = new HashMap<>();
    private HashMap<String, HighLevelTransition> highLevelTransitionHashMap = new HashMap<>();
    private HighLevelPlace entryPlace = new HighLevelPlace();

    public void setEntryPlace(HighLevelPlace entryPlace) {
        this.entryPlace = entryPlace;
    }

    // transition -> place // place -> transition
    public void linkPlaceAndTransition(){
        for(HighLevelEdge highLevelEdge: highLevelEdges){
            String source = highLevelEdge.getSource();
            String target = highLevelEdge.getTarget();
            if(highLevelPlaceHashMap.containsKey(source)){
                HighLevelPlace prevPlace = highLevelPlaceHashMap.get(source);

                ArrayList<HighLevelEdge> outcomes = prevPlace.getOutcomeEdges();
                outcomes.add(highLevelEdge);
                prevPlace.setOutcomes(outcomes);

                highLevelPlaceHashMap.put(source, prevPlace);
            }else{
                HighLevelTransition prevTransition = highLevelTransitionHashMap.get(source);

                ArrayList<HighLevelEdge> outcomes = prevTransition.getOutHighLevelEdges();
                outcomes.add(highLevelEdge);
                prevTransition.setOutHighLevelEdges(outcomes);

                highLevelTransitionHashMap.put(source, prevTransition);
            }
        }
    }

    public HighLevelPlace getEntryPlace() {
        return entryPlace;
    }

    public void setHighLevelEdges(ArrayList<HighLevelEdge> highLevelEdges) {
        this.highLevelEdges = highLevelEdges;
    }

    public void setHighLevelPlaceHashMap(HashMap<String, HighLevelPlace> highLevelPlaceHashMap) {
        this.highLevelPlaceHashMap = highLevelPlaceHashMap;
    }

    public void setHighLevelTransitionHashMap(HashMap<String, HighLevelTransition> highLevelTransitionHashMap) {
        this.highLevelTransitionHashMap = highLevelTransitionHashMap;
    }

    public ArrayList<HighLevelEdge> getHighLevelEdges() {
        return highLevelEdges;
    }

    public HashMap<String, HighLevelPlace> getHighLevelPlaceHashMap() {
        return highLevelPlaceHashMap;
    }

    public HashMap<String, HighLevelTransition> getHighLevelTransitionHashMap() {
        return highLevelTransitionHashMap;
    }
}
