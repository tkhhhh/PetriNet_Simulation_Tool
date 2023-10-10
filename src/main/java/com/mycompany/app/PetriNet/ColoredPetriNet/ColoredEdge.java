package com.mycompany.app.PetriNet.ColoredPetriNet;

import com.mycompany.app.PetriNet.Edge;
import fr.lip6.move.pnml.hlpn.hlcorestructure.HLAnnotation;
import fr.lip6.move.pnml.hlpn.hlcorestructure.Position;
import fr.lip6.move.pnml.hlpn.hlcorestructure.impl.ArcImpl;
import fr.lip6.move.pnml.hlpn.integers.impl.NumberConstantImpl;
import fr.lip6.move.pnml.hlpn.multisets.impl.AddImpl;
import fr.lip6.move.pnml.hlpn.multisets.impl.NumberOfImpl;
import fr.lip6.move.pnml.hlpn.terms.Term;
import fr.lip6.move.pnml.hlpn.terms.impl.VariableImpl;

import java.util.ArrayList;
import java.util.List;

public class ColoredEdge extends Edge {
    protected ArrayList<Variable> moveVariables = new ArrayList<>();
    protected ArrayList<Constant> moveConstants = new ArrayList<>();
    protected String condition;

    public ColoredEdge(ArcImpl arc){
        for(Position position : arc.getArcgraphics().getPositions()){
            int x = position.getX();
            int y = position.getY();
            route.add(new float[]{x,y});
        }
        id = arc.getId();
        source = arc.getSource().getId();
        target = arc.getTarget().getId();
        HLAnnotation hlAnnotation = arc.getHlinscription();
        abstractItem(hlAnnotation);
    }

    public ColoredEdge(){}

    protected void abstractItem(HLAnnotation hlAnnotation){
        Term term = hlAnnotation.getStructure();
        recursiveItem(term);
        condition = hlAnnotation.getText();
    }

    // number of tokens moved
    @Override
    public String covertCondition(){
        return String.valueOf(moveVariables.size());
    }

    protected void recursiveItem(Term term){
        if(term.getClass() == NumberOfImpl.class){
            NumberOfImpl numberOf = (NumberOfImpl) term;
            List<Term> subTerms = numberOf.getSubterm();
            Term termNumber = subTerms.get(0);
            NumberConstantImpl numberConstant = (NumberConstantImpl) termNumber;
            long number = numberConstant.getValue();
            Term termX = subTerms.get(1);
            if(termX.getClass() == NumberConstantImpl.class){
                Constant constant = new Constant(termX);
                while(number > 0){
                    moveConstants.add(constant);
                    number--;
                }
            }
            if(termX.getClass() == VariableImpl.class) {
                Variable variable = new Variable((VariableImpl) termX);
                while(number > 0) {
                    moveVariables.add(variable);
                    number--;
                }
            }
        }
        if(term.getClass() == AddImpl.class){
            AddImpl add = (AddImpl) term;
            List<Term> subTerms = add.getSubterm();
            for(Term subTerm: subTerms){
                recursiveItem(subTerm);
            }
        }
    }

    public ArrayList<Constant> getMoveConstants() {
        return moveConstants;
    }

    public ArrayList<Variable> getMoveVariables() {
        return moveVariables;
    }
}
