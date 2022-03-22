package com.android.systemui.unfold.progress;

import android.util.Log;
import android.util.MathUtils;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.unfold.updates.FoldStateProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PhysicsBasedUnfoldTransitionProgressProvider.kt */
/* loaded from: classes.dex */
public final class PhysicsBasedUnfoldTransitionProgressProvider implements UnfoldTransitionProgressProvider, FoldStateProvider.FoldUpdatesListener, DynamicAnimation.OnAnimationEndListener {
    public final FoldStateProvider foldStateProvider;
    public boolean isAnimatedCancelRunning;
    public boolean isTransitionRunning;
    public final ArrayList listeners = new ArrayList();
    public final SpringAnimation springAnimation;
    public float transitionProgress;

    /* compiled from: PhysicsBasedUnfoldTransitionProgressProvider.kt */
    /* loaded from: classes.dex */
    public static final class AnimationProgressProperty extends FloatPropertyCompat<PhysicsBasedUnfoldTransitionProgressProvider> {
        public static final AnimationProgressProperty INSTANCE = new AnimationProgressProperty();

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(PhysicsBasedUnfoldTransitionProgressProvider physicsBasedUnfoldTransitionProgressProvider) {
            return physicsBasedUnfoldTransitionProgressProvider.transitionProgress;
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(PhysicsBasedUnfoldTransitionProgressProvider physicsBasedUnfoldTransitionProgressProvider, float f) {
            physicsBasedUnfoldTransitionProgressProvider.setTransitionProgress(f);
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(UnfoldTransitionProgressProvider.TransitionProgressListener transitionProgressListener) {
        this.listeners.add(transitionProgressListener);
    }

    public final void cancelTransition(float f, boolean z) {
        if (!this.isTransitionRunning || !z) {
            setTransitionProgress(f);
            this.isAnimatedCancelRunning = false;
            this.isTransitionRunning = false;
            this.springAnimation.cancel();
            Iterator it = this.listeners.iterator();
            while (it.hasNext()) {
                ((UnfoldTransitionProgressProvider.TransitionProgressListener) it.next()).onTransitionFinished();
            }
            Log.d("PhysicsBasedUnfoldTransitionProgressProvider", "onTransitionFinished");
            return;
        }
        this.isAnimatedCancelRunning = true;
        this.springAnimation.animateToFinalPosition(f);
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(DynamicAnimation<? extends DynamicAnimation<?>> dynamicAnimation, boolean z, float f, float f2) {
        if (this.isAnimatedCancelRunning) {
            cancelTransition(f, false);
        }
    }

    @Override // com.android.systemui.unfold.updates.FoldStateProvider.FoldUpdatesListener
    public final void onFoldUpdate(int i) {
        if (i != 1) {
            if (i == 2) {
                startTransition(0.0f);
                if (this.foldStateProvider.isFullyOpened()) {
                    cancelTransition(1.0f, true);
                }
            } else if (i == 3 || i == 4) {
                if (this.isTransitionRunning) {
                    cancelTransition(1.0f, true);
                }
            } else if (i == 5) {
                cancelTransition(0.0f, false);
            }
        } else if (!this.isTransitionRunning) {
            startTransition(1.0f);
        }
        Log.d("PhysicsBasedUnfoldTransitionProgressProvider", Intrinsics.stringPlus("onFoldUpdate = ", Integer.valueOf(i)));
    }

    @Override // com.android.systemui.unfold.updates.FoldStateProvider.FoldUpdatesListener
    public final void onHingeAngleUpdate(float f) {
        if (this.isTransitionRunning && !this.isAnimatedCancelRunning) {
            this.springAnimation.animateToFinalPosition(MathUtils.saturate(f / 165.0f));
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(UnfoldTransitionProgressProvider.TransitionProgressListener transitionProgressListener) {
        this.listeners.remove(transitionProgressListener);
    }

    public final void setTransitionProgress(float f) {
        if (this.isTransitionRunning) {
            Iterator it = this.listeners.iterator();
            while (it.hasNext()) {
                ((UnfoldTransitionProgressProvider.TransitionProgressListener) it.next()).onTransitionProgress(f);
            }
        }
        this.transitionProgress = f;
    }

    public final void startTransition(float f) {
        if (!this.isTransitionRunning) {
            Iterator it = this.listeners.iterator();
            while (it.hasNext()) {
                ((UnfoldTransitionProgressProvider.TransitionProgressListener) it.next()).onTransitionStarted();
            }
            this.isTransitionRunning = true;
            Log.d("PhysicsBasedUnfoldTransitionProgressProvider", "onTransitionStarted");
        }
        SpringAnimation springAnimation = this.springAnimation;
        SpringForce springForce = new SpringForce();
        springForce.mFinalPosition = f;
        springForce.setDampingRatio(1.0f);
        springForce.setStiffness(200.0f);
        Objects.requireNonNull(springAnimation);
        springAnimation.mSpring = springForce;
        springAnimation.mMinVisibleChange = 0.001f;
        springAnimation.mValue = f;
        springAnimation.mStartValueIsSet = true;
        springAnimation.mMinValue = 0.0f;
        springAnimation.mMaxValue = 1.0f;
        this.springAnimation.start();
    }

    public PhysicsBasedUnfoldTransitionProgressProvider(FoldStateProvider foldStateProvider) {
        this.foldStateProvider = foldStateProvider;
        SpringAnimation springAnimation = new SpringAnimation(this, AnimationProgressProperty.INSTANCE);
        springAnimation.addEndListener(this);
        this.springAnimation = springAnimation;
        foldStateProvider.addCallback(this);
        foldStateProvider.start();
    }
}
