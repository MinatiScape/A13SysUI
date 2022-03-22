package com.android.systemui.statusbar.phone;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.LockIconViewController;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.classifier.BrightLineFalsingManager$$ExternalSyntheticLambda1;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeLog;
import com.android.systemui.doze.DozeReceiver;
import com.android.systemui.doze.DozeScreenState$$ExternalSyntheticLambda1;
import com.android.systemui.doze.DozeTriggers;
import com.android.systemui.doze.DozeUi;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.PulseExpansionHandler;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DozeServiceHost implements DozeHost {
    public View mAmbientIndicationContainer;
    public boolean mAnimateWakeup;
    public final Lazy<AssistManager> mAssistManagerLazy;
    public final AuthController mAuthController;
    public final BatteryController mBatteryController;
    public final Lazy<BiometricUnlockController> mBiometricUnlockControllerLazy;
    public final DeviceProvisionedController mDeviceProvisionedController;
    public final DozeLog mDozeLog;
    public final DozeScrimController mDozeScrimController;
    public boolean mDozingRequested;
    public final HeadsUpManagerPhone mHeadsUpManagerPhone;
    public boolean mIgnoreTouchWhilePulsing;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final NotificationIconAreaController mNotificationIconAreaController;
    public NotificationPanelViewController mNotificationPanel;
    public final NotificationShadeWindowController mNotificationShadeWindowController;
    public NotificationShadeWindowViewController mNotificationShadeWindowViewController;
    public final NotificationWakeUpCoordinator mNotificationWakeUpCoordinator;
    public Runnable mPendingScreenOffCallback;
    public final PowerManager mPowerManager;
    public final PulseExpansionHandler mPulseExpansionHandler;
    public boolean mPulsing;
    public final ScrimController mScrimController;
    public StatusBar mStatusBar;
    public StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public final SysuiStatusBarStateController mStatusBarStateController;
    public boolean mSuppressed;
    public final WakefulnessLifecycle mWakefulnessLifecycle;
    public final ArrayList<DozeHost.Callback> mCallbacks = new ArrayList<>();
    @VisibleForTesting
    public boolean mWakeLockScreenPerformsAuth = SystemProperties.getBoolean("persist.sysui.wake_performs_auth", true);

    /* renamed from: com.android.systemui.statusbar.phone.DozeServiceHost$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass1 implements DozeHost.PulseCallback {
        public final /* synthetic */ DozeHost.PulseCallback val$callback;
        public final /* synthetic */ boolean val$passiveAuthInterrupt;

        public AnonymousClass1(DozeUi.AnonymousClass2 r2, boolean z) {
            this.val$callback = r2;
            this.val$passiveAuthInterrupt = z;
        }

        @Override // com.android.systemui.doze.DozeHost.PulseCallback
        public final void onPulseFinished() {
            DozeServiceHost.this.mPulsing = false;
            this.val$callback.onPulseFinished();
            DozeServiceHost.this.mStatusBar.updateNotificationPanelTouchState();
            DozeServiceHost.this.mScrimController.setWakeLockScreenSensorActive(false);
            setPulsing(false);
        }

        @Override // com.android.systemui.doze.DozeHost.PulseCallback
        public final void onPulseStarted() {
            this.val$callback.onPulseStarted();
            DozeServiceHost.this.mStatusBar.updateNotificationPanelTouchState();
            setPulsing(true);
        }

        public final void setPulsing(boolean z) {
            boolean z2;
            StatusBarKeyguardViewManager statusBarKeyguardViewManager = DozeServiceHost.this.mStatusBarKeyguardViewManager;
            Objects.requireNonNull(statusBarKeyguardViewManager);
            if (statusBarKeyguardViewManager.mPulsing != z) {
                statusBarKeyguardViewManager.mPulsing = z;
                statusBarKeyguardViewManager.updateStates();
            }
            NotificationPanelViewController notificationPanelViewController = DozeServiceHost.this.mNotificationPanel;
            Objects.requireNonNull(notificationPanelViewController);
            notificationPanelViewController.mPulsing = z;
            if (notificationPanelViewController.mDozeParameters.getDisplayNeedsBlanking() || !notificationPanelViewController.mDozeParameters.getAlwaysOn()) {
                z2 = false;
            } else {
                z2 = true;
            }
            if (z2) {
                notificationPanelViewController.mAnimateNextPositionUpdate = true;
            }
            if (!notificationPanelViewController.mPulsing && !notificationPanelViewController.mDozing) {
                notificationPanelViewController.mAnimateNextPositionUpdate = false;
            }
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationPanelViewController.mNotificationStackScrollLayoutController;
            Objects.requireNonNull(notificationStackScrollLayoutController);
            NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
            Objects.requireNonNull(notificationStackScrollLayout);
            if (notificationStackScrollLayout.mPulsing || z) {
                notificationStackScrollLayout.mPulsing = z;
                AmbientState ambientState = notificationStackScrollLayout.mAmbientState;
                Objects.requireNonNull(ambientState);
                ambientState.mPulsing = z;
                NotificationSwipeHelper notificationSwipeHelper = notificationStackScrollLayout.mSwipeHelper;
                Objects.requireNonNull(notificationSwipeHelper);
                notificationSwipeHelper.mPulsing = z;
                notificationStackScrollLayout.updateNotificationAnimationStates();
                notificationStackScrollLayout.updateAlgorithmHeightAndPadding();
                notificationStackScrollLayout.updateContentHeight();
                notificationStackScrollLayout.requestChildrenUpdate();
                notificationStackScrollLayout.notifyHeightChangeListener(null, z2);
            }
            DozeServiceHost.this.mStatusBarStateController.setPulsing(z);
            DozeServiceHost dozeServiceHost = DozeServiceHost.this;
            dozeServiceHost.mIgnoreTouchWhilePulsing = false;
            KeyguardUpdateMonitor keyguardUpdateMonitor = dozeServiceHost.mKeyguardUpdateMonitor;
            if (keyguardUpdateMonitor != null && this.val$passiveAuthInterrupt) {
                if (KeyguardUpdateMonitor.DEBUG) {
                    Objects.requireNonNull(keyguardUpdateMonitor);
                    Log.d("KeyguardUpdateMonitor", "onAuthInterruptDetected(" + z + ")");
                }
                if (keyguardUpdateMonitor.mAuthInterruptActive != z) {
                    keyguardUpdateMonitor.mAuthInterruptActive = z;
                    keyguardUpdateMonitor.updateFaceListeningState(2);
                    keyguardUpdateMonitor.requestActiveUnlock();
                }
            }
            DozeServiceHost.this.mStatusBar.updateScrimController();
            PulseExpansionHandler pulseExpansionHandler = DozeServiceHost.this.mPulseExpansionHandler;
            Objects.requireNonNull(pulseExpansionHandler);
            pulseExpansionHandler.mPulsing = z;
            NotificationWakeUpCoordinator notificationWakeUpCoordinator = DozeServiceHost.this.mNotificationWakeUpCoordinator;
            Objects.requireNonNull(notificationWakeUpCoordinator);
            notificationWakeUpCoordinator.pulsing = z;
            if (z) {
                notificationWakeUpCoordinator.updateNotificationVisibility(notificationWakeUpCoordinator.shouldAnimateVisibility(), false);
            }
        }
    }

    public DozeServiceHost(DozeLog dozeLog, PowerManager powerManager, WakefulnessLifecycle wakefulnessLifecycle, SysuiStatusBarStateController sysuiStatusBarStateController, DeviceProvisionedController deviceProvisionedController, HeadsUpManagerPhone headsUpManagerPhone, BatteryController batteryController, ScrimController scrimController, Lazy lazy, Lazy lazy2, DozeScrimController dozeScrimController, KeyguardUpdateMonitor keyguardUpdateMonitor, PulseExpansionHandler pulseExpansionHandler, NotificationShadeWindowController notificationShadeWindowController, NotificationWakeUpCoordinator notificationWakeUpCoordinator, AuthController authController, NotificationIconAreaController notificationIconAreaController) {
        this.mDozeLog = dozeLog;
        this.mPowerManager = powerManager;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mHeadsUpManagerPhone = headsUpManagerPhone;
        this.mBatteryController = batteryController;
        this.mScrimController = scrimController;
        this.mBiometricUnlockControllerLazy = lazy;
        this.mAssistManagerLazy = lazy2;
        this.mDozeScrimController = dozeScrimController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mPulseExpansionHandler = pulseExpansionHandler;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mNotificationWakeUpCoordinator = notificationWakeUpCoordinator;
        this.mAuthController = authController;
        this.mNotificationIconAreaController = notificationIconAreaController;
    }

    @Override // com.android.systemui.doze.DozeHost
    public final void cancelGentleSleep() {
        this.mPendingScreenOffCallback = null;
        ScrimController scrimController = this.mScrimController;
        Objects.requireNonNull(scrimController);
        if (scrimController.mState == ScrimState.OFF) {
            this.mStatusBar.updateScrimController();
        }
    }

    @Override // com.android.systemui.doze.DozeHost
    public final void extendPulse(int i) {
        boolean z;
        HeadsUpManagerPhone headsUpManagerPhone;
        if (i == 8) {
            this.mScrimController.setWakeLockScreenSensorActive(true);
        }
        DozeScrimController dozeScrimController = this.mDozeScrimController;
        Objects.requireNonNull(dozeScrimController);
        if (dozeScrimController.mPulseCallback != null) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            Objects.requireNonNull(this.mHeadsUpManagerPhone);
            if (!headsUpManagerPhone.mAlertEntries.isEmpty()) {
                HeadsUpManagerPhone headsUpManagerPhone2 = this.mHeadsUpManagerPhone;
                Objects.requireNonNull(headsUpManagerPhone2);
                HeadsUpManagerPhone.HeadsUpEntryPhone headsUpEntryPhone = (HeadsUpManagerPhone.HeadsUpEntryPhone) headsUpManagerPhone2.getTopHeadsUpEntry();
                if (headsUpEntryPhone != null && !headsUpEntryPhone.extended) {
                    headsUpEntryPhone.extended = true;
                    headsUpEntryPhone.updateEntry(false);
                    return;
                }
                return;
            }
        }
        DozeScrimController dozeScrimController2 = this.mDozeScrimController;
        Objects.requireNonNull(dozeScrimController2);
        dozeScrimController2.mHandler.removeCallbacks(dozeScrimController2.mPulseOut);
    }

    @Override // com.android.systemui.doze.DozeHost
    public final void onSlpiTap(float f, float f2) {
        View view;
        if (f > 0.0f && f2 > 0.0f && (view = this.mAmbientIndicationContainer) != null && view.getVisibility() == 0) {
            int[] iArr = new int[2];
            this.mAmbientIndicationContainer.getLocationOnScreen(iArr);
            float f3 = f - iArr[0];
            float f4 = f2 - iArr[1];
            if (0.0f <= f3 && f3 <= this.mAmbientIndicationContainer.getWidth() && 0.0f <= f4 && f4 <= this.mAmbientIndicationContainer.getHeight()) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                MotionEvent obtain = MotionEvent.obtain(elapsedRealtime, elapsedRealtime, 0, f, f2, 0);
                this.mAmbientIndicationContainer.dispatchTouchEvent(obtain);
                obtain.recycle();
                MotionEvent obtain2 = MotionEvent.obtain(elapsedRealtime, elapsedRealtime, 1, f, f2, 0);
                this.mAmbientIndicationContainer.dispatchTouchEvent(obtain2);
                obtain2.recycle();
            }
        }
    }

    @Override // com.android.systemui.doze.DozeHost
    public final void pulseWhileDozing(DozeUi.AnonymousClass2 r4, int i) {
        boolean z;
        if (i == 5) {
            this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 4, "com.android.systemui:LONG_PRESS");
            this.mAssistManagerLazy.get().startAssist(new Bundle());
            return;
        }
        if (i == 8) {
            this.mScrimController.setWakeLockScreenSensorActive(true);
        }
        if (i != 8 || !this.mWakeLockScreenPerformsAuth) {
            z = false;
        } else {
            z = true;
        }
        this.mPulsing = true;
        this.mDozeScrimController.pulse(new AnonymousClass1(r4, z), i);
        this.mStatusBar.updateScrimController();
    }

    @Override // com.android.systemui.doze.DozeHost
    public final void addCallback(DozeTriggers.AnonymousClass1 r1) {
        this.mCallbacks.add(r1);
    }

    @Override // com.android.systemui.doze.DozeHost
    public final void dozeTimeTick() {
        NotificationPanelViewController notificationPanelViewController = this.mNotificationPanel;
        Objects.requireNonNull(notificationPanelViewController);
        LockIconViewController lockIconViewController = notificationPanelViewController.mLockIconViewController;
        Objects.requireNonNull(lockIconViewController);
        lockIconViewController.updateBurnInOffsets();
        notificationPanelViewController.mKeyguardBottomArea.dozeTimeTick();
        notificationPanelViewController.mKeyguardStatusViewController.dozeTimeTick();
        if (notificationPanelViewController.mInterpolatedDarkAmount > 0.0f) {
            notificationPanelViewController.positionClockAndNotifications(false);
        }
        this.mAuthController.dozeTimeTick();
        NotificationShadeWindowViewController notificationShadeWindowViewController = this.mNotificationShadeWindowViewController;
        Objects.requireNonNull(notificationShadeWindowViewController);
        notificationShadeWindowViewController.mLowLightClockController.ifPresent(BrightLineFalsingManager$$ExternalSyntheticLambda1.INSTANCE$1);
        View view = this.mAmbientIndicationContainer;
        if (view instanceof DozeReceiver) {
            ((DozeReceiver) view).dozeTimeTick();
        }
    }

    @Override // com.android.systemui.doze.DozeHost
    public final boolean isBlockingDoze() {
        boolean z;
        BiometricUnlockController biometricUnlockController = this.mBiometricUnlockControllerLazy.get();
        Objects.requireNonNull(biometricUnlockController);
        BiometricUnlockController.PendingAuthenticated pendingAuthenticated = biometricUnlockController.mPendingAuthenticated;
        if (pendingAuthenticated == null || !biometricUnlockController.mUpdateMonitor.isUnlockingWithBiometricAllowed(pendingAuthenticated.isStrongBiometric) || biometricUnlockController.mPendingAuthenticated.userId != KeyguardUpdateMonitor.getCurrentUser()) {
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            return false;
        }
        Log.i("StatusBar", "Blocking AOD because fingerprint has authenticated");
        return true;
    }

    @Override // com.android.systemui.doze.DozeHost
    public final boolean isPowerSaveActive() {
        return this.mBatteryController.isAodPowerSave();
    }

    @Override // com.android.systemui.doze.DozeHost
    public final boolean isProvisioned() {
        if (!this.mDeviceProvisionedController.isDeviceProvisioned() || !this.mDeviceProvisionedController.isCurrentUserSetup()) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.doze.DozeHost
    public final boolean isPulsingBlocked() {
        BiometricUnlockController biometricUnlockController = this.mBiometricUnlockControllerLazy.get();
        Objects.requireNonNull(biometricUnlockController);
        if (biometricUnlockController.mMode == 1) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.doze.DozeHost
    public final void onIgnoreTouchWhilePulsing(boolean z) {
        if (z != this.mIgnoreTouchWhilePulsing) {
            this.mDozeLog.tracePulseTouchDisabledByProx(z);
        }
        this.mIgnoreTouchWhilePulsing = z;
        if (this.mStatusBarStateController.isDozing() && z) {
            this.mNotificationShadeWindowViewController.cancelCurrentTouch();
        }
    }

    @Override // com.android.systemui.doze.DozeHost
    public final void prepareForGentleSleep(DozeScreenState$$ExternalSyntheticLambda1 dozeScreenState$$ExternalSyntheticLambda1) {
        if (this.mPendingScreenOffCallback != null) {
            Log.w("DozeServiceHost", "Overlapping onDisplayOffCallback. Ignoring previous one.");
        }
        this.mPendingScreenOffCallback = dozeScreenState$$ExternalSyntheticLambda1;
        this.mStatusBar.updateScrimController();
    }

    @Override // com.android.systemui.doze.DozeHost
    public final void removeCallback(DozeTriggers.AnonymousClass1 r1) {
        this.mCallbacks.remove(r1);
    }

    @Override // com.android.systemui.doze.DozeHost
    public final void setAnimateWakeup(boolean z) {
        WakefulnessLifecycle wakefulnessLifecycle = this.mWakefulnessLifecycle;
        Objects.requireNonNull(wakefulnessLifecycle);
        if (wakefulnessLifecycle.mWakefulness != 2) {
            WakefulnessLifecycle wakefulnessLifecycle2 = this.mWakefulnessLifecycle;
            Objects.requireNonNull(wakefulnessLifecycle2);
            if (wakefulnessLifecycle2.mWakefulness != 1) {
                this.mAnimateWakeup = z;
            }
        }
    }

    @Override // com.android.systemui.doze.DozeHost
    public final void setAodDimmingScrim(float f) {
        this.mDozeLog.traceSetAodDimmingScrim(f);
        ScrimController scrimController = this.mScrimController;
        Objects.requireNonNull(scrimController);
        ScrimState scrimState = ScrimState.PULSING;
        ScrimState scrimState2 = ScrimState.AOD;
        if (scrimController.mInFrontAlpha != f) {
            boolean z = true;
            if ((scrimController.mState != scrimState2 || (!scrimController.mDozeParameters.getAlwaysOn() && !scrimController.mDockManager.isDocked())) && scrimController.mState != scrimState) {
                z = false;
            }
            if (z) {
                scrimController.mInFrontAlpha = f;
                scrimController.updateScrims();
            }
        }
        scrimState2.mAodFrontScrimAlpha = f;
        scrimState.mAodFrontScrimAlpha = f;
    }

    @Override // com.android.systemui.doze.DozeHost
    public final void setDozeScreenBrightness(int i) {
        this.mDozeLog.traceDozeScreenBrightness(i);
        this.mNotificationShadeWindowController.setDozeScreenBrightness(i);
    }

    @Override // com.android.systemui.doze.DozeHost
    public final void startDozing() {
        if (!this.mDozingRequested) {
            this.mDozingRequested = true;
            updateDozing();
            this.mDozeLog.traceDozing(this.mStatusBarStateController.isDozing());
            this.mStatusBar.updateIsKeyguard();
        }
    }

    @Override // com.android.systemui.doze.DozeHost
    public final void stopDozing() {
        if (this.mDozingRequested) {
            this.mDozingRequested = false;
            updateDozing();
            this.mDozeLog.traceDozing(this.mStatusBarStateController.isDozing());
        }
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("PSB.DozeServiceHost[mCallbacks=");
        m.append(this.mCallbacks.size());
        m.append("]");
        return m.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0031  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateDozing() {
        /*
            r4 = this;
            boolean r0 = r4.mDozingRequested
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x000e
            com.android.systemui.statusbar.SysuiStatusBarStateController r0 = r4.mStatusBarStateController
            int r0 = r0.getState()
            if (r0 == r2) goto L_0x001e
        L_0x000e:
            dagger.Lazy<com.android.systemui.statusbar.phone.BiometricUnlockController> r0 = r4.mBiometricUnlockControllerLazy
            java.lang.Object r0 = r0.get()
            com.android.systemui.statusbar.phone.BiometricUnlockController r0 = (com.android.systemui.statusbar.phone.BiometricUnlockController) r0
            java.util.Objects.requireNonNull(r0)
            int r0 = r0.mMode
            r3 = 2
            if (r0 != r3) goto L_0x0020
        L_0x001e:
            r0 = r2
            goto L_0x0021
        L_0x0020:
            r0 = r1
        L_0x0021:
            dagger.Lazy<com.android.systemui.statusbar.phone.BiometricUnlockController> r3 = r4.mBiometricUnlockControllerLazy
            java.lang.Object r3 = r3.get()
            com.android.systemui.statusbar.phone.BiometricUnlockController r3 = (com.android.systemui.statusbar.phone.BiometricUnlockController) r3
            java.util.Objects.requireNonNull(r3)
            int r3 = r3.mMode
            if (r3 != r2) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r1 = r0
        L_0x0032:
            com.android.systemui.statusbar.SysuiStatusBarStateController r0 = r4.mStatusBarStateController
            r0.setIsDozing(r1)
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController r4 = r4.mNotificationShadeWindowViewController
            java.util.Objects.requireNonNull(r4)
            java.util.Optional<com.android.systemui.lowlightclock.LowLightClockController> r4 = r4.mLowLightClockController
            com.android.systemui.statusbar.phone.NotificationShadeWindowViewController$$ExternalSyntheticLambda1 r0 = new com.android.systemui.statusbar.phone.NotificationShadeWindowViewController$$ExternalSyntheticLambda1
            r0.<init>()
            r4.ifPresent(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.DozeServiceHost.updateDozing():void");
    }

    @Override // com.android.systemui.doze.DozeHost
    public final boolean isDozeSuppressed() {
        return this.mSuppressed;
    }
}
