package com.android.systemui.power;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Binder;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import java.util.Objects;
/* loaded from: classes.dex */
public final class InattentiveSleepWarningView extends FrameLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public boolean mDismissing;
    public Animator mFadeOutAnimator;
    public final Binder mWindowToken = new Binder();
    public final WindowManager mWindowManager = (WindowManager) ((FrameLayout) this).mContext.getSystemService(WindowManager.class);

    public InattentiveSleepWarningView(Context context) {
        super(context);
        LayoutInflater.from(((FrameLayout) this).mContext).inflate(2131624140, (ViewGroup) this, true);
        setFocusable(true);
        setOnKeyListener(InattentiveSleepWarningView$$ExternalSyntheticLambda0.INSTANCE);
        Animator loadAnimator = AnimatorInflater.loadAnimator(getContext(), 17498113);
        this.mFadeOutAnimator = loadAnimator;
        loadAnimator.setTarget(this);
        this.mFadeOutAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.power.InattentiveSleepWarningView.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                InattentiveSleepWarningView inattentiveSleepWarningView = InattentiveSleepWarningView.this;
                inattentiveSleepWarningView.mDismissing = false;
                inattentiveSleepWarningView.setAlpha(1.0f);
                InattentiveSleepWarningView.this.setVisibility(0);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                InattentiveSleepWarningView inattentiveSleepWarningView = InattentiveSleepWarningView.this;
                Objects.requireNonNull(inattentiveSleepWarningView);
                if (inattentiveSleepWarningView.mDismissing) {
                    inattentiveSleepWarningView.setVisibility(4);
                    inattentiveSleepWarningView.mWindowManager.removeView(inattentiveSleepWarningView);
                }
            }
        });
    }
}
