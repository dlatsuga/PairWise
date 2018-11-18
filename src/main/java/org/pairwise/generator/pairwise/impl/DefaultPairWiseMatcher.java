package org.pairwise.generator.pairwise.impl;

import org.pairwise.generator.SetDescription;
import org.pairwise.generator.SubSetDescription;
import org.pairwise.generator.pairwise.PairWiseDuplicateChecker;
import org.pairwise.generator.pairwise.PairWiseMatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.pairwise.generator.pairwise.Constants.INDEX_OF_FIRST_ELEMENT_IN_PAIR;
import static org.pairwise.generator.pairwise.Constants.INDEX_OF_FIRST_SUB_SET_IN_PAIR;
import static org.pairwise.generator.pairwise.Constants.INDEX_OF_SECOND_ELEMENT_IN_PAIR;
import static org.pairwise.generator.pairwise.Constants.INDEX_OF_SECOND_SUB_SET_IN_PAIR;

public class DefaultPairWiseMatcher implements PairWiseMatcher {

    private Integer firstSubSetMainCandidate;
    private Integer firstElementMainCandidate;
    private Integer secondSubSetMainCandidate;
    private Integer secondElementMainCandidate;

    private List<Integer> potentialClaimant;
    private Integer firstSubSetPotentialClaimant;
    private Integer firstElementPotentialClaimant;
    private Integer secondSubSetPotentialClaimant;
    private Integer secondElementPotentialClaimant;

    private Map<Integer, List<Integer>> freeMatchedPairsToCompleteTuple = new HashMap<>();
    private Map<Integer, Boolean> requiredIndexesToCompleteTuple;

    private Integer maxSubSetIndex;
    private List<SubSetDescription> subSets;
    private Map<Integer, List<Integer>> uniquePairsIndexes;
    private Map<Integer, Boolean> freePairs;
    private PairWiseDuplicateChecker duplicateChecker;

    public DefaultPairWiseMatcher(SetDescription setDescription, PairWiseDuplicateChecker duplicateChecker, Map<Integer, Boolean> freePairs) {
        this.maxSubSetIndex = setDescription.getMaxSubSetIndex();
        this.subSets = setDescription.getSubSets();
        this.uniquePairsIndexes = setDescription.getUniquePairsIndexes();
        this.duplicateChecker = duplicateChecker;
        this.freePairs = freePairs;
    }

    @Override
    public Map<Integer, List<Integer>> getFreeMatchedPairsToCompleteTuple(Integer currentIndex) {
        freeMatchedPairsToCompleteTuple.clear();

        initMainCandidate(currentIndex);
        requiredIndexesToCompleteTuple = getRequiredIndexesToCompleteTuple();
        List<Integer> freeIndexesToCheck = getFreeIndexesToCheck(currentIndex);

        requiredIndex:
        for (Map.Entry<Integer, Boolean> requiredIndex : requiredIndexesToCompleteTuple.entrySet()) {
            for (Integer freeIndex : freeIndexesToCheck) {
                initPotentialClaimant(freeIndex);
                if (!duplicateChecker.pairCandidateAlreadyExistInPairWiseMap(potentialClaimant)) {
                    if (potentialClaimantMatchesMainCandidate(requiredIndex.getKey(), freeIndex)) {
                        continue requiredIndex;
                    }
                }
            }
        }

        List<Integer> requiredIndexesList = getRequiredIndexesList();

        if (!requiredIndexesList.isEmpty()) {
            addArtificialPair(requiredIndexesList);
        }
        return freeMatchedPairsToCompleteTuple;
    }

    private void initMainCandidate(Integer currentIndex) {
        List<Integer> mainCandidatePairForTuple = uniquePairsIndexes.get(currentIndex);
        firstSubSetMainCandidate = mainCandidatePairForTuple.get(INDEX_OF_FIRST_SUB_SET_IN_PAIR);
        firstElementMainCandidate = mainCandidatePairForTuple.get(INDEX_OF_FIRST_ELEMENT_IN_PAIR);
        secondSubSetMainCandidate = mainCandidatePairForTuple.get(INDEX_OF_SECOND_SUB_SET_IN_PAIR);
        secondElementMainCandidate = mainCandidatePairForTuple.get(INDEX_OF_SECOND_ELEMENT_IN_PAIR);
    }

    private Map<Integer, Boolean> getRequiredIndexesToCompleteTuple() {
        Map<Integer, Boolean> requiredIndexesToCompleteTuple = new HashMap<>();

        List<Integer> requiredIndexes = IntStream.rangeClosed(0, maxSubSetIndex - 1).boxed().collect(toList());
        requiredIndexes.remove(firstSubSetMainCandidate);
        requiredIndexes.remove(secondSubSetMainCandidate);

        for (Integer requiredIndex : requiredIndexes) {
            requiredIndexesToCompleteTuple.put(requiredIndex, FALSE);
        }
        return requiredIndexesToCompleteTuple;
    }

    private List<Integer> getFreeIndexesToCheck(Integer currentIndex) {
        return freePairs.entrySet().stream()
                .filter(allSubSet -> !allSubSet.getKey().equals(currentIndex))
                .filter(allSubSet -> allSubSet.getValue().equals(TRUE))
                .map(Map.Entry::getKey)
                .collect(toList());
    }

