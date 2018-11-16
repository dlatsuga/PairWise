package org.pairwise.domain.entity;

public enum Currency {
    CHF("CHF"),
    USD("USD"),
    UAH("UAH"),
    PLN("PLN"),
    EUR("EUR");

    private String value;

    Currency(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}