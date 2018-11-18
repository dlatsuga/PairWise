package org.pairwise.other;

import org.pairwise.domain.builder.OrderSingleBuilder;

import java.math.BigDecimal;

import static org.pairwise.domain.builder.OrderSingleBuilder.anOrderSingle;

public class Main {
    public static void main(String[] args) {

    /*    Map<Integer, List<Integer>> indexes = new TestDataGenerator()
                .forTemplate(getTemplate())
                .withEnumData("currency", Currency.class)
                .generateUniquePairsIndexes();

        indexes.forEach((k, v) -> System.out.println(k + ":\t" + v));

        System.out.println("***********************************************************");

        Map<Integer, List<Object>> values = new TestDataGenerator()
                .forTemplate(getTemplate())
                .withEnumData("currency", Currency.class)
                .generateUniquePairsValues();

        values.forEach((k, v) -> System.out.println(k + ":\t" + v));*/


      /*  SubSetDescription parameter_A = new SubSetDescription("parameter_A", ParameterA.class);
        SubSetDescription parameter_B = new SubSetDescription("parameter_B", ParameterB.class);
        SubSetDescription parameter_C = new SubSetDescription("parameter_C", ParameterC.class);
        SubSetDescription parameter_D = new SubSetDescription("parameter_D", ParameterD.class);

        SetDescription setDescription = new SetDescription();
        setDescription.addSubSet(parameter_A);
        setDescription.addSubSet(parameter_B);
        setDescription.addSubSet(parameter_C);
        setDescription.addSubSet(parameter_D);*/

/*        SubSetDescription currency = new SubSetDescription("currency", Currency.class);
        SubSetDescription side = new SubSetDescription("side", Side.class);
        SubSetDescription quantity = new SubSetDescription("quantity", Quantity.class);

        SetDescription setDescription = new SetDescription();
        setDescription.addSubSet(currency);
        setDescription.addSubSet(side);
        setDescription.addSubSet(quantity);*/

 /*       Map<Integer, List<Object>> values = new DefaultPairWiseGenerator(setDescription)
                .generatePairsWiseValues();

        values.forEach((k, v) -> System.out.println(k + ":\t" + v));*/




    }

    private static OrderSingleBuilder getTemplate() {
        return anOrderSingle()
                .withQuantity(new BigDecimal("1000"))
                .withPrice(new BigDecimal("20"));
    }
}