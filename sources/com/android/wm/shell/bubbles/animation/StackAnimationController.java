package com.android.wm.shell.bubbles.animation;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.keyguard.KeyguardStatusView$$ExternalSyntheticLambda0;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda1;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.bubbles.Bubble$$ExternalSyntheticLambda1;
import com.android.wm.shell.bubbles.BubblePositioner;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda16;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda18;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda30;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
import com.android.wm.shell.common.ExecutorUtils$$ExternalSyntheticLambda1;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.google.android.collect.Sets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.IntSupplier;
import kotlin.jvm.functions.Function1;
/* loaded from: classes.dex */
public final class StackAnimationController extends PhysicsAnimationLayout.PhysicsAnimationController {
    public IntSupplier mBubbleCountSupplier;
    public int mBubbleOffscreen;
    public int mBubblePaddingTop;
    public int mBubbleSize;
    public int mElevation;
    public FloatingContentCoordinator mFloatingContentCoordinator;
    public AnonymousClass2 mMagnetizedStack;
    public int mMaxBubbles;
    public Runnable mOnBubbleAnimatedOutAction;
    public Runnable mOnStackAnimationFinished;
    public BubblePositioner mPositioner;
    public float mStackOffset;
    public float mSwapAnimationOffset;
    public final PhysicsAnimator.SpringConfig mAnimateOutSpringConfig = new PhysicsAnimator.SpringConfig(700.0f, 1.0f);
    public PointF mStackPosition = new PointF(-1.0f, -1.0f);
    public Rect mAnimatingToBounds = new Rect();
    public boolean mStackMovedToStartPosition = false;
    public float mPreImeY = -1.4E-45f;
    public HashMap<DynamicAnimation.ViewProperty, DynamicAnimation> mStackPositionAnimations = new HashMap<>();
    public boolean mIsMovingFromFlinging = false;
    public boolean mFirstBubbleSpringingToTouch = false;
    public boolean mSpringToTouchOnNextMotionEvent = false;
    public final AnonymousClass1 mStackFloatingContent = new AnonymousClass1();

    /* renamed from: com.android.wm.shell.bubbles.animation.StackAnimationController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements FloatingContentCoordinator.FloatingContent {
        public final Rect mFloatingBoundsOnScreen = new Rect();

        public AnonymousClass1() {
        }

        @Override // com.android.wm.shell.common.FloatingContentCoordinator.FloatingContent
        public final Rect getFloatingBoundsOnScreen() {
            if (!StackAnimationController.this.mAnimatingToBounds.isEmpty()) {
                return StackAnimationController.this.mAnimatingToBounds;
            }
            if (StackAnimationController.this.mLayout.getChildCount() > 0) {
                Rect rect = this.mFloatingBoundsOnScreen;
                StackAnimationController stackAnimationController = StackAnimationController.this;
                PointF pointF = stackAnimationController.mStackPosition;
                float f = pointF.x;
                float f2 = pointF.y;
                int i = stackAnimationController.mBubbleSize;
                rect.set((int) f, (int) f2, ((int) f) + i, ((int) f2) + i + stackAnimationController.mBubblePaddingTop);
            } else {
                this.mFloatingBoundsOnScreen.setEmpty();
            }
            return this.mFloatingBoundsOnScreen;
        }

        @Override // com.android.wm.shell.common.FloatingContentCoordinator.FloatingContent
        public final void moveToBounds(Rect rect) {
            StackAnimationController.this.springStack(rect.left, rect.top, 700.0f);
        }

        @Override // com.android.wm.shell.common.FloatingContentCoordinator.FloatingContent
        public final Rect getAllowedFloatingBoundsRegion() {
            Rect floatingBoundsOnScreen = getFloatingBoundsOnScreen();
            Rect rect = new Rect();
            StackAnimationController.this.getAllowableStackPositionRegion().roundOut(rect);
            rect.right = floatingBoundsOnScreen.width() + rect.right;
            rect.bottom = floatingBoundsOnScreen.height() + rect.bottom;
            return rect;
        }
    }

    /* loaded from: classes.dex */
    public class StackPositionProperty extends FloatPropertyCompat<StackAnimationController> {
        public final DynamicAnimation.ViewProperty mProperty;

