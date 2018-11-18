package org.pairwise.domain.entity;

public enum ParameterB {
    B_1("B_1"),
    B_2("B_2"),
    B_3("B_3"),
    B_4("B_4");

    private String value;

    ParameterB(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}