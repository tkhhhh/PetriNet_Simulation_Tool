package com.mycompany.app.PetriNet.ColoredPetriNet;

import com.mycompany.app.PetriNet.HighLevelPetriNet.HighLevelPetriNet;
import fr.lip6.move.pnml.hlpn.booleans.impl.EqualityImpl;
import fr.lip6.move.pnml.hlpn.integers.impl.GreaterThanImpl;
import fr.lip6.move.pnml.hlpn.integers.impl.LessThanImpl;
import fr.lip6.move.pnml.hlpn.integers.impl.NumberConstantImpl;
import fr.lip6.move.pnml.hlpn.terms.Term;
import fr.lip6.move.pnml.hlpn.terms.impl.VariableImpl;

// only supports int currently
public class BinaryOperator {
    private Variable firstVariable;
    private Constant firstConstant;
    private Variable secondVariable;
    private Constant secondConstant;
    private Object result;
    private String operator;

    public BinaryOperator(Term term){
        Term firstElement;
        Term secondElement;
        if(term.getClass() == GreaterThanImpl.class) {
            GreaterThanImpl greaterThan = (GreaterThanImpl) term;
            firstElement  = greaterThan.getSubterm().get(0);
            secondElement = greaterThan.getSubterm().get(1);
            operator = "gt";
        }
        else if(term.getClass() == LessThanImpl.class) {
            LessThanImpl lessThan = (LessThanImpl) term;
            firstElement = lessThan.getSubterm().get(0);
            secondElement = lessThan.getSubterm().get(1);
            operator = "lt";
        }
        else {
            EqualityImpl equality = (EqualityImpl) term;
            firstElement = equality.getSubterm().get(0);
            secondElement = equality.getSubterm().get(1);
            operator = "eq";
        }
        if(firstElement.getClass() == NumberConstantImpl.class) {
            firstConstant = new Constant(firstElement);
        }
        if(firstElement.getClass() == VariableImpl.class) {
            firstVariable = new Variable((VariableImpl)firstElement);
        }
        if(secondElement.getClass() == NumberConstantImpl.class) {
            secondConstant = new Constant(secondElement);
        }
        if(secondElement.getClass() == VariableImpl.class) {
            secondVariable = new Variable((VariableImpl)secondElement);
        }
    }

    public BinaryOperator(String condition, HighLevelPetriNet highLevelPetriNet, int index){
        String variableString = "", constantString = "";
        if(condition.contains("<")){
            operator = "lt";
            variableString = condition.split("<")[0];
            constantString = condition.split("<")[1];
        }
        if(condition.contains(">")){
            operator = "gt";
            variableString = condition.split(">")[0];
            constantString = condition.split(">")[1];
        }
        if(condition.contains("==")){
            operator = "eq";
            variableString = condition.split("==")[0];
            constantString = condition.split("==")[1];
        }
        for(Variable variable: highLevelPetriNet.getVariables().values()){
            if(variable.getName().equals(variableString) && (index >= variable.getFrom() && index <= variable.getTo())){
                firstVariable = variable;
            }
        }
        secondConstant = new Constant("int", constantString);
    }

    public void invoke(ColoredPetriNet coloredPetriNet){
        if(firstConstant == null){
            String id1 = firstVariable.getId();
            firstVariable = coloredPetriNet.getVariables().get(id1);
            if(secondConstant == null){
                String id2 = secondVariable.getId();
                secondVariable = coloredPetriNet.getVariables().get(id2);
                calculate(firstVariable.getConstant().getValue(), secondVariable.getConstant().getValue());
            }else{
                calculate(firstVariable.getConstant().getValue(), secondConstant.getValue());
            }
        }else{
            String id2 = secondVariable.getId();
            secondVariable = coloredPetriNet.getVariables().get(id2);
            calculate(firstConstant.getValue(), secondVariable.getConstant().getValue());
        }
    }

    private void calculate(String value1, String value2){
        if(operator.equals("gt")) result = (Integer.parseInt(value1) > Integer.parseInt(value2));
        if(operator.equals("lt")) result = (Integer.parseInt(value1) < Integer.parseInt(value2));
        if(operator.equals("eq")) result = (Integer.parseInt(value1) == Integer.parseInt(value2));
    }

    public Object getResult() {
        return result;
    }
}
