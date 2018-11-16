package org.pairwise.generator;

import java.util.HashMap;
import java.util.Map;

public class TestDataGenerator {
    private Object template;
    private Map<String, Class> enumDescription = new HashMap<>();

    public TestDataGenerator forTemplate(Object template) {
        this.template = template;
        return this;
    }

    public TestDataGenerator withEnumData(String field, Class enumData) {
        enumDescription.put(field, enumData);
        return this;
    }

    public Object[][] generateTestData() {

        SetDescription setDescription = createSetOfElements();

        return null;
    }

    private SetDescription createSetOfElements() {
        SetDescription setDescription = new SetDescription();
        for (Map.Entry<String, Class> enumEntry : enumDescription.entrySet()) {
//            setDescription

//            new org.pairwise.generator.SubSetDescription(enumEntry.getKey(), enumEntry.getValue());

        }
        return null;
    }
}