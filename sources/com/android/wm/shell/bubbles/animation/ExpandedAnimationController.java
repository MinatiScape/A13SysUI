package com.android.wm.shell.bubbles.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.View;
import android.view.animation.LinearInterpolator;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda6;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda7;
import com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda0;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda3;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.bubbles.BubblePositioner;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda16;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
import com.android.wm.shell.common.ExecutorUtils$$ExternalSyntheticLambda1;
import com.android.wm.shell.onehanded.OneHandedDisplayAreaOrganizer$$ExternalSyntheticLambda0;
import com.google.android.collect.Sets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import kotlin.jvm.functions.Function1;
/* loaded from: classes.dex */
public final class ExpandedAnimationController extends PhysicsAnimationLayout.PhysicsAnimationController {
    public Runnable mAfterCollapse;
    public Runnable mAfterExpand;
    public float mBubbleSizePx;
    public BubbleStackView mBubbleStackView;
    public PointF mCollapsePoint;
    public Runnable mLeadBubbleEndAction;
    public AnonymousClass1 mMagnetizedBubbleDraggingOut;
    public Runnable mOnBubbleAnimatedOutAction;
    public BubblePositioner mPositioner;
    public float mStackOffsetPx;
    public final PhysicsAnimator.SpringConfig mAnimateOutSpringConfig = new PhysicsAnimator.SpringConfig(1000.0f, 1.0f);
    public boolean mAnimatingExpand = false;
    public boolean mPreparingToCollapse = false;
    public boolean mAnimatingCollapse = false;
    public boolean mSpringingBubbleToTouch = false;
    public boolean mSpringToTouchOnNextMotionEvent = false;
    public boolean mBubbleDraggedOutEnough = false;

