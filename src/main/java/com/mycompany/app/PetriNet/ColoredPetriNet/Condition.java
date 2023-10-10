package com.mycompany.app.PetriNet.ColoredPetriNet;


import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelPetriNet;
import fr.lip6.move.pnml.hlpn.terms.Term;

public class Condition {
    private BinaryOperator binaryOperator;

    public Condition(Term term){
        binaryOperator = new BinaryOperator(term);
    }

    public boolean consider(ColoredPetriNet coloredPetriNet){
        binaryOperator.invoke(coloredPetriNet);
        return (boolean)binaryOperator.getResult();
    }

    public Condition(String condition, HighLevelPetriNet highLevelPetriNet, int index){
        binaryOperator = new BinaryOperator(condition, highLevelPetriNet, index);
    }

}

// type vc cv vv
// binary operator only