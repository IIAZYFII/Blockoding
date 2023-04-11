package com.azyf.finalyearproject;


import java.util.ArrayList;

public class VariableManager {
    private ArrayList<Integer> initialValues;
    private ArrayList<Variable> variables;

    public VariableManager() {
        variables = new ArrayList<>();
        initialValues = new ArrayList<>();
    }



    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public void deleteVariable(String variableName) {
        for(int i = 0; i < variables.size(); i++) {
            if(variables.get(i).getName().equals(variableName)) {
                variables.remove(i);
                break;
            }
        }
    }

    public boolean addVariable(Variable variable) {
        boolean sameName = false;
        for(int i = 0; i < variables.size(); i++) {
            if(variable.getName().equals(variables.get(i).getName())) {
                sameName = true;
            }
        }
        if(sameName == false) {
            variables.add(variable);
            initialValues.add(variable.getValue());
        }
        return  sameName;
    }


    public String[] getVariableNamesAsArray() {
        String[] variableNames = new String[this.variables.size()];
        for (int i = 0; i < this.variables.size(); i++) {
            variableNames[i] = this.variables.get(i).getName();
        }
        return variableNames;
    }

    public void resetToInitialValues() {
        for(int i =0; i < variables.size(); i++) {
           Variable tmpVariable = variables.get(i);
           tmpVariable.setValue(initialValues.get(i));
           variables.set(i, tmpVariable);
        }
    }
}


