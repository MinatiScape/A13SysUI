package com.android.systemui.util.wakelock;

import android.app.PendingIntent;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WakeLock$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ WakeLock$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                Runnable runnable = (Runnable) this.f$0;
                WakeLock wakeLock = (WakeLock) this.f$1;
                try {
                    runnable.run();
                    return;
                } finally {
                    wakeLock.release("wrap");
                }
            default:
                ((PendingIntent) this.f$0).unregisterCancelListener((PendingIntent.CancelListener) this.f$1);
                return;
        }
    }
}
