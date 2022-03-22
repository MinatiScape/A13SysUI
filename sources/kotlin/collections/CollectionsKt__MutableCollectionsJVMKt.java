package kotlin.collections;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/* compiled from: MutableCollectionsJVM.kt */
/* loaded from: classes.dex */
public class CollectionsKt__MutableCollectionsJVMKt extends CollectionsKt__IteratorsJVMKt {
    public static final <T> void sortWith(List<T> list, Comparator<? super T> comparator) {
        if (list.size() > 1) {
            Collections.sort(list, comparator);
        }
    }
}
