package kotlinx.coroutines.internal;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
/* compiled from: LockFreeLinkedList.kt */
/* loaded from: classes.dex */
public final class Removed {
    public final LockFreeLinkedListNode ref;

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Removed[");
        m.append(this.ref);
        m.append(']');
        return m.toString();
    }

    public Removed(LockFreeLinkedListNode lockFreeLinkedListNode) {
        this.ref = lockFreeLinkedListNode;
    }
}
