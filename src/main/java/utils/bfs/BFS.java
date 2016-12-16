package utils.bfs;

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
     * @param nextMoves - function to return a set of valid moves from a given state
     * @param goal - tests if given state is goal state
     * @return 
     */
    public static <T> T solve(T init, Function<T, Collection<T>> nextMoves, Predicate<T> goal) {
        return solve(init, nextMoves, goal, null);
    }

    /**
     * Applies a BFS search to the given State and returns the goal state when found.
     * @param init - initial state
     * @param nextMoves - function to return a set of valid moves from a given state
     * @param goal - tests if given state is goal state
     * @Param prune - accepts the current state and history and returns a pruned set of states; useful when non-trivial pruning is required. 
     * @return goal state when reached
     */
    public static <T> T solve(T init, Function<T, Collection<T>> nextMoves, Predicate<T> goal, BiFunction<Set<T>, Set<T>, Set<T>> prune) {
        T answer = null;

        Objects.requireNonNull(init);
        Objects.requireNonNull(nextMoves);
        Objects.requireNonNull(goal);

        Set<T> history = Sets.newLinkedHashSet();
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

                history.add(state);
                newStates.addAll((Collection<T>) nextMoves.apply(state));
                newStates.removeAll(history);
            }

            if (prune != null) {
                newStates = prune.apply(newStates, history);
            }

            currentStates = newStates;
        }

        return answer;
    }

    /**
     * 
     * @param init - initial state
     * @param nextMoves - function to return a set of valid moves from a given state
     * @param accumulate - tests if given state should be accumulated into answers
     * @return set of states which passed accumulator
     */
    public static <T extends State> Set<T> accumulate(T init, Function<T, Collection<T>> nextMoves, Predicate<T> accumulate) {
        Set<T> answers = Sets.newLinkedHashSet();

        Objects.requireNonNull(init);
        Objects.requireNonNull(nextMoves);

        Set<T> history = Sets.newLinkedHashSet();
        Set<T> currentStates = Sets.newLinkedHashSet();
        currentStates.add(init);

        while (!currentStates.isEmpty()) {
            Set<T> newStates = Sets.newLinkedHashSet();
            for (T state : currentStates) {

                if (accumulate.test(state)) {
                    answers.add(state);
                }

                history.add(state);
                newStates.addAll((Collection<T>) nextMoves.apply(state));
                newStates.removeAll(history);
            }

            currentStates = newStates;
        }

        return answers;
    }

}
