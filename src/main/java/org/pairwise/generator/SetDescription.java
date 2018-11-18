package org.pairwise.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toMap;
import static org.pairwise.generator.pairwise.Constants.INDEX_OF_FIRST_ELEMENT_IN_PAIR;
import static org.pairwise.generator.pairwise.Constants.INDEX_OF_FIRST_SUB_SET_IN_PAIR;
import static org.pairwise.generator.pairwise.Constants.INDEX_OF_SECOND_ELEMENT_IN_PAIR;
import static org.pairwise.generator.pairwise.Constants.INDEX_OF_SECOND_SUB_SET_IN_PAIR;
import static org.pairwise.generator.pairwise.Constants.MIN_REQUIRED_NUMBER_SUBSETS_FOR_PAIR;

public class SetDescription {

    private Integer maxSubSetIndex = 0;
    private List<SubSetDescription> subSets = new ArrayList<>();
    private Map<Integer, Set<Integer>> elementsInSet = new HashMap<>();
    private Map<Integer, List<Integer>> uniquePairsIndexes = new HashMap<>();
    private Map<Integer, List<Object>> uniquePairsValues = new HashMap<>();

    public Integer getMaxSubSetIndex() {
        return maxSubSetIndex;
    }

    public List<SubSetDescription> getSubSets() {
        return subSets;
    }

    public void addSubSet(SubSetDescription subSetDescription) {

        subSetDescription.setSubSetIndex(maxSubSetIndex);
        subSets.add(subSetDescription);

        Set<Integer> elementsIndexesInSubSet = subSetDescription.getElementsInSubSet().keySet();
        elementsInSet.put(maxSubSetIndex, elementsIndexesInSubSet);

        maxSubSetIndex++;
    }

    public Map<Integer, List<Integer>> generateUniquePairsIndexes() {
        int indexOfUniquePair = 0;
        if (maxSubSetIndex < MIN_REQUIRED_NUMBER_SUBSETS_FOR_PAIR) {
            generateUniquePairsForMonoSet(indexOfUniquePair);
        } else {
            generateUniquePairsForRegularSet(indexOfUniquePair);
        }

        return uniquePairsIndexes;
    }

    public Map<Integer, List<Object>> generateUniquePairsValues() {
        generateUniquePairsIndexes();

        for (Entry<Integer, List<Integer>> uniquePairIndexes : uniquePairsIndexes.entrySet()) {
            if (maxSubSetIndex < MIN_REQUIRED_NUMBER_SUBSETS_FOR_PAIR) {
                getValuesForMonoSet(uniquePairIndexes);
            } else {
                getValuesForRegularSet(uniquePairIndexes);
            }
        }

        return uniquePairsValues;
    }

    private void generateUniquePairsForMonoSet(int indexOfUniquePair) {
        for (Entry<Integer, Set<Integer>> elements : elementsInSet.entrySet()) {
            for (Integer value : elements.getValue()) {
                uniquePairsIndexes.put(indexOfUniquePair++, asList(elements.getKey(), value));
            }
        }
    }

    private void generateUniquePairsForRegularSet(int indexOfUniquePair) {
        for (int currentSubSetIndex = 0; currentSubSetIndex < elementsInSet.keySet().size() - 1; currentSubSetIndex++) {

            int currentIndex = currentSubSetIndex;

            Map<Integer, Set<Integer>> firstSubSetElements = elementsInSet.entrySet().stream()
                    .filter(allSubSets -> allSubSets.getKey().equals(currentIndex))
                    .collect(toMap(Entry::getKey, Entry::getValue));

            Map<Integer, Set<Integer>> secondSubSetElements = elementsInSet.entrySet().stream()
                    .filter(allSubSets -> !allSubSets.getKey().equals(currentIndex))
                    .filter(allSubSets -> allSubSets.getKey() > currentIndex)
                    .collect(toMap(Entry::getKey, Entry::getValue));

            for (Integer indexOfCurrentElement : firstSubSetElements.get(currentIndex)) {
                for (Entry<Integer, Set<Integer>> pairSubSets : secondSubSetElements.entrySet()) {
                    int pairSubSetIndex = pairSubSets.getKey();
                    for (Integer indexOfPairElement : pairSubSets.getValue()) {
                        uniquePairsIndexes.put(indexOfUniquePair++, asList(currentSubSetIndex, indexOfCurrentElement, pairSubSetIndex, indexOfPairElement));
                    }
                }
            }
        }

        uniquePairsIndexes.forEach((k, v) -> System.out.println(k + ":\t" + v));
        System.out.println("***********************************************************");
    }

    private void getValuesForMonoSet(Entry<Integer, List<Integer>> uniquePairIndexes) {
        Integer indexOfFirstSubSet = uniquePairIndexes.getValue().get(INDEX_OF_FIRST_SUB_SET_IN_PAIR);
        Integer indexOfFirstElement = uniquePairIndexes.getValue().get(INDEX_OF_FIRST_ELEMENT_IN_PAIR);

        SubSetDescription firstSubSetDescription = subSets.get(indexOfFirstSubSet);
        Object firstElement = firstSubSetDescription.getElementsInSubSet().get(indexOfFirstElement);

        uniquePairsValues.put(uniquePairIndexes.getKey(), singletonList(firstElement));
    }

    private void getValuesForRegularSet(Entry<Integer, List<Integer>> uniquePairIndexes) {
        Integer indexOfFirstSubSet = uniquePairIndexes.getValue().get(INDEX_OF_FIRST_SUB_SET_IN_PAIR);
        Integer indexOfFirstElement = uniquePairIndexes.getValue().get(INDEX_OF_FIRST_ELEMENT_IN_PAIR);

        Integer indexOfSecondSubSet = uniquePairIndexes.getValue().get(INDEX_OF_SECOND_SUB_SET_IN_PAIR);
        Integer indexOfSecondElement = uniquePairIndexes.getValue().get(INDEX_OF_SECOND_ELEMENT_IN_PAIR);

        SubSetDescription firstSubSetDescription = subSets.get(indexOfFirstSubSet);
        SubSetDescription secondSubSetDescription = subSets.get(indexOfSecondSubSet);
        Object firstElement = firstSubSetDescription.getElementsInSubSet().get(indexOfFirstElement);
        Object secondElement = secondSubSetDescription.getElementsInSubSet().get(indexOfSecondElement);

        uniquePairsValues.put(uniquePairIndexes.getKey(), asList(firstElement, secondElement));
    }

    public Map<Integer, List<Integer>> getUniquePairsIndexes() {
        return uniquePairsIndexes;
    }
}