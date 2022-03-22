package com.google.android.systemui.fingerprint;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.view.Surface;
import com.android.keyguard.FontInterpolator$VarFontKey$$ExternalSyntheticOutline0;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.UdfpsView$doIlluminate$1;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UdfpsHbmController.kt */
/* loaded from: classes.dex */
public final class HbmRequest {
    public final AuthController authController;
    public final Executor biometricExecutor;
    public final int displayId;
    public boolean finishedStarting;
    public final UdfpsGhbmProvider ghbmProvider;
    public final int hbmType;
    public final UdfpsLhbmProvider lhbmProvider;
    public final Handler mainHandler;
    public final Runnable onHbmEnabled;
    public boolean started;
    public final Surface surface;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HbmRequest)) {
            return false;
        }
        HbmRequest hbmRequest = (HbmRequest) obj;
        return Intrinsics.areEqual(this.mainHandler, hbmRequest.mainHandler) && Intrinsics.areEqual(this.biometricExecutor, hbmRequest.biometricExecutor) && Intrinsics.areEqual(this.authController, hbmRequest.authController) && Intrinsics.areEqual(this.ghbmProvider, hbmRequest.ghbmProvider) && Intrinsics.areEqual(this.lhbmProvider, hbmRequest.lhbmProvider) && this.displayId == hbmRequest.displayId && this.hbmType == hbmRequest.hbmType && Intrinsics.areEqual(this.surface, hbmRequest.surface) && Intrinsics.areEqual(this.onHbmEnabled, hbmRequest.onHbmEnabled);
    }

    public final int hashCode() {
        int i;
        int hashCode = this.biometricExecutor.hashCode();
        int hashCode2 = this.authController.hashCode();
        int hashCode3 = this.ghbmProvider.hashCode();
        int m = FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.hbmType, FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.displayId, (this.lhbmProvider.hashCode() + ((hashCode3 + ((hashCode2 + ((hashCode + (this.mainHandler.hashCode() * 31)) * 31)) * 31)) * 31)) * 31, 31), 31);
        Surface surface = this.surface;
        int i2 = 0;
        if (surface == null) {
            i = 0;
        } else {
            i = surface.hashCode();
        }
        int i3 = (m + i) * 31;
        Runnable runnable = this.onHbmEnabled;
        if (runnable != null) {
            i2 = runnable.hashCode();
        }
        return i3 + i2;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("HbmRequest(mainHandler=");
        m.append(this.mainHandler);
        m.append(", biometricExecutor=");
        m.append(this.biometricExecutor);
        m.append(", authController=");
        m.append(this.authController);
        m.append(", ghbmProvider=");
        m.append(this.ghbmProvider);
        m.append(", lhbmProvider=");
        m.append(this.lhbmProvider);
        m.append(", displayId=");
        m.append(this.displayId);
        m.append(", hbmType=");
        m.append(this.hbmType);
        m.append(", surface=");
        m.append(this.surface);
        m.append(", onHbmEnabled=");
        m.append(this.onHbmEnabled);
        m.append(')');
        return m.toString();
    }

    public HbmRequest(Handler handler, Executor executor, AuthController authController, UdfpsGhbmProvider udfpsGhbmProvider, UdfpsLhbmProvider udfpsLhbmProvider, int i, int i2, Surface surface, UdfpsView$doIlluminate$1 udfpsView$doIlluminate$1) {
        this.mainHandler = handler;
        this.biometricExecutor = executor;
        this.authController = authController;
        this.ghbmProvider = udfpsGhbmProvider;
        this.lhbmProvider = udfpsLhbmProvider;
        this.displayId = i;
        this.hbmType = i2;
        this.surface = surface;
        this.onHbmEnabled = udfpsView$doIlluminate$1;
    }
}
