package com.lss;

import java.util.HashMap;
import java.util.Map;

public class ObjectTracker {

    private static final Map<String, Integer> counters = new HashMap<>();

    public static void created(Class<?> clazz) {
        String name = clazz.getSimpleName();
        counters.merge(name, 1, Integer::sum);
    }

    public static void print() {
        System.out.println("\n Об'єкти, що були створені:");
        counters.forEach((name, count) ->
                System.out.printf(" - %s: %d%n", name, count));
    }

    public static int total() {
        return counters.values().stream().mapToInt(i -> i).sum();
    }
}
