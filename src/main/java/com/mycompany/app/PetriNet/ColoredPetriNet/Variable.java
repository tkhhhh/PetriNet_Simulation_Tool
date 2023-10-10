package com.mycompany.app.PetriNet.ColoredPetriNet;


import fr.lip6.move.pnml.hlpn.terms.impl.VariableDeclImpl;
import fr.lip6.move.pnml.hlpn.terms.impl.VariableImpl;

import static java.util.UUID.randomUUID;


// name means color
public class Variable {
    private String name;
    private String id;
    private Constant constant;
    private int from;
    private int to;

    public Variable(VariableImpl variable) {
        name = variable.getVariableDecl().getName();
        id = variable.getVariableDecl().getId();
    }

    public Variable(VariableDeclImpl variableDecl) {
        name = variableDecl.getName();
        id = variableDecl.getId();
    }

    public Variable(int start, int end, String name) {
        this.name = name;
        this.from = start;
        this.to = end;
        this.id = String.valueOf(randomUUID());
    }

    public Variable() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setConstant(Constant constant) {
        this.constant = constant;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Constant getConstant() {
        return constant;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public boolean equal(Variable variable){
        if(variable == null)return false;
        return name.equals(variable.getName()) && id.equals(variable.getId());
    }
}
