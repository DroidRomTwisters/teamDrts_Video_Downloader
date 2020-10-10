package com.teamdrt.teamdrtdownloader.Utils;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class NumberUtils {

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "KB");
        suffixes.put(1_000_000L, "MB");
        suffixes.put(1_000_000_000L, "GB");
        suffixes.put(1_000_000_000_000L, "TB");
        suffixes.put(1_000_000_000_000_000L, "PB");
        suffixes.put(1_000_000_000_000_000_000L, "EB");
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }


}
