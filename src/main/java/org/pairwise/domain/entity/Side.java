package org.pairwise.domain.entity;

public enum Side {
    BUY("Buy"),
    SELL("Sell");

    private String value;

    Side(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}