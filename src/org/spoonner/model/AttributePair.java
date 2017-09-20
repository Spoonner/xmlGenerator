package org.spoonner.model;

public class AttributePair {

    private String name;
    private String value;

    public AttributePair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public AttributePair() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
