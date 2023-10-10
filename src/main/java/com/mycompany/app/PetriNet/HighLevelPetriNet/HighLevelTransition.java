package com.mycompany.app.PetriNet.HighLevelPetriNet;


import com.mycompany.app.PetriNet.ColoredPetriNet.ColoredTransition;
import com.mycompany.app.PetriNet.ColoredPetriNet.Condition;
import com.mycompany.app.PetriNet.Edge;
import fr.lip6.move.pnml.hlpn.booleans.impl.AndImpl;
import fr.lip6.move.pnml.hlpn.booleans.impl.OrImpl;
import fr.lip6.move.pnml.hlpn.hlcorestructure.Arc;
import fr.lip6.move.pnml.hlpn.hlcorestructure.impl.ArcImpl;
import fr.lip6.move.pnml.hlpn.hlcorestructure.impl.TransitionImpl;
import fr.lip6.move.pnml.hlpn.terms.Term;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HighLevelTransition extends ColoredTransition {
    private ArrayList<HighLevelEdge> inHighLevelEdges = new ArrayList<>();
    private ArrayList<HighLevelEdge> outHighLevelEdges = new ArrayList<>();

    public HighLevelTransition(TransitionImpl transition) {
        id = transition.getId();
        label = transition.getName().getText();
        x = transition.getNodegraphics().getPosition().getX();
        y = transition.getNodegraphics().getPosition().getY();
        x_dim = transition.getNodegraphics().getDimension().getX();
        y_dim = transition.getNodegraphics().getDimension().getY();
        List<Arc> arcList = transition.getInArcs();
        for(Arc arc: arcList) {
            HighLevelEdge edge = new HighLevelEdge((ArcImpl) arc);
            inHighLevelEdges.add(edge);
        }
        arcList = transition.getOutArcs();
        for(Arc arc: arcList) {
            HighLevelEdge edge = new HighLevelEdge((ArcImpl) arc);
            outHighLevelEdges.add(edge);
        }
        Term term = transition.getCondition().getStructure();
        analyseTerm(term);
    }

    public void setInHighLevelEdges(ArrayList<HighLevelEdge> inHighLevelEdges){
        this.inHighLevelEdges = inHighLevelEdges;
    }

    public void setOutHighLevelEdges(ArrayList<HighLevelEdge> outHighLevelEdges){
        this.outHighLevelEdges = outHighLevelEdges;
    }

    public HighLevelTransition(){
        label = "transition";
    }

    @Override
    public String getId() {
        return id;
    }

    public boolean fireCondition(HighLevelPetriNet highLevelPetriNet){
        if(xor){
            for(Condition condition: conditions){
                if(!condition.consider(highLevelPetriNet)){
                    return false;
                }
            }
            return true;
        }else{
            for(Condition condition: conditions){
                if(condition.consider(highLevelPetriNet)){
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void drawColor(Graphics graphics){
        graphics.setColor(Color.GRAY);
        graphics.fillRect((int)x, (int)y, x_dim, y_dim);
    }

    @Override
    public void drawPoints(Graphics graphics) {

    }

    public ArrayList<HighLevelEdge> getInHighLevelEdges() {
        return inHighLevelEdges;
    }

    public ArrayList<HighLevelEdge> getOutHighLevelEdges() {
        return outHighLevelEdges;
    }
}
