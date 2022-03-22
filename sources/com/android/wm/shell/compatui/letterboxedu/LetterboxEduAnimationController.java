package com.android.wm.shell.compatui.letterboxedu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.IntProperty;
import android.view.ContextThemeWrapper;
import android.view.animation.Animation;
import com.android.internal.R;
import com.android.internal.policy.TransitionAnimation;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline1;
/* loaded from: classes.dex */
public final class LetterboxEduAnimationController {
    public static final AnonymousClass3 DRAWABLE_ALPHA = new IntProperty<Drawable>() { // from class: com.android.wm.shell.compatui.letterboxedu.LetterboxEduAnimationController.3
        @Override // android.util.Property
        public final Integer get(Object obj) {
            return Integer.valueOf(((Drawable) obj).getAlpha());
        }

        @Override // android.util.IntProperty
        public final void setValue(Drawable drawable, int i) {
            drawable.setAlpha(i);
        }
    };
    public final int mAnimStyleResId;
    public Animator mBackgroundDimAnimator;
    public Animation mDialogAnimation;
    public final String mPackageName;
    public final TransitionAnimation mTransitionAnimation;

    /* renamed from: com.android.wm.shell.compatui.letterboxedu.LetterboxEduAnimationController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass1 implements Animation.AnimationListener {
        public final /* synthetic */ Runnable val$endCallback;
        public final /* synthetic */ Runnable val$startCallback;

        @Override // android.view.animation.Animation.AnimationListener
        public final void onAnimationRepeat(Animation animation) {
        }

        public AnonymousClass1(Runnable runnable, Runnable runnable2) {
            this.val$startCallback = runnable;
            this.val$endCallback = runnable2;
        }

        @Override // android.view.animation.Animation.AnimationListener
        public final void onAnimationEnd(Animation animation) {
            this.val$endCallback.run();
        }

        @Override // android.view.animation.Animation.AnimationListener
        public final void onAnimationStart(Animation animation) {
            this.val$startCallback.run();
        }
    }

    /* renamed from: com.android.wm.shell.compatui.letterboxedu.LetterboxEduAnimationController$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass2 extends AnimatorListenerAdapter {
        public AnonymousClass2() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            LetterboxEduAnimationController.this.mBackgroundDimAnimator = null;
        }
    }

    public final void cancelAnimation() {
        Animation animation = this.mDialogAnimation;
        if (animation != null) {
            animation.cancel();
            this.mDialogAnimation = null;
        }
        Animator animator = this.mBackgroundDimAnimator;
        if (animator != null) {
            animator.cancel();
            this.mBackgroundDimAnimator = null;
        }
    }

    public final Animation loadAnimation(int i) {
        Animation loadAnimationAttr = this.mTransitionAnimation.loadAnimationAttr(this.mPackageName, this.mAnimStyleResId, i, false);
        if (loadAnimationAttr == null) {
            KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("Failed to load animation ", i, "LetterboxEduAnimation");
        }
        return loadAnimationAttr;
    }

    public LetterboxEduAnimationController(Context context) {
        this.mTransitionAnimation = new TransitionAnimation(context, false, "LetterboxEduAnimation");
        this.mAnimStyleResId = new ContextThemeWrapper(context, 16974550).getTheme().obtainStyledAttributes(R.styleable.Window).getResourceId(8, 0);
        this.mPackageName = context.getPackageName();
    }
}
