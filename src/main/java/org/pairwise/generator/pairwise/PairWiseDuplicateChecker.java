package org.pairwise.generator.pairwise;

import java.util.List;

public interface PairWiseDuplicateChecker {
    boolean pairCandidateAlreadyExistInPairWiseMap(List<Integer> pair);
}
