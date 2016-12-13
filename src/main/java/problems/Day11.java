package problems;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Day11 {

    private static final List<String> SAMPLE = Lists.newArrayList(
            "The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.",
            "The second floor contains a hydrogen generator.",
            "The third floor contains a lithium generator.",
            "The fourth floor contains nothing relevant.");

    private static final Map<String, String> ABBR = Maps.newHashMap();

    public static void main(String[] args) throws Exception {
        Path path = Paths.get("src/main/input/input11.txt");
        List<String> input = Files.readAllLines(path).stream()
                .map(StringUtils::stripToEmpty)
                .collect(Collectors.toList());

        System.out.println("Running Day11");
        problem1(input);

        // Add data to first floor for second part...
        String firstFloor = input.get(0);
        firstFloor += " and a " + "elerium generator";
        firstFloor += " and a " + "elerium-compatible microchip";
        firstFloor += " and a " + "dilithium generator";
        firstFloor += " and a " + "dilithium-compatible microchip";
        input.set(0, firstFloor);
        problem1(input);
    }

    private static class Floor {
        private int floorNumber = 0;
        private Set<String> gens = Sets.newLinkedHashSet();
        private Set<String> chips = Sets.newLinkedHashSet();

        protected Floor(int floorNumber) {
            this.floorNumber = floorNumber;
        }

        protected Floor(Floor floor) {
            this.floorNumber = floor.floorNumber;
            this.gens = Sets.newLinkedHashSet(floor.gens);
            this.chips = Sets.newLinkedHashSet(floor.chips);
        }

        public Set<String> getGens() {
            return Collections.unmodifiableSet(gens);
        }

        public Set<String> getChips() {
            return Collections.unmodifiableSet(chips);
        }

        public boolean isEmpty() {
            return gens.isEmpty() && chips.isEmpty();
        }

        public boolean isValid() {
            // If any chips are on the floor that don't have their generator, we're boned
            if (!gens.isEmpty()) {
                for (String chip : chips) {
                    if (!gens.contains(chip)) {
                        return false;
                    }
                }
            }

            return true;
        }

        @Override
        public int hashCode() {
            return Objects.hash(floorNumber, gens, chips);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Floor)) {
                return false;
            }

            Floor floor = (Floor) obj;
            return Objects.equals(this.floorNumber, floor.floorNumber) &&
                    Objects.equals(this.gens, floor.gens) &&
                    Objects.equals(this.chips, floor.chips);
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            str.append("F").append(floorNumber + 1).append(": ");

            for (Entry<String, String> pair : ABBR.entrySet()) {
                if (gens.contains(pair.getKey())) {
                    str.append(String.format("%5s ", pair.getValue() + "G"));
                }
                else {
                    str.append(String.format("%5s ", "."));
                }

                if (chips.contains(pair.getKey())) {
                    str.append(String.format("%-5s", pair.getValue() + "M"));
                }
                else {
                    str.append(String.format("%-5s", "."));
                }
            }

            return str.toString();

            //            return String.format("F%s: %5s %-5s %5s %-5s %5s %-5s %5s %-5s %5s %5s -- isValid=%s",
            //                    floorNumber + 1,
            //                    gens.contains("promethium") ? "PRG" : ".",
            //                    chips.contains("promethium") ? "PRC" : ".",
            //                    gens.contains("cobalt") ? "COG" : ".",
            //                    chips.contains("cobalt") ? "COC" : ".",
            //                    gens.contains("curium") ? "CUG" : ".",
            //                    chips.contains("curium") ? "CUC" : ".",
            //                    gens.contains("ruthenium") ? "RUG" : ".",
            //                    chips.contains("ruthenium") ? "RUC" : ".",
            //                    gens.contains("plutonium") ? "PLG" : ".",
            //                    chips.contains("plutonium") ? "PLC" : ".",
            //                    isValid());
        }
    }

    private static class State {
        private State parentState = null;
        private int currentFloor = 0;
        private List<Floor> floors;
        private String actions = "";

        public State(List<Floor> floors) {
            this(null, 0, floors);
        }

        private State(State parentState, int currentFloor, List<Floor> floors) {
            this.parentState = parentState;
            this.currentFloor = currentFloor;
            this.floors = floors;
        }

        public Set<State> nextStates() {
            Set<State> nextStates = Sets.newLinkedHashSet();

            // Move any one microchip or any two items up or down one floor from our current floor
            Floor floor = getFloor(this.currentFloor);

            // Elevator doesn't work if there's no chips!
            if (floor.getChips().isEmpty() && floor.getGens().isEmpty()) {
                throw new RuntimeException("I'M STUCK! What?!");
            }

            for (int dir : new int[] { 1, -1 }) {
                int nextFloor = currentFloor + dir;
                if (nextFloor < 0 || nextFloor >= floors.size()) {
                    continue;
                }

                for (String chip : floor.getChips()) {
                    for (String chip2 : floor.getChips()) {
                        State clonedState = this.cloneState(this, nextFloor);
                        clonedState.moveChip(chip, currentFloor, nextFloor);
                        clonedState.moveChip(chip2, currentFloor, nextFloor);
                        if (clonedState.isValid()) {
                            nextStates.add(clonedState);
                        }
                    }
                }

                for (String gen : floor.getGens()) {
                    for (String gen2 : floor.getGens()) {
                        State clonedState = this.cloneState(this, nextFloor);
                        clonedState.moveGen(gen, currentFloor, nextFloor);
                        clonedState.moveGen(gen2, currentFloor, nextFloor);
                        if (clonedState.isValid()) {
                            nextStates.add(clonedState);
                        }
                    }
                }

                if (!floor.getChips().isEmpty() && !floor.getGens().isEmpty()) {
                    for (String chip : floor.getChips()) {
                        for (String gen : floor.getGens()) {
                            State clonedState = this.cloneState(this, nextFloor);
                            clonedState.moveChip(chip, currentFloor, nextFloor);
                            clonedState.moveGen(gen, currentFloor, nextFloor);
                            if (clonedState.isValid()) {
                                nextStates.add(clonedState);
                            }
                        }
                    }
                }
            }

            return nextStates;
        }

        private void moveGen(String gen, int from, int to) {
            if (getFloor(from).gens.remove(gen)) {
                getFloor(to).gens.add(gen);
                if (actions.length() > 0) actions += "; ";
                actions += String.format("move gen=%s from [%s] to [%s]", gen, from + 1, to + 1);
            }
        }

        private void moveChip(String chip, int from, int to) {
            if (getFloor(from).chips.remove(chip)) {
                getFloor(to).chips.add(chip);
                if (actions.length() > 0) actions += "; ";
                actions += String.format("move chip=%s from [%s] to [%s]", chip, from + 1, to + 1);
            }
        }

        public int step() {
            int steps = 0;
            State state = this.parentState;
            while (state != null) {
                steps++;
                state = state.parentState;
            }
            return steps;
        }

        public Floor getFloor(int i) {
            return floors.get(i);
        }

        public State cloneState(State parentState, int currentFloor) {
            return new State(parentState, currentFloor, cloneFloors(this.floors));
        }

        public List<Floor> cloneFloors(List<Floor> floors) {
            List<Floor> clones = Lists.newArrayList();
            for (Floor floor : floors) {
                clones.add(new Floor(floor));
            }
            return clones;
        }

        public boolean isValid() {
            for (Floor floor : this.floors) {
                if (!floor.isValid()) {
                    return false;
                }
            }
            return true;
        }

        public boolean isDone() {
            for (int i = 0; i < floors.size() - 1; i++) {
                Floor floor = floors.get(i);
                if (!floor.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            return Objects.hash(currentFloor, floors);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof State)) {
                return false;
            }
            State state = (State) obj;
            return Objects.equals(state.currentFloor, this.currentFloor) &&
                    Objects.equals(state.floors, this.floors);
        }

        /**
         * Creates a "generic" version of the current state such that
         * any states which are functionally identical will generate the
         * same generic state.
         */
        public State createGenericState() {
            State clone = cloneState(null, currentFloor);
            Map<String, String> m = Maps.newLinkedHashMap();
            char n = 'A';
            for (Floor floor : floors) {
                for (String gen : floor.getGens()) {
                    m.put(gen, String.valueOf(n++));
                }
            }

            for (Floor floor : clone.floors) {
                for (String gen : Lists.newArrayList(floor.getGens())) {
                    floor.gens.remove(gen);
                    floor.gens.add(m.get(gen) + "G");
                }
                for (String chip : Lists.newArrayList(floor.getChips())) {
                    floor.chips.remove(chip);
                    floor.chips.add(m.get(chip) + "C");
                }
            }

            return clone;
        }

        @Override
        public String toString() {
            String floorsStr = floors.stream()
                    .sorted((f1, f2) -> Integer.compare(f2.floorNumber, f1.floorNumber))
                    .map(f -> String.format("   %s %s", f == getFloor(currentFloor) ? "E" : "-", f.toString()))
                    .collect(Collectors.joining(System.lineSeparator()));

            return String.format("State [step=%s, currentFloor=%s, actions=%s, hash=%s%n%s]",
                    step(),
                    currentFloor + 1,
                    actions,
                    hashCode(),
                    floorsStr);
        }

    }

    public static List<Floor> readFloors(List<String> input) {
        List<Floor> floors = Lists.newArrayList();

        int floorNum = 0;
        for (String line : input) {
            System.out.println(line);
            String[] words = line.split(" ");

            Floor floor = new Floor(floorNum++);
            floors.add(floor);

            for (int i = 0; i < words.length; i++) {
                if (words[i].equals("a")) {
                    String word = words[i + 1];
                    if (word.contains("compatible")) {
                        word = word.split("-")[0];
                        floor.chips.add(word);
                    }
                    else {
                        floor.gens.add(word);
                    }

                    ABBR.put(word, word.substring(0, 1).toUpperCase() + word.substring(1, 2).toLowerCase());
                }
            }
        }

        return floors;
    }

    protected static Set<State> pruneEquivalent(Set<State> states, Set<State> processedStates) {
        states.removeAll(processedStates);

        // Now we want to do some trickery... If we find effectively equivalent states in our history
        // we're going to prune those as well as adding them to our processed set... Effectively
        // equivalent states are states which basically look the same, except the *type* of chip/generator
        // pairs are are swapped between floors...

        Set<State> remove = Sets.newLinkedHashSet();
        for (State state : states) {
            State generic = state.createGenericState();

            if (!processedStates.add(generic)) {
                remove.add(state);
            }
        }
        states.removeAll(remove);

        return states;
    }

    public static void problem1(List<String> input) throws Exception {
        long startTime = System.nanoTime();
        List<Floor> floors = readFloors(input);

        State init = new State(floors);
        Set<State> states = Sets.newLinkedHashSet();
        states.add(init);

        Set<State> processedStates = Sets.newHashSet();

        System.out.println(init);
        System.out.println();

        outer:
        while (!states.isEmpty()) {
            Set<State> newStates = Collections.synchronizedSet(Sets.newLinkedHashSet());

            Optional<State> answer = states.stream().filter(State::isDone).findFirst();
            if (answer.isPresent()) {
                printAnswer(answer.get());
                break outer;
            }

            states.parallelStream().forEach(state -> {
                processedStates.add(state);
                newStates.addAll(state.nextStates());
            });

            states = pruneEquivalent(newStates, processedStates);
            System.out.println("Current step: " + states.iterator().next().step() + " [" + states.size() + "]");
        }

        long totalTime = System.nanoTime() - startTime;
        System.out.format("Total time: %.5ss%n", TimeUnit.NANOSECONDS.toMillis(totalTime) / 1000d);
    }

    protected static void printAnswer(State state) {
        List<State> prevStates = Lists.newArrayList();
        while (state != null) {
            prevStates.add(state);
            state = state.parentState;
        }
        Collections.reverse(prevStates);

        prevStates.forEach(s -> {
            try {
                System.out.println(" - " + s + System.lineSeparator());
                //Thread.sleep(1000);
            }
            catch (Exception e) {
            }
        });

        System.out.println(state);
    }

}
