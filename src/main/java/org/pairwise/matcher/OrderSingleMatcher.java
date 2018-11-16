package org.pairwise.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.pairwise.domain.entity.OrderSingle;

import java.math.BigDecimal;

public class OrderSingleMatcher {
    public static Matcher<OrderSingle> quantityGreaterThan(BigDecimal expectedQuantity) {

        return new TypeSafeMatcher<OrderSingle>() {
            @Override
            protected boolean matchesSafely(OrderSingle orderSingle) {
                return (orderSingle.getQuantity().compareTo(expectedQuantity) >= 0)
                        ? Boolean.TRUE
                        : Boolean.FALSE;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Quantity is smaller than 1000");
            }
        };
    }
}
