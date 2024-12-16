package uk.gov.justice.laa.crime.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SortUtils {

    public static <T, U extends Comparable<? super U>> void sortListWithComparing(List<T> t, Function<T, U> compFunction, Function<T, U> thenCompFunc, Comparator<U> comparator) {
        if (t != null) {
            t.sort(Comparator.comparing(compFunction, comparator).thenComparing(thenCompFunc, comparator));
        }
    }

    public static <U extends Comparable<? super U>> Comparator<U> getComparator() {
        return Comparator.naturalOrder();
    }

    public static <U extends Comparable<? super U>> Comparator<U> getReverseComparator() {
        return Comparator.reverseOrder();
    }
}
