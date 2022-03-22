package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.view.View;
import android.view.animation.Interpolator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.notification.TransformState;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ViewTransformationHelper implements TransformableView, TransformState.TransformInfo {
    public ValueAnimator mViewTransformationAnimation;
    public ArrayMap<Integer, View> mTransformedViews = new ArrayMap<>();
    public ArraySet<Integer> mKeysTransformingToSimilar = new ArraySet<>();
    public ArrayMap<Integer, CustomTransformation> mCustomTransformations = new ArrayMap<>();

    /* loaded from: classes.dex */
    public static abstract class CustomTransformation {
        public boolean customTransformTarget(TransformState transformState, TransformState transformState2) {
            return false;
        }

        public Interpolator getCustomInterpolator(int i, boolean z) {
            return null;
        }

        public boolean initTransformation(TransformState transformState, TransformState transformState2) {
            return false;
        }

        public abstract boolean transformFrom(TransformState transformState, TransformableView transformableView, float f);

        public abstract boolean transformTo(TransformState transformState, TransformableView transformableView, float f);
    }

    public final void addTransformedView(int i, View view) {
        this.mTransformedViews.put(Integer.valueOf(i), view);
    }

    @Override // com.android.systemui.statusbar.TransformableView
    public final void transformFrom(final TransformableView transformableView) {
        ValueAnimator valueAnimator = this.mViewTransformationAnimation;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mViewTransformationAnimation = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.ViewTransformationHelper$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ViewTransformationHelper viewTransformationHelper = ViewTransformationHelper.this;
                TransformableView transformableView2 = transformableView;
                Objects.requireNonNull(viewTransformationHelper);
                viewTransformationHelper.transformFrom(transformableView2, valueAnimator2.getAnimatedFraction());
            }
        });
        this.mViewTransformationAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.ViewTransformationHelper.2
            public boolean mCancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                if (!this.mCancelled) {
                    ViewTransformationHelper.this.setVisible(true);
                } else {
                    ViewTransformationHelper.m89$$Nest$mabortTransformations(ViewTransformationHelper.this);
                }
            }
        });
        this.mViewTransformationAnimation.setInterpolator(Interpolators.LINEAR);
        this.mViewTransformationAnimation.setDuration(360L);
        this.mViewTransformationAnimation.start();
    }

    @Override // com.android.systemui.statusbar.TransformableView
    public final void transformTo(final TransformableView transformableView, final Runnable runnable) {
        ValueAnimator valueAnimator = this.mViewTransformationAnimation;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mViewTransformationAnimation = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.ViewTransformationHelper$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ViewTransformationHelper viewTransformationHelper = ViewTransformationHelper.this;
                TransformableView transformableView2 = transformableView;
                Objects.requireNonNull(viewTransformationHelper);
                viewTransformationHelper.transformTo(transformableView2, valueAnimator2.getAnimatedFraction());
            }
        });
        this.mViewTransformationAnimation.setInterpolator(Interpolators.LINEAR);
        this.mViewTransformationAnimation.setDuration(360L);
        this.mViewTransformationAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.ViewTransformationHelper.1
            public boolean mCancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                if (!this.mCancelled) {
                    Runnable runnable2 = runnable;
                    if (runnable2 != null) {
                        runnable2.run();
                    }
                    ViewTransformationHelper.this.setVisible(false);
                    ViewTransformationHelper.this.mViewTransformationAnimation = null;
                    return;
                }
                ViewTransformationHelper.m89$$Nest$mabortTransformations(ViewTransformationHelper.this);
            }
        });
        this.mViewTransformationAnimation.start();
    }

    public final void addTransformedView(View view) {
        int id = view.getId();
        if (id != -1) {
            addTransformedView(id, view);
            return;
        }
        throw new IllegalArgumentException("View argument does not have a valid id");
    }

    @Override // com.android.systemui.statusbar.TransformableView
    public final TransformState getCurrentState(int i) {
        View view = this.mTransformedViews.get(Integer.valueOf(i));
        if (view == null || view.getVisibility() == 8) {
            return null;
        }
        TransformState createFrom = TransformState.createFrom(view, this);
        if (this.mKeysTransformingToSimilar.contains(Integer.valueOf(i))) {
            createFrom.mSameAsAny = true;
        }
        return createFrom;
    }

    @Override // com.android.systemui.statusbar.TransformableView
    public final void setVisible(boolean z) {
        ValueAnimator valueAnimator = this.mViewTransformationAnimation;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        for (Integer num : this.mTransformedViews.keySet()) {
            TransformState currentState = getCurrentState(num.intValue());
            if (currentState != null) {
                currentState.setVisible(z, false);
                currentState.recycle();
            }
        }
    }

    /* renamed from: -$$Nest$mabortTransformations  reason: not valid java name */
    public static void m89$$Nest$mabortTransformations(ViewTransformationHelper viewTransformationHelper) {
        Objects.requireNonNull(viewTransformationHelper);
        for (Integer num : viewTransformationHelper.mTransformedViews.keySet()) {
            TransformState currentState = viewTransformationHelper.getCurrentState(num.intValue());
            if (currentState != null) {
                currentState.abortTransformation();
                currentState.recycle();
            }
        }
    }

    public final void addViewTransformingToSimilar(View view) {
        int id = view.getId();
        if (id != -1) {
            addTransformedView(id, view);
            this.mKeysTransformingToSimilar.add(Integer.valueOf(id));
            return;
        }
        throw new IllegalArgumentException("View argument does not have a valid id");
    }

    @Override // com.android.systemui.statusbar.TransformableView
    public final void transformFrom(TransformableView transformableView, float f) {
        for (Integer num : this.mTransformedViews.keySet()) {
            TransformState currentState = getCurrentState(num.intValue());
            if (currentState != null) {
                CustomTransformation customTransformation = this.mCustomTransformations.get(num);
                if (customTransformation == null || !customTransformation.transformFrom(currentState, transformableView, f)) {
                    TransformState currentState2 = transformableView.getCurrentState(num.intValue());
                    if (currentState2 != null) {
                        currentState.transformViewFrom(currentState2, f);
                        currentState2.recycle();
                    } else {
                        currentState.appear(f, transformableView);
                    }
                    currentState.recycle();
                } else {
                    currentState.recycle();
                }
            }
        }
    }

    @Override // com.android.systemui.statusbar.TransformableView
    public final void transformTo(TransformableView transformableView, float f) {
        for (Integer num : this.mTransformedViews.keySet()) {
            TransformState currentState = getCurrentState(num.intValue());
            if (currentState != null) {
                CustomTransformation customTransformation = this.mCustomTransformations.get(num);
                if (customTransformation == null || !customTransformation.transformTo(currentState, transformableView, f)) {
                    TransformState currentState2 = transformableView.getCurrentState(num.intValue());
                    if (currentState2 != null) {
                        currentState.transformViewTo(currentState2, f);
                        currentState2.recycle();
                    } else {
                        currentState.disappear(f, transformableView);
                    }
                    currentState.recycle();
                } else {
                    currentState.recycle();
                }
            }
        }
    }
}
