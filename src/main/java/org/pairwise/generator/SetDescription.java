package org.pairwise.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

public class SetDescription {

    private static final Integer INDEX_OF_FIRST_SUB_SET_IN_PAIR = 0;
    private static final Integer INDEX_OF_FIRST_ELEMENT_IN_PAIR = 1;
    private static final Integer INDEX_OF_SECOND_SUB_SET_IN_PAIR = 2;
    private static final Integer INDEX_OF_SECOND_ELEMENT_IN_PAIR = 3;
    private int subSetIndex = 0;
    private int indexOfUniquePair = 0;
    private List<SubSetDescription> subSets = new ArrayList<>();
    private Map<Integer, Set<Integer>> elementsInSet = new HashMap<>();
    private Map<Integer, List<Integer>> uniquePairsIndexes = new HashMap<>();
    private Map<Integer, List<Object>> uniquePairsValues = new HashMap<>();
    private Map<Integer, List<Integer>> pairsWise = new HashMap<>();
    private Map<Integer, List<Object>> pairsWiseValues = new HashMap<>();
    private Map<Integer, Boolean> freePairs = new HashMap<>();

    public void addSubSet(SubSetDescription subSetDescription) {

        subSetDescription.setSubSetIndex(subSetIndex);
        subSets.add(subSetDescription);

        Set<Integer> elementsIndexesInSubSet = subSetDescription.getElementsInSubSet().keySet();
        elementsInSet.put(subSetIndex, elementsIndexesInSubSet);

        subSetIndex++;
    }

    public void generateUniquePairs() {

        if (subSetIndex < 2) {
            generateUniquePairsForMonoSet();
            return;
        }

        for (int currentSubSetIndex = 0; currentSubSetIndex < elementsInSet.keySet().size() - 1; currentSubSetIndex++) {

            int currentIndex = currentSubSetIndex;

            // Подмножество для первого элемента пары
            Map<Integer, Set<Integer>> firstElementSet = elementsInSet.entrySet().stream()
                    .filter(allSubSets -> allSubSets.getKey().equals(currentIndex))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

            // Подмножество для второго элемента пары
            Map<Integer, Set<Integer>> secondElementSet = elementsInSet.entrySet().stream()
                    .filter(allSubSets -> !allSubSets.getKey().equals(currentIndex))
                    .filter(allSubSets -> allSubSets.getKey() > currentIndex)
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));