        public StackPositionProperty(DynamicAnimation.ViewProperty viewProperty) {
            viewProperty.toString();
            this.mProperty = viewProperty;
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(StackAnimationController stackAnimationController) {
            if (StackAnimationController.this.mLayout.getChildCount() > 0) {
                return this.mProperty.getValue(StackAnimationController.this.mLayout.getChildAt(0));
            }
            return 0.0f;
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(StackAnimationController stackAnimationController, float f) {
            StackAnimationController.this.moveFirstBubbleWithStackFollowing(this.mProperty, f);
        }
    }

    public final float flingStackThenSpringToEdge(float f, float f2, float f3) {
        boolean z;
        boolean z2;
        float f4;
        float f5;
        if (f - (this.mBubbleSize / 2) < this.mLayout.getWidth() / 2) {
            z = true;
        } else {
            z = false;
        }
        if (!z ? f2 >= -750.0f : f2 >= 750.0f) {
            z2 = false;
        } else {
            z2 = true;
        }
        RectF allowableStackPositionRegion = getAllowableStackPositionRegion();
        if (z2) {
            f4 = allowableStackPositionRegion.left;
        } else {
            f4 = allowableStackPositionRegion.right;
        }
        PhysicsAnimationLayout physicsAnimationLayout = this.mLayout;
        if (!(physicsAnimationLayout == null || physicsAnimationLayout.getChildCount() == 0)) {
            ContentResolver contentResolver = this.mLayout.getContext().getContentResolver();
            float f6 = Settings.Secure.getFloat(contentResolver, "bubble_stiffness", 700.0f);
            float f7 = Settings.Secure.getFloat(contentResolver, "bubble_damping", 0.85f);
            float f8 = Settings.Secure.getFloat(contentResolver, "bubble_friction", 1.9f);
            float f9 = 4.2f * f8;
            float f10 = (f4 - f) * f9;
            notifyFloatingCoordinatorStackAnimatingTo(f4, Math.min(allowableStackPositionRegion.bottom, Math.max(allowableStackPositionRegion.top, (f3 / f9) + this.mStackPosition.y)));
            if (z2) {
                f5 = Math.min(f10, f2);
            } else {
                f5 = Math.max(f10, f2);
            }
            DynamicAnimation.AnonymousClass1 r1 = DynamicAnimation.TRANSLATION_X;
            SpringForce springForce = new SpringForce();
            springForce.setStiffness(f6);
            springForce.setDampingRatio(f7);
            flingThenSpringFirstBubbleWithStackFollowing(r1, f5, f8, springForce, Float.valueOf(f4));
            DynamicAnimation.AnonymousClass2 r12 = DynamicAnimation.TRANSLATION_Y;
            SpringForce springForce2 = new SpringForce();
            springForce2.setStiffness(f6);
            springForce2.setDampingRatio(f7);
            flingThenSpringFirstBubbleWithStackFollowing(r12, f3, f8, springForce2, null);
            this.mFirstBubbleSpringingToTouch = false;
            this.mIsMovingFromFlinging = true;
        }
        return f4;
    }

    public final void flingThenSpringFirstBubbleWithStackFollowing(final DynamicAnimation.ViewProperty viewProperty, float f, float f2, final SpringForce springForce, final Float f3) {
        float f4;
        final float f5;
        final float f6;
        if (isActiveController()) {
            Log.d("Bubbs.StackCtrl", String.format("Flinging %s.", PhysicsAnimationLayout.getReadablePropertyName(viewProperty)));
            StackPositionProperty stackPositionProperty = new StackPositionProperty(viewProperty);
            if (this.mLayout.getChildCount() > 0) {
                f4 = stackPositionProperty.mProperty.getValue(this.mLayout.getChildAt(0));
            } else {
                f4 = 0.0f;
            }
            RectF allowableStackPositionRegion = getAllowableStackPositionRegion();
            DynamicAnimation.AnonymousClass1 r4 = DynamicAnimation.TRANSLATION_X;
            if (viewProperty.equals(r4)) {
                f5 = allowableStackPositionRegion.left;
            } else {
                f5 = allowableStackPositionRegion.top;
            }
            if (viewProperty.equals(r4)) {
                f6 = allowableStackPositionRegion.right;
            } else {
                f6 = allowableStackPositionRegion.bottom;
            }
            FlingAnimation flingAnimation = new FlingAnimation(this, stackPositionProperty);
            if (f2 > 0.0f) {
                FlingAnimation.DragForce dragForce = flingAnimation.mFlingForce;
                Objects.requireNonNull(dragForce);
                dragForce.mFriction = (-4.2f) * f2;
                flingAnimation.mVelocity = f;
                flingAnimation.mMinValue = Math.min(f4, f5);
                flingAnimation.mMaxValue = Math.max(f4, f6);
                flingAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: com.android.wm.shell.bubbles.animation.StackAnimationController$$ExternalSyntheticLambda0
                    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f7, float f8) {
                        float f9;
                        StackAnimationController stackAnimationController = StackAnimationController.this;
                        DynamicAnimation.ViewProperty viewProperty2 = viewProperty;
                        SpringForce springForce2 = springForce;
                        Float f10 = f3;
                        float f11 = f5;
                        float f12 = f6;
                        if (!z) {
                            stackAnimationController.mPositioner.setRestingPosition(stackAnimationController.mStackPosition);
                            if (f10 != null) {
                                f9 = f10.floatValue();
                            } else {
                                f9 = Math.max(f11, Math.min(f12, f7));
                            }
                            stackAnimationController.springFirstBubbleWithStackFollowing(viewProperty2, springForce2, f8, f9, new Runnable[0]);
                            return;
                        }
                        Objects.requireNonNull(stackAnimationController);
                    }
                });
                cancelStackPositionAnimation(viewProperty);
                this.mStackPositionAnimations.put(viewProperty, flingAnimation);
                flingAnimation.start();
                return;
            }
            throw new IllegalArgumentException("Friction must be positive");
        }
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    public final HashSet getAnimatedProperties() {
        return Sets.newHashSet(new DynamicAnimation.ViewProperty[]{DynamicAnimation.TRANSLATION_X, DynamicAnimation.TRANSLATION_Y, DynamicAnimation.ALPHA, DynamicAnimation.SCALE_X, DynamicAnimation.SCALE_Y});
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    public final void onChildReordered() {
    }

    public final void setStackPosition(PointF pointF) {
        Log.d("Bubbs.StackCtrl", String.format("Setting position to (%f, %f).", Float.valueOf(pointF.x), Float.valueOf(pointF.y)));
        this.mStackPosition.set(pointF.x, pointF.y);
        this.mPositioner.setRestingPosition(this.mStackPosition);
        if (isActiveController()) {
            PhysicsAnimationLayout physicsAnimationLayout = this.mLayout;
            DynamicAnimation.AnonymousClass1 r2 = DynamicAnimation.TRANSLATION_X;
            DynamicAnimation.AnonymousClass2 r5 = DynamicAnimation.TRANSLATION_Y;
            physicsAnimationLayout.cancelAllAnimationsOfProperties(r2, r5);
            cancelStackPositionAnimation(r2);
            cancelStackPositionAnimation(r5);
            this.mLayout.mEndActionForProperty.remove(r2);
            this.mLayout.mEndActionForProperty.remove(r5);
            float offsetForChainedPropertyAnimation = getOffsetForChainedPropertyAnimation(r2, 0);
            float offsetForChainedPropertyAnimation2 = getOffsetForChainedPropertyAnimation(r5, 0);
            for (int i = 0; i < this.mLayout.getChildCount(); i++) {
                float min = Math.min(i, 1);
                this.mLayout.getChildAt(i).setTranslationX((min * offsetForChainedPropertyAnimation) + pointF.x);
                this.mLayout.getChildAt(i).setTranslationY((min * offsetForChainedPropertyAnimation2) + pointF.y);
            }
        }
    }

    public final void cancelStackPositionAnimation(DynamicAnimation.ViewProperty viewProperty) {
        if (this.mStackPositionAnimations.containsKey(viewProperty)) {
            this.mStackPositionAnimations.get(viewProperty).cancel();
        }
    }

    public final RectF getAllowableStackPositionRegion() {
        int i;
        float f;
        int i2;
        BubblePositioner bubblePositioner = this.mPositioner;
        Objects.requireNonNull(bubblePositioner);
        RectF rectF = new RectF(bubblePositioner.mPositionRect);
        BubblePositioner bubblePositioner2 = this.mPositioner;
        Objects.requireNonNull(bubblePositioner2);
        if (bubblePositioner2.mImeVisible) {
            i = bubblePositioner2.mImeHeight;
        } else {
            i = 0;
        }
        if (this.mBubbleCountSupplier.getAsInt() > 1) {
            f = this.mBubblePaddingTop + this.mStackOffset;
        } else {
            f = this.mBubblePaddingTop;
        }
        rectF.left = rectF.left - this.mBubbleOffscreen;
        rectF.top += this.mBubblePaddingTop;
        float f2 = rectF.right;
        int i3 = this.mBubbleSize;
        rectF.right = f2 + (i2 - i3);
        rectF.bottom -= (i + f) + i3;
        return rectF;
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    public final int getNextAnimationInChain(DynamicAnimation.ViewProperty viewProperty, int i) {
        if (viewProperty.equals(DynamicAnimation.TRANSLATION_X) || viewProperty.equals(DynamicAnimation.TRANSLATION_Y)) {
            return i + 1;
        }
        return -1;
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    public final float getOffsetForChainedPropertyAnimation(DynamicAnimation.ViewProperty viewProperty, int i) {
        if (!viewProperty.equals(DynamicAnimation.TRANSLATION_Y) || isStackStuckToTarget() || i > 1) {
            return 0.0f;
        }
        return this.mStackOffset;
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    public final SpringForce getSpringForce() {
        float f = Settings.Secure.getFloat(this.mLayout.getContext().getContentResolver(), "bubble_damping", 0.9f);
        SpringForce springForce = new SpringForce();
        springForce.setDampingRatio(f);
        springForce.setStiffness(800.0f);
        return springForce;
    }

    public final boolean isStackOnLeftSide() {
        if (this.mLayout == null || !this.mStackMovedToStartPosition) {
            return true;
        }
        BubblePositioner bubblePositioner = this.mPositioner;
        PointF pointF = this.mStackPosition;
        if (pointF == null) {
            pointF = bubblePositioner.getRestingPosition();
        } else {
            Objects.requireNonNull(bubblePositioner);
        }
        if ((bubblePositioner.mBubbleSize / 2) + ((int) pointF.x) < bubblePositioner.mScreenRect.width() / 2) {
            return true;
        }
        return false;
    }

    public final boolean isStackStuckToTarget() {
        AnonymousClass2 r0 = this.mMagnetizedStack;
        if (r0 == null || !r0.getObjectStuckToTarget()) {
            return false;
        }
        return true;
    }

    public final void moveFirstBubbleWithStackFollowing(DynamicAnimation.ViewProperty viewProperty, float f) {
        if (viewProperty.equals(DynamicAnimation.TRANSLATION_X)) {
            this.mStackPosition.x = f;
        } else if (viewProperty.equals(DynamicAnimation.TRANSLATION_Y)) {
            this.mStackPosition.y = f;
        }
        if (this.mLayout.getChildCount() > 0) {
            viewProperty.setValue(this.mLayout.getChildAt(0), f);
            if (this.mLayout.getChildCount() > 1) {
                float offsetForChainedPropertyAnimation = getOffsetForChainedPropertyAnimation(viewProperty, 0) + f;
                PhysicsAnimationLayout.PhysicsPropertyAnimator animationForChild = animationForChild(this.mLayout.getChildAt(1));
                animationForChild.property(viewProperty, offsetForChainedPropertyAnimation, new Runnable[0]);
                animationForChild.start(new Runnable[0]);
            }
        }
    }

    public final void notifyFloatingCoordinatorStackAnimatingTo(float f, float f2) {
        Rect floatingBoundsOnScreen = this.mStackFloatingContent.getFloatingBoundsOnScreen();
        floatingBoundsOnScreen.offsetTo((int) f, (int) f2);
        this.mAnimatingToBounds = floatingBoundsOnScreen;
        this.mFloatingContentCoordinator.onContentMoved(this.mStackFloatingContent);
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    public final void onChildRemoved(View view, ExecutorUtils$$ExternalSyntheticLambda1 executorUtils$$ExternalSyntheticLambda1) {
        Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
        PhysicsAnimator instance = PhysicsAnimator.Companion.getInstance(view);
        instance.spring(DynamicAnimation.ALPHA, 0.0f);
        instance.spring(DynamicAnimation.SCALE_X, 0.0f, 0.0f, this.mAnimateOutSpringConfig);
        instance.spring(DynamicAnimation.SCALE_Y, 0.0f, 0.0f, this.mAnimateOutSpringConfig);
        instance.withEndActions(executorUtils$$ExternalSyntheticLambda1, this.mOnBubbleAnimatedOutAction);
        instance.start();
        if (this.mBubbleCountSupplier.getAsInt() > 0) {
            PhysicsAnimationLayout.PhysicsPropertyAnimator animationForChild = animationForChild(this.mLayout.getChildAt(0));
            animationForChild.mPathAnimator = null;
            animationForChild.property(DynamicAnimation.TRANSLATION_X, this.mStackPosition.x, new Runnable[0]);
            animationForChild.start(new Runnable[0]);
            return;
        }
        BubblePositioner bubblePositioner = this.mPositioner;
        bubblePositioner.setRestingPosition(bubblePositioner.getRestingPosition());
        FloatingContentCoordinator floatingContentCoordinator = this.mFloatingContentCoordinator;
        AnonymousClass1 r3 = this.mStackFloatingContent;
        Objects.requireNonNull(floatingContentCoordinator);
        floatingContentCoordinator.allContentBounds.remove(r3);
    }

    public final void springFirstBubbleWithStackFollowing(DynamicAnimation.ViewProperty viewProperty, SpringForce springForce, float f, float f2, final Runnable... runnableArr) {
        if (this.mLayout.getChildCount() != 0 && isActiveController()) {
            Log.d("Bubbs.StackCtrl", String.format("Springing %s to final position %f.", PhysicsAnimationLayout.getReadablePropertyName(viewProperty), Float.valueOf(f2)));
            final boolean z = this.mSpringToTouchOnNextMotionEvent;
            SpringAnimation springAnimation = new SpringAnimation(this, new StackPositionProperty(viewProperty));
            springAnimation.mSpring = springForce;
            springAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: com.android.wm.shell.bubbles.animation.StackAnimationController$$ExternalSyntheticLambda1
                @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z2, float f3, float f4) {
                    StackAnimationController stackAnimationController = StackAnimationController.this;
                    boolean z3 = z;
                    Runnable[] runnableArr2 = runnableArr;
                    Objects.requireNonNull(stackAnimationController);
                    if (!z3) {
                        stackAnimationController.mPositioner.setRestingPosition(stackAnimationController.mStackPosition);
                    }
                    Runnable runnable = stackAnimationController.mOnStackAnimationFinished;
                    if (runnable != null) {
                        runnable.run();
                    }
                    if (runnableArr2 != null) {
                        for (Runnable runnable2 : runnableArr2) {
                            runnable2.run();
                        }
                    }
                }
            });
            springAnimation.mVelocity = f;
            cancelStackPositionAnimation(viewProperty);
            this.mStackPositionAnimations.put(viewProperty, springAnimation);
            springAnimation.animateToFinalPosition(f2);
        }
    }

    public final void springStack(float f, float f2, float f3) {
        notifyFloatingCoordinatorStackAnimatingTo(f, f2);
        DynamicAnimation.AnonymousClass1 r2 = DynamicAnimation.TRANSLATION_X;
        SpringForce springForce = new SpringForce();
        springForce.setStiffness(f3);
        springForce.setDampingRatio(0.85f);
        springFirstBubbleWithStackFollowing(r2, springForce, 0.0f, f, new Runnable[0]);
        DynamicAnimation.AnonymousClass2 r10 = DynamicAnimation.TRANSLATION_Y;
        SpringForce springForce2 = new SpringForce();
        springForce2.setStiffness(f3);
        springForce2.setDampingRatio(0.85f);
        springFirstBubbleWithStackFollowing(r10, springForce2, 0.0f, f2, new Runnable[0]);
    }

    public StackAnimationController(FloatingContentCoordinator floatingContentCoordinator, BubbleStackView$$ExternalSyntheticLambda30 bubbleStackView$$ExternalSyntheticLambda30, BubbleStackView$$ExternalSyntheticLambda16 bubbleStackView$$ExternalSyntheticLambda16, BubbleStackView$$ExternalSyntheticLambda18 bubbleStackView$$ExternalSyntheticLambda18, BubblePositioner bubblePositioner) {
        this.mFloatingContentCoordinator = floatingContentCoordinator;
        this.mBubbleCountSupplier = bubbleStackView$$ExternalSyntheticLambda30;
        this.mOnBubbleAnimatedOutAction = bubbleStackView$$ExternalSyntheticLambda16;
        this.mOnStackAnimationFinished = bubbleStackView$$ExternalSyntheticLambda18;
        this.mPositioner = bubblePositioner;
    }

    public final void animateInBubble(View view, int i) {
        float f;
        if (isActiveController()) {
            float offsetForChainedPropertyAnimation = getOffsetForChainedPropertyAnimation(DynamicAnimation.TRANSLATION_Y, 0);
            PointF pointF = this.mStackPosition;
            float f2 = (offsetForChainedPropertyAnimation * i) + pointF.y;
            float f3 = pointF.x;
            if (this.mPositioner.showBubblesVertically()) {
                view.setTranslationY(f2);
                if (isStackOnLeftSide()) {
                    f = f3 - 100.0f;
                } else {
                    f = f3 + 100.0f;
                }
                view.setTranslationX(f);
            } else {
                view.setTranslationX(this.mStackPosition.x);
                view.setTranslationY(100.0f + f2);
            }
            view.setScaleX(0.5f);
            view.setScaleY(0.5f);
            view.setAlpha(0.0f);
            ViewPropertyAnimator withEndAction = view.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(300L).withEndAction(new LockIconViewController$$ExternalSyntheticLambda1(view, 9));
            view.setTag(2131428691, withEndAction);
            if (this.mPositioner.showBubblesVertically()) {
                withEndAction.translationX(f3);
            } else {
                withEndAction.translationY(f2);
            }
        }
    }

    public final void moveToFinalIndex(View view, int i, Runnable runnable) {
        view.setTag(2131428691, view.animate().translationY((Math.min(i, 1) * this.mStackOffset) + this.mStackPosition.y).setDuration(300L).withEndAction(new Bubble$$ExternalSyntheticLambda1(view, runnable, 3)));
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    public final void onActiveControllerForLayout(PhysicsAnimationLayout physicsAnimationLayout) {
        Resources resources = physicsAnimationLayout.getResources();
        this.mStackOffset = resources.getDimensionPixelSize(2131165455);
        this.mSwapAnimationOffset = resources.getDimensionPixelSize(2131165457);
        this.mMaxBubbles = resources.getInteger(2131492874);
        this.mElevation = resources.getDimensionPixelSize(2131165417);
        BubblePositioner bubblePositioner = this.mPositioner;
        Objects.requireNonNull(bubblePositioner);
        this.mBubbleSize = bubblePositioner.mBubbleSize;
        this.mBubblePaddingTop = resources.getDimensionPixelSize(2131165446);
        this.mBubbleOffscreen = resources.getDimensionPixelSize(2131165454);
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    public final void onChildAdded(View view, int i) {
        if (!isStackStuckToTarget()) {
            if (this.mBubbleCountSupplier.getAsInt() == 1) {
                this.mLayout.setVisibility(4);
                this.mLayout.post(new KeyguardStatusView$$ExternalSyntheticLambda0(this, 8));
            } else if (!this.mStackMovedToStartPosition || this.mLayout.indexOfChild(view) != 0) {
                view.setAlpha(1.0f);
                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
            } else {
                animateInBubble(view, i);
            }
        }
    }
}
