package com.mycompany.app.Painter.HighLevelController;

import com.mycompany.app.Painter.ColoredController.ColoredController;
import com.mycompany.app.Painter.Controller;
import com.mycompany.app.PetriNet.ColoredPetriNet.*;
import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelPetriNet;
import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelTransition;

import java.util.ArrayList;
import java.util.HashMap;

public class HighLevelController extends ColoredController {

    private HighLevelTransition currentHighLevelTransition;
    private HighLevelPetriNet highLevelPetriNet;

    public HighLevelController(HighLevelPetriNet highLevelPetriNet) {
        super(highLevelPetriNet);
        this.highLevelPetriNet = highLevelPetriNet;
    }

    public HighLevelPetriNet getHighLevelPetriNet() {
        return highLevelPetriNet;
    }

    @Override
    public void executeTransition(){
        HashMap<String, HighLevelTransition> highLevelTransitionHashMap = highLevelPetriNet.getHighLevelTransitionHashMap();
        for(HighLevelTransition highLevelTransition: highLevelTransitionHashMap.values()){
            currentHighLevelTransition = highLevelTransition;
            /*
            if(condition()){
                updateToken();
            }
             */
            if(condition()){
                updateToken();
            }
        }
    }


    private boolean condition(){
        return true;
    }


    private void updateToken(){

    }

}
