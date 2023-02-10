package com.azyf.finalyearproject;

public class Variable {
    private int value;
    private String content;
    private String name;
    private VariableType type;

    public Variable(int value, String name, VariableType type) {
        this.value = value;
        this.name = name;
        this.type = type;
    }

    public Variable(String content, String name, VariableType type) {
        this.content = content;
        this.name = name;
        this.type = type;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VariableType getType(){
        return  type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