    public final void expandFromStack(Runnable runnable) {
        this.mPreparingToCollapse = false;
        this.mAnimatingCollapse = false;
        this.mAnimatingExpand = true;
        this.mAfterExpand = runnable;
        this.mLeadBubbleEndAction = null;
        startOrUpdatePathAnimation(true);
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    public final HashSet getAnimatedProperties() {
        return Sets.newHashSet(new DynamicAnimation.ViewProperty[]{DynamicAnimation.TRANSLATION_X, DynamicAnimation.TRANSLATION_Y, DynamicAnimation.SCALE_X, DynamicAnimation.SCALE_Y, DynamicAnimation.ALPHA});
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    public final int getNextAnimationInChain(DynamicAnimation.ViewProperty viewProperty, int i) {
        return -1;
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    public final float getOffsetForChainedPropertyAnimation(DynamicAnimation.ViewProperty viewProperty, int i) {
        return 0.0f;
    }

    public final View getDraggedOutBubble() {
        AnonymousClass1 r0 = this.mMagnetizedBubbleDraggingOut;
        if (r0 == null) {
            return null;
        }
        return (View) r0.underlyingObject;
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    public final SpringForce getSpringForce() {
        SpringForce springForce = new SpringForce();
        springForce.setDampingRatio(0.65f);
        springForce.setStiffness(200.0f);
        return springForce;
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    public final void onChildAdded(View view, int i) {
        float f;
        boolean z = true;
        if (this.mAnimatingExpand) {
            startOrUpdatePathAnimation(true);
        } else if (this.mAnimatingCollapse) {
            startOrUpdatePathAnimation(false);
        } else {
            BubblePositioner bubblePositioner = this.mPositioner;
            PointF pointF = this.mCollapsePoint;
            if (pointF == null) {
                pointF = bubblePositioner.getRestingPosition();
            } else {
                Objects.requireNonNull(bubblePositioner);
            }
            if ((bubblePositioner.mBubbleSize / 2) + ((int) pointF.x) >= bubblePositioner.mScreenRect.width() / 2) {
                z = false;
            }
            PointF expandedBubbleXY = this.mPositioner.getExpandedBubbleXY(i, this.mBubbleStackView.getState());
            if (this.mPositioner.showBubblesVertically()) {
                view.setTranslationY(expandedBubbleXY.y);
            } else {
                view.setTranslationX(expandedBubbleXY.x);
            }
            if (!this.mPreparingToCollapse) {
                if (this.mPositioner.showBubblesVertically()) {
                    if (z) {
                        f = expandedBubbleXY.x - (this.mBubbleSizePx * 4.0f);
                    } else {
                        f = expandedBubbleXY.x + (this.mBubbleSizePx * 4.0f);
                    }
                    PhysicsAnimationLayout.PhysicsPropertyAnimator animationForChild = animationForChild(view);
                    HashMap hashMap = animationForChild.mInitialPropertyValues;
                    DynamicAnimation.AnonymousClass1 r4 = DynamicAnimation.TRANSLATION_X;
                    hashMap.put(r4, Float.valueOf(f));
                    animationForChild.mPathAnimator = null;
                    animationForChild.property(r4, expandedBubbleXY.y, new Runnable[0]);
                    animationForChild.start(new Runnable[0]);
                } else {
                    float f2 = expandedBubbleXY.y - (this.mBubbleSizePx * 4.0f);
                    PhysicsAnimationLayout.PhysicsPropertyAnimator animationForChild2 = animationForChild(view);
                    animationForChild2.mInitialPropertyValues.put(DynamicAnimation.TRANSLATION_Y, Float.valueOf(f2));
                    animationForChild2.translationY(expandedBubbleXY.y, new Runnable[0]);
                    animationForChild2.start(new Runnable[0]);
                }
                updateBubblePositions();
            }
        }
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    public final void onChildReordered() {
        if (!this.mPreparingToCollapse) {
            if (this.mAnimatingCollapse) {
                startOrUpdatePathAnimation(false);
            } else {
                updateBubblePositions();
            }
        }
    }

    public final void snapBubbleBack(View view, float f, float f2) {
        PhysicsAnimationLayout physicsAnimationLayout = this.mLayout;
        if (physicsAnimationLayout != null) {
            int indexOfChild = physicsAnimationLayout.indexOfChild(view);
            PointF expandedBubbleXY = this.mPositioner.getExpandedBubbleXY(indexOfChild, this.mBubbleStackView.getState());
            PhysicsAnimationLayout.PhysicsPropertyAnimator animationForChild = animationForChild(this.mLayout.getChildAt(indexOfChild));
            float f3 = expandedBubbleXY.x;
            float f4 = expandedBubbleXY.y;
            animationForChild.mPositionEndActions = new Runnable[0];
            animationForChild.mPathAnimator = null;
            DynamicAnimation.AnonymousClass1 r6 = DynamicAnimation.TRANSLATION_X;
            animationForChild.property(r6, f3, new Runnable[0]);
            animationForChild.translationY(f4, new Runnable[0]);
            animationForChild.mPositionStartVelocities.put(r6, Float.valueOf(f));
            animationForChild.mPositionStartVelocities.put(DynamicAnimation.TRANSLATION_Y, Float.valueOf(f2));
            animationForChild.start(new TaskView$$ExternalSyntheticLambda3(view, 7));
            this.mMagnetizedBubbleDraggingOut = null;
            updateBubblePositions();
        }
    }

    public final void startOrUpdatePathAnimation(final boolean z) {
        Runnable runnable;
        if (z) {
            runnable = new ScrimView$$ExternalSyntheticLambda0(this, 5);
        } else {
            runnable = new KeyguardUpdateMonitor$$ExternalSyntheticLambda6(this, 9);
        }
        animationsForChildrenFromIndex(new PhysicsAnimationLayout.PhysicsAnimationController.ChildAnimationConfigurator() { // from class: com.android.wm.shell.bubbles.animation.ExpandedAnimationController$$ExternalSyntheticLambda0
            @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController.ChildAnimationConfigurator
            public final void configureAnimationForChildAtIndex(int i, PhysicsAnimationLayout.PhysicsPropertyAnimator physicsPropertyAnimator) {
                boolean z2;
                int i2;
                boolean z3;
                Runnable runnable2;
                ExpandedAnimationController expandedAnimationController = ExpandedAnimationController.this;
                boolean z4 = z;
                Objects.requireNonNull(expandedAnimationController);
                View childAt = expandedAnimationController.mLayout.getChildAt(i);
                Path path = new Path();
                path.moveTo(childAt.getTranslationX(), childAt.getTranslationY());
                PointF expandedBubbleXY = expandedAnimationController.mPositioner.getExpandedBubbleXY(i, expandedAnimationController.mBubbleStackView.getState());
                if (z4) {
                    path.lineTo(childAt.getTranslationX(), expandedBubbleXY.y);
                    path.lineTo(expandedBubbleXY.x, expandedBubbleXY.y);
                } else {
                    float f = expandedAnimationController.mCollapsePoint.x;
                    path.lineTo(f, expandedBubbleXY.y);
                    path.lineTo(f, (Math.min(i, 1) * expandedAnimationController.mStackOffsetPx) + expandedAnimationController.mCollapsePoint.y);
                }
                if ((!z4 || expandedAnimationController.mLayout.isFirstChildXLeftOfCenter(childAt.getTranslationX())) && (z4 || !expandedAnimationController.mLayout.isFirstChildXLeftOfCenter(expandedAnimationController.mCollapsePoint.x))) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                if (z2) {
                    i2 = i * 10;
                } else {
                    i2 = (expandedAnimationController.mLayout.getChildCount() - i) * 10;
                }
                if ((!z2 || i != 0) && (z2 || i != expandedAnimationController.mLayout.getChildCount() - 1)) {
                    z3 = false;
                } else {
                    z3 = true;
                }
                LinearInterpolator linearInterpolator = Interpolators.LINEAR;
                final Runnable[] runnableArr = new Runnable[2];
                if (z3) {
                    runnable2 = expandedAnimationController.mLeadBubbleEndAction;
                } else {
                    runnable2 = null;
                }
                runnableArr[0] = runnable2;
                runnableArr[1] = new KeyguardUpdateMonitor$$ExternalSyntheticLambda7(expandedAnimationController, 5);
                ObjectAnimator objectAnimator = physicsPropertyAnimator.mPathAnimator;
                if (objectAnimator != null) {
                    objectAnimator.cancel();
                }
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(physicsPropertyAnimator, physicsPropertyAnimator.mCurrentPointOnPathXProperty, physicsPropertyAnimator.mCurrentPointOnPathYProperty, path);
                physicsPropertyAnimator.mPathAnimator = ofFloat;
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsPropertyAnimator.3
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        Runnable[] runnableArr2;
                        for (Runnable runnable3 : runnableArr) {
                            if (runnable3 != null) {
                                runnable3.run();
                            }
                        }
                    }
                });
                physicsPropertyAnimator.mPathAnimator.setDuration(175);
                physicsPropertyAnimator.mPathAnimator.setInterpolator(linearInterpolator);
                HashMap hashMap = physicsPropertyAnimator.mAnimatedProperties;
                DynamicAnimation.AnonymousClass1 r9 = DynamicAnimation.TRANSLATION_X;
                hashMap.remove(r9);
                HashMap hashMap2 = physicsPropertyAnimator.mAnimatedProperties;
                DynamicAnimation.AnonymousClass2 r0 = DynamicAnimation.TRANSLATION_Y;
                hashMap2.remove(r0);
                physicsPropertyAnimator.mInitialPropertyValues.remove(r9);
                physicsPropertyAnimator.mInitialPropertyValues.remove(r0);
                PhysicsAnimationLayout.this.mEndActionForProperty.remove(r9);
                PhysicsAnimationLayout.this.mEndActionForProperty.remove(r0);
                physicsPropertyAnimator.mStartDelay = i2;
                physicsPropertyAnimator.mStiffness = 1000.0f;
            }
        }).startAll(new Runnable[]{runnable});
    }

    public final void updateBubblePositions() {
        if (!(this.mAnimatingExpand || this.mAnimatingCollapse)) {
            for (int i = 0; i < this.mLayout.getChildCount(); i++) {
                View childAt = this.mLayout.getChildAt(i);
                if (!childAt.equals(getDraggedOutBubble())) {
                    PointF expandedBubbleXY = this.mPositioner.getExpandedBubbleXY(i, this.mBubbleStackView.getState());
                    PhysicsAnimationLayout.PhysicsPropertyAnimator animationForChild = animationForChild(childAt);
                    animationForChild.mPathAnimator = null;
                    animationForChild.property(DynamicAnimation.TRANSLATION_X, expandedBubbleXY.x, new Runnable[0]);
                    animationForChild.translationY(expandedBubbleXY.y, new Runnable[0]);
                    animationForChild.start(new Runnable[0]);
                } else {
                    return;
                }
            }
        }
    }

    public final void updateResources() {
        PhysicsAnimationLayout physicsAnimationLayout = this.mLayout;
        if (physicsAnimationLayout != null) {
            this.mStackOffsetPx = physicsAnimationLayout.getContext().getResources().getDimensionPixelSize(2131165455);
            BubblePositioner bubblePositioner = this.mPositioner;
            Objects.requireNonNull(bubblePositioner);
            this.mBubbleSizePx = bubblePositioner.mBubbleSize;
        }
    }

    public ExpandedAnimationController(BubblePositioner bubblePositioner, BubbleStackView$$ExternalSyntheticLambda16 bubbleStackView$$ExternalSyntheticLambda16, BubbleStackView bubbleStackView) {
        this.mPositioner = bubblePositioner;
        updateResources();
        this.mOnBubbleAnimatedOutAction = bubbleStackView$$ExternalSyntheticLambda16;
        this.mCollapsePoint = this.mPositioner.getDefaultStartPosition();
        this.mBubbleStackView = bubbleStackView;
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    public final void onActiveControllerForLayout(PhysicsAnimationLayout physicsAnimationLayout) {
        updateResources();
        this.mLayout.setVisibility(0);
        animationsForChildrenFromIndex(OneHandedDisplayAreaOrganizer$$ExternalSyntheticLambda0.INSTANCE$1).startAll(new Runnable[0]);
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    public final void onChildRemoved(View view, ExecutorUtils$$ExternalSyntheticLambda1 executorUtils$$ExternalSyntheticLambda1) {
        if (view.equals(getDraggedOutBubble())) {
            this.mMagnetizedBubbleDraggingOut = null;
            executorUtils$$ExternalSyntheticLambda1.run();
            this.mOnBubbleAnimatedOutAction.run();
        } else {
            Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
            PhysicsAnimator instance = PhysicsAnimator.Companion.getInstance(view);
            instance.spring(DynamicAnimation.ALPHA, 0.0f);
            instance.spring(DynamicAnimation.SCALE_X, 0.0f, 0.0f, this.mAnimateOutSpringConfig);
            instance.spring(DynamicAnimation.SCALE_Y, 0.0f, 0.0f, this.mAnimateOutSpringConfig);
            instance.withEndActions(executorUtils$$ExternalSyntheticLambda1, this.mOnBubbleAnimatedOutAction);
            instance.start();
        }
        updateBubblePositions();
    }
}
