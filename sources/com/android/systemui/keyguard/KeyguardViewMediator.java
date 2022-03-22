package com.android.systemui.keyguard;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.StatusBarManager;
import android.app.trust.TrustManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hardware.biometrics.BiometricSourceType;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.EventLog;
import android.util.Log;
import android.util.Slog;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import android.view.RemoteAnimationTarget;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.constraintlayout.motion.widget.MotionLayout$$ExternalSyntheticOutline0;
import androidx.core.view.ViewCompat$$ExternalSyntheticLambda0;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline0;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.policy.IKeyguardDismissCallback;
import com.android.internal.policy.IKeyguardStateCallback;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardConstants;
import com.android.keyguard.KeyguardDisplayManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda10;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3;
import com.android.keyguard.KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.KeyguardViewController;
import com.android.keyguard.ViewMediatorCallback;
import com.android.keyguard.mediator.ScreenOnCoordinator;
import com.android.systemui.CoreStartable;
import com.android.systemui.DejankUtils;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda0;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda4;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda0;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda19;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda20;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.wm.shell.bubbles.BubbleExpandedView$1$$ExternalSyntheticLambda0;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class KeyguardViewMediator extends CoreStartable implements StatusBarStateController.StateListener {
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public static final Intent USER_PRESENT_INTENT = new Intent("android.intent.action.USER_PRESENT").addFlags(606076928);
    public Lazy<ActivityLaunchAnimator> mActivityLaunchAnimator;
    public AlarmManager mAlarmManager;
    public boolean mAnimatingScreenOff;
    public boolean mAodShowing;
    public AudioManager mAudioManager;
    public boolean mBootCompleted;
    public boolean mBootSendUserPresent;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public CharSequence mCustomMessage;
    public int mDelayedProfileShowingSequence;
    public int mDelayedShowingSequence;
    public DeviceConfigProxy mDeviceConfig;
    public boolean mDeviceInteractive;
    public final DismissCallbackRegistry mDismissCallbackRegistry;
    public DozeParameters mDozeParameters;
    public boolean mDozing;
    public boolean mDreamOverlayShowing;
    public final DreamOverlayStateController mDreamOverlayStateController;
    public final FalsingCollector mFalsingCollector;
    public boolean mGoingToSleep;
    public Animation mHideAnimation;
    public boolean mHiding;
    public boolean mInGestureNavigationMode;
    public boolean mInputRestricted;
    public final InteractionJankMonitor mInteractionJankMonitor;
    public final KeyguardDisplayManager mKeyguardDisplayManager;
    public IRemoteAnimationRunner mKeyguardExitAnimationRunner;
    public final KeyguardStateController mKeyguardStateController;
    public final Lazy<KeyguardUnlockAnimationController> mKeyguardUnlockAnimationControllerLazy;
    public final Lazy<KeyguardViewController> mKeyguardViewControllerLazy;
    public boolean mLockLater;
    public final LockPatternUtils mLockPatternUtils;
    public int mLockSoundId;
    public int mLockSoundStreamId;
    public float mLockSoundVolume;
    public SoundPool mLockSounds;
    public final Lazy<NotificationShadeDepthController> mNotificationShadeDepthController;
    public final Lazy<NotificationShadeWindowController> mNotificationShadeWindowControllerLazy;
    public final AnonymousClass5 mOccludeAnimationController;
    public ActivityLaunchRemoteAnimationRunner mOccludeAnimationRunner;
    public final AnonymousClass1 mOnPropertiesChangedListener;
    public final PowerManager mPM;
    public boolean mPendingLock;
    public boolean mPendingReset;
    public final float mPowerButtonY;
    public final ScreenOffAnimationController mScreenOffAnimationController;
    public ScreenOnCoordinator mScreenOnCoordinator;
    public PowerManager.WakeLock mShowKeyguardWakeLock;
    public boolean mShowing;
    public boolean mShuttingDown;
    public StatusBarManager mStatusBarManager;
    public final SysuiStatusBarStateController mStatusBarStateController;
    public IRemoteAnimationFinishedCallback mSurfaceBehindRemoteAnimationFinishedCallback;
    public boolean mSurfaceBehindRemoteAnimationRunning;
    public boolean mSystemReady;
    public final TrustManager mTrustManager;
    public int mTrustedSoundId;
    public final Executor mUiBgExecutor;
    public int mUiSoundsStreamType;
    public int mUnlockSoundId;
    public final AnonymousClass6 mUnoccludeAnimationController;
    public ActivityLaunchRemoteAnimationRunner mUnoccludeAnimationRunner;
    public final KeyguardUpdateMonitor mUpdateMonitor;
    public final UserSwitcherController mUserSwitcherController;
    public boolean mWallpaperSupportsAmbientMode;
    public final float mWindowCornerRadius;
    public boolean mExternallyEnabled = true;
    public boolean mNeedToReshowWhenReenabled = false;
    public boolean mOccluded = false;
    public final SparseIntArray mLastSimStates = new SparseIntArray();
    public final SparseBooleanArray mSimWasLocked = new SparseBooleanArray();
    public String mPhoneState = TelephonyManager.EXTRA_STATE_IDLE;
    public boolean mWaitingUntilKeyguardVisible = false;
    public boolean mKeyguardDonePending = false;
    public boolean mHideAnimationRun = false;
    public boolean mHideAnimationRunning = false;
    public final ArrayList<IKeyguardStateCallback> mKeyguardStateCallbacks = new ArrayList<>();
    public boolean mPendingPinLock = false;
    public boolean mSurfaceBehindRemoteAnimationRequested = false;
    public final AnonymousClass2 mDreamOverlayStateCallback = new DreamOverlayStateController.Callback() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.2
        @Override // com.android.systemui.dreams.DreamOverlayStateController.Callback
        public final void onStateChanged() {
            KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
            DreamOverlayStateController dreamOverlayStateController = keyguardViewMediator.mDreamOverlayStateController;
            Objects.requireNonNull(dreamOverlayStateController);
            boolean z = true;
            if ((dreamOverlayStateController.mState & 1) == 0) {
                z = false;
            }
            keyguardViewMediator.mDreamOverlayShowing = z;
        }
    };
    public AnonymousClass3 mUpdateCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.3
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onUserInfoChanged() {
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
            if (KeyguardViewMediator.this.mLockPatternUtils.isSecure(i)) {
                KeyguardViewMediator.this.mLockPatternUtils.getDevicePolicyManager().reportSuccessfulBiometricAttempt(i);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onClockVisibilityChanged() {
            KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
            boolean z = KeyguardViewMediator.DEBUG;
            Objects.requireNonNull(keyguardViewMediator);
            keyguardViewMediator.adjustStatusBarLocked(false, false);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onDeviceProvisioned() {
            boolean z;
            KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
            boolean z2 = KeyguardViewMediator.DEBUG;
            keyguardViewMediator.sendUserPresentBroadcast();
            synchronized (KeyguardViewMediator.this) {
                Objects.requireNonNull(KeyguardViewMediator.this);
                if (!UserManager.isSplitSystemUser() || KeyguardUpdateMonitor.getCurrentUser() != 0) {
                    z = false;
                } else {
                    z = true;
                }
                if (z) {
                    KeyguardViewMediator.this.doKeyguardLocked(null);
                }
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onKeyguardVisibilityChanged(boolean z) {
            synchronized (KeyguardViewMediator.this) {
                if (!z) {
                    if (KeyguardViewMediator.this.mPendingPinLock) {
                        Log.i("KeyguardViewMediator", "PIN lock requested, starting keyguard");
                        KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                        keyguardViewMediator.mPendingPinLock = false;
                        keyguardViewMediator.doKeyguardLocked(null);
                    }
                }
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onSimStateChanged(int i, int i2, int i3) {
            boolean z;
            StringBuilder m = GridLayoutManager$$ExternalSyntheticOutline0.m("onSimStateChanged(subId=", i, ", slotId=", i2, ",state=");
            m.append(i3);
            m.append(")");
            Log.d("KeyguardViewMediator", m.toString());
            int size = KeyguardViewMediator.this.mKeyguardStateCallbacks.size();
            boolean isSimPinSecure = KeyguardViewMediator.this.mUpdateMonitor.isSimPinSecure();
            for (int i4 = size - 1; i4 >= 0; i4--) {
                try {
                    KeyguardViewMediator.this.mKeyguardStateCallbacks.get(i4).onSimSecureStateChanged(isSimPinSecure);
                } catch (RemoteException e) {
                    Slog.w("KeyguardViewMediator", "Failed to call onSimSecureStateChanged", e);
                    if (e instanceof DeadObjectException) {
                        KeyguardViewMediator.this.mKeyguardStateCallbacks.remove(i4);
                    }
                }
            }
            synchronized (KeyguardViewMediator.this) {
                int i5 = KeyguardViewMediator.this.mLastSimStates.get(i2);
                if (!(i5 == 2 || i5 == 3)) {
                    z = false;
                    KeyguardViewMediator.this.mLastSimStates.append(i2, i3);
                }
                z = true;
                KeyguardViewMediator.this.mLastSimStates.append(i2, i3);
            }
            if (i3 != 1) {
                if (i3 == 2 || i3 == 3) {
                    synchronized (KeyguardViewMediator.this) {
                        KeyguardViewMediator.this.mSimWasLocked.append(i2, true);
                        KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                        if (!keyguardViewMediator.mShowing) {
                            Log.d("KeyguardViewMediator", "INTENT_VALUE_ICC_LOCKED and keygaurd isn't showing; need to show keyguard so user can enter sim pin");
                            KeyguardViewMediator.this.doKeyguardLocked(null);
                        } else {
                            keyguardViewMediator.mPendingPinLock = true;
                            keyguardViewMediator.resetStateLocked();
                        }
                    }
                    return;
                } else if (i3 == 5) {
                    synchronized (KeyguardViewMediator.this) {
                        Log.d("KeyguardViewMediator", "READY, reset state? " + KeyguardViewMediator.this.mShowing);
                        KeyguardViewMediator keyguardViewMediator2 = KeyguardViewMediator.this;
                        if (keyguardViewMediator2.mShowing && keyguardViewMediator2.mSimWasLocked.get(i2, false)) {
                            Log.d("KeyguardViewMediator", "SIM moved to READY when the previously was locked. Reset the state.");
                            KeyguardViewMediator.this.mSimWasLocked.append(i2, false);
                            KeyguardViewMediator.this.resetStateLocked();
                        }
                    }
                    return;
                } else if (i3 != 6) {
                    if (i3 != 7) {
                        Log.v("KeyguardViewMediator", "Unspecific state: " + i3);
                        return;
                    }
                    synchronized (KeyguardViewMediator.this) {
                        if (!KeyguardViewMediator.this.mShowing) {
                            Log.d("KeyguardViewMediator", "PERM_DISABLED and keygaurd isn't showing.");
                            KeyguardViewMediator.this.doKeyguardLocked(null);
                        } else {
                            Log.d("KeyguardViewMediator", "PERM_DISABLED, resetStateLocked toshow permanently disabled message in lockscreen.");
                            KeyguardViewMediator.this.resetStateLocked();
                        }
                    }
                    return;
                }
            }
            synchronized (KeyguardViewMediator.this) {
                if (KeyguardViewMediator.this.shouldWaitForProvisioning()) {
                    KeyguardViewMediator keyguardViewMediator3 = KeyguardViewMediator.this;
                    if (!keyguardViewMediator3.mShowing) {
                        Log.d("KeyguardViewMediator", "ICC_ABSENT isn't showing, we need to show the keyguard since the device isn't provisioned yet.");
                        KeyguardViewMediator.this.doKeyguardLocked(null);
                    } else {
                        keyguardViewMediator3.resetStateLocked();
                    }
                }
                if (i3 == 1) {
                    if (z) {
                        Log.d("KeyguardViewMediator", "SIM moved to ABSENT when the previous state was locked. Reset the state.");
                        KeyguardViewMediator.this.resetStateLocked();
                    }
                    KeyguardViewMediator.this.mSimWasLocked.append(i2, false);
                }
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onUserSwitchComplete(int i) {
            UserInfo userInfo;
            if (KeyguardViewMediator.DEBUG) {
                Log.d("KeyguardViewMediator", String.format("onUserSwitchComplete %d", Integer.valueOf(i)));
            }
            if (i != 0 && (userInfo = UserManager.get(KeyguardViewMediator.this.mContext).getUserInfo(i)) != null && !KeyguardViewMediator.this.mLockPatternUtils.isSecure(i)) {
                if (userInfo.isGuest() || userInfo.isDemo()) {
                    KeyguardViewMediator.this.dismiss(null, null);
                }
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onUserSwitching(int i) {
            if (KeyguardViewMediator.DEBUG) {
                Log.d("KeyguardViewMediator", String.format("onUserSwitching %d", Integer.valueOf(i)));
            }
            synchronized (KeyguardViewMediator.this) {
                KeyguardViewMediator.this.resetKeyguardDonePendingLocked();
                if (KeyguardViewMediator.this.mLockPatternUtils.isLockScreenDisabled(i)) {
                    KeyguardViewMediator.this.dismiss(null, null);
                } else {
                    KeyguardViewMediator.this.resetStateLocked();
                }
                KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                Objects.requireNonNull(keyguardViewMediator);
                keyguardViewMediator.adjustStatusBarLocked(false, false);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onBiometricAuthFailed(BiometricSourceType biometricSourceType) {
            int currentUser = KeyguardUpdateMonitor.getCurrentUser();
            if (KeyguardViewMediator.this.mLockPatternUtils.isSecure(currentUser)) {
                KeyguardViewMediator.this.mLockPatternUtils.getDevicePolicyManager().reportFailedBiometricAttempt(currentUser);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onTrustChanged(int i) {
            if (i == KeyguardUpdateMonitor.getCurrentUser()) {
                synchronized (KeyguardViewMediator.this) {
                    KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                    keyguardViewMediator.notifyTrustedChangedLocked(keyguardViewMediator.mUpdateMonitor.getUserHasTrust(i));
                }
            }
        }
    };
    public AnonymousClass4 mViewMediatorCallback = new AnonymousClass4();
    public final AnonymousClass7 mDelayedLockBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.7
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if ("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_KEYGUARD".equals(intent.getAction())) {
                int intExtra = intent.getIntExtra("seq", 0);
                if (KeyguardViewMediator.DEBUG) {
                    KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(ExifInterface$$ExternalSyntheticOutline0.m("received DELAYED_KEYGUARD_ACTION with seq = ", intExtra, ", mDelayedShowingSequence = "), KeyguardViewMediator.this.mDelayedShowingSequence, "KeyguardViewMediator");
                }
                synchronized (KeyguardViewMediator.this) {
                    KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                    if (keyguardViewMediator.mDelayedShowingSequence == intExtra) {
                        keyguardViewMediator.doKeyguardLocked(null);
                    }
                }
            } else if ("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_LOCK".equals(intent.getAction())) {
                int intExtra2 = intent.getIntExtra("seq", 0);
                int intExtra3 = intent.getIntExtra("android.intent.extra.USER_ID", 0);
                if (intExtra3 != 0) {
                    synchronized (KeyguardViewMediator.this) {
                        KeyguardViewMediator keyguardViewMediator2 = KeyguardViewMediator.this;
                        if (keyguardViewMediator2.mDelayedProfileShowingSequence == intExtra2) {
                            keyguardViewMediator2.mTrustManager.setDeviceLockedForUser(intExtra3, true);
                        }
                    }
                }
            }
        }
    };
    public final AnonymousClass8 mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.8
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if ("android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction())) {
                synchronized (KeyguardViewMediator.this) {
                    KeyguardViewMediator.this.mShuttingDown = true;
                }
            }
        }
    };
    public AnonymousClass9 mHandler = new AnonymousClass9(Looper.myLooper());
    public final AnonymousClass10 mKeyguardGoingAwayRunnable = new AnonymousClass10();
    public final QSTileImpl$$ExternalSyntheticLambda0 mHideAnimationFinishedRunnable = new QSTileImpl$$ExternalSyntheticLambda0(this, 2);
    public boolean mShowHomeOverLockscreen = DeviceConfig.getBoolean("systemui", "nav_bar_handle_show_over_lockscreen", true);

    /* renamed from: com.android.systemui.keyguard.KeyguardViewMediator$10  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass10 implements Runnable {
        public AnonymousClass10() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x003e, code lost:
            if (r4.this$0.mWallpaperSupportsAmbientMode == false) goto L_0x0040;
         */
        /* JADX WARN: Code restructure failed: missing block: B:17:0x0060, code lost:
            if (r4.this$0.mWallpaperSupportsAmbientMode != false) goto L_0x0062;
         */
        /* JADX WARN: Removed duplicated region for block: B:14:0x0051  */
        /* JADX WARN: Removed duplicated region for block: B:21:0x0074  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x0086  */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void run() {
            /*
                r4 = this;
                java.lang.String r0 = "KeyguardViewMediator.mKeyGuardGoingAwayRunnable"
                android.os.Trace.beginSection(r0)
                boolean r0 = com.android.systemui.keyguard.KeyguardViewMediator.DEBUG
                if (r0 == 0) goto L_0x0010
                java.lang.String r0 = "KeyguardViewMediator"
                java.lang.String r1 = "keyguardGoingAway"
                android.util.Log.d(r0, r1)
            L_0x0010:
                com.android.systemui.keyguard.KeyguardViewMediator r0 = com.android.systemui.keyguard.KeyguardViewMediator.this
                dagger.Lazy<com.android.keyguard.KeyguardViewController> r0 = r0.mKeyguardViewControllerLazy
                java.lang.Object r0 = r0.get()
                com.android.keyguard.KeyguardViewController r0 = (com.android.keyguard.KeyguardViewController) r0
                r0.keyguardGoingAway()
                r0 = 0
                com.android.systemui.keyguard.KeyguardViewMediator r1 = com.android.systemui.keyguard.KeyguardViewMediator.this
                dagger.Lazy<com.android.keyguard.KeyguardViewController> r1 = r1.mKeyguardViewControllerLazy
                java.lang.Object r1 = r1.get()
                com.android.keyguard.KeyguardViewController r1 = (com.android.keyguard.KeyguardViewController) r1
                boolean r1 = r1.shouldDisableWindowAnimationsForUnlock()
                r2 = 2
                if (r1 != 0) goto L_0x0040
                com.android.systemui.keyguard.KeyguardViewMediator r1 = com.android.systemui.keyguard.KeyguardViewMediator.this
                com.android.keyguard.mediator.ScreenOnCoordinator r1 = r1.mScreenOnCoordinator
                java.util.Objects.requireNonNull(r1)
                boolean r1 = r1.wakeAndUnlocking
                if (r1 == 0) goto L_0x0041
                com.android.systemui.keyguard.KeyguardViewMediator r1 = com.android.systemui.keyguard.KeyguardViewMediator.this
                boolean r1 = r1.mWallpaperSupportsAmbientMode
                if (r1 != 0) goto L_0x0041
            L_0x0040:
                r0 = r2
            L_0x0041:
                com.android.systemui.keyguard.KeyguardViewMediator r1 = com.android.systemui.keyguard.KeyguardViewMediator.this
                dagger.Lazy<com.android.keyguard.KeyguardViewController> r1 = r1.mKeyguardViewControllerLazy
                java.lang.Object r1 = r1.get()
                com.android.keyguard.KeyguardViewController r1 = (com.android.keyguard.KeyguardViewController) r1
                boolean r1 = r1.isGoingToNotificationShade()
                if (r1 != 0) goto L_0x0062
                com.android.systemui.keyguard.KeyguardViewMediator r1 = com.android.systemui.keyguard.KeyguardViewMediator.this
                com.android.keyguard.mediator.ScreenOnCoordinator r1 = r1.mScreenOnCoordinator
                java.util.Objects.requireNonNull(r1)
                boolean r1 = r1.wakeAndUnlocking
                if (r1 == 0) goto L_0x0064
                com.android.systemui.keyguard.KeyguardViewMediator r1 = com.android.systemui.keyguard.KeyguardViewMediator.this
                boolean r1 = r1.mWallpaperSupportsAmbientMode
                if (r1 == 0) goto L_0x0064
            L_0x0062:
                r0 = r0 | 1
            L_0x0064:
                com.android.systemui.keyguard.KeyguardViewMediator r1 = com.android.systemui.keyguard.KeyguardViewMediator.this
                dagger.Lazy<com.android.keyguard.KeyguardViewController> r1 = r1.mKeyguardViewControllerLazy
                java.lang.Object r1 = r1.get()
                com.android.keyguard.KeyguardViewController r1 = (com.android.keyguard.KeyguardViewController) r1
                boolean r1 = r1.isUnlockWithWallpaper()
                if (r1 == 0) goto L_0x0076
                r0 = r0 | 4
            L_0x0076:
                com.android.systemui.keyguard.KeyguardViewMediator r1 = com.android.systemui.keyguard.KeyguardViewMediator.this
                dagger.Lazy<com.android.keyguard.KeyguardViewController> r1 = r1.mKeyguardViewControllerLazy
                java.lang.Object r1 = r1.get()
                com.android.keyguard.KeyguardViewController r1 = (com.android.keyguard.KeyguardViewController) r1
                boolean r1 = r1.shouldSubtleWindowAnimationsForUnlock()
                if (r1 == 0) goto L_0x0088
                r0 = r0 | 8
            L_0x0088:
                com.android.systemui.keyguard.KeyguardViewMediator r1 = com.android.systemui.keyguard.KeyguardViewMediator.this
                com.android.keyguard.KeyguardUpdateMonitor r1 = r1.mUpdateMonitor
                java.util.Objects.requireNonNull(r1)
                r3 = 1
                r1.mKeyguardGoingAway = r3
                r1.updateBiometricListeningState(r2)
                com.android.systemui.keyguard.KeyguardViewMediator r1 = com.android.systemui.keyguard.KeyguardViewMediator.this
                dagger.Lazy<com.android.keyguard.KeyguardViewController> r1 = r1.mKeyguardViewControllerLazy
                java.lang.Object r1 = r1.get()
                com.android.keyguard.KeyguardViewController r1 = (com.android.keyguard.KeyguardViewController) r1
                r1.setKeyguardGoingAwayState(r3)
                com.android.systemui.keyguard.KeyguardViewMediator r4 = com.android.systemui.keyguard.KeyguardViewMediator.this
                java.util.concurrent.Executor r4 = r4.mUiBgExecutor
                com.android.systemui.keyguard.KeyguardViewMediator$10$$ExternalSyntheticLambda0 r1 = new com.android.systemui.keyguard.KeyguardViewMediator$10$$ExternalSyntheticLambda0
                r1.<init>()
                r4.execute(r1)
                android.os.Trace.endSection()
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardViewMediator.AnonymousClass10.run():void");
        }
    }

    /* renamed from: com.android.systemui.keyguard.KeyguardViewMediator$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass4 implements ViewMediatorCallback {
        public AnonymousClass4() {
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public final CharSequence consumeCustomMessage() {
            KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
            CharSequence charSequence = keyguardViewMediator.mCustomMessage;
            keyguardViewMediator.mCustomMessage = null;
            return charSequence;
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public final boolean isScreenOn() {
            return KeyguardViewMediator.this.mDeviceInteractive;
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public final void keyguardDoneDrawing() {
            Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#keyguardDoneDrawing");
            KeyguardViewMediator.this.mHandler.sendEmptyMessage(8);
            Trace.endSection();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public final void keyguardDonePending(int i) {
            Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#keyguardDonePending");
            if (KeyguardViewMediator.DEBUG) {
                Log.d("KeyguardViewMediator", "keyguardDonePending");
            }
            if (i != ActivityManager.getCurrentUser()) {
                Trace.endSection();
                return;
            }
            KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
            keyguardViewMediator.mKeyguardDonePending = true;
            keyguardViewMediator.mHideAnimationRun = true;
            keyguardViewMediator.mHideAnimationRunning = true;
            keyguardViewMediator.mKeyguardViewControllerLazy.get().startPreHideAnimation(KeyguardViewMediator.this.mHideAnimationFinishedRunnable);
            KeyguardViewMediator.this.mHandler.sendEmptyMessageDelayed(13, 3000L);
            Trace.endSection();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public final void keyguardGone() {
            Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#keyguardGone");
            if (KeyguardViewMediator.DEBUG) {
                Log.d("KeyguardViewMediator", "keyguardGone");
            }
            KeyguardViewMediator.this.mKeyguardViewControllerLazy.get().setKeyguardGoingAwayState(false);
            KeyguardDisplayManager keyguardDisplayManager = KeyguardViewMediator.this.mKeyguardDisplayManager;
            Objects.requireNonNull(keyguardDisplayManager);
            if (keyguardDisplayManager.mShowing) {
                if (KeyguardDisplayManager.DEBUG) {
                    Log.v("KeyguardDisplayManager", "hide");
                }
                MediaRouter mediaRouter = keyguardDisplayManager.mMediaRouter;
                if (mediaRouter != null) {
                    mediaRouter.removeCallback(keyguardDisplayManager.mMediaRouterCallback);
                }
                keyguardDisplayManager.updateDisplays(false);
            }
            keyguardDisplayManager.mShowing = false;
            Trace.endSection();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public final void onBouncerVisiblityChanged(boolean z) {
            synchronized (KeyguardViewMediator.this) {
                if (z) {
                    KeyguardViewMediator.this.mPendingPinLock = false;
                }
                KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                boolean z2 = KeyguardViewMediator.DEBUG;
                keyguardViewMediator.adjustStatusBarLocked(z, false);
            }
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public final void onCancelClicked() {
            KeyguardViewMediator.this.mKeyguardViewControllerLazy.get().onCancelClicked();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public final void playTrustedSound() {
            KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
            boolean z = KeyguardViewMediator.DEBUG;
            Objects.requireNonNull(keyguardViewMediator);
            keyguardViewMediator.playSound(keyguardViewMediator.mTrustedSoundId);
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public final void readyForKeyguardDone() {
            Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#readyForKeyguardDone");
            KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
            if (keyguardViewMediator.mKeyguardDonePending) {
                keyguardViewMediator.mKeyguardDonePending = false;
                keyguardViewMediator.tryKeyguardDone();
            }
            Trace.endSection();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public final void resetKeyguard() {
            KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
            boolean z = KeyguardViewMediator.DEBUG;
            keyguardViewMediator.resetStateLocked();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public final void setNeedsInput(boolean z) {
            KeyguardViewMediator.this.mKeyguardViewControllerLazy.get().setNeedsInput(z);
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public final void userActivity() {
            KeyguardViewMediator.this.userActivity();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public final int getBouncerPromptReason() {
            boolean z;
            boolean z2;
            boolean z3;
            boolean z4;
            int currentUser = KeyguardUpdateMonitor.getCurrentUser();
            KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardViewMediator.this.mUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            Assert.isMainThread();
            boolean z5 = keyguardUpdateMonitor.mUserTrustIsUsuallyManaged.get(currentUser);
            KeyguardUpdateMonitor keyguardUpdateMonitor2 = KeyguardViewMediator.this.mUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor2);
            boolean booleanValue = ((Boolean) DejankUtils.whitelistIpcs(new KeyguardUpdateMonitor$$ExternalSyntheticLambda10(keyguardUpdateMonitor2, currentUser))).booleanValue();
            keyguardUpdateMonitor2.mIsFaceEnrolled = booleanValue;
            boolean z6 = true;
            if (!booleanValue || keyguardUpdateMonitor2.isFaceDisabled(currentUser)) {
                z = false;
            } else {
                z = true;
            }
            if (z || keyguardUpdateMonitor2.isUnlockWithFingerprintPossible(currentUser)) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z5 || z2) {
                z3 = true;
            } else {
                z3 = false;
            }
            KeyguardUpdateMonitor keyguardUpdateMonitor3 = KeyguardViewMediator.this.mUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor3);
            KeyguardUpdateMonitor.StrongAuthTracker strongAuthTracker = keyguardUpdateMonitor3.mStrongAuthTracker;
            int strongAuthForUser = strongAuthTracker.getStrongAuthForUser(currentUser);
            if (z3) {
                if ((strongAuthTracker.getStrongAuthForUser(KeyguardUpdateMonitor.getCurrentUser()) & 1) == 0) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                if (!z4) {
                    return 1;
                }
            }
            if (z3 && (strongAuthForUser & 16) != 0) {
                return 2;
            }
            if ((strongAuthForUser & 2) != 0) {
                return 3;
            }
            if (z5 && (strongAuthForUser & 4) != 0) {
                return 4;
            }
            if (z3) {
                if ((strongAuthForUser & 8) != 0) {
                    return 5;
                }
                KeyguardUpdateMonitor keyguardUpdateMonitor4 = KeyguardViewMediator.this.mUpdateMonitor;
                Objects.requireNonNull(keyguardUpdateMonitor4);
                if (!keyguardUpdateMonitor4.mFingerprintLockedOut && !keyguardUpdateMonitor4.mFingerprintLockedOutPermanent) {
                    z6 = false;
                }
                if (z6) {
                    return 5;
                }
            }
            if (z3 && (strongAuthForUser & 64) != 0) {
                return 6;
            }
            if (!z3 || (strongAuthForUser & 128) == 0) {
                return 0;
            }
            return 7;
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public final void keyguardDone(int i) {
            if (i == ActivityManager.getCurrentUser()) {
                if (KeyguardViewMediator.DEBUG) {
                    Log.d("KeyguardViewMediator", "keyguardDone");
                }
                KeyguardViewMediator.this.tryKeyguardDone();
            }
        }
    }

    /* renamed from: com.android.systemui.keyguard.KeyguardViewMediator$9  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass9 extends Handler {
        public AnonymousClass9(Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            boolean z;
            boolean z2 = true;
            switch (message.what) {
                case 1:
                    KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                    Bundle bundle = (Bundle) message.obj;
                    boolean z3 = KeyguardViewMediator.DEBUG;
                    Objects.requireNonNull(keyguardViewMediator);
                    Trace.beginSection("KeyguardViewMediator#handleShow");
                    int currentUser = KeyguardUpdateMonitor.getCurrentUser();
                    if (keyguardViewMediator.mLockPatternUtils.isSecure(currentUser)) {
                        keyguardViewMediator.mLockPatternUtils.getDevicePolicyManager().reportKeyguardSecured(currentUser);
                    }
                    synchronized (keyguardViewMediator) {
                        if (!keyguardViewMediator.mSystemReady) {
                            if (KeyguardViewMediator.DEBUG) {
                                Log.d("KeyguardViewMediator", "ignoring handleShow because system is not ready.");
                            }
                            return;
                        }
                        if (KeyguardViewMediator.DEBUG) {
                            Log.d("KeyguardViewMediator", "handleShow");
                        }
                        keyguardViewMediator.mHiding = false;
                        keyguardViewMediator.mKeyguardExitAnimationRunner = null;
                        keyguardViewMediator.mScreenOnCoordinator.setWakeAndUnlocking(false);
                        keyguardViewMediator.mPendingLock = false;
                        keyguardViewMediator.setShowingLocked(true, false);
                        keyguardViewMediator.mKeyguardViewControllerLazy.get().show$2();
                        keyguardViewMediator.resetKeyguardDonePendingLocked();
                        keyguardViewMediator.mHideAnimationRun = false;
                        keyguardViewMediator.adjustStatusBarLocked(false, false);
                        keyguardViewMediator.userActivity();
                        KeyguardUpdateMonitor keyguardUpdateMonitor = keyguardViewMediator.mUpdateMonitor;
                        Objects.requireNonNull(keyguardUpdateMonitor);
                        keyguardUpdateMonitor.mKeyguardGoingAway = false;
                        keyguardUpdateMonitor.updateBiometricListeningState(2);
                        keyguardViewMediator.mKeyguardViewControllerLazy.get().setKeyguardGoingAwayState(false);
                        keyguardViewMediator.mShowKeyguardWakeLock.release();
                        KeyguardDisplayManager keyguardDisplayManager = keyguardViewMediator.mKeyguardDisplayManager;
                        Objects.requireNonNull(keyguardDisplayManager);
                        if (!keyguardDisplayManager.mShowing) {
                            if (KeyguardDisplayManager.DEBUG) {
                                Log.v("KeyguardDisplayManager", "show");
                            }
                            MediaRouter mediaRouter = keyguardDisplayManager.mMediaRouter;
                            if (mediaRouter != null) {
                                mediaRouter.addCallback(4, keyguardDisplayManager.mMediaRouterCallback, 8);
                            } else {
                                Log.w("KeyguardDisplayManager", "MediaRouter not yet initialized");
                            }
                            keyguardDisplayManager.updateDisplays(true);
                        }
                        keyguardDisplayManager.mShowing = true;
                        keyguardViewMediator.mLockPatternUtils.scheduleNonStrongBiometricIdleTimeout(KeyguardUpdateMonitor.getCurrentUser());
                        Trace.endSection();
                        return;
                    }
                case 2:
                    KeyguardViewMediator keyguardViewMediator2 = KeyguardViewMediator.this;
                    boolean z4 = KeyguardViewMediator.DEBUG;
                    keyguardViewMediator2.handleHide();
                    return;
                case 3:
                    KeyguardViewMediator keyguardViewMediator3 = KeyguardViewMediator.this;
                    boolean z5 = KeyguardViewMediator.DEBUG;
                    Objects.requireNonNull(keyguardViewMediator3);
                    synchronized (keyguardViewMediator3) {
                        if (KeyguardViewMediator.DEBUG) {
                            Log.d("KeyguardViewMediator", "handleReset");
                        }
                        keyguardViewMediator3.mKeyguardViewControllerLazy.get().reset(true);
                    }
                    return;
                case 4:
                    Trace.beginSection("KeyguardViewMediator#handleMessage VERIFY_UNLOCK");
                    KeyguardViewMediator keyguardViewMediator4 = KeyguardViewMediator.this;
                    boolean z6 = KeyguardViewMediator.DEBUG;
                    Objects.requireNonNull(keyguardViewMediator4);
                    Trace.beginSection("KeyguardViewMediator#handleVerifyUnlock");
                    synchronized (keyguardViewMediator4) {
                        if (KeyguardViewMediator.DEBUG) {
                            Log.d("KeyguardViewMediator", "handleVerifyUnlock");
                        }
                        keyguardViewMediator4.setShowingLocked(true, false);
                        keyguardViewMediator4.mKeyguardViewControllerLazy.get().dismissAndCollapse();
                    }
                    Trace.endSection();
                    Trace.endSection();
                    return;
                case 5:
                    KeyguardViewMediator.m64$$Nest$mhandleNotifyFinishedGoingToSleep(KeyguardViewMediator.this);
                    return;
                case FalsingManager.VERSION /* 6 */:
                case 15:
                case 16:
                default:
                    return;
                case 7:
                    Trace.beginSection("KeyguardViewMediator#handleMessage KEYGUARD_DONE");
                    KeyguardViewMediator keyguardViewMediator5 = KeyguardViewMediator.this;
                    boolean z7 = KeyguardViewMediator.DEBUG;
                    keyguardViewMediator5.handleKeyguardDone();
                    Trace.endSection();
                    return;
                case 8:
                    Trace.beginSection("KeyguardViewMediator#handleMessage KEYGUARD_DONE_DRAWING");
                    KeyguardViewMediator.m63$$Nest$mhandleKeyguardDoneDrawing(KeyguardViewMediator.this);
                    Trace.endSection();
                    return;
                case 9:
                    Trace.beginSection("KeyguardViewMediator#handleMessage SET_OCCLUDED");
                    KeyguardViewMediator keyguardViewMediator6 = KeyguardViewMediator.this;
                    if (message.arg1 != 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (message.arg2 == 0) {
                        z2 = false;
                    }
                    KeyguardViewMediator.m67$$Nest$mhandleSetOccluded(keyguardViewMediator6, z, z2);
                    Trace.endSection();
                    return;
                case 10:
                    synchronized (KeyguardViewMediator.this) {
                        boolean z8 = KeyguardViewMediator.DEBUG;
                        KeyguardViewMediator.this.doKeyguardLocked((Bundle) message.obj);
                        KeyguardViewMediator keyguardViewMediator7 = KeyguardViewMediator.this;
                        DejankUtils.whitelistIpcs(new KeyguardViewMediator$$ExternalSyntheticLambda5(keyguardViewMediator7, keyguardViewMediator7.mShowing));
                        keyguardViewMediator7.updateInputRestrictedLocked();
                        keyguardViewMediator7.mUiBgExecutor.execute(new BubbleExpandedView$1$$ExternalSyntheticLambda0(keyguardViewMediator7, 3));
                    }
                    return;
                case QSTileImpl.H.STALE /* 11 */:
                    DismissMessage dismissMessage = (DismissMessage) message.obj;
                    KeyguardViewMediator keyguardViewMediator8 = KeyguardViewMediator.this;
                    Objects.requireNonNull(dismissMessage);
                    IKeyguardDismissCallback iKeyguardDismissCallback = dismissMessage.mCallback;
                    CharSequence charSequence = dismissMessage.mMessage;
                    boolean z9 = KeyguardViewMediator.DEBUG;
                    Objects.requireNonNull(keyguardViewMediator8);
                    if (keyguardViewMediator8.mShowing) {
                        if (iKeyguardDismissCallback != null) {
                            DismissCallbackRegistry dismissCallbackRegistry = keyguardViewMediator8.mDismissCallbackRegistry;
                            Objects.requireNonNull(dismissCallbackRegistry);
                            dismissCallbackRegistry.mDismissCallbacks.add(new DismissCallbackWrapper(iKeyguardDismissCallback));
                        }
                        keyguardViewMediator8.mCustomMessage = charSequence;
                        keyguardViewMediator8.mKeyguardViewControllerLazy.get().dismissAndCollapse();
                        return;
                    } else if (iKeyguardDismissCallback != null) {
                        try {
                            iKeyguardDismissCallback.onDismissError();
                            return;
                        } catch (RemoteException e) {
                            Log.i("DismissCallbackWrapper", "Failed to call callback", e);
                            return;
                        }
                    } else {
                        return;
                    }
                case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                    Trace.beginSection("KeyguardViewMediator#handleMessage START_KEYGUARD_EXIT_ANIM");
                    KeyguardViewMediator.this.mNotificationShadeWindowControllerLazy.get().batchApplyWindowLayoutParams(new KeyguardViewMediator$9$$ExternalSyntheticLambda0(this, (StartKeyguardExitAnimParams) message.obj, 0));
                    Trace.endSection();
                    return;
                case QS.VERSION /* 13 */:
                    Trace.beginSection("KeyguardViewMediator#handleMessage KEYGUARD_DONE_PENDING_TIMEOUT");
                    Log.w("KeyguardViewMediator", "Timeout while waiting for activity drawn!");
                    Trace.endSection();
                    return;
                case 14:
                    Trace.beginSection("KeyguardViewMediator#handleMessage NOTIFY_STARTED_WAKING_UP");
                    KeyguardViewMediator.m66$$Nest$mhandleNotifyStartedWakingUp(KeyguardViewMediator.this);
                    Trace.endSection();
                    return;
                case 17:
                    KeyguardViewMediator.m65$$Nest$mhandleNotifyStartedGoingToSleep(KeyguardViewMediator.this);
                    return;
                case 18:
                    KeyguardViewMediator.m68$$Nest$mhandleSystemReady(KeyguardViewMediator.this);
                    return;
                case 19:
                    Trace.beginSection("KeyguardViewMediator#handleMessage CANCEL_KEYGUARD_EXIT_ANIM");
                    KeyguardViewMediator keyguardViewMediator9 = KeyguardViewMediator.this;
                    boolean z10 = KeyguardViewMediator.DEBUG;
                    Objects.requireNonNull(keyguardViewMediator9);
                    keyguardViewMediator9.mSurfaceBehindRemoteAnimationRequested = true;
                    try {
                        ActivityTaskManager.getService().keyguardGoingAway(6);
                    } catch (RemoteException e2) {
                        keyguardViewMediator9.mSurfaceBehindRemoteAnimationRequested = false;
                        e2.printStackTrace();
                    }
                    keyguardViewMediator9.onKeyguardExitRemoteAnimationFinished(true);
                    Trace.endSection();
                    return;
            }
        }
    }

    /* loaded from: classes.dex */
    public class ActivityLaunchRemoteAnimationRunner extends IRemoteAnimationRunner.Stub {
        public final ActivityLaunchAnimator.Controller mActivityLaunchController;
        public ActivityLaunchAnimator.Runner mRunner;

        public ActivityLaunchRemoteAnimationRunner(ActivityLaunchAnimator.Controller controller) {
            this.mActivityLaunchController = controller;
        }

        public final void onAnimationCancelled() throws RemoteException {
            ActivityLaunchAnimator.Runner runner = this.mRunner;
            if (runner != null) {
                runner.onAnimationCancelled();
            }
        }

        public final void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) throws RemoteException {
            ActivityLaunchAnimator.Runner createRunner = KeyguardViewMediator.this.mActivityLaunchAnimator.get().createRunner(this.mActivityLaunchController);
            this.mRunner = createRunner;
            createRunner.onAnimationStart(i, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback);
        }
    }

    /* JADX WARN: Type inference failed for: r4v10, types: [com.android.systemui.keyguard.KeyguardViewMediator$8] */
    /* JADX WARN: Type inference failed for: r4v4, types: [com.android.systemui.keyguard.KeyguardViewMediator$2] */
    /* JADX WARN: Type inference failed for: r4v5, types: [com.android.systemui.keyguard.KeyguardViewMediator$3] */
    /* JADX WARN: Type inference failed for: r4v7, types: [com.android.systemui.animation.ActivityLaunchAnimator$Controller, com.android.systemui.keyguard.KeyguardViewMediator$5] */
    /* JADX WARN: Type inference failed for: r4v9, types: [com.android.systemui.keyguard.KeyguardViewMediator$7] */
    /* JADX WARN: Type inference failed for: r5v0, types: [com.android.systemui.animation.ActivityLaunchAnimator$Controller, com.android.systemui.keyguard.KeyguardViewMediator$6] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public KeyguardViewMediator(android.content.Context r8, com.android.systemui.classifier.FalsingCollector r9, com.android.internal.widget.LockPatternUtils r10, com.android.systemui.broadcast.BroadcastDispatcher r11, dagger.Lazy<com.android.keyguard.KeyguardViewController> r12, com.android.systemui.keyguard.DismissCallbackRegistry r13, com.android.keyguard.KeyguardUpdateMonitor r14, com.android.systemui.dump.DumpManager r15, java.util.concurrent.Executor r16, android.os.PowerManager r17, android.app.trust.TrustManager r18, com.android.systemui.statusbar.policy.UserSwitcherController r19, com.android.systemui.util.DeviceConfigProxy r20, com.android.systemui.navigationbar.NavigationModeController r21, com.android.keyguard.KeyguardDisplayManager r22, com.android.systemui.statusbar.phone.DozeParameters r23, com.android.systemui.statusbar.SysuiStatusBarStateController r24, com.android.systemui.statusbar.policy.KeyguardStateController r25, dagger.Lazy<com.android.systemui.keyguard.KeyguardUnlockAnimationController> r26, com.android.systemui.statusbar.phone.ScreenOffAnimationController r27, dagger.Lazy<com.android.systemui.statusbar.NotificationShadeDepthController> r28, com.android.keyguard.mediator.ScreenOnCoordinator r29, com.android.internal.jank.InteractionJankMonitor r30, com.android.systemui.dreams.DreamOverlayStateController r31, dagger.Lazy<com.android.systemui.statusbar.NotificationShadeWindowController> r32, dagger.Lazy<com.android.systemui.animation.ActivityLaunchAnimator> r33) {
        /*
            Method dump skipped, instructions count: 316
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardViewMediator.<init>(android.content.Context, com.android.systemui.classifier.FalsingCollector, com.android.internal.widget.LockPatternUtils, com.android.systemui.broadcast.BroadcastDispatcher, dagger.Lazy, com.android.systemui.keyguard.DismissCallbackRegistry, com.android.keyguard.KeyguardUpdateMonitor, com.android.systemui.dump.DumpManager, java.util.concurrent.Executor, android.os.PowerManager, android.app.trust.TrustManager, com.android.systemui.statusbar.policy.UserSwitcherController, com.android.systemui.util.DeviceConfigProxy, com.android.systemui.navigationbar.NavigationModeController, com.android.keyguard.KeyguardDisplayManager, com.android.systemui.statusbar.phone.DozeParameters, com.android.systemui.statusbar.SysuiStatusBarStateController, com.android.systemui.statusbar.policy.KeyguardStateController, dagger.Lazy, com.android.systemui.statusbar.phone.ScreenOffAnimationController, dagger.Lazy, com.android.keyguard.mediator.ScreenOnCoordinator, com.android.internal.jank.InteractionJankMonitor, com.android.systemui.dreams.DreamOverlayStateController, dagger.Lazy, dagger.Lazy):void");
    }

    public final void handleStartKeyguardExitAnimation(long j, long j2, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, final IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
        Trace.beginSection("KeyguardViewMediator#handleStartKeyguardExitAnimation");
        if (DEBUG) {
            Log.d("KeyguardViewMediator", "handleStartKeyguardExitAnimation startTime=" + j + " fadeoutDuration=" + j2);
        }
        synchronized (this) {
            if (this.mHiding || this.mSurfaceBehindRemoteAnimationRequested || this.mKeyguardStateController.isFlingingToDismissKeyguardDuringSwipeGesture()) {
                this.mHiding = false;
                IRemoteAnimationRunner iRemoteAnimationRunner = this.mKeyguardExitAnimationRunner;
                this.mKeyguardExitAnimationRunner = null;
                ScreenOnCoordinator screenOnCoordinator = this.mScreenOnCoordinator;
                Objects.requireNonNull(screenOnCoordinator);
                if (screenOnCoordinator.wakeAndUnlocking) {
                    this.mKeyguardViewControllerLazy.get().getViewRootImpl().setReportNextDraw();
                    this.mScreenOnCoordinator.setWakeAndUnlocking(false);
                }
                LatencyTracker.getInstance(this.mContext).onActionEnd(11);
                boolean z = KeyguardService.sEnableRemoteKeyguardGoingAwayAnimation;
                if (z && iRemoteAnimationRunner != null && iRemoteAnimationFinishedCallback != null) {
                    IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback2 = new IRemoteAnimationFinishedCallback() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.11
                        public final IBinder asBinder() {
                            return iRemoteAnimationFinishedCallback.asBinder();
                        }

                        public final void onAnimationFinished() throws RemoteException {
                            try {
                                iRemoteAnimationFinishedCallback.onAnimationFinished();
                            } catch (RemoteException e) {
                                Slog.w("KeyguardViewMediator", "Failed to call onAnimationFinished", e);
                            }
                            KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                            boolean z2 = KeyguardViewMediator.DEBUG;
                            keyguardViewMediator.onKeyguardExitFinished();
                            KeyguardViewMediator.this.mKeyguardViewControllerLazy.get().hide(0L, 0L);
                            KeyguardViewMediator.this.mInteractionJankMonitor.end(29);
                        }
                    };
                    try {
                        this.mInteractionJankMonitor.begin(createInteractionJankMonitorConf("RunRemoteAnimation"));
                        iRemoteAnimationRunner.onAnimationStart(7, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback2);
                    } catch (RemoteException e) {
                        Slog.w("KeyguardViewMediator", "Failed to call onAnimationStart", e);
                    }
                } else if (!z || this.mStatusBarStateController.leaveOpenOnKeyguardHide() || remoteAnimationTargetArr == null || remoteAnimationTargetArr.length <= 0) {
                    this.mInteractionJankMonitor.begin(createInteractionJankMonitorConf("RemoteAnimationDisabled"));
                    this.mKeyguardViewControllerLazy.get().hide(j, j2);
                    this.mContext.getMainExecutor().execute(new ScreenDecorations$$ExternalSyntheticLambda0(this, iRemoteAnimationFinishedCallback, remoteAnimationTargetArr, 1));
                    onKeyguardExitFinished();
                } else {
                    this.mSurfaceBehindRemoteAnimationFinishedCallback = iRemoteAnimationFinishedCallback;
                    this.mSurfaceBehindRemoteAnimationRunning = true;
                    this.mInteractionJankMonitor.begin(createInteractionJankMonitorConf("DismissPanel"));
                    this.mKeyguardUnlockAnimationControllerLazy.get().notifyStartSurfaceBehindRemoteAnimation(remoteAnimationTargetArr[0], j, this.mSurfaceBehindRemoteAnimationRequested);
                }
                Trace.endSection();
                return;
            }
            if (iRemoteAnimationFinishedCallback != null) {
                try {
                    iRemoteAnimationFinishedCallback.onAnimationFinished();
                } catch (RemoteException e2) {
                    Slog.w("KeyguardViewMediator", "Failed to call onAnimationFinished", e2);
                }
            }
            setShowingLocked(this.mShowing, true);
            return;
        }
    }

    @Override // com.android.systemui.CoreStartable
    public final void onBootCompleted() {
        synchronized (this) {
            if (this.mContext.getResources().getBoolean(17891665)) {
                UserSwitcherController userSwitcherController = this.mUserSwitcherController;
                Objects.requireNonNull(userSwitcherController);
                if (userSwitcherController.isDeviceAllowedToAddGuest()) {
                    userSwitcherController.guaranteeGuestPresent();
                } else {
                    userSwitcherController.mDeviceProvisionedController.addCallback(userSwitcherController.mGuaranteeGuestPresentAfterProvisioned);
                }
            }
            this.mBootCompleted = true;
            adjustStatusBarLocked(false, true);
            if (this.mBootSendUserPresent) {
                sendUserPresentBroadcast();
            }
        }
    }

    public final void resetKeyguardDonePendingLocked() {
        this.mKeyguardDonePending = false;
        this.mHandler.removeMessages(13);
    }

    public final void sendUserPresentBroadcast() {
        synchronized (this) {
            if (this.mBootCompleted) {
                final int currentUser = KeyguardUpdateMonitor.getCurrentUser();
                final UserHandle userHandle = new UserHandle(currentUser);
                final UserManager userManager = (UserManager) this.mContext.getSystemService("user");
                this.mUiBgExecutor.execute(new Runnable() { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                        UserManager userManager2 = userManager;
                        UserHandle userHandle2 = userHandle;
                        int i = currentUser;
                        boolean z = KeyguardViewMediator.DEBUG;
                        Objects.requireNonNull(keyguardViewMediator);
                        for (int i2 : userManager2.getProfileIdsWithDisabled(userHandle2.getIdentifier())) {
                            keyguardViewMediator.mContext.sendBroadcastAsUser(KeyguardViewMediator.USER_PRESENT_INTENT, UserHandle.of(i2));
                        }
                        keyguardViewMediator.mLockPatternUtils.userPresent(i);
                    }
                });
            } else {
                this.mBootSendUserPresent = true;
            }
        }
    }

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        synchronized (this) {
            setupLocked();
        }
    }

    public final void startKeyguardExitAnimation(int i, long j, long j2, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
        Trace.beginSection("KeyguardViewMediator#startKeyguardExitAnimation");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(12, new StartKeyguardExitAnimParams(j, j2, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback)));
        Trace.endSection();
    }

    /* loaded from: classes.dex */
    public static class DismissMessage {
        public final IKeyguardDismissCallback mCallback;
        public final CharSequence mMessage;

        public DismissMessage(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
            this.mCallback = iKeyguardDismissCallback;
            this.mMessage = charSequence;
        }
    }

    /* loaded from: classes.dex */
    public static class StartKeyguardExitAnimParams {
        public long fadeoutDuration;
        public RemoteAnimationTarget[] mApps;
        public IRemoteAnimationFinishedCallback mFinishedCallback;
        public RemoteAnimationTarget[] mNonApps;
        public RemoteAnimationTarget[] mWallpapers;
        public long startTime;

        public StartKeyguardExitAnimParams(long j, long j2, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
            this.startTime = j;
            this.fadeoutDuration = j2;
            this.mApps = remoteAnimationTargetArr;
            this.mWallpapers = remoteAnimationTargetArr2;
            this.mNonApps = remoteAnimationTargetArr3;
            this.mFinishedCallback = iRemoteAnimationFinishedCallback;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0030, code lost:
        if (r5 != false) goto L_0x0032;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void adjustStatusBarLocked(boolean r4, boolean r5) {
        /*
            r3 = this;
            android.app.StatusBarManager r0 = r3.mStatusBarManager
            if (r0 != 0) goto L_0x0011
            android.content.Context r0 = r3.mContext
            java.lang.String r1 = "statusbar"
            java.lang.Object r0 = r0.getSystemService(r1)
            android.app.StatusBarManager r0 = (android.app.StatusBarManager) r0
            r3.mStatusBarManager = r0
        L_0x0011:
            android.app.StatusBarManager r0 = r3.mStatusBarManager
            java.lang.String r1 = "KeyguardViewMediator"
            if (r0 != 0) goto L_0x001d
            java.lang.String r3 = "Could not get status bar manager"
            android.util.Log.w(r1, r3)
            goto L_0x0084
        L_0x001d:
            r2 = 0
            if (r5 == 0) goto L_0x0023
            r0.disable(r2)
        L_0x0023:
            if (r4 != 0) goto L_0x0032
            boolean r5 = r3.mShowing
            if (r5 == 0) goto L_0x002f
            boolean r5 = r3.mOccluded
            if (r5 != 0) goto L_0x002f
            r5 = 1
            goto L_0x0030
        L_0x002f:
            r5 = r2
        L_0x0030:
            if (r5 == 0) goto L_0x003f
        L_0x0032:
            boolean r5 = r3.mShowHomeOverLockscreen
            if (r5 == 0) goto L_0x003a
            boolean r5 = r3.mInGestureNavigationMode
            if (r5 != 0) goto L_0x003c
        L_0x003a:
            r2 = 2097152(0x200000, float:2.938736E-39)
        L_0x003c:
            r5 = 16777216(0x1000000, float:2.3509887E-38)
            r2 = r2 | r5
        L_0x003f:
            boolean r5 = com.android.systemui.keyguard.KeyguardViewMediator.DEBUG
            if (r5 == 0) goto L_0x007f
            java.lang.String r5 = "adjustStatusBarLocked: mShowing="
            java.lang.StringBuilder r5 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r5)
            boolean r0 = r3.mShowing
            r5.append(r0)
            java.lang.String r0 = " mOccluded="
            r5.append(r0)
            boolean r0 = r3.mOccluded
            r5.append(r0)
            java.lang.String r0 = " isSecure="
            r5.append(r0)
            boolean r0 = r3.isSecure()
            r5.append(r0)
            java.lang.String r0 = " force="
            r5.append(r0)
            r5.append(r4)
            java.lang.String r4 = " --> flags=0x"
            r5.append(r4)
            java.lang.String r4 = java.lang.Integer.toHexString(r2)
            r5.append(r4)
            java.lang.String r4 = r5.toString()
            android.util.Log.d(r1, r4)
        L_0x007f:
            android.app.StatusBarManager r3 = r3.mStatusBarManager
            r3.disable(r2)
        L_0x0084:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardViewMediator.adjustStatusBarLocked(boolean, boolean):void");
    }

    public final InteractionJankMonitor.Configuration.Builder createInteractionJankMonitorConf(String str) {
        return InteractionJankMonitor.Configuration.Builder.withView(29, this.mKeyguardViewControllerLazy.get().getViewRootImpl().getView()).setTag(str);
    }

    public final void dismiss(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
        this.mHandler.obtainMessage(11, new DismissMessage(iKeyguardDismissCallback, charSequence)).sendToTarget();
    }

    public final void doKeyguardForChildProfilesLocked() {
        int[] enabledProfileIds;
        for (int i : UserManager.get(this.mContext).getEnabledProfileIds(UserHandle.myUserId())) {
            if (this.mLockPatternUtils.isSeparateProfileChallengeEnabled(i)) {
                this.mTrustManager.setDeviceLockedForUser(i, true);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x0059, code lost:
        if (r0.mDeviceProvisioned == false) goto L_0x005b;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void doKeyguardLocked(android.os.Bundle r8) {
        /*
            Method dump skipped, instructions count: 234
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardViewMediator.doKeyguardLocked(android.os.Bundle):void");
    }

    @Override // com.android.systemui.CoreStartable, com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.print("  mSystemReady: ");
        printWriter.println(this.mSystemReady);
        printWriter.print("  mBootCompleted: ");
        printWriter.println(this.mBootCompleted);
        printWriter.print("  mBootSendUserPresent: ");
        printWriter.println(this.mBootSendUserPresent);
        printWriter.print("  mExternallyEnabled: ");
        printWriter.println(this.mExternallyEnabled);
        printWriter.print("  mShuttingDown: ");
        printWriter.println(this.mShuttingDown);
        printWriter.print("  mNeedToReshowWhenReenabled: ");
        printWriter.println(this.mNeedToReshowWhenReenabled);
        printWriter.print("  mShowing: ");
        printWriter.println(this.mShowing);
        printWriter.print("  mInputRestricted: ");
        printWriter.println(this.mInputRestricted);
        printWriter.print("  mOccluded: ");
        printWriter.println(this.mOccluded);
        printWriter.print("  mDelayedShowingSequence: ");
        printWriter.println(this.mDelayedShowingSequence);
        printWriter.print("  mExitSecureCallback: ");
        printWriter.println((Object) null);
        printWriter.print("  mDeviceInteractive: ");
        printWriter.println(this.mDeviceInteractive);
        printWriter.print("  mGoingToSleep: ");
        printWriter.println(this.mGoingToSleep);
        printWriter.print("  mHiding: ");
        printWriter.println(this.mHiding);
        printWriter.print("  mDozing: ");
        printWriter.println(this.mDozing);
        printWriter.print("  mAodShowing: ");
        printWriter.println(this.mAodShowing);
        printWriter.print("  mWaitingUntilKeyguardVisible: ");
        printWriter.println(this.mWaitingUntilKeyguardVisible);
        printWriter.print("  mKeyguardDonePending: ");
        printWriter.println(this.mKeyguardDonePending);
        printWriter.print("  mHideAnimationRun: ");
        printWriter.println(this.mHideAnimationRun);
        printWriter.print("  mPendingReset: ");
        printWriter.println(this.mPendingReset);
        printWriter.print("  mPendingLock: ");
        printWriter.println(this.mPendingLock);
        printWriter.print("  wakeAndUnlocking: ");
        ScreenOnCoordinator screenOnCoordinator = this.mScreenOnCoordinator;
        Objects.requireNonNull(screenOnCoordinator);
        printWriter.println(screenOnCoordinator.wakeAndUnlocking);
    }

    public final long getLockTimeout(int i) {
        ContentResolver contentResolver;
        long j = Settings.Secure.getInt(this.mContext.getContentResolver(), "lock_screen_lock_after_timeout", 5000);
        long maximumTimeToLock = this.mLockPatternUtils.getDevicePolicyManager().getMaximumTimeToLock(null, i);
        if (maximumTimeToLock <= 0) {
            return j;
        }
        return Math.max(Math.min(maximumTimeToLock - Math.max(Settings.System.getInt(contentResolver, "screen_off_timeout", 30000), 0L), j), 0L);
    }

    public final void handleHide() {
        boolean z;
        Trace.beginSection("KeyguardViewMediator#handleHide");
        if (this.mAodShowing || this.mDreamOverlayShowing) {
            ((PowerManager) this.mContext.getSystemService(PowerManager.class)).wakeUp(SystemClock.uptimeMillis(), 4, "com.android.systemui:BOUNCER_DOZING");
        }
        synchronized (this) {
            try {
                boolean z2 = DEBUG;
                if (z2) {
                    Log.d("KeyguardViewMediator", "handleHide");
                }
                if (!UserManager.isSplitSystemUser() || KeyguardUpdateMonitor.getCurrentUser() != 0) {
                    z = false;
                } else {
                    z = true;
                }
                if (z) {
                    if (z2) {
                        Log.d("KeyguardViewMediator", "Split system user, quit unlocking.");
                    }
                    this.mKeyguardExitAnimationRunner = null;
                    return;
                }
                this.mHiding = true;
                if (!this.mShowing || this.mOccluded) {
                    this.mNotificationShadeWindowControllerLazy.get().batchApplyWindowLayoutParams(new ScreenDecorations$$ExternalSyntheticLambda4(this, 1));
                } else {
                    this.mKeyguardGoingAwayRunnable.run();
                }
                Trace.endSection();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public final void handleKeyguardDone() {
        Trace.beginSection("KeyguardViewMediator#handleKeyguardDone");
        int currentUser = KeyguardUpdateMonitor.getCurrentUser();
        this.mUiBgExecutor.execute(new KeyguardViewMediator$$ExternalSyntheticLambda2(this, currentUser, 0));
        if (DEBUG) {
            Log.d("KeyguardViewMediator", "handleKeyguardDone");
        }
        synchronized (this) {
            resetKeyguardDonePendingLocked();
        }
        if (this.mGoingToSleep) {
            KeyguardUpdateMonitor keyguardUpdateMonitor = this.mUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            keyguardUpdateMonitor.clearBiometricRecognized(currentUser);
            Log.i("KeyguardViewMediator", "Device is going to sleep, aborting keyguardDone");
            return;
        }
        handleHide();
        KeyguardUpdateMonitor keyguardUpdateMonitor2 = this.mUpdateMonitor;
        Objects.requireNonNull(keyguardUpdateMonitor2);
        keyguardUpdateMonitor2.clearBiometricRecognized(currentUser);
        Trace.endSection();
    }

    public final void hideLocked() {
        Trace.beginSection("KeyguardViewMediator#hideLocked");
        if (DEBUG) {
            Log.d("KeyguardViewMediator", "hideLocked");
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(2));
        Trace.endSection();
    }

    public final boolean isAnimatingBetweenKeyguardAndSurfaceBehindOrWillBe() {
        if (this.mSurfaceBehindRemoteAnimationRunning || this.mKeyguardStateController.isFlingingToDismissKeyguard()) {
            return true;
        }
        return false;
    }

    public final void maybeSendUserPresentBroadcast() {
        if (this.mSystemReady && this.mLockPatternUtils.isLockScreenDisabled(KeyguardUpdateMonitor.getCurrentUser())) {
            sendUserPresentBroadcast();
        } else if (this.mSystemReady && shouldWaitForProvisioning()) {
            this.mLockPatternUtils.userPresent(KeyguardUpdateMonitor.getCurrentUser());
        }
    }

    public final void notifyTrustedChangedLocked(boolean z) {
        for (int size = this.mKeyguardStateCallbacks.size() - 1; size >= 0; size--) {
            try {
                this.mKeyguardStateCallbacks.get(size).onTrustedChanged(z);
            } catch (RemoteException e) {
                Slog.w("KeyguardViewMediator", "Failed to call notifyTrustedChangedLocked", e);
                if (e instanceof DeadObjectException) {
                    this.mKeyguardStateCallbacks.remove(size);
                }
            }
        }
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onDozeAmountChanged(float f, float f2) {
        if (this.mAnimatingScreenOff && this.mDozing && f == 1.0f) {
            this.mAnimatingScreenOff = false;
            setShowingLocked(this.mShowing, true);
        }
    }

    public final void onKeyguardExitFinished() {
        if (TelephonyManager.EXTRA_STATE_IDLE.equals(this.mPhoneState)) {
            playSound(this.mUnlockSoundId);
        }
        setShowingLocked(false, false);
        this.mScreenOnCoordinator.setWakeAndUnlocking(false);
        DismissCallbackRegistry dismissCallbackRegistry = this.mDismissCallbackRegistry;
        Objects.requireNonNull(dismissCallbackRegistry);
        int size = dismissCallbackRegistry.mDismissCallbacks.size();
        while (true) {
            size--;
            if (size >= 0) {
                DismissCallbackWrapper dismissCallbackWrapper = dismissCallbackRegistry.mDismissCallbacks.get(size);
                Executor executor = dismissCallbackRegistry.mUiBgExecutor;
                Objects.requireNonNull(dismissCallbackWrapper);
                executor.execute(new StatusBar$$ExternalSyntheticLambda19(dismissCallbackWrapper, 2));
            } else {
                dismissCallbackRegistry.mDismissCallbacks.clear();
                resetKeyguardDonePendingLocked();
                this.mHideAnimationRun = false;
                adjustStatusBarLocked(false, false);
                sendUserPresentBroadcast();
                return;
            }
        }
    }

    public final void onKeyguardExitRemoteAnimationFinished(final boolean z) {
        if (this.mSurfaceBehindRemoteAnimationRunning || this.mSurfaceBehindRemoteAnimationRequested) {
            this.mKeyguardViewControllerLazy.get().blockPanelExpansionFromCurrentTouch();
            final boolean z2 = this.mShowing;
            InteractionJankMonitor.getInstance().end(29);
            DejankUtils.postAfterTraversal(new Runnable() { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                    boolean z3 = z2;
                    boolean z4 = KeyguardViewMediator.DEBUG;
                    Objects.requireNonNull(keyguardViewMediator);
                    keyguardViewMediator.onKeyguardExitFinished();
                    if (keyguardViewMediator.mKeyguardStateController.isDismissingFromSwipe() || z3) {
                        KeyguardUnlockAnimationController keyguardUnlockAnimationController = keyguardViewMediator.mKeyguardUnlockAnimationControllerLazy.get();
                        Objects.requireNonNull(keyguardUnlockAnimationController);
                        keyguardUnlockAnimationController.keyguardViewController.hide(keyguardUnlockAnimationController.surfaceBehindRemoteAnimationStartTime, 0L);
                    }
                    if (keyguardViewMediator.mSurfaceBehindRemoteAnimationRunning) {
                        keyguardViewMediator.mSurfaceBehindRemoteAnimationRunning = false;
                        IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback = keyguardViewMediator.mSurfaceBehindRemoteAnimationFinishedCallback;
                        if (iRemoteAnimationFinishedCallback != null) {
                            try {
                                iRemoteAnimationFinishedCallback.onAnimationFinished();
                                keyguardViewMediator.mSurfaceBehindRemoteAnimationFinishedCallback = null;
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    keyguardViewMediator.mSurfaceBehindRemoteAnimationRequested = false;
                }
            });
            KeyguardUnlockAnimationController keyguardUnlockAnimationController = this.mKeyguardUnlockAnimationControllerLazy.get();
            Objects.requireNonNull(keyguardUnlockAnimationController);
            keyguardUnlockAnimationController.handler.removeCallbacksAndMessages(null);
            keyguardUnlockAnimationController.setSurfaceBehindAppearAmount(1.0f);
            ILauncherUnlockAnimationController iLauncherUnlockAnimationController = keyguardUnlockAnimationController.launcherUnlockController;
            if (iLauncherUnlockAnimationController != null) {
                iLauncherUnlockAnimationController.setUnlockAmount(1.0f);
            }
            keyguardUnlockAnimationController.smartspaceDestBounds.setEmpty();
            keyguardUnlockAnimationController.surfaceBehindRemoteAnimationTarget = null;
            keyguardUnlockAnimationController.surfaceBehindParams = null;
            keyguardUnlockAnimationController.playingCannedUnlockAnimation = false;
            keyguardUnlockAnimationController.unlockingToLauncherWithInWindowAnimations = false;
            keyguardUnlockAnimationController.unlockingWithSmartspaceTransition = false;
            keyguardUnlockAnimationController.smartspaceUnlockProgress = 0.0f;
            View view = keyguardUnlockAnimationController.lockscreenSmartspace;
            if (view != null) {
                view.post(new KeyguardUnlockAnimationController$resetSmartspaceTransition$1(keyguardUnlockAnimationController));
            }
            Iterator<KeyguardUnlockAnimationController.KeyguardUnlockAnimationListener> it = keyguardUnlockAnimationController.listeners.iterator();
            while (it.hasNext()) {
                it.next().onUnlockAnimationFinished();
            }
        }
    }

    public final void onWakeAndUnlocking() {
        Trace.beginSection("KeyguardViewMediator#onWakeAndUnlocking");
        this.mScreenOnCoordinator.setWakeAndUnlocking(true);
        Trace.beginSection("KeyguardViewMediator#keyguardDone");
        if (DEBUG) {
            Log.d("KeyguardViewMediator", "keyguardDone()");
        }
        userActivity();
        EventLog.writeEvent(70000, 2);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(7));
        Trace.endSection();
        Trace.endSection();
    }

    public final void playSound(final int i) {
        if (i != 0 && Settings.System.getInt(this.mContext.getContentResolver(), "lockscreen_sounds_enabled", 1) == 1) {
            this.mLockSounds.stop(this.mLockSoundStreamId);
            if (this.mAudioManager == null) {
                AudioManager audioManager = (AudioManager) this.mContext.getSystemService("audio");
                this.mAudioManager = audioManager;
                if (audioManager != null) {
                    this.mUiSoundsStreamType = audioManager.getUiSoundsStreamType();
                } else {
                    return;
                }
            }
            this.mUiBgExecutor.execute(new Runnable() { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                    int i2 = i;
                    boolean z = KeyguardViewMediator.DEBUG;
                    Objects.requireNonNull(keyguardViewMediator);
                    if (!keyguardViewMediator.mAudioManager.isStreamMute(keyguardViewMediator.mUiSoundsStreamType)) {
                        SoundPool soundPool = keyguardViewMediator.mLockSounds;
                        float f = keyguardViewMediator.mLockSoundVolume;
                        int play = soundPool.play(i2, f, f, 1, 0, 1.0f);
                        synchronized (keyguardViewMediator) {
                            keyguardViewMediator.mLockSoundStreamId = play;
                        }
                    }
                }
            });
        }
    }

    public final void resetStateLocked() {
        if (DEBUG) {
            Log.e("KeyguardViewMediator", "resetStateLocked");
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(3));
    }

    public final void setBlursDisabledForAppLaunch(boolean z) {
        boolean z2;
        NotificationShadeDepthController notificationShadeDepthController = this.mNotificationShadeDepthController.get();
        Objects.requireNonNull(notificationShadeDepthController);
        if (notificationShadeDepthController.blursDisabledForAppLaunch != z) {
            notificationShadeDepthController.blursDisabledForAppLaunch = z;
            notificationShadeDepthController.scheduleUpdate(null);
            boolean z3 = true;
            if (notificationShadeDepthController.shadeExpansion == 0.0f) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                NotificationShadeDepthController.DepthAnimation depthAnimation = notificationShadeDepthController.shadeAnimation;
                Objects.requireNonNull(depthAnimation);
                if (depthAnimation.radius != 0.0f) {
                    z3 = false;
                }
                if (z3) {
                    return;
                }
            }
            if (z) {
                NotificationShadeDepthController.DepthAnimation.animateTo$default(notificationShadeDepthController.shadeAnimation, 0);
                NotificationShadeDepthController.DepthAnimation depthAnimation2 = notificationShadeDepthController.shadeAnimation;
                Objects.requireNonNull(depthAnimation2);
                SpringAnimation springAnimation = depthAnimation2.springAnimation;
                Objects.requireNonNull(springAnimation);
                if (springAnimation.mRunning) {
                    depthAnimation2.springAnimation.skipToEnd();
                }
            }
        }
    }

    public final void setOccluded(boolean z, boolean z2) {
        Trace.beginSection("KeyguardViewMediator#setOccluded");
        if (DEBUG) {
            ViewCompat$$ExternalSyntheticLambda0.m("setOccluded ", z, "KeyguardViewMediator");
        }
        this.mHandler.removeMessages(9);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(9, z ? 1 : 0, z2 ? 1 : 0));
        Trace.endSection();
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0024  */
    /* JADX WARN: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setShowingLocked(final boolean r5, boolean r6) {
        /*
            r4 = this;
            boolean r0 = r4.mDozing
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x0011
            com.android.keyguard.mediator.ScreenOnCoordinator r0 = r4.mScreenOnCoordinator
            java.util.Objects.requireNonNull(r0)
            boolean r0 = r0.wakeAndUnlocking
            if (r0 != 0) goto L_0x0011
            r0 = r1
            goto L_0x0012
        L_0x0011:
            r0 = r2
        L_0x0012:
            boolean r3 = r4.mShowing
            if (r5 != r3) goto L_0x001e
            boolean r3 = r4.mAodShowing
            if (r0 != r3) goto L_0x001e
            if (r6 == 0) goto L_0x001d
            goto L_0x001e
        L_0x001d:
            r1 = r2
        L_0x001e:
            r4.mShowing = r5
            r4.mAodShowing = r0
            if (r1 == 0) goto L_0x0044
            com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda5 r6 = new com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda5
            r6.<init>(r4, r5)
            com.android.systemui.DejankUtils.whitelistIpcs(r6)
            r4.updateInputRestrictedLocked()
            java.util.concurrent.Executor r6 = r4.mUiBgExecutor
            com.android.wm.shell.bubbles.BubbleExpandedView$1$$ExternalSyntheticLambda0 r1 = new com.android.wm.shell.bubbles.BubbleExpandedView$1$$ExternalSyntheticLambda0
            r2 = 3
            r1.<init>(r4, r2)
            r6.execute(r1)
            java.util.concurrent.Executor r4 = r4.mUiBgExecutor
            com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda7 r6 = new com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda7
            r6.<init>()
            r4.execute(r6)
        L_0x0044:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardViewMediator.setShowingLocked(boolean, boolean):void");
    }

    public final void setupLocked() {
        boolean z;
        PowerManager.WakeLock newWakeLock = this.mPM.newWakeLock(1, "show keyguard");
        this.mShowKeyguardWakeLock = newWakeLock;
        boolean z2 = false;
        newWakeLock.setReferenceCounted(false);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_KEYGUARD");
        intentFilter2.addAction("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_LOCK");
        this.mContext.registerReceiver(this.mDelayedLockBroadcastReceiver, intentFilter2, "com.android.systemui.permission.SELF", null, 2);
        this.mAlarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
        int currentUser = ActivityManager.getCurrentUser();
        boolean z3 = KeyguardUpdateMonitor.DEBUG;
        synchronized (KeyguardUpdateMonitor.class) {
            KeyguardUpdateMonitor.sCurrentUser = currentUser;
        }
        try {
            z = this.mContext.getPackageManager().getServiceInfo(new ComponentName(this.mContext, KeyguardService.class), 0).isEnabled();
        } catch (PackageManager.NameNotFoundException unused) {
            z = true;
        }
        if (z) {
            if (!shouldWaitForProvisioning() && !this.mLockPatternUtils.isLockScreenDisabled(KeyguardUpdateMonitor.getCurrentUser())) {
                z2 = true;
            }
            setShowingLocked(z2, true);
        } else {
            setShowingLocked(false, true);
        }
        ContentResolver contentResolver = this.mContext.getContentResolver();
        this.mDeviceInteractive = this.mPM.isInteractive();
        this.mLockSounds = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(new AudioAttributes.Builder().setUsage(13).setContentType(4).build()).build();
        String string = Settings.Global.getString(contentResolver, "lock_sound");
        if (string != null) {
            this.mLockSoundId = this.mLockSounds.load(string, 1);
        }
        if (string == null || this.mLockSoundId == 0) {
            MotionLayout$$ExternalSyntheticOutline0.m("failed to load lock sound from ", string, "KeyguardViewMediator");
        }
        String string2 = Settings.Global.getString(contentResolver, "unlock_sound");
        if (string2 != null) {
            this.mUnlockSoundId = this.mLockSounds.load(string2, 1);
        }
        if (string2 == null || this.mUnlockSoundId == 0) {
            MotionLayout$$ExternalSyntheticOutline0.m("failed to load unlock sound from ", string2, "KeyguardViewMediator");
        }
        String string3 = Settings.Global.getString(contentResolver, "trusted_sound");
        if (string3 != null) {
            this.mTrustedSoundId = this.mLockSounds.load(string3, 1);
        }
        if (string3 == null || this.mTrustedSoundId == 0) {
            MotionLayout$$ExternalSyntheticOutline0.m("failed to load trusted sound from ", string3, "KeyguardViewMediator");
        }
        this.mLockSoundVolume = (float) Math.pow(10.0d, this.mContext.getResources().getInteger(17694849) / 20.0f);
        this.mHideAnimation = AnimationUtils.loadAnimation(this.mContext, 17432687);
        new WorkLockActivityController(this.mContext, TaskStackChangeListeners.INSTANCE, ActivityTaskManager.getService());
    }

    public final boolean shouldWaitForProvisioning() {
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.mUpdateMonitor;
        Objects.requireNonNull(keyguardUpdateMonitor);
        if (keyguardUpdateMonitor.mDeviceProvisioned || isSecure()) {
            return false;
        }
        return true;
    }

    public final void showLocked(Bundle bundle) {
        Trace.beginSection("KeyguardViewMediator#showLocked acquiring mShowKeyguardWakeLock");
        if (DEBUG) {
            Log.d("KeyguardViewMediator", "showLocked");
        }
        this.mShowKeyguardWakeLock.acquire();
        this.mHandler.sendMessageAtFrontOfQueue(this.mHandler.obtainMessage(1, bundle));
        Trace.endSection();
    }

    public final void tryKeyguardDone() {
        boolean z = DEBUG;
        if (z) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("tryKeyguardDone: pending - ");
            m.append(this.mKeyguardDonePending);
            m.append(", animRan - ");
            m.append(this.mHideAnimationRun);
            m.append(" animRunning - ");
            KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0.m(m, this.mHideAnimationRunning, "KeyguardViewMediator");
        }
        if (!this.mKeyguardDonePending && this.mHideAnimationRun && !this.mHideAnimationRunning) {
            handleKeyguardDone();
        } else if (!this.mHideAnimationRun) {
            if (z) {
                Log.d("KeyguardViewMediator", "tryKeyguardDone: starting pre-hide animation");
            }
            this.mHideAnimationRun = true;
            this.mHideAnimationRunning = true;
            this.mKeyguardViewControllerLazy.get().startPreHideAnimation(this.mHideAnimationFinishedRunnable);
        }
    }

    public final void updateInputRestrictedLocked() {
        boolean z;
        if (this.mShowing || this.mNeedToReshowWhenReenabled) {
            z = true;
        } else {
            z = false;
        }
        if (this.mInputRestricted != z) {
            this.mInputRestricted = z;
            for (int size = this.mKeyguardStateCallbacks.size() - 1; size >= 0; size--) {
                IKeyguardStateCallback iKeyguardStateCallback = this.mKeyguardStateCallbacks.get(size);
                try {
                    iKeyguardStateCallback.onInputRestrictedStateChanged(z);
                } catch (RemoteException e) {
                    Slog.w("KeyguardViewMediator", "Failed to call onDeviceProvisioned", e);
                    if (e instanceof DeadObjectException) {
                        this.mKeyguardStateCallbacks.remove(iKeyguardStateCallback);
                    }
                }
            }
        }
    }

    public final void userActivity() {
        this.mPM.userActivity(SystemClock.uptimeMillis(), false);
    }

    /* renamed from: -$$Nest$mhandleKeyguardDoneDrawing  reason: not valid java name */
    public static void m63$$Nest$mhandleKeyguardDoneDrawing(KeyguardViewMediator keyguardViewMediator) {
        Objects.requireNonNull(keyguardViewMediator);
        Trace.beginSection("KeyguardViewMediator#handleKeyguardDoneDrawing");
        synchronized (keyguardViewMediator) {
            boolean z = DEBUG;
            if (z) {
                Log.d("KeyguardViewMediator", "handleKeyguardDoneDrawing");
            }
            if (keyguardViewMediator.mWaitingUntilKeyguardVisible) {
                if (z) {
                    Log.d("KeyguardViewMediator", "handleKeyguardDoneDrawing: notifying mWaitingUntilKeyguardVisible");
                }
                keyguardViewMediator.mWaitingUntilKeyguardVisible = false;
                keyguardViewMediator.notifyAll();
                keyguardViewMediator.mHandler.removeMessages(8);
            }
        }
        Trace.endSection();
    }

    /* renamed from: -$$Nest$mhandleNotifyFinishedGoingToSleep  reason: not valid java name */
    public static void m64$$Nest$mhandleNotifyFinishedGoingToSleep(KeyguardViewMediator keyguardViewMediator) {
        Objects.requireNonNull(keyguardViewMediator);
        synchronized (keyguardViewMediator) {
            if (DEBUG) {
                Log.d("KeyguardViewMediator", "handleNotifyFinishedGoingToSleep");
            }
            keyguardViewMediator.mKeyguardViewControllerLazy.get().onFinishedGoingToSleep();
        }
    }

    /* renamed from: -$$Nest$mhandleNotifyStartedGoingToSleep  reason: not valid java name */
    public static void m65$$Nest$mhandleNotifyStartedGoingToSleep(KeyguardViewMediator keyguardViewMediator) {
        Objects.requireNonNull(keyguardViewMediator);
        synchronized (keyguardViewMediator) {
            if (DEBUG) {
                Log.d("KeyguardViewMediator", "handleNotifyStartedGoingToSleep");
            }
            keyguardViewMediator.mKeyguardViewControllerLazy.get().onStartedGoingToSleep();
        }
    }

    /* renamed from: -$$Nest$mhandleNotifyStartedWakingUp  reason: not valid java name */
    public static void m66$$Nest$mhandleNotifyStartedWakingUp(KeyguardViewMediator keyguardViewMediator) {
        Objects.requireNonNull(keyguardViewMediator);
        Trace.beginSection("KeyguardViewMediator#handleMotifyStartedWakingUp");
        synchronized (keyguardViewMediator) {
            if (DEBUG) {
                Log.d("KeyguardViewMediator", "handleNotifyWakingUp");
            }
            keyguardViewMediator.mKeyguardViewControllerLazy.get().onStartedWakingUp();
        }
        Trace.endSection();
    }

    /* renamed from: -$$Nest$mhandleSetOccluded  reason: not valid java name */
    public static void m67$$Nest$mhandleSetOccluded(KeyguardViewMediator keyguardViewMediator, boolean z, boolean z2) {
        boolean z3;
        Objects.requireNonNull(keyguardViewMediator);
        Trace.beginSection("KeyguardViewMediator#handleSetOccluded");
        synchronized (keyguardViewMediator) {
            try {
                if (keyguardViewMediator.mHiding && z) {
                    keyguardViewMediator.startKeyguardExitAnimation(0, 0L, 0L, null, null, null, null);
                }
                if (keyguardViewMediator.mOccluded != z) {
                    keyguardViewMediator.mOccluded = z;
                    KeyguardUpdateMonitor keyguardUpdateMonitor = keyguardViewMediator.mUpdateMonitor;
                    Objects.requireNonNull(keyguardUpdateMonitor);
                    keyguardUpdateMonitor.mKeyguardOccluded = z;
                    keyguardUpdateMonitor.updateBiometricListeningState(2);
                    KeyguardViewController keyguardViewController = keyguardViewMediator.mKeyguardViewControllerLazy.get();
                    if (!z2 || !keyguardViewMediator.mDeviceInteractive) {
                        z3 = false;
                    } else {
                        z3 = true;
                    }
                    keyguardViewController.setOccluded(z, z3);
                    keyguardViewMediator.adjustStatusBarLocked(false, false);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        Trace.endSection();
    }

    /* renamed from: -$$Nest$mhandleSystemReady  reason: not valid java name */
    public static void m68$$Nest$mhandleSystemReady(KeyguardViewMediator keyguardViewMediator) {
        Objects.requireNonNull(keyguardViewMediator);
        synchronized (keyguardViewMediator) {
            if (DEBUG) {
                Log.d("KeyguardViewMediator", "onSystemReady");
            }
            keyguardViewMediator.mSystemReady = true;
            keyguardViewMediator.doKeyguardLocked(null);
            keyguardViewMediator.mUpdateMonitor.registerCallback(keyguardViewMediator.mUpdateCallback);
            DreamOverlayStateController dreamOverlayStateController = keyguardViewMediator.mDreamOverlayStateController;
            AnonymousClass2 r1 = keyguardViewMediator.mDreamOverlayStateCallback;
            Objects.requireNonNull(dreamOverlayStateController);
            dreamOverlayStateController.mExecutor.execute(new StatusBar$$ExternalSyntheticLambda20(dreamOverlayStateController, r1, 2));
        }
        keyguardViewMediator.maybeSendUserPresentBroadcast();
    }

    public final void doKeyguardLaterLocked(long j) {
        int[] enabledProfileIds;
        long elapsedRealtime = SystemClock.elapsedRealtime() + j;
        Intent intent = new Intent("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_KEYGUARD");
        intent.putExtra("seq", this.mDelayedShowingSequence);
        intent.addFlags(268435456);
        this.mAlarmManager.setExactAndAllowWhileIdle(2, elapsedRealtime, PendingIntent.getBroadcast(this.mContext, 0, intent, 335544320));
        if (DEBUG) {
            KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(VendorAtomValue$$ExternalSyntheticOutline1.m("setting alarm to turn off keyguard, seq = "), this.mDelayedShowingSequence, "KeyguardViewMediator");
        }
        for (int i : UserManager.get(this.mContext).getEnabledProfileIds(UserHandle.myUserId())) {
            if (this.mLockPatternUtils.isSeparateProfileChallengeEnabled(i)) {
                long lockTimeout = getLockTimeout(i);
                if (lockTimeout == 0) {
                    doKeyguardForChildProfilesLocked();
                } else {
                    long elapsedRealtime2 = SystemClock.elapsedRealtime() + lockTimeout;
                    Intent intent2 = new Intent("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_LOCK");
                    intent2.putExtra("seq", this.mDelayedProfileShowingSequence);
                    intent2.putExtra("android.intent.extra.USER_ID", i);
                    intent2.addFlags(268435456);
                    this.mAlarmManager.setExactAndAllowWhileIdle(2, elapsedRealtime2, PendingIntent.getBroadcast(this.mContext, 0, intent2, 301989888));
                }
            }
        }
    }

    public final boolean isSecure() {
        if (this.mLockPatternUtils.isSecure(KeyguardUpdateMonitor.getCurrentUser()) || this.mUpdateMonitor.isSimPinSecure()) {
            return true;
        }
        return false;
    }
}
