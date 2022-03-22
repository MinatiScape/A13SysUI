package com.android.keyguard;

import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.PluralsMessageFormatter;
import android.view.MotionEvent;
import android.view.View;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternChecker;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockPatternView;
import com.android.internal.widget.LockscreenCredential;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardInputView;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.settingslib.Utils;
import com.android.settingslib.animation.AppearAnimationUtils;
import com.android.settingslib.animation.DisappearAnimationUtils;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.statusbar.policy.DevicePostureController;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyguardPatternViewController extends KeyguardInputViewController<KeyguardPatternView> {
    public CountDownTimer mCountdownTimer;
    public final EmergencyButtonController mEmergencyButtonController;
    public final FalsingCollector mFalsingCollector;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final LatencyTracker mLatencyTracker;
    public final LockPatternUtils mLockPatternUtils;
    public LockPatternView mLockPatternView;
    public KeyguardMessageAreaController mMessageAreaController;
    public AsyncTask<?, ?, ?> mPendingLockCheck;
    public final DevicePostureController mPostureController;
    public final KeyguardPatternViewController$$ExternalSyntheticLambda2 mPostureCallback = new DevicePostureController.Callback() { // from class: com.android.keyguard.KeyguardPatternViewController$$ExternalSyntheticLambda2
        @Override // com.android.systemui.statusbar.policy.DevicePostureController.Callback
        public final void onPostureChanged(int i) {
            KeyguardPatternViewController keyguardPatternViewController = KeyguardPatternViewController.this;
            Objects.requireNonNull(keyguardPatternViewController);
            ((KeyguardPatternView) keyguardPatternViewController.mView).onDevicePostureChanged(i);
        }
    };
    public AnonymousClass1 mEmergencyButtonCallback = new EmergencyButtonController.EmergencyButtonCallback() { // from class: com.android.keyguard.KeyguardPatternViewController.1
        @Override // com.android.keyguard.EmergencyButtonController.EmergencyButtonCallback
        public final void onEmergencyButtonClickedWhenInCall() {
            KeyguardPatternViewController.this.getKeyguardSecurityCallback().reset();
        }
    };
    public AnonymousClass2 mCancelPatternRunnable = new Runnable() { // from class: com.android.keyguard.KeyguardPatternViewController.2
        @Override // java.lang.Runnable
        public final void run() {
            KeyguardPatternViewController.this.mLockPatternView.clearPattern();
        }
    };

    /* loaded from: classes.dex */
    public class UnlockPatternListener implements LockPatternView.OnPatternListener {
        public final void onPatternCleared() {
        }

        public UnlockPatternListener() {
        }

        public final void onPatternCellAdded(List<LockPatternView.Cell> list) {
            KeyguardPatternViewController.this.getKeyguardSecurityCallback().userActivity();
            KeyguardPatternViewController.this.getKeyguardSecurityCallback().onUserInput();
        }

        public final void onPatternDetected(List<LockPatternView.Cell> list) {
            KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardPatternViewController.this.mKeyguardUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            keyguardUpdateMonitor.mCredentialAttempted = true;
            keyguardUpdateMonitor.updateFingerprintListeningState(2);
            KeyguardPatternViewController.this.mLockPatternView.disableInput();
            AsyncTask<?, ?, ?> asyncTask = KeyguardPatternViewController.this.mPendingLockCheck;
            if (asyncTask != null) {
                asyncTask.cancel(false);
            }
            final int currentUser = KeyguardUpdateMonitor.getCurrentUser();
            if (list.size() < 4) {
                if (list.size() == 1) {
                    KeyguardPatternViewController.this.mFalsingCollector.updateFalseConfidence(new FalsingClassifier.Result(true, 0.7d, UnlockPatternListener.class.getSimpleName(), "empty pattern input"));
                }
                KeyguardPatternViewController.this.mLockPatternView.enableInput();
                onPatternChecked(currentUser, false, 0, false);
                return;
            }
            KeyguardPatternViewController.this.mLatencyTracker.onActionStart(3);
            KeyguardPatternViewController.this.mLatencyTracker.onActionStart(4);
            KeyguardPatternViewController keyguardPatternViewController = KeyguardPatternViewController.this;
            keyguardPatternViewController.mPendingLockCheck = LockPatternChecker.checkCredential(keyguardPatternViewController.mLockPatternUtils, LockscreenCredential.createPattern(list), currentUser, new LockPatternChecker.OnCheckCallback() { // from class: com.android.keyguard.KeyguardPatternViewController.UnlockPatternListener.1
                public final void onCancelled() {
                    KeyguardPatternViewController.this.mLatencyTracker.onActionEnd(4);
                }

                public final void onChecked(boolean z, int i) {
                    KeyguardPatternViewController.this.mLatencyTracker.onActionEnd(4);
                    KeyguardPatternViewController.this.mLockPatternView.enableInput();
                    UnlockPatternListener unlockPatternListener = UnlockPatternListener.this;
                    KeyguardPatternViewController.this.mPendingLockCheck = null;
                    if (!z) {
                        unlockPatternListener.onPatternChecked(currentUser, false, i, true);
                    }
                }

                public final void onEarlyMatched() {
                    KeyguardPatternViewController.this.mLatencyTracker.onActionEnd(3);
                    UnlockPatternListener.this.onPatternChecked(currentUser, true, 0, true);
                }
            });
            if (list.size() > 2) {
                KeyguardPatternViewController.this.getKeyguardSecurityCallback().userActivity();
                KeyguardPatternViewController.this.getKeyguardSecurityCallback().onUserInput();
            }
        }

        public final void onPatternStart() {
            KeyguardPatternViewController keyguardPatternViewController = KeyguardPatternViewController.this;
            keyguardPatternViewController.mLockPatternView.removeCallbacks(keyguardPatternViewController.mCancelPatternRunnable);
            KeyguardPatternViewController.this.mMessageAreaController.setMessage("");
        }

        public final void onPatternChecked(int i, boolean z, int i2, boolean z2) {
            boolean z3;
            if (KeyguardUpdateMonitor.getCurrentUser() == i) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (z) {
                KeyguardPatternViewController.this.getKeyguardSecurityCallback().reportUnlockAttempt(i, true, 0);
                if (z3) {
                    KeyguardPatternViewController.this.mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                    KeyguardPatternViewController.this.mLatencyTracker.onActionStart(11);
                    KeyguardPatternViewController.this.getKeyguardSecurityCallback().dismiss(i);
                    return;
                }
                return;
            }
            KeyguardPatternViewController.this.mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            if (z2) {
                KeyguardPatternViewController.this.getKeyguardSecurityCallback().reportUnlockAttempt(i, false, i2);
                if (i2 > 0) {
                    KeyguardPatternViewController.this.handleAttemptLockout(KeyguardPatternViewController.this.mLockPatternUtils.setLockoutAttemptDeadline(i, i2));
                }
            }
            if (i2 == 0) {
                KeyguardPatternViewController.this.mMessageAreaController.setMessage(2131952606);
                KeyguardPatternViewController keyguardPatternViewController = KeyguardPatternViewController.this;
                keyguardPatternViewController.mLockPatternView.postDelayed(keyguardPatternViewController.mCancelPatternRunnable, 2000L);
            }
        }
    }

    @Override // com.android.keyguard.KeyguardSecurityView
    public final boolean needsInput() {
        return false;
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public final void onPause() {
        this.mPaused = true;
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
        this.mMessageAreaController.setMessage("");
    }

    public final void handleAttemptLockout(long j) {
        this.mLockPatternView.clearPattern();
        this.mLockPatternView.setEnabled(false);
        this.mCountdownTimer = new CountDownTimer(((long) Math.ceil((j - SystemClock.elapsedRealtime()) / 1000.0d)) * 1000) { // from class: com.android.keyguard.KeyguardPatternViewController.3
            @Override // android.os.CountDownTimer
            public final void onTick(long j2) {
                HashMap hashMap = new HashMap();
                hashMap.put("count", Integer.valueOf((int) Math.round(j2 / 1000.0d)));
                KeyguardPatternViewController keyguardPatternViewController = KeyguardPatternViewController.this;
                keyguardPatternViewController.mMessageAreaController.setMessage(PluralsMessageFormatter.format(((KeyguardPatternView) keyguardPatternViewController.mView).getResources(), hashMap, 2131952601));
            }

            @Override // android.os.CountDownTimer
            public final void onFinish() {
                KeyguardPatternViewController.this.mLockPatternView.setEnabled(true);
                KeyguardPatternViewController keyguardPatternViewController = KeyguardPatternViewController.this;
                Objects.requireNonNull(keyguardPatternViewController);
                keyguardPatternViewController.mMessageAreaController.setMessage("");
            }
        }.start();
    }

    @Override // com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public final void onViewAttached() {
        this.mLockPatternView.setOnPatternListener(new UnlockPatternListener());
        this.mLockPatternView.setSaveEnabled(false);
        this.mLockPatternView.setInStealthMode(!this.mLockPatternUtils.isVisiblePatternEnabled(KeyguardUpdateMonitor.getCurrentUser()));
        this.mLockPatternView.setOnTouchListener(new View.OnTouchListener() { // from class: com.android.keyguard.KeyguardPatternViewController$$ExternalSyntheticLambda1
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                KeyguardPatternViewController keyguardPatternViewController = KeyguardPatternViewController.this;
                Objects.requireNonNull(keyguardPatternViewController);
                if (motionEvent.getActionMasked() != 0) {
                    return false;
                }
                keyguardPatternViewController.mFalsingCollector.avoidGesture();
                return false;
            }
        });
        EmergencyButtonController emergencyButtonController = this.mEmergencyButtonController;
        AnonymousClass1 r2 = this.mEmergencyButtonCallback;
        Objects.requireNonNull(emergencyButtonController);
        emergencyButtonController.mEmergencyButtonCallback = r2;
        View findViewById = ((KeyguardPatternView) this.mView).findViewById(2131427661);
        if (findViewById != null) {
            findViewById.setOnClickListener(new KeyguardPatternViewController$$ExternalSyntheticLambda0(this, 0));
        }
        this.mPostureController.addCallback(this.mPostureCallback);
    }

    @Override // com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mLockPatternView.setOnPatternListener((LockPatternView.OnPatternListener) null);
        this.mLockPatternView.setOnTouchListener(null);
        EmergencyButtonController emergencyButtonController = this.mEmergencyButtonController;
        Objects.requireNonNull(emergencyButtonController);
        emergencyButtonController.mEmergencyButtonCallback = null;
        View findViewById = ((KeyguardPatternView) this.mView).findViewById(2131427661);
        if (findViewById != null) {
            findViewById.setOnClickListener(null);
        }
        this.mPostureController.removeCallback(this.mPostureCallback);
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public final void reset() {
        this.mLockPatternView.setInStealthMode(!this.mLockPatternUtils.isVisiblePatternEnabled(KeyguardUpdateMonitor.getCurrentUser()));
        this.mLockPatternView.enableInput();
        this.mLockPatternView.setEnabled(true);
        this.mLockPatternView.clearPattern();
        long lockoutAttemptDeadline = this.mLockPatternUtils.getLockoutAttemptDeadline(KeyguardUpdateMonitor.getCurrentUser());
        if (lockoutAttemptDeadline != 0) {
            handleAttemptLockout(lockoutAttemptDeadline);
        } else {
            this.mMessageAreaController.setMessage("");
        }
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
        if (i == 0) {
            return;
        }
        if (i == 1) {
            this.mMessageAreaController.setMessage(2131952588);
        } else if (i == 2) {
            this.mMessageAreaController.setMessage(2131952591);
        } else if (i == 3) {
            this.mMessageAreaController.setMessage(2131952586);
        } else if (i == 4) {
            this.mMessageAreaController.setMessage(2131952593);
        } else if (i == 6) {
            this.mMessageAreaController.setMessage(2131952591);
        } else if (i != 7) {
            this.mMessageAreaController.setMessage(2131952591);
        } else {
            this.mMessageAreaController.setMessage(2131952591);
        }
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public final boolean startDisappearAnimation(Runnable runnable) {
        float f;
        DisappearAnimationUtils disappearAnimationUtils;
        KeyguardPatternView keyguardPatternView = (KeyguardPatternView) this.mView;
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
        Objects.requireNonNull(keyguardUpdateMonitor);
        boolean z = keyguardUpdateMonitor.mNeedsSlowUnlockTransition;
        Objects.requireNonNull(keyguardPatternView);
        if (z) {
            f = 1.5f;
        } else {
            f = 1.0f;
        }
        keyguardPatternView.mLockPatternView.clearPattern();
        keyguardPatternView.enableClipping(false);
        keyguardPatternView.setTranslationY(0.0f);
        long j = 300.0f * f;
        DisappearAnimationUtils disappearAnimationUtils2 = keyguardPatternView.mDisappearAnimationUtils;
        Objects.requireNonNull(disappearAnimationUtils2);
        float f2 = -disappearAnimationUtils2.mStartTranslation;
        DisappearAnimationUtils disappearAnimationUtils3 = keyguardPatternView.mDisappearAnimationUtils;
        Objects.requireNonNull(disappearAnimationUtils3);
        AppearAnimationUtils.startTranslationYAnimation(keyguardPatternView, 0L, j, f2, disappearAnimationUtils3.mInterpolator, new KeyguardInputView.AnonymousClass1(21));
        if (z) {
            disappearAnimationUtils = keyguardPatternView.mDisappearAnimationUtilsLocked;
        } else {
            disappearAnimationUtils = keyguardPatternView.mDisappearAnimationUtils;
        }
        disappearAnimationUtils.startAnimation2d(keyguardPatternView.mLockPatternView.getCellStates(), new KeyguardPatternView$$ExternalSyntheticLambda0(keyguardPatternView, runnable, 0), keyguardPatternView);
        if (TextUtils.isEmpty(keyguardPatternView.mSecurityMessageDisplay.getText())) {
            return true;
        }
        DisappearAnimationUtils disappearAnimationUtils4 = keyguardPatternView.mDisappearAnimationUtils;
        Objects.requireNonNull(disappearAnimationUtils4);
        float f3 = (-disappearAnimationUtils4.mStartTranslation) * 3.0f;
        DisappearAnimationUtils disappearAnimationUtils5 = keyguardPatternView.mDisappearAnimationUtils;
        Objects.requireNonNull(disappearAnimationUtils5);
        AppearAnimationUtils.createAnimation2((View) keyguardPatternView.mSecurityMessageDisplay, 0L, f * 200.0f, f3, false, disappearAnimationUtils5.mInterpolator, (Runnable) null);
        return true;
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [com.android.keyguard.KeyguardPatternViewController$$ExternalSyntheticLambda2] */
    /* JADX WARN: Type inference failed for: r3v2, types: [com.android.keyguard.KeyguardPatternViewController$1] */
    /* JADX WARN: Type inference failed for: r3v3, types: [com.android.keyguard.KeyguardPatternViewController$2] */
    public KeyguardPatternViewController(KeyguardPatternView keyguardPatternView, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, LatencyTracker latencyTracker, FalsingCollector falsingCollector, EmergencyButtonController emergencyButtonController, KeyguardMessageAreaController.Factory factory, DevicePostureController devicePostureController) {
        super(keyguardPatternView, securityMode, keyguardSecurityCallback, emergencyButtonController);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mLockPatternUtils = lockPatternUtils;
        this.mLatencyTracker = latencyTracker;
        this.mFalsingCollector = falsingCollector;
        this.mEmergencyButtonController = emergencyButtonController;
        KeyguardMessageArea findSecurityMessageDisplay = KeyguardMessageArea.findSecurityMessageDisplay(keyguardPatternView);
        Objects.requireNonNull(factory);
        this.mMessageAreaController = new KeyguardMessageAreaController(findSecurityMessageDisplay, factory.mKeyguardUpdateMonitor, factory.mConfigurationController);
        this.mLockPatternView = keyguardPatternView.findViewById(2131428273);
        this.mPostureController = devicePostureController;
    }

    @Override // com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public final void onInit() {
        super.onInit();
        this.mMessageAreaController.init();
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public final void reloadColors() {
        super.reloadColors();
        KeyguardMessageAreaController keyguardMessageAreaController = this.mMessageAreaController;
        Objects.requireNonNull(keyguardMessageAreaController);
        KeyguardMessageArea keyguardMessageArea = (KeyguardMessageArea) keyguardMessageAreaController.mView;
        Objects.requireNonNull(keyguardMessageArea);
        keyguardMessageArea.mDefaultColorState = Utils.getColorAttr(keyguardMessageArea.getContext(), 16842806);
        keyguardMessageArea.update();
        int defaultColor = Utils.getColorAttr(this.mLockPatternView.getContext(), 16842806).getDefaultColor();
        this.mLockPatternView.setColors(defaultColor, defaultColor, Utils.getColorAttr(this.mLockPatternView.getContext(), 16844099).getDefaultColor());
    }

    @Override // com.android.keyguard.KeyguardInputViewController
    public final void startAppearAnimation() {
        super.startAppearAnimation();
    }
}
