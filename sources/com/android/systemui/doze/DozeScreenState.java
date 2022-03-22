package com.android.systemui.doze;

import android.os.Handler;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.util.wakelock.SettableWakeLock;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeScreenState implements DozeMachine.Part {
    public static final boolean DEBUG = DozeService.DEBUG;
    public final AuthController mAuthController;
    public final AnonymousClass1 mAuthControllerCallback;
    public final DozeHost mDozeHost;
    public final DozeLog mDozeLog;
    public final DozeScreenBrightness mDozeScreenBrightness;
    public final DozeMachine.Service mDozeService;
    public final Handler mHandler;
    public final DozeParameters mParameters;
    public UdfpsController mUdfpsController;
    public final Provider<UdfpsController> mUdfpsControllerProvider;
    public SettableWakeLock mWakeLock;
    public final DozeScreenState$$ExternalSyntheticLambda0 mApplyPendingScreenState = new DozeScreenState$$ExternalSyntheticLambda0(this, 0);
    public int mPendingScreenState = 0;

    public final void applyScreenState(int i) {
        if (i != 0) {
            if (DEBUG) {
                Log.d("DozeScreenState", "setDozeScreenState(" + i + ")");
            }
            this.mDozeService.setDozeScreenState(i);
            if (i == 3) {
                this.mDozeScreenBrightness.updateBrightnessAndReady(false);
            }
            this.mPendingScreenState = 0;
            this.mWakeLock.setAcquired(false);
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public final void destroy() {
        AuthController authController = this.mAuthController;
        AnonymousClass1 r1 = this.mAuthControllerCallback;
        Objects.requireNonNull(authController);
        authController.mCallbacks.remove(r1);
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0016, code lost:
        if (r2.getDisplayNeedsBlanking() != false) goto L_0x0024;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0020, code lost:
        if (r2.mControlScreenOffAnimation != false) goto L_0x0022;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0148 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x00e1  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x00fa  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x013d  */
    /* JADX WARN: Type inference failed for: r13v8, types: [com.android.systemui.doze.DozeScreenState$$ExternalSyntheticLambda1] */
    @Override // com.android.systemui.doze.DozeMachine.Part
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void transitionTo(com.android.systemui.doze.DozeMachine.State r12, com.android.systemui.doze.DozeMachine.State r13) {
        /*
            Method dump skipped, instructions count: 364
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeScreenState.transitionTo(com.android.systemui.doze.DozeMachine$State, com.android.systemui.doze.DozeMachine$State):void");
    }

    public final void updateUdfpsController() {
        if (this.mAuthController.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser())) {
            this.mUdfpsController = this.mUdfpsControllerProvider.mo144get();
        } else {
            this.mUdfpsController = null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.doze.DozeScreenState$1, com.android.systemui.biometrics.AuthController$Callback] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public DozeScreenState(com.android.systemui.doze.DozeMachine.Service r3, android.os.Handler r4, com.android.systemui.doze.DozeHost r5, com.android.systemui.statusbar.phone.DozeParameters r6, com.android.systemui.util.wakelock.WakeLock r7, com.android.systemui.biometrics.AuthController r8, javax.inject.Provider<com.android.systemui.biometrics.UdfpsController> r9, com.android.systemui.doze.DozeLog r10, com.android.systemui.doze.DozeScreenBrightness r11) {
        /*
            r2 = this;
            r2.<init>()
            com.android.systemui.doze.DozeScreenState$$ExternalSyntheticLambda0 r0 = new com.android.systemui.doze.DozeScreenState$$ExternalSyntheticLambda0
            r1 = 0
            r0.<init>(r2, r1)
            r2.mApplyPendingScreenState = r0
            r2.mPendingScreenState = r1
            com.android.systemui.doze.DozeScreenState$1 r0 = new com.android.systemui.doze.DozeScreenState$1
            r0.<init>()
            r2.mAuthControllerCallback = r0
            r2.mDozeService = r3
            r2.mHandler = r4
            r2.mParameters = r6
            r2.mDozeHost = r5
            com.android.systemui.util.wakelock.SettableWakeLock r3 = new com.android.systemui.util.wakelock.SettableWakeLock
            java.lang.String r4 = "DozeScreenState"
            r3.<init>(r7, r4)
            r2.mWakeLock = r3
            r2.mAuthController = r8
            r2.mUdfpsControllerProvider = r9
            r2.mDozeLog = r10
            r2.mDozeScreenBrightness = r11
            r2.updateUdfpsController()
            com.android.systemui.biometrics.UdfpsController r2 = r2.mUdfpsController
            if (r2 != 0) goto L_0x0037
            r8.addCallback(r0)
        L_0x0037:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeScreenState.<init>(com.android.systemui.doze.DozeMachine$Service, android.os.Handler, com.android.systemui.doze.DozeHost, com.android.systemui.statusbar.phone.DozeParameters, com.android.systemui.util.wakelock.WakeLock, com.android.systemui.biometrics.AuthController, javax.inject.Provider, com.android.systemui.doze.DozeLog, com.android.systemui.doze.DozeScreenBrightness):void");
    }
}
