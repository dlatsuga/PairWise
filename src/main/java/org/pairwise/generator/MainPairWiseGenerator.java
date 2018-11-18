package org.pairwise.generator;

import org.pairwise.domain.entity.Currency;
import org.pairwise.domain.entity.Quantity;
import org.pairwise.domain.entity.Side;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static org.pairwise.domain.template.OrderSingleBuilderTemplateRepository.templateForTest;

public class MainPairWiseGenerator {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {

        Map<Integer, Object> values = new TestDataGenerator()
                .forTemplate(templateForTest())
                .withEnumData("currency", Currency.class)
                .withEnumData("side", Side.class)
                .withEnumData("quantity", Quantity.class)
                .generateTestData();

        values.forEach((k, v) -> System.out.println(k + ":\t" + v));
    }
}
