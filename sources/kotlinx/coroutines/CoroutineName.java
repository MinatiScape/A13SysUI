package kotlinx.coroutines;

import java.util.Objects;
import kotlin.coroutines.AbstractCoroutineContextElement;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CoroutineName.kt */
/* loaded from: classes.dex */
public final class CoroutineName extends AbstractCoroutineContextElement {
    public static final Key Key = new Key();

    /* compiled from: CoroutineName.kt */
    /* loaded from: classes.dex */
    public static final class Key implements CoroutineContext.Key<CoroutineName> {
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CoroutineName)) {
            return false;
        }
        Objects.requireNonNull((CoroutineName) obj);
        return Intrinsics.areEqual(null, null);
    }

    public final int hashCode() {
        throw null;
    }

    public final String toString() {
        return "CoroutineName(null)";
    }
}
