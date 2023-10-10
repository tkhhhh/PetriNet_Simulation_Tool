package com.mycompany.app.PetriNet.ColoredPetriNet;

import com.mycompany.app.PetriNet.PetriNet;

import java.util.ArrayList;
import java.util.HashMap;

public class ColoredPetriNet extends PetriNet {
    protected HashMap<String, Variable> variables = new HashMap<>();
    private ArrayList<ColoredEdge> coloredEdges = new ArrayList<>();
    private HashMap<String, ColoredPlace> coloredPlaceHashMap = new HashMap<>();
    private HashMap<String, ColoredTransition> coloredTransitionHashMap = new HashMap<>();

    public HashMap<String, Variable> getVariables() {
        return variables;
    }

    public void setColoredEdges(ArrayList<ColoredEdge> coloredEdges) {
        this.coloredEdges = coloredEdges;
    }

    public void setColoredPlaceHashMap(HashMap<String, ColoredPlace> coloredPlaceHashMap) {
        this.coloredPlaceHashMap = coloredPlaceHashMap;
    }

    public void setVariables(HashMap<String, Variable> variables) {
        this.variables = variables;
    }

    public void setColoredTransitionHashMap(HashMap<String, ColoredTransition> coloredTransitionHashMap) {
        this.coloredTransitionHashMap = coloredTransitionHashMap;
    }

    public ArrayList<ColoredEdge> getColoredEdges() {
        return coloredEdges;
    }

    public HashMap<String, ColoredPlace> getColoredPlaceHashMap() {
        return coloredPlaceHashMap;
    }

    public HashMap<String, ColoredTransition> getColoredTransitionHashMap() {
        return coloredTransitionHashMap;
    }
}
