package utils.backtracking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BackTracker<Key, Typ> {

    protected final LinkedHashMap<Key, List<Typ>> possibleValues;
    protected final LinkedHashMap<Key, Integer> currentSelectedValues;

    public BackTracker() {
        possibleValues = new LinkedHashMap<Key, List<Typ>>();
        currentSelectedValues = new LinkedHashMap<Key, Integer>();
    }

    public BackTracker(Map<Key, Set<Typ>> possibleValues) {
        this();

        for (Entry<Key, Set<Typ>> pair : possibleValues.entrySet()) {
            Key key = pair.getKey();
            List<Typ> valuesList = new ArrayList<Typ>(pair.getValue());
            if (valuesList.isEmpty()) {
                throw new RuntimeException("Invalid backtracking values - list with [" + key + "] has 0 elements.");
            }
            this.possibleValues.put(key, valuesList);
        }

        reset();
    }

    public BackTracker<Key, Typ> addGroupValue(Key key, Typ value) {
        List<Typ> valuesList = possibleValues.get(key);
        if (valuesList == null) {
            valuesList = new ArrayList<Typ>();
            possibleValues.put(key, valuesList);
        }

        if (!valuesList.contains(value)) {
            valuesList.add(value);
        }

        reset();
        return this;
    }

    public void reset() {
        currentSelectedValues.clear();
        sortPossibleValuesKeys();
    }

    /**
     * This method will sort the keys in the possible values map based on the
     * total number of possible values that can be assigned to that key. This should
     * ensure that backtracking will assign to the "most constrained" variables first.
     */
    protected void sortPossibleValuesKeys() {
        List<Key> sortedKeys = new ArrayList<Key>(possibleValues.keySet());

        // Sort the keys such that keys with LESS values are processed FIRST
        Collections.sort(sortedKeys, new Comparator<Key>() {
            public int compare(Key o1, Key o2) {
                return possibleValues.get(o1).size() - possibleValues.get(o2).size();
            }
        });

        // Iterate through sorted keys and remove/re-add in order
        for (Key key : sortedKeys) {
            List<Typ> valuesList = possibleValues.get(key);
            possibleValues.remove(key);
            possibleValues.put(key, valuesList);
        }
    }

    public Map<Key, Typ> getCurrentValues() {
        Map<Key, Typ> values = new LinkedHashMap<Key, Typ>();
        for (Entry<Key, Integer> pair : currentSelectedValues.entrySet()) {
            Key key = pair.getKey();
            int currentIndex = pair.getValue();

            values.put(key, possibleValues.get(key).get(currentIndex));
        }

        return values;
    }

    /**
     * This method will assign a value to a key with no currently selected value.
     * @return true if changes to current values were detected, false otherwise
     */
    public boolean advanceCurrentValues() {
        boolean changeDetected = false;

        // Current set of values is fine, so simply assign next key without value
        Key firstKeyWithNoAssignedValue = getFirstKeyWithNoAssignedValue();

        // If key is null it means we've successfully found values for everything
        // so nothing will be changed
        if (firstKeyWithNoAssignedValue != null) {
            currentSelectedValues.put(firstKeyWithNoAssignedValue, 0);
            changeDetected = true;
        }

        return changeDetected;
    }

    /**
     * This method will update the most recently assigned key/value to the next value. If no
     * next value exists for that key then that key is removed from the selected values and
     * backtracking on previous keys will occur.
     * @return true if changes to current values were detected, false otherwise
     */
    public boolean backtrackCurrentValues() {
        boolean changeDetected = false;

        // Find the last key we assigned a value to
        Key lastKeyWithAssignedValue = getLastKeyWithAssignedValue();
        if (lastKeyWithAssignedValue != null) {
            // If no keys have an assigned value then backtracking is impossible (and we will return false)

            int lastKeyCurrentAssignedValue = currentSelectedValues.get(lastKeyWithAssignedValue);
            int totalPossibleValues = possibleValues.get(lastKeyWithAssignedValue).size();
            if (lastKeyCurrentAssignedValue + 1 < totalPossibleValues) {
                // We have more values we can try for the last key -- so simply change it
                currentSelectedValues.put(lastKeyWithAssignedValue, lastKeyCurrentAssignedValue + 1);
                changeDetected = true;
            }
            else {
                // No more values exist for last key?
                // Remove it from selected values list and backtrack!
                currentSelectedValues.remove(lastKeyWithAssignedValue);
                changeDetected = backtrackCurrentValues();
            }
        }

        return changeDetected;
    }

    public Key getLastKeyWithAssignedValue() {
        Key lastKeyWithAssignedValue = null;
        for (Entry<Key, Integer> pair : currentSelectedValues.entrySet()) {
            lastKeyWithAssignedValue = pair.getKey();
        }
        return lastKeyWithAssignedValue;
    }

    protected Key getFirstKeyWithNoAssignedValue() {
        for (Key key : possibleValues.keySet()) {
            if (!currentSelectedValues.containsKey(key)) {
                return key;
            }
        }

        return null;
    }

}
