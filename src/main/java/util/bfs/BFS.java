package util.bfs;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.common.collect.Sets;

public class BFS {

    /**
     * Applies a BFS search to the given State and returns the goal state when found.
     * @param init - initial state
     * @param goal - tests if given state is goal state
     * @param nextMoves - function to return a set of valid moves from a given state
     * @return 
     */
    public static <T> T solve(T init, Predicate<T> goal, Function<T, Collection<T>> nextMoves) {
        return solve(init, goal, nextMoves, null);
    }

    /**
     * Applies a BFS search to the given State and returns the goal state when found.
     * @param init - initial state
     * @param goal - tests if given state is goal state
     * @param nextMoves - function to return a set of valid moves from a given state
     * @Param prune - accepts the current state and history and returns a pruned collection of states; useful when non-trivial pruning is required. 
     * @return goal state when reached
     */
    public static <T> T solve(T init, Predicate<T> goal, Function<T, Collection<T>> nextMoves, BiFunction<Set<T>, Set<T>, Set<T>> prune) {
        T answer = null;

        Objects.requireNonNull(init);
        Objects.requireNonNull(nextMoves);
        Objects.requireNonNull(goal);

        Set<T> processedStates = Sets.newLinkedHashSet();
        Set<T> currentStates = Sets.newLinkedHashSet();
        currentStates.add(init);

        outer:
        while (!currentStates.isEmpty()) {
            Set<T> newStates = Sets.newLinkedHashSet();
            for (T state : currentStates) {

                if (goal.test(state)) {
                    answer = state;
                    break outer;
                }

                processedStates.add(state);
                newStates.addAll((Collection<T>) nextMoves.apply(state));
            }
            newStates.removeAll(processedStates);

            if (prune != null) {
                newStates = prune.apply(newStates, processedStates);
            }

            currentStates = newStates;
        }

        return answer;
    }

    /**
     * 
     * @param init - initial state
     * @param nextMoves - function to return a set of valid moves from a given state
     * @param accept - tests if given state should be accumulated into answers
     * @return set of states which passed accumulator
     */
    public static <T extends State> Set<T> accumulate(T init, Predicate<T> accept, Function<T, Collection<T>> nextMoves) {
        Set<T> answers = Sets.newLinkedHashSet();

        Objects.requireNonNull(init);
        Objects.requireNonNull(accept);
        Objects.requireNonNull(nextMoves);

        Set<T> processedStates = Sets.newLinkedHashSet();
        Set<T> currentStates = Sets.newLinkedHashSet();
        currentStates.add(init);

        while (!currentStates.isEmpty()) {
            Set<T> newStates = Sets.newLinkedHashSet();
            for (T state : currentStates) {

                if (accept.test(state)) {
                    answers.add(state);
                }

                processedStates.add(state);
                newStates.addAll((Collection<T>) nextMoves.apply(state));
                newStates.removeAll(processedStates);
            }

            currentStates = newStates;
        }

        return answers;
    }

}
