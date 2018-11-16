package org.pairwise.domain.entity;

public enum ParameterA {
    A_1("A_1"),
    A_2("A_2");

    private String value;

    ParameterA(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}