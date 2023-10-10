package com.mycompany.app.PetriNet.HighLevelPetriNet;

import com.mycompany.app.Painter.Algorithm;
import com.mycompany.app.PetriNet.ColoredPetriNet.ColoredPlace;
import com.mycompany.app.PetriNet.ColoredPetriNet.Constant;
import com.mycompany.app.PetriNet.ColoredPetriNet.Variable;
import com.mycompany.app.PetriNet.Place;
import fr.lip6.move.pnml.hlpn.hlcorestructure.impl.PlaceImpl;
import fr.lip6.move.pnml.hlpn.integers.impl.NumberConstantImpl;
import fr.lip6.move.pnml.hlpn.multisets.impl.AddImpl;
import fr.lip6.move.pnml.hlpn.multisets.impl.NumberOfImpl;
import fr.lip6.move.pnml.hlpn.terms.Term;
import fr.lip6.move.pnml.hlpn.terms.impl.VariableImpl;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HighLevelPlace extends ColoredPlace {

    //private ArrayList<String> tokens; // test
    private ArrayList<HighLevelEdge> outcomeEdges = new ArrayList<>();

    public HighLevelPlace(PlaceImpl place) {
        super(place);
    }

    public HighLevelPlace() {
        label = "";
    }

    public void setOutcomes(ArrayList<HighLevelEdge> outcomeEdges) {
        this.outcomeEdges = outcomeEdges;
    }

    public ArrayList<HighLevelEdge> getOutcomeEdges() {
        return outcomeEdges;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void drawPoints(Graphics graphics){
        long mark = currentVariables.size();
        HashSet<int[]> pointPositions = Algorithm.calculatePoints((int)x, (int)y, x_dim, (int)mark);
        int i = 0;
        for(int[] position: pointPositions){
            graphics.setColor(Color.blue);
            graphics.drawOval(position[0], position[1], 5,5);
            graphics.fillOval(position[0], position[1], 5,5);
            i++;
        }
    }

}
