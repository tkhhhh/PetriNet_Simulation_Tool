package com.mycompany.app.PetriNet;

import java.util.ArrayList;
import java.util.HashMap;

public class PetriNet {
    private String id;
    private ArrayList<Edge> edges = new ArrayList<>();
    private HashMap<String, Transition> transitions = new HashMap<>();
    private HashMap<String, Place> places = new HashMap<>();

    public PetriNet() {
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPlaces(HashMap<String, Place> places) {
        this.places = places;
    }

    public HashMap<String, Place> getPlaces() {
        return places;
    }

    public void setTransitions(HashMap<String, Transition> transitions) {
        this.transitions = transitions;
    }

    public HashMap<String, Transition> getTransitions() {
        return transitions;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }
}
