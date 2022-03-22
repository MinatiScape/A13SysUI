package com.android.systemui.util.sensors;

import android.hardware.SensorManager;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class AsyncSensorManager$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ AsyncSensorManager$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                AsyncSensorManager asyncSensorManager = (AsyncSensorManager) this.f$0;
                Objects.requireNonNull(asyncSensorManager);
                asyncSensorManager.mInner.unregisterDynamicSensorCallback((SensorManager.DynamicSensorCallback) this.f$1);
                return;
            default:
                LegacySplitScreenController.SplitScreenImpl splitScreenImpl = (LegacySplitScreenController.SplitScreenImpl) this.f$0;
                Objects.requireNonNull(splitScreenImpl);
                LegacySplitScreenController.this.registerInSplitScreenListener((Consumer) this.f$1);
                return;
        }
    }
}
