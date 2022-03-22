package kotlinx.coroutines.internal;

import java.util.ArrayList;
import kotlinx.coroutines.DebugKt;
/* compiled from: InlineList.kt */
/* loaded from: classes.dex */
public final class InlineList<E> {
    /* renamed from: plus-FjFbRPM  reason: not valid java name */
    public static final Object m186plusFjFbRPM(Object obj, LockFreeLinkedListNode lockFreeLinkedListNode) {
        boolean z = DebugKt.DEBUG;
        if (obj == null) {
            return lockFreeLinkedListNode;
        }
        if (obj instanceof ArrayList) {
            ((ArrayList) obj).add(lockFreeLinkedListNode);
            return obj;
        }
        ArrayList arrayList = new ArrayList(4);
        arrayList.add(obj);
        arrayList.add(lockFreeLinkedListNode);
        return arrayList;
    }
}
