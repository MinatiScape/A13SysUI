package com.android.systemui;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.camera2.CameraManager;
import android.hardware.display.DisplayManager;
import android.os.Looper;
import android.util.PathParser;
import android.view.WindowManager;
import androidx.lifecycle.Lifecycle;
import com.android.keyguard.AdminSecondaryLockScreenController;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.navigationbar.NavigationBar;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.BatteryControllerImpl;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda0;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.pip.phone.PipResizeGestureHandler;
import java.util.Objects;
import java.util.function.Consumer;
import kotlin.text.StringsKt__StringsKt;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenDecorations$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ScreenDecorations$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                final ScreenDecorations screenDecorations = (ScreenDecorations) this.f$0;
                boolean z = ScreenDecorations.DEBUG_DISABLE_SCREEN_DECORATIONS;
                Objects.requireNonNull(screenDecorations);
                screenDecorations.mRotation = screenDecorations.mContext.getDisplay().getRotation();
                String uniqueId = screenDecorations.mContext.getDisplay().getUniqueId();
                screenDecorations.mDisplayUniqueId = uniqueId;
                screenDecorations.mIsRoundedCornerMultipleRadius = ScreenDecorations.isRoundedCornerMultipleRadius(screenDecorations.mContext, uniqueId);
                screenDecorations.mWindowManager = (WindowManager) screenDecorations.mContext.getSystemService(WindowManager.class);
                screenDecorations.mDisplayManager = (DisplayManager) screenDecorations.mContext.getSystemService(DisplayManager.class);
                screenDecorations.mHwcScreenDecorationSupport = screenDecorations.mContext.getDisplay().getDisplayDecorationSupport();
                screenDecorations.updateRoundedCornerDrawable();
                screenDecorations.updateRoundedCornerRadii();
                screenDecorations.setupDecorations();
                if (screenDecorations.mContext.getResources().getBoolean(2131034127)) {
                    Context context = screenDecorations.mContext;
                    DelayableExecutor delayableExecutor = screenDecorations.mExecutor;
                    Object systemService = context.getSystemService("camera");
                    Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.hardware.camera2.CameraManager");
                    CameraManager cameraManager = (CameraManager) systemService;
                    Resources resources = context.getResources();
                    String string = resources.getString(2131952137);
                    try {
                        CameraAvailabilityListener cameraAvailabilityListener = new CameraAvailabilityListener(cameraManager, PathParser.createPathFromPathData(StringsKt__StringsKt.trim(string).toString()), resources.getString(2131952143), resources.getString(2131952132), delayableExecutor);
                        screenDecorations.mCameraListener = cameraAvailabilityListener;
                        cameraAvailabilityListener.listeners.add(screenDecorations.mCameraTransitionCallback);
                        CameraAvailabilityListener cameraAvailabilityListener2 = screenDecorations.mCameraListener;
                        Objects.requireNonNull(cameraAvailabilityListener2);
                        cameraAvailabilityListener2.cameraManager.registerAvailabilityCallback(cameraAvailabilityListener2.executor, cameraAvailabilityListener2.availabilityCallback);
                    } catch (Throwable th) {
                        throw new IllegalArgumentException("Invalid protection path", th);
                    }
                }
                DisplayManager.DisplayListener displayListener = new DisplayManager.DisplayListener() { // from class: com.android.systemui.ScreenDecorations.3
                    @Override // android.hardware.display.DisplayManager.DisplayListener
                    public final void onDisplayAdded(int i) {
                    }

                    @Override // android.hardware.display.DisplayManager.DisplayListener
                    public final void onDisplayRemoved(int i) {
                    }

                    /* JADX WARN: Code restructure failed: missing block: B:55:0x0118, code lost:
                        if (kotlin.jvm.internal.Intrinsics.areEqual(r1, r2) == false) goto L_0x011a;
                     */
                    @Override // android.hardware.display.DisplayManager.DisplayListener
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct add '--show-bad-code' argument
                    */
                    public final void onDisplayChanged(int r10) {
                        /*
                            Method dump skipped, instructions count: 326
                            To view this dump add '--comments-level debug' option
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.ScreenDecorations.AnonymousClass3.onDisplayChanged(int):void");
                    }
                };
                screenDecorations.mDisplayListener = displayListener;
                screenDecorations.mDisplayManager.registerDisplayListener(displayListener, screenDecorations.mHandler);
                screenDecorations.updateOrientation();
                return;
            case 1:
                AdminSecondaryLockScreenController.AnonymousClass2 r7 = (AdminSecondaryLockScreenController.AnonymousClass2) this.f$0;
                int i = AdminSecondaryLockScreenController.AnonymousClass2.$r8$clinit;
                Objects.requireNonNull(r7);
                AdminSecondaryLockScreenController.this.dismiss(KeyguardUpdateMonitor.getCurrentUser());
                return;
            case 2:
                ((UdfpsController) this.f$0).onCancelUdfps();
                return;
            case 3:
                NavigationBar navigationBar = (NavigationBar) this.f$0;
                Objects.requireNonNull(navigationBar);
                navigationBar.getBarTransitions().setAutoDim(true);
                return;
            case 4:
                ScreenshotController.AnonymousClass1 r0 = ScreenshotController.SCREENSHOT_REMOTE_RUNNER;
                ((ScreenshotController) this.f$0).requestScrollCapture();
                return;
            case 5:
                AccessPointControllerImpl accessPointControllerImpl = (AccessPointControllerImpl) this.f$0;
                boolean z2 = AccessPointControllerImpl.DEBUG;
                Objects.requireNonNull(accessPointControllerImpl);
                accessPointControllerImpl.mLifecycle.setCurrentState(Lifecycle.State.CREATED);
                return;
            case FalsingManager.VERSION /* 6 */:
                StatusBar statusBar = (StatusBar) this.f$0;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                statusBar.mNotificationsController.requestNotificationUpdate("onBubbleExpandChanged");
                statusBar.updateScrimController();
                return;
            case 7:
                BatteryControllerImpl batteryControllerImpl = (BatteryControllerImpl) this.f$0;
                boolean z3 = BatteryControllerImpl.DEBUG;
                Objects.requireNonNull(batteryControllerImpl);
                synchronized (batteryControllerImpl.mFetchCallbacks) {
                    batteryControllerImpl.mEstimate = null;
                    if (batteryControllerImpl.mEstimates.isHybridNotificationEnabled()) {
                        batteryControllerImpl.updateEstimate();
                    }
                }
                batteryControllerImpl.mFetchingEstimate = false;
                batteryControllerImpl.mMainHandler.post(new StandardWifiEntry$$ExternalSyntheticLambda0(batteryControllerImpl, 8));
                return;
            case 8:
                ((InternetDialogController.WifiEntryConnectCallback) ((WifiEntry.ConnectCallback) this.f$0)).onConnectResult(3);
                return;
            case 9:
                int i2 = BubbleStackView.FLYOUT_HIDE_AFTER;
                ((Consumer) this.f$0).accept(Boolean.TRUE);
                return;
            default:
                PipResizeGestureHandler pipResizeGestureHandler = (PipResizeGestureHandler) this.f$0;
                Objects.requireNonNull(pipResizeGestureHandler);
                pipResizeGestureHandler.mInputEventReceiver = new PipResizeGestureHandler.PipResizeInputEventReceiver(pipResizeGestureHandler.mInputMonitor.getInputChannel(), Looper.myLooper());
                return;
        }
    }
}
