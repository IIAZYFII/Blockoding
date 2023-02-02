package com.azyf.finalyearproject;


import java.util.ArrayList;

public class VariableManager {
    private ArrayList<Variable> variables;

    public VariableManager() {
        variables = new ArrayList<>();
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public void addVariable(Variable variable) {
        variables.add(variable);
    }


    public String[] getVariableNamesAsArray() {
        String[] variableNames = new String[this.variables.size()];
        for (int i = 0; i < this.variables.size(); i++) {
            variableNames[i] = this.variables.get(i).getName();
        }
        return variableNames;
    }
}


