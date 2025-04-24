package com.lss.utils;

import java.util.HashMap;
import java.util.Map;

public class ObjectTracker {

    private static final Map<String, Integer> counts = new HashMap<>();

    public static void created(Class<?> clazz) {
        counts.merge(clazz.getSimpleName(), 1, Integer::sum);
    }

    public static void total() {
        System.out.println("\n--- Object Counters ---");
        counts.forEach((k, v) -> System.out.println(k + ": " + v));
        System.out.println("\n--- Total ---");
        System.out.println(counts.values().stream().reduce(0, Integer::sum));
    }
}
