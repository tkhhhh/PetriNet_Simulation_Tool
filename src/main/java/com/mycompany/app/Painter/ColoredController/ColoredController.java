package com.mycompany.app.Painter.ColoredController;

import com.mycompany.app.Painter.Controller;
import com.mycompany.app.PetriNet.ColoredPetriNet.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ColoredController extends Controller {

    private ColoredTransition currentColoredTransition;
    private ColoredPetriNet coloredPetriNet;

    public ColoredController(ColoredPetriNet coloredPetriNet) {
        super(coloredPetriNet);
        this.coloredPetriNet = coloredPetriNet;
    }

    public ColoredPetriNet getColoredPetriNet() {
        return coloredPetriNet;
    }

    @Override
    public void executeTransition(){
        HashMap<String, ColoredTransition> coloredTransitionHashMap = coloredPetriNet.getColoredTransitionHashMap();
        int size = coloredTransitionHashMap.size();
        Random rand = new Random();
        int next = rand.nextInt(size);
        currentColoredTransition = (ColoredTransition) coloredTransitionHashMap.values().toArray()[next];
        currentColoredTransition.setSelected(true);
        if(conditionColor()){
            updateTokenBefore();
            updateTokenAfter();
        }else{
            updateTokenBefore();
        }
        /*
        for(ColoredTransition coloredTransition: coloredTransitionHashMap.values()){
            currentColoredTransition = coloredTransition;

            if(conditionColor()){
                updateToken();
            }
        }*/
    }

    private boolean condition(){
        if(currentColoredTransition.fireCondition(coloredPetriNet)){
            return conditionColor();
        }else{
            return false;
        }
    }

    // for color condition!!!!!
    private boolean conditionColor(){
        ArrayList<ColoredEdge> coloredSatisfyingEdges = currentColoredTransition.getColoredSatisfyingEdges();
        for(ColoredEdge coloredEdge: currentColoredTransition.getInColoredEdges()){
            ColoredPlace source = coloredPetriNet.getColoredPlaceHashMap().get(coloredEdge.getSource());
            /*
            if(!source.getCurrentConstants().containsAll(coloredEdge.getMoveConstants())){
                return false;
            }
             */
            boolean exist = false;
            for(ColoredEdge coloredEdge1: coloredSatisfyingEdges){
                if(coloredEdge1.equals(coloredEdge)){
                    exist = true;
                }
            }
            if(!exist){
                for(final Variable variable: coloredEdge.getMoveVariables()){
                    long countEdge = coloredEdge.getMoveVariables().stream().filter(variablex -> variablex.equal(variable)).count();
                    long countPlace = source.getCurrentVariables().stream().filter(variablex -> variablex.equal(variable)).count();
                    if (countPlace < countEdge){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void updateTokenBefore(){
        // update petriNet
        // change the amount of elements currently
        ArrayList<ColoredEdge> coloredSatisfyingEdges = currentColoredTransition.getColoredSatisfyingEdges();
        for(ColoredEdge coloredEdge: currentColoredTransition.getInColoredEdges()){
            ColoredPlace source = coloredPetriNet.getColoredPlaceHashMap().get(coloredEdge.getSource());
            /*
            ArrayList<Constant> constants = source.getCurrentConstants();
            constants.removeAll(coloredEdge.getMoveConstants());
            source.setCurrentConstants(constants);

             */
            ArrayList<Variable> variables = source.getCurrentVariables();
            int count = 0;
            Variable variableExample = coloredEdge.getMoveVariables().get(0);
            for(Variable variable: variables){
                if(variable.equal(variableExample)){
                    count += 1;
                }
            }
            if(count >= coloredEdge.getMoveVariables().size()){
                coloredSatisfyingEdges.add(coloredEdge);
                for(Variable variable1: coloredEdge.getMoveVariables()){
                    for(Variable variable: variables){
                        if(variable.equal(variable1)){
                            variables.remove(variable);
                            break;
                        }
                    }
                }
                source.setCurrentVariables(variables);
                String id = source.getId();
                // assign variable
                HashMap<String, ColoredPlace> coloredPlaces = coloredPetriNet.getColoredPlaceHashMap();
                coloredPlaces.put(id, source);
                coloredPetriNet.setColoredPlaceHashMap(coloredPlaces);
                petriNet = coloredPetriNet;
            }
        }
        String id = currentColoredTransition.getId();
        currentColoredTransition.setColoredSatisfyingEdges(coloredSatisfyingEdges);
        HashMap<String, ColoredTransition> transitionHashMap = coloredPetriNet.getColoredTransitionHashMap();
        transitionHashMap.put(id, currentColoredTransition);
        coloredPetriNet.setColoredTransitionHashMap(transitionHashMap);
        petriNet = coloredPetriNet;
    }

    private void updateTokenAfter(){
        for(ColoredEdge coloredEdge: currentColoredTransition.getOutColoredEdges()){
            ColoredPlace target = coloredPetriNet.getColoredPlaceHashMap().get(coloredEdge.getTarget());
            /*
            ArrayList<Constant> constants = target.getCurrentConstants();
            constants.addAll(coloredEdge.getMoveConstants());
            target.setCurrentConstants(constants);

             */
            ArrayList<Variable> variables = target.getCurrentVariables();
            variables.addAll(coloredEdge.getMoveVariables());
            target.setCurrentVariables(variables);
            String id = target.getId();
            // assign variable
            HashMap<String, ColoredPlace> coloredPlaces = coloredPetriNet.getColoredPlaceHashMap();
            coloredPlaces.put(id, target);
            coloredPetriNet.setColoredPlaceHashMap(coloredPlaces);
            petriNet = coloredPetriNet;
        }
        String id = currentColoredTransition.getId();
        currentColoredTransition.setColoredSatisfyingEdges(new ArrayList<>());
        HashMap<String, ColoredTransition> transitionHashMap = coloredPetriNet.getColoredTransitionHashMap();
        transitionHashMap.put(id, currentColoredTransition);
        coloredPetriNet.setColoredTransitionHashMap(transitionHashMap);
        petriNet = coloredPetriNet;
    }

}