    private void initPotentialClaimant(Integer freeIndex) {
        potentialClaimant = uniquePairsIndexes.get(freeIndex);
        firstSubSetPotentialClaimant = potentialClaimant.get(INDEX_OF_FIRST_SUB_SET_IN_PAIR);
        firstElementPotentialClaimant = potentialClaimant.get(INDEX_OF_FIRST_ELEMENT_IN_PAIR);
        secondSubSetPotentialClaimant = potentialClaimant.get(INDEX_OF_SECOND_SUB_SET_IN_PAIR);
        secondElementPotentialClaimant = potentialClaimant.get(INDEX_OF_SECOND_ELEMENT_IN_PAIR);
    }

    private Boolean potentialClaimantMatchesMainCandidate(Integer requiredIndex, Integer freeIndex) {
        if (requiredIndex.equals(secondSubSetPotentialClaimant)) {
            return analyseFirstSubSetForDuplicates(requiredIndex, freeIndex);
        } else if (requiredIndex.equals(firstSubSetPotentialClaimant)) {
            return analyseSecondSubSetForDuplicates(requiredIndex, freeIndex);

        }
        return FALSE;
    }

    private List<Integer> getRequiredIndexesList() {
        return requiredIndexesToCompleteTuple.entrySet().stream()
                .filter(allSubSet -> allSubSet.getValue().equals(FALSE))
                .map(Map.Entry::getKey)
                .collect(toList());
    }

    private void addArtificialPair(List<Integer> requiredIndexesList) {
        Integer INDEX_FOR_ARTIFICIAL_PAIR = -2;
        for (Integer requiredIndex : requiredIndexesList) {

            Integer RANDOM_ARTIFICIAL_ELEMENT = subSets.get(requiredIndex)
                    .getElementsInSubSet().keySet()
                    .stream()
                    .findAny()
                    .orElseGet(() -> -2);

            List<Integer> ARTIFICIAL_PAIR = asList(firstSubSetMainCandidate, firstElementMainCandidate, requiredIndex, RANDOM_ARTIFICIAL_ELEMENT);
            freeMatchedPairsToCompleteTuple.put(INDEX_FOR_ARTIFICIAL_PAIR--, ARTIFICIAL_PAIR);
        }
    }

    private Boolean analyseFirstSubSetForDuplicates(Integer requiredIndex, Integer freeIndex) {
        List<Integer> temporaryPairToCheck;
        if (firstSubSetPotentialClaimant.equals(firstSubSetMainCandidate)) {
            if (firstElementPotentialClaimant.equals(firstElementMainCandidate)) {
                temporaryPairToCheck = asList(secondSubSetMainCandidate, secondElementMainCandidate, secondSubSetPotentialClaimant, secondElementPotentialClaimant);
                return addMatchedPairIfWillNotCreateDuplicate(requiredIndex, freeIndex, temporaryPairToCheck);
            }
        } else if (firstSubSetPotentialClaimant.equals(secondSubSetMainCandidate)) {
            if (firstElementPotentialClaimant.equals(secondElementMainCandidate)) {
                temporaryPairToCheck = asList(firstSubSetMainCandidate, firstElementMainCandidate, secondSubSetPotentialClaimant, secondElementPotentialClaimant);
                return addMatchedPairIfWillNotCreateDuplicate(requiredIndex, freeIndex, temporaryPairToCheck);
            }
        }
        return FALSE;
    }

    private Boolean analyseSecondSubSetForDuplicates(Integer requiredIndex, Integer freeIndex) {
        List<Integer> temporaryPairToCheck;
        if (secondSubSetPotentialClaimant.equals(firstSubSetMainCandidate)) {
            if (secondElementPotentialClaimant.equals(firstElementMainCandidate)) {
                temporaryPairToCheck = asList(secondSubSetPotentialClaimant, secondElementPotentialClaimant, secondSubSetMainCandidate, secondElementMainCandidate);
                return addMatchedPairIfWillNotCreateDuplicate(requiredIndex, freeIndex, temporaryPairToCheck);
            }
        } else if (secondSubSetPotentialClaimant.equals(secondSubSetMainCandidate)) {
            if (secondElementPotentialClaimant.equals(secondElementMainCandidate)) {
                temporaryPairToCheck = asList(firstSubSetMainCandidate, firstElementMainCandidate, firstSubSetPotentialClaimant, firstElementPotentialClaimant);
                return addMatchedPairIfWillNotCreateDuplicate(requiredIndex, freeIndex, temporaryPairToCheck);
            }
        }
        return FALSE;
    }

    private Boolean addMatchedPairIfWillNotCreateDuplicate(Integer requiredIndex, Integer freeIndex, List<Integer> temporaryPairToCheck) {
        if (!duplicateChecker.pairCandidateAlreadyExistInPairWiseMap(temporaryPairToCheck)) {
            freeMatchedPairsToCompleteTuple.put(freeIndex, potentialClaimant);
            requiredIndexesToCompleteTuple.put(requiredIndex, TRUE);
            return TRUE;
        }
        return FALSE;
    }
}