package org.pairwise.other;

import org.pairwise.domain.builder.OrderSingleBuilder;
import org.pairwise.domain.entity.Currency;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import static org.pairwise.domain.builder.OrderSingleBuilder.anOrderSingle;

public class MainWithTemplate {
    public static void main(String[] args) throws NoSuchFieldException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Object[][] data = new TestDataGenerator_Old()
                .forTemplate(getTemplate())
                .withEnumData("currency", Currency.class)
                .generateTestData();

        for (Object[] dt : data) {
            for (Object o : dt) {
                System.out.println(o);
            }
        }

    }

    private static OrderSingleBuilder getTemplate() {
        return anOrderSingle()
                .withQuantity(new BigDecimal("1000"))
                .withPrice(new BigDecimal("20"));
    }
}
