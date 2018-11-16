package org.pairwise.domain.entity;

public enum ParameterC {
    C_1("C_1"),
    C_2("C_2");

    private String value;

    ParameterC(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}