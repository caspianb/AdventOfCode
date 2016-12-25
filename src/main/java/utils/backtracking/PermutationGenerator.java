package utils.backtracking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class PermutationGenerator<Typ> {

    private final List initialPermutation;
    private final Comparator listComparator;
    private List currentPermutation;
    private boolean firstPermutation = true;

    public PermutationGenerator(Collection<? extends Comparable> initialList) {
        listComparator = null;
        initialPermutation = new ArrayList(initialList);
        Collections.sort(initialPermutation);
        reset();
    }

    public PermutationGenerator(Collection<Typ> initialList, Comparator<Typ> listComparator) {
        this.listComparator = listComparator;
        initialPermutation = new ArrayList(initialList);
        Collections.sort(initialPermutation, listComparator);
        reset();
    }

    public void reset() {
        firstPermutation = true;
        currentPermutation = new ArrayList(initialPermutation);
    }

    public List<Typ> getCurrentPermutation() {
        return (currentPermutation == null) ? null : Collections.unmodifiableList(currentPermutation);
    }

    public List<Typ> nextPermutation() {
        if (firstPermutation) {
            firstPermutation = false;
            return Collections.unmodifiableList(currentPermutation);
        }

        if (currentPermutation == null) {
            // previously located the last permutation -- return null
            return null;
        }

        int length = currentPermutation.size();
        int key = length - 1;

        // Find first element to permute
        for (; key > 0; key--) {
            Object e1 = currentPermutation.get(key - 1);
            Object e2 = currentPermutation.get(key);
            if (listComparator == null) {
                if (((Comparable) e1).compareTo(e2) < 0) {
                    break;
                }
            }
            else if (listComparator.compare(e1, e2) < 0) {
                break;
            }
        }

        if (--key <= -1) {
            // last permutation has been found -- return null
            currentPermutation = null;
            return null;
        }

        // Find next element to permute with element at key index
        int nextKey = length - 1;
        for (; nextKey > key; nextKey--) {
            Object e1 = currentPermutation.get(key);
            Object e2 = currentPermutation.get(nextKey);
            if (listComparator == null) {
                if (((Comparable) e1).compareTo(e2) < 0) {
                    break;
                }
            }
            else if (listComparator.compare(e1, e2) < 0) {
                break;
            }
        }

        // Swap elements at key and nextKey
        swapElements(key, nextKey);

        // Reverse elements between key and length
        while (--length > ++key) {
            swapElements(length, key);
        }

        return Collections.unmodifiableList(currentPermutation);
    }

    private void swapElements(int pos1, int pos2) {
        Object tempVal = currentPermutation.get(pos1);
        currentPermutation.set(pos1, currentPermutation.get(pos2));
        currentPermutation.set(pos2, tempVal);
    }

}
