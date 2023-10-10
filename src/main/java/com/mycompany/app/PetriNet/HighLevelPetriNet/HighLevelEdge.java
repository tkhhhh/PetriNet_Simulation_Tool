package com.mycompany.app.PetriNet.HighLevelPetriNet;

import com.mycompany.app.PetriNet.ColoredPetriNet.ColoredEdge;
import com.mycompany.app.PetriNet.ColoredPetriNet.Constant;
import com.mycompany.app.PetriNet.ColoredPetriNet.Variable;
import fr.lip6.move.pnml.hlpn.hlcorestructure.impl.ArcImpl;

import java.util.ArrayList;

public class HighLevelEdge extends ColoredEdge {
    public HighLevelEdge(ArcImpl arc){
        super(arc);
    }

    private String condition = "";

    public HighLevelEdge(){}

    public String getHighCondition(){
        return condition;
    }

    public void setHighCondition(String condition){
        this.condition = condition;
    }

    // number of tokens moved
    @Override
    public String covertCondition(){
        return condition;
    }

    public ArrayList<Constant> getMoveConstants() {
        return moveConstants;
    }

    public ArrayList<Variable> getMoveVariables() {
        return moveVariables;
    }
}
