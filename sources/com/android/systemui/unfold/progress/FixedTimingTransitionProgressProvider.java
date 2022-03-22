package com.android.systemui.unfold.progress;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.util.FloatProperty;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.unfold.updates.FoldStateProvider;
import java.util.ArrayList;
import java.util.Iterator;
/* compiled from: FixedTimingTransitionProgressProvider.kt */
/* loaded from: classes.dex */
public final class FixedTimingTransitionProgressProvider implements UnfoldTransitionProgressProvider, FoldStateProvider.FoldUpdatesListener {
    public final ObjectAnimator animator;
    public final FoldStateProvider foldStateProvider;
    public final ArrayList listeners = new ArrayList();
    public float transitionProgress;

    /* compiled from: FixedTimingTransitionProgressProvider.kt */
    /* loaded from: classes.dex */
    public static final class AnimationProgressProperty extends FloatProperty<FixedTimingTransitionProgressProvider> {
        public static final AnimationProgressProperty INSTANCE = new AnimationProgressProperty();

        public AnimationProgressProperty() {
            super("animation_progress");
        }

        @Override // android.util.Property
        public final Float get(Object obj) {
            return Float.valueOf(((FixedTimingTransitionProgressProvider) obj).transitionProgress);
        }

        @Override // android.util.FloatProperty
        public final void setValue(FixedTimingTransitionProgressProvider fixedTimingTransitionProgressProvider, float f) {
            FixedTimingTransitionProgressProvider fixedTimingTransitionProgressProvider2 = fixedTimingTransitionProgressProvider;
            Iterator it = fixedTimingTransitionProgressProvider2.listeners.iterator();
            while (it.hasNext()) {
                ((UnfoldTransitionProgressProvider.TransitionProgressListener) it.next()).onTransitionProgress(f);
            }
            fixedTimingTransitionProgressProvider2.transitionProgress = f;
        }
    }

    /* compiled from: FixedTimingTransitionProgressProvider.kt */
    /* loaded from: classes.dex */
    public final class AnimatorListener implements Animator.AnimatorListener {
        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationRepeat(Animator animator) {
        }

        public AnimatorListener() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            Iterator it = FixedTimingTransitionProgressProvider.this.listeners.iterator();
            while (it.hasNext()) {
                ((UnfoldTransitionProgressProvider.TransitionProgressListener) it.next()).onTransitionFinished();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationStart(Animator animator) {
            Iterator it = FixedTimingTransitionProgressProvider.this.listeners.iterator();
            while (it.hasNext()) {
                ((UnfoldTransitionProgressProvider.TransitionProgressListener) it.next()).onTransitionStarted();
            }
        }
    }

    @Override // com.android.systemui.unfold.updates.FoldStateProvider.FoldUpdatesListener
    public final void onFoldUpdate(int i) {
        if (i == 2) {
            this.animator.start();
        } else if (i == 5) {
            this.animator.cancel();
        }
    }

    @Override // com.android.systemui.unfold.updates.FoldStateProvider.FoldUpdatesListener
    public final void onHingeAngleUpdate(float f) {
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(UnfoldTransitionProgressProvider.TransitionProgressListener transitionProgressListener) {
        this.listeners.add(transitionProgressListener);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(UnfoldTransitionProgressProvider.TransitionProgressListener transitionProgressListener) {
        this.listeners.remove(transitionProgressListener);
    }

    public FixedTimingTransitionProgressProvider(FoldStateProvider foldStateProvider) {
        this.foldStateProvider = foldStateProvider;
        AnimatorListener animatorListener = new AnimatorListener();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, AnimationProgressProperty.INSTANCE, 0.0f, 1.0f);
        ofFloat.setDuration(400L);
        ofFloat.addListener(animatorListener);
        this.animator = ofFloat;
        foldStateProvider.addCallback(this);
        foldStateProvider.start();
    }
}
