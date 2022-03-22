package com.android.systemui.doze;

import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda1;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.UdfpsController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DozeTriggers$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ DozeTriggers f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ float f$2;
    public final /* synthetic */ float[] f$3;

    public /* synthetic */ DozeTriggers$$ExternalSyntheticLambda1(DozeTriggers dozeTriggers, float f, float f2, float[] fArr) {
        this.f$0 = dozeTriggers;
        this.f$1 = f;
        this.f$2 = f2;
        this.f$3 = fArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        DozeTriggers dozeTriggers = this.f$0;
        float f = this.f$1;
        float f2 = this.f$2;
        float[] fArr = this.f$3;
        Objects.requireNonNull(dozeTriggers);
        AuthController authController = dozeTriggers.mAuthController;
        final int i = (int) f;
        final int i2 = (int) f2;
        final float f3 = fArr[3];
        final float f4 = fArr[4];
        Objects.requireNonNull(authController);
        final UdfpsController udfpsController = authController.mUdfpsController;
        if (udfpsController == null || udfpsController.mIsAodInterruptActive) {
            return;
        }
        if (!udfpsController.mKeyguardUpdateMonitor.isFingerprintDetectionRunning()) {
            udfpsController.mKeyguardViewManager.showBouncer(true);
            return;
        }
        Runnable udfpsController$$ExternalSyntheticLambda0 = new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                UdfpsController udfpsController2 = UdfpsController.this;
                int i3 = i;
                int i4 = i2;
                float f5 = f4;
                float f6 = f3;
                Objects.requireNonNull(udfpsController2);
                udfpsController2.mIsAodInterruptActive = true;
                udfpsController2.mCancelAodTimeoutAction = udfpsController2.mFgExecutor.executeDelayed(new ScreenDecorations$$ExternalSyntheticLambda1(udfpsController2, 2), 1000L);
                udfpsController2.onFingerDown(i3, i4, f5, f6);
            }
        };
        udfpsController.mAodInterruptRunnable = udfpsController$$ExternalSyntheticLambda0;
        if (udfpsController.mScreenOn) {
            udfpsController$$ExternalSyntheticLambda0.run();
            udfpsController.mAodInterruptRunnable = null;
        }
    }
}
