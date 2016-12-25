package utils.backtracking;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PermutationGroupGenerator<Key, Typ> {

    private final LinkedHashMap<Key, List<Typ>> initialPermutation;
    private LinkedHashMap<Key, Integer> currentPermutationGroupIndices = null;
    private boolean firstPermutation = true;

    public PermutationGroupGenerator() {
        initialPermutation = new LinkedHashMap<Key, List<Typ>>();
    }

    public PermutationGroupGenerator(Map<Key, Set<Typ>> initialMap) {
        this();

        for (Key key : initialMap.keySet()) {
            List<Typ> valueList = new ArrayList<Typ>(initialMap.get(key));
            if (valueList.isEmpty()) {
                throw new RuntimeException("Invalid permutation group - permutation group [" + key + "] has 0 elements.");
            }
            initialPermutation.put(key, valueList);
        }

        reset();
    }

    public PermutationGroupGenerator<Key, Typ> addGroupValue(Key key, Typ value) {
        List<Typ> valuesList = initialPermutation.get(key);
        if (valuesList == null) {
            valuesList = new ArrayList<Typ>();
            initialPermutation.put(key, valuesList);
        }

        if (!valuesList.contains(value)) {
            valuesList.add(value);
        }

        reset();
        return this;
    }

    public void reset() {
        firstPermutation = true;
        initializeCurrentPermutationGroupIndices();
    }

    protected void initializeCurrentPermutationGroupIndices() {
        currentPermutationGroupIndices = new LinkedHashMap<Key, Integer>();
        for (Key key : initialPermutation.keySet()) {
            currentPermutationGroupIndices.put(key, 0);
        }
    }

    public Map<Key, Typ> getCurrentPermutation() {
        return buildCurrentPermutation();
    }

    public Map<Key, Typ> nextPermutation() {
        if (firstPermutation) {
            firstPermutation = false;
            return buildCurrentPermutation();
        }

        if (currentPermutationGroupIndices == null) {
            // previously located the last permutation -- return null
            return null;
        }

        // advance current permutation indices to next permutation
        boolean nextPermutationFound = false;
        for (Key key : currentPermutationGroupIndices.keySet()) {
            int maxSize = initialPermutation.get(key).size();
            int nextVal = currentPermutationGroupIndices.get(key) + 1;

            // Check if we overflow our list of elements for this key
            if (nextVal < maxSize) {
                // No overflow? New permutation located!
                nextPermutationFound = true;
                currentPermutationGroupIndices.put(key, nextVal);
                break;
            }

            // Overflow occurred - reset nextVal and try advancing key
            nextVal = 0;
            currentPermutationGroupIndices.put(key, nextVal);
        }

        if (!nextPermutationFound) {
            // no more permutations exist - return null
            currentPermutationGroupIndices = null;
            return null;
        }

        // Return new permutation map based on new permutation indices
        return buildCurrentPermutation();
    }

    protected Map<Key, Typ> buildCurrentPermutation() {
        // Build currentPermutation map based on permutation indices
        if (currentPermutationGroupIndices == null) {
            return null;
        }

        Map<Key, Typ> currentPermutation = new LinkedHashMap<Key, Typ>();
        for (Entry<Key, Integer> pair : currentPermutationGroupIndices.entrySet()) {
            Key key = pair.getKey();
            int position = pair.getValue();
            currentPermutation.put(key, initialPermutation.get(key).get(position));
        }

        return currentPermutation;
    }

}
