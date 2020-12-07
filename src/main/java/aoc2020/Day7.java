package aoc2020;

import lombok.RequiredArgsConstructor;
import util.ResourceReader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;

public class Day7 {

    @RequiredArgsConstructor
    static class Bag {
        final String name;
        Map<Bag, Integer> contains = new HashMap<>();

        boolean containsBag(Bag bag) {
            return contains.containsKey(bag) ||
                    contains.keySet().stream().anyMatch(b -> b.containsBag(bag));
        }

        int countContained() {
            int c1 = this.contains.values().stream().mapToInt(i -> i).sum();
            int c2 = contains.entrySet().stream().mapToInt(e -> e.getKey().countContained() * e.getValue()).sum();
            return c1 + c2;
        }
    }

    public static void main(String[] args) {
        var input = ResourceReader.readLines("aoc2020/day7.txt");
        Map<String, Bag> bagsMap = buildBags(input);

        var goldBag = bagsMap.get("shiny gold");
        System.out.println(bagsMap.values().stream().filter(b -> b.containsBag(goldBag)).count());
        System.out.println(goldBag.countContained());
    }

    static Map<String, Bag> buildBags(List<String> input) {
        Map<String, Bag> bagsMap = new HashMap<>();

        for (String line : input) {
            String bagName = line.split(" contain ")[0].replaceAll("bags", "").trim();
            Bag bag = bagsMap.computeIfAbsent(bagName, Bag::new);
            Map<String, Integer> contains = Arrays.stream(line.split(" contain ")[1].split(", "))
                    .map(s -> s.replaceAll("bags?\\.?", "").trim())
                    .map(s -> s.split(" ", 2))
                    .collect(Collectors.toMap(s -> s[1], s -> NumberUtils.toInt(s[0])));

            contains.forEach((n, i) -> {
                if (i == 0) return;
                Bag b = bagsMap.computeIfAbsent(n, Bag::new);
                bag.contains.put(b, i);
            });
        }

        return bagsMap;
    }

}
