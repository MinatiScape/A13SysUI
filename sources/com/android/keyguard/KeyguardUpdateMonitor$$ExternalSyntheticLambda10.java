package com.android.keyguard;

import android.hardware.face.FaceManager;
import java.util.Objects;
import java.util.function.Supplier;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardUpdateMonitor$$ExternalSyntheticLambda10 implements Supplier {
    public final /* synthetic */ KeyguardUpdateMonitor f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ KeyguardUpdateMonitor$$ExternalSyntheticLambda10(KeyguardUpdateMonitor keyguardUpdateMonitor, int i) {
        this.f$0 = keyguardUpdateMonitor;
        this.f$1 = i;
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        boolean z;
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.f$0;
        int i = this.f$1;
        Objects.requireNonNull(keyguardUpdateMonitor);
        FaceManager faceManager = keyguardUpdateMonitor.mFaceManager;
        if (faceManager == null || !faceManager.isHardwareDetected() || !keyguardUpdateMonitor.mFaceManager.hasEnrolledTemplates(i) || !keyguardUpdateMonitor.mBiometricEnabledForUser.get(i)) {
            z = false;
        } else {
            z = true;
        }
        return Boolean.valueOf(z);
    }
}
