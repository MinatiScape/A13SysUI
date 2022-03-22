package com.android.keyguard;

import android.view.View;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardInputView;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.settingslib.animation.AppearAnimationUtils;
import com.android.settingslib.animation.DisappearAnimationUtils;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.statusbar.policy.DevicePostureController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyguardPinViewController extends KeyguardPinBasedInputViewController<KeyguardPINView> {
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final KeyguardPinViewController$$ExternalSyntheticLambda1 mPostureCallback = new DevicePostureController.Callback() { // from class: com.android.keyguard.KeyguardPinViewController$$ExternalSyntheticLambda1
        @Override // com.android.systemui.statusbar.policy.DevicePostureController.Callback
        public final void onPostureChanged(int i) {
            KeyguardPinViewController keyguardPinViewController = KeyguardPinViewController.this;
            Objects.requireNonNull(keyguardPinViewController);
            KeyguardPINView keyguardPINView = (KeyguardPINView) keyguardPinViewController.mView;
            Objects.requireNonNull(keyguardPINView);
            keyguardPINView.mLastDevicePosture = i;
            keyguardPINView.updateMargins();
        }
    };
    public final DevicePostureController mPostureController;

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController
    public final void resetState() {
        ((KeyguardPinBasedInputView) this.mView).setPasswordEntryEnabled(true);
        this.mMessageAreaController.setMessage("");
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public final boolean startDisappearAnimation(Runnable runnable) {
        DisappearAnimationUtils disappearAnimationUtils;
        KeyguardPINView keyguardPINView = (KeyguardPINView) this.mView;
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
        Objects.requireNonNull(keyguardUpdateMonitor);
        boolean z = keyguardUpdateMonitor.mNeedsSlowUnlockTransition;
        Objects.requireNonNull(keyguardPINView);
        keyguardPINView.enableClipping(false);
        keyguardPINView.setTranslationY(0.0f);
        float f = keyguardPINView.mDisappearYTranslation;
        DisappearAnimationUtils disappearAnimationUtils2 = keyguardPINView.mDisappearAnimationUtils;
        Objects.requireNonNull(disappearAnimationUtils2);
        AppearAnimationUtils.startTranslationYAnimation(keyguardPINView, 0L, 280L, f, disappearAnimationUtils2.mInterpolator, new KeyguardInputView.AnonymousClass1(22));
        if (z) {
            disappearAnimationUtils = keyguardPINView.mDisappearAnimationUtilsLocked;
        } else {
            disappearAnimationUtils = keyguardPINView.mDisappearAnimationUtils;
        }
        View[][] viewArr = keyguardPINView.mViews;
        KeyguardPINView$$ExternalSyntheticLambda0 keyguardPINView$$ExternalSyntheticLambda0 = new KeyguardPINView$$ExternalSyntheticLambda0(keyguardPINView, runnable, 0);
        Objects.requireNonNull(disappearAnimationUtils);
        disappearAnimationUtils.startAnimation2d(viewArr, keyguardPINView$$ExternalSyntheticLambda0, disappearAnimationUtils);
        return true;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.keyguard.KeyguardPinViewController$$ExternalSyntheticLambda1] */
    public KeyguardPinViewController(KeyguardPINView keyguardPINView, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, LiftToActivateListener liftToActivateListener, EmergencyButtonController emergencyButtonController, FalsingCollector falsingCollector, DevicePostureController devicePostureController) {
        super(keyguardPINView, keyguardUpdateMonitor, securityMode, lockPatternUtils, keyguardSecurityCallback, factory, latencyTracker, liftToActivateListener, emergencyButtonController, falsingCollector);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mPostureController = devicePostureController;
    }

    @Override // com.android.keyguard.KeyguardPinBasedInputViewController, com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public final void onViewAttached() {
        super.onViewAttached();
        View findViewById = ((KeyguardPINView) this.mView).findViewById(2131427661);
        if (findViewById != null) {
            findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.android.keyguard.KeyguardPinViewController$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    KeyguardPinViewController keyguardPinViewController = KeyguardPinViewController.this;
                    Objects.requireNonNull(keyguardPinViewController);
                    keyguardPinViewController.getKeyguardSecurityCallback().reset();
                    keyguardPinViewController.getKeyguardSecurityCallback().onCancelClicked();
                }
            });
        }
        this.mPostureController.addCallback(this.mPostureCallback);
    }

    @Override // com.android.keyguard.KeyguardPinBasedInputViewController, com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public final void onViewDetached() {
        super.onViewDetached();
        this.mPostureController.removeCallback(this.mPostureCallback);
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardInputViewController
    public final void reloadColors() {
        super.reloadColors();
        ((KeyguardPINView) this.mView).reloadColors();
    }
}
