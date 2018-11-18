package org.pairwise;

import org.pairwise.domain.builder.OrderSingleBuilder;
import org.pairwise.domain.entity.Currency;
import org.pairwise.generator.TestDataGenerator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.pairwise.domain.builder.OrderSingleBuilder.anOrderSingle;

public class Main {
    public static void main(String[] args) {

        Map<Integer, List<Integer>> indexes = new TestDataGenerator()
                .forTemplate(getTemplate())
                .withEnumData("currency", Currency.class)
                .generateUniquePairsIndexes();

        indexes.forEach((k, v) -> System.out.println(k + ":\t" + v));

        System.out.println("***********************************************************");

        Map<Integer, List<Object>> values = new TestDataGenerator()
                .forTemplate(getTemplate())
                .withEnumData("currency", Currency.class)
                .generateUniquePairsValues();

        values.forEach((k, v) -> System.out.println(k + ":\t" + v));

    }

    private static OrderSingleBuilder getTemplate() {
        return anOrderSingle()
                .withQuantity(new BigDecimal("1000"))
                .withPrice(new BigDecimal("20"));
    }
}