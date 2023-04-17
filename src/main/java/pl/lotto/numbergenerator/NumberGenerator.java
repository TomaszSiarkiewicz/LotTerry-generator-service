package pl.lotto.numbergenerator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class NumberGenerator {
    private final Random random;

    public NumberGenerator() {
        random = new Random();
    }

     Set<Integer> generate() {
        Set<Integer> drawnNumbers = new HashSet<>();
        while (drawnNumbers.size() < 6) {
            drawnNumbers.add(random.nextInt(99) + 1);
        }
        return drawnNumbers;
    }
}
