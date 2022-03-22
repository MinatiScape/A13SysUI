package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Build;
import android.os.SystemProperties;
import android.os.Trace;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda10;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline0;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.classifier.FalsingDataProvider$$ExternalSyntheticLambda1;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.tuner.TunerActivity$$ExternalSyntheticLambda0;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyguardStateControllerImpl implements KeyguardStateController, Dumpable {
    public boolean mBypassFadingAnimation;
    public boolean mCanDismissLockScreen;
    public final Context mContext;
    public boolean mFaceAuthEnabled;
    public boolean mKeyguardFadingAway;
    public long mKeyguardFadingAwayDelay;
    public long mKeyguardFadingAwayDuration;
    public boolean mKeyguardGoingAway;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final UpdateMonitorCallback mKeyguardUpdateMonitorCallback;
    public boolean mLaunchTransitionFadingAway;
    public final LockPatternUtils mLockPatternUtils;
    public boolean mOccluded;
    public boolean mSecure;
    public boolean mShowing;
    public boolean mTrustManaged;
    public boolean mTrusted;
    public final Lazy<KeyguardUnlockAnimationController> mUnlockAnimationControllerLazy;
    public final ArrayList<KeyguardStateController.Callback> mCallbacks = new ArrayList<>();
    public float mDismissAmount = 0.0f;
    public boolean mDismissingFromTouch = false;
    public boolean mFlingingToDismissKeyguard = false;
    public boolean mFlingingToDismissKeyguardDuringSwipeGesture = false;
    public boolean mSnappingKeyguardBackAfterSwipe = false;

    /* loaded from: classes.dex */
    public class UpdateMonitorCallback extends KeyguardUpdateMonitorCallback {
        public UpdateMonitorCallback() {
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
            Trace.beginSection("KeyguardUpdateMonitorCallback#onBiometricAuthenticated");
            if (KeyguardStateControllerImpl.this.mKeyguardUpdateMonitor.isUnlockingWithBiometricAllowed(z)) {
                KeyguardStateControllerImpl.this.update(false);
            }
            Trace.endSection();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onBiometricsCleared() {
            KeyguardStateControllerImpl.this.update(false);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onFaceUnlockStateChanged() {
            KeyguardStateControllerImpl.this.update(false);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onKeyguardVisibilityChanged(boolean z) {
            KeyguardStateControllerImpl.this.update(false);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onStartedWakingUp() {
            KeyguardStateControllerImpl.this.update(false);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onStrongAuthStateChanged(int i) {
            KeyguardStateControllerImpl.this.update(false);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onTrustChanged(int i) {
            KeyguardStateControllerImpl.this.update(false);
            KeyguardStateControllerImpl keyguardStateControllerImpl = KeyguardStateControllerImpl.this;
            Objects.requireNonNull(keyguardStateControllerImpl);
            Trace.beginSection("KeyguardStateController#notifyKeyguardChanged");
            new ArrayList(keyguardStateControllerImpl.mCallbacks).forEach(KeyguardStateControllerImpl$$ExternalSyntheticLambda0.INSTANCE);
            Trace.endSection();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onTrustManagedChanged() {
            KeyguardStateControllerImpl.this.update(false);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onUserSwitchComplete(int i) {
            KeyguardStateControllerImpl.this.update(false);
        }
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final void notifyKeyguardDoneFading() {
        this.mKeyguardGoingAway = false;
        setKeyguardFadingAway(false);
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final void notifyKeyguardGoingAway() {
        this.mKeyguardGoingAway = true;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final void notifyPanelFlingEnd() {
        this.mFlingingToDismissKeyguard = false;
        this.mFlingingToDismissKeyguardDuringSwipeGesture = false;
        this.mSnappingKeyguardBackAfterSwipe = false;
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(KeyguardStateController.Callback callback) {
        KeyguardStateController.Callback callback2 = callback;
        Objects.requireNonNull(callback2, "Callback must not be null. b/128895449");
        if (!this.mCallbacks.contains(callback2)) {
            this.mCallbacks.add(callback2);
        }
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final long calculateGoingToFullShadeDelay() {
        return this.mKeyguardFadingAwayDelay + this.mKeyguardFadingAwayDuration;
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(LockIconView$$ExternalSyntheticOutline0.m(printWriter, "KeyguardStateController:", "  mSecure: "), this.mSecure, printWriter, "  mCanDismissLockScreen: "), this.mCanDismissLockScreen, printWriter, "  mTrustManaged: "), this.mTrustManaged, printWriter, "  mTrusted: ");
        m.append(this.mTrusted);
        printWriter.println(m.toString());
        printWriter.println("  mDebugUnlocked: false");
        printWriter.println("  mFaceAuthEnabled: " + this.mFaceAuthEnabled);
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final boolean isKeyguardScreenRotationAllowed() {
        if (SystemProperties.getBoolean("lockscreen.rot_override", false) || this.mContext.getResources().getBoolean(2131034129)) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final void notifyKeyguardDismissAmountChanged(float f, boolean z) {
        this.mDismissAmount = f;
        this.mDismissingFromTouch = z;
        new ArrayList(this.mCallbacks).forEach(TunerActivity$$ExternalSyntheticLambda0.INSTANCE$1);
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final void notifyKeyguardFadingAway(long j, long j2, boolean z) {
        this.mKeyguardFadingAwayDelay = j;
        this.mKeyguardFadingAwayDuration = j2;
        this.mBypassFadingAnimation = z;
        setKeyguardFadingAway(true);
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final void notifyKeyguardState(boolean z, boolean z2) {
        float f;
        if (this.mShowing != z || this.mOccluded != z2) {
            this.mShowing = z;
            this.mOccluded = z2;
            Trace.instantForTrack(4096L, "UI Events", "Keyguard showing: " + z + " occluded: " + z2);
            Trace.beginSection("KeyguardStateController#notifyKeyguardChanged");
            new ArrayList(this.mCallbacks).forEach(KeyguardStateControllerImpl$$ExternalSyntheticLambda0.INSTANCE);
            Trace.endSection();
            if (z) {
                f = 0.0f;
            } else {
                f = 1.0f;
            }
            notifyKeyguardDismissAmountChanged(f, false);
        }
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final void notifyPanelFlingStart(boolean z) {
        boolean z2;
        this.mFlingingToDismissKeyguard = z;
        if (!z || !this.mDismissingFromTouch) {
            z2 = false;
        } else {
            z2 = true;
        }
        this.mFlingingToDismissKeyguardDuringSwipeGesture = z2;
        this.mSnappingKeyguardBackAfterSwipe = !z;
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(KeyguardStateController.Callback callback) {
        KeyguardStateController.Callback callback2 = callback;
        Objects.requireNonNull(callback2, "Callback must not be null. b/128895449");
        this.mCallbacks.remove(callback2);
    }

    public final void setKeyguardFadingAway(boolean z) {
        if (this.mKeyguardFadingAway != z) {
            this.mKeyguardFadingAway = z;
            ArrayList arrayList = new ArrayList(this.mCallbacks);
            for (int i = 0; i < arrayList.size(); i++) {
                ((KeyguardStateController.Callback) arrayList.get(i)).onKeyguardFadingAwayChanged();
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final void setLaunchTransitionFadingAway(boolean z) {
        this.mLaunchTransitionFadingAway = z;
        new ArrayList(this.mCallbacks).forEach(FalsingDataProvider$$ExternalSyntheticLambda1.INSTANCE$1);
    }

    public void update(boolean z) {
        boolean z2;
        Trace.beginSection("KeyguardStateController#update");
        int currentUser = KeyguardUpdateMonitor.getCurrentUser();
        boolean isSecure = this.mLockPatternUtils.isSecure(currentUser);
        boolean z3 = false;
        if (!isSecure || this.mKeyguardUpdateMonitor.getUserCanSkipBouncer(currentUser)) {
            z2 = true;
        } else {
            boolean z4 = Build.IS_DEBUGGABLE;
            z2 = false;
        }
        boolean userTrustIsManaged = this.mKeyguardUpdateMonitor.getUserTrustIsManaged(currentUser);
        boolean userHasTrust = this.mKeyguardUpdateMonitor.getUserHasTrust(currentUser);
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
        Objects.requireNonNull(keyguardUpdateMonitor);
        boolean booleanValue = ((Boolean) DejankUtils.whitelistIpcs(new KeyguardUpdateMonitor$$ExternalSyntheticLambda10(keyguardUpdateMonitor, currentUser))).booleanValue();
        keyguardUpdateMonitor.mIsFaceEnrolled = booleanValue;
        if (!(isSecure == this.mSecure && z2 == this.mCanDismissLockScreen && userTrustIsManaged == this.mTrustManaged && this.mTrusted == userHasTrust && this.mFaceAuthEnabled == booleanValue)) {
            z3 = true;
        }
        if (z3 || z) {
            this.mSecure = isSecure;
            this.mCanDismissLockScreen = z2;
            this.mTrusted = userHasTrust;
            this.mTrustManaged = userTrustIsManaged;
            this.mFaceAuthEnabled = booleanValue;
            Trace.beginSection("KeyguardStateController#notifyUnlockedChanged");
            new ArrayList(this.mCallbacks).forEach(KeyguardStateControllerImpl$$ExternalSyntheticLambda1.INSTANCE);
            Trace.endSection();
        }
        Trace.endSection();
    }

    public KeyguardStateControllerImpl(Context context, KeyguardUpdateMonitor keyguardUpdateMonitor, LockPatternUtils lockPatternUtils, Lazy<KeyguardUnlockAnimationController> lazy, DumpManager dumpManager) {
        UpdateMonitorCallback updateMonitorCallback = new UpdateMonitorCallback();
        this.mKeyguardUpdateMonitorCallback = updateMonitorCallback;
        this.mContext = context;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mLockPatternUtils = lockPatternUtils;
        keyguardUpdateMonitor.registerCallback(updateMonitorCallback);
        this.mUnlockAnimationControllerLazy = lazy;
        dumpManager.registerDumpable("KeyguardStateControllerImpl", this);
        update(true);
        boolean z = Build.IS_DEBUGGABLE;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final boolean canDismissLockScreen() {
        return this.mCanDismissLockScreen;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final float getDismissAmount() {
        return this.mDismissAmount;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final long getKeyguardFadingAwayDelay() {
        return this.mKeyguardFadingAwayDelay;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final long getKeyguardFadingAwayDuration() {
        return this.mKeyguardFadingAwayDuration;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final boolean isBypassFadingAnimation() {
        return this.mBypassFadingAnimation;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final boolean isDismissingFromSwipe() {
        return this.mDismissingFromTouch;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final boolean isFaceAuthEnabled() {
        return this.mFaceAuthEnabled;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final boolean isFlingingToDismissKeyguard() {
        return this.mFlingingToDismissKeyguard;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final boolean isFlingingToDismissKeyguardDuringSwipeGesture() {
        return this.mFlingingToDismissKeyguardDuringSwipeGesture;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final boolean isKeyguardFadingAway() {
        return this.mKeyguardFadingAway;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final boolean isKeyguardGoingAway() {
        return this.mKeyguardGoingAway;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final boolean isLaunchTransitionFadingAway() {
        return this.mLaunchTransitionFadingAway;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final boolean isMethodSecure() {
        return this.mSecure;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final boolean isOccluded() {
        return this.mOccluded;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final boolean isShowing() {
        return this.mShowing;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController
    public final boolean isSnappingKeyguardBackAfterSwipe() {
        return this.mSnappingKeyguardBackAfterSwipe;
    }
}
