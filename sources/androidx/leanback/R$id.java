package androidx.leanback;

import com.google.common.collect.Ordering;
import com.google.common.collect.SortedIterable;
import java.util.Comparator;
import java.util.Objects;
import java.util.SortedSet;
/* loaded from: classes.dex */
public final class R$id {
    public static boolean hasSameComparator(Comparator comparator, Iterable iterable) {
        Object obj;
        Objects.requireNonNull(comparator);
        Objects.requireNonNull(iterable);
        if (iterable instanceof SortedSet) {
            obj = ((SortedSet) iterable).comparator();
            if (obj == null) {
                obj = Ordering.natural();
            }
        } else if (!(iterable instanceof SortedIterable)) {
            return false;
        } else {
            obj = ((SortedIterable) iterable).comparator();
        }
        return comparator.equals(obj);
    }
}
