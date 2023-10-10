package com.mycompany.app.PetriNet.ColoredPetriNet;

import fr.lip6.move.pnml.hlpn.integers.impl.NumberConstantImpl;
import fr.lip6.move.pnml.hlpn.terms.Term;

import java.util.Objects;

public class Constant {
    private String type;
    private String value;
    private String color;

    public Constant(Term term){
        if(term.getClass() == NumberConstantImpl.class) {
            NumberConstantImpl constant = (NumberConstantImpl) term;
            value = constant.getValue().toString();
            this.type = "number";
        }
    }

    public Constant(String type, String value){
        this.type = type;
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
