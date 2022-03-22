package com.android.systemui.statusbar;

import android.animation.AnimatorSet;
import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Handler;
import android.os.Message;
import android.os.UserManager;
import android.text.TextUtils;
import android.view.ViewGroup;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IBatteryStats;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.Utils;
import com.android.settingslib.users.AvatarPickerActivity$$ExternalSyntheticLambda0;
import com.android.systemui.DejankUtils;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.KeyguardIndication;
import com.android.systemui.keyguard.KeyguardIndicationRotateTextViewController;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.navigationbar.NavigationBarView$$ExternalSyntheticLambda3;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBottomAreaView;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.KeyguardIndicationTextView;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda18;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.wakelock.SettableWakeLock;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda0;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda2;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda18;
import com.google.android.systemui.elmyra.actions.Action$$ExternalSyntheticLambda0;
import java.text.NumberFormat;
import java.util.Objects;
/* loaded from: classes.dex */
public class KeyguardIndicationController {
    public String mAlignmentIndication;
    public final DelayableExecutor mBackgroundExecutor;
    public final IBatteryStats mBatteryInfo;
    public int mBatteryLevel;
    public boolean mBatteryOverheated;
    public CharSequence mBiometricMessage;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public AnonymousClass3 mBroadcastReceiver;
    public int mChargingSpeed;
    public long mChargingTimeRemaining;
    public int mChargingWattage;
    public final Context mContext;
    public final DevicePolicyManager mDevicePolicyManager;
    public final DockManager mDockManager;
    public boolean mDozing;
    public boolean mEnableBatteryDefender;
    public final DelayableExecutor mExecutor;
    public final FalsingManager mFalsingManager;
    public final IActivityManager mIActivityManager;
    public ViewGroup mIndicationArea;
    public boolean mInited;
    public ColorStateList mInitialTextColorState;
    public final KeyguardBypassController mKeyguardBypassController;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final LockPatternUtils mLockPatternUtils;
    public KeyguardIndicationTextView mLockScreenIndicationView;
    public String mMessageToShowOnScreenOn;
    public boolean mOrganizationOwnedDevice;
    public boolean mPowerCharged;
    public boolean mPowerPluggedIn;
    public boolean mPowerPluggedInDock;
    public boolean mPowerPluggedInWired;
    public boolean mPowerPluggedInWireless;
    public KeyguardIndicationRotateTextViewController mRotateTextViewController;
    public ScreenLifecycle mScreenLifecycle;
    public final AnonymousClass2 mScreenObserver;
    public StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public final StatusBarStateController mStatusBarStateController;
    public KeyguardIndicationTextView mTopIndicationView;
    public CharSequence mTransientIndication;
    public CharSequence mTrustGrantedIndication;
    public BaseKeyguardCallback mUpdateMonitorCallback;
    public final UserManager mUserManager;
    public boolean mVisible;
    public final SettableWakeLock mWakeLock;
    public boolean mBatteryPresent = true;
    public final AnonymousClass4 mTickReceiver = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.statusbar.KeyguardIndicationController.4
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onTimeChanged() {
            KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
            if (keyguardIndicationController.mVisible) {
                keyguardIndicationController.updateIndication(false);
            }
        }
    };
    public final AnonymousClass5 mHandler = new Handler() { // from class: com.android.systemui.statusbar.KeyguardIndicationController.5
        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                KeyguardIndicationController.this.hideTransientIndication();
            } else if (i == 2) {
                KeyguardIndicationController.this.showActionToUnlock();
            } else if (i == 3) {
                KeyguardIndicationController.m86$$Nest$mhideBiometricMessage(KeyguardIndicationController.this);
            }
        }
    };
    public AnonymousClass6 mStatusBarStateListener = new AnonymousClass6();
    public AnonymousClass7 mKeyguardStateCallback = new KeyguardStateController.Callback() { // from class: com.android.systemui.statusbar.KeyguardIndicationController.7
        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public final void onKeyguardShowingChanged() {
            if (!KeyguardIndicationController.this.mKeyguardStateController.isShowing()) {
                KeyguardIndicationTextView keyguardIndicationTextView = KeyguardIndicationController.this.mTopIndicationView;
                Objects.requireNonNull(keyguardIndicationTextView);
                AnimatorSet animatorSet = keyguardIndicationTextView.mLastAnimator;
                if (animatorSet != null) {
                    animatorSet.cancel();
                }
                keyguardIndicationTextView.setText("");
                KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController = KeyguardIndicationController.this.mRotateTextViewController;
                Objects.requireNonNull(keyguardIndicationRotateTextViewController);
                keyguardIndicationRotateTextViewController.mCurrIndicationType = -1;
                keyguardIndicationRotateTextViewController.mIndicationQueue.clear();
                keyguardIndicationRotateTextViewController.mIndicationMessages.clear();
                KeyguardIndicationTextView keyguardIndicationTextView2 = (KeyguardIndicationTextView) keyguardIndicationRotateTextViewController.mView;
                Objects.requireNonNull(keyguardIndicationTextView2);
                AnimatorSet animatorSet2 = keyguardIndicationTextView2.mLastAnimator;
                if (animatorSet2 != null) {
                    animatorSet2.cancel();
                }
                keyguardIndicationTextView2.setText("");
                return;
            }
            KeyguardIndicationController.this.updatePersistentIndications(false, KeyguardUpdateMonitor.getCurrentUser());
        }

        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public final void onUnlockedChanged() {
            KeyguardIndicationController.this.updateIndication(false);
        }
    };

    /* renamed from: com.android.systemui.statusbar.KeyguardIndicationController$6  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass6 implements StatusBarStateController.StateListener {
        public AnonymousClass6() {
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onDozingChanged(boolean z) {
            KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
            if (keyguardIndicationController.mDozing != z) {
                keyguardIndicationController.mDozing = z;
                if (z) {
                    KeyguardIndicationController.m86$$Nest$mhideBiometricMessage(keyguardIndicationController);
                }
                KeyguardIndicationController.this.updateIndication(false);
            }
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onStateChanged(int i) {
            KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
            boolean z = true;
            if (i != 1) {
                z = false;
            }
            keyguardIndicationController.setVisible(z);
        }
    }

    /* loaded from: classes.dex */
    public class BaseKeyguardCallback extends KeyguardUpdateMonitorCallback {
        public BaseKeyguardCallback() {
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
            KeyguardIndicationController.m86$$Nest$mhideBiometricMessage(KeyguardIndicationController.this);
            if (biometricSourceType == BiometricSourceType.FACE && !KeyguardIndicationController.this.mKeyguardBypassController.canBypass()) {
                KeyguardIndicationController.this.showActionToUnlock();
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onBiometricError(int i, String str, BiometricSourceType biometricSourceType) {
            boolean z;
            KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardIndicationController.this.mKeyguardUpdateMonitor;
            boolean z2 = false;
            if (biometricSourceType != BiometricSourceType.FINGERPRINT ? biometricSourceType != BiometricSourceType.FACE || ((keyguardUpdateMonitor.isUnlockingWithBiometricAllowed(true) || i == 9) && i != 5) : !((!keyguardUpdateMonitor.isUnlockingWithBiometricAllowed(true) && i != 9) || i == 5 || i == 10)) {
                z = false;
            } else {
                z = true;
            }
            if (!z) {
                if (biometricSourceType == BiometricSourceType.FACE) {
                    if (KeyguardIndicationController.this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning() && KeyguardIndicationController.this.mKeyguardUpdateMonitor.isUnlockingWithBiometricAllowed(true)) {
                        z2 = true;
                    }
                    if (z2 && !KeyguardIndicationController.this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
                        ScreenLifecycle screenLifecycle = KeyguardIndicationController.this.mScreenLifecycle;
                        Objects.requireNonNull(screenLifecycle);
                        if (screenLifecycle.mScreenState == 2) {
                            KeyguardIndicationController.m87$$Nest$mshowTryFingerprintMsg(KeyguardIndicationController.this, i, str);
                            return;
                        }
                    }
                }
                if (i == 3) {
                    if (!KeyguardIndicationController.this.mStatusBarKeyguardViewManager.isBouncerShowing() && KeyguardIndicationController.this.mKeyguardUpdateMonitor.isUdfpsEnrolled() && KeyguardIndicationController.this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning()) {
                        KeyguardIndicationController.m87$$Nest$mshowTryFingerprintMsg(KeyguardIndicationController.this, i, str);
                    } else if (KeyguardIndicationController.this.mStatusBarKeyguardViewManager.isShowingAlternateAuth()) {
                        KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
                        keyguardIndicationController.mStatusBarKeyguardViewManager.showBouncerMessage(keyguardIndicationController.mContext.getResources().getString(2131952564), KeyguardIndicationController.this.mInitialTextColorState);
                    } else {
                        KeyguardIndicationController.this.showActionToUnlock();
                    }
                } else if (KeyguardIndicationController.this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
                    KeyguardIndicationController keyguardIndicationController2 = KeyguardIndicationController.this;
                    keyguardIndicationController2.mStatusBarKeyguardViewManager.showBouncerMessage(str, keyguardIndicationController2.mInitialTextColorState);
                } else {
                    ScreenLifecycle screenLifecycle2 = KeyguardIndicationController.this.mScreenLifecycle;
                    Objects.requireNonNull(screenLifecycle2);
                    if (screenLifecycle2.mScreenState == 2) {
                        KeyguardIndicationController.this.showBiometricMessage(str);
                    } else {
                        KeyguardIndicationController.this.mMessageToShowOnScreenOn = str;
                    }
                }
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onBiometricHelp(int i, String str, BiometricSourceType biometricSourceType) {
            boolean z;
            boolean z2 = true;
            if (KeyguardIndicationController.this.mKeyguardUpdateMonitor.isUnlockingWithBiometricAllowed(true)) {
                if (i == -2) {
                    z = true;
                } else {
                    z = false;
                }
                if (KeyguardIndicationController.this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
                    KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
                    keyguardIndicationController.mStatusBarKeyguardViewManager.showBouncerMessage(str, keyguardIndicationController.mInitialTextColorState);
                    return;
                }
                ScreenLifecycle screenLifecycle = KeyguardIndicationController.this.mScreenLifecycle;
                Objects.requireNonNull(screenLifecycle);
                if (screenLifecycle.mScreenState == 2) {
                    if (biometricSourceType == BiometricSourceType.FACE) {
                        if (!KeyguardIndicationController.this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning() || !KeyguardIndicationController.this.mKeyguardUpdateMonitor.isUnlockingWithBiometricAllowed(true)) {
                            z2 = false;
                        }
                        if (z2) {
                            KeyguardIndicationController.m87$$Nest$mshowTryFingerprintMsg(KeyguardIndicationController.this, i, str);
                            return;
                        }
                    }
                    KeyguardIndicationController.this.showBiometricMessage(str);
                } else if (z) {
                    AnonymousClass5 r5 = KeyguardIndicationController.this.mHandler;
                    r5.sendMessageDelayed(r5.obtainMessage(2), 1300L);
                }
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
            if (z && biometricSourceType == BiometricSourceType.FACE) {
                KeyguardIndicationController.m86$$Nest$mhideBiometricMessage(KeyguardIndicationController.this);
                KeyguardIndicationController.this.mMessageToShowOnScreenOn = null;
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onLogoutEnabledChanged() {
            KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
            if (keyguardIndicationController.mVisible) {
                keyguardIndicationController.updateIndication(false);
            }
        }

        /* JADX WARN: Can't wrap try/catch for region: R(39:2|(2:(1:8)(1:7)|(29:10|12|(1:16)(1:15)|(1:20)(1:19)|21|(1:23)(1:24)|(1:28)(1:27)|29|(1:31)(1:32)|(1:36)(1:35)|37|(1:41)(1:40)|42|(1:47)(1:46)|48|(1:50)(1:(1:52)(1:(1:54)))|55|(1:57)(1:58)|59|(1:64)(1:63)|65|89|66|(1:68)(1:69)|70|73|(1:77)|78|(1:(1:(1:94)(2:87|88))(2:83|92))(1:91)))|11|12|(0)|16|(0)|20|21|(0)(0)|(0)|28|29|(0)(0)|(0)|36|37|(0)|41|42|(1:44)|47|48|(0)(0)|55|(0)(0)|59|(1:61)|64|65|89|66|(0)(0)|70|73|(1:75)|77|78|(0)(0)) */
        /* JADX WARN: Code restructure failed: missing block: B:71:0x00cd, code lost:
            r11 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:72:0x00ce, code lost:
            android.util.Log.e("KeyguardIndication", "Error calling IBatteryStats: ", r11);
            r10.this$0.mChargingTimeRemaining = -1;
         */
        /* JADX WARN: Removed duplicated region for block: B:23:0x0034  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x0036  */
        /* JADX WARN: Removed duplicated region for block: B:31:0x0044  */
        /* JADX WARN: Removed duplicated region for block: B:32:0x0046  */
        /* JADX WARN: Removed duplicated region for block: B:50:0x008d  */
        /* JADX WARN: Removed duplicated region for block: B:51:0x008f  */
        /* JADX WARN: Removed duplicated region for block: B:57:0x00a8  */
        /* JADX WARN: Removed duplicated region for block: B:58:0x00aa  */
        /* JADX WARN: Removed duplicated region for block: B:68:0x00c2 A[Catch: RemoteException -> 0x00cd, TryCatch #0 {RemoteException -> 0x00cd, blocks: (B:66:0x00bc, B:68:0x00c2, B:70:0x00ca), top: B:89:0x00bc }] */
        /* JADX WARN: Removed duplicated region for block: B:69:0x00c9  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x00ec  */
        /* JADX WARN: Removed duplicated region for block: B:91:? A[RETURN, SYNTHETIC] */
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onRefreshBatteryInfo(com.android.settingslib.fuelgauge.BatteryStatus r11) {
            /*
                Method dump skipped, instructions count: 260
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.KeyguardIndicationController.BaseKeyguardCallback.onRefreshBatteryInfo(com.android.settingslib.fuelgauge.BatteryStatus):void");
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onRequireUnlockForNfc() {
            KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
            keyguardIndicationController.showTransientIndication(keyguardIndicationController.mContext.getString(2131953156));
            KeyguardIndicationController keyguardIndicationController2 = KeyguardIndicationController.this;
            Objects.requireNonNull(keyguardIndicationController2);
            AnonymousClass5 r3 = keyguardIndicationController2.mHandler;
            r3.sendMessageDelayed(r3.obtainMessage(1), 5000L);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onTrustAgentErrorMessage(CharSequence charSequence) {
            KeyguardIndicationController.this.showBiometricMessage(charSequence);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onUserSwitchComplete(int i) {
            KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
            if (keyguardIndicationController.mVisible) {
                keyguardIndicationController.updateIndication(false);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onUserUnlocked() {
            KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
            if (keyguardIndicationController.mVisible) {
                keyguardIndicationController.updateIndication(false);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void showTrustGrantedMessage(CharSequence charSequence) {
            KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
            keyguardIndicationController.mTrustGrantedIndication = charSequence;
            int currentUser = KeyguardUpdateMonitor.getCurrentUser();
            String trustGrantedIndication = KeyguardIndicationController.this.getTrustGrantedIndication();
            Objects.requireNonNull(KeyguardIndicationController.this);
            keyguardIndicationController.updateTrust(currentUser, trustGrantedIndication);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onTrustChanged(int i) {
            if (KeyguardUpdateMonitor.getCurrentUser() == i) {
                KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
                String trustGrantedIndication = keyguardIndicationController.getTrustGrantedIndication();
                Objects.requireNonNull(KeyguardIndicationController.this);
                keyguardIndicationController.updateTrust(i, trustGrantedIndication);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [java.lang.Object, com.android.systemui.statusbar.KeyguardIndicationController$2] */
    /* JADX WARN: Type inference failed for: r3v0, types: [com.android.systemui.statusbar.KeyguardIndicationController$4] */
    /* JADX WARN: Type inference failed for: r3v1, types: [com.android.systemui.statusbar.KeyguardIndicationController$5] */
    /* JADX WARN: Type inference failed for: r3v3, types: [com.android.systemui.statusbar.KeyguardIndicationController$7] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public KeyguardIndicationController(android.content.Context r7, com.android.systemui.util.wakelock.WakeLock.Builder r8, com.android.systemui.statusbar.policy.KeyguardStateController r9, com.android.systemui.plugins.statusbar.StatusBarStateController r10, com.android.keyguard.KeyguardUpdateMonitor r11, com.android.systemui.dock.DockManager r12, com.android.systemui.broadcast.BroadcastDispatcher r13, android.app.admin.DevicePolicyManager r14, com.android.internal.app.IBatteryStats r15, android.os.UserManager r16, com.android.systemui.util.concurrency.DelayableExecutor r17, com.android.systemui.util.concurrency.DelayableExecutor r18, com.android.systemui.plugins.FalsingManager r19, com.android.internal.widget.LockPatternUtils r20, com.android.systemui.keyguard.ScreenLifecycle r21, android.app.IActivityManager r22, com.android.systemui.statusbar.phone.KeyguardBypassController r23) {
        /*
            r6 = this;
            r0 = r6
            r1 = r21
            r6.<init>()
            r2 = 1
            r0.mBatteryPresent = r2
            com.android.systemui.statusbar.KeyguardIndicationController$2 r2 = new com.android.systemui.statusbar.KeyguardIndicationController$2
            r2.<init>()
            r0.mScreenObserver = r2
            com.android.systemui.statusbar.KeyguardIndicationController$4 r3 = new com.android.systemui.statusbar.KeyguardIndicationController$4
            r3.<init>()
            r0.mTickReceiver = r3
            com.android.systemui.statusbar.KeyguardIndicationController$5 r3 = new com.android.systemui.statusbar.KeyguardIndicationController$5
            r3.<init>()
            r0.mHandler = r3
            com.android.systemui.statusbar.KeyguardIndicationController$6 r3 = new com.android.systemui.statusbar.KeyguardIndicationController$6
            r3.<init>()
            r0.mStatusBarStateListener = r3
            com.android.systemui.statusbar.KeyguardIndicationController$7 r3 = new com.android.systemui.statusbar.KeyguardIndicationController$7
            r3.<init>()
            r0.mKeyguardStateCallback = r3
            r3 = r7
            r0.mContext = r3
            r3 = r13
            r0.mBroadcastDispatcher = r3
            r3 = r14
            r0.mDevicePolicyManager = r3
            r3 = r9
            r0.mKeyguardStateController = r3
            r3 = r10
            r0.mStatusBarStateController = r3
            r3 = r11
            r0.mKeyguardUpdateMonitor = r3
            r3 = r12
            r0.mDockManager = r3
            com.android.systemui.util.wakelock.SettableWakeLock r3 = new com.android.systemui.util.wakelock.SettableWakeLock
            java.util.Objects.requireNonNull(r8)
            r4 = r8
            android.content.Context r4 = r4.mContext
            java.lang.String r5 = "Doze:KeyguardIndication"
            com.android.systemui.util.wakelock.WakeLock r4 = com.android.systemui.util.wakelock.WakeLock.createPartial$1(r4, r5)
            java.lang.String r5 = "KeyguardIndication"
            r3.<init>(r4, r5)
            r0.mWakeLock = r3
            r3 = r15
            r0.mBatteryInfo = r3
            r3 = r16
            r0.mUserManager = r3
            r3 = r17
            r0.mExecutor = r3
            r3 = r18
            r0.mBackgroundExecutor = r3
            r3 = r20
            r0.mLockPatternUtils = r3
            r3 = r22
            r0.mIActivityManager = r3
            r3 = r19
            r0.mFalsingManager = r3
            r3 = r23
            r0.mKeyguardBypassController = r3
            r0.mScreenLifecycle = r1
            java.util.Objects.requireNonNull(r21)
            java.util.ArrayList<T> r0 = r1.mObservers
            r0.add(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.KeyguardIndicationController.<init>(android.content.Context, com.android.systemui.util.wakelock.WakeLock$Builder, com.android.systemui.statusbar.policy.KeyguardStateController, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.keyguard.KeyguardUpdateMonitor, com.android.systemui.dock.DockManager, com.android.systemui.broadcast.BroadcastDispatcher, android.app.admin.DevicePolicyManager, com.android.internal.app.IBatteryStats, android.os.UserManager, com.android.systemui.util.concurrency.DelayableExecutor, com.android.systemui.util.concurrency.DelayableExecutor, com.android.systemui.plugins.FalsingManager, com.android.internal.widget.LockPatternUtils, com.android.systemui.keyguard.ScreenLifecycle, android.app.IActivityManager, com.android.systemui.statusbar.phone.KeyguardBypassController):void");
    }

    public final void showBiometricMessage(int i) {
        showBiometricMessage(this.mContext.getResources().getString(i));
    }

    public final void showTransientIndication(int i) {
        showTransientIndication(this.mContext.getResources().getString(i));
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0055, code lost:
        if (r0 != false) goto L_0x008d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0058, code lost:
        r5 = 2131952551;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x008b, code lost:
        if (r0 != false) goto L_0x008d;
     */
    /* JADX WARN: Removed duplicated region for block: B:41:0x009c  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00b5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String computePowerIndication() {
        /*
            r10 = this;
            boolean r0 = r10.mBatteryOverheated
            r1 = 1120403456(0x42c80000, float:100.0)
            r2 = 0
            r3 = 1
            if (r0 == 0) goto L_0x0027
            r0 = 2131952553(0x7f1303a9, float:1.9541552E38)
            java.text.NumberFormat r4 = java.text.NumberFormat.getPercentInstance()
            int r5 = r10.mBatteryLevel
            float r5 = (float) r5
            float r5 = r5 / r1
            double r5 = (double) r5
            java.lang.String r1 = r4.format(r5)
            android.content.Context r10 = r10.mContext
            android.content.res.Resources r10 = r10.getResources()
            java.lang.Object[] r3 = new java.lang.Object[r3]
            r3[r2] = r1
            java.lang.String r10 = r10.getString(r0, r3)
            return r10
        L_0x0027:
            boolean r0 = r10.mPowerCharged
            if (r0 == 0) goto L_0x0039
            android.content.Context r10 = r10.mContext
            android.content.res.Resources r10 = r10.getResources()
            r0 = 2131952531(0x7f130393, float:1.9541507E38)
            java.lang.String r10 = r10.getString(r0)
            return r10
        L_0x0039:
            long r4 = r10.mChargingTimeRemaining
            r6 = 0
            int r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r0 <= 0) goto L_0x0043
            r0 = r3
            goto L_0x0044
        L_0x0043:
            r0 = r2
        L_0x0044:
            boolean r4 = r10.mPowerPluggedInWired
            r5 = 2131952537(0x7f130399, float:1.954152E38)
            r6 = 2131952551(0x7f1303a7, float:1.9541548E38)
            r7 = 2
            if (r4 == 0) goto L_0x006f
            int r4 = r10.mChargingSpeed
            if (r4 == 0) goto L_0x0065
            if (r4 == r7) goto L_0x005a
            if (r0 == 0) goto L_0x0058
            goto L_0x008d
        L_0x0058:
            r5 = r6
            goto L_0x008d
        L_0x005a:
            if (r0 == 0) goto L_0x0060
            r4 = 2131952539(0x7f13039b, float:1.9541524E38)
            goto L_0x0063
        L_0x0060:
            r4 = 2131952552(0x7f1303a8, float:1.954155E38)
        L_0x0063:
            r5 = r4
            goto L_0x008d
        L_0x0065:
            if (r0 == 0) goto L_0x006b
            r4 = 2131952540(0x7f13039c, float:1.9541526E38)
            goto L_0x0063
        L_0x006b:
            r4 = 2131952554(0x7f1303aa, float:1.9541554E38)
            goto L_0x0063
        L_0x006f:
            boolean r4 = r10.mPowerPluggedInWireless
            if (r4 == 0) goto L_0x007d
            if (r0 == 0) goto L_0x0079
            r4 = 2131952541(0x7f13039d, float:1.9541528E38)
            goto L_0x0063
        L_0x0079:
            r4 = 2131952556(0x7f1303ac, float:1.9541558E38)
            goto L_0x0063
        L_0x007d:
            boolean r4 = r10.mPowerPluggedInDock
            if (r4 == 0) goto L_0x008b
            if (r0 == 0) goto L_0x0087
            r4 = 2131952538(0x7f13039a, float:1.9541522E38)
            goto L_0x0063
        L_0x0087:
            r4 = 2131952555(0x7f1303ab, float:1.9541556E38)
            goto L_0x0063
        L_0x008b:
            if (r0 == 0) goto L_0x0058
        L_0x008d:
            java.text.NumberFormat r4 = java.text.NumberFormat.getPercentInstance()
            int r6 = r10.mBatteryLevel
            float r6 = (float) r6
            float r6 = r6 / r1
            double r8 = (double) r6
            java.lang.String r1 = r4.format(r8)
            if (r0 == 0) goto L_0x00b5
            android.content.Context r0 = r10.mContext
            long r8 = r10.mChargingTimeRemaining
            java.lang.String r0 = android.text.format.Formatter.formatShortElapsedTimeRoundingUpToMinutes(r0, r8)
            android.content.Context r10 = r10.mContext
            android.content.res.Resources r10 = r10.getResources()
            java.lang.Object[] r4 = new java.lang.Object[r7]
            r4[r2] = r0
            r4[r3] = r1
            java.lang.String r10 = r10.getString(r5, r4)
            return r10
        L_0x00b5:
            android.content.Context r10 = r10.mContext
            android.content.res.Resources r10 = r10.getResources()
            java.lang.Object[] r0 = new java.lang.Object[r3]
            r0[r2] = r1
            java.lang.String r10 = r10.getString(r5, r0)
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.KeyguardIndicationController.computePowerIndication():java.lang.String");
    }

    public KeyguardUpdateMonitorCallback getKeyguardCallback() {
        if (this.mUpdateMonitorCallback == null) {
            this.mUpdateMonitorCallback = new BaseKeyguardCallback();
        }
        return this.mUpdateMonitorCallback;
    }

    @VisibleForTesting
    public String getTrustGrantedIndication() {
        if (TextUtils.isEmpty(this.mTrustGrantedIndication)) {
            return this.mContext.getString(2131952542);
        }
        return this.mTrustGrantedIndication.toString();
    }

    public final void hideTransientIndication() {
        if (this.mTransientIndication != null) {
            this.mTransientIndication = null;
            removeMessages(1);
            updateTransient();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [com.android.systemui.statusbar.KeyguardIndicationController$$ExternalSyntheticLambda0] */
    public void init() {
        if (!this.mInited) {
            this.mInited = true;
            this.mDockManager.addAlignmentStateListener(new DockManager.AlignmentStateListener() { // from class: com.android.systemui.statusbar.KeyguardIndicationController$$ExternalSyntheticLambda0
                @Override // com.android.systemui.dock.DockManager.AlignmentStateListener
                public final void onAlignmentStateChanged(int i) {
                    KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
                    Objects.requireNonNull(keyguardIndicationController);
                    keyguardIndicationController.mHandler.post(new KeyguardIndicationController$$ExternalSyntheticLambda1(keyguardIndicationController, i, 0));
                }
            });
            this.mKeyguardUpdateMonitor.registerCallback(getKeyguardCallback());
            this.mKeyguardUpdateMonitor.registerCallback(this.mTickReceiver);
            this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
            this.mKeyguardStateController.addCallback(this.mKeyguardStateCallback);
            this.mStatusBarStateListener.onDozingChanged(this.mStatusBarStateController.isDozing());
        }
    }

    /* JADX WARN: Type inference failed for: r4v8, types: [com.android.systemui.statusbar.KeyguardIndicationController$3] */
    public final void setIndicationArea(KeyguardBottomAreaView keyguardBottomAreaView) {
        ColorStateList colorStateList;
        this.mIndicationArea = keyguardBottomAreaView;
        this.mTopIndicationView = (KeyguardIndicationTextView) keyguardBottomAreaView.findViewById(2131428179);
        this.mLockScreenIndicationView = (KeyguardIndicationTextView) keyguardBottomAreaView.findViewById(2131428180);
        KeyguardIndicationTextView keyguardIndicationTextView = this.mTopIndicationView;
        if (keyguardIndicationTextView != null) {
            colorStateList = keyguardIndicationTextView.getTextColors();
        } else {
            colorStateList = ColorStateList.valueOf(-1);
        }
        this.mInitialTextColorState = colorStateList;
        this.mRotateTextViewController = new KeyguardIndicationRotateTextViewController(this.mLockScreenIndicationView, this.mExecutor, this.mStatusBarStateController);
        updateIndication(false);
        this.mOrganizationOwnedDevice = ((Boolean) DejankUtils.whitelistIpcs(new NavigationBarView$$ExternalSyntheticLambda3(this, 1))).booleanValue();
        updatePersistentIndications(false, KeyguardUpdateMonitor.getCurrentUser());
        if (this.mBroadcastReceiver == null) {
            this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.KeyguardIndicationController.3
                @Override // android.content.BroadcastReceiver
                public final void onReceive(Context context, Intent intent) {
                    KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
                    Objects.requireNonNull(keyguardIndicationController);
                    keyguardIndicationController.mOrganizationOwnedDevice = ((Boolean) DejankUtils.whitelistIpcs(new NavigationBarView$$ExternalSyntheticLambda3(keyguardIndicationController, 1))).booleanValue();
                    keyguardIndicationController.updatePersistentIndications(false, KeyguardUpdateMonitor.getCurrentUser());
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
            intentFilter.addAction("android.intent.action.USER_REMOVED");
            this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, intentFilter);
        }
    }

    public final void setVisible(boolean z) {
        int i;
        this.mVisible = z;
        ViewGroup viewGroup = this.mIndicationArea;
        if (z) {
            i = 0;
        } else {
            i = 8;
        }
        viewGroup.setVisibility(i);
        if (z) {
            if (!hasMessages(1)) {
                hideTransientIndication();
            }
            updateIndication(false);
        } else if (!z) {
            hideTransientIndication();
        }
    }

    public final void showActionToUnlock() {
        if (this.mDozing && !this.mKeyguardUpdateMonitor.getUserCanSkipBouncer(KeyguardUpdateMonitor.getCurrentUser())) {
            return;
        }
        if (this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
            if (!this.mStatusBarKeyguardViewManager.isShowingAlternateAuth()) {
                KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
                Objects.requireNonNull(keyguardUpdateMonitor);
                if (keyguardUpdateMonitor.mIsFaceEnrolled) {
                    this.mStatusBarKeyguardViewManager.showBouncerMessage(this.mContext.getString(2131952557), this.mInitialTextColorState);
                }
            }
        } else if (!this.mKeyguardUpdateMonitor.isUdfpsSupported() || !this.mKeyguardUpdateMonitor.getUserCanSkipBouncer(KeyguardUpdateMonitor.getCurrentUser())) {
            showBiometricMessage(this.mContext.getString(2131952563));
        } else {
            showBiometricMessage(this.mContext.getString(2131952564));
        }
    }

    public final void showBiometricMessage(CharSequence charSequence) {
        this.mBiometricMessage = charSequence;
        removeMessages(2);
        removeMessages(3);
        AnonymousClass5 r4 = this.mHandler;
        r4.sendMessageDelayed(r4.obtainMessage(3), 5000L);
        updateBiometricMessage();
    }

    public final void showTransientIndication(String str) {
        this.mTransientIndication = str;
        removeMessages(1);
        AnonymousClass5 r4 = this.mHandler;
        r4.sendMessageDelayed(r4.obtainMessage(1), 5000L);
        updateTransient();
    }

    public final void updateBiometricMessage() {
        if (!TextUtils.isEmpty(this.mBiometricMessage)) {
            KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController = this.mRotateTextViewController;
            CharSequence charSequence = this.mBiometricMessage;
            ColorStateList colorStateList = this.mInitialTextColorState;
            if (TextUtils.isEmpty(charSequence)) {
                throw new IllegalStateException("message or icon must be set");
            } else if (colorStateList != null) {
                keyguardIndicationRotateTextViewController.updateIndication(11, new KeyguardIndication(charSequence, colorStateList, null, null, null, 2600L), true);
            } else {
                throw new IllegalStateException("text color must be set");
            }
        } else {
            this.mRotateTextViewController.hideIndication(11);
        }
        if (this.mDozing) {
            updateIndication(false);
        }
    }

    public final void updateIndication(boolean z) {
        if (this.mVisible) {
            this.mIndicationArea.setVisibility(0);
            if (this.mDozing) {
                this.mLockScreenIndicationView.setVisibility(8);
                this.mTopIndicationView.setVisibility(0);
                this.mTopIndicationView.setTextColor(-1);
                if (!TextUtils.isEmpty(this.mBiometricMessage)) {
                    this.mWakeLock.setAcquired(true);
                    this.mTopIndicationView.switchIndication(this.mBiometricMessage, null, true, new StatusBar$$ExternalSyntheticLambda18(this, 6));
                } else if (!TextUtils.isEmpty(this.mTransientIndication)) {
                    this.mWakeLock.setAcquired(true);
                    this.mTopIndicationView.switchIndication(this.mTransientIndication, null, true, new BubbleStackView$$ExternalSyntheticLambda18(this, 3));
                } else if (!this.mBatteryPresent) {
                    this.mIndicationArea.setVisibility(8);
                } else if (!TextUtils.isEmpty(this.mAlignmentIndication)) {
                    this.mTopIndicationView.switchIndication(this.mAlignmentIndication, null, false, null);
                    this.mTopIndicationView.setTextColor(this.mContext.getColor(2131100395));
                } else if (this.mPowerPluggedIn || this.mEnableBatteryDefender) {
                    String computePowerIndication = computePowerIndication();
                    if (z) {
                        this.mWakeLock.setAcquired(true);
                        this.mTopIndicationView.switchIndication(computePowerIndication, null, true, new Action$$ExternalSyntheticLambda0(this, 3));
                        return;
                    }
                    this.mTopIndicationView.switchIndication(computePowerIndication, null, false, null);
                } else {
                    this.mTopIndicationView.switchIndication(NumberFormat.getPercentInstance().format(this.mBatteryLevel / 100.0f), null, false, null);
                }
            } else {
                this.mTopIndicationView.setVisibility(8);
                this.mTopIndicationView.setText((CharSequence) null);
                this.mLockScreenIndicationView.setVisibility(0);
                updatePersistentIndications(z, KeyguardUpdateMonitor.getCurrentUser());
            }
        }
    }

    public final void updatePersistentIndications(boolean z, int i) {
        boolean z2;
        boolean z3 = true;
        if (this.mOrganizationOwnedDevice) {
            this.mBackgroundExecutor.execute(new WMShell$7$$ExternalSyntheticLambda0(this, 4));
        } else {
            this.mRotateTextViewController.hideIndication(1);
        }
        this.mBackgroundExecutor.execute(new WMShell$7$$ExternalSyntheticLambda2(this, 5));
        if (this.mPowerPluggedIn || this.mEnableBatteryDefender) {
            String computePowerIndication = computePowerIndication();
            KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController = this.mRotateTextViewController;
            ColorStateList colorStateList = this.mInitialTextColorState;
            if (TextUtils.isEmpty(computePowerIndication)) {
                throw new IllegalStateException("message or icon must be set");
            } else if (colorStateList != null) {
                keyguardIndicationRotateTextViewController.updateIndication(3, new KeyguardIndication(computePowerIndication, colorStateList, null, null, null, null), z);
            } else {
                throw new IllegalStateException("text color must be set");
            }
        } else {
            this.mRotateTextViewController.hideIndication(3);
        }
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
        Objects.requireNonNull(keyguardUpdateMonitor);
        if (!keyguardUpdateMonitor.mUserIsUnlocked.get(i)) {
            KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController2 = this.mRotateTextViewController;
            CharSequence text = this.mContext.getResources().getText(17040624);
            ColorStateList colorStateList2 = this.mInitialTextColorState;
            if (TextUtils.isEmpty(text)) {
                throw new IllegalStateException("message or icon must be set");
            } else if (colorStateList2 != null) {
                keyguardIndicationRotateTextViewController2.updateIndication(8, new KeyguardIndication(text, colorStateList2, null, null, null, null), false);
            } else {
                throw new IllegalStateException("text color must be set");
            }
        } else {
            this.mRotateTextViewController.hideIndication(8);
        }
        updateTrust(i, getTrustGrantedIndication());
        if (!TextUtils.isEmpty(this.mAlignmentIndication)) {
            KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController3 = this.mRotateTextViewController;
            String str = this.mAlignmentIndication;
            ColorStateList valueOf = ColorStateList.valueOf(this.mContext.getColor(2131100395));
            if (TextUtils.isEmpty(str)) {
                throw new IllegalStateException("message or icon must be set");
            } else if (valueOf != null) {
                keyguardIndicationRotateTextViewController3.updateIndication(4, new KeyguardIndication(str, valueOf, null, null, null, null), true);
            } else {
                throw new IllegalStateException("text color must be set");
            }
        } else {
            this.mRotateTextViewController.hideIndication(4);
        }
        KeyguardUpdateMonitor keyguardUpdateMonitor2 = this.mKeyguardUpdateMonitor;
        Objects.requireNonNull(keyguardUpdateMonitor2);
        if (!keyguardUpdateMonitor2.mLogoutEnabled || KeyguardUpdateMonitor.getCurrentUser() == 0) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (z2) {
            KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController4 = this.mRotateTextViewController;
            String string = this.mContext.getResources().getString(17040386);
            ColorStateList colorAttr = Utils.getColorAttr(this.mContext, 17957103);
            Drawable drawable = this.mContext.getDrawable(2131232415);
            AvatarPickerActivity$$ExternalSyntheticLambda0 avatarPickerActivity$$ExternalSyntheticLambda0 = new AvatarPickerActivity$$ExternalSyntheticLambda0(this, 2);
            if (TextUtils.isEmpty(string)) {
                throw new IllegalStateException("message or icon must be set");
            } else if (colorAttr != null) {
                keyguardIndicationRotateTextViewController4.updateIndication(2, new KeyguardIndication(string, colorAttr, null, avatarPickerActivity$$ExternalSyntheticLambda0, drawable, null), false);
            } else {
                throw new IllegalStateException("text color must be set");
            }
        } else {
            this.mRotateTextViewController.hideIndication(2);
        }
        if (!TextUtils.isEmpty(null)) {
            KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController5 = this.mRotateTextViewController;
            Objects.requireNonNull(keyguardIndicationRotateTextViewController5);
            if (keyguardIndicationRotateTextViewController5.mIndicationMessages.keySet().size() <= 0) {
                z3 = false;
            }
            if (!z3) {
                KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController6 = this.mRotateTextViewController;
                ColorStateList colorStateList3 = this.mInitialTextColorState;
                if (TextUtils.isEmpty(null)) {
                    throw new IllegalStateException("message or icon must be set");
                } else if (colorStateList3 != null) {
                    keyguardIndicationRotateTextViewController6.updateIndication(7, new KeyguardIndication(null, colorStateList3, null, null, null, null), false);
                    return;
                } else {
                    throw new IllegalStateException("text color must be set");
                }
            }
        }
        this.mRotateTextViewController.hideIndication(7);
    }

    public final void updateTransient() {
        if (!TextUtils.isEmpty(this.mTransientIndication)) {
            KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController = this.mRotateTextViewController;
            CharSequence charSequence = this.mTransientIndication;
            Objects.requireNonNull(keyguardIndicationRotateTextViewController);
            ColorStateList colorStateList = keyguardIndicationRotateTextViewController.mInitialTextColorState;
            if (TextUtils.isEmpty(charSequence)) {
                throw new IllegalStateException("message or icon must be set");
            } else if (colorStateList != null) {
                keyguardIndicationRotateTextViewController.updateIndication(5, new KeyguardIndication(charSequence, colorStateList, null, null, null, 2600L), true);
            } else {
                throw new IllegalStateException("text color must be set");
            }
        } else {
            KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController2 = this.mRotateTextViewController;
            Objects.requireNonNull(keyguardIndicationRotateTextViewController2);
            keyguardIndicationRotateTextViewController2.hideIndication(5);
        }
        if (this.mDozing) {
            updateIndication(false);
        }
    }

    /* renamed from: -$$Nest$mhideBiometricMessage  reason: not valid java name */
    public static void m86$$Nest$mhideBiometricMessage(KeyguardIndicationController keyguardIndicationController) {
        Objects.requireNonNull(keyguardIndicationController);
        if (keyguardIndicationController.mBiometricMessage != null) {
            keyguardIndicationController.mBiometricMessage = null;
            keyguardIndicationController.mHandler.removeMessages(3);
            keyguardIndicationController.updateBiometricMessage();
        }
    }

    /* renamed from: -$$Nest$mshowTryFingerprintMsg  reason: not valid java name */
    public static void m87$$Nest$mshowTryFingerprintMsg(KeyguardIndicationController keyguardIndicationController, int i, String str) {
        Objects.requireNonNull(keyguardIndicationController);
        if (keyguardIndicationController.mKeyguardUpdateMonitor.isUdfpsSupported()) {
            KeyguardBypassController keyguardBypassController = keyguardIndicationController.mKeyguardBypassController;
            Objects.requireNonNull(keyguardBypassController);
            if (keyguardBypassController.userHasDeviceEntryIntent) {
                keyguardIndicationController.showBiometricMessage(2131952564);
            } else if (i == 9) {
                keyguardIndicationController.showBiometricMessage(2131952562);
            } else {
                keyguardIndicationController.showBiometricMessage(2131952535);
            }
        } else {
            keyguardIndicationController.showBiometricMessage(2131952562);
        }
        if (!TextUtils.isEmpty(str)) {
            keyguardIndicationController.mLockScreenIndicationView.announceForAccessibility(str);
        }
    }

    public final void updateTrust(int i, String str) {
        if (!TextUtils.isEmpty(str) && this.mKeyguardUpdateMonitor.getUserHasTrust(i)) {
            KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController = this.mRotateTextViewController;
            ColorStateList colorStateList = this.mInitialTextColorState;
            if (TextUtils.isEmpty(str)) {
                throw new IllegalStateException("message or icon must be set");
            } else if (colorStateList != null) {
                keyguardIndicationRotateTextViewController.updateIndication(6, new KeyguardIndication(str, colorStateList, null, null, null, null), false);
            } else {
                throw new IllegalStateException("text color must be set");
            }
        } else if (TextUtils.isEmpty(null) || !this.mKeyguardUpdateMonitor.getUserTrustIsManaged(i) || this.mKeyguardUpdateMonitor.getUserHasTrust(i)) {
            this.mRotateTextViewController.hideIndication(6);
        } else {
            KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController2 = this.mRotateTextViewController;
            ColorStateList colorStateList2 = this.mInitialTextColorState;
            if (TextUtils.isEmpty(null)) {
                throw new IllegalStateException("message or icon must be set");
            } else if (colorStateList2 != null) {
                keyguardIndicationRotateTextViewController2.updateIndication(6, new KeyguardIndication(null, colorStateList2, null, null, null, null), false);
            } else {
                throw new IllegalStateException("text color must be set");
            }
        }
    }

    @VisibleForTesting
    public void setPowerPluggedIn(boolean z) {
        this.mPowerPluggedIn = z;
    }
}
