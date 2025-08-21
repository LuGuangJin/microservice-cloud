
package tech.jabari.order.util;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * 平滑加权随机工具类
 */
public class SmoothedWeightedRandomUtils {
    private static final double SMOOTH_FACTOR = 0.2; // 平滑系数
    private static final double MIN_WEIGHT = 0.1;    // 最小权重阈值

    public static int choose(double[] originalWeights) {
        // 使用原子引用保证线程安全
        AtomicReferenceArray<Double> currentWeights =
                new AtomicReferenceArray<>(Arrays.stream(originalWeights)
                        .boxed().toArray(Double[]::new));

        double totalWeight = Arrays.stream(originalWeights).sum();
        double random = Math.random() * totalWeight;
        double sum = 0;
        int selectedIndex = originalWeights.length - 1;

        for (int i = 0; i < currentWeights.length(); i++) {
            sum += currentWeights.get(i);
            if (random <= sum) {
                selectedIndex = i;
                break;
            }
        }

        // 平滑调整权重
        for (int i = 0; i < currentWeights.length(); i++) {
            double newWeight;
            if (i == selectedIndex) {
                newWeight = Math.max(
                        currentWeights.get(i) * (1 - SMOOTH_FACTOR),
                        originalWeights[i] * MIN_WEIGHT);
            } else {
                newWeight = Math.min(
                        currentWeights.get(i) * (1 + SMOOTH_FACTOR),
                        originalWeights[i] * 2); // 上限设为原始权重2倍
            }
            currentWeights.set(i, newWeight);
        }

        return selectedIndex;
    }
}
