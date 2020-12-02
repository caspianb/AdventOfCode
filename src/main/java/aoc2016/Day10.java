package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Day10 {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/aoc2016/input10.txt");
        List<String> input = Files.readAllLines(path).stream()
                .map(StringUtils::stripToEmpty)
                .collect(Collectors.toList());

        System.out.println("start...");
        problem1(input);
    }

    static class Bot {
        int botId;
        Integer low;
        Integer high;

        Bot(int botId) {
            this.botId = botId;
        }

        public void giveValue(int val) {
            if (low == null && high == null) {
                low = val;
            }
            else if (low == null && high != null) {
                low = Math.min(high, val);
                high = Math.max(high, val);
            }
            else if (high == null) {
                high = Math.max(low, val);
                low = Math.min(low, val);
            }
            else {
                System.out.println("BOT GOT TOO MANY VALUES?!?!?");
            }

            //System.out.println(" has " + this.toString());

            if (low != null && high != null && low > high) {
                throw new RuntimeException();
            }
        }

        @Override
        public String toString() {
            return "Bot [botId=" + botId + ", low=" + low + ", high=" + high + "]";
        }

    }

    private static Map<Integer, Bot> bots = Maps.newLinkedHashMap();

    private static Bot getBot(int botId) {
        Bot bot = bots.computeIfAbsent(botId, (b) -> new Bot(b));
        return bot;
    }

    public static void problem1(List<String> input) {
        ListMultimap<Integer, Integer> outputs = ArrayListMultimap.create();

        Set<String> processed = Sets.newHashSet();
        while (true) {
            for (String line : input) {
                String[] vals = line.split(" ");

                if (!processed.add(line)) {
                    continue;
                }
                else if (line.startsWith("value")) {
                    int val = Integer.valueOf(vals[1]);
                    int botId = Integer.valueOf(vals[5]);

                    Bot bot = getBot(botId);
                    bot.giveValue(val);
                }
                else if (line.startsWith("bot ")) {
                    int botId = Integer.valueOf(vals[1]);
                    int low = Integer.valueOf(vals[6]);
                    int high = Integer.valueOf(vals[11]);

                    Bot bot = getBot(botId);
                    if (bot.low == null || bot.high == null) {
                        processed.remove(line);
                        continue;
                    }

                    if (vals[5].equals("bot")) {
                        Bot l = getBot(low);
                        l.giveValue(bot.low);
                    }
                    else {
                        outputs.put(low, bot.low);
                    }
                    bot.low = null;

                    if (vals[10].equals("bot")) {
                        Bot h = getBot(high);
                        h.giveValue(bot.high);
                    }
                    else {
                        outputs.put(high, bot.high);
                    }
                    bot.high = null;

                    break;
                }
            }

            Bot answer = bots.values().stream()
                    .filter(b -> b.low != null && b.high != null && b.low == 17 && b.high == 61)
                    .findFirst().orElse(null);

            if (answer != null) {
                System.out.println(answer);
            }

            if (processed.size() == input.size()) break;
        }

        int p2 = outputs.get(0).get(0) *
                outputs.get(1).get(0) *
                outputs.get(2).get(0);

        System.out.println(p2);
    }

}
