package org.pairwise.domain.entity;

import java.math.BigDecimal;

public enum Quantity {
    //    NEGATIVE(new BigDecimal("-1")),
    ZERO(new BigDecimal("0")),
    _1K(new BigDecimal("1000"));

    private BigDecimal value;

    Quantity(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}