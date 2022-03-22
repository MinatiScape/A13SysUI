package kotlin.collections;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: IndexedValue.kt */
/* loaded from: classes.dex */
public final class IndexedValue<T> {
    public final int index;
    public final T value;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IndexedValue)) {
            return false;
        }
        IndexedValue indexedValue = (IndexedValue) obj;
        return this.index == indexedValue.index && Intrinsics.areEqual(this.value, indexedValue.value);
    }

    public final int hashCode() {
        int i = this.index * 31;
        T t = this.value;
        return i + (t == null ? 0 : t.hashCode());
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("IndexedValue(index=");
        m.append(this.index);
        m.append(", value=");
        m.append(this.value);
        m.append(')');
        return m.toString();
    }

    public IndexedValue(int i, T t) {
        this.index = i;
        this.value = t;
    }
}
