package com.mycompany.app.Painter;

import com.mycompany.app.PetriNet.Edge;
import com.mycompany.app.PetriNet.PetriNet;
import com.mycompany.app.PetriNet.Place;
import com.mycompany.app.PetriNet.Transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class Controller {
    protected PetriNet petriNet;
    private Transition currentTransition;
    private boolean asynchronous = false;

    public Controller(PetriNet petriNet){
        this.petriNet = petriNet;
    }

    public PetriNet getPetriNet() {
        return petriNet;
    }

    public void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }

    public void executeTransition(){
        HashMap<String, Transition> transitionHashMap = petriNet.getTransitions();
        if (asynchronous) {
            for(Transition transition: transitionHashMap.values()){
                currentTransition = transition;
                currentTransition.setSelected(true);
                if(condition()){
                    updateTokenBefore();
                    updateTokenAfter();
                }else{
                    updateTokenBefore();
                }
            }
        }
        else {
            int size = transitionHashMap.size();
            Random rand = new Random();
            int next = rand.nextInt(size);
            currentTransition = (Transition) transitionHashMap.values().toArray()[next];
            currentTransition.setSelected(true);
            if(condition()){
                updateTokenBefore();
                updateTokenAfter();
            }else{
                updateTokenBefore();
            }
        }
    }

    private boolean condition(){
        ArrayList<Edge> incomes = currentTransition.getInEdges();
        HashMap<String, Place> placeHashMap = petriNet.getPlaces();
        ArrayList<Edge> satisfyingEdges = currentTransition.getSatisfyingEdges();
        for(Edge edge: incomes){
            Place source = placeHashMap.get(edge.getSource());
            if(source.getNumberOfTokenInit() < edge.getCondition()){
                boolean exist = false;
                for(Edge edge1: satisfyingEdges){
                    if (Objects.equals(edge1.getId(), edge.getId())) {
                        exist = true;
                        break;
                    }
                }
                if(!exist) return false;
            }
        }
        return true;
    }

    private void updateTokenAfter(){
        HashMap<String, Place> placeHashMap = petriNet.getPlaces();
        ArrayList<Edge> outcomes = currentTransition.getOutEdges();
        for(Edge edge: outcomes){
            Place target = placeHashMap.get(edge.getTarget());
            long tokens = target.getNumberOfTokenInit() + edge.getCondition();
            target.setNumberOfTokenInit(tokens);
            placeHashMap.put(edge.getTarget(), target);
            petriNet.setPlaces(placeHashMap);
        }
        currentTransition.setSatisfyingEdges(new ArrayList<>());
        String transitionId = currentTransition.getId();
        HashMap<String, Transition> transitionHashMap = petriNet.getTransitions();
        transitionHashMap.put(transitionId, currentTransition);
        petriNet.setTransitions(transitionHashMap);
    }

    private void updateTokenBefore(){
        ArrayList<Edge> incomes = currentTransition.getInEdges();
        ArrayList<Edge> satisfyingEdges = currentTransition.getSatisfyingEdges();
        HashMap<String, Place> placeHashMap = petriNet.getPlaces();
        for(Edge edge: incomes){
            Place source = placeHashMap.get(edge.getSource());
            if(source.getNumberOfTokenInit() >= edge.getCondition()){
                satisfyingEdges.add(edge);
                long tokens = source.getNumberOfTokenInit() - edge.getCondition();
                source.setNumberOfTokenInit(tokens);
                placeHashMap.put(edge.getSource(), source);
                petriNet.setPlaces(placeHashMap);
            }
        }
        currentTransition.setSatisfyingEdges(satisfyingEdges);
        String transitionId = currentTransition.getId();
        HashMap<String, Transition> transitionHashMap = petriNet.getTransitions();
        transitionHashMap.put(transitionId, currentTransition);
        petriNet.setTransitions(transitionHashMap);
    }

}