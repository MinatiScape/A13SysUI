package com.google.android.systemui.fingerprint;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.IUdfpsHbmListener;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.Trace;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.UdfpsHbmProvider;
import com.android.systemui.util.concurrency.Execution;
import com.google.hardware.pixel.display.IDisplay;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UdfpsHbmController.kt */
/* loaded from: classes.dex */
public final class UdfpsHbmController implements UdfpsHbmProvider, DisplayManager.DisplayListener {
    public final AuthController authController;
    public final Executor biometricExecutor;
    public final Context context;
    public HbmRequest currentRequest;
    public final DisplayManager displayManager;
    public final Execution execution;
    public final UdfpsGhbmProvider ghbmProvider;
    public final UdfpsLhbmProvider lhbmProvider;
    public final Handler mainHandler;
    public final float peakRefreshRate;

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public final void onDisplayAdded(int i) {
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public final void onDisplayRemoved(int i) {
    }

    @Override // com.android.systemui.biometrics.UdfpsHbmProvider
    public final void disableHbm() {
        this.execution.isMainThread();
        Log.v("UdfpsHbmController", "disableHbm");
        Trace.beginSection("UdfpsHbmController.disableHbm");
        final HbmRequest hbmRequest = this.currentRequest;
        if (hbmRequest == null) {
            Log.w("UdfpsHbmController", "disableHbm | HBM is already disabled");
            return;
        }
        AuthController authController = this.authController;
        Objects.requireNonNull(authController);
        if (authController.mUdfpsHbmListener == null) {
            Log.e("UdfpsHbmController", "disableHbm | mDisplayManagerCallback is null");
        }
        Trace.beginAsyncSection("UdfpsHbmController.e2e.disableHbm", 0);
        this.displayManager.unregisterDisplayListener(this);
        this.currentRequest = null;
        if (hbmRequest.started) {
            hbmRequest.biometricExecutor.execute(new Runnable() { // from class: com.google.android.systemui.fingerprint.HbmRequest$disable$1
                public final /* synthetic */ Runnable $onHbmDisabled = null;

                @Override // java.lang.Runnable
                public final void run() {
                    HbmRequest hbmRequest2 = HbmRequest.this;
                    Objects.requireNonNull(hbmRequest2);
                    int i = hbmRequest2.hbmType;
                    if (i == 0) {
                        HbmRequest hbmRequest3 = HbmRequest.this;
                        UdfpsGhbmProvider udfpsGhbmProvider = hbmRequest3.ghbmProvider;
                        Objects.requireNonNull(hbmRequest3);
                        Surface surface = hbmRequest3.surface;
                        Intrinsics.checkNotNull(surface);
                        udfpsGhbmProvider.disableGhbm(surface);
                    } else if (i != 1) {
                        HbmRequest hbmRequest4 = HbmRequest.this;
                        Objects.requireNonNull(hbmRequest4);
                        Log.e("UdfpsHbmController", Intrinsics.stringPlus("doDisableHbm | unsupported HBM type: ", Integer.valueOf(hbmRequest4.hbmType)));
                    } else {
                        UdfpsLhbmProvider udfpsLhbmProvider = HbmRequest.this.lhbmProvider;
                        Objects.requireNonNull(udfpsLhbmProvider);
                        Log.v("UdfpsLhbmProvider", "disableLhbm");
                        IDisplay displayHal = udfpsLhbmProvider.getDisplayHal();
                        if (displayHal == null) {
                            Log.e("UdfpsLhbmProvider", "disableLhbm | displayHal is null");
                        } else {
                            try {
                                displayHal.setLhbmState(false);
                            } catch (RemoteException e) {
                                Log.e("UdfpsLhbmProvider", "disableLhbm | RemoteException", e);
                            }
                        }
                    }
                    Trace.endAsyncSection("UdfpsHbmController.e2e.disableHbm", 0);
                    final HbmRequest hbmRequest5 = HbmRequest.this;
                    hbmRequest5.mainHandler.post(new Runnable() { // from class: com.google.android.systemui.fingerprint.HbmRequest$disable$1.1
                        @Override // java.lang.Runnable
                        public final void run() {
                            try {
                                AuthController authController2 = HbmRequest.this.authController;
                                Objects.requireNonNull(authController2);
                                IUdfpsHbmListener iUdfpsHbmListener = authController2.mUdfpsHbmListener;
                                Intrinsics.checkNotNull(iUdfpsHbmListener);
                                HbmRequest hbmRequest6 = HbmRequest.this;
                                Objects.requireNonNull(hbmRequest6);
                                int i2 = hbmRequest6.hbmType;
                                HbmRequest hbmRequest7 = HbmRequest.this;
                                Objects.requireNonNull(hbmRequest7);
                                iUdfpsHbmListener.onHbmDisabled(i2, hbmRequest7.displayId);
                                Log.v("UdfpsHbmController", "disableHbm | requested to unfreeze the refresh rate");
                            } catch (RemoteException e2) {
                                Log.e("UdfpsHbmController", "disableHbm", e2);
                            }
                        }
                    });
                    Runnable runnable = this.$onHbmDisabled;
                    if (runnable != null) {
                        HbmRequest.this.mainHandler.post(runnable);
                    } else {
                        Log.w("UdfpsHbmController", "doDisableHbm | onHbmDisabled is null");
                    }
                }
            });
        }
        Trace.endSection();
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0050  */
    @Override // com.android.systemui.biometrics.UdfpsHbmProvider
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void enableHbm(int r17, android.view.Surface r18, com.android.systemui.biometrics.UdfpsView$doIlluminate$1 r19) {
        /*
            r16 = this;
            r1 = r16
            r0 = r17
            com.android.systemui.util.concurrency.Execution r2 = r1.execution
            r2.isMainThread()
            java.lang.String r12 = "UdfpsHbmController"
            java.lang.String r13 = "enableHbm"
            android.util.Log.v(r12, r13)
            java.lang.String r2 = "UdfpsHbmController.enableHbm"
            android.os.Trace.beginSection(r2)
            r14 = 0
            r15 = 1
            if (r0 == 0) goto L_0x0029
            if (r0 == r15) goto L_0x0029
            java.lang.Integer r2 = java.lang.Integer.valueOf(r17)
            java.lang.String r3 = "enableHbm | unsupported hbmType: "
            java.lang.String r2 = kotlin.jvm.internal.Intrinsics.stringPlus(r3, r2)
            android.util.Log.e(r12, r2)
            goto L_0x004b
        L_0x0029:
            if (r0 != 0) goto L_0x0033
            if (r18 != 0) goto L_0x0033
            java.lang.String r2 = "enableHbm | surface must be non-null for GHBM"
            android.util.Log.e(r12, r2)
            goto L_0x004b
        L_0x0033:
            com.android.systemui.biometrics.AuthController r2 = r1.authController
            java.util.Objects.requireNonNull(r2)
            android.hardware.fingerprint.IUdfpsHbmListener r2 = r2.mUdfpsHbmListener
            if (r2 != 0) goto L_0x0042
            java.lang.String r2 = "enableHbm | mDisplayManagerCallback is null"
            android.util.Log.e(r12, r2)
            goto L_0x004b
        L_0x0042:
            com.google.android.systemui.fingerprint.HbmRequest r2 = r1.currentRequest
            if (r2 == 0) goto L_0x004d
            java.lang.String r2 = "enableHbm | HBM is already requested"
            android.util.Log.e(r12, r2)
        L_0x004b:
            r2 = r14
            goto L_0x004e
        L_0x004d:
            r2 = r15
        L_0x004e:
            if (r2 == 0) goto L_0x00c5
            java.lang.String r2 = "UdfpsHbmController.e2e.enableHbm"
            android.os.Trace.beginAsyncSection(r2, r14)
            com.google.android.systemui.fingerprint.HbmRequest r11 = new com.google.android.systemui.fingerprint.HbmRequest
            android.os.Handler r3 = r1.mainHandler
            java.util.concurrent.Executor r4 = r1.biometricExecutor
            com.android.systemui.biometrics.AuthController r5 = r1.authController
            com.google.android.systemui.fingerprint.UdfpsGhbmProvider r6 = r1.ghbmProvider
            com.google.android.systemui.fingerprint.UdfpsLhbmProvider r7 = r1.lhbmProvider
            android.content.Context r2 = r1.context
            int r10 = r2.getDisplayId()
            r2 = r11
            r8 = r10
            r9 = r17
            r14 = r10
            r10 = r18
            r15 = r11
            r11 = r19
            r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10, r11)
            r1.currentRequest = r15
            android.hardware.display.DisplayManager r2 = r1.displayManager
            android.os.Handler r3 = r1.mainHandler
            r2.registerDisplayListener(r1, r3)
            com.android.systemui.biometrics.AuthController r2 = r1.authController     // Catch: RemoteException -> 0x0098
            java.util.Objects.requireNonNull(r2)     // Catch: RemoteException -> 0x0098
            android.hardware.fingerprint.IUdfpsHbmListener r2 = r2.mUdfpsHbmListener     // Catch: RemoteException -> 0x0098
            if (r2 != 0) goto L_0x0087
            goto L_0x008a
        L_0x0087:
            r2.onHbmEnabled(r0, r14)     // Catch: RemoteException -> 0x0098
        L_0x008a:
            java.lang.String r2 = "enableHbm | request freeze refresh rate for type: "
            java.lang.Integer r0 = java.lang.Integer.valueOf(r17)     // Catch: RemoteException -> 0x0098
            java.lang.String r0 = kotlin.jvm.internal.Intrinsics.stringPlus(r2, r0)     // Catch: RemoteException -> 0x0098
            android.util.Log.v(r12, r0)     // Catch: RemoteException -> 0x0098
            goto L_0x009c
        L_0x0098:
            r0 = move-exception
            android.util.Log.e(r12, r13, r0)
        L_0x009c:
            int r0 = r15.displayId
            android.hardware.display.DisplayManager r2 = r1.displayManager
            android.view.Display r0 = r2.getDisplay(r0)
            float r0 = r0.getRefreshRate()
            int r2 = r15.hbmType
            if (r2 == 0) goto L_0x00b4
            r3 = 1
            if (r2 == r3) goto L_0x00b1
            r2 = 0
            goto L_0x00b7
        L_0x00b1:
            float r2 = r1.peakRefreshRate
            goto L_0x00b7
        L_0x00b4:
            r3 = 1
            r2 = 1114636288(0x42700000, float:60.0)
        L_0x00b7:
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 != 0) goto L_0x00bd
            r14 = r3
            goto L_0x00be
        L_0x00bd:
            r14 = 0
        L_0x00be:
            if (r14 == 0) goto L_0x00c5
            int r0 = r15.displayId
            r1.onDisplayChanged(r0)
        L_0x00c5:
            android.os.Trace.endSection()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.fingerprint.UdfpsHbmController.enableHbm(int, android.view.Surface, com.android.systemui.biometrics.UdfpsView$doIlluminate$1):void");
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public final void onDisplayChanged(int i) {
        float f;
        boolean z;
        this.execution.isMainThread();
        final HbmRequest hbmRequest = this.currentRequest;
        if (hbmRequest == null) {
            Log.w("UdfpsHbmController", "onDisplayChanged | mHbmRequest is null");
        } else if (i != hbmRequest.displayId) {
            StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("onDisplayChanged | displayId: ", i, " != ");
            m.append(hbmRequest.displayId);
            Log.w("UdfpsHbmController", m.toString());
        } else {
            float refreshRate = this.displayManager.getDisplay(i).getRefreshRate();
            int i2 = hbmRequest.hbmType;
            if (i2 == 0) {
                f = 60.0f;
            } else if (i2 != 1) {
                f = 0.0f;
            } else {
                f = this.peakRefreshRate;
            }
            if (refreshRate == f) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                Log.w("UdfpsHbmController", "onDisplayChanged | hz: " + refreshRate + " != " + f);
                if (hbmRequest.finishedStarting) {
                    Log.e("UdfpsHbmController", "onDisplayChanged | refresh rate changed while HBM is enabled.");
                    return;
                }
                return;
            }
            Log.v("UdfpsHbmController", Intrinsics.stringPlus("onDisplayChanged | froze the refresh rate at hz: ", Float.valueOf(refreshRate)));
            if (!hbmRequest.started) {
                hbmRequest.started = true;
                hbmRequest.biometricExecutor.execute(new Runnable() { // from class: com.google.android.systemui.fingerprint.HbmRequest$enable$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        HbmRequest hbmRequest2 = HbmRequest.this;
                        Objects.requireNonNull(hbmRequest2);
                        int i3 = hbmRequest2.hbmType;
                        if (i3 == 0) {
                            HbmRequest hbmRequest3 = HbmRequest.this;
                            UdfpsGhbmProvider udfpsGhbmProvider = hbmRequest3.ghbmProvider;
                            Objects.requireNonNull(hbmRequest3);
                            Surface surface = hbmRequest3.surface;
                            Intrinsics.checkNotNull(surface);
                            udfpsGhbmProvider.enableGhbm(surface);
                        } else if (i3 != 1) {
                            HbmRequest hbmRequest4 = HbmRequest.this;
                            Objects.requireNonNull(hbmRequest4);
                            Log.e("UdfpsHbmController", Intrinsics.stringPlus("doEnableHbm | unsupported HBM type: ", Integer.valueOf(hbmRequest4.hbmType)));
                        } else {
                            UdfpsLhbmProvider udfpsLhbmProvider = HbmRequest.this.lhbmProvider;
                            Objects.requireNonNull(udfpsLhbmProvider);
                            Log.v("UdfpsLhbmProvider", "enableLhbm");
                            IDisplay displayHal = udfpsLhbmProvider.getDisplayHal();
                            if (displayHal == null) {
                                Log.e("UdfpsLhbmProvider", "enableLhbm | displayHal is null");
                            } else {
                                try {
                                    displayHal.setLhbmState(true);
                                } catch (RemoteException e) {
                                    Log.e("UdfpsLhbmProvider", "enableLhbm | RemoteException", e);
                                }
                            }
                        }
                        Trace.endAsyncSection("UdfpsHbmController.e2e.enableHbm", 0);
                        try {
                            HbmRequest hbmRequest5 = HbmRequest.this;
                            Runnable runnable = hbmRequest5.onHbmEnabled;
                            if (runnable != null) {
                                hbmRequest5.mainHandler.post(runnable);
                            } else {
                                Log.w("UdfpsHbmController", "doEnableHbm | onHbmEnabled is null");
                            }
                        } finally {
                            HbmRequest.this.finishedStarting = true;
                        }
                    }
                });
            }
        }
    }

    public UdfpsHbmController(Context context, Execution execution, Handler handler, Executor executor, UdfpsGhbmProvider udfpsGhbmProvider, UdfpsLhbmProvider udfpsLhbmProvider, AuthController authController, DisplayManager displayManager) {
        Float f;
        float f2;
        this.context = context;
        this.execution = execution;
        this.mainHandler = handler;
        this.biometricExecutor = executor;
        this.ghbmProvider = udfpsGhbmProvider;
        this.lhbmProvider = udfpsLhbmProvider;
        this.authController = authController;
        this.displayManager = displayManager;
        Display.Mode[] supportedModes = displayManager.getDisplay(context.getDisplayId()).getSupportedModes();
        ArrayList arrayList = new ArrayList(supportedModes.length);
        int length = supportedModes.length;
        int i = 0;
        while (i < length) {
            Display.Mode mode = supportedModes[i];
            i++;
            arrayList.add(Float.valueOf(mode.getRefreshRate()));
        }
        Iterator it = arrayList.iterator();
        if (!it.hasNext()) {
            f = null;
        } else {
            float floatValue = ((Number) it.next()).floatValue();
            while (it.hasNext()) {
                floatValue = Math.max(floatValue, ((Number) it.next()).floatValue());
            }
            f = Float.valueOf(floatValue);
        }
        if (f == null) {
            f2 = 0.0f;
        } else {
            f2 = f.floatValue();
        }
        this.peakRefreshRate = f2;
        Settings.Secure.putIntForUser(this.context.getContentResolver(), "com.android.systemui.biometrics.UdfpsSurfaceView.hbmType", !SystemProperties.getBoolean("persist.fingerprint.ghbm", false) ? 1 : 0, -2);
    }
}
