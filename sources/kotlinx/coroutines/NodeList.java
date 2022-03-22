package kotlinx.coroutines;

import androidx.activity.result.ActivityResultRegistry$3$$ExternalSyntheticOutline0;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
/* compiled from: JobSupport.kt */
/* loaded from: classes.dex */
public final class NodeList extends LockFreeLinkedListHead implements Incomplete {
    @Override // kotlinx.coroutines.Incomplete
    public final NodeList getList() {
        return this;
    }

    @Override // kotlinx.coroutines.Incomplete
    public final boolean isActive() {
        return true;
    }

    public final String getString(String str) {
        StringBuilder m = ActivityResultRegistry$3$$ExternalSyntheticOutline0.m("List{", str, "}[");
        boolean z = true;
        for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) getNext(); !Intrinsics.areEqual(lockFreeLinkedListNode, this); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
            if (lockFreeLinkedListNode instanceof JobNode) {
                JobNode jobNode = (JobNode) lockFreeLinkedListNode;
                if (z) {
                    z = false;
                } else {
                    m.append(", ");
                }
                m.append(jobNode);
            }
        }
        m.append("]");
        return m.toString();
    }

    @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
    public final String toString() {
        if (DebugKt.DEBUG) {
            return getString("Active");
        }
        return super.toString();
    }
}
