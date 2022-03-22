package kotlinx.coroutines;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import kotlin.Unit;
import kotlinx.coroutines.channels.SendElement;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
/* compiled from: CancellableContinuation.kt */
/* loaded from: classes.dex */
public final class RemoveOnCancel extends BeforeResumeCancelHandler {
    public final LockFreeLinkedListNode node;

    @Override // kotlin.jvm.functions.Function1
    public final /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
        invoke2(th);
        return Unit.INSTANCE;
    }

    @Override // kotlinx.coroutines.CancelHandlerBase
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(Throwable th) {
        this.node.remove();
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("RemoveOnCancel[");
        m.append(this.node);
        m.append(']');
        return m.toString();
    }

    public RemoveOnCancel(SendElement sendElement) {
        this.node = sendElement;
    }
}
