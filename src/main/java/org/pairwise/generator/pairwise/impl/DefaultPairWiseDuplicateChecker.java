package org.pairwise.generator.pairwise.impl;

import org.pairwise.generator.pairwise.PairWiseDuplicateChecker;

import java.util.List;
import java.util.Map;

import static org.pairwise.generator.pairwise.Constants.INDEX_OF_FIRST_ELEMENT_IN_PAIR;
import static org.pairwise.generator.pairwise.Constants.INDEX_OF_FIRST_SUB_SET_IN_PAIR;
import static org.pairwise.generator.pairwise.Constants.INDEX_OF_SECOND_ELEMENT_IN_PAIR;
import static org.pairwise.generator.pairwise.Constants.INDEX_OF_SECOND_SUB_SET_IN_PAIR;

public class DefaultPairWiseDuplicateChecker implements PairWiseDuplicateChecker {

    private Map<Integer, List<Integer>> pairsWiseIndexes;

    public DefaultPairWiseDuplicateChecker(Map<Integer, List<Integer>> pairsWiseIndexes) {
        this.pairsWiseIndexes = pairsWiseIndexes;
    }

    @Override
    public boolean pairCandidateAlreadyExistInPairWiseMap(List<Integer> pair) {

        Integer firstSubSetCandidate = pair.get(INDEX_OF_FIRST_SUB_SET_IN_PAIR);
        Integer firstElementCandidate = pair.get(INDEX_OF_FIRST_ELEMENT_IN_PAIR);
        Integer secondSubSetCandidate = pair.get(INDEX_OF_SECOND_SUB_SET_IN_PAIR);
        Integer secondElementCandidate = pair.get(INDEX_OF_SECOND_ELEMENT_IN_PAIR);

        for (Map.Entry<Integer, List<Integer>> pairWiseTuple : pairsWiseIndexes.entrySet()) {

            List<Integer> pairWiseTupleValues = pairWiseTuple.getValue();

            Integer firstElementPairWiseToCompare = pairWiseTupleValues.get(firstSubSetCandidate);
            Integer secondElementPairWiseToCompare = pairWiseTupleValues.get(secondSubSetCandidate);

            boolean isFirstElementOfPairDuplicate = firstElementCandidate.equals(firstElementPairWiseToCompare);
            boolean isSecondElementOfPairDuplicate = secondElementCandidate.equals(secondElementPairWiseToCompare);

            if (isFirstElementOfPairDuplicate && isSecondElementOfPairDuplicate) {
                return true;
            }

        }
        return false;
    }
}
