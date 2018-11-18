package org.pairwise.other;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Enum.valueOf;

public class TestDataGenerator_Old {
    private Object template;
    private Map<String, Class> enumDescription = new HashMap<>();

    public TestDataGenerator_Old forTemplate(Object template) {
        this.template = template;
        return this;
    }

    public TestDataGenerator_Old withEnumData(String field, Class enumData) {
        enumDescription.put(field, enumData);
        return this;
    }

    public Object[][] generateTestData() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {

        Class enumToInjectClass = enumDescription.get("currency");
        Object[] enumConstants = enumToInjectClass.getEnumConstants();
        int countOfEnumValues = enumConstants.length;
        Object[][] generatedTestData = new Object[countOfEnumValues][1];
        for (int i = 0; i < countOfEnumValues; i++) {
            Method buildMethod = template.getClass().getMethod("build");
            Object mainClass = buildMethod.invoke(template);
            Object enumToInjectInstance = valueOf(enumToInjectClass, enumConstants[i].toString());
            Field field = mainClass.getClass().getDeclaredField(Introspector
                    .decapitalize(enumToInjectClass.getSimpleName()));
            field.setAccessible(true);
            field.set(mainClass, enumToInjectInstance);
            generatedTestData[i][0] = mainClass;
        }
        return generatedTestData;
    }
}