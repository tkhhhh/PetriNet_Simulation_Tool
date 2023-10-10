package com.mycompany.app.Parser.HighLevel;

import com.mycompany.app.PetriNet.ColoredPetriNet.Condition;
import com.mycompany.app.PetriNet.ColoredPetriNet.Constant;
import com.mycompany.app.PetriNet.ColoredPetriNet.Variable;
import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelEdge;
import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelPetriNet;
import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelPlace;
import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelTransition;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import static java.util.UUID.randomUUID;

public class AnalysisOfSegment {
    ArrayList<String> statements = new ArrayList<>();
    HighLevelPetriNet highLevelPetriNet = new HighLevelPetriNet();
    HighLevelPlace currentHighLevelPlace = new HighLevelPlace();
    HighLevelTransition currentHighLevelTransition = new HighLevelTransition();
    String currentNode;
    String currentGuard;

    public AnalysisOfSegment(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();

        while (line != null) {
            statements.add(line);
            line = reader.readLine();
        }
        currentNode = "";
        currentHighLevelPlace.setId(String.valueOf(randomUUID()));
        analyseStatement(0, statements.size() - 1);
        PositionGeneration positionGeneration = new PositionGeneration(highLevelPetriNet);
        positionGeneration.positionGenerator();
        highLevelPetriNet = positionGeneration.getHighLevelPetriNet();
    }

    private void analyseStatement(int start, int end) {
        int initialStart = start;
        int initialEnd = end;
        for(int i = start;i <= end;i++){
            String statement = statements.get(i);
            if (statement.startsWith("var ")){
                analyseDeclareStatement(start, end, statement, i);
            }
            else if(statement.startsWith("if ")){
                start = i;
                i++;
                while(!statements.get(i).equals("fi")){
                    i++;
                }
                end = i;
                analyseIfStatement(start, end);
            } else if (statement.startsWith("do ")) {
                start = i;
                i++;
                while(!statements.get(i).equals("od")){
                    i++;
                }
                end = i;
                analyseRepeatStatement(start, end);
            } else {
                analyseAssignStatement(i, statement);
            }
            start = initialStart;
            end = initialEnd;
        }
    }

    // if var not an entry place
    private void analyseDeclareStatement(int start, int end, String statement, int index) {
        String variableName = statement.replace("var ", "");
        variableName = variableName.replace(";", "");
        Variable variable = new Variable(start, end, variableName);
        HashMap<String, Variable> variableHashMap = highLevelPetriNet.getVariables();
        variableHashMap.put(variable.getId(), variable);
        highLevelPetriNet.setVariables(variableHashMap);

        if(!currentNode.equals("place")){
            currentHighLevelPlace = new HighLevelPlace();
            currentHighLevelPlace.setId(String.valueOf(randomUUID()));
            currentNode = "place";
        }

        currentHighLevelPlace.setLabel(currentHighLevelPlace.getLabel() + " " + statement);
        ArrayList<Variable> variables = currentHighLevelPlace.getCurrentVariables();
        variables.add(variable);
        currentHighLevelPlace.setCurrentVariables(variables);

        HashMap<String, HighLevelPlace> highLevelPlaceHashMap = highLevelPetriNet.getHighLevelPlaceHashMap();
        highLevelPlaceHashMap.put(currentHighLevelPlace.getId(), currentHighLevelPlace);
        highLevelPetriNet.setHighLevelPlaceHashMap(highLevelPlaceHashMap);

        if(index == 0){
            highLevelPetriNet.setEntryPlace(currentHighLevelPlace);
        }
    }

    private void analyseIfStatement(int start, int end){
        /*
        HashMap<String, HighLevelPlace> highLevelPlaceHashMap = highLevelPetriNet.getHighLevelPlaceHashMap();
        highLevelPlaceHashMap.put(currentHighLevelPlace.getId(), currentHighLevelPlace);
        highLevelPetriNet.setHighLevelPlaceHashMap(highLevelPlaceHashMap);

         */

        currentHighLevelTransition = new HighLevelTransition();
        currentHighLevelTransition.setId(String.valueOf(randomUUID()));
        currentNode = "transition";

        String startString = statements.get(start);
        startString = startString.replace("if ","");
        String[] commandGuard = startString.split("->");
        currentGuard = commandGuard[0];
        String command = commandGuard[1];

        HighLevelEdge highLevelEdge = new HighLevelEdge();
        highLevelEdge.setId(String.valueOf(randomUUID()));
        highLevelEdge.setTarget(currentHighLevelTransition.getId());
        highLevelEdge.setSource(currentHighLevelPlace.getId());

        ArrayList<HighLevelEdge> outcomes = currentHighLevelPlace.getOutcomeEdges();
        outcomes.add(highLevelEdge);
        currentHighLevelPlace.setOutcomes(outcomes);
        HashMap<String, HighLevelPlace> highLevelPlaceHashMap = highLevelPetriNet.getHighLevelPlaceHashMap();
        highLevelPlaceHashMap.put(currentHighLevelPlace.getId(), currentHighLevelPlace);

        ArrayList<HighLevelEdge> incomes = currentHighLevelTransition.getInHighLevelEdges();
        incomes.add(highLevelEdge);
        currentHighLevelTransition.setInHighLevelEdges(incomes);

        ArrayList<HighLevelEdge> highLevelEdges = highLevelPetriNet.getHighLevelEdges();
        highLevelEdges.add(highLevelEdge);
        highLevelPetriNet.setHighLevelEdges(highLevelEdges);

        String guard = currentGuard.replaceAll(" ","");
        Condition condition = new Condition(guard, highLevelPetriNet, start);
        ArrayList<Condition> conditions = currentHighLevelTransition.getConditions();
        conditions.add(condition);
        currentHighLevelTransition.setConditions(conditions);

        currentNode = "transition";
        analyseAssignStatement(start, command);

        int index = start + 1;
        while(index < end){
            startString = statements.get(index);
            startString = startString.replace("[] ","");
            commandGuard = startString.split("->");
            currentGuard = commandGuard[0];
            command = commandGuard[1];

            guard = currentGuard.replaceAll(" ","");
            condition = new Condition(guard, highLevelPetriNet, index);
            conditions = currentHighLevelTransition.getConditions();
            conditions.add(condition);
            currentHighLevelTransition.setConditions(conditions);

            currentNode = "transition";
            analyseAssignStatement(index, command); // assign value
            // append
            index++;
        }

    }