            for (Integer indexOfCurrentElement : firstElementSet.get(currentIndex)) {
                for (Map.Entry<Integer, Set<Integer>> pairSubSets : secondElementSet.entrySet()) {

                    int pairSubSetIndex = pairSubSets.getKey();

                    for (Integer indexOfPairElement : pairSubSets.getValue()) {
                        uniquePairsIndexes.put(indexOfUniquePair++, asList(currentSubSetIndex, indexOfCurrentElement, pairSubSetIndex, indexOfPairElement));
                    }

                }
            }
        }
    }

    private void generateUniquePairsForMonoSet() {
        for (Map.Entry<Integer, Set<Integer>> elements : elementsInSet.entrySet()) {
            for (Integer value : elements.getValue()) {
                uniquePairsIndexes.put(indexOfUniquePair++, asList(elements.getKey(), value));
            }
        }
    }

    public Map<Integer, List<Integer>> getUniquePairsIndexes() {
        return uniquePairsIndexes;
    }

    public Map<Integer, List<Object>> getUniquePairsValues() {
        for (Map.Entry<Integer, List<Integer>> uniquePairIndexes : uniquePairsIndexes.entrySet()) {

            if (subSetIndex < 2) {
                Integer indexOfFirstSubSet = uniquePairIndexes.getValue().get(0);
                Integer indexOfFirstElement = uniquePairIndexes.getValue().get(1);

                SubSetDescription firstSubSetDescription = subSets.get(indexOfFirstSubSet);
                Object firstElement = firstSubSetDescription.getElementsInSubSet().get(indexOfFirstElement);

                uniquePairsValues.put(uniquePairIndexes.getKey(), asList(firstElement));
            } else {
                Integer indexOfFirstSubSet = uniquePairIndexes.getValue().get(0);
                Integer indexOfFirstElement = uniquePairIndexes.getValue().get(1);

                Integer indexOfSecondSubSet = uniquePairIndexes.getValue().get(2);
                Integer indexOfSecondElement = uniquePairIndexes.getValue().get(3);

                SubSetDescription firstSubSetDescription = subSets.get(indexOfFirstSubSet);
                SubSetDescription secondSubSetDescription = subSets.get(indexOfSecondSubSet);
                Object firstElement = firstSubSetDescription.getElementsInSubSet().get(indexOfFirstElement);
                Object secondElement = secondSubSetDescription.getElementsInSubSet().get(indexOfSecondElement);

                uniquePairsValues.put(uniquePairIndexes.getKey(), asList(firstElement, secondElement));
            }
        }
        return uniquePairsValues;
    }

    public void newGeneratePairWise() {

        if (subSetIndex <= 2) {
            for (Map.Entry<Integer, List<Integer>> uniquePair : uniquePairsIndexes.entrySet()) {
                List<Integer> values = uniquePair.getValue();
                pairsWise.put(uniquePair.getKey(), range(0, values.size()).filter(index -> index % 2 != 0).mapToObj(values::get).collect(toList()));
            }
            return;
        }

        initFreePairs();

        Integer indexOfPairWiseTuple = 0;

        Integer indexOfCurrentCandidateForPairWiseTuple = getFirstFreeIndex();
        // Если есть свободный индекс - начинаем собирать кортеж
        if (indexOfCurrentCandidateForPairWiseTuple >= 0) {

//            System.out.println("indexOfCurrentCandidateForPairWiseTuple : " + indexOfCurrentCandidateForPairWiseTuple);
//            if(indexOfCurrentCandidateForPairWiseTuple.equals(2)){
//                System.out.println();
//            }

            for (; indexOfCurrentCandidateForPairWiseTuple < freePairs.keySet().size() && indexOfCurrentCandidateForPairWiseTuple >= 0; ) {
                // Все индексы которые надо заполнить в кортеже
//                indexesOfRequiredElementsForCompleteTuple = IntStream.rangeClosed(0, subSetIndex - 1).boxed().collect(toList());
                Integer[] tuple = new Integer[subSetIndex];

                List<Integer> initialCandidatePairForTuple = uniquePairsIndexes.get(indexOfCurrentCandidateForPairWiseTuple);

                if (indexOfCurrentCandidateForPairWiseTuple.equals(9)) {
                    System.out.println();
                }

                if (pairCandidateAlreadyExistInPairWiseMap(initialCandidatePairForTuple)) {
                    freePairs.put(indexOfCurrentCandidateForPairWiseTuple, FALSE); // Скипаем initialCandidatePairForTuple, уже покрыта в мапе Кортежей, для нее не надо искать пары
                    indexOfCurrentCandidateForPairWiseTuple = getFirstFreeIndex();
                    continue;
                }

                // Ищем свободные пары для кортежа
                Map<Integer, List<Integer>> freeMatchedPairsToCompleteTuple =
                        findFreeMatchedPairsToCompleteTuple(initialCandidatePairForTuple, indexOfCurrentCandidateForPairWiseTuple);

//                freePairs.put(indexOfCurrentCandidateForPairWiseTuple, FALSE);
//                System.out.println("init index FALSE : " + indexOfCurrentCandidateForPairWiseTuple);
//                Integer key = (Integer) freeMatchedPairsToCompleteTuple.keySet().toArray()[0];
//                freePairs.put(key, FALSE);
//                System.out.println("pair index FALSE : " + key);
//                indexOfCurrentCandidateForPairWiseTuple = getFirstFreeIndex();
//                System.out.println("new index : " + indexOfCurrentCandidateForPairWiseTuple);
//                System.out.println();
                if (indexOfCurrentCandidateForPairWiseTuple.equals(6)) {
                    System.out.println();
                    ;
                }

                if (freeMatchedPairsToCompleteTuple.keySet().size() > 0) {

                    Integer firstSubSetCandidate = initialCandidatePairForTuple.get(INDEX_OF_FIRST_SUB_SET_IN_PAIR);
                    Integer firstElementCandidate = initialCandidatePairForTuple.get(INDEX_OF_FIRST_ELEMENT_IN_PAIR);
                    Integer secondSubSetCandidate = initialCandidatePairForTuple.get(INDEX_OF_SECOND_SUB_SET_IN_PAIR);
                    Integer secondElementCandidate = initialCandidatePairForTuple.get(INDEX_OF_SECOND_ELEMENT_IN_PAIR);

//                    List<Integer> pairWiseTuple = new ArrayList<>(subSets.size());
                    Integer[] pairWiseTuple = new Integer[subSets.size()];
                    pairWiseTuple[firstSubSetCandidate] = firstElementCandidate;
                    pairWiseTuple[secondSubSetCandidate] = secondElementCandidate;

                    System.out.println("indexOfCurrentCandidateForPairWiseTuple  FALSE : " + indexOfCurrentCandidateForPairWiseTuple);
                    freePairs.put(indexOfCurrentCandidateForPairWiseTuple, FALSE);

                    for (Integer indexOfPairToCompleteTuple : freeMatchedPairsToCompleteTuple.keySet()) {
                        List<Integer> pairToCompleteTuple = freeMatchedPairsToCompleteTuple.get(indexOfPairToCompleteTuple);

                        Integer firstSubSetPotentialMatched = pairToCompleteTuple.get(INDEX_OF_FIRST_SUB_SET_IN_PAIR);
                        Integer firstElementPotentialMatched = pairToCompleteTuple.get(INDEX_OF_FIRST_ELEMENT_IN_PAIR);
                        Integer secondSubSetPotentialMatched = pairToCompleteTuple.get(INDEX_OF_SECOND_SUB_SET_IN_PAIR);
                        Integer secondElementPotentialMatched = pairToCompleteTuple.get(INDEX_OF_SECOND_ELEMENT_IN_PAIR);

                        pairWiseTuple[firstSubSetPotentialMatched] = firstElementPotentialMatched;
                        pairWiseTuple[secondSubSetPotentialMatched] = secondElementPotentialMatched;

                        System.out.println("indexOfPairToCompleteTuple  FALSE : " + indexOfPairToCompleteTuple);
                        freePairs.put(indexOfPairToCompleteTuple, FALSE);

                    }
                    if (indexOfPairWiseTuple.equals(4)) {
                        System.out.println();
                    }
                    pairsWise.put(indexOfPairWiseTuple++, asList(pairWiseTuple));
                }
                indexOfCurrentCandidateForPairWiseTuple = getFirstFreeIndex();
            }
        }
        System.out.println("THE END");
    }

    private void initFreePairs() {
        // Мапа которая хранит флаг свободен индекс или нет
        // Изначально все свободны
        for (Integer indexOfUniquePair : uniquePairsIndexes.keySet()) {
            freePairs.put(indexOfUniquePair, TRUE);
        }
    }

    private Integer getFirstFreeIndex() {
        for (Map.Entry<Integer, Boolean> candidateForFreeIndex : freePairs.entrySet()) {
            if (candidateForFreeIndex.getValue()) {
                return candidateForFreeIndex.getKey();
            }
        }
        return -1; // Если нет свободного индекса - вернет -1
    }

    private Map<Integer, List<Integer>> findFreeMatchedPairsToCompleteTuple(List<Integer> initialCandidatePairForTuple, Integer currentIndex) {

        Map<Integer, List<Integer>> freeMatchedPairsForCompleteTuple = new HashMap<>();

//        System.out.println("currentIndex : " + currentIndex);

        if (currentIndex.equals(4)) {
            System.out.println();
        }

        List<Integer> requiredIndexes = IntStream.rangeClosed(0, subSetIndex - 1).boxed().collect(toList());
        requiredIndexes.remove(initialCandidatePairForTuple.get(INDEX_OF_FIRST_SUB_SET_IN_PAIR));
        requiredIndexes.remove(initialCandidatePairForTuple.get(INDEX_OF_SECOND_SUB_SET_IN_PAIR));

        Map<Integer, Boolean> requiredIndexesToCompleteTupl = new HashMap<>();

        // Мапа индексов которые надо найти в Свободных парах freeIndexesToCheck
        for (Integer requiredIndex : requiredIndexes) {
            requiredIndexesToCompleteTupl.put(requiredIndex, FALSE);
        }

        List<Integer> freeIndexesToCheck = freePairs.entrySet().stream()
                .filter(allSubSet -> !allSubSet.getKey().equals(currentIndex)) // Исключаем текущий индекс
                .filter(allSubSet -> allSubSet.getValue().equals(TRUE)) // Включаем только свободные индексы
                .map(Map.Entry::getKey)
                .collect(toList());

        Integer firstSubSetCandidate = initialCandidatePairForTuple.get(INDEX_OF_FIRST_SUB_SET_IN_PAIR);
        Integer firstElementCandidate = initialCandidatePairForTuple.get(INDEX_OF_FIRST_ELEMENT_IN_PAIR);
        Integer secondSubSetCandidate = initialCandidatePairForTuple.get(INDEX_OF_SECOND_SUB_SET_IN_PAIR);
        Integer secondElementCandidate = initialCandidatePairForTuple.get(INDEX_OF_SECOND_ELEMENT_IN_PAIR);

//        requiredIndex: for (Integer requiredIndex : requiredIndexesToCompleteTuple) {
        requiredIndex:
//        while (!requiredIndexesToCompleteTuple.isEmpty()) {
        for (Map.Entry<Integer, Boolean> index : requiredIndexesToCompleteTupl.entrySet()) {


            for (Integer freeIndex : freeIndexesToCheck) {

                List<Integer> potentialMatchedFreePair = uniquePairsIndexes.get(freeIndex);


                // Проверяем есть ли данная пара уже в мапе pairsWise
                if (!pairCandidateAlreadyExistInPairWiseMap(potentialMatchedFreePair)) {

                    Integer firstSubSetPotentialMatched = potentialMatchedFreePair.get(INDEX_OF_FIRST_SUB_SET_IN_PAIR);
                    Integer firstElementPotentialMatched = potentialMatchedFreePair.get(INDEX_OF_FIRST_ELEMENT_IN_PAIR);
                    Integer secondSubSetPotentialMatched = potentialMatchedFreePair.get(INDEX_OF_SECOND_SUB_SET_IN_PAIR);
                    Integer secondElementPotentialMatched = potentialMatchedFreePair.get(INDEX_OF_SECOND_ELEMENT_IN_PAIR);

                    // Проверяем есть ли индекс первого сабсета в Листе Требуемых индексов requiredIndexesToCompleteTuple для Кортежа
                    if (index.getKey().equals(firstSubSetPotentialMatched)) {

                        //Второй сабсет Matched равен ПЕРВОМУ сабсет Candidate ?
                        if (secondSubSetPotentialMatched.equals(firstSubSetCandidate)) {
                            //Второй элемент Matched равен первому элементу Candidate ?
                            if (secondElementPotentialMatched.equals(firstElementCandidate)) {

//                                // TODO : Протестировать это гавно
                                List<Integer> temporaryPairToCheck = asList(secondSubSetPotentialMatched, secondElementPotentialMatched, secondSubSetCandidate, secondElementCandidate);
                                if (!pairCandidateAlreadyExistInPairWiseMap(temporaryPairToCheck)) {
                                    // Да сука равен...
                                    // Добавляем его в нашу ебаную мапу Matched
                                    freeMatchedPairsForCompleteTuple.put(freeIndex, potentialMatchedFreePair);
                                    // Удаляем Требуемый индекс из requiredIndexesToCompleteTuple (Пара для индекса найдена)
//                                        requiredIndexesToCompleteTuple.remove(firstSubSetPotentialMatched);
                                    requiredIndexesToCompleteTupl.put(index.getKey(), TRUE);
                                    // ??????????????????????????????????????
                                    // Надо ли блокировать индекс как занятый в freePairs -- Возможно в методе где конкретно заюзаем... пару
                                    continue requiredIndex;
                                }
                            }
                            //Второй сабсет Matched равен ВТОРОМУ сабсет Candidate ?
                        } else if (secondSubSetPotentialMatched.equals(secondSubSetCandidate)) {
                            if (secondElementPotentialMatched.equals(secondElementCandidate)) {

                                // TODO : Протестировать это гавно
                                List<Integer> temporaryPairToCheck = asList(firstSubSetCandidate, firstElementCandidate, firstSubSetPotentialMatched, firstElementPotentialMatched);
                                if (!pairCandidateAlreadyExistInPairWiseMap(temporaryPairToCheck)) {

                                    freeMatchedPairsForCompleteTuple.put(freeIndex, potentialMatchedFreePair);
                                    requiredIndexesToCompleteTupl.put(index.getKey(), TRUE);
//                                        requiredIndexesToCompleteTuple.remove(firstSubSetPotentialMatched);
                                    continue requiredIndex;
                                }
                            }
                        }
                    }
                    // Проверяем есть ли индекс Второго сабсета в Листе Требуемых индексов requiredIndexesToCompleteTuple для Кортежа
                    else if (index.getKey().equals(secondSubSetPotentialMatched)) {
                        if (firstSubSetPotentialMatched.equals(firstSubSetCandidate)) {
                            if (firstElementPotentialMatched.equals(firstElementCandidate)) {

                                // TODO : Протестировать это гавно ++
                                List<Integer> temporaryPairToCheck = asList(secondSubSetCandidate, secondElementCandidate, secondSubSetPotentialMatched, secondElementPotentialMatched);
                                if (!pairCandidateAlreadyExistInPairWiseMap(temporaryPairToCheck)) {

                                    freeMatchedPairsForCompleteTuple.put(freeIndex, potentialMatchedFreePair);
                                    requiredIndexesToCompleteTupl.put(index.getKey(), TRUE);
//                                        requiredIndexesToCompleteTuple.remove(secondSubSetPotentialMatched);
                                    continue requiredIndex;  // ++++++
                                }
                            }
                        } else if (firstSubSetPotentialMatched.equals(secondSubSetCandidate)) {
                            if (firstElementPotentialMatched.equals(secondElementCandidate)) {


                                if (freeIndex.equals(21)) {
                                    System.out.println();
                                }
                                // TODO : Протестировать это гавно
                                List<Integer> temporaryPairToCheck = asList(firstSubSetCandidate, firstElementCandidate, secondSubSetPotentialMatched, secondElementPotentialMatched);
                                if (!pairCandidateAlreadyExistInPairWiseMap(temporaryPairToCheck)) {
                                    freeMatchedPairsForCompleteTuple.put(freeIndex, potentialMatchedFreePair);

                                    requiredIndexesToCompleteTupl.put(index.getKey(), TRUE);
//                                        requiredIndexesToCompleteTuple.remove(secondSubSetPotentialMatched);
                                    continue requiredIndex;
                                }
                            }
                        }
                    }
                }


            }
        }


        // TODO : Проверить правильная ли тут логика и куда надо кидать если свободные пары закончились, а кортеж не собран
//            break requiredIndex; // Если сука закончились свободные индексы
//        }


        List<Integer> requiredIndexesList = requiredIndexesToCompleteTupl.entrySet().stream()
                .filter(allSubSet -> allSubSet.getValue().equals(FALSE)) // Включаем только свободные индексы
                .map(Map.Entry::getKey)
                .collect(toList());


        // TODO : Перенести эту  проверку в начало метода, чтобы не искать для инишиал пары все остальные пары
        // Если не все нужные для Кортежа индексы найдены
        if (!requiredIndexesList.isEmpty()) {
            if (pairCandidateAlreadyExistInPairWiseMap(initialCandidatePairForTuple)) {
                System.out.println("currentIndex FALSE : " + currentIndex);
                freePairs.put(currentIndex, FALSE); // Скипаем initialCandidatePairForTuple, уже покрыта в мапе Кортежей, для нее не надо искать пары
            } else {
                // дохуяриваем случайными значениями Из конкретного Параметра (СабСета) чтоюбы дозаполнить мапу freeMatchedPairsForCompleteTuple
                // TODO : черячим пары с фиктивным индексом -- Например (-2)
                Integer INDEX_FOR_ARTIFICIAL_PAIR = -2;
                for (Integer requiredIndex : requiredIndexesList) {

                    Integer RANDOM_ARTIFICIAL_ELEMENT = subSets.get(requiredIndex)
                            .getElementsInSubSet().keySet()
                            .stream()
                            .findAny()
                            .get();

                    // TODO : проверить можно ли пихать на первое место первый Элемент из InitialCandidate
                    List<Integer> ARTIFICIAL_PAIR = asList(firstSubSetCandidate, firstElementCandidate, requiredIndex, RANDOM_ARTIFICIAL_ELEMENT);
                    freeMatchedPairsForCompleteTuple.put(INDEX_FOR_ARTIFICIAL_PAIR--, ARTIFICIAL_PAIR);
                }
            }
        }

        return freeMatchedPairsForCompleteTuple;
    }

    private boolean pairCandidateAlreadyExistInPairWiseMap(List<Integer> initialCandidatePairForTuple) {

        Integer firstSubSetCandidate = initialCandidatePairForTuple.get(INDEX_OF_FIRST_SUB_SET_IN_PAIR);
        Integer firstElementCandidate = initialCandidatePairForTuple.get(INDEX_OF_FIRST_ELEMENT_IN_PAIR);
        Integer secondSubSetCandidate = initialCandidatePairForTuple.get(INDEX_OF_SECOND_SUB_SET_IN_PAIR);
        Integer secondElementCandidate = initialCandidatePairForTuple.get(INDEX_OF_SECOND_ELEMENT_IN_PAIR);

        for (Map.Entry<Integer, List<Integer>> pairWiseTuple : pairsWise.entrySet()) {

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

    public Map<Integer, List<Object>> getPairsWiseValues() {

        for (Map.Entry<Integer, List<Integer>> pairWiseIndexes : pairsWise.entrySet()) {
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
}