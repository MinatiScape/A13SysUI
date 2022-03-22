package com.android.systemui.statusbar.phone;

import android.hardware.biometrics.BiometricSourceType;
import android.metrics.LogMaker;
import android.os.Handler;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.service.dreams.IDreamManager;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.internal.util.LatencyTracker;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.KeyguardViewController;
import com.android.settingslib.wifi.WifiTracker;
import com.android.systemui.Dumpable;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.keyguard.KeyguardService;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.log.SessionTracker;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.wm.shell.ShellTaskOrganizer$$ExternalSyntheticLambda1;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda10;
import com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda1;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class BiometricUnlockController extends KeyguardUpdateMonitorCallback implements Dumpable {
    public static final UiEventLoggerImpl UI_EVENT_LOGGER = new UiEventLoggerImpl();
    public final AuthController mAuthController;
    public BiometricModeListener mBiometricModeListener;
    public BiometricSourceType mBiometricType;
    public final int mConsecutiveFpFailureThreshold;
    public final DozeParameters mDozeParameters;
    public DozeScrimController mDozeScrimController;
    public boolean mFadedAwayAfterWakeAndUnlock;
    public final Handler mHandler;
    public final KeyguardBypassController mKeyguardBypassController;
    public final KeyguardStateController mKeyguardStateController;
    public KeyguardUnlockAnimationController mKeyguardUnlockAnimationController;
    public KeyguardViewController mKeyguardViewController;
    public KeyguardViewMediator mKeyguardViewMediator;
    public long mLastFpFailureUptimeMillis;
    public final LatencyTracker mLatencyTracker;
    public final NotificationMediaManager mMediaManager;
    public final MetricsLogger mMetricsLogger;
    public int mMode;
    public final NotificationShadeWindowController mNotificationShadeWindowController;
    public int mNumConsecutiveFpFailures;
    public boolean mPendingShowBouncer;
    public final PowerManager mPowerManager;
    public final AnonymousClass4 mScreenObserver;
    public ScrimController mScrimController;
    public final SessionTracker mSessionTracker;
    public final ShadeController mShadeController;
    public final StatusBarStateController mStatusBarStateController;
    public final KeyguardUpdateMonitor mUpdateMonitor;
    public PowerManager.WakeLock mWakeLock;
    public final int mWakeUpDelay;
    @VisibleForTesting
    public final WakefulnessLifecycle.Observer mWakefulnessObserver;
    public PendingAuthenticated mPendingAuthenticated = null;
    public final AnonymousClass1 mReleaseBiometricWakeLockRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.BiometricUnlockController.1
        @Override // java.lang.Runnable
        public final void run() {
            Log.i("BiometricUnlockCtrl", "biometric wakelock: TIMEOUT!!");
            BiometricUnlockController.this.releaseBiometricWakeLock();
        }
    };

    /* loaded from: classes.dex */
    public interface BiometricModeListener {
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public enum BiometricUiEvent implements UiEventLogger.UiEventEnum {
        /* JADX INFO: Fake field, exist only in values array */
        BIOMETRIC_FINGERPRINT_SUCCESS(396),
        /* JADX INFO: Fake field, exist only in values array */
        BIOMETRIC_FINGERPRINT_FAILURE(397),
        /* JADX INFO: Fake field, exist only in values array */
        BIOMETRIC_FINGERPRINT_ERROR(398),
        /* JADX INFO: Fake field, exist only in values array */
        BIOMETRIC_FACE_SUCCESS(399),
        /* JADX INFO: Fake field, exist only in values array */
        BIOMETRIC_FACE_FAILURE(400),
        /* JADX INFO: Fake field, exist only in values array */
        BIOMETRIC_FACE_ERROR(401),
        /* JADX INFO: Fake field, exist only in values array */
        BIOMETRIC_IRIS_SUCCESS(402),
        /* JADX INFO: Fake field, exist only in values array */
        BIOMETRIC_IRIS_FAILURE(403),
        /* JADX INFO: Fake field, exist only in values array */
        BIOMETRIC_IRIS_ERROR(404),
        BIOMETRIC_BOUNCER_SHOWN(916);
        
        public static final Map<BiometricSourceType, BiometricUiEvent> ERROR_EVENT_BY_SOURCE_TYPE;
        public static final Map<BiometricSourceType, BiometricUiEvent> FAILURE_EVENT_BY_SOURCE_TYPE;
        public static final Map<BiometricSourceType, BiometricUiEvent> SUCCESS_EVENT_BY_SOURCE_TYPE;
        private final int mId;

        static {
            BiometricUiEvent biometricUiEvent;
            BiometricUiEvent biometricUiEvent2;
            BiometricUiEvent biometricUiEvent3;
            BiometricUiEvent biometricUiEvent4;
            BiometricUiEvent biometricUiEvent5;
            BiometricUiEvent biometricUiEvent6;
            BiometricUiEvent biometricUiEvent7;
            BiometricUiEvent biometricUiEvent8;
            BiometricUiEvent biometricUiEvent9;
            ERROR_EVENT_BY_SOURCE_TYPE = Map.of(BiometricSourceType.FINGERPRINT, biometricUiEvent3, BiometricSourceType.FACE, biometricUiEvent6, BiometricSourceType.IRIS, biometricUiEvent9);
            SUCCESS_EVENT_BY_SOURCE_TYPE = Map.of(BiometricSourceType.FINGERPRINT, biometricUiEvent, BiometricSourceType.FACE, biometricUiEvent4, BiometricSourceType.IRIS, biometricUiEvent7);
            FAILURE_EVENT_BY_SOURCE_TYPE = Map.of(BiometricSourceType.FINGERPRINT, biometricUiEvent2, BiometricSourceType.FACE, biometricUiEvent5, BiometricSourceType.IRIS, biometricUiEvent8);
        }

        BiometricUiEvent(int i) {
            this.mId = i;
        }

        public final int getId() {
            return this.mId;
        }
    }

    /* JADX WARN: Type inference failed for: r4v1, types: [com.android.systemui.statusbar.phone.BiometricUnlockController$1] */
    /* JADX WARN: Type inference failed for: r5v0, types: [com.android.systemui.statusbar.phone.BiometricUnlockController$4, java.lang.Object] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public BiometricUnlockController(com.android.systemui.statusbar.phone.DozeScrimController r8, com.android.systemui.keyguard.KeyguardViewMediator r9, com.android.systemui.statusbar.phone.ScrimController r10, com.android.systemui.statusbar.phone.ShadeController r11, com.android.systemui.statusbar.NotificationShadeWindowController r12, com.android.systemui.statusbar.policy.KeyguardStateController r13, android.os.Handler r14, com.android.keyguard.KeyguardUpdateMonitor r15, android.content.res.Resources r16, com.android.systemui.statusbar.phone.KeyguardBypassController r17, com.android.systemui.statusbar.phone.DozeParameters r18, com.android.internal.logging.MetricsLogger r19, com.android.systemui.dump.DumpManager r20, android.os.PowerManager r21, com.android.systemui.statusbar.NotificationMediaManager r22, com.android.systemui.keyguard.WakefulnessLifecycle r23, com.android.systemui.keyguard.ScreenLifecycle r24, com.android.systemui.biometrics.AuthController r25, com.android.systemui.plugins.statusbar.StatusBarStateController r26, com.android.systemui.keyguard.KeyguardUnlockAnimationController r27, com.android.systemui.log.SessionTracker r28, com.android.internal.util.LatencyTracker r29) {
        /*
            r7 = this;
            r0 = r7
            r1 = r15
            r2 = r16
            r3 = r17
            r7.<init>()
            r4 = 0
            r0.mPendingAuthenticated = r4
            com.android.systemui.statusbar.phone.BiometricUnlockController$1 r4 = new com.android.systemui.statusbar.phone.BiometricUnlockController$1
            r4.<init>()
            r0.mReleaseBiometricWakeLockRunnable = r4
            com.android.systemui.statusbar.phone.BiometricUnlockController$3 r4 = new com.android.systemui.statusbar.phone.BiometricUnlockController$3
            r4.<init>()
            r0.mWakefulnessObserver = r4
            com.android.systemui.statusbar.phone.BiometricUnlockController$4 r5 = new com.android.systemui.statusbar.phone.BiometricUnlockController$4
            r5.<init>()
            r0.mScreenObserver = r5
            r6 = r21
            r0.mPowerManager = r6
            r6 = r11
            r0.mShadeController = r6
            r0.mUpdateMonitor = r1
            r6 = r18
            r0.mDozeParameters = r6
            r15.registerCallback(r7)
            r1 = r22
            r0.mMediaManager = r1
            r1 = r29
            r0.mLatencyTracker = r1
            java.util.Objects.requireNonNull(r23)
            r1 = r23
            java.util.ArrayList<T> r1 = r1.mObservers
            r1.add(r4)
            java.util.Objects.requireNonNull(r24)
            r1 = r24
            java.util.ArrayList<T> r1 = r1.mObservers
            r1.add(r5)
            r1 = r12
            r0.mNotificationShadeWindowController = r1
            r1 = r8
            r0.mDozeScrimController = r1
            r1 = r9
            r0.mKeyguardViewMediator = r1
            r1 = r10
            r0.mScrimController = r1
            r1 = r13
            r0.mKeyguardStateController = r1
            r1 = r14
            r0.mHandler = r1
            r1 = 17694961(0x10e00f1, float:2.6081956E-38)
            int r1 = r2.getInteger(r1)
            r0.mWakeUpDelay = r1
            r1 = 2131492932(0x7f0c0044, float:1.860933E38)
            int r1 = r2.getInteger(r1)
            r0.mConsecutiveFpFailureThreshold = r1
            r0.mKeyguardBypassController = r3
            java.util.Objects.requireNonNull(r17)
            r3.unlockController = r0
            r1 = r19
            r0.mMetricsLogger = r1
            r1 = r25
            r0.mAuthController = r1
            r1 = r26
            r0.mStatusBarStateController = r1
            r1 = r27
            r0.mKeyguardUnlockAnimationController = r1
            r1 = r28
            r0.mSessionTracker = r1
            java.lang.Class<com.android.systemui.statusbar.phone.BiometricUnlockController> r1 = com.android.systemui.statusbar.phone.BiometricUnlockController.class
            java.lang.String r1 = r1.getName()
            r2 = r20
            r2.registerDumpable(r1, r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.BiometricUnlockController.<init>(com.android.systemui.statusbar.phone.DozeScrimController, com.android.systemui.keyguard.KeyguardViewMediator, com.android.systemui.statusbar.phone.ScrimController, com.android.systemui.statusbar.phone.ShadeController, com.android.systemui.statusbar.NotificationShadeWindowController, com.android.systemui.statusbar.policy.KeyguardStateController, android.os.Handler, com.android.keyguard.KeyguardUpdateMonitor, android.content.res.Resources, com.android.systemui.statusbar.phone.KeyguardBypassController, com.android.systemui.statusbar.phone.DozeParameters, com.android.internal.logging.MetricsLogger, com.android.systemui.dump.DumpManager, android.os.PowerManager, com.android.systemui.statusbar.NotificationMediaManager, com.android.systemui.keyguard.WakefulnessLifecycle, com.android.systemui.keyguard.ScreenLifecycle, com.android.systemui.biometrics.AuthController, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.systemui.keyguard.KeyguardUnlockAnimationController, com.android.systemui.log.SessionTracker, com.android.internal.util.LatencyTracker):void");
    }

    public final void resetMode() {
        this.mMode = 0;
        this.mBiometricType = null;
        this.mNotificationShadeWindowController.setForceDozeBrightness(false);
        BiometricModeListener biometricModeListener = this.mBiometricModeListener;
        if (biometricModeListener != null) {
            StatusBar.AnonymousClass4 r1 = (StatusBar.AnonymousClass4) biometricModeListener;
            Objects.requireNonNull(r1);
            r1.setWakeAndUnlocking(false);
            ((StatusBar.AnonymousClass4) this.mBiometricModeListener).notifyBiometricAuthModeChanged();
        }
        this.mNumConsecutiveFpFailures = 0;
        this.mLastFpFailureUptimeMillis = 0L;
    }

    public final void startWakeAndUnlock(int i) {
        IDreamManager iDreamManager;
        Log.v("BiometricUnlockCtrl", "startWakeAndUnlock(" + i + ")");
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.mUpdateMonitor;
        Objects.requireNonNull(keyguardUpdateMonitor);
        final boolean z = keyguardUpdateMonitor.mDeviceInteractive;
        this.mMode = i;
        if (i == 2) {
            ScrimController scrimController = this.mScrimController;
            Objects.requireNonNull(scrimController);
            ScrimState scrimState = scrimController.mState;
            if (scrimState == ScrimState.AOD || scrimState == ScrimState.PULSING) {
                this.mNotificationShadeWindowController.setForceDozeBrightness(true);
            }
        }
        final boolean z2 = i == 1 && this.mDozeParameters.getAlwaysOn() && this.mWakeUpDelay > 0;
        Runnable biometricUnlockController$$ExternalSyntheticLambda1 = new Runnable() { // from class: com.android.systemui.statusbar.phone.BiometricUnlockController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                BiometricUnlockController biometricUnlockController = BiometricUnlockController.this;
                boolean z3 = z;
                boolean z4 = z2;
                Objects.requireNonNull(biometricUnlockController);
                if (!z3) {
                    Log.i("BiometricUnlockCtrl", "bio wakelock: Authenticated, waking up...");
                    biometricUnlockController.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 4, "android.policy:BIOMETRIC");
                }
                if (z4) {
                    biometricUnlockController.mKeyguardViewMediator.onWakeAndUnlocking();
                }
                Trace.beginSection("release wake-and-unlock");
                biometricUnlockController.releaseBiometricWakeLock();
                Trace.endSection();
            }
        };
        if (!z2 && this.mMode != 0) {
            biometricUnlockController$$ExternalSyntheticLambda1.run();
        }
        int i2 = this.mMode;
        switch (i2) {
            case 1:
            case 2:
            case FalsingManager.VERSION /* 6 */:
                if (i2 == 2) {
                    Trace.beginSection("MODE_WAKE_AND_UNLOCK_PULSING");
                    this.mMediaManager.updateMediaMetaData(false, true);
                } else if (i2 == 1) {
                    Trace.beginSection("MODE_WAKE_AND_UNLOCK");
                } else {
                    Trace.beginSection("MODE_WAKE_AND_UNLOCK_FROM_DREAM");
                    KeyguardUpdateMonitor keyguardUpdateMonitor2 = this.mUpdateMonitor;
                    Objects.requireNonNull(keyguardUpdateMonitor2);
                    if (keyguardUpdateMonitor2.mIsDreaming && (iDreamManager = keyguardUpdateMonitor2.mDreamManager) != null) {
                        try {
                            iDreamManager.awaken();
                        } catch (RemoteException unused) {
                            Log.e("KeyguardUpdateMonitor", "Unable to awaken from dream");
                        }
                    }
                }
                this.mNotificationShadeWindowController.setNotificationShadeFocusable(false);
                if (z2) {
                    this.mHandler.postDelayed(biometricUnlockController$$ExternalSyntheticLambda1, this.mWakeUpDelay);
                } else {
                    this.mKeyguardViewMediator.onWakeAndUnlocking();
                }
                Trace.endSection();
                break;
            case 3:
                Trace.beginSection("MODE_SHOW_BOUNCER");
                if (!z) {
                    this.mPendingShowBouncer = true;
                } else {
                    if (this.mMode == 3) {
                        this.mKeyguardViewController.showBouncer(true);
                    }
                    this.mShadeController.animateCollapsePanels(0, true, false, 1.1f);
                    this.mPendingShowBouncer = false;
                }
                Trace.endSection();
                break;
            case 5:
                Trace.beginSection("MODE_UNLOCK_COLLAPSING");
                if (!z) {
                    this.mPendingShowBouncer = true;
                } else {
                    Objects.requireNonNull(this.mKeyguardUnlockAnimationController);
                    if (!KeyguardService.sEnableRemoteKeyguardGoingAwayAnimation) {
                        this.mShadeController.animateCollapsePanels(0, true, false, 1.1f);
                    }
                    this.mPendingShowBouncer = false;
                    this.mKeyguardViewController.notifyKeyguardAuthenticated();
                }
                Trace.endSection();
                break;
            case 7:
            case 8:
                Trace.beginSection("MODE_DISMISS_BOUNCER or MODE_UNLOCK_FADING");
                this.mKeyguardViewController.notifyKeyguardAuthenticated();
                Trace.endSection();
                break;
        }
        int i3 = this.mMode;
        BiometricModeListener biometricModeListener = this.mBiometricModeListener;
        if (biometricModeListener != null) {
            StatusBar.AnonymousClass4 r0 = (StatusBar.AnonymousClass4) biometricModeListener;
            if (i3 == 1 || i3 == 2 || i3 == 6) {
                r0.setWakeAndUnlocking(true);
            }
        }
        BiometricModeListener biometricModeListener2 = this.mBiometricModeListener;
        if (biometricModeListener2 != null) {
            ((StatusBar.AnonymousClass4) biometricModeListener2).notifyBiometricAuthModeChanged();
        }
        Trace.endSection();
    }

    /* renamed from: com.android.systemui.statusbar.phone.BiometricUnlockController$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass5 {
        public static final /* synthetic */ int[] $SwitchMap$android$hardware$biometrics$BiometricSourceType;

        static {
            int[] iArr = new int[BiometricSourceType.values().length];
            $SwitchMap$android$hardware$biometrics$BiometricSourceType = iArr;
            try {
                iArr[BiometricSourceType.FINGERPRINT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$hardware$biometrics$BiometricSourceType[BiometricSourceType.FACE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$android$hardware$biometrics$BiometricSourceType[BiometricSourceType.IRIS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class PendingAuthenticated {
        public final BiometricSourceType biometricSourceType;
        public final boolean isStrongBiometric;
        public final int userId;

        public PendingAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
            this.userId = i;
            this.biometricSourceType = biometricSourceType;
            this.isStrongBiometric = z;
        }
    }

    public static int toSubtype(BiometricSourceType biometricSourceType) {
        int i = AnonymousClass5.$SwitchMap$android$hardware$biometrics$BiometricSourceType[biometricSourceType.ordinal()];
        if (i == 1) {
            return 0;
        }
        if (i == 2) {
            return 1;
        }
        if (i != 3) {
            return 3;
        }
        return 2;
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(" BiometricUnlockController:");
        printWriter.print("   mMode=");
        printWriter.println(this.mMode);
        printWriter.print("   mWakeLock=");
        printWriter.println(this.mWakeLock);
        if (this.mUpdateMonitor.isUdfpsSupported()) {
            printWriter.print("   mNumConsecutiveFpFailures=");
            printWriter.println(this.mNumConsecutiveFpFailures);
            printWriter.print("   time since last failure=");
            printWriter.println(SystemClock.uptimeMillis() - this.mLastFpFailureUptimeMillis);
        }
    }

    public final boolean isWakeAndUnlock() {
        int i = this.mMode;
        if (i == 1 || i == 2 || i == 6) {
            return true;
        }
        return false;
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public final void onBiometricAcquired(BiometricSourceType biometricSourceType) {
        Trace.beginSection("BiometricUnlockController#onBiometricAcquired");
        releaseBiometricWakeLock();
        if (isWakeAndUnlock()) {
            if (this.mLatencyTracker.isEnabled()) {
                int i = 2;
                if (biometricSourceType == BiometricSourceType.FACE) {
                    i = 7;
                }
                this.mLatencyTracker.onActionStart(i);
            }
            this.mWakeLock = this.mPowerManager.newWakeLock(1, "wake-and-unlock:wakelock");
            Trace.beginSection("acquiring wake-and-unlock");
            this.mWakeLock.acquire();
            Trace.endSection();
            Log.i("BiometricUnlockCtrl", "biometric acquired, grabbing biometric wakelock");
            this.mHandler.postDelayed(this.mReleaseBiometricWakeLockRunnable, WifiTracker.MAX_SCAN_RESULT_AGE_MILLIS);
        }
        Trace.endSection();
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public final void onBiometricAuthFailed(BiometricSourceType biometricSourceType) {
        int i;
        this.mMetricsLogger.write(new LogMaker(1697).setType(11).setSubtype(toSubtype(biometricSourceType)));
        Optional.ofNullable(BiometricUiEvent.FAILURE_EVENT_BY_SOURCE_TYPE.get(biometricSourceType)).ifPresent(new ShellTaskOrganizer$$ExternalSyntheticLambda1(this, 1));
        if (this.mLatencyTracker.isEnabled()) {
            if (biometricSourceType == BiometricSourceType.FACE) {
                i = 7;
            } else {
                i = 2;
            }
            this.mLatencyTracker.onActionCancel(i);
        }
        if (biometricSourceType == BiometricSourceType.FINGERPRINT && this.mUpdateMonitor.isUdfpsSupported()) {
            long uptimeMillis = SystemClock.uptimeMillis();
            if (uptimeMillis - this.mLastFpFailureUptimeMillis < this.mConsecutiveFpFailureThreshold) {
                this.mNumConsecutiveFpFailures++;
            } else {
                this.mNumConsecutiveFpFailures = 1;
            }
            this.mLastFpFailureUptimeMillis = uptimeMillis;
            if (this.mNumConsecutiveFpFailures >= 2) {
                startWakeAndUnlock(3);
                UiEventLoggerImpl uiEventLoggerImpl = UI_EVENT_LOGGER;
                BiometricUiEvent biometricUiEvent = BiometricUiEvent.BIOMETRIC_BOUNCER_SHOWN;
                SessionTracker sessionTracker = this.mSessionTracker;
                Objects.requireNonNull(sessionTracker);
                uiEventLoggerImpl.log(biometricUiEvent, (InstanceId) sessionTracker.mSessionToInstanceId.getOrDefault(1, null));
                this.mNumConsecutiveFpFailures = 0;
            }
        }
        releaseBiometricWakeLock();
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public final void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
        boolean z2;
        Trace.beginSection("BiometricUnlockController#onBiometricAuthenticated");
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.mUpdateMonitor;
        Objects.requireNonNull(keyguardUpdateMonitor);
        if (keyguardUpdateMonitor.mGoingToSleep) {
            this.mPendingAuthenticated = new PendingAuthenticated(i, biometricSourceType, z);
            Trace.endSection();
            return;
        }
        this.mBiometricType = biometricSourceType;
        this.mMetricsLogger.write(new LogMaker(1697).setType(10).setSubtype(toSubtype(biometricSourceType)));
        Optional.ofNullable(BiometricUiEvent.SUCCESS_EVENT_BY_SOURCE_TYPE.get(biometricSourceType)).ifPresent(new PipMotionHelper$$ExternalSyntheticLambda1(this, 3));
        if (this.mKeyguardStateController.isOccluded() || this.mKeyguardBypassController.onBiometricAuthenticated(biometricSourceType, z)) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2) {
            this.mKeyguardViewMediator.userActivity();
            startWakeAndUnlock(biometricSourceType, z);
            return;
        }
        Log.d("BiometricUnlockCtrl", "onBiometricAuthenticated aborted by bypass controller");
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public final void onBiometricError(int i, String str, BiometricSourceType biometricSourceType) {
        this.mMetricsLogger.write(new LogMaker(1697).setType(15).setSubtype(toSubtype(biometricSourceType)).addTaggedData(1741, Integer.valueOf(i)));
        Optional.ofNullable(BiometricUiEvent.ERROR_EVENT_BY_SOURCE_TYPE.get(biometricSourceType)).ifPresent(new BubbleController$$ExternalSyntheticLambda10(this, 1));
        if (biometricSourceType == BiometricSourceType.FINGERPRINT && ((i == 7 || i == 9) && this.mUpdateMonitor.isUdfpsSupported() && (this.mStatusBarStateController.getState() == 0 || this.mStatusBarStateController.getState() == 2))) {
            startWakeAndUnlock(3);
            UiEventLoggerImpl uiEventLoggerImpl = UI_EVENT_LOGGER;
            BiometricUiEvent biometricUiEvent = BiometricUiEvent.BIOMETRIC_BOUNCER_SHOWN;
            SessionTracker sessionTracker = this.mSessionTracker;
            Objects.requireNonNull(sessionTracker);
            uiEventLoggerImpl.log(biometricUiEvent, (InstanceId) sessionTracker.mSessionToInstanceId.getOrDefault(1, null));
        }
        releaseBiometricWakeLock();
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public final void onFinishedGoingToSleep(int i) {
        Trace.beginSection("BiometricUnlockController#onFinishedGoingToSleep");
        PendingAuthenticated pendingAuthenticated = this.mPendingAuthenticated;
        if (pendingAuthenticated != null) {
            this.mHandler.post(new BiometricUnlockController$$ExternalSyntheticLambda0(this, pendingAuthenticated, 0));
            this.mPendingAuthenticated = null;
        }
        Trace.endSection();
    }

    public final void releaseBiometricWakeLock() {
        if (this.mWakeLock != null) {
            this.mHandler.removeCallbacks(this.mReleaseBiometricWakeLockRunnable);
            Log.i("BiometricUnlockCtrl", "releasing biometric wakelock");
            this.mWakeLock.release();
            this.mWakeLock = null;
        }
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public final void onStartedGoingToSleep$1() {
        resetMode();
        this.mFadedAwayAfterWakeAndUnlock = false;
        this.mPendingAuthenticated = null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0043, code lost:
        if (r4.mKeyguardStateController.isMethodSecure() != false) goto L_0x012e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x006b, code lost:
        if (r4.mKeyguardViewController.isBouncerShowing() == false) goto L_0x012e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00a2, code lost:
        if (r0 != false) goto L_0x0131;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00a8, code lost:
        if (r0 != false) goto L_0x012e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00c5, code lost:
        if (r0 != false) goto L_0x00c7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00f2, code lost:
        if (r6.altBouncerShowing != false) goto L_0x00f4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0111, code lost:
        if (r1 != 0) goto L_0x012a;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0128, code lost:
        if ((r5 == null ? false : r5.mOnFingerDown) != false) goto L_0x012a;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x012c, code lost:
        if (r0 != false) goto L_0x012e;
     */
    /* JADX WARN: Removed duplicated region for block: B:36:0x009a  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00c1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void startWakeAndUnlock(android.hardware.biometrics.BiometricSourceType r5, boolean r6) {
        /*
            Method dump skipped, instructions count: 309
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.BiometricUnlockController.startWakeAndUnlock(android.hardware.biometrics.BiometricSourceType, boolean):void");
    }
}
