package com.android.keyguard;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.admin.DevicePolicyManager;
import android.app.trust.TrustManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageManager;
import android.content.pm.ResolveInfo;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hardware.SensorPrivacyManager;
import android.hardware.biometrics.BiometricSourceType;
import android.hardware.biometrics.CryptoObject;
import android.hardware.biometrics.IBiometricEnabledOnKeyguardCallback;
import android.hardware.face.FaceManager;
import android.hardware.face.FaceSensorPropertiesInternal;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.IRemoteCallback;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.service.dreams.IDreamManager;
import android.telephony.CarrierConfigManager;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import androidx.core.view.ViewCompat$$ExternalSyntheticLambda0;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import androidx.fragment.app.DialogFragment$$ExternalSyntheticOutline0;
import androidx.preference.R$id;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.settingslib.fuelgauge.BatteryStatus;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda2;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda0;
import com.android.systemui.shared.system.TaskStackChangeListener;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.util.Assert;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda0;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda5;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda15;
import com.google.android.collect.Lists;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import kotlin.collections.ArrayDeque;
/* loaded from: classes.dex */
public final class KeyguardUpdateMonitor implements TrustManager.TrustListener, Dumpable {
    public static final boolean CORE_APPS_ONLY;
    public static final boolean DEBUG_ACTIVE_UNLOCK;
    public static final boolean DEBUG_FACE;
    public static final boolean DEBUG_FINGERPRINT;
    public static int sCurrentUser;
    public boolean mAssistantVisible;
    public final AuthController mAuthController;
    public boolean mAuthInterruptActive;
    public final Executor mBackgroundExecutor;
    @VisibleForTesting
    public BatteryStatus mBatteryStatus;
    public boolean mBouncer;
    @VisibleForTesting
    public final BroadcastReceiver mBroadcastAllReceiver;
    @VisibleForTesting
    public final BroadcastReceiver mBroadcastReceiver;
    public final Context mContext;
    public boolean mCredentialAttempted;
    public boolean mDeviceInteractive;
    public final DevicePolicyManager mDevicePolicyManager;
    public boolean mDeviceProvisioned;
    public AnonymousClass18 mDeviceProvisionedObserver;
    public final IDreamManager mDreamManager;
    public int mFaceAuthUserId;
    public CancellationSignal mFaceCancelSignal;
    public boolean mFaceLockedOutPermanent;
    public FaceManager mFaceManager;
    public List<FaceSensorPropertiesInternal> mFaceSensorProperties;
    public CancellationSignal mFingerprintCancelSignal;
    public boolean mFingerprintLockedOut;
    public boolean mFingerprintLockedOutPermanent;
    public FingerprintManager mFpm;
    public boolean mGoingToSleep;
    public final AnonymousClass14 mHandler;
    public final InteractionJankMonitor mInteractionJankMonitor;
    public final boolean mIsAutomotive;
    public boolean mIsDreaming;
    public boolean mIsFaceEnrolled;
    public final boolean mIsPrimaryUser;
    public KeyguardBypassController mKeyguardBypassController;
    public boolean mKeyguardGoingAway;
    public boolean mKeyguardIsVisible;
    public boolean mKeyguardOccluded;
    public final LatencyTracker mLatencyTracker;
    public LockPatternUtils mLockPatternUtils;
    public boolean mLogoutEnabled;
    public boolean mNeedsSlowUnlockTransition;
    public boolean mOccludingAppRequestingFace;
    public boolean mOccludingAppRequestingFp;
    public int mPhoneState;
    public boolean mSecureCameraLaunched;
    public SensorPrivacyManager mSensorPrivacyManager;
    public int mStatusBarState;
    public final StatusBarStateController mStatusBarStateController;
    public final AnonymousClass1 mStatusBarStateControllerListener;
    public StrongAuthTracker mStrongAuthTracker;
    public List<SubscriptionInfo> mSubscriptionInfo;
    public SubscriptionManager mSubscriptionManager;
    public boolean mSwitchingUser;
    @VisibleForTesting
    public boolean mTelephonyCapable;
    public final TelephonyListenerManager mTelephonyListenerManager;
    public TelephonyManager mTelephonyManager;
    public AnonymousClass16 mTimeFormatChangeObserver;
    public TrustManager mTrustManager;
    public UserManager mUserManager;
    public final AnonymousClass17 mUserSwitchObserver;
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public static final ComponentName FALLBACK_HOME_COMPONENT = new ComponentName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.FallbackHome");
    public HashMap<Integer, SimData> mSimDatas = new HashMap<>();
    public HashMap<Integer, ServiceState> mServiceStates = new HashMap<>();
    public final ArrayList<WeakReference<KeyguardUpdateMonitorCallback>> mCallbacks = Lists.newArrayList();
    public int mFingerprintRunningState = 0;
    public int mFaceRunningState = 0;
    public int mActiveMobileDataSubscription = -1;
    public int mHardwareFingerprintUnavailableRetryCount = 0;
    public int mHardwareFaceUnavailableRetryCount = 0;
    public final TaskView$$ExternalSyntheticLambda5 mFpCancelNotReceived = new TaskView$$ExternalSyntheticLambda5(this, 1);
    public final KeyguardUpdateMonitor$$ExternalSyntheticLambda8 mFaceCancelNotReceived = new KeyguardUpdateMonitor$$ExternalSyntheticLambda8(this, 0);
    public SparseBooleanArray mBiometricEnabledForUser = new SparseBooleanArray();
    public AnonymousClass2 mBiometricEnabledCallback = new AnonymousClass2();
    @VisibleForTesting
    public TelephonyCallback.ActiveDataSubscriptionIdListener mPhoneStateListener = new TelephonyCallback.ActiveDataSubscriptionIdListener() { // from class: com.android.keyguard.KeyguardUpdateMonitor.3
        public final void onActiveDataSubscriptionIdChanged(int i) {
            KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardUpdateMonitor.this;
            keyguardUpdateMonitor.mActiveMobileDataSubscription = i;
            keyguardUpdateMonitor.mHandler.sendEmptyMessage(328);
        }
    };
    public AnonymousClass4 mSubscriptionListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.keyguard.KeyguardUpdateMonitor.4
        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public final void onSubscriptionsChanged() {
            sendEmptyMessage(328);
        }
    };
    public SparseBooleanArray mUserIsUnlocked = new SparseBooleanArray();
    public SparseBooleanArray mUserHasTrust = new SparseBooleanArray();
    public SparseBooleanArray mUserTrustIsManaged = new SparseBooleanArray();
    public SparseBooleanArray mUserTrustIsUsuallyManaged = new SparseBooleanArray();
    public SparseBooleanArray mUserFaceUnlockRunning = new SparseBooleanArray();
    public HashMap mSecondaryLockscreenRequirement = new HashMap();
    @VisibleForTesting
    public SparseArray<BiometricAuthenticated> mUserFingerprintAuthenticated = new SparseArray<>();
    @VisibleForTesting
    public SparseArray<BiometricAuthenticated> mUserFaceAuthenticated = new SparseArray<>();
    public final KeyguardListenQueue mListenModels = new KeyguardListenQueue();
    public AnonymousClass6 mRetryFingerprintAuthentication = new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor.6
        @Override // java.lang.Runnable
        public final void run() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Retrying fingerprint after HW unavailable, attempt ");
            m.append(KeyguardUpdateMonitor.this.mHardwareFingerprintUnavailableRetryCount);
            Log.w("KeyguardUpdateMonitor", m.toString());
            if (KeyguardUpdateMonitor.this.mFpm.isHardwareDetected()) {
                KeyguardUpdateMonitor.this.updateFingerprintListeningState(2);
                return;
            }
            KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardUpdateMonitor.this;
            int i = keyguardUpdateMonitor.mHardwareFingerprintUnavailableRetryCount;
            if (i < 20) {
                keyguardUpdateMonitor.mHardwareFingerprintUnavailableRetryCount = i + 1;
                keyguardUpdateMonitor.mHandler.postDelayed(keyguardUpdateMonitor.mRetryFingerprintAuthentication, 500L);
            }
        }
    };
    public AnonymousClass7 mRetryFaceAuthentication = new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor.7
        @Override // java.lang.Runnable
        public final void run() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Retrying face after HW unavailable, attempt ");
            m.append(KeyguardUpdateMonitor.this.mHardwareFaceUnavailableRetryCount);
            Log.w("KeyguardUpdateMonitor", m.toString());
            KeyguardUpdateMonitor.this.updateFaceListeningState(2);
        }
    };
    public final AnonymousClass10 mFingerprintLockoutResetCallback = new FingerprintManager.LockoutResetCallback() { // from class: com.android.keyguard.KeyguardUpdateMonitor.10
        public final void onLockoutReset(int i) {
            boolean z;
            KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardUpdateMonitor.this;
            Objects.requireNonNull(keyguardUpdateMonitor);
            if (keyguardUpdateMonitor.mFingerprintLockedOut || keyguardUpdateMonitor.mFingerprintLockedOutPermanent) {
                z = true;
            } else {
                z = false;
            }
            keyguardUpdateMonitor.mFingerprintLockedOut = false;
            keyguardUpdateMonitor.mFingerprintLockedOutPermanent = false;
            if (keyguardUpdateMonitor.isUdfpsEnrolled()) {
                keyguardUpdateMonitor.mHandler.postDelayed(new ScrimView$$ExternalSyntheticLambda0(keyguardUpdateMonitor, 2), 600L);
            } else {
                keyguardUpdateMonitor.updateFingerprintListeningState(2);
            }
            if (z) {
                keyguardUpdateMonitor.notifyLockedOutStateChanged(BiometricSourceType.FINGERPRINT);
            }
        }
    };
    public final AnonymousClass11 mFaceLockoutResetCallback = new FaceManager.LockoutResetCallback() { // from class: com.android.keyguard.KeyguardUpdateMonitor.11
        public final void onLockoutReset(int i) {
            KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardUpdateMonitor.this;
            Objects.requireNonNull(keyguardUpdateMonitor);
            boolean z = keyguardUpdateMonitor.mFaceLockedOutPermanent;
            keyguardUpdateMonitor.mFaceLockedOutPermanent = false;
            keyguardUpdateMonitor.mHandler.postDelayed(new KeyguardUpdateMonitor$$ExternalSyntheticLambda7(keyguardUpdateMonitor, 0), 600L);
            if (z) {
                keyguardUpdateMonitor.notifyLockedOutStateChanged(BiometricSourceType.FACE);
            }
        }
    };
    public final KeyguardUpdateMonitor$$ExternalSyntheticLambda5 mFingerprintDetectionCallback = new FingerprintManager.FingerprintDetectionCallback() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda5
        public final void onFingerprintDetected(int i, int i2, boolean z) {
            KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardUpdateMonitor.this;
            Objects.requireNonNull(keyguardUpdateMonitor);
            keyguardUpdateMonitor.handleFingerprintAuthenticated(i2, z);
        }
    };
    @VisibleForTesting
    public final FingerprintManager.AuthenticationCallback mFingerprintAuthenticationCallback = new FingerprintManager.AuthenticationCallback() { // from class: com.android.keyguard.KeyguardUpdateMonitor.12
        public final void onAuthenticationAcquired(int i) {
            KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardUpdateMonitor.this;
            Objects.requireNonNull(keyguardUpdateMonitor);
            Assert.isMainThread();
            if (i == 0) {
                for (int i2 = 0; i2 < keyguardUpdateMonitor.mCallbacks.size(); i2++) {
                    KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = keyguardUpdateMonitor.mCallbacks.get(i2).get();
                    if (keyguardUpdateMonitorCallback != null) {
                        keyguardUpdateMonitorCallback.onBiometricAcquired(BiometricSourceType.FINGERPRINT);
                    }
                }
            }
        }

        @Override // android.hardware.fingerprint.FingerprintManager.AuthenticationCallback
        public final void onAuthenticationError(int i, CharSequence charSequence) {
            boolean z;
            KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardUpdateMonitor.this;
            String charSequence2 = charSequence.toString();
            Objects.requireNonNull(keyguardUpdateMonitor);
            Assert.isMainThread();
            if (keyguardUpdateMonitor.mHandler.hasCallbacks(keyguardUpdateMonitor.mFpCancelNotReceived)) {
                keyguardUpdateMonitor.mHandler.removeCallbacks(keyguardUpdateMonitor.mFpCancelNotReceived);
            }
            keyguardUpdateMonitor.mFingerprintCancelSignal = null;
            if (i == 5 && keyguardUpdateMonitor.mFingerprintRunningState == 3) {
                keyguardUpdateMonitor.setFingerprintRunningState(0);
                keyguardUpdateMonitor.updateFingerprintListeningState(2);
            } else {
                keyguardUpdateMonitor.setFingerprintRunningState(0);
            }
            if (i == 1) {
                keyguardUpdateMonitor.mHandler.postDelayed(keyguardUpdateMonitor.mRetryFingerprintAuthentication, 500L);
            }
            if (i == 9) {
                z = (!keyguardUpdateMonitor.mFingerprintLockedOutPermanent) | false;
                keyguardUpdateMonitor.mFingerprintLockedOutPermanent = true;
                Log.d("KeyguardUpdateMonitor", "Fingerprint locked out - requiring strong auth");
                keyguardUpdateMonitor.mLockPatternUtils.requireStrongAuth(8, KeyguardUpdateMonitor.getCurrentUser());
            } else {
                z = false;
            }
            if (i == 7 || i == 9) {
                z |= !keyguardUpdateMonitor.mFingerprintLockedOut;
                keyguardUpdateMonitor.mFingerprintLockedOut = true;
                if (keyguardUpdateMonitor.isUdfpsEnrolled()) {
                    keyguardUpdateMonitor.updateFingerprintListeningState(2);
                }
                keyguardUpdateMonitor.stopListeningForFace();
            }
            for (int i2 = 0; i2 < keyguardUpdateMonitor.mCallbacks.size(); i2++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = keyguardUpdateMonitor.mCallbacks.get(i2).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onBiometricError(i, charSequence2, BiometricSourceType.FINGERPRINT);
                }
            }
            if (z) {
                keyguardUpdateMonitor.notifyLockedOutStateChanged(BiometricSourceType.FINGERPRINT);
            }
        }

        @Override // android.hardware.fingerprint.FingerprintManager.AuthenticationCallback
        public final void onAuthenticationFailed() {
            KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardUpdateMonitor.this;
            Objects.requireNonNull(keyguardUpdateMonitor);
            Assert.isMainThread();
            for (int i = 0; i < keyguardUpdateMonitor.mCallbacks.size(); i++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = keyguardUpdateMonitor.mCallbacks.get(i).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onBiometricAuthFailed(BiometricSourceType.FINGERPRINT);
                }
            }
            if (keyguardUpdateMonitor.isUdfpsSupported()) {
                keyguardUpdateMonitor.handleFingerprintHelp(-1, keyguardUpdateMonitor.mContext.getString(17040347));
            } else {
                keyguardUpdateMonitor.handleFingerprintHelp(-1, keyguardUpdateMonitor.mContext.getString(17040333));
            }
        }

        @Override // android.hardware.fingerprint.FingerprintManager.AuthenticationCallback
        public final void onAuthenticationHelp(int i, CharSequence charSequence) {
            KeyguardUpdateMonitor.this.handleFingerprintHelp(i, charSequence.toString());
        }

        @Override // android.hardware.fingerprint.FingerprintManager.AuthenticationCallback
        public final void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult authenticationResult) {
            Trace.beginSection("KeyguardUpdateMonitor#onAuthenticationSucceeded");
            KeyguardUpdateMonitor.this.handleFingerprintAuthenticated(authenticationResult.getUserId(), authenticationResult.isStrongBiometric());
            Trace.endSection();
        }

        public final void onUdfpsPointerDown(int i) {
            ExifInterface$$ExternalSyntheticOutline1.m("onUdfpsPointerDown, sensorId: ", i, "KeyguardUpdateMonitor");
        }

        public final void onUdfpsPointerUp(int i) {
            ExifInterface$$ExternalSyntheticOutline1.m("onUdfpsPointerUp, sensorId: ", i, "KeyguardUpdateMonitor");
        }
    };
    public final KeyguardUpdateMonitor$$ExternalSyntheticLambda4 mFaceDetectionCallback = new FaceManager.FaceDetectionCallback() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda4
        public final void onFaceDetected(int i, int i2, boolean z) {
            KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardUpdateMonitor.this;
            Objects.requireNonNull(keyguardUpdateMonitor);
            keyguardUpdateMonitor.handleFaceAuthenticated(i2, z);
        }
    };
    @VisibleForTesting
    public final FaceManager.AuthenticationCallback mFaceAuthenticationCallback = new FaceManager.AuthenticationCallback() { // from class: com.android.keyguard.KeyguardUpdateMonitor.13
        public final void onAuthenticationAcquired(int i) {
            KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardUpdateMonitor.this;
            Objects.requireNonNull(keyguardUpdateMonitor);
            Assert.isMainThread();
            if (i == 0) {
                if (KeyguardUpdateMonitor.DEBUG_FACE) {
                    Log.d("KeyguardUpdateMonitor", "Face acquired");
                }
                for (int i2 = 0; i2 < keyguardUpdateMonitor.mCallbacks.size(); i2++) {
                    KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = keyguardUpdateMonitor.mCallbacks.get(i2).get();
                    if (keyguardUpdateMonitorCallback != null) {
                        keyguardUpdateMonitorCallback.onBiometricAcquired(BiometricSourceType.FACE);
                    }
                }
            }
        }

        public final void onAuthenticationError(int i, CharSequence charSequence) {
            boolean z;
            boolean z2;
            boolean z3;
            int i2;
            KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardUpdateMonitor.this;
            String charSequence2 = charSequence.toString();
            Objects.requireNonNull(keyguardUpdateMonitor);
            Assert.isMainThread();
            if (KeyguardUpdateMonitor.DEBUG_FACE) {
                DialogFragment$$ExternalSyntheticOutline0.m("Face error received: ", charSequence2, "KeyguardUpdateMonitor");
            }
            if (keyguardUpdateMonitor.mHandler.hasCallbacks(keyguardUpdateMonitor.mFaceCancelNotReceived)) {
                keyguardUpdateMonitor.mHandler.removeCallbacks(keyguardUpdateMonitor.mFaceCancelNotReceived);
            }
            keyguardUpdateMonitor.mFaceCancelSignal = null;
            SensorPrivacyManager sensorPrivacyManager = keyguardUpdateMonitor.mSensorPrivacyManager;
            if (sensorPrivacyManager != null) {
                z = sensorPrivacyManager.isSensorPrivacyEnabled(2, keyguardUpdateMonitor.mFaceAuthUserId);
            } else {
                z = false;
            }
            if (i == 5 && keyguardUpdateMonitor.mFaceRunningState == 3) {
                keyguardUpdateMonitor.setFaceRunningState(0);
                keyguardUpdateMonitor.updateFaceListeningState(2);
            } else {
                keyguardUpdateMonitor.setFaceRunningState(0);
            }
            if (i == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            if ((z2 || i == 2) && (i2 = keyguardUpdateMonitor.mHardwareFaceUnavailableRetryCount) < 20) {
                keyguardUpdateMonitor.mHardwareFaceUnavailableRetryCount = i2 + 1;
                keyguardUpdateMonitor.mHandler.removeCallbacks(keyguardUpdateMonitor.mRetryFaceAuthentication);
                keyguardUpdateMonitor.mHandler.postDelayed(keyguardUpdateMonitor.mRetryFaceAuthentication, 500L);
            }
            if (i == 9) {
                z3 = !keyguardUpdateMonitor.mFaceLockedOutPermanent;
                keyguardUpdateMonitor.mFaceLockedOutPermanent = true;
            } else {
                z3 = false;
            }
            if (z2 && z) {
                charSequence2 = keyguardUpdateMonitor.mContext.getString(2131952571);
            }
            for (int i3 = 0; i3 < keyguardUpdateMonitor.mCallbacks.size(); i3++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = keyguardUpdateMonitor.mCallbacks.get(i3).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onBiometricError(i, charSequence2, BiometricSourceType.FACE);
                }
            }
            if (z3) {
                keyguardUpdateMonitor.notifyLockedOutStateChanged(BiometricSourceType.FACE);
            }
            KeyguardBypassController keyguardBypassController = KeyguardUpdateMonitor.this.mKeyguardBypassController;
            if (keyguardBypassController != null) {
                keyguardBypassController.userHasDeviceEntryIntent = false;
            }
        }

        public final void onAuthenticationFailed() {
            KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardUpdateMonitor.this;
            Objects.requireNonNull(keyguardUpdateMonitor);
            Assert.isMainThread();
            keyguardUpdateMonitor.mFaceCancelSignal = null;
            keyguardUpdateMonitor.setFaceRunningState(0);
            for (int i = 0; i < keyguardUpdateMonitor.mCallbacks.size(); i++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = keyguardUpdateMonitor.mCallbacks.get(i).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onBiometricAuthFailed(BiometricSourceType.FACE);
                }
            }
            keyguardUpdateMonitor.handleFaceHelp(-2, keyguardUpdateMonitor.mContext.getString(2131952570));
            KeyguardBypassController keyguardBypassController = KeyguardUpdateMonitor.this.mKeyguardBypassController;
            if (keyguardBypassController != null) {
                Objects.requireNonNull(keyguardBypassController);
                keyguardBypassController.userHasDeviceEntryIntent = false;
            }
        }

        public final void onAuthenticationHelp(int i, CharSequence charSequence) {
            KeyguardUpdateMonitor.this.handleFaceHelp(i, charSequence.toString());
        }

        public final void onAuthenticationSucceeded(FaceManager.AuthenticationResult authenticationResult) {
            Trace.beginSection("KeyguardUpdateMonitor#onAuthenticationSucceeded");
            KeyguardUpdateMonitor.this.handleFaceAuthenticated(authenticationResult.getUserId(), authenticationResult.isStrongBiometric());
            Trace.endSection();
            KeyguardBypassController keyguardBypassController = KeyguardUpdateMonitor.this.mKeyguardBypassController;
            if (keyguardBypassController != null) {
                Objects.requireNonNull(keyguardBypassController);
                keyguardBypassController.userHasDeviceEntryIntent = false;
            }
        }
    };
    public final AnonymousClass19 mTaskStackListener = new TaskStackChangeListener() { // from class: com.android.keyguard.KeyguardUpdateMonitor.19
        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public final void onTaskStackChangedBackground() {
            try {
                ActivityTaskManager.RootTaskInfo rootTaskInfo = ActivityTaskManager.getService().getRootTaskInfo(0, 4);
                if (rootTaskInfo != null) {
                    AnonymousClass14 r3 = KeyguardUpdateMonitor.this.mHandler;
                    r3.sendMessage(r3.obtainMessage(335, Boolean.valueOf(rootTaskInfo.visible)));
                }
            } catch (RemoteException e) {
                Log.e("KeyguardUpdateMonitor", "unable to check task stack", e);
            }
        }
    };

    /* renamed from: com.android.keyguard.KeyguardUpdateMonitor$15  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass15 implements AuthController.Callback {
        public final /* synthetic */ Executor val$mainExecutor;

        public AnonymousClass15(Executor executor) {
            this.val$mainExecutor = executor;
        }

        @Override // com.android.systemui.biometrics.AuthController.Callback
        public final void onAllAuthenticatorsRegistered() {
            this.val$mainExecutor.execute(new WifiEntry$$ExternalSyntheticLambda0(this, 1));
        }

        @Override // com.android.systemui.biometrics.AuthController.Callback
        public final void onEnrollmentsChanged() {
            this.val$mainExecutor.execute(new BubbleStackView$$ExternalSyntheticLambda15(this, 2));
        }
    }

    /* renamed from: com.android.keyguard.KeyguardUpdateMonitor$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 extends IBiometricEnabledOnKeyguardCallback.Stub {
        public AnonymousClass2() {
        }

        public final void onChanged(final boolean z, final int i) throws RemoteException {
            post(new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    KeyguardUpdateMonitor.AnonymousClass2 r0 = KeyguardUpdateMonitor.AnonymousClass2.this;
                    int i2 = i;
                    boolean z2 = z;
                    Objects.requireNonNull(r0);
                    KeyguardUpdateMonitor.this.mBiometricEnabledForUser.put(i2, z2);
                    KeyguardUpdateMonitor.this.updateBiometricListeningState(2);
                }
            });
        }
    }

    /* renamed from: com.android.keyguard.KeyguardUpdateMonitor$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass5 implements Runnable {
        public final /* synthetic */ boolean val$isStrongBiometric;
        public final /* synthetic */ int val$userId;

        public AnonymousClass5(boolean z, int i) {
            this.val$isStrongBiometric = z;
            this.val$userId = i;
        }

        @Override // java.lang.Runnable
        public final void run() {
            KeyguardUpdateMonitor.this.mLockPatternUtils.reportSuccessfulBiometricUnlock(this.val$isStrongBiometric, this.val$userId);
        }
    }

    /* loaded from: classes.dex */
    public static class SimData {
        public int simState;
        public int slotId;
        public int subId;

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("SimData{state=");
            m.append(this.simState);
            m.append(",slotId=");
            m.append(this.slotId);
            m.append(",subId=");
            m.append(this.subId);
            m.append("}");
            return m.toString();
        }

        public SimData(int i, int i2, int i3) {
            this.simState = i;
            this.slotId = i2;
            this.subId = i3;
        }
    }

    /* loaded from: classes.dex */
    public static class StrongAuthTracker extends LockPatternUtils.StrongAuthTracker {
        public final Consumer<Integer> mStrongAuthRequiredChangedCallback;

        public final void onStrongAuthRequiredChanged(int i) {
            this.mStrongAuthRequiredChangedCallback.accept(Integer.valueOf(i));
        }

        public StrongAuthTracker(Context context, DozeTriggers$$ExternalSyntheticLambda2 dozeTriggers$$ExternalSyntheticLambda2) {
            super(context);
            this.mStrongAuthRequiredChangedCallback = dozeTriggers$$ExternalSyntheticLambda2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v37, types: [com.android.keyguard.KeyguardUpdateMonitor$16] */
    /* JADX WARN: Type inference failed for: r12v0, types: [com.android.keyguard.KeyguardUpdateMonitor$19] */
    /* JADX WARN: Type inference failed for: r4v9, types: [com.android.keyguard.KeyguardUpdateMonitor$14, android.os.Handler] */
    /* JADX WARN: Type inference failed for: r5v0, types: [com.android.keyguard.KeyguardUpdateMonitor$1, com.android.systemui.plugins.statusbar.StatusBarStateController$StateListener] */
    /* JADX WARN: Type inference failed for: r5v21, types: [com.android.keyguard.KeyguardUpdateMonitor$18] */
    /* JADX WARN: Type inference failed for: r8v15, types: [com.android.keyguard.KeyguardUpdateMonitor$6] */
    /* JADX WARN: Type inference failed for: r8v16, types: [com.android.keyguard.KeyguardUpdateMonitor$7] */
    /* JADX WARN: Type inference failed for: r8v5, types: [com.android.keyguard.KeyguardUpdateMonitor$4] */
    /* JADX WARN: Unknown variable types count: 2 */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public KeyguardUpdateMonitor(android.content.Context r15, android.os.Looper r16, com.android.systemui.broadcast.BroadcastDispatcher r17, com.android.systemui.dump.DumpManager r18, java.util.concurrent.Executor r19, java.util.concurrent.Executor r20, com.android.systemui.plugins.statusbar.StatusBarStateController r21, com.android.internal.widget.LockPatternUtils r22, com.android.systemui.biometrics.AuthController r23, com.android.systemui.telephony.TelephonyListenerManager r24, com.android.internal.jank.InteractionJankMonitor r25, com.android.internal.util.LatencyTracker r26) {
        /*
            Method dump skipped, instructions count: 909
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardUpdateMonitor.<init>(android.content.Context, android.os.Looper, com.android.systemui.broadcast.BroadcastDispatcher, com.android.systemui.dump.DumpManager, java.util.concurrent.Executor, java.util.concurrent.Executor, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.internal.widget.LockPatternUtils, com.android.systemui.biometrics.AuthController, com.android.systemui.telephony.TelephonyListenerManager, com.android.internal.jank.InteractionJankMonitor, com.android.internal.util.LatencyTracker):void");
    }

    public static boolean containsFlag(int i, int i2) {
        return (i & i2) != 0;
    }

    public final ArrayList getFilteredSubscriptionInfo() {
        ArrayList subscriptionInfo = getSubscriptionInfo(false);
        if (subscriptionInfo.size() == 2) {
            SubscriptionInfo subscriptionInfo2 = (SubscriptionInfo) subscriptionInfo.get(0);
            SubscriptionInfo subscriptionInfo3 = (SubscriptionInfo) subscriptionInfo.get(1);
            if (subscriptionInfo2.getGroupUuid() != null && subscriptionInfo2.getGroupUuid().equals(subscriptionInfo3.getGroupUuid())) {
                if (!subscriptionInfo2.isOpportunistic() && !subscriptionInfo3.isOpportunistic()) {
                    return subscriptionInfo;
                }
                if (CarrierConfigManager.getDefaultConfig().getBoolean("always_show_primary_signal_bar_in_opportunistic_network_boolean")) {
                    if (!subscriptionInfo2.isOpportunistic()) {
                        subscriptionInfo2 = subscriptionInfo3;
                    }
                    subscriptionInfo.remove(subscriptionInfo2);
                } else {
                    if (subscriptionInfo2.getSubscriptionId() == this.mActiveMobileDataSubscription) {
                        subscriptionInfo2 = subscriptionInfo3;
                    }
                    subscriptionInfo.remove(subscriptionInfo2);
                }
            }
        }
        return subscriptionInfo;
    }

    public final int getNextSubIdForState(int i) {
        ArrayList subscriptionInfo = getSubscriptionInfo(false);
        int i2 = -1;
        int i3 = Integer.MAX_VALUE;
        for (int i4 = 0; i4 < subscriptionInfo.size(); i4++) {
            int subscriptionId = ((SubscriptionInfo) subscriptionInfo.get(i4)).getSubscriptionId();
            int slotId = getSlotId(subscriptionId);
            if (i == getSimState(subscriptionId) && i3 > slotId) {
                i2 = subscriptionId;
                i3 = slotId;
            }
        }
        return i2;
    }

    public final SubscriptionInfo getSubscriptionInfoForSubId(int i) {
        ArrayList subscriptionInfo = getSubscriptionInfo(false);
        for (int i2 = 0; i2 < subscriptionInfo.size(); i2++) {
            SubscriptionInfo subscriptionInfo2 = (SubscriptionInfo) subscriptionInfo.get(i2);
            if (i == subscriptionInfo2.getSubscriptionId()) {
                return subscriptionInfo2;
            }
        }
        return null;
    }

    public final boolean isSimPinSecure() {
        boolean z;
        Iterator it = getSubscriptionInfo(false).iterator();
        while (it.hasNext()) {
            int simState = getSimState(((SubscriptionInfo) it.next()).getSubscriptionId());
            if (simState == 2 || simState == 3 || simState == 7) {
                z = true;
                continue;
            } else {
                z = false;
                continue;
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    @VisibleForTesting
    public void resetBiometricListeningState() {
        this.mFingerprintRunningState = 0;
        this.mFaceRunningState = 0;
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class BiometricAuthenticated {
        public final boolean mAuthenticated = true;
        public final boolean mIsStrongBiometric;

        public BiometricAuthenticated(boolean z) {
            this.mIsStrongBiometric = z;
        }
    }

    static {
        boolean z = Build.IS_DEBUGGABLE;
        DEBUG_FACE = z;
        DEBUG_FINGERPRINT = z;
        DEBUG_ACTIVE_UNLOCK = z;
        try {
            CORE_APPS_ONLY = IPackageManager.Stub.asInterface(ServiceManager.getService("package")).isOnlyCoreApps();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static synchronized int getCurrentUser() {
        int i;
        synchronized (KeyguardUpdateMonitor.class) {
            i = sCurrentUser;
        }
        return i;
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        printWriter.println("KeyguardUpdateMonitor state:");
        printWriter.println("  SIM States:");
        for (SimData simData : this.mSimDatas.values()) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("    ");
            m.append(simData.toString());
            printWriter.println(m.toString());
        }
        printWriter.println("  Subs:");
        if (this.mSubscriptionInfo != null) {
            for (int i = 0; i < this.mSubscriptionInfo.size(); i++) {
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("    ");
                m2.append(this.mSubscriptionInfo.get(i));
                printWriter.println(m2.toString());
            }
        }
        StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("  Current active data subId=");
        m3.append(this.mActiveMobileDataSubscription);
        printWriter.println(m3.toString());
        printWriter.println("  Service states:");
        for (Integer num : this.mServiceStates.keySet()) {
            int intValue = num.intValue();
            StringBuilder m4 = ExifInterface$$ExternalSyntheticOutline0.m("    ", intValue, "=");
            m4.append(this.mServiceStates.get(Integer.valueOf(intValue)));
            printWriter.println(m4.toString());
        }
        FingerprintManager fingerprintManager = this.mFpm;
        if (fingerprintManager == null || !fingerprintManager.isHardwareDetected()) {
            str = "    enabledByUser=";
        } else {
            int currentUser = ActivityManager.getCurrentUser();
            int strongAuthForUser = this.mStrongAuthTracker.getStrongAuthForUser(currentUser);
            BiometricAuthenticated biometricAuthenticated = this.mUserFingerprintAuthenticated.get(currentUser);
            printWriter.println("  Fingerprint state (user=" + currentUser + ")");
            StringBuilder sb = new StringBuilder();
            sb.append("    areAllAuthenticatorsRegistered=");
            AuthController authController = this.mAuthController;
            Objects.requireNonNull(authController);
            StringBuilder m5 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb, authController.mAllAuthenticatorsRegistered, printWriter, "    allowed=");
            if (biometricAuthenticated == null || !isUnlockingWithBiometricAllowed(biometricAuthenticated.mIsStrongBiometric)) {
                z4 = false;
            } else {
                z4 = true;
            }
            StringBuilder m6 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(m5, z4, printWriter, "    auth'd=");
            if (biometricAuthenticated == null || !biometricAuthenticated.mAuthenticated) {
                z5 = false;
            } else {
                z5 = true;
            }
            StringBuilder m7 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(m6, z5, printWriter, "    authSinceBoot=");
            StrongAuthTracker strongAuthTracker = this.mStrongAuthTracker;
            Objects.requireNonNull(strongAuthTracker);
            if ((strongAuthTracker.getStrongAuthForUser(getCurrentUser()) & 1) == 0) {
                z6 = true;
            } else {
                z6 = false;
            }
            StringBuilder m8 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(m7, z6, printWriter, "    disabled(DPM)=");
            m8.append(isFingerprintDisabled(currentUser));
            printWriter.println(m8.toString());
            printWriter.println("    possible=" + isUnlockWithFingerprintPossible(currentUser));
            printWriter.println("    listening: actual=" + this.mFingerprintRunningState + " expected=" + (shouldListenForFingerprint(isUdfpsEnrolled()) ? 1 : 0));
            StringBuilder sb2 = new StringBuilder();
            sb2.append("    strongAuthFlags=");
            sb2.append(Integer.toHexString(strongAuthForUser));
            printWriter.println(sb2.toString());
            printWriter.println("    trustManaged=" + getUserTrustIsManaged(currentUser));
            StringBuilder sb3 = new StringBuilder();
            sb3.append("    mFingerprintLockedOut=");
            str = "    enabledByUser=";
            StringBuilder m9 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb3, this.mFingerprintLockedOut, printWriter, "    mFingerprintLockedOutPermanent="), this.mFingerprintLockedOutPermanent, printWriter, str);
            m9.append(this.mBiometricEnabledForUser.get(currentUser));
            printWriter.println(m9.toString());
            if (isUdfpsSupported()) {
                StringBuilder m10 = VendorAtomValue$$ExternalSyntheticOutline1.m("        udfpsEnrolled=");
                m10.append(isUdfpsEnrolled());
                printWriter.println(m10.toString());
                printWriter.println("        shouldListenForUdfps=" + shouldListenForFingerprint(true));
                StringBuilder sb4 = new StringBuilder();
                sb4.append("        bouncerVisible=");
                StringBuilder m11 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb4, this.mBouncer, printWriter, "        mStatusBarState=");
                m11.append(R$id.toShortString(this.mStatusBarState));
                printWriter.println(m11.toString());
            }
        }
        FaceManager faceManager = this.mFaceManager;
        if (faceManager != null && faceManager.isHardwareDetected()) {
            int currentUser2 = ActivityManager.getCurrentUser();
            int strongAuthForUser2 = this.mStrongAuthTracker.getStrongAuthForUser(currentUser2);
            BiometricAuthenticated biometricAuthenticated2 = this.mUserFaceAuthenticated.get(currentUser2);
            printWriter.println("  Face authentication state (user=" + currentUser2 + ")");
            StringBuilder sb5 = new StringBuilder();
            sb5.append("    allowed=");
            if (biometricAuthenticated2 == null || !isUnlockingWithBiometricAllowed(biometricAuthenticated2.mIsStrongBiometric)) {
                z = false;
            } else {
                z = true;
            }
            StringBuilder m12 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb5, z, printWriter, "    auth'd=");
            if (biometricAuthenticated2 == null || !biometricAuthenticated2.mAuthenticated) {
                z2 = false;
            } else {
                z2 = true;
            }
            StringBuilder m13 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(m12, z2, printWriter, "    authSinceBoot=");
            StrongAuthTracker strongAuthTracker2 = this.mStrongAuthTracker;
            Objects.requireNonNull(strongAuthTracker2);
            boolean z7 = true;
            if ((strongAuthTracker2.getStrongAuthForUser(getCurrentUser()) & 1) == 0) {
                z3 = true;
            } else {
                z3 = false;
            }
            StringBuilder m14 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(m13, z3, printWriter, "    disabled(DPM)=");
            m14.append(isFaceDisabled(currentUser2));
            printWriter.println(m14.toString());
            StringBuilder sb6 = new StringBuilder();
            sb6.append("    possible=");
            boolean booleanValue = ((Boolean) DejankUtils.whitelistIpcs(new KeyguardUpdateMonitor$$ExternalSyntheticLambda10(this, currentUser2))).booleanValue();
            this.mIsFaceEnrolled = booleanValue;
            if (!booleanValue || isFaceDisabled(currentUser2)) {
                z7 = false;
            }
            StringBuilder m15 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb6, z7, printWriter, "    listening: actual=");
            m15.append(this.mFaceRunningState);
            m15.append(" expected=(");
            m15.append(shouldListenForFace() ? 1 : 0);
            printWriter.println(m15.toString());
            printWriter.println("    strongAuthFlags=" + Integer.toHexString(strongAuthForUser2));
            printWriter.println("    trustManaged=" + getUserTrustIsManaged(currentUser2));
            StringBuilder sb7 = new StringBuilder();
            sb7.append("    mFaceLockedOutPermanent=");
            StringBuilder m16 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb7, this.mFaceLockedOutPermanent, printWriter, str);
            m16.append(this.mBiometricEnabledForUser.get(currentUser2));
            printWriter.println(m16.toString());
            printWriter.println("    mSecureCameraLaunched=" + this.mSecureCameraLaunched);
        }
        KeyguardListenQueue keyguardListenQueue = this.mListenModels;
        Objects.requireNonNull(keyguardListenQueue);
        KeyguardListenQueue$print$stringify$1 keyguardListenQueue$print$stringify$1 = new KeyguardListenQueue$print$stringify$1(KeyguardListenQueueKt.DEFAULT_FORMATTING);
        StringBuilder m17 = VendorAtomValue$$ExternalSyntheticOutline1.m("  Face listen results (last ");
        ArrayDeque<KeyguardFaceListenModel> arrayDeque = keyguardListenQueue.faceQueue;
        Objects.requireNonNull(arrayDeque);
        m17.append(arrayDeque.size);
        m17.append(" calls):");
        printWriter.println(m17.toString());
        Iterator<KeyguardFaceListenModel> it = keyguardListenQueue.faceQueue.iterator();
        while (it.hasNext()) {
            printWriter.println((String) keyguardListenQueue$print$stringify$1.invoke(it.next()));
        }
        StringBuilder m18 = VendorAtomValue$$ExternalSyntheticOutline1.m("  Fingerprint listen results (last ");
        ArrayDeque<KeyguardFingerprintListenModel> arrayDeque2 = keyguardListenQueue.fingerprintQueue;
        Objects.requireNonNull(arrayDeque2);
        m18.append(arrayDeque2.size);
        m18.append(" calls):");
        printWriter.println(m18.toString());
        Iterator<KeyguardFingerprintListenModel> it2 = keyguardListenQueue.fingerprintQueue.iterator();
        while (it2.hasNext()) {
            printWriter.println((String) keyguardListenQueue$print$stringify$1.invoke(it2.next()));
        }
        StringBuilder m19 = VendorAtomValue$$ExternalSyntheticOutline1.m("  Active unlock triggers (last ");
        ArrayDeque<KeyguardActiveUnlockModel> arrayDeque3 = keyguardListenQueue.activeUnlockQueue;
        Objects.requireNonNull(arrayDeque3);
        m19.append(arrayDeque3.size);
        m19.append(" calls):");
        printWriter.println(m19.toString());
        Iterator<KeyguardActiveUnlockModel> it3 = keyguardListenQueue.activeUnlockQueue.iterator();
        while (it3.hasNext()) {
            printWriter.println((String) keyguardListenQueue$print$stringify$1.invoke(it3.next()));
        }
        if (this.mIsAutomotive) {
            printWriter.println("  Running on Automotive build");
        }
    }

    public final int getSimState(int i) {
        if (this.mSimDatas.containsKey(Integer.valueOf(i))) {
            return this.mSimDatas.get(Integer.valueOf(i)).simState;
        }
        return 0;
    }

    public final int getSlotId(int i) {
        if (!this.mSimDatas.containsKey(Integer.valueOf(i))) {
            refreshSimState(i, SubscriptionManager.getSlotIndex(i));
        }
        return this.mSimDatas.get(Integer.valueOf(i)).slotId;
    }

    public final ArrayList getSubscriptionInfo(boolean z) {
        List<SubscriptionInfo> list = this.mSubscriptionInfo;
        if (list == null || z) {
            list = this.mSubscriptionManager.getCompleteActiveSubscriptionInfoList();
        }
        if (list == null) {
            this.mSubscriptionInfo = new ArrayList();
        } else {
            this.mSubscriptionInfo = list;
        }
        return new ArrayList(this.mSubscriptionInfo);
    }

    public final boolean getUserTrustIsManaged(int i) {
        if (!this.mUserTrustIsManaged.get(i) || isSimPinSecure()) {
            return false;
        }
        return true;
    }

    public final boolean getUserUnlockedWithBiometric(int i) {
        boolean z;
        boolean z2;
        BiometricAuthenticated biometricAuthenticated = this.mUserFingerprintAuthenticated.get(i);
        BiometricAuthenticated biometricAuthenticated2 = this.mUserFaceAuthenticated.get(i);
        if (biometricAuthenticated == null || !biometricAuthenticated.mAuthenticated || !isUnlockingWithBiometricAllowed(biometricAuthenticated.mIsStrongBiometric)) {
            z = false;
        } else {
            z = true;
        }
        if (biometricAuthenticated2 == null || !biometricAuthenticated2.mAuthenticated || !isUnlockingWithBiometricAllowed(biometricAuthenticated2.mIsStrongBiometric)) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (z || z2) {
            return true;
        }
        return false;
    }

    public final boolean getUserUnlockedWithBiometricAndIsBypassing(int i) {
        boolean z;
        boolean z2;
        BiometricAuthenticated biometricAuthenticated = this.mUserFingerprintAuthenticated.get(i);
        BiometricAuthenticated biometricAuthenticated2 = this.mUserFaceAuthenticated.get(i);
        if (biometricAuthenticated == null || !biometricAuthenticated.mAuthenticated || !isUnlockingWithBiometricAllowed(biometricAuthenticated.mIsStrongBiometric)) {
            z = false;
        } else {
            z = true;
        }
        if (biometricAuthenticated2 == null || !biometricAuthenticated2.mAuthenticated || !isUnlockingWithBiometricAllowed(biometricAuthenticated2.mIsStrongBiometric)) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (z) {
            return true;
        }
        if (!z2 || !this.mKeyguardBypassController.canBypass()) {
            return false;
        }
        return true;
    }

    public final void handleFaceAuthenticated(int i, boolean z) {
        Trace.beginSection("KeyGuardUpdateMonitor#handlerFaceAuthenticated");
        try {
            if (this.mGoingToSleep) {
                Log.d("KeyguardUpdateMonitor", "Aborted successful auth because device is going to sleep.");
                return;
            }
            int i2 = ActivityManager.getService().getCurrentUser().id;
            if (i2 != i) {
                Log.d("KeyguardUpdateMonitor", "Face authenticated for wrong user: " + i);
            } else if (isFaceDisabled(i2)) {
                Log.d("KeyguardUpdateMonitor", "Face authentication disabled by DPM for userId: " + i2);
            } else {
                if (DEBUG_FACE) {
                    Log.d("KeyguardUpdateMonitor", "Face auth succeeded for user " + i2);
                }
                onFaceAuthenticated(i2, z);
                setFaceRunningState(0);
                Trace.endSection();
            }
        } catch (RemoteException e) {
            Log.e("KeyguardUpdateMonitor", "Failed to get current user id: ", e);
        } finally {
            setFaceRunningState(0);
        }
    }

    public final void handleFingerprintAuthenticated(int i, boolean z) {
        Trace.beginSection("KeyGuardUpdateMonitor#handlerFingerPrintAuthenticated");
        try {
            int i2 = ActivityManager.getService().getCurrentUser().id;
            if (i2 != i) {
                Log.d("KeyguardUpdateMonitor", "Fingerprint authenticated for wrong user: " + i);
            } else if (isFingerprintDisabled(i2)) {
                Log.d("KeyguardUpdateMonitor", "Fingerprint disabled by DPM for userId: " + i2);
            } else {
                onFingerprintAuthenticated(i2, z);
                setFingerprintRunningState(0);
                Trace.endSection();
            }
        } catch (RemoteException e) {
            Log.e("KeyguardUpdateMonitor", "Failed to get current user id: ", e);
        } finally {
            setFingerprintRunningState(0);
        }
    }

    @VisibleForTesting
    public void handleServiceStateChange(int i, ServiceState serviceState) {
        if (DEBUG) {
            Log.d("KeyguardUpdateMonitor", "handleServiceStateChange(subId=" + i + ", serviceState=" + serviceState);
        }
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            Log.w("KeyguardUpdateMonitor", "invalid subId in handleServiceStateChange()");
            return;
        }
        updateTelephonyCapable(true);
        this.mServiceStates.put(Integer.valueOf(i), serviceState);
        callbacksRefreshCarrierInfo();
    }

    public final boolean isDeviceProvisionedInSettingsDb() {
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "device_provisioned", 0) != 0) {
            return true;
        }
        return false;
    }

    public final boolean isEncryptedOrLockdown(int i) {
        boolean z;
        int strongAuthForUser = this.mStrongAuthTracker.getStrongAuthForUser(i);
        if (containsFlag(strongAuthForUser, 2) || containsFlag(strongAuthForUser, 32)) {
            z = true;
        } else {
            z = false;
        }
        if (containsFlag(strongAuthForUser, 1) || z) {
            return true;
        }
        return false;
    }

    public final boolean isFaceDisabled(final int i) {
        final DevicePolicyManager devicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService("device_policy");
        return ((Boolean) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda11
            /* JADX WARN: Code restructure failed: missing block: B:5:0x0012, code lost:
                if ((r1.getKeyguardDisabledFeatures(null, r3) & 128) == 0) goto L_0x0014;
             */
            @Override // java.util.function.Supplier
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final java.lang.Object get() {
                /*
                    r3 = this;
                    com.android.keyguard.KeyguardUpdateMonitor r0 = com.android.keyguard.KeyguardUpdateMonitor.this
                    android.app.admin.DevicePolicyManager r1 = r2
                    int r3 = r3
                    if (r1 == 0) goto L_0x0014
                    java.util.Objects.requireNonNull(r0)
                    r2 = 0
                    int r3 = r1.getKeyguardDisabledFeatures(r2, r3)
                    r3 = r3 & 128(0x80, float:1.794E-43)
                    if (r3 != 0) goto L_0x001a
                L_0x0014:
                    boolean r3 = r0.isSimPinSecure()
                    if (r3 == 0) goto L_0x001c
                L_0x001a:
                    r3 = 1
                    goto L_0x001d
                L_0x001c:
                    r3 = 0
                L_0x001d:
                    java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)
                    return r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda11.get():java.lang.Object");
            }
        })).booleanValue();
    }

    public final boolean isFingerprintDetectionRunning() {
        if (this.mFingerprintRunningState == 1) {
            return true;
        }
        return false;
    }

    public final boolean isFingerprintDisabled(int i) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService("device_policy");
        if ((devicePolicyManager == null || (devicePolicyManager.getKeyguardDisabledFeatures(null, i) & 32) == 0) && !isSimPinSecure()) {
            return false;
        }
        return true;
    }

    public final boolean isUdfpsEnrolled() {
        return this.mAuthController.isUdfpsEnrolled(getCurrentUser());
    }

    public final boolean isUdfpsSupported() {
        AuthController authController = this.mAuthController;
        Objects.requireNonNull(authController);
        if (authController.mUdfpsProps != null) {
            AuthController authController2 = this.mAuthController;
            Objects.requireNonNull(authController2);
            if (!authController2.mUdfpsProps.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public final boolean isUnlockWithFingerprintPossible(int i) {
        FingerprintManager fingerprintManager = this.mFpm;
        if (fingerprintManager == null || !fingerprintManager.isHardwareDetected() || isFingerprintDisabled(i) || !this.mFpm.hasEnrolledTemplates(i)) {
            return false;
        }
        return true;
    }

    public final boolean isUnlockingWithBiometricAllowed(boolean z) {
        StrongAuthTracker strongAuthTracker = this.mStrongAuthTracker;
        Objects.requireNonNull(strongAuthTracker);
        return strongAuthTracker.isBiometricAllowedForUser(z, getCurrentUser());
    }

    public final void maybeLogListenerModelData(KeyguardListenModel keyguardListenModel) {
        boolean z;
        if (!DEBUG_ACTIVE_UNLOCK || !(keyguardListenModel instanceof KeyguardActiveUnlockModel) || !keyguardListenModel.getListening()) {
            boolean z2 = DEBUG_FACE;
            boolean z3 = false;
            if ((!z2 || !(keyguardListenModel instanceof KeyguardFaceListenModel) || this.mFaceRunningState == 1) && (!DEBUG_FINGERPRINT || !(keyguardListenModel instanceof KeyguardFingerprintListenModel) || this.mFingerprintRunningState == 1)) {
                z = false;
            } else {
                z = true;
            }
            if ((z2 && (keyguardListenModel instanceof KeyguardFaceListenModel) && this.mFaceRunningState == 1) || (DEBUG_FINGERPRINT && (keyguardListenModel instanceof KeyguardFingerprintListenModel) && this.mFingerprintRunningState == 1)) {
                z3 = true;
            }
            if ((z && keyguardListenModel.getListening()) || (z3 && !keyguardListenModel.getListening())) {
                this.mListenModels.add(keyguardListenModel);
                return;
            }
            return;
        }
        this.mListenModels.add(keyguardListenModel);
    }

    @VisibleForTesting
    public void onFaceAuthenticated(int i, boolean z) {
        Trace.beginSection("KeyGuardUpdateMonitor#onFaceAuthenticated");
        Assert.isMainThread();
        this.mUserFaceAuthenticated.put(i, new BiometricAuthenticated(z));
        if (getUserCanSkipBouncer(i)) {
            this.mTrustManager.unlockedByBiometricForUser(i, BiometricSourceType.FACE);
        }
        this.mFaceCancelSignal = null;
        updateBiometricListeningState(2);
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricAuthenticated(i, BiometricSourceType.FACE, z);
            }
        }
        this.mAssistantVisible = false;
        this.mBackgroundExecutor.execute(new AnonymousClass5(z, i));
        Trace.endSection();
    }

    public final boolean refreshSimState(int i, int i2) {
        int i3;
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        boolean z = false;
        if (telephonyManager != null) {
            i3 = telephonyManager.getSimState(i2);
        } else {
            i3 = 0;
        }
        SimData simData = this.mSimDatas.get(Integer.valueOf(i));
        if (simData == null) {
            this.mSimDatas.put(Integer.valueOf(i), new SimData(i3, i2, i));
            return true;
        }
        if (simData.simState != i3) {
            z = true;
        }
        simData.simState = i3;
        return z;
    }

    public final void reportSimUnlocked(int i) {
        Log.v("KeyguardUpdateMonitor", "reportSimUnlocked(subId=" + i + ")");
        handleSimStateChange(i, getSlotId(i), 5);
    }

    public final void requestActiveUnlock() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        if (!hasMessages(336)) {
            boolean z7 = false;
            if (!this.mAssistantVisible || !this.mKeyguardOccluded || this.mUserHasTrust.get(getCurrentUser(), false)) {
                z = false;
            } else {
                z = true;
            }
            if (!this.mKeyguardIsVisible || !this.mDeviceInteractive || this.mGoingToSleep || this.mStatusBarState == 2) {
                z2 = false;
            } else {
                z2 = true;
            }
            int currentUser = getCurrentUser();
            if (getUserCanSkipBouncer(currentUser) || !this.mLockPatternUtils.isSecure(currentUser)) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (this.mFingerprintLockedOut || this.mFingerprintLockedOutPermanent) {
                z4 = true;
            } else {
                z4 = false;
            }
            int strongAuthForUser = this.mStrongAuthTracker.getStrongAuthForUser(currentUser);
            if (containsFlag(strongAuthForUser, 2) || containsFlag(strongAuthForUser, 32)) {
                z5 = true;
            } else {
                z5 = false;
            }
            if (containsFlag(strongAuthForUser, 1) || containsFlag(strongAuthForUser, 16)) {
                z6 = true;
            } else {
                z6 = false;
            }
            if ((this.mAuthInterruptActive || z || z2) && !this.mSwitchingUser && !z3 && !z4 && !z5 && !z6 && !this.mKeyguardGoingAway && !this.mSecureCameraLaunched) {
                z7 = true;
            }
            if (DEBUG_ACTIVE_UNLOCK) {
                maybeLogListenerModelData(new KeyguardActiveUnlockModel(System.currentTimeMillis(), currentUser, z7, this.mAuthInterruptActive, z6, z4, z5, this.mSwitchingUser, z, z3));
            }
            if (z7) {
                this.mTrustManager.reportUserRequestedUnlock(getCurrentUser());
            }
        }
    }

    public final void requestFaceAuth(boolean z) {
        if (DEBUG) {
            ViewCompat$$ExternalSyntheticLambda0.m("requestFaceAuth() userInitiated=", z, "KeyguardUpdateMonitor");
        }
        updateFaceListeningState(2);
    }

    @VisibleForTesting
    public void setAssistantVisible(boolean z) {
        this.mAssistantVisible = z;
        updateBiometricListeningState(2);
        if (this.mAssistantVisible) {
            requestActiveUnlock();
        }
    }

    public final void setFaceRunningState(int i) {
        boolean z;
        boolean z2;
        boolean z3;
        if (this.mFaceRunningState == 1) {
            z = true;
        } else {
            z = false;
        }
        if (i == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.mFaceRunningState = i;
        KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(VendorAtomValue$$ExternalSyntheticOutline1.m("faceRunningState: "), this.mFaceRunningState, "KeyguardUpdateMonitor");
        if (z != z2) {
            Assert.isMainThread();
            for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
                if (keyguardUpdateMonitorCallback != null) {
                    if (this.mFaceRunningState == 1) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    keyguardUpdateMonitorCallback.onBiometricRunningStateChanged(z3, BiometricSourceType.FACE);
                }
            }
        }
    }

    public final void setFingerprintRunningState(int i) {
        boolean z;
        boolean z2 = true;
        if (this.mFingerprintRunningState == 1) {
            z = true;
        } else {
            z = false;
        }
        if (i != 1) {
            z2 = false;
        }
        this.mFingerprintRunningState = i;
        KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(VendorAtomValue$$ExternalSyntheticOutline1.m("fingerprintRunningState: "), this.mFingerprintRunningState, "KeyguardUpdateMonitor");
        if (z != z2) {
            Assert.isMainThread();
            for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onBiometricRunningStateChanged(isFingerprintDetectionRunning(), BiometricSourceType.FINGERPRINT);
                }
            }
        }
    }

    @VisibleForTesting
    public void setStrongAuthTracker(StrongAuthTracker strongAuthTracker) {
        StrongAuthTracker strongAuthTracker2 = this.mStrongAuthTracker;
        if (strongAuthTracker2 != null) {
            this.mLockPatternUtils.unregisterStrongAuthTracker(strongAuthTracker2);
        }
        this.mStrongAuthTracker = strongAuthTracker;
        this.mLockPatternUtils.registerStrongAuthTracker(strongAuthTracker);
    }

    public final boolean shouldListenForFace() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9;
        boolean z10;
        boolean z11;
        boolean z12;
        boolean z13 = false;
        if (this.mFaceManager == null) {
            return false;
        }
        if (this.mStatusBarState == 2) {
            z = true;
        } else {
            z = false;
        }
        if (!this.mKeyguardIsVisible || !this.mDeviceInteractive || this.mGoingToSleep || z) {
            z2 = false;
        } else {
            z2 = true;
        }
        int currentUser = getCurrentUser();
        int strongAuthForUser = this.mStrongAuthTracker.getStrongAuthForUser(currentUser);
        if (containsFlag(strongAuthForUser, 2) || containsFlag(strongAuthForUser, 32)) {
            z3 = true;
        } else {
            z3 = false;
        }
        if (containsFlag(strongAuthForUser, 1) || containsFlag(strongAuthForUser, 16)) {
            z4 = true;
        } else {
            z4 = false;
        }
        if (this.mFingerprintLockedOut || this.mFingerprintLockedOutPermanent) {
            z5 = true;
        } else {
            z5 = false;
        }
        KeyguardBypassController keyguardBypassController = this.mKeyguardBypassController;
        if (keyguardBypassController == null || !keyguardBypassController.canBypass()) {
            z6 = false;
        } else {
            z6 = true;
        }
        if (!getUserCanSkipBouncer(currentUser) || z6) {
            z7 = true;
        } else {
            z7 = false;
        }
        if (!z4 || (z6 && !this.mBouncer)) {
            z8 = true;
        } else {
            z8 = false;
        }
        if (this.mFaceSensorProperties.isEmpty() || !this.mFaceSensorProperties.get(0).supportsFaceDetection) {
            z9 = false;
        } else {
            z9 = true;
        }
        if (!z3 || z9) {
            z10 = z8;
        } else {
            z10 = false;
        }
        BiometricAuthenticated biometricAuthenticated = this.mUserFaceAuthenticated.get(getCurrentUser());
        if (biometricAuthenticated != null) {
            z11 = biometricAuthenticated.mAuthenticated;
        } else {
            z11 = false;
        }
        boolean isFaceDisabled = isFaceDisabled(currentUser);
        boolean z14 = this.mBiometricEnabledForUser.get(currentUser);
        BiometricAuthenticated biometricAuthenticated2 = this.mUserFaceAuthenticated.get(getCurrentUser());
        if (!this.mAssistantVisible || !this.mKeyguardOccluded || ((biometricAuthenticated2 != null && biometricAuthenticated2.mAuthenticated) || this.mUserHasTrust.get(getCurrentUser(), false))) {
            z12 = false;
        } else {
            z12 = true;
        }
        if ((this.mBouncer || this.mAuthInterruptActive || this.mOccludingAppRequestingFace || z2 || z12) && !this.mSwitchingUser && !isFaceDisabled && z7 && !this.mKeyguardGoingAway && z14 && z10 && this.mIsPrimaryUser && ((!this.mSecureCameraLaunched || this.mOccludingAppRequestingFace) && !z11 && !z5)) {
            z13 = true;
        }
        if (DEBUG_FACE) {
            maybeLogListenerModelData(new KeyguardFaceListenModel(System.currentTimeMillis(), currentUser, z13, this.mAuthInterruptActive, z7, z14, this.mBouncer, z11, isFaceDisabled, z2, this.mKeyguardGoingAway, z12, false, this.mOccludingAppRequestingFace, this.mIsPrimaryUser, z10, this.mSecureCameraLaunched, this.mSwitchingUser));
        }
        return z13;
    }

    @VisibleForTesting
    public boolean shouldListenForFingerprint(boolean z) {
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        int currentUser = getCurrentUser();
        boolean z9 = !getUserHasTrust(currentUser);
        BiometricAuthenticated biometricAuthenticated = this.mUserFingerprintAuthenticated.get(getCurrentUser());
        if (!this.mAssistantVisible || !this.mKeyguardOccluded || ((biometricAuthenticated != null && biometricAuthenticated.mAuthenticated) || this.mUserHasTrust.get(getCurrentUser(), false))) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (this.mKeyguardIsVisible || !this.mDeviceInteractive || ((this.mBouncer && !this.mKeyguardGoingAway) || this.mGoingToSleep || z2 || (((z8 = this.mKeyguardOccluded) && this.mIsDreaming) || (z8 && z9 && (this.mOccludingAppRequestingFp || z))))) {
            z3 = true;
        } else {
            z3 = false;
        }
        boolean z10 = this.mBiometricEnabledForUser.get(currentUser);
        boolean userCanSkipBouncer = getUserCanSkipBouncer(currentUser);
        boolean isFingerprintDisabled = isFingerprintDisabled(currentUser);
        if (this.mSwitchingUser || isFingerprintDisabled || ((this.mKeyguardGoingAway && this.mDeviceInteractive) || !this.mIsPrimaryUser || !z10)) {
            z4 = false;
        } else {
            z4 = true;
        }
        if (!this.mFingerprintLockedOut || !this.mBouncer || !this.mCredentialAttempted) {
            z5 = true;
        } else {
            z5 = false;
        }
        boolean isEncryptedOrLockdown = isEncryptedOrLockdown(currentUser);
        if (!z || (!userCanSkipBouncer && !isEncryptedOrLockdown && z9 && !this.mFingerprintLockedOut)) {
            z6 = true;
        } else {
            z6 = false;
        }
        if (!z3 || !z4 || !z5 || !z6) {
            z7 = false;
        } else {
            z7 = true;
        }
        if (DEBUG_FINGERPRINT) {
            maybeLogListenerModelData(new KeyguardFingerprintListenModel(System.currentTimeMillis(), currentUser, z7, z10, this.mBouncer, userCanSkipBouncer, this.mCredentialAttempted, this.mDeviceInteractive, this.mIsDreaming, isEncryptedOrLockdown, isFingerprintDisabled, this.mFingerprintLockedOut, this.mGoingToSleep, this.mKeyguardGoingAway, this.mKeyguardIsVisible, this.mKeyguardOccluded, this.mOccludingAppRequestingFp, this.mIsPrimaryUser, z2, this.mSwitchingUser, z, z9));
        }
        return z7;
    }

    public final void stopListeningForFace() {
        if (DEBUG) {
            Log.v("KeyguardUpdateMonitor", "stopListeningForFace()");
        }
        if (this.mFaceRunningState == 1) {
            CancellationSignal cancellationSignal = this.mFaceCancelSignal;
            if (cancellationSignal != null) {
                cancellationSignal.cancel();
                this.mFaceCancelSignal = null;
                removeCallbacks(this.mFaceCancelNotReceived);
                postDelayed(this.mFaceCancelNotReceived, 3000L);
            }
            setFaceRunningState(2);
        }
        if (this.mFaceRunningState == 3) {
            setFaceRunningState(2);
        }
    }

    public final void updateFaceListeningState(int i) {
        boolean z;
        boolean z2;
        boolean z3;
        if (!hasMessages(336)) {
            removeCallbacks(this.mRetryFaceAuthentication);
            boolean shouldListenForFace = shouldListenForFace();
            int i2 = this.mFaceRunningState;
            if (i2 != 1 || shouldListenForFace) {
                if (i2 != 1 && shouldListenForFace) {
                    if (i == 1) {
                        Log.v("KeyguardUpdateMonitor", "Ignoring startListeningForFace()");
                        return;
                    }
                    int currentUser = getCurrentUser();
                    boolean booleanValue = ((Boolean) DejankUtils.whitelistIpcs(new KeyguardUpdateMonitor$$ExternalSyntheticLambda10(this, currentUser))).booleanValue();
                    this.mIsFaceEnrolled = booleanValue;
                    if (!booleanValue || isFaceDisabled(currentUser)) {
                        z = false;
                    } else {
                        z = true;
                    }
                    if (this.mFaceCancelSignal != null) {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Cancellation signal is not null, high chance of bug in face auth lifecycle management. Face state: ");
                        m.append(this.mFaceRunningState);
                        m.append(", unlockPossible: ");
                        m.append(z);
                        Log.e("KeyguardUpdateMonitor", m.toString());
                    }
                    int i3 = this.mFaceRunningState;
                    if (i3 == 2) {
                        setFaceRunningState(3);
                    } else if (i3 != 3) {
                        if (DEBUG) {
                            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("startListeningForFace(): ");
                            m2.append(this.mFaceRunningState);
                            Log.v("KeyguardUpdateMonitor", m2.toString());
                        }
                        if (z) {
                            this.mFaceCancelSignal = new CancellationSignal();
                            if (this.mFaceSensorProperties.isEmpty() || !this.mFaceSensorProperties.get(0).supportsFaceDetection) {
                                z2 = false;
                            } else {
                                z2 = true;
                            }
                            this.mFaceAuthUserId = currentUser;
                            if (!isEncryptedOrLockdown(currentUser) || !z2) {
                                KeyguardBypassController keyguardBypassController = this.mKeyguardBypassController;
                                if (keyguardBypassController == null || !keyguardBypassController.getBypassEnabled()) {
                                    z3 = false;
                                } else {
                                    z3 = true;
                                }
                                this.mFaceManager.authenticate((CryptoObject) null, this.mFaceCancelSignal, this.mFaceAuthenticationCallback, (Handler) null, currentUser, z3);
                            } else {
                                this.mFaceManager.detectFace(this.mFaceCancelSignal, this.mFaceDetectionCallback, currentUser);
                            }
                            setFaceRunningState(1);
                        }
                    }
                }
            } else if (i == 0) {
                Log.v("KeyguardUpdateMonitor", "Ignoring stopListeningForFace()");
            } else {
                stopListeningForFace();
            }
        }
    }

    public final void updateFingerprintListeningState(int i) {
        boolean z;
        if (!hasMessages(336)) {
            AuthController authController = this.mAuthController;
            Objects.requireNonNull(authController);
            if (authController.mAllAuthenticatorsRegistered) {
                boolean shouldListenForFingerprint = shouldListenForFingerprint(isUdfpsSupported());
                int i2 = this.mFingerprintRunningState;
                if (i2 == 1 || i2 == 3) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z || shouldListenForFingerprint) {
                    if (!z && shouldListenForFingerprint) {
                        if (i == 1) {
                            Log.v("KeyguardUpdateMonitor", "Ignoring startListeningForFingerprint()");
                            return;
                        }
                        int currentUser = getCurrentUser();
                        boolean isUnlockWithFingerprintPossible = isUnlockWithFingerprintPossible(currentUser);
                        if (this.mFingerprintCancelSignal != null) {
                            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Cancellation signal is not null, high chance of bug in fp auth lifecycle management. FP state: ");
                            m.append(this.mFingerprintRunningState);
                            m.append(", unlockPossible: ");
                            m.append(isUnlockWithFingerprintPossible);
                            Log.e("KeyguardUpdateMonitor", m.toString());
                        }
                        int i3 = this.mFingerprintRunningState;
                        if (i3 == 2) {
                            setFingerprintRunningState(3);
                        } else if (i3 != 3) {
                            if (DEBUG) {
                                Log.v("KeyguardUpdateMonitor", "startListeningForFingerprint()");
                            }
                            if (isUnlockWithFingerprintPossible) {
                                this.mFingerprintCancelSignal = new CancellationSignal();
                                if (isEncryptedOrLockdown(currentUser)) {
                                    this.mFpm.detectFingerprint(this.mFingerprintCancelSignal, this.mFingerprintDetectionCallback, currentUser);
                                } else {
                                    this.mFpm.authenticate(null, this.mFingerprintCancelSignal, this.mFingerprintAuthenticationCallback, null, -1, currentUser, 0);
                                }
                                setFingerprintRunningState(1);
                            }
                        }
                    }
                } else if (i == 0) {
                    Log.v("KeyguardUpdateMonitor", "Ignoring stopListeningForFingerprint()");
                } else {
                    if (DEBUG) {
                        Log.v("KeyguardUpdateMonitor", "stopListeningForFingerprint()");
                    }
                    if (this.mFingerprintRunningState == 1) {
                        CancellationSignal cancellationSignal = this.mFingerprintCancelSignal;
                        if (cancellationSignal != null) {
                            cancellationSignal.cancel();
                            this.mFingerprintCancelSignal = null;
                            removeCallbacks(this.mFpCancelNotReceived);
                            postDelayed(this.mFpCancelNotReceived, 3000L);
                        }
                        setFingerprintRunningState(2);
                    }
                    if (this.mFingerprintRunningState == 3) {
                        setFingerprintRunningState(2);
                    }
                }
            }
        }
    }

    public final void updateSecondaryLockscreenRequirement(int i) {
        boolean z;
        Intent intent = (Intent) this.mSecondaryLockscreenRequirement.get(Integer.valueOf(i));
        boolean isSecondaryLockscreenEnabled = this.mDevicePolicyManager.isSecondaryLockscreenEnabled(UserHandle.of(i));
        if (!isSecondaryLockscreenEnabled || intent != null) {
            if (!isSecondaryLockscreenEnabled && intent != null) {
                this.mSecondaryLockscreenRequirement.put(Integer.valueOf(i), null);
                z = true;
            }
            z = false;
        } else {
            ComponentName profileOwnerOrDeviceOwnerSupervisionComponent = this.mDevicePolicyManager.getProfileOwnerOrDeviceOwnerSupervisionComponent(UserHandle.of(i));
            if (profileOwnerOrDeviceOwnerSupervisionComponent == null) {
                KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("No Profile Owner or Device Owner supervision app found for User ", i, "KeyguardUpdateMonitor");
            } else {
                ResolveInfo resolveService = this.mContext.getPackageManager().resolveService(new Intent("android.app.action.BIND_SECONDARY_LOCKSCREEN_SERVICE").setPackage(profileOwnerOrDeviceOwnerSupervisionComponent.getPackageName()), 0);
                if (!(resolveService == null || resolveService.serviceInfo == null)) {
                    this.mSecondaryLockscreenRequirement.put(Integer.valueOf(i), new Intent().setComponent(resolveService.serviceInfo.getComponentName()));
                    z = true;
                }
            }
            z = false;
        }
        if (z) {
            for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onSecondaryLockscreenRequirementChanged(i);
                }
            }
        }
    }

    public final void callbacksRefreshCarrierInfo() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onRefreshCarrierInfo();
            }
        }
    }

    public final void clearBiometricRecognized(int i) {
        Assert.isMainThread();
        this.mUserFingerprintAuthenticated.clear();
        this.mUserFaceAuthenticated.clear();
        this.mTrustManager.clearAllBiometricRecognized(BiometricSourceType.FINGERPRINT, i);
        this.mTrustManager.clearAllBiometricRecognized(BiometricSourceType.FACE, i);
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricsCleared();
            }
        }
    }

    public final boolean getUserCanSkipBouncer(int i) {
        if (getUserHasTrust(i) || getUserUnlockedWithBiometric(i)) {
            return true;
        }
        return false;
    }

    public final boolean getUserHasTrust(int i) {
        if (isSimPinSecure() || !this.mUserHasTrust.get(i)) {
            return false;
        }
        return true;
    }

    public final void handleFaceHelp(int i, String str) {
        Assert.isMainThread();
        if (DEBUG_FACE) {
            DialogFragment$$ExternalSyntheticOutline0.m("Face help received: ", str, "KeyguardUpdateMonitor");
        }
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricHelp(i, str, BiometricSourceType.FACE);
            }
        }
    }

    public final void handleFingerprintHelp(int i, String str) {
        Assert.isMainThread();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricHelp(i, str, BiometricSourceType.FINGERPRINT);
            }
        }
    }

    public final void handleReportEmergencyCallAction() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onEmergencyCallAction();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00a8  */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void handleSimStateChange(int r7, int r8, int r9) {
        /*
            r6 = this;
            com.android.systemui.util.Assert.isMainThread()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "handleSimStateChange(subId="
            r0.append(r1)
            r0.append(r7)
            java.lang.String r1 = ", slotId="
            r0.append(r1)
            r0.append(r8)
            java.lang.String r1 = ", state="
            r0.append(r1)
            r0.append(r9)
            java.lang.String r1 = ")"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "KeyguardUpdateMonitor"
            android.util.Log.d(r1, r0)
            boolean r0 = android.telephony.SubscriptionManager.isValidSubscriptionId(r7)
            r2 = 0
            r3 = 1
            if (r0 != 0) goto L_0x0068
            java.lang.String r0 = "invalid subId in handleSimStateChange()"
            android.util.Log.w(r1, r0)
            if (r9 != r3) goto L_0x005f
            r6.updateTelephonyCapable(r3)
            java.util.HashMap<java.lang.Integer, com.android.keyguard.KeyguardUpdateMonitor$SimData> r0 = r6.mSimDatas
            java.util.Collection r0 = r0.values()
            java.util.Iterator r0 = r0.iterator()
        L_0x004a:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x005d
            java.lang.Object r1 = r0.next()
            com.android.keyguard.KeyguardUpdateMonitor$SimData r1 = (com.android.keyguard.KeyguardUpdateMonitor.SimData) r1
            int r4 = r1.slotId
            if (r4 != r8) goto L_0x004a
            r1.simState = r3
            goto L_0x004a
        L_0x005d:
            r0 = r3
            goto L_0x0069
        L_0x005f:
            r0 = 8
            if (r9 != r0) goto L_0x0067
            r6.updateTelephonyCapable(r3)
            goto L_0x0068
        L_0x0067:
            return
        L_0x0068:
            r0 = r2
        L_0x0069:
            java.util.HashMap<java.lang.Integer, com.android.keyguard.KeyguardUpdateMonitor$SimData> r1 = r6.mSimDatas
            java.lang.Integer r4 = java.lang.Integer.valueOf(r7)
            java.lang.Object r1 = r1.get(r4)
            com.android.keyguard.KeyguardUpdateMonitor$SimData r1 = (com.android.keyguard.KeyguardUpdateMonitor.SimData) r1
            if (r1 != 0) goto L_0x0086
            com.android.keyguard.KeyguardUpdateMonitor$SimData r1 = new com.android.keyguard.KeyguardUpdateMonitor$SimData
            r1.<init>(r9, r8, r7)
            java.util.HashMap<java.lang.Integer, com.android.keyguard.KeyguardUpdateMonitor$SimData> r4 = r6.mSimDatas
            java.lang.Integer r5 = java.lang.Integer.valueOf(r7)
            r4.put(r5, r1)
            goto L_0x009a
        L_0x0086:
            int r4 = r1.simState
            if (r4 != r9) goto L_0x0094
            int r4 = r1.subId
            if (r4 != r7) goto L_0x0094
            int r4 = r1.slotId
            if (r4 == r8) goto L_0x0093
            goto L_0x0094
        L_0x0093:
            r3 = r2
        L_0x0094:
            r1.simState = r9
            r1.subId = r7
            r1.slotId = r8
        L_0x009a:
            if (r3 != 0) goto L_0x009e
            if (r0 == 0) goto L_0x00be
        L_0x009e:
            if (r9 == 0) goto L_0x00be
        L_0x00a0:
            java.util.ArrayList<java.lang.ref.WeakReference<com.android.keyguard.KeyguardUpdateMonitorCallback>> r0 = r6.mCallbacks
            int r0 = r0.size()
            if (r2 >= r0) goto L_0x00be
            java.util.ArrayList<java.lang.ref.WeakReference<com.android.keyguard.KeyguardUpdateMonitorCallback>> r0 = r6.mCallbacks
            java.lang.Object r0 = r0.get(r2)
            java.lang.ref.WeakReference r0 = (java.lang.ref.WeakReference) r0
            java.lang.Object r0 = r0.get()
            com.android.keyguard.KeyguardUpdateMonitorCallback r0 = (com.android.keyguard.KeyguardUpdateMonitorCallback) r0
            if (r0 == 0) goto L_0x00bb
            r0.onSimStateChanged(r7, r8, r9)
        L_0x00bb:
            int r2 = r2 + 1
            goto L_0x00a0
        L_0x00be:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardUpdateMonitor.handleSimStateChange(int, int, int):void");
    }

    @VisibleForTesting
    public void handleUserRemoved(int i) {
        Assert.isMainThread();
        this.mUserIsUnlocked.delete(i);
        this.mUserTrustIsUsuallyManaged.delete(i);
    }

    @VisibleForTesting
    public void handleUserSwitchComplete(int i) {
        Assert.isMainThread();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onUserSwitchComplete(i);
            }
        }
        this.mInteractionJankMonitor.end(37);
        this.mLatencyTracker.onActionEnd(12);
    }

    @VisibleForTesting
    public void handleUserSwitching(int i, IRemoteCallback iRemoteCallback) {
        Assert.isMainThread();
        clearBiometricRecognized(-10000);
        this.mUserTrustIsUsuallyManaged.put(i, this.mTrustManager.isTrustUsuallyManaged(i));
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onUserSwitching(i);
            }
        }
        try {
            iRemoteCallback.sendResult((Bundle) null);
        } catch (RemoteException unused) {
        }
    }

    public final void notifyLockedOutStateChanged(BiometricSourceType biometricSourceType) {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onLockedOutStateChanged(biometricSourceType);
            }
        }
    }

    @VisibleForTesting
    public void onFingerprintAuthenticated(int i, boolean z) {
        Assert.isMainThread();
        Trace.beginSection("KeyGuardUpdateMonitor#onFingerPrintAuthenticated");
        this.mUserFingerprintAuthenticated.put(i, new BiometricAuthenticated(z));
        if (getUserCanSkipBouncer(i)) {
            this.mTrustManager.unlockedByBiometricForUser(i, BiometricSourceType.FINGERPRINT);
        }
        this.mFingerprintCancelSignal = null;
        updateBiometricListeningState(2);
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricAuthenticated(i, BiometricSourceType.FINGERPRINT, z);
            }
        }
        AnonymousClass14 r1 = this.mHandler;
        r1.sendMessageDelayed(r1.obtainMessage(336), 500L);
        this.mAssistantVisible = false;
        this.mBackgroundExecutor.execute(new AnonymousClass5(z, i));
        Trace.endSection();
    }

    public final void onTrustChanged(boolean z, int i, int i2, List<String> list) {
        Assert.isMainThread();
        boolean z2 = this.mUserHasTrust.get(i, false);
        this.mUserHasTrust.put(i, z);
        if (z2 == z) {
            updateBiometricListeningState(1);
        } else if (!z) {
            updateBiometricListeningState(0);
        }
        for (int i3 = 0; i3 < this.mCallbacks.size(); i3++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i3).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTrustChanged(i);
                if (z && i2 != 0) {
                    keyguardUpdateMonitorCallback.onTrustGrantedWithFlags(i2, i);
                }
            }
        }
        if (getCurrentUser() == i && getUserHasTrust(i)) {
            String str = null;
            if (list != null && list.size() > 0) {
                str = list.get(0);
            }
            for (int i4 = 0; i4 < this.mCallbacks.size(); i4++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback2 = this.mCallbacks.get(i4).get();
                if (keyguardUpdateMonitorCallback2 != null) {
                    keyguardUpdateMonitorCallback2.showTrustGrantedMessage(str);
                }
            }
        }
    }

    public final void onTrustError(CharSequence charSequence) {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTrustAgentErrorMessage(charSequence);
            }
        }
    }

    public final void onTrustManagedChanged(boolean z, int i) {
        Assert.isMainThread();
        this.mUserTrustIsManaged.put(i, z);
        this.mUserTrustIsUsuallyManaged.put(i, this.mTrustManager.isTrustUsuallyManaged(i));
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTrustManagedChanged();
            }
        }
    }

    public final void registerCallback(KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback) {
        Assert.isMainThread();
        if (DEBUG) {
            Log.v("KeyguardUpdateMonitor", "*** register callback for " + keyguardUpdateMonitorCallback);
        }
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            if (this.mCallbacks.get(i).get() == keyguardUpdateMonitorCallback) {
                if (DEBUG) {
                    Log.e("KeyguardUpdateMonitor", "Object tried to add another callback", new Exception("Called by"));
                    return;
                } else {
                    return;
                }
            }
        }
        this.mCallbacks.add(new WeakReference<>(keyguardUpdateMonitorCallback));
        removeCallback(null);
        keyguardUpdateMonitorCallback.onRefreshBatteryInfo(this.mBatteryStatus);
        keyguardUpdateMonitorCallback.onTimeChanged();
        keyguardUpdateMonitorCallback.onPhoneStateChanged();
        keyguardUpdateMonitorCallback.onRefreshCarrierInfo();
        keyguardUpdateMonitorCallback.onClockVisibilityChanged();
        keyguardUpdateMonitorCallback.onKeyguardOccludedChanged(this.mKeyguardOccluded);
        boolean z = this.mKeyguardIsVisible;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (z != keyguardUpdateMonitorCallback.mShowing || elapsedRealtime - keyguardUpdateMonitorCallback.mVisibilityChangedCalled >= 1000) {
            keyguardUpdateMonitorCallback.onKeyguardVisibilityChanged(z);
            keyguardUpdateMonitorCallback.mVisibilityChangedCalled = elapsedRealtime;
            keyguardUpdateMonitorCallback.mShowing = z;
        }
        keyguardUpdateMonitorCallback.onTelephonyCapable(this.mTelephonyCapable);
        for (Map.Entry<Integer, SimData> entry : this.mSimDatas.entrySet()) {
            SimData value = entry.getValue();
            keyguardUpdateMonitorCallback.onSimStateChanged(value.subId, value.slotId, value.simState);
        }
    }

    public final void removeCallback(final KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback) {
        Assert.isMainThread();
        if (DEBUG) {
            Log.v("KeyguardUpdateMonitor", "*** unregister callback for " + keyguardUpdateMonitorCallback);
        }
        this.mCallbacks.removeIf(new Predicate() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                if (((WeakReference) obj).get() == KeyguardUpdateMonitorCallback.this) {
                    return true;
                }
                return false;
            }
        });
    }

    public final boolean resolveNeedsSlowUnlockTransition() {
        if (this.mUserIsUnlocked.get(getCurrentUser())) {
            return false;
        }
        ResolveInfo resolveActivityAsUser = this.mContext.getPackageManager().resolveActivityAsUser(new Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME"), 0, getCurrentUser());
        if (resolveActivityAsUser != null) {
            return FALLBACK_HOME_COMPONENT.equals(resolveActivityAsUser.getComponentInfo().getComponentName());
        }
        Log.w("KeyguardUpdateMonitor", "resolveNeedsSlowUnlockTransition: returning false since activity could not be resolved.");
        return false;
    }

    public final void updateBiometricListeningState(int i) {
        updateFingerprintListeningState(i);
        updateFaceListeningState(i);
    }

    @VisibleForTesting
    public void updateTelephonyCapable(boolean z) {
        Assert.isMainThread();
        if (z != this.mTelephonyCapable) {
            this.mTelephonyCapable = z;
            for (int i = 0; i < this.mCallbacks.size(); i++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onTelephonyCapable(this.mTelephonyCapable);
                }
            }
        }
    }
}
