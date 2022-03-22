package kotlin.collections;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
/* compiled from: Sets.kt */
/* loaded from: classes.dex */
public class SetsKt__SetsKt {
    public static final List listOf(Object... objArr) {
        if (objArr.length > 0) {
            return Arrays.asList(objArr);
        }
        return EmptyList.INSTANCE;
    }

    public static final Set setOf(Object... objArr) {
        if (objArr.length > 0) {
            return ArraysKt___ArraysKt.toSet(objArr);
        }
        return EmptySet.INSTANCE;
    }

    public static final void throwIndexOverflow() {
        throw new ArithmeticException("Index overflow has happened.");
    }

    public static final int getLastIndex(List list) {
        return list.size() - 1;
    }

    public static final List optimizeReadOnlyList(List list) {
        int size = list.size();
        if (size == 0) {
            return EmptyList.INSTANCE;
        }
        if (size != 1) {
            return list;
        }
        return Collections.singletonList(list.get(0));
    }
}
