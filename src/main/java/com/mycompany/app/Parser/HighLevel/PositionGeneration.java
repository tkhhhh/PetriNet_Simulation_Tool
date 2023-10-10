package com.mycompany.app.Parser.HighLevel;

import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelEdge;
import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelPetriNet;
import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelPlace;
import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class PositionGeneration {
    private HighLevelPetriNet highLevelPetriNet;
    private int totalY = 40;
    private int totalX = 200;
    private Queue<String> nodes = new LinkedList<>();
    private int currentSize = 0;

    public void positionGenerator(){
        HighLevelPlace entryPlace = highLevelPetriNet.getEntryPlace();
        entryPlace.setX((float)200);
        entryPlace.setY((float)40);
        entryPlace.setX_dim(40);

        HashMap<String, HighLevelPlace> highLevelPlaceHashMap = highLevelPetriNet.getHighLevelPlaceHashMap();
        highLevelPlaceHashMap.put(entryPlace.getId(), entryPlace);

        nodes.add(entryPlace.getId());

        while(!nodes.isEmpty()){
            currentSize = nodes.size();
            while(currentSize > 0){
                String currentNode = nodes.poll();
                updatePosition(currentNode);
                currentSize--;
            }
        }

    }

    public void updatePosition(String currentNode){
        if(highLevelPetriNet.getHighLevelPlaceHashMap().containsKey(currentNode)){
            HashMap<String, HighLevelPlace> highLevelPlaceHashMap = highLevelPetriNet.getHighLevelPlaceHashMap();
            HighLevelPlace highLevelPlace = highLevelPlaceHashMap.get(currentNode);

            // update position
            int step = 80;
            int sideSize = highLevelPlace.getOutcomeEdges().size() / 2;
            boolean middle = highLevelPlace.getOutcomeEdges().size() % 2 == 1;
            HashMap<String, HighLevelTransition> highLevelTransitionHashMap = highLevelPetriNet.getHighLevelTransitionHashMap();
            float[] start = new float[]{highLevelPlace.getX() + highLevelPlace.getX_dim()/2,
                                    highLevelPlace.getY() + highLevelPlace.getX_dim()};
            ArrayList<HighLevelEdge> highLevelEdges = highLevelPlace.getOutcomeEdges();
            for(HighLevelEdge edge: highLevelEdges){
                HighLevelTransition currentTransition = highLevelTransitionHashMap.get(edge.getTarget());
                currentTransition.setX_dim(40);
                currentTransition.setY_dim(20);
                currentTransition.setY(highLevelPlace.getY() + 60);
                if(middle){
                    currentTransition.setX(highLevelPlace.getX());
                    middle = false;
                }else{
                    if(sideSize == 0){
                        sideSize--;
                    }
                    currentTransition.setX(highLevelPlace.getX() - sideSize*step);
                    sideSize--;
                }

                float[] end = new float[]{currentTransition.getX() + currentTransition.getX_dim()/2,
                        currentTransition.getY()};

                ArrayList<float[]> route = new ArrayList<>();
                route.add(start);
                route.add(end);
                edge.setRoute(route);

                ArrayList<HighLevelEdge> incomeEdges = currentTransition.getInHighLevelEdges();
                for(HighLevelEdge highLevelEdge: incomeEdges){
                    if(highLevelEdge.getId().equals(edge.getId())){
                        highLevelEdge.setRoute(route);
                    }
                }
                currentTransition.setInHighLevelEdges(incomeEdges);
                // update in income edges in transition

                highLevelTransitionHashMap.put(currentTransition.getId(), currentTransition);
                highLevelPetriNet.setHighLevelTransitionHashMap(highLevelTransitionHashMap);
                // update in petri net transition

                ArrayList<HighLevelEdge> highLevelPetriNetHighLevelEdges = highLevelPetriNet.getHighLevelEdges();
                for(HighLevelEdge highLevelEdge: highLevelPetriNetHighLevelEdges){
                    if(highLevelEdge.getId().equals(edge.getId())){
                        highLevelEdge.setRoute(route);
                    }
                }
                highLevelPetriNet.setHighLevelEdges(highLevelPetriNetHighLevelEdges);
                // update in petri net edges

                nodes.add(edge.getTarget());
            }
            highLevelPlace.setOutcomes(highLevelEdges); // update in this place outcome edges
            highLevelPlaceHashMap.put(highLevelPlace.getId(), highLevelPlace); // update in petri net placeMap
        }else{
            HashMap<String, HighLevelTransition> highLevelTransitionHashMap = highLevelPetriNet.getHighLevelTransitionHashMap();
            HighLevelTransition highLevelTransition = highLevelTransitionHashMap.get(currentNode);

            // update
            int step = 80;
            int sideSize = highLevelTransition.getOutHighLevelEdges().size() / 2;
            boolean middle = highLevelTransition.getOutHighLevelEdges().size() % 2 == 1;
            HashMap<String, HighLevelPlace> highLevelPlaceHashMap = highLevelPetriNet.getHighLevelPlaceHashMap();
            float[] start = new float[]{highLevelTransition.getX() + highLevelTransition.getX_dim()/2,
                    highLevelTransition.getY() + highLevelTransition.getY_dim()};
            ArrayList<HighLevelEdge> highLevelEdges = highLevelTransition.getOutHighLevelEdges();
            for(HighLevelEdge edge: highLevelEdges){
                HighLevelPlace currentPlace = highLevelPlaceHashMap.get(edge.getTarget());
                currentPlace.setX_dim(40);
                currentPlace.setY_dim(40);
                currentPlace.setY(highLevelTransition.getY() + 60);
                if(middle){
                    currentPlace.setX(highLevelTransition.getX());
                    middle = false;
                }else{
                    if(sideSize == 0) {
                        sideSize--;
                    }
                    currentPlace.setX(highLevelTransition.getX() - sideSize*step);
                    sideSize--;
                }

                float[] end = new float[]{currentPlace.getX() + currentPlace.getX_dim()/2,
                        currentPlace.getY()};

                ArrayList<float[]> route = new ArrayList<>();
                route.add(start);
                route.add(end);
                edge.setRoute(route);

                /*
                ArrayList<HighLevelEdge> incomeEdges = currentPlace.get();
                for(HighLevelEdge highLevelEdge: incomeEdges){
                    if(highLevelEdge.getId().equals(edge.getId())){
                        highLevelEdge.setRoute(route);
                    }
                }
                currentTransition.setInHighLevelEdges(incomeEdges);
                */
                // update in income edges in transition

                highLevelPlaceHashMap.put(currentPlace.getId(), currentPlace);
                highLevelPetriNet.setHighLevelPlaceHashMap(highLevelPlaceHashMap);
                // update in petri net place

                ArrayList<HighLevelEdge> highLevelPetriNetHighLevelEdges = highLevelPetriNet.getHighLevelEdges();
                for(HighLevelEdge highLevelEdge: highLevelPetriNetHighLevelEdges){
                    if(highLevelEdge.getId().equals(edge.getId())){
                        highLevelEdge.setRoute(route);
                    }
                }
                highLevelPetriNet.setHighLevelEdges(highLevelPetriNetHighLevelEdges);
                // update in petri net edges

                nodes.add(edge.getTarget());
            }
            highLevelTransition.setOutHighLevelEdges(highLevelEdges); // update in this transition outcome edges
            highLevelTransitionHashMap.put(highLevelTransition.getId(), highLevelTransition); // update in petri net transitionMap
        }
    }

    public PositionGeneration(HighLevelPetriNet highLevelPetriNet){
        this.highLevelPetriNet = highLevelPetriNet;
    }

    public HighLevelPetriNet getHighLevelPetriNet() {
        return highLevelPetriNet;
    }
}