    // still in progress
    private void analyseRepeatStatement(int start, int end){

    }

    private void analyseAssignStatement(int index, String statement){
        //Variable variable = new Variable();
        // empty variable

        String statementWithoutSplit = statement.replace(";","");
        String variableString = statementWithoutSplit.split(":=")[0];
        String constantString = statementWithoutSplit.split(":=")[1];
        variableString = variableString.replaceAll(" ", "");
        constantString = constantString.replaceAll(" ", "");
        Constant constant = new Constant("int", constantString);

        /*
        HashMap<String, Variable> variableHashMap = highLevelPetriNet.getVariables();
        for(Variable variableExample: variableHashMap.values()){
            if(variableExample.getName().equals(variableString) && index <= variableExample.getTo() && index >= variableExample.getFrom()){
                variableExample.setConstant(constant);
                variableHashMap.put(variableExample.getId(), variableExample);
                variable = variableExample;
            }
        }
        highLevelPetriNet.setVariables(variableHashMap);
         */

        if (!currentNode.equals("place")){
            HashMap<String, HighLevelTransition> highLevelTransitionHashMap = highLevelPetriNet.getHighLevelTransitionHashMap();
            highLevelTransitionHashMap.put(currentHighLevelTransition.getId(), currentHighLevelTransition);
            highLevelPetriNet.setHighLevelTransitionHashMap(highLevelTransitionHashMap);

            currentHighLevelPlace = new HighLevelPlace();
            currentHighLevelPlace.setId(String.valueOf(randomUUID()));
            currentNode = "place";

            HighLevelEdge highLevelEdge = new HighLevelEdge();
            highLevelEdge.setId(String.valueOf(randomUUID()));
            highLevelEdge.setSource(currentHighLevelTransition.getId());
            highLevelEdge.setTarget(currentHighLevelPlace.getId());
            highLevelEdge.setHighCondition(currentGuard);

            ArrayList<HighLevelEdge> outcomes = currentHighLevelTransition.getOutHighLevelEdges();
            outcomes.add(highLevelEdge);
            currentHighLevelTransition.setOutHighLevelEdges(outcomes);
            highLevelTransitionHashMap.put(currentHighLevelTransition.getId(), currentHighLevelTransition);
            highLevelPetriNet.setHighLevelTransitionHashMap(highLevelTransitionHashMap);


            ArrayList<HighLevelEdge> highLevelEdges = highLevelPetriNet.getHighLevelEdges();
            highLevelEdges.add(highLevelEdge);
            highLevelPetriNet.setHighLevelEdges(highLevelEdges);
        }
        ArrayList<Variable> variables = currentHighLevelPlace.getCurrentVariables();

        boolean isExist = false;
        for(Variable variable1: variables){
            if(variable1.getName().equals(variableString) && index <= variable1.getTo() && index >= variable1.getFrom()){
                isExist = true;
                variable1.setConstant(constant);
            }
        }
        if (!isExist){
            HashMap<String, Variable> variableHashMap = highLevelPetriNet.getVariables();
            for(Variable variableExample: variableHashMap.values()){
                if(variableExample.getName().equals(variableString) && index <= variableExample.getTo() && index >= variableExample.getFrom()){
                    Variable variable = new Variable(variableExample.getFrom(), variableExample.getTo(), variableString);
                    variable.setId(variableExample.getId());
                    variable.setConstant(constant);
                    variables.add(variable);
                }
            }
        }
        currentHighLevelPlace.setCurrentVariables(variables);
        currentHighLevelPlace.setLabel(currentHighLevelPlace.getLabel() + " " + statement);

        HashMap<String, HighLevelPlace> highLevelPlaceHashMap = highLevelPetriNet.getHighLevelPlaceHashMap();
        highLevelPlaceHashMap.put(currentHighLevelPlace.getId(), currentHighLevelPlace);
        highLevelPetriNet.setHighLevelPlaceHashMap(highLevelPlaceHashMap);
    }
    // only support int now


    public HighLevelPetriNet getHighLevelPetriNet() {
        return highLevelPetriNet;
    }

    public ArrayList<String> getStatements() {
        return statements;
    }
}
