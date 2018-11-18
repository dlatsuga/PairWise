package org.pairwise.generator.pairwise.impl;

import org.pairwise.generator.SetDescription;
import org.pairwise.generator.SubSetDescription;
import org.pairwise.generator.pairwise.PairWiseDuplicateChecker;
import org.pairwise.generator.pairwise.PairWiseGenerator;
import org.pairwise.generator.pairwise.PairWiseMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.pairwise.generator.pairwise.Constants.INDEX_OF_FIRST_ELEMENT_IN_PAIR;
import static org.pairwise.generator.pairwise.Constants.INDEX_OF_FIRST_SUB_SET_IN_PAIR;
import static org.pairwise.generator.pairwise.Constants.INDEX_OF_SECOND_ELEMENT_IN_PAIR;
import static org.pairwise.generator.pairwise.Constants.INDEX_OF_SECOND_SUB_SET_IN_PAIR;
import static org.pairwise.generator.pairwise.Constants.MIN_REQUIRED_NUMBER_SUBSETS_FOR_PAIR;

public class DefaultPairWiseGenerator implements PairWiseGenerator {

    private Integer maxSubSetIndex;
    private List<SubSetDescription> subSets;
    private Map<Integer, List<Integer>> uniquePairsIndexes;

    private Map<Integer, Boolean> freePairs;
    private Map<Integer, List<Integer>> pairsWiseIndexes;
    private Map<Integer, List<Object>> pairsWiseValues;
    private PairWiseDuplicateChecker duplicateChecker;
    private PairWiseMatcher pairWiseMatcher;

    public DefaultPairWiseGenerator(SetDescription setDescription) {
        this.maxSubSetIndex = setDescription.getMaxSubSetIndex();
        this.subSets = setDescription.getSubSets();
        this.uniquePairsIndexes = setDescription.generateUniquePairsIndexes();

        this.freePairs = new HashMap<>();
        this.pairsWiseIndexes = new HashMap<>();
        this.pairsWiseValues = new HashMap<>();
        this.duplicateChecker = new DefaultPairWiseDuplicateChecker(this.pairsWiseIndexes);
        this.pairWiseMatcher = new DefaultPairWiseMatcher(setDescription, this.duplicateChecker, this.freePairs);
    }

    @Override
    public Map<Integer, List<Integer>> generatePairWiseIndexes() {
        if (maxSubSetIndex <= MIN_REQUIRED_NUMBER_SUBSETS_FOR_PAIR) {
            generatePairWiseIndexesForMonoSet();
        } else {
            generatePairWiseIndexesForRegularSet();
        }
        return pairsWiseIndexes;
    }

    @Override
    public Map<Integer, List<Object>> generatePairsWiseValues() {
        Map<Integer, List<Integer>> indexes = generatePairWiseIndexes();
        indexes.forEach((k, v) -> System.out.println(k + ":\t" + v));
        System.out.println("***********************************************************");

        for (Map.Entry<Integer, List<Integer>> pairWiseIndexes : pairsWiseIndexes.entrySet()) {
            List<Object> values = new ArrayList<>();
            List<Integer> valuesIndexes = pairWiseIndexes.getValue();
            for (int i = 0; i < valuesIndexes.size(); i++) {
                Integer indexOfElement = valuesIndexes.get(i);
                values.add(subSets.get(i).getElementsInSubSet().get(indexOfElement));
            }
            pairsWiseValues.put(pairWiseIndexes.getKey(), values);
        }
        return pairsWiseValues;
    }

    private void initFreePairs() {
        for (Integer indexOfUniquePair : uniquePairsIndexes.keySet()) {
            freePairs.put(indexOfUniquePair, TRUE);
        }
    }

    private Integer getFreeIndex() {
        for (Map.Entry<Integer, Boolean> candidateForFreeIndex : freePairs.entrySet()) {
            if (candidateForFreeIndex.getValue()) {
                return candidateForFreeIndex.getKey();
            }
        }
        return -1;
    }

    private void generatePairWiseIndexesForMonoSet() {
        for (Map.Entry<Integer, List<Integer>> uniquePair : uniquePairsIndexes.entrySet()) {
            List<Integer> values = uniquePair.getValue();
            pairsWiseIndexes.put(uniquePair.getKey(), range(0, values.size()).filter(index -> index % 2 != 0).mapToObj(values::get).collect(toList()));
        }
    }

    private void generatePairWiseIndexesForRegularSet() {

        initFreePairs();

        Integer indexOfPairWiseTuple = 0;
        Integer indexOfCurrentCandidateForPairWiseTuple = getFreeIndex();

        while ((indexOfCurrentCandidateForPairWiseTuple < freePairs.keySet().size()) && (indexOfCurrentCandidateForPairWiseTuple >= 0)) {

            List<Integer> initialCandidatePairForTuple = uniquePairsIndexes.get(indexOfCurrentCandidateForPairWiseTuple);

            if (duplicateChecker.pairCandidateAlreadyExistInPairWiseMap(initialCandidatePairForTuple)) {

                freePairs.put(indexOfCurrentCandidateForPairWiseTuple, FALSE);
                indexOfCurrentCandidateForPairWiseTuple = getFreeIndex();
                continue;
            }

            Map<Integer, List<Integer>> freeMatchedPairs = pairWiseMatcher.getFreeMatchedPairsToCompleteTuple(indexOfCurrentCandidateForPairWiseTuple);

            if (freeMatchedPairs.keySet().size() > 0) {

                Integer[] pairWiseTuple = new Integer[maxSubSetIndex];
                addPairToTuple(initialCandidatePairForTuple, pairWiseTuple);
                freePairs.put(indexOfCurrentCandidateForPairWiseTuple, FALSE);

                for (Integer indexOfPairToCompleteTuple : freeMatchedPairs.keySet()) {
                    List<Integer> pairToCompleteTuple = freeMatchedPairs.get(indexOfPairToCompleteTuple);
                    addPairToTuple(pairToCompleteTuple, pairWiseTuple);
                    freePairs.put(indexOfPairToCompleteTuple, FALSE);
                }

                pairsWiseIndexes.put(indexOfPairWiseTuple++, asList(pairWiseTuple));
            }
            indexOfCurrentCandidateForPairWiseTuple = getFreeIndex();
        }
    }

    private void addPairToTuple(List<Integer> pair, Integer[] tuple) {
        Integer firstSubSet = pair.get(INDEX_OF_FIRST_SUB_SET_IN_PAIR);
        Integer firstElement = pair.get(INDEX_OF_FIRST_ELEMENT_IN_PAIR);
        Integer secondSubSet = pair.get(INDEX_OF_SECOND_SUB_SET_IN_PAIR);
        Integer secondElement = pair.get(INDEX_OF_SECOND_ELEMENT_IN_PAIR);

        tuple[firstSubSet] = firstElement;
        tuple[secondSubSet] = secondElement;
    }
}