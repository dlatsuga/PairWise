package org.pairwise.generator.pairwise;

import java.util.List;
import java.util.Map;

public interface PairWiseMatcher {
    Map<Integer, List<Integer>> getFreeMatchedPairsToCompleteTuple(Integer currentIndex);
}
