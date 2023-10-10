package com.mycompany.app.Painter;

import com.mycompany.app.PetriNet.ColoredPetriNet.ColoredEdge;
import com.mycompany.app.PetriNet.ColoredPetriNet.ColoredPetriNet;
import com.mycompany.app.PetriNet.ColoredPetriNet.ColoredPlace;
import com.mycompany.app.PetriNet.ColoredPetriNet.ColoredTransition;
import com.mycompany.app.PetriNet.Edge;
import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelEdge;
import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelPetriNet;
import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelPlace;
import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelTransition;
import com.mycompany.app.PetriNet.PetriNet;
import com.mycompany.app.PetriNet.Place;
import com.mycompany.app.PetriNet.Transition;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;


public class Drawer extends JPanel {
    private HashSet<String> drawnNode = new HashSet<>();
    private Edge currentEdge;

    private PetriNet currentNet;
    private ColoredPetriNet currentColoredPetriNet;
    private HighLevelPetriNet currentHighLevelPetriNet;
    double scale;

    public Drawer(PetriNet petriNet) {
        currentNet = petriNet;
        setGraphics();
    }

    public Drawer(ColoredPetriNet coloredPetriNet) {
        currentColoredPetriNet = coloredPetriNet;
        setGraphics();
    }

    public Drawer(HighLevelPetriNet highLevelPetriNet) {
        this.currentHighLevelPetriNet = highLevelPetriNet;
        setGraphics();
    }

    public Drawer() {
        setGraphics();
    }

    public void setCurrentNet(PetriNet currentNet) {
        this.currentNet = currentNet;
    }

    private void drawEdge(Graphics graphics) {
        if(!drawnNode.contains(currentEdge.getId())) {
            currentEdge.draw(graphics);
            drawnNode.add(currentEdge.getId());
        }
    }

    private void drawNode(String id, Graphics graphics) {
        if(!drawnNode.contains(id)) {
            if(currentNet.getPlaces().containsKey(id)) {
                Place source = currentNet.getPlaces().get(id);
                source.draw(graphics);
            } else {
                Transition source = currentNet.getTransitions().get(id);
                source.draw(graphics);
            }
            drawnNode.add(id);
        }
    }

    private void drawColoredNode(String id, Graphics graphics) {
        if(!drawnNode.contains(id)) {
            if(currentColoredPetriNet.getColoredPlaceHashMap().containsKey(id)) {
                ColoredPlace source = currentColoredPetriNet.getColoredPlaceHashMap().get(id);
                source.draw(graphics);
            } else {
                ColoredTransition source = currentColoredPetriNet.getColoredTransitionHashMap().get(id);
                source.draw(graphics);
            }
            drawnNode.add(id);
        }
    }

    private void drawHighLevelNode(String id, Graphics graphics) {
        if(!drawnNode.contains(id)) {
            if(currentHighLevelPetriNet.getHighLevelPlaceHashMap().containsKey(id)) {
                HighLevelPlace source = currentHighLevelPetriNet.getHighLevelPlaceHashMap().get(id);
                source.draw(graphics);
            } else {
                HighLevelTransition source = currentHighLevelPetriNet.getHighLevelTransitionHashMap().get(id);
                source.draw(graphics);
            }
            drawnNode.add(id);
        }
    }

    private void setGraphics() {
        setPreferredSize(new Dimension(700, 700));
        //this.setLayout(null);
    }

    @Override
    public void paint(Graphics graphics){
        super.paintComponent(graphics);
        if(currentNet != null) {
            ArrayList<Edge> edges = currentNet.getEdges();
            for(Edge edge: edges) {
                currentEdge = edge;
                drawEdge(graphics);
                String sourceId = currentEdge.getSource();
                String targetId = currentEdge.getTarget();
                drawNode(sourceId, graphics);
                drawNode(targetId, graphics);
            }
        }
        if(currentColoredPetriNet != null) {
            ArrayList<ColoredEdge> coloredEdges = currentColoredPetriNet.getColoredEdges();
            for(ColoredEdge coloredEdge: coloredEdges){
                currentEdge = coloredEdge;
                drawEdge(graphics);
                String sourceId = currentEdge.getSource();
                String targetId = currentEdge.getTarget();
                drawColoredNode(sourceId, graphics);
                drawColoredNode(targetId, graphics);
            }
        }
        if(currentHighLevelPetriNet != null) {
            ArrayList<HighLevelEdge> highLevelEdges = currentHighLevelPetriNet.getHighLevelEdges();
            for(HighLevelEdge highLevelEdge: highLevelEdges){
                currentEdge = highLevelEdge;
                drawEdge(graphics);
                String sourceId = currentEdge.getSource();
                String targetId = currentEdge.getTarget();
                drawHighLevelNode(sourceId, graphics);
                drawHighLevelNode(targetId, graphics);
            }
        }
        /*
        graphics.setColor(Color.BLACK);
        graphics.drawRect((int)40, (int)40, 20, 60);
        graphics.setColor(Color.GRAY);
        graphics.fillRect((int)40, (int)40, 20, 60);
         */
    }

    @Override
    public void update(Graphics graphics){
        paint(graphics);
    }
}
