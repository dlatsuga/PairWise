package org.pairwise.domain.template;

import org.pairwise.domain.builder.OrderSingleBuilder;

import java.math.BigDecimal;

import static org.pairwise.domain.builder.OrderSingleBuilder.anOrderSingle;

public class OrderSingleBuilderTemplateRepository {
    public static OrderSingleBuilder templateForTest() {
        return anOrderSingle()
                .withPrice(new BigDecimal("777"));
    }
}
