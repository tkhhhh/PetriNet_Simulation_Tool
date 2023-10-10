package com.mycompany.app.PetriNet.ColoredPetriNet;

import com.mycompany.app.Painter.Algorithm;
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

public class ColoredPlace extends Place {
    protected ArrayList<Variable> currentVariables = new ArrayList<>();
    // id -> variable
    protected ArrayList<Constant> currentConstants = new ArrayList<>();

    //private ArrayList<String> tokens; // test

    public ColoredPlace(PlaceImpl place){
        Term term = place.getHlinitialMarking().getStructure();
        analyseTerm(term);
        label = place.getName().getText();
        id = place.getId();
        x = place.getNodegraphics().getPosition().getX();
        y = place.getNodegraphics().getPosition().getY();
        x_dim = place.getNodegraphics().getDimension().getX();
        y_dim = place.getNodegraphics().getDimension().getY();
    }

    public ColoredPlace(){}

    @Override
    public String getId() {
        return id;
    }

    protected void analyseTerm(Term term){
        if(term.getClass() == AddImpl.class) {
            AddImpl add = (AddImpl) term;
            List<Term> subTerms = add.getSubterm();
            for(Term subTerm: subTerms){
                analyseTerm(subTerm);
            }
            // add
        }
        if(term.getClass() == NumberOfImpl.class) {
            NumberOfImpl numberOf = (NumberOfImpl) term;
            List<Term> subTerms = numberOf.getSubterm();
            Term termNumber = subTerms.get(0);
            NumberConstantImpl numberConstant = (NumberConstantImpl) termNumber;
            long number = numberConstant.getValue();
            Term termX = subTerms.get(1);
            if(termX.getClass() == NumberConstantImpl.class){
                Constant constant = new Constant(termX);
                while(number > 0){
                    currentConstants.add(constant);
                    number--;
                }
            }
            if(termX.getClass() == VariableImpl.class) {
                Variable variable = new Variable((VariableImpl) termX);
                while(number > 0) {
                    currentVariables.add(variable);
                    number--;
                }
            }
        }
    }

    public ArrayList<Constant> getCurrentConstants() {
        return currentConstants;
    }

    public void setCurrentConstants(ArrayList<Constant> currentConstants) {
        this.currentConstants = currentConstants;
    }

    public void setCurrentVariables(ArrayList<Variable> currentVariables) {
        this.currentVariables = currentVariables;
    }

    public ArrayList<Variable> getCurrentVariables() {
        return currentVariables;
    }

    @Override
    public void drawPoints(Graphics graphics){
        long mark = currentVariables.size();
        HashSet<int[]> pointPositions = Algorithm.calculatePoints((int)x, (int)y, x_dim, (int)mark);
        int i = 0;
        for(int[] position: pointPositions){
            graphics.setColor(new Color(Integer.parseInt(currentVariables.get(i).getName())));
            graphics.drawOval(position[0], position[1], 5,5);
            graphics.fillOval(position[0], position[1], 5,5);
            i++;
        }
    }
}
