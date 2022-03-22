package kotlin.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.RandomAccess;
import kotlin.jvm.functions.Function1;
import kotlin.sequences.Sequence;
/* compiled from: ReversedViews.kt */
/* loaded from: classes.dex */
public class CollectionsKt__ReversedViewsKt extends CollectionsKt__MutableCollectionsJVMKt {
    public static final boolean addAll(Collection collection, Collection collection2) {
        if (collection2 instanceof Collection) {
            return collection.addAll(collection2);
        }
        boolean z = false;
        for (Object obj : collection2) {
            if (collection.add(obj)) {
                z = true;
            }
        }
        return z;
    }

    public static final boolean removeAll(ArrayList arrayList, Function1 function1) {
        int i;
        if (!(arrayList instanceof RandomAccess)) {
            return filterInPlace$CollectionsKt__MutableCollectionsKt(arrayList, function1);
        }
        int lastIndex = SetsKt__SetsKt.getLastIndex(arrayList);
        if (lastIndex >= 0) {
            int i2 = 0;
            i = 0;
            while (true) {
                int i3 = i2 + 1;
                Object obj = arrayList.get(i2);
                if (!((Boolean) function1.invoke(obj)).booleanValue()) {
                    if (i != i2) {
                        arrayList.set(i, obj);
                    }
                    i++;
                }
                if (i2 == lastIndex) {
                    break;
                }
                i2 = i3;
            }
        } else {
            i = 0;
        }
        if (i >= arrayList.size()) {
            return false;
        }
        int lastIndex2 = SetsKt__SetsKt.getLastIndex(arrayList);
        if (i > lastIndex2) {
            return true;
        }
        while (true) {
            int i4 = lastIndex2 - 1;
            arrayList.remove(lastIndex2);
            if (lastIndex2 == i) {
                return true;
            }
            lastIndex2 = i4;
        }
    }

    public static final boolean filterInPlace$CollectionsKt__MutableCollectionsKt(Collection collection, Function1 function1) {
        Iterator it = collection.iterator();
        boolean z = false;
        while (it.hasNext()) {
            if (((Boolean) function1.invoke(it.next())).booleanValue()) {
                it.remove();
                z = true;
            }
        }
        return z;
    }

    public static final boolean addAll(Collection collection, Sequence sequence) {
        boolean z = false;
        for (Object obj : sequence) {
            if (collection.add(obj)) {
                z = true;
            }
        }
        return z;
    }
}
