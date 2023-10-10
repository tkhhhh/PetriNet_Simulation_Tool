package com.mycompany.app.PetriNet.ColoredPetriNet;


import com.mycompany.app.Painter.Algorithm;
import com.mycompany.app.PetriNet.Transition;
import fr.lip6.move.pnml.hlpn.booleans.impl.AndImpl;
import fr.lip6.move.pnml.hlpn.booleans.impl.OrImpl;
import fr.lip6.move.pnml.hlpn.hlcorestructure.Arc;
import fr.lip6.move.pnml.hlpn.hlcorestructure.impl.ArcImpl;
import fr.lip6.move.pnml.hlpn.hlcorestructure.impl.TransitionImpl;
import fr.lip6.move.pnml.hlpn.terms.Term;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// label is color
public class ColoredTransition extends Transition {
    protected ArrayList<Condition> conditions = new ArrayList<>();
    private ArrayList<ColoredEdge> inColoredEdges = new ArrayList<>();
    private ArrayList<ColoredEdge> outColoredEdges = new ArrayList<>();
    private ArrayList<ColoredEdge> coloredSatisfyingEdges = new ArrayList<>();
    protected boolean xor;

    public ArrayList<Condition> getConditions() {
        return conditions;
    }

    public ColoredTransition(TransitionImpl transition) {
        id = transition.getId();
        label = transition.getName().getText();
        x = transition.getNodegraphics().getPosition().getX();
        y = transition.getNodegraphics().getPosition().getY();
        x_dim = transition.getNodegraphics().getDimension().getX();
        y_dim = transition.getNodegraphics().getDimension().getY();
        List<Arc> arcList = transition.getInArcs();
        for(Arc arc: arcList) {
            ColoredEdge edge = new ColoredEdge((ArcImpl) arc);
            inColoredEdges.add(edge);
        }
        arcList = transition.getOutArcs();
        for(Arc arc: arcList) {
            ColoredEdge edge = new ColoredEdge((ArcImpl) arc);
            outColoredEdges.add(edge);
        }
        Term term = transition.getCondition().getStructure();
        analyseTerm(term);
    }

    public ColoredTransition(){}

    @Override
    public String getId() {
        return id;
    }

    protected void analyseTerm(Term term){
        if(term.getClass() == AndImpl.class) {
            xor = true;
            AndImpl and = (AndImpl) term;
            for(Term subTerm: and.getSubterm()){
                Condition condition = new Condition(subTerm);
                conditions.add(condition);
            }
        }
        if(term.getClass() == OrImpl.class) {
            xor = false;
            OrImpl or = (OrImpl) term;
            for(Term subTerm: or.getSubterm()){
                Condition condition = new Condition(subTerm);
                conditions.add(condition);
            }
        }
        // not recursive here
        // supports 2 simple conditions
    }

    public boolean fireCondition(ColoredPetriNet coloredPetriNet){
        if(xor){
            for(Condition condition: conditions){
                if(!condition.consider(coloredPetriNet)){
                    return false;
                }
            }
            return true;
        }else{
            for(Condition condition: conditions){
                if(condition.consider(coloredPetriNet)){
                    return true;
                }
            }
            return false;
        }
    }

    public void setConditions(ArrayList<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public void drawColor(Graphics graphics){
        graphics.setColor(new Color(Integer.parseInt(label)));
        graphics.fillRect((int)x, (int)y, x_dim, y_dim);
    }

    @Override
    public void drawPoints(Graphics graphics){
        int mark = 0;
        ArrayList<Variable> variables = new ArrayList<>();
        for(ColoredEdge coloredEdge: coloredSatisfyingEdges){
            mark += coloredEdge.getMoveVariables().size();
            variables.addAll(coloredEdge.getMoveVariables());
        }
        HashSet<int[]> pointPositions = Algorithm.calculatePoints((int)x, (int)y, x_dim, mark);
        int i = 0;
        for(int[] position: pointPositions){
            graphics.setColor(new Color(Integer.parseInt(variables.get(i).getName())));
            graphics.drawOval(position[0], position[1], 5,5);
            graphics.fillOval(position[0], position[1], 5,5);
            i++;
        }
    }

    public ArrayList<ColoredEdge> getInColoredEdges() {
        return inColoredEdges;
    }

    public ArrayList<ColoredEdge> getOutColoredEdges() {
        return outColoredEdges;
    }

    public ArrayList<ColoredEdge> getColoredSatisfyingEdges() {
        return coloredSatisfyingEdges;
    }

    public void setColoredSatisfyingEdges(ArrayList<ColoredEdge> coloredSatisfyingEdges) {
        this.coloredSatisfyingEdges = coloredSatisfyingEdges;
    }
}
