package com.lss.l8springdata.sql;

public class QueryCounter {
    private static final ThreadLocal<Integer> counter = ThreadLocal.withInitial(() -> 0);

    public static void increment() {
        counter.set(counter.get() + 1);
    }

    public static int get() {
        return counter.get();
    }

    public static void reset() {
        counter.set(0);
    }
}
