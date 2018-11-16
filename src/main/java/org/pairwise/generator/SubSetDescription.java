package org.pairwise.generator;

import java.util.HashMap;
import java.util.Map;

public class SubSetDescription {
    private int subSetIndex = -1;
    private String fieldToInject;
    private Class enumClass;
    private Map<Integer, Object> elementsInSubSet = new HashMap<>();

    public SubSetDescription(String fieldToInject, Class enumClass) {
        this.fieldToInject = fieldToInject;
        this.enumClass = enumClass;
        int elementIndexInSubSet = 0;
        for (Object enumValue : enumClass.getEnumConstants()) {
            elementsInSubSet.put(elementIndexInSubSet++, enumValue);
        }
    }

    public int getSubSetIndex() {
        return subSetIndex;
    }

    public void setSubSetIndex(int subSetIndex) {
        this.subSetIndex = subSetIndex;
    }

    public String getFieldToInject() {
        return fieldToInject;
    }

    public Class getEnumClass() {
        return enumClass;
    }

    public Map<Integer, Object> getElementsInSubSet() {
        return elementsInSubSet;
    }
}