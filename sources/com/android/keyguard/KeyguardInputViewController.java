package com.android.keyguard;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardInputView;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.settingslib.Utils;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class KeyguardInputViewController<T extends KeyguardInputView> extends ViewController<T> implements KeyguardSecurityView {
    public final EmergencyButton mEmergencyButton;
    public final EmergencyButtonController mEmergencyButtonController;
    public final KeyguardSecurityCallback mKeyguardSecurityCallback;
    public AnonymousClass1 mNullCallback = new KeyguardSecurityCallback() { // from class: com.android.keyguard.KeyguardInputViewController.1
        @Override // com.android.keyguard.KeyguardSecurityCallback
        public final void dismiss(int i) {
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public final void dismiss(int i, boolean z) {
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public final void onUserInput() {
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public final void reportUnlockAttempt(int i, boolean z, int i2) {
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public final void reset() {
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public final void userActivity() {
        }
    };
    public boolean mPaused;
    public final KeyguardSecurityModel.SecurityMode mSecurityMode;

    public void onPause() {
        this.mPaused = true;
    }

    public void onResume(int i) {
        this.mPaused = false;
    }

    @Override // com.android.systemui.util.ViewController
    public void onViewAttached() {
    }

    @Override // com.android.systemui.util.ViewController
    public void onViewDetached() {
    }

    public void reset() {
    }

    public void showMessage(CharSequence charSequence, ColorStateList colorStateList) {
    }

    public void showPromptReason(int i) {
    }

    /* loaded from: classes.dex */
    public static class Factory {
        public final DevicePostureController mDevicePostureController;
        public final EmergencyButtonController.Factory mEmergencyButtonControllerFactory;
        public final FalsingCollector mFalsingCollector;
        public final InputMethodManager mInputMethodManager;
        public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        public final LatencyTracker mLatencyTracker;
        public final LiftToActivateListener mLiftToActivateListener;
        public final LockPatternUtils mLockPatternUtils;
        public final DelayableExecutor mMainExecutor;
        public final KeyguardMessageAreaController.Factory mMessageAreaControllerFactory;
        public final Resources mResources;
        public final TelephonyManager mTelephonyManager;

        public Factory(KeyguardUpdateMonitor keyguardUpdateMonitor, LockPatternUtils lockPatternUtils, LatencyTracker latencyTracker, KeyguardMessageAreaController.Factory factory, InputMethodManager inputMethodManager, DelayableExecutor delayableExecutor, Resources resources, LiftToActivateListener liftToActivateListener, TelephonyManager telephonyManager, FalsingCollector falsingCollector, EmergencyButtonController.Factory factory2, DevicePostureController devicePostureController) {
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mLockPatternUtils = lockPatternUtils;
            this.mLatencyTracker = latencyTracker;
            this.mMessageAreaControllerFactory = factory;
            this.mInputMethodManager = inputMethodManager;
            this.mMainExecutor = delayableExecutor;
            this.mResources = resources;
            this.mLiftToActivateListener = liftToActivateListener;
            this.mTelephonyManager = telephonyManager;
            this.mEmergencyButtonControllerFactory = factory2;
            this.mFalsingCollector = falsingCollector;
            this.mDevicePostureController = devicePostureController;
        }
    }

    public final KeyguardSecurityCallback getKeyguardSecurityCallback() {
        if (this.mPaused) {
            return this.mNullCallback;
        }
        return this.mKeyguardSecurityCallback;
    }

    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        this.mEmergencyButtonController.init();
    }

    public void reloadColors() {
        EmergencyButton emergencyButton = this.mEmergencyButton;
        if (emergencyButton != null) {
            Objects.requireNonNull(emergencyButton);
            emergencyButton.setTextColor(Utils.getColorAttrDefaultColor(emergencyButton.getContext(), 16842809));
            emergencyButton.setBackground(emergencyButton.getContext().getDrawable(2131232350));
        }
    }

    public void startAppearAnimation() {
        ((KeyguardInputView) this.mView).startAppearAnimation();
    }

    public boolean startDisappearAnimation(Runnable runnable) {
        return ((KeyguardInputView) this.mView).startDisappearAnimation((QSTileImpl$$ExternalSyntheticLambda0) runnable);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.keyguard.KeyguardInputViewController$1] */
    public KeyguardInputViewController(T t, KeyguardSecurityModel.SecurityMode securityMode, KeyguardSecurityCallback keyguardSecurityCallback, EmergencyButtonController emergencyButtonController) {
        super(t);
        EmergencyButton emergencyButton;
        this.mSecurityMode = securityMode;
        this.mKeyguardSecurityCallback = keyguardSecurityCallback;
        if (t == null) {
            emergencyButton = null;
        } else {
            emergencyButton = (EmergencyButton) t.findViewById(2131427906);
        }
        this.mEmergencyButton = emergencyButton;
        this.mEmergencyButtonController = emergencyButtonController;
    }
}
