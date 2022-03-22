package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Insets;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimationControlListener;
import android.view.WindowInsetsAnimationController;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.android.internal.widget.LockscreenCredential;
import com.android.internal.widget.TextViewInputDisabler;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda0;
import java.util.Objects;
/* loaded from: classes.dex */
public class KeyguardPasswordView extends KeyguardAbsKeyInputView {
    public TextView mPasswordEntry;
    public TextViewInputDisabler mPasswordEntryDisabler;

    public KeyguardPasswordView(Context context) {
        this(context, null);
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public final int getPasswordTextViewId() {
        return 2131428570;
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public final int getPromptReasonStringRes(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return 2131952587;
        }
        if (i != 3) {
            return i != 4 ? 2131952590 : 2131952593;
        }
        return 2131952586;
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public final int getWrongPasswordStringId() {
        return 2131952605;
    }

    @Override // com.android.keyguard.KeyguardInputView
    public final void startAppearAnimation() {
        setAlpha(0.0f);
        animate().alpha(1.0f).setDuration(300L).start();
        setTranslationY(0.0f);
    }

    public KeyguardPasswordView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getResources().getDimensionPixelSize(2131165665);
        AnimationUtils.loadInterpolator(context, 17563662);
        AnimationUtils.loadInterpolator(context, 17563663);
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public final LockscreenCredential getEnteredCredential() {
        return LockscreenCredential.createPasswordOrNone(this.mPasswordEntry.getText());
    }

    @Override // android.view.ViewGroup
    public final boolean onRequestFocusInDescendants(int i, Rect rect) {
        return this.mPasswordEntry.requestFocus(i, rect);
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public final void resetPasswordText(boolean z, boolean z2) {
        this.mPasswordEntry.setText("");
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public final void setPasswordEntryEnabled(boolean z) {
        this.mPasswordEntry.setEnabled(z);
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public final void setPasswordEntryInputEnabled(boolean z) {
        this.mPasswordEntryDisabler.setInputEnabled(z);
    }

    @Override // com.android.keyguard.KeyguardInputView
    public final String getTitle() {
        return getResources().getString(17040501);
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputView, android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mPasswordEntry = (TextView) findViewById(2131428570);
        this.mPasswordEntryDisabler = new TextViewInputDisabler(this.mPasswordEntry);
    }

    @Override // com.android.keyguard.KeyguardInputView
    public final boolean startDisappearAnimation(final QSTileImpl$$ExternalSyntheticLambda0 qSTileImpl$$ExternalSyntheticLambda0) {
        getWindowInsetsController().controlWindowInsetsAnimation(WindowInsets.Type.ime(), 100L, Interpolators.LINEAR, null, new WindowInsetsAnimationControlListener() { // from class: com.android.keyguard.KeyguardPasswordView.1
            @Override // android.view.WindowInsetsAnimationControlListener
            public final void onFinished(WindowInsetsAnimationController windowInsetsAnimationController) {
            }

            @Override // android.view.WindowInsetsAnimationControlListener
            public final void onReady(final WindowInsetsAnimationController windowInsetsAnimationController, int i) {
                final ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.keyguard.KeyguardPasswordView$1$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        WindowInsetsAnimationController windowInsetsAnimationController2 = windowInsetsAnimationController;
                        ValueAnimator valueAnimator2 = ofFloat;
                        if (!windowInsetsAnimationController2.isCancelled()) {
                            Insets shownStateInsets = windowInsetsAnimationController2.getShownStateInsets();
                            windowInsetsAnimationController2.setInsetsAndAlpha(Insets.add(shownStateInsets, Insets.of(0, 0, 0, (int) (valueAnimator2.getAnimatedFraction() * ((-shownStateInsets.bottom) / 4)))), ((Float) valueAnimator.getAnimatedValue()).floatValue(), valueAnimator2.getAnimatedFraction());
                        }
                    }
                });
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.keyguard.KeyguardPasswordView.1.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationStart(Animator animator) {
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        windowInsetsAnimationController.finish(false);
                        KeyguardPasswordView keyguardPasswordView = KeyguardPasswordView.this;
                        Objects.requireNonNull(keyguardPasswordView);
                        Runnable runnable = keyguardPasswordView.mOnFinishImeAnimationRunnable;
                        if (runnable != null) {
                            runnable.run();
                            keyguardPasswordView.mOnFinishImeAnimationRunnable = null;
                        }
                        qSTileImpl$$ExternalSyntheticLambda0.run();
                    }
                });
                ofFloat.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
                ofFloat.start();
            }

            @Override // android.view.WindowInsetsAnimationControlListener
            public final void onCancelled(WindowInsetsAnimationController windowInsetsAnimationController) {
                KeyguardPasswordView keyguardPasswordView = KeyguardPasswordView.this;
                Objects.requireNonNull(keyguardPasswordView);
                Runnable runnable = keyguardPasswordView.mOnFinishImeAnimationRunnable;
                if (runnable != null) {
                    runnable.run();
                    keyguardPasswordView.mOnFinishImeAnimationRunnable = null;
                }
                qSTileImpl$$ExternalSyntheticLambda0.run();
            }
        });
        return true;
    }
}
