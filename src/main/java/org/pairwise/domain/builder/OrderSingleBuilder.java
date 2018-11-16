package org.pairwise.domain.builder;

import org.pairwise.domain.entity.Currency;
import org.pairwise.domain.entity.OrderSingle;
import org.pairwise.domain.entity.Side;

import java.math.BigDecimal;

public final class OrderSingleBuilder {
    private Currency currency;
    private Side side;
    private BigDecimal quantity;
    private BigDecimal price;

    private OrderSingleBuilder() {
    }

    public static OrderSingleBuilder anOrderSingle() {
        return new OrderSingleBuilder();
    }

    public OrderSingleBuilder withCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public OrderSingleBuilder withSide(Side side) {
        this.side = side;
        return this;
    }

    public OrderSingleBuilder withQuantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderSingleBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public OrderSingle build() {
        OrderSingle orderSingle = new OrderSingle();
        orderSingle.setCurrency(currency);
        orderSingle.setSide(side);
        orderSingle.setQuantity(quantity);
        orderSingle.setPrice(price);
        return orderSingle;
    }
}
