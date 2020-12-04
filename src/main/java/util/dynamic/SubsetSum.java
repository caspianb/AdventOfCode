package util.dynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Subset-sum solver via dynamic programming (executes in pseudo-polynomial time).
 * @see https://en.wikipedia.org/wiki/Subset_sum_problem#Pseudo-polynomial_time_dynamic_programming_solution
 */
public class SubsetSum {

    /**
     * Locates the subset of values in the passed in list which sums up to the
     * target value. This algorithm supports positive and negative values. If
     * no solution could be located, an empty list will be returned.
     */
    public static List<Integer> solve(List<Integer> values, int targetValue) {
        boolean solutionFound = false;

        // A == sum of negative numbers
        int A = 0;
        for (int value : values) {
            A += (value < 0) ? value : 0;
        }

        // B == sum of positive numbers
        int B = 0;
        for (int value : values) {
            B += (value > 0) ? value : 0;
        }

        if (targetValue < A || targetValue > B) {
            // we can't possibly reach the target value!
            return Collections.emptyList();
        }

        // We don't need to track above our target value if only positive values exist in the array...
        // This is a simple, but drastic, optimization when dealing with only positive values.
        if (A == 0 && B > targetValue) {
            B = targetValue;
        }

        // Create an array to hold the values Q(i, s) for 1 <= i <= N and A <= s <= B.

        // Because arrays must be zero-based and A might be negative, we expand the array out
        // based on how "negative" A is -- for all latter references to the column index, we need
        // to ensure we utilize the value of A as an offset (e.g. Q[i][index - A])...
        boolean[][] Q = new boolean[values.size()][B - A + 1];

        // Initialization:
        // for A <= s <= B:
        //     Q(1, s) := (x1 == s)
        int x_i = values.get(0);
        for (int s = A; s <= B; s++) {
            Q[0][s - A] = (x_i == s);
        }

        // for i = 2...N
        //     Q(i, s) := Q(i - 1, s) or (xi == s) or Q(i - 1, s - xi),  for A <= s <= B.
        for (int i = 1; i < values.size(); i++) {
            x_i = values.get(i);

            for (int s = A; s <= B; s++) {

                Q[i][s - A] =
                        // Copy previous rows value for this column...
                        (Q[i - 1][s - A]) ||

                        // This row can obviously sum to itself!
                                (x_i == s) ||

                                // Add current row's value to true values in previous row...
                                (s - x_i >= A && s - x_i <= B && Q[i - 1][s - x_i - A]);

                if (s == targetValue && Q[i][s - A]) {
                    // We found a solution!
                    solutionFound = true;
                    break;
                }
            }
        }

        // We need to build our solution's list, now; this is a bit tricky.
        // We can easily determine the *last* number we used by locating the FIRST
        // Q[i][targetValue] that is true! We then need to go backwards through the array
        // to determine the previous values used to reach that value, and so on...
        List<Integer> solution = new ArrayList<Integer>(values.size());

        if (solutionFound) {
            int currTargetValue = targetValue;
            for (int i = Q.length - 1; i >= 0; i--) {
                int value = values.get(i);
                if (Q[i][currTargetValue - A] && (i == 0 || !Q[i - 1][currTargetValue - A])) {
                    solution.add(0, value);
                    currTargetValue = currTargetValue - value;
                }
            }
        }

        return solution;
    }

}
