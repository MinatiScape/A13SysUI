package com.android.systemui.util.concurrency;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Looper;
/* compiled from: Execution.kt */
/* loaded from: classes.dex */
public final class ExecutionImpl implements Execution {
    public final Looper mainLooper = Looper.getMainLooper();

    @Override // com.android.systemui.util.concurrency.Execution
    public final void assertIsMainThread() {
        if (!this.mainLooper.isCurrentThread()) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("should be called from the main thread. Main thread name=");
            m.append((Object) this.mainLooper.getThread().getName());
            m.append(" Thread.currentThread()=");
            m.append((Object) Thread.currentThread().getName());
            throw new IllegalStateException(m.toString());
        }
    }

    @Override // com.android.systemui.util.concurrency.Execution
    public final boolean isMainThread() {
        return this.mainLooper.isCurrentThread();
    }
}
