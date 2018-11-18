package org.pairwise.domain.entity;

public enum ParameterD {
    D_1("D_1"),
    D_2("D_2"),
    D_3("D_3");

    private String value;

    ParameterD(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}