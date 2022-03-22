package kotlinx.coroutines;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import kotlin.Unit;
/* compiled from: CancellableContinuation.kt */
/* loaded from: classes.dex */
public final class DisposeOnCancel extends CancelHandler {
    public final DisposableHandle handle;

    @Override // kotlin.jvm.functions.Function1
    public final /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
        invoke2(th);
        return Unit.INSTANCE;
    }

    @Override // kotlinx.coroutines.CancelHandlerBase
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(Throwable th) {
        this.handle.dispose();
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("DisposeOnCancel[");
        m.append(this.handle);
        m.append(']');
        return m.toString();
    }

    public DisposeOnCancel(DisposableHandle disposableHandle) {
        this.handle = disposableHandle;
    }
}
