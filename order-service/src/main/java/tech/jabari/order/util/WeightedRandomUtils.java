package tech.jabari.order.util;

import java.util.Arrays;

public class WeightedRandomUtils {
    public static int choose(double[] weights) {
        double totalWeight = Arrays.stream(weights).sum();
        double random = Math.random() * totalWeight;
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i];
            if (random <= sum) {
                return i;
            }
        }
        return weights.length - 1;
    }
}