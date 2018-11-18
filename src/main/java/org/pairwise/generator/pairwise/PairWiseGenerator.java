package org.pairwise.generator.pairwise;

import java.util.List;
import java.util.Map;

public interface PairWiseGenerator {
    Map<Integer, List<Integer>> generatePairWiseIndexes();

    Map<Integer, List<Object>> generatePairsWiseValues();
}
