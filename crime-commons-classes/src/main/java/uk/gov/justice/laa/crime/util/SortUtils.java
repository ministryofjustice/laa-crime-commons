package uk.gov.justice.laa.crime.util;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SortUtils {

    public static <T, U extends Comparable<? super U>> void sortList(
            List<T> list,
            Function<T, U> keyExtractor,
            boolean reverse
    ) {
        if (Objects.nonNull(list)) {
            Comparator<T> comparator = Comparator.comparing(
                    keyExtractor,
                    reverse ? Comparator.reverseOrder() : Comparator.naturalOrder()
            );
            list.sort(comparator);
        }
    }

    public static <T, U extends Comparable<? super U>> void sortList(
            List<T> list,
            Function<T, U> primary,
            Function<T, U> secondary,
            boolean reverse
    ) {
        if (Objects.nonNull(list)) {
            Comparator<U> order = reverse ? Comparator.reverseOrder() : Comparator.naturalOrder();
            list.sort(Comparator.comparing(primary, order).thenComparing(secondary, order));
        }
    }
}
