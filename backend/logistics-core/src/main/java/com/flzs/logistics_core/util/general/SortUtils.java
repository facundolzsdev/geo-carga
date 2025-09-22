package com.flzs.logistics_core.util.general;

import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class SortUtils {

    public static <T, U extends Comparable<? super U>> List<T> sortBy(
            List<T> list, Function<T, U> keyExtractor) {
        return list.stream()
                .sorted(Comparator.comparing(keyExtractor))
                .collect(Collectors.toList());
    }
}