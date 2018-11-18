package org.pairwise.generator;

import java.util.List;
import java.util.Map;

public class TestDataGenerator {
    private Object template;
    private SetDescription setDescription = new SetDescription();

    public TestDataGenerator forTemplate(Object template) {
        this.template = template;
        return this;
    }

    public TestDataGenerator withEnumData(String fieldToInject, Class enumData) {
        SubSetDescription subSetDescription = new SubSetDescription(fieldToInject, enumData);
        setDescription.addSubSet(subSetDescription);
        return this;
    }

    public Map<Integer, List<Integer>> generateUniquePairsIndexes() {
//        setDescription.generateUniquePairsIndexes();
        return setDescription.generateUniquePairsIndexes();
    }

    public Map<Integer, List<Object>> generateUniquePairsValues() {
//        setDescription.generateUniquePairsIndexes();
        return setDescription.generateUniquePairsValues();
    }
}