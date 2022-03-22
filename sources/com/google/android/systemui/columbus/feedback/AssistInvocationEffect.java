package com.google.android.systemui.columbus.feedback;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;
import com.android.systemui.assist.AssistManager;
import com.google.android.systemui.assist.AssistManagerGoogle;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import java.util.Objects;
/* compiled from: AssistInvocationEffect.kt */
/* loaded from: classes.dex */
public final class AssistInvocationEffect implements FeedbackEffect {
    public ValueAnimator animation;
    public final AssistInvocationEffect$animatorListener$1 animatorListener;
    public final AssistInvocationEffect$animatorUpdateListener$1 animatorUpdateListener;
    public final AssistManagerGoogle assistManager;
    public float progress;

    @Override // com.google.android.systemui.columbus.feedback.FeedbackEffect
    public final void onGestureDetected(int i, GestureSensor.DetectionProperties detectionProperties) {
        ValueAnimator valueAnimator;
        boolean z;
        ValueAnimator valueAnimator2;
        boolean z2 = false;
        if (i == 0) {
            ValueAnimator valueAnimator3 = this.animation;
            if (valueAnimator3 != null && valueAnimator3.isRunning()) {
                z2 = true;
            }
            if (z2 && (valueAnimator = this.animation) != null) {
                valueAnimator.cancel();
            }
            this.animation = null;
        } else if (i == 1) {
            ValueAnimator valueAnimator4 = this.animation;
            if (valueAnimator4 != null && valueAnimator4.isRunning()) {
                z = true;
            } else {
                z = false;
            }
            if (z && (valueAnimator2 = this.animation) != null) {
                valueAnimator2.cancel();
            }
            this.animation = null;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.progress, 1.0f);
            ofFloat.setDuration(200L);
            ofFloat.setInterpolator(new DecelerateInterpolator());
            ofFloat.addUpdateListener(this.animatorUpdateListener);
            ofFloat.addListener(this.animatorListener);
            this.animation = ofFloat;
            ofFloat.start();
        }
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [com.google.android.systemui.columbus.feedback.AssistInvocationEffect$animatorUpdateListener$1] */
    /* JADX WARN: Type inference failed for: r2v3, types: [com.google.android.systemui.columbus.feedback.AssistInvocationEffect$animatorListener$1] */
    public AssistInvocationEffect(AssistManager assistManager) {
        AssistManagerGoogle assistManagerGoogle;
        if (assistManager instanceof AssistManagerGoogle) {
            assistManagerGoogle = (AssistManagerGoogle) assistManager;
        } else {
            assistManagerGoogle = null;
        }
        this.assistManager = assistManagerGoogle;
        this.animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.systemui.columbus.feedback.AssistInvocationEffect$animatorUpdateListener$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (valueAnimator != null) {
                    AssistInvocationEffect assistInvocationEffect = AssistInvocationEffect.this;
                    Object animatedValue = valueAnimator.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                    float floatValue = ((Float) animatedValue).floatValue();
                    assistInvocationEffect.progress = floatValue;
                    AssistManagerGoogle assistManagerGoogle2 = assistInvocationEffect.assistManager;
                    if (assistManagerGoogle2 != null) {
                        assistManagerGoogle2.onInvocationProgress(2, floatValue);
                    }
                }
            }
        };
        this.animatorListener = new Animator.AnimatorListener() { // from class: com.google.android.systemui.columbus.feedback.AssistInvocationEffect$animatorListener$1
            @Override // android.animation.Animator.AnimatorListener
            public final void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public final void onAnimationStart(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                AssistInvocationEffect.this.progress = 0.0f;
            }

            @Override // android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                AssistInvocationEffect.this.progress = 0.0f;
            }
        };
    }
}
