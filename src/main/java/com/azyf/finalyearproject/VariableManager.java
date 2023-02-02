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
}
