package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda0;
/* loaded from: classes.dex */
public abstract class KeyguardInputView extends LinearLayout {
    public Runnable mOnFinishImeAnimationRunnable;

    /* renamed from: com.android.keyguard.KeyguardInputView$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass1 extends AnimatorListenerAdapter {
        public boolean mIsCancel;
        public final /* synthetic */ int val$cuj;

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationCancel(Animator animator) {
            this.mIsCancel = true;
        }

        public AnonymousClass1(int i) {
            this.val$cuj = i;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            if (this.mIsCancel) {
                InteractionJankMonitor.getInstance().cancel(this.val$cuj);
            } else {
                InteractionJankMonitor.getInstance().end(this.val$cuj);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationStart(Animator animator) {
            InteractionJankMonitor.getInstance().begin(KeyguardInputView.this, this.val$cuj);
        }
    }

    public KeyguardInputView(Context context) {
        super(context);
    }

    public boolean disallowInterceptTouch(MotionEvent motionEvent) {
        return false;
    }

    public abstract String getTitle();

    public void startAppearAnimation() {
    }

    public boolean startDisappearAnimation(QSTileImpl$$ExternalSyntheticLambda0 qSTileImpl$$ExternalSyntheticLambda0) {
        return false;
    }

    public KeyguardInputView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public KeyguardInputView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
