package org.pairwise.other;

import org.pairwise.domain.entity.OrderSingle;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.pairwise.domain.builder.OrderSingleBuilder.anOrderSingle;
import static org.pairwise.domain.entity.Currency.CHF;
import static org.pairwise.domain.entity.Currency.USD;
import static org.pairwise.matcher.OrderSingleMatcher.quantityGreaterThan;

public class TestMain {
    private OrderSingle orderSingle;

    @DataProvider(name = "dtls")
    public static Object[][] dataProvider() {
        return new Object[][]{
                {anOrderSingle()
                        .withCurrency(CHF)
                        .withQuantity(new BigDecimal("1000"))
                        .withPrice(new BigDecimal("20"))
                        .build()
                },
                {anOrderSingle()
                        .withCurrency(USD)
                        .withQuantity(new BigDecimal("2000"))
                        .withPrice(new BigDecimal("30"))
                        .build()
                }
        };
    }

    @Factory(dataProvider = "dtls")
    public TestMain(OrderSingle orderSingle) {
        this.orderSingle = orderSingle;
    }

    @Test
    public void test1() {
        assertThat(orderSingle, quantityGreaterThan(new BigDecimal("1000")));
    }
}