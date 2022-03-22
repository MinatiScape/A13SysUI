package kotlinx.coroutines;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
/* compiled from: CancellableContinuationImpl.kt */
/* loaded from: classes.dex */
public final class InvokeOnCancel extends CancelHandler {
    public final Function1<Throwable, Unit> handler;

    @Override // kotlin.jvm.functions.Function1
    public final /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
        invoke2(th);
        return Unit.INSTANCE;
    }

    @Override // kotlinx.coroutines.CancelHandlerBase
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(Throwable th) {
        this.handler.invoke(th);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("InvokeOnCancel[");
        m.append(DebugStringsKt.getClassSimpleName(this.handler));
        m.append('@');
        m.append(DebugStringsKt.getHexAddress(this));
        m.append(']');
        return m.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public InvokeOnCancel(Function1<? super Throwable, Unit> function1) {
        this.handler = function1;
    }
}
