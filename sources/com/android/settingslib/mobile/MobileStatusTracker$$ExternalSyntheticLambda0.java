package com.android.settingslib.mobile;

import android.os.Handler;
import java.util.concurrent.Executor;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class MobileStatusTracker$$ExternalSyntheticLambda0 implements Executor {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Handler f$0;

    @Override // java.util.concurrent.Executor
    public final void execute(Runnable runnable) {
        switch (this.$r8$classId) {
            case 0:
            default:
                this.f$0.post(runnable);
                return;
        }
    }

    public /* synthetic */ MobileStatusTracker$$ExternalSyntheticLambda0(Handler handler, int i) {
        this.$r8$classId = i;
        this.f$0 = handler;
    }
}
