package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.keyguard.KeyguardIndication;
import java.util.Objects;
/* loaded from: classes.dex */
public class KeyguardIndicationTextView extends TextView {
    public static final /* synthetic */ int $r8$clinit = 0;
    public boolean mAnimationsEnabled = true;
    public KeyguardIndication mKeyguardIndicationInfo;
    public AnimatorSet mLastAnimator;
    public CharSequence mMessage;

    public KeyguardIndicationTextView(Context context) {
        super(context);
    }

    public final AnimatorSet getOutAnimator() {
        long j;
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, View.ALPHA, 0.0f);
        long j2 = 0;
        if (!this.mAnimationsEnabled) {
            j = 0;
        } else {
            j = 167;
        }
        ofFloat.setDuration(j);
        ofFloat.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.KeyguardIndicationTextView.3
            public boolean mCancelled = false;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                this.mCancelled = true;
                KeyguardIndicationTextView.this.setAlpha(0.0f);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                if (!this.mCancelled) {
                    KeyguardIndicationTextView keyguardIndicationTextView = KeyguardIndicationTextView.this;
                    int i = KeyguardIndicationTextView.$r8$clinit;
                    keyguardIndicationTextView.setNextIndication();
                }
            }
        });
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, 0.0f, -((TextView) this).mContext.getResources().getDimensionPixelSize(2131165852));
        if (this.mAnimationsEnabled) {
            j2 = 167;
        }
        ofFloat2.setDuration(j2);
        animatorSet.playTogether(ofFloat, ofFloat2);
        return animatorSet;
    }

    public final void setNextIndication() {
        boolean z;
        KeyguardIndication keyguardIndication = this.mKeyguardIndicationInfo;
        if (keyguardIndication != null) {
            Objects.requireNonNull(keyguardIndication);
            if (keyguardIndication.mBackground != null) {
                setTextAppearance(2132017918);
            } else {
                setTextAppearance(2132017917);
            }
            KeyguardIndication keyguardIndication2 = this.mKeyguardIndicationInfo;
            Objects.requireNonNull(keyguardIndication2);
            setBackground(keyguardIndication2.mBackground);
            KeyguardIndication keyguardIndication3 = this.mKeyguardIndicationInfo;
            Objects.requireNonNull(keyguardIndication3);
            setTextColor(keyguardIndication3.mTextColor);
            KeyguardIndication keyguardIndication4 = this.mKeyguardIndicationInfo;
            Objects.requireNonNull(keyguardIndication4);
            setOnClickListener(keyguardIndication4.mOnClickListener);
            KeyguardIndication keyguardIndication5 = this.mKeyguardIndicationInfo;
            Objects.requireNonNull(keyguardIndication5);
            if (keyguardIndication5.mOnClickListener != null) {
                z = true;
            } else {
                z = false;
            }
            setClickable(z);
            KeyguardIndication keyguardIndication6 = this.mKeyguardIndicationInfo;
            Objects.requireNonNull(keyguardIndication6);
            Drawable drawable = keyguardIndication6.mIcon;
            if (drawable != null) {
                drawable.setTint(getCurrentTextColor());
                if (drawable instanceof AnimatedVectorDrawable) {
                    ((AnimatedVectorDrawable) drawable).start();
                }
            }
            setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
        }
        setText(this.mMessage);
    }

    public final void switchIndication(CharSequence charSequence, KeyguardIndication keyguardIndication, boolean z, final Runnable runnable) {
        boolean z2;
        long j;
        long j2;
        this.mMessage = charSequence;
        this.mKeyguardIndicationInfo = keyguardIndication;
        if (z) {
            if (keyguardIndication == null || keyguardIndication.mIcon == null) {
                z2 = false;
            } else {
                z2 = true;
            }
            AnimatorSet animatorSet = new AnimatorSet();
            if (!TextUtils.isEmpty(this.mMessage) || z2) {
                AnimatorSet animatorSet2 = new AnimatorSet();
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, View.ALPHA, 1.0f);
                long j3 = 0;
                if (!this.mAnimationsEnabled) {
                    j = 0;
                } else {
                    j = 150;
                }
                ofFloat.setStartDelay(j);
                if (!this.mAnimationsEnabled) {
                    j2 = 0;
                } else {
                    j2 = 317;
                }
                ofFloat.setDuration(j2);
                ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
                ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, ((TextView) this).mContext.getResources().getDimensionPixelSize(2131165852), 0.0f);
                if (this.mAnimationsEnabled) {
                    j3 = 600;
                }
                ofFloat2.setDuration(j3);
                ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.KeyguardIndicationTextView.4
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationCancel(Animator animator) {
                        super.onAnimationCancel(animator);
                        KeyguardIndicationTextView.this.setTranslationY(0.0f);
                        KeyguardIndicationTextView.this.setAlpha(1.0f);
                    }
                });
                animatorSet2.playTogether(ofFloat2, ofFloat);
                animatorSet2.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.KeyguardIndicationTextView.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        Runnable runnable2 = runnable;
                        if (runnable2 != null) {
                            runnable2.run();
                        }
                    }
                });
                animatorSet.playSequentially(getOutAnimator(), animatorSet2);
            } else {
                Animator outAnimator = getOutAnimator();
                outAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.KeyguardIndicationTextView.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        Runnable runnable2 = runnable;
                        if (runnable2 != null) {
                            runnable2.run();
                        }
                    }
                });
                animatorSet.play(outAnimator);
            }
            AnimatorSet animatorSet3 = this.mLastAnimator;
            if (animatorSet3 != null) {
                animatorSet3.cancel();
            }
            this.mLastAnimator = animatorSet;
            animatorSet.start();
            return;
        }
        setAlpha(1.0f);
        setTranslationY(0.0f);
        setNextIndication();
        if (runnable != null) {
            runnable.run();
        }
        AnimatorSet animatorSet4 = this.mLastAnimator;
        if (animatorSet4 != null) {
            animatorSet4.cancel();
            this.mLastAnimator = null;
        }
    }

    public KeyguardIndicationTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public KeyguardIndicationTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public KeyguardIndicationTextView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @VisibleForTesting
    public void setAnimationsEnabled(boolean z) {
        this.mAnimationsEnabled = z;
    }
}
