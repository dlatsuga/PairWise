package org.pairwise.domain.entity;

import java.math.BigDecimal;

public class OrderSingle {
    private Currency currency;
    private Side side;
    private BigDecimal quantity;
    private BigDecimal price;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderSingle{" +
                "currency=" + currency +
                ", side=" + side +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}