package com.android.keyguard;

import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.PluralsMessageFormatter;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternChecker;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockscreenCredential;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardAbsKeyInputView;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.settingslib.Utils;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.classifier.FalsingCollector;
import java.util.HashMap;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class KeyguardAbsKeyInputViewController<T extends KeyguardAbsKeyInputView> extends KeyguardInputViewController<T> {
    public CountDownTimer mCountdownTimer;
    public boolean mDismissing;
    public final EmergencyButtonController mEmergencyButtonController;
    public final FalsingCollector mFalsingCollector;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final LatencyTracker mLatencyTracker;
    public final LockPatternUtils mLockPatternUtils;
    public KeyguardMessageAreaController mMessageAreaController;
    public AsyncTask<?, ?, ?> mPendingLockCheck;
    public boolean mResumed;
    public final KeyguardAbsKeyInputViewController$$ExternalSyntheticLambda0 mKeyDownListener = new KeyguardAbsKeyInputViewController$$ExternalSyntheticLambda0(this);
    public final AnonymousClass1 mEmergencyButtonCallback = new EmergencyButtonController.EmergencyButtonCallback() { // from class: com.android.keyguard.KeyguardAbsKeyInputViewController.1
        @Override // com.android.keyguard.EmergencyButtonController.EmergencyButtonCallback
        public final void onEmergencyButtonClickedWhenInCall() {
            KeyguardAbsKeyInputViewController.this.getKeyguardSecurityCallback().reset();
        }
    };

    @Override // com.android.keyguard.KeyguardSecurityView
    public boolean needsInput() {
        return this instanceof KeyguardPasswordViewController;
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public void onPause() {
        this.mResumed = false;
        CountDownTimer countDownTimer = this.mCountdownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            this.mCountdownTimer = null;
        }
        AsyncTask<?, ?, ?> asyncTask = this.mPendingLockCheck;
        if (asyncTask != null) {
            asyncTask.cancel(false);
            this.mPendingLockCheck = null;
        }
        reset();
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public final void reset() {
        this.mDismissing = false;
        ((KeyguardAbsKeyInputView) this.mView).resetPasswordText(false, false);
        long lockoutAttemptDeadline = this.mLockPatternUtils.getLockoutAttemptDeadline(KeyguardUpdateMonitor.getCurrentUser());
        if (shouldLockout(lockoutAttemptDeadline)) {
            handleAttemptLockout(lockoutAttemptDeadline);
        } else {
            resetState();
        }
    }

    public abstract void resetState();

    public boolean shouldLockout(long j) {
        return j != 0;
    }

    public final void handleAttemptLockout(long j) {
        ((KeyguardAbsKeyInputView) this.mView).setPasswordEntryEnabled(false);
        this.mCountdownTimer = new CountDownTimer(((long) Math.ceil((j - SystemClock.elapsedRealtime()) / 1000.0d)) * 1000) { // from class: com.android.keyguard.KeyguardAbsKeyInputViewController.2
            @Override // android.os.CountDownTimer
            public final void onTick(long j2) {
                HashMap hashMap = new HashMap();
                hashMap.put("count", Integer.valueOf((int) Math.round(j2 / 1000.0d)));
                KeyguardAbsKeyInputViewController keyguardAbsKeyInputViewController = KeyguardAbsKeyInputViewController.this;
                keyguardAbsKeyInputViewController.mMessageAreaController.setMessage(PluralsMessageFormatter.format(((KeyguardAbsKeyInputView) keyguardAbsKeyInputViewController.mView).getResources(), hashMap, 2131952601));
            }

            @Override // android.os.CountDownTimer
            public final void onFinish() {
                KeyguardAbsKeyInputViewController.this.mMessageAreaController.setMessage("");
                KeyguardAbsKeyInputViewController.this.resetState();
            }
        }.start();
    }

    public final void onUserInput() {
        this.mFalsingCollector.updateFalseConfidence(FalsingClassifier.Result.passed(0.6d));
        getKeyguardSecurityCallback().userActivity();
        getKeyguardSecurityCallback().onUserInput();
        this.mMessageAreaController.setMessage("");
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public final void showMessage(CharSequence charSequence, ColorStateList colorStateList) {
        if (colorStateList != null) {
            KeyguardMessageAreaController keyguardMessageAreaController = this.mMessageAreaController;
            Objects.requireNonNull(keyguardMessageAreaController);
            KeyguardMessageArea keyguardMessageArea = (KeyguardMessageArea) keyguardMessageAreaController.mView;
            Objects.requireNonNull(keyguardMessageArea);
            keyguardMessageArea.mNextMessageColorState = colorStateList;
        }
        this.mMessageAreaController.setMessage(charSequence);
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public final void showPromptReason(int i) {
        int promptReasonStringRes;
        if (i != 0 && (promptReasonStringRes = ((KeyguardAbsKeyInputView) this.mView).getPromptReasonStringRes(i)) != 0) {
            this.mMessageAreaController.setMessage(promptReasonStringRes);
        }
    }

    public void verifyPasswordAndUnlock() {
        if (!this.mDismissing) {
            final LockscreenCredential enteredCredential = ((KeyguardAbsKeyInputView) this.mView).getEnteredCredential();
            ((KeyguardAbsKeyInputView) this.mView).setPasswordEntryInputEnabled(false);
            AsyncTask<?, ?, ?> asyncTask = this.mPendingLockCheck;
            if (asyncTask != null) {
                asyncTask.cancel(false);
            }
            final int currentUser = KeyguardUpdateMonitor.getCurrentUser();
            if (enteredCredential.size() <= 3) {
                ((KeyguardAbsKeyInputView) this.mView).setPasswordEntryInputEnabled(true);
                onPasswordChecked(currentUser, false, 0, false);
                enteredCredential.zeroize();
                return;
            }
            this.mLatencyTracker.onActionStart(3);
            this.mLatencyTracker.onActionStart(4);
            KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            keyguardUpdateMonitor.mCredentialAttempted = true;
            keyguardUpdateMonitor.updateFingerprintListeningState(2);
            this.mPendingLockCheck = LockPatternChecker.checkCredential(this.mLockPatternUtils, enteredCredential, currentUser, new LockPatternChecker.OnCheckCallback() { // from class: com.android.keyguard.KeyguardAbsKeyInputViewController.3
                public final void onCancelled() {
                    KeyguardAbsKeyInputViewController.this.mLatencyTracker.onActionEnd(4);
                    enteredCredential.zeroize();
                }

                public final void onChecked(boolean z, int i) {
                    KeyguardAbsKeyInputViewController.this.mLatencyTracker.onActionEnd(4);
                    ((KeyguardAbsKeyInputView) KeyguardAbsKeyInputViewController.this.mView).setPasswordEntryInputEnabled(true);
                    KeyguardAbsKeyInputViewController keyguardAbsKeyInputViewController = KeyguardAbsKeyInputViewController.this;
                    keyguardAbsKeyInputViewController.mPendingLockCheck = null;
                    if (!z) {
                        keyguardAbsKeyInputViewController.onPasswordChecked(currentUser, false, i, true);
                    }
                    enteredCredential.zeroize();
                }

                public final void onEarlyMatched() {
                    KeyguardAbsKeyInputViewController.this.mLatencyTracker.onActionEnd(3);
                    KeyguardAbsKeyInputViewController.this.onPasswordChecked(currentUser, true, 0, true);
                    enteredCredential.zeroize();
                }
            });
        }
    }

    /* JADX WARN: Type inference failed for: r3v2, types: [com.android.keyguard.KeyguardAbsKeyInputViewController$1] */
    public KeyguardAbsKeyInputViewController(T t, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, FalsingCollector falsingCollector, EmergencyButtonController emergencyButtonController) {
        super(t, securityMode, keyguardSecurityCallback, emergencyButtonController);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mLockPatternUtils = lockPatternUtils;
        this.mLatencyTracker = latencyTracker;
        this.mFalsingCollector = falsingCollector;
        this.mEmergencyButtonController = emergencyButtonController;
        KeyguardMessageArea findSecurityMessageDisplay = KeyguardMessageArea.findSecurityMessageDisplay(t);
        Objects.requireNonNull(factory);
        this.mMessageAreaController = new KeyguardMessageAreaController(findSecurityMessageDisplay, factory.mKeyguardUpdateMonitor, factory.mConfigurationController);
    }

    @Override // com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public final void onInit() {
        super.onInit();
        this.mMessageAreaController.init();
    }

    public final void onPasswordChecked(int i, boolean z, int i2, boolean z2) {
        boolean z3;
        if (KeyguardUpdateMonitor.getCurrentUser() == i) {
            z3 = true;
        } else {
            z3 = false;
        }
        if (z) {
            getKeyguardSecurityCallback().reportUnlockAttempt(i, true, 0);
            if (z3) {
                this.mDismissing = true;
                this.mLatencyTracker.onActionStart(11);
                getKeyguardSecurityCallback().dismiss(i);
                return;
            }
            return;
        }
        if (z2) {
            getKeyguardSecurityCallback().reportUnlockAttempt(i, false, i2);
            if (i2 > 0) {
                handleAttemptLockout(this.mLockPatternUtils.setLockoutAttemptDeadline(i, i2));
            }
        }
        if (i2 == 0) {
            this.mMessageAreaController.setMessage(((KeyguardAbsKeyInputView) this.mView).getWrongPasswordStringId());
        }
        ((KeyguardAbsKeyInputView) this.mView).resetPasswordText(true, false);
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public void reloadColors() {
        super.reloadColors();
        KeyguardMessageAreaController keyguardMessageAreaController = this.mMessageAreaController;
        Objects.requireNonNull(keyguardMessageAreaController);
        KeyguardMessageArea keyguardMessageArea = (KeyguardMessageArea) keyguardMessageAreaController.mView;
        Objects.requireNonNull(keyguardMessageArea);
        keyguardMessageArea.mDefaultColorState = Utils.getColorAttr(keyguardMessageArea.getContext(), 16842806);
        keyguardMessageArea.update();
    }
}
