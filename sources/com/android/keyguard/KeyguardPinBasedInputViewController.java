package com.android.keyguard;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardAbsKeyInputViewController;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardPinBasedInputView;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda5;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda0;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class KeyguardPinBasedInputViewController<T extends KeyguardPinBasedInputView> extends KeyguardAbsKeyInputViewController<T> {
    public final FalsingCollector mFalsingCollector;
    public final LiftToActivateListener mLiftToActivateListener;
    public PasswordTextView mPasswordEntry;
    public final KeyguardPinBasedInputViewController$$ExternalSyntheticLambda0 mOnKeyListener = new View.OnKeyListener() { // from class: com.android.keyguard.KeyguardPinBasedInputViewController$$ExternalSyntheticLambda0
        @Override // android.view.View.OnKeyListener
        public final boolean onKey(View view, int i, KeyEvent keyEvent) {
            KeyguardPinBasedInputViewController keyguardPinBasedInputViewController = KeyguardPinBasedInputViewController.this;
            Objects.requireNonNull(keyguardPinBasedInputViewController);
            if (keyEvent.getAction() == 0) {
                return ((KeyguardPinBasedInputView) keyguardPinBasedInputViewController.mView).onKeyDown(i, keyEvent);
            }
            return false;
        }
    };
    public final KeyguardPinBasedInputViewController$$ExternalSyntheticLambda3 mActionButtonTouchListener = new View.OnTouchListener() { // from class: com.android.keyguard.KeyguardPinBasedInputViewController$$ExternalSyntheticLambda3
        @Override // android.view.View.OnTouchListener
        public final boolean onTouch(View view, MotionEvent motionEvent) {
            KeyguardPinBasedInputViewController keyguardPinBasedInputViewController = KeyguardPinBasedInputViewController.this;
            Objects.requireNonNull(keyguardPinBasedInputViewController);
            if (motionEvent.getActionMasked() != 0) {
                return false;
            }
            KeyguardPinBasedInputView keyguardPinBasedInputView = (KeyguardPinBasedInputView) keyguardPinBasedInputViewController.mView;
            Objects.requireNonNull(keyguardPinBasedInputView);
            keyguardPinBasedInputView.performHapticFeedback(1, 3);
            return false;
        }
    };

    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.keyguard.KeyguardPinBasedInputViewController$$ExternalSyntheticLambda0] */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.keyguard.KeyguardPinBasedInputViewController$$ExternalSyntheticLambda3] */
    public KeyguardPinBasedInputViewController(T t, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, LiftToActivateListener liftToActivateListener, EmergencyButtonController emergencyButtonController, FalsingCollector falsingCollector) {
        super(t, keyguardUpdateMonitor, securityMode, lockPatternUtils, keyguardSecurityCallback, factory, latencyTracker, falsingCollector, emergencyButtonController);
        this.mLiftToActivateListener = liftToActivateListener;
        this.mFalsingCollector = falsingCollector;
        this.mPasswordEntry = (PasswordTextView) t.findViewById(t.getPasswordTextViewId());
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public void onResume(int i) {
        this.mResumed = true;
        this.mPasswordEntry.requestFocus();
    }

    @Override // com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public void onViewAttached() {
        KeyguardAbsKeyInputView keyguardAbsKeyInputView = (KeyguardAbsKeyInputView) this.mView;
        KeyguardAbsKeyInputViewController$$ExternalSyntheticLambda0 keyguardAbsKeyInputViewController$$ExternalSyntheticLambda0 = this.mKeyDownListener;
        Objects.requireNonNull(keyguardAbsKeyInputView);
        keyguardAbsKeyInputView.mKeyDownListener = keyguardAbsKeyInputViewController$$ExternalSyntheticLambda0;
        EmergencyButtonController emergencyButtonController = ((KeyguardAbsKeyInputViewController) this).mEmergencyButtonController;
        KeyguardAbsKeyInputViewController.AnonymousClass1 r1 = this.mEmergencyButtonCallback;
        Objects.requireNonNull(emergencyButtonController);
        emergencyButtonController.mEmergencyButtonCallback = r1;
        KeyguardPinBasedInputView keyguardPinBasedInputView = (KeyguardPinBasedInputView) this.mView;
        Objects.requireNonNull(keyguardPinBasedInputView);
        for (NumPadKey numPadKey : keyguardPinBasedInputView.mButtons) {
            numPadKey.setOnTouchListener(new View.OnTouchListener() { // from class: com.android.keyguard.KeyguardPinBasedInputViewController$$ExternalSyntheticLambda2
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    KeyguardPinBasedInputViewController keyguardPinBasedInputViewController = KeyguardPinBasedInputViewController.this;
                    Objects.requireNonNull(keyguardPinBasedInputViewController);
                    if (motionEvent.getActionMasked() != 0) {
                        return false;
                    }
                    keyguardPinBasedInputViewController.mFalsingCollector.avoidGesture();
                    return false;
                }
            });
        }
        this.mPasswordEntry.setOnKeyListener(this.mOnKeyListener);
        PasswordTextView passwordTextView = this.mPasswordEntry;
        DozeTriggers$$ExternalSyntheticLambda0 dozeTriggers$$ExternalSyntheticLambda0 = new DozeTriggers$$ExternalSyntheticLambda0(this);
        Objects.requireNonNull(passwordTextView);
        passwordTextView.mUserActivityListener = dozeTriggers$$ExternalSyntheticLambda0;
        View findViewById = ((KeyguardPinBasedInputView) this.mView).findViewById(2131427813);
        findViewById.setOnTouchListener(this.mActionButtonTouchListener);
        findViewById.setOnClickListener(new AuthBiometricView$$ExternalSyntheticLambda5(this, 1));
        findViewById.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.keyguard.KeyguardPinBasedInputViewController$$ExternalSyntheticLambda1
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                KeyguardPinBasedInputViewController keyguardPinBasedInputViewController = KeyguardPinBasedInputViewController.this;
                Objects.requireNonNull(keyguardPinBasedInputViewController);
                if (keyguardPinBasedInputViewController.mPasswordEntry.isEnabled()) {
                    ((KeyguardPinBasedInputView) keyguardPinBasedInputViewController.mView).resetPasswordText(true, true);
                }
                KeyguardPinBasedInputView keyguardPinBasedInputView2 = (KeyguardPinBasedInputView) keyguardPinBasedInputViewController.mView;
                Objects.requireNonNull(keyguardPinBasedInputView2);
                keyguardPinBasedInputView2.performHapticFeedback(1, 3);
                return true;
            }
        });
        View findViewById2 = ((KeyguardPinBasedInputView) this.mView).findViewById(2131428158);
        if (findViewById2 != null) {
            findViewById2.setOnTouchListener(this.mActionButtonTouchListener);
            findViewById2.setOnClickListener(new View.OnClickListener() { // from class: com.android.keyguard.KeyguardPinBasedInputViewController.1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    if (KeyguardPinBasedInputViewController.this.mPasswordEntry.isEnabled()) {
                        KeyguardPinBasedInputViewController.this.verifyPasswordAndUnlock();
                    }
                }
            });
            findViewById2.setOnHoverListener(this.mLiftToActivateListener);
        }
    }

    @Override // com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public void onViewDetached() {
        KeyguardPinBasedInputView keyguardPinBasedInputView = (KeyguardPinBasedInputView) this.mView;
        Objects.requireNonNull(keyguardPinBasedInputView);
        for (NumPadKey numPadKey : keyguardPinBasedInputView.mButtons) {
            numPadKey.setOnTouchListener(null);
        }
    }
}
