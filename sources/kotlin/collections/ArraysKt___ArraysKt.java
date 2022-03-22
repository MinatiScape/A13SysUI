package kotlin.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.jvm.internal.ArrayIterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.EmptySequence;
import kotlin.sequences.Sequence;
/* compiled from: _Arrays.kt */
/* loaded from: classes.dex */
public class ArraysKt___ArraysKt extends ArraysKt__ArraysKt {
    public static final <T> Sequence<T> asSequence(final T[] tArr) {
        boolean z;
        if (tArr.length == 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return EmptySequence.INSTANCE;
        }
        return new Sequence<T>() { // from class: kotlin.collections.ArraysKt___ArraysKt$asSequence$$inlined$Sequence$1
            @Override // kotlin.sequences.Sequence
            public final Iterator<T> iterator() {
                return new ArrayIterator(tArr);
            }
        };
    }

    public static final <T> boolean contains(T[] tArr, T t) {
        int i;
        if (t == null) {
            int length = tArr.length;
            i = 0;
            while (i < length) {
                i++;
                if (tArr[i] == null) {
                    break;
                }
            }
            i = -1;
        } else {
            int length2 = tArr.length;
            int i2 = 0;
            while (i2 < length2) {
                i2++;
                if (Intrinsics.areEqual(t, tArr[i2])) {
                    i = i2;
                    break;
                }
            }
            i = -1;
        }
        return i >= 0;
    }

    public static final <T extends Comparable<? super T>> T maxOrNull(T[] tArr) {
        boolean z;
        int i = 1;
        if (tArr.length == 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return null;
        }
        T t = tArr[0];
        int length = tArr.length - 1;
        if (1 <= length) {
            while (true) {
                int i2 = i + 1;
                T t2 = tArr[i];
                if (t.compareTo(t2) < 0) {
                    t = t2;
                }
                if (i == length) {
                    break;
                }
                i = i2;
            }
        }
        return t;
    }

    public static final <T> Set<T> toSet(T[] tArr) {
        int length = tArr.length;
        if (length == 0) {
            return EmptySet.INSTANCE;
        }
        int i = 0;
        if (length == 1) {
            return Collections.singleton(tArr[0]);
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet(MapsKt__MapsJVMKt.mapCapacity(tArr.length));
        int length2 = tArr.length;
        while (i < length2) {
            T t = tArr[i];
            i++;
            linkedHashSet.add(t);
        }
        return linkedHashSet;
    }

    public static Object[] copyInto$default(Object[] objArr, Object[] objArr2, int i, int i2, int i3, int i4) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = objArr.length;
        }
        System.arraycopy(objArr, i2, objArr2, i, i3 - i2);
        return objArr2;
    }

    public static final ArrayList filterNotNull(Object[] objArr) {
        ArrayList arrayList = new ArrayList();
        int length = objArr.length;
        int i = 0;
        while (i < length) {
            Object obj = objArr[i];
            i++;
            if (obj != null) {
                arrayList.add(obj);
            }
        }
        return arrayList;
    }

    public static final boolean contains(int[] iArr, int i) {
        int length = iArr.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                i2 = -1;
                break;
            }
            i2++;
            if (i == iArr[i2]) {
                break;
            }
        }
        return i2 >= 0;
    }
}
