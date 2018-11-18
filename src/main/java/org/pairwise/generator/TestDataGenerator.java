package org.pairwise.generator;

import org.pairwise.generator.pairwise.SetDescription;
import org.pairwise.generator.pairwise.SubSetDescription;
import org.pairwise.generator.pairwise.impl.DefaultPairWiseGenerator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Enum.valueOf;

public class TestDataGenerator {
    private Object template;
    private SetDescription setDescription = new SetDescription();

    private Map<Integer, List<Object>> pairsWiseValues;
    private Map<Integer, Object> testScenario = new HashMap<>();

    public TestDataGenerator forTemplate(Object template) {
        this.template = template;
        return this;
    }

    public TestDataGenerator withEnumData(String fieldToInject, Class enumData) {
        SubSetDescription subSetDescription = new SubSetDescription(fieldToInject, enumData);
        setDescription.addSubSet(subSetDescription);
        return this;
    }

    public Map<Integer, Object> generateTestData() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {

        pairsWiseValues = generatePairsWiseValues();

        pairsWiseValues.forEach((k, v) -> System.out.println(k + ":\t" + v));
        System.out.println("***********************************************************");

        List<SubSetDescription> subSets = setDescription.getSubSets();

        Method buildMethod = template.getClass().getMethod("build");
        for (Integer tupleIndex : pairsWiseValues.keySet()) {
            Object mainClass = buildMethod.invoke(template);
            List<Object> objects = pairsWiseValues.get(tupleIndex);
            for (int indexOfEnum = 0; indexOfEnum < objects.size(); indexOfEnum++) {
                String fieldName = subSets.get(indexOfEnum).getFieldToInject();
                Field field = mainClass.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object enumToInjectInstance;
                Class enumToInjectClass = subSets.get(indexOfEnum).getEnumClass();
                if (fieldName.equals("quantity")) {
                    Method method = enumToInjectClass.getDeclaredMethod("getValue");
                    BigDecimal quantity = (BigDecimal) method.invoke(objects.get(indexOfEnum));
                    field.set(mainClass, quantity);
                } else {
                    enumToInjectInstance = valueOf(enumToInjectClass, objects.get(indexOfEnum).toString());
                    field.set(mainClass, enumToInjectInstance);
                }
            }
            testScenario.put(tupleIndex, mainClass);
        }
        return testScenario;
    }

    private Map<Integer, List<Object>> generatePairsWiseValues() {
        return new DefaultPairWiseGenerator(setDescription)
                .generatePairsWiseValues();
    }
}