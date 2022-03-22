package com.android.wm.shell.bubbles;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.Choreographer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ContrastColorUtil;
import com.android.internal.util.FrameworkStatsLog;
import com.android.keyguard.KeyguardStatusView$$ExternalSyntheticLambda0;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda8;
import com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda0;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda3;
import com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda13;
import com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda2;
import com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda10;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda1;
import com.android.wm.shell.TaskView;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda5;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.bubbles.BadgedImageView;
import com.android.wm.shell.bubbles.Bubble;
import com.android.wm.shell.bubbles.BubbleLogger;
import com.android.wm.shell.bubbles.BubbleOverflowContainerView;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.bubbles.animation.AnimatableScaleMatrix;
import com.android.wm.shell.bubbles.animation.ExpandedAnimationController;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
import com.android.wm.shell.bubbles.animation.StackAnimationController;
import com.android.wm.shell.common.DismissCircleView;
import com.android.wm.shell.common.ExecutorUtils$$ExternalSyntheticLambda0;
import com.android.wm.shell.common.ExecutorUtils$$ExternalSyntheticLambda1;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import kotlin.jvm.functions.Function1;
/* loaded from: classes.dex */
public final class BubbleStackView extends FrameLayout implements ViewTreeObserver.OnComputeInternalInsetsListener {
    @VisibleForTesting
    public static final int FLYOUT_HIDE_AFTER = 5000;
    public ExecutorUtils$$ExternalSyntheticLambda0 mAfterFlyoutHidden;
    public final BubbleStackView$$ExternalSyntheticLambda9 mAfterFlyoutTransitionSpring;
    public WifiEntry$$ExternalSyntheticLambda1 mAnimateInFlyout;
    public SurfaceControl.ScreenshotHardwareBuffer mAnimatingOutBubbleBuffer;
    public final ValueAnimator mAnimatingOutSurfaceAlphaAnimator;
    public FrameLayout mAnimatingOutSurfaceContainer;
    public boolean mAnimatingOutSurfaceReady;
    public SurfaceView mAnimatingOutSurfaceView;
    public PhysicsAnimationLayout mBubbleContainer;
    public final BubbleController mBubbleController;
    public final BubbleData mBubbleData;
    public int mBubbleElevation;
    public BubbleOverflow mBubbleOverflow;
    public int mBubbleSize;
    public int mBubbleTouchPadding;
    public int mCornerRadius;
    public BubbleStackView$$ExternalSyntheticLambda25 mDelayedAnimation;
    public final ShellExecutor mDelayedAnimationExecutor;
    public final ValueAnimator mDismissBubbleAnimator;
    public DismissView mDismissView;
    public Bubbles.BubbleExpandListener mExpandListener;
    public ExpandedAnimationController mExpandedAnimationController;
    public BubbleViewProvider mExpandedBubble;
    public final ValueAnimator mExpandedViewAlphaAnimator;
    public FrameLayout mExpandedViewContainer;
    public int mExpandedViewPadding;
    public BubbleFlyoutView mFlyout;
    public final AnonymousClass3 mFlyoutCollapseProperty;
    public final SpringAnimation mFlyoutTransitionSpring;
    public boolean mIsExpanded;
    public MagnetizedObject.MagneticTarget mMagneticTarget;
    public MagnetizedObject<?> mMagnetizedObject;
    public ManageEducationView mManageEduView;
    public ViewGroup mManageMenu;
    public View mManageMenuScrim;
    public ImageView mManageSettingsIcon;
    public TextView mManageSettingsText;
    public BubbleStackView$$ExternalSyntheticLambda7 mOrientationChangedListener;
    public BubblePositioner mPositioner;
    public RelativeStackPosition mRelativeStackPositionBeforeRotation;
    public View mScrim;
    public StackAnimationController mStackAnimationController;
    public StackEducationView mStackEduView;
    public final SurfaceSynchronizer mSurfaceSynchronizer;
    public Consumer<String> mUnbubbleConversationCallback;
    public View mViewBeingDismissed;
    public static final PhysicsAnimator.SpringConfig FLYOUT_IME_ANIMATION_SPRING_CONFIG = new PhysicsAnimator.SpringConfig(200.0f, 0.9f);
    public static final AnonymousClass1 DEFAULT_SURFACE_SYNCHRONIZER = new SurfaceSynchronizer() { // from class: com.android.wm.shell.bubbles.BubbleStackView.1

        /* renamed from: com.android.wm.shell.bubbles.BubbleStackView$1$1 */
        /* loaded from: classes.dex */
        public final class Choreographer$FrameCallbackC00071 implements Choreographer.FrameCallback {
            public int mFrameWait = 2;
            public final /* synthetic */ Runnable val$callback;

            public Choreographer$FrameCallbackC00071(Runnable runnable) {
                this.val$callback = runnable;
            }

            @Override // android.view.Choreographer.FrameCallback
            public final void doFrame(long j) {
                int i = this.mFrameWait - 1;
                this.mFrameWait = i;
                if (i > 0) {
                    Choreographer.getInstance().postFrameCallback(this);
                } else {
                    this.val$callback.run();
                }
            }
        }
    };
    public final PhysicsAnimator.SpringConfig mScaleInSpringConfig = new PhysicsAnimator.SpringConfig(300.0f, 0.9f);
    public final PhysicsAnimator.SpringConfig mScaleOutSpringConfig = new PhysicsAnimator.SpringConfig(900.0f, 1.0f);
    public final PhysicsAnimator.SpringConfig mTranslateSpringConfig = new PhysicsAnimator.SpringConfig(50.0f, 1.0f);
    public StackViewState mStackViewState = new StackViewState();
    public final AnimatableScaleMatrix mExpandedViewContainerMatrix = new AnimatableScaleMatrix();
    public VolumeDialogImpl$$ExternalSyntheticLambda10 mHideFlyout = new VolumeDialogImpl$$ExternalSyntheticLambda10(this, 7);
    public BubbleViewProvider mBubbleToExpandAfterFlyoutCollapse = null;
    public boolean mStackOnLeftOrWillBe = true;
    public boolean mIsGestureInProgress = false;
    public boolean mTemporarilyInvisible = false;
    public boolean mIsDraggingStack = false;
    public boolean mExpandedViewTemporarilyHidden = false;
    public int mPointerIndexDown = -1;
    public boolean mViewUpdatedRequested = false;
    public boolean mIsExpansionAnimating = false;
    public boolean mIsBubbleSwitchAnimating = false;
    public Rect mTempRect = new Rect();
    public final List<Rect> mSystemGestureExclusionRects = Collections.singletonList(new Rect());
    public AnonymousClass2 mViewUpdater = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.wm.shell.bubbles.BubbleStackView.2
        {
            BubbleStackView.this = this;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public final boolean onPreDraw() {
            BubbleStackView.this.getViewTreeObserver().removeOnPreDrawListener(BubbleStackView.this.mViewUpdater);
            BubbleStackView.this.updateExpandedView();
            BubbleStackView.this.mViewUpdatedRequested = false;
            return true;
        }
    };
    public BubbleStackView$$ExternalSyntheticLambda8 mSystemGestureExcludeUpdater = new ViewTreeObserver.OnDrawListener() { // from class: com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda8
        @Override // android.view.ViewTreeObserver.OnDrawListener
        public final void onDraw() {
            BubbleStackView bubbleStackView = BubbleStackView.this;
            Objects.requireNonNull(bubbleStackView);
            Rect rect = bubbleStackView.mSystemGestureExclusionRects.get(0);
            if (bubbleStackView.getBubbleCount() > 0) {
                View childAt = bubbleStackView.mBubbleContainer.getChildAt(0);
                rect.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
                rect.offset((int) (childAt.getTranslationX() + 0.5f), (int) (childAt.getTranslationY() + 0.5f));
                bubbleStackView.mBubbleContainer.setSystemGestureExclusionRects(bubbleStackView.mSystemGestureExclusionRects);
                return;
            }
            rect.setEmpty();
            bubbleStackView.mBubbleContainer.setSystemGestureExclusionRects(Collections.emptyList());
        }
    };
    public float mFlyoutDragDeltaX = 0.0f;
    public final AnonymousClass4 mIndividualBubbleMagnetListener = new MagnetizedObject.MagnetListener() { // from class: com.android.wm.shell.bubbles.BubbleStackView.4
        {
            BubbleStackView.this = this;
        }

        @Override // com.android.wm.shell.common.magnetictarget.MagnetizedObject.MagnetListener
        public final void onReleasedInTarget() {
            if (BubbleStackView.this.mExpandedAnimationController.getDraggedOutBubble() != null) {
                ExpandedAnimationController expandedAnimationController = BubbleStackView.this.mExpandedAnimationController;
                View draggedOutBubble = expandedAnimationController.getDraggedOutBubble();
                float height = BubbleStackView.this.mDismissView.getHeight();
                TaskView$$ExternalSyntheticLambda5 taskView$$ExternalSyntheticLambda5 = new TaskView$$ExternalSyntheticLambda5(BubbleStackView.this, 5);
                if (draggedOutBubble != null) {
                    PhysicsAnimationLayout.PhysicsPropertyAnimator animationForChild = expandedAnimationController.animationForChild(draggedOutBubble);
                    animationForChild.mStiffness = 10000.0f;
                    animationForChild.property(DynamicAnimation.SCALE_X, 0.0f, new Runnable[0]);
                    animationForChild.property(DynamicAnimation.SCALE_Y, 0.0f, new Runnable[0]);
                    animationForChild.translationY(draggedOutBubble.getTranslationY() + height, new Runnable[0]);
                    animationForChild.property(DynamicAnimation.ALPHA, 0.0f, taskView$$ExternalSyntheticLambda5);
                    animationForChild.start(new Runnable[0]);
                    expandedAnimationController.updateBubblePositions();
                }
                BubbleStackView.this.mDismissView.hide();
            }
        }

        @Override // com.android.wm.shell.common.magnetictarget.MagnetizedObject.MagnetListener
        public final void onStuckToTarget() {
            if (BubbleStackView.this.mExpandedAnimationController.getDraggedOutBubble() != null) {
                BubbleStackView bubbleStackView = BubbleStackView.this;
                BubbleStackView.m151$$Nest$manimateDismissBubble(bubbleStackView, bubbleStackView.mExpandedAnimationController.getDraggedOutBubble(), true);
            }
        }

        @Override // com.android.wm.shell.common.magnetictarget.MagnetizedObject.MagnetListener
        public final void onUnstuckFromTarget(float f, float f2, boolean z) {
            if (BubbleStackView.this.mExpandedAnimationController.getDraggedOutBubble() != null) {
                BubbleStackView bubbleStackView = BubbleStackView.this;
                BubbleStackView.m151$$Nest$manimateDismissBubble(bubbleStackView, bubbleStackView.mExpandedAnimationController.getDraggedOutBubble(), false);
                if (z) {
                    ExpandedAnimationController expandedAnimationController = BubbleStackView.this.mExpandedAnimationController;
                    expandedAnimationController.snapBubbleBack(expandedAnimationController.getDraggedOutBubble(), f, f2);
                    BubbleStackView.this.mDismissView.hide();
                    return;
                }
                ExpandedAnimationController expandedAnimationController2 = BubbleStackView.this.mExpandedAnimationController;
                Objects.requireNonNull(expandedAnimationController2);
                expandedAnimationController2.mSpringToTouchOnNextMotionEvent = true;
            }
        }
    };
    public final AnonymousClass5 mStackMagnetListener = new AnonymousClass5();
    public AnonymousClass6 mBubbleClickListener = new View.OnClickListener() { // from class: com.android.wm.shell.bubbles.BubbleStackView.6
        {
            BubbleStackView.this = this;
        }

        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            Bubble bubbleWithView;
            BubbleStackView bubbleStackView = BubbleStackView.this;
            bubbleStackView.mIsDraggingStack = false;
            if (!bubbleStackView.mIsExpansionAnimating && !bubbleStackView.mIsBubbleSwitchAnimating && (bubbleWithView = bubbleStackView.mBubbleData.getBubbleWithView(view)) != null) {
                boolean equals = bubbleWithView.mKey.equals(BubbleStackView.this.mExpandedBubble.getKey());
                BubbleStackView bubbleStackView2 = BubbleStackView.this;
                Objects.requireNonNull(bubbleStackView2);
                if (bubbleStackView2.mIsExpanded) {
                    ExpandedAnimationController expandedAnimationController = BubbleStackView.this.mExpandedAnimationController;
                    Objects.requireNonNull(expandedAnimationController);
                    expandedAnimationController.mBubbleDraggedOutEnough = false;
                    expandedAnimationController.mMagnetizedBubbleDraggingOut = null;
                    expandedAnimationController.updateBubblePositions();
                }
                BubbleStackView bubbleStackView3 = BubbleStackView.this;
                Objects.requireNonNull(bubbleStackView3);
                if (!bubbleStackView3.mIsExpanded || equals) {
                    if (!BubbleStackView.this.maybeShowStackEdu()) {
                        BubbleStackView bubbleStackView4 = BubbleStackView.this;
                        if (!bubbleStackView4.mShowedUserEducationInTouchListenerActive) {
                            BubbleData bubbleData = bubbleStackView4.mBubbleData;
                            Objects.requireNonNull(bubbleData);
                            bubbleData.setExpanded(!bubbleData.mExpanded);
                        }
                    }
                    BubbleStackView.this.mShowedUserEducationInTouchListenerActive = false;
                    return;
                }
                BubbleData bubbleData2 = BubbleStackView.this.mBubbleData;
                Objects.requireNonNull(bubbleData2);
                if (bubbleWithView != bubbleData2.mSelectedBubble) {
                    BubbleStackView.this.mBubbleData.setSelectedBubble(bubbleWithView);
                } else {
                    BubbleStackView.this.setSelectedBubble(bubbleWithView);
                }
            }
        }
    };
    public AnonymousClass7 mBubbleTouchListener = new RelativeTouchListener() { // from class: com.android.wm.shell.bubbles.BubbleStackView.7
        {
            BubbleStackView.this = this;
        }

        /* JADX WARN: Type inference failed for: r11v0, types: [com.android.wm.shell.bubbles.animation.StackAnimationController$2, com.android.wm.shell.common.magnetictarget.MagnetizedObject] */
        /* JADX WARN: Type inference failed for: r4v12, types: [com.android.wm.shell.common.magnetictarget.MagnetizedObject, com.android.wm.shell.bubbles.animation.ExpandedAnimationController$1] */
        /* JADX WARN: Unknown variable types count: 2 */
        @Override // com.android.wm.shell.bubbles.RelativeTouchListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onDown(final android.view.View r13, android.view.MotionEvent r14) {
            /*
                Method dump skipped, instructions count: 393
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.bubbles.BubbleStackView.AnonymousClass7.onDown(android.view.View, android.view.MotionEvent):void");
        }

        @Override // com.android.wm.shell.bubbles.RelativeTouchListener
        public final void onMove(View view, MotionEvent motionEvent, float f, float f2, float f3, float f4) {
            BubbleViewProvider bubbleViewProvider;
            BubbleViewProvider bubbleViewProvider2;
            BubbleStackView bubbleStackView = BubbleStackView.this;
            if (!bubbleStackView.mIsExpansionAnimating) {
                Objects.requireNonNull(bubbleStackView.mPositioner);
                BubbleStackView bubbleStackView2 = BubbleStackView.this;
                if (!bubbleStackView2.mShowedUserEducationInTouchListenerActive) {
                    DismissView dismissView = bubbleStackView2.mDismissView;
                    Objects.requireNonNull(dismissView);
                    boolean z = true;
                    if (!dismissView.isShowing) {
                        dismissView.isShowing = true;
                        dismissView.setVisibility(0);
                        Drawable background = dismissView.getBackground();
                        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.TransitionDrawable");
                        ((TransitionDrawable) background).startTransition(dismissView.DISMISS_SCRIM_FADE_MS);
                        dismissView.animator.cancel();
                        PhysicsAnimator<DismissCircleView> physicsAnimator = dismissView.animator;
                        DynamicAnimation.AnonymousClass2 r5 = DynamicAnimation.TRANSLATION_Y;
                        PhysicsAnimator.SpringConfig springConfig = dismissView.spring;
                        Objects.requireNonNull(physicsAnimator);
                        physicsAnimator.spring(r5, 0.0f, 0.0f, springConfig);
                        physicsAnimator.start();
                    }
                    BubbleStackView bubbleStackView3 = BubbleStackView.this;
                    if (bubbleStackView3.mIsExpanded && (bubbleViewProvider = bubbleStackView3.mExpandedBubble) != null && view.equals(bubbleViewProvider.getIconView$1())) {
                        final BubbleStackView bubbleStackView4 = BubbleStackView.this;
                        Objects.requireNonNull(bubbleStackView4);
                        if (!(bubbleStackView4.mExpandedViewTemporarilyHidden || (bubbleViewProvider2 = bubbleStackView4.mExpandedBubble) == null || bubbleViewProvider2.getExpandedView() == null)) {
                            bubbleStackView4.mExpandedViewTemporarilyHidden = true;
                            AnimatableScaleMatrix animatableScaleMatrix = bubbleStackView4.mExpandedViewContainerMatrix;
                            Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
                            PhysicsAnimator instance = PhysicsAnimator.Companion.getInstance(animatableScaleMatrix);
                            instance.spring(AnimatableScaleMatrix.SCALE_X, 449.99997f, 0.0f, bubbleStackView4.mScaleOutSpringConfig);
                            instance.spring(AnimatableScaleMatrix.SCALE_Y, 449.99997f, 0.0f, bubbleStackView4.mScaleOutSpringConfig);
                            instance.updateListeners.add(new PhysicsAnimator.UpdateListener() { // from class: com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda10
                                @Override // com.android.wm.shell.animation.PhysicsAnimator.UpdateListener
                                public final void onAnimationUpdateForProperty(Object obj) {
                                    BubbleStackView bubbleStackView5 = BubbleStackView.this;
                                    AnimatableScaleMatrix animatableScaleMatrix2 = (AnimatableScaleMatrix) obj;
                                    Objects.requireNonNull(bubbleStackView5);
                                    bubbleStackView5.mExpandedViewContainer.setAnimationMatrix(bubbleStackView5.mExpandedViewContainerMatrix);
                                }
                            });
                            instance.start();
                            bubbleStackView4.mExpandedViewAlphaAnimator.reverse();
                        }
                    }
                    if (!BubbleStackView.m153$$Nest$mpassEventToMagnetizedObject(BubbleStackView.this, motionEvent)) {
                        BubbleStackView.this.updateBubbleShadows(true);
                        BubbleData bubbleData = BubbleStackView.this.mBubbleData;
                        Objects.requireNonNull(bubbleData);
                        if (!bubbleData.mExpanded) {
                            Objects.requireNonNull(BubbleStackView.this.mPositioner);
                            if (BubbleStackView.this.isStackEduShowing()) {
                                BubbleStackView.this.mStackEduView.hide(false);
                            }
                            StackAnimationController stackAnimationController = BubbleStackView.this.mStackAnimationController;
                            float f5 = f + f3;
                            float f6 = f2 + f4;
                            Objects.requireNonNull(stackAnimationController);
                            if (stackAnimationController.mSpringToTouchOnNextMotionEvent) {
                                stackAnimationController.springStack(f5, f6, 12000.0f);
                                stackAnimationController.mSpringToTouchOnNextMotionEvent = false;
                                stackAnimationController.mFirstBubbleSpringingToTouch = true;
                            } else if (stackAnimationController.mFirstBubbleSpringingToTouch) {
                                SpringAnimation springAnimation = (SpringAnimation) stackAnimationController.mStackPositionAnimations.get(DynamicAnimation.TRANSLATION_X);
                                SpringAnimation springAnimation2 = (SpringAnimation) stackAnimationController.mStackPositionAnimations.get(DynamicAnimation.TRANSLATION_Y);
                                Objects.requireNonNull(springAnimation);
                                if (!springAnimation.mRunning) {
                                    Objects.requireNonNull(springAnimation2);
                                    if (!springAnimation2.mRunning) {
                                        stackAnimationController.mFirstBubbleSpringingToTouch = false;
                                    }
                                }
                                springAnimation.animateToFinalPosition(f5);
                                springAnimation2.animateToFinalPosition(f6);
                            }
                            if (!stackAnimationController.mFirstBubbleSpringingToTouch && !stackAnimationController.isStackStuckToTarget()) {
                                stackAnimationController.mAnimatingToBounds.setEmpty();
                                stackAnimationController.mPreImeY = -1.4E-45f;
                                stackAnimationController.moveFirstBubbleWithStackFollowing(DynamicAnimation.TRANSLATION_X, f5);
                                stackAnimationController.moveFirstBubbleWithStackFollowing(DynamicAnimation.TRANSLATION_Y, f6);
                                stackAnimationController.mIsMovingFromFlinging = false;
                                return;
                            }
                            return;
                        }
                        ExpandedAnimationController expandedAnimationController = BubbleStackView.this.mExpandedAnimationController;
                        float f7 = f + f3;
                        float f8 = f2 + f4;
                        Objects.requireNonNull(expandedAnimationController);
                        ExpandedAnimationController.AnonymousClass1 r10 = expandedAnimationController.mMagnetizedBubbleDraggingOut;
                        if (r10 != null) {
                            if (expandedAnimationController.mSpringToTouchOnNextMotionEvent) {
                                PhysicsAnimationLayout.PhysicsPropertyAnimator animationForChild = expandedAnimationController.animationForChild((View) r10.underlyingObject);
                                animationForChild.mPathAnimator = null;
                                animationForChild.property(DynamicAnimation.TRANSLATION_X, f7, new Runnable[0]);
                                animationForChild.translationY(f8, new Runnable[0]);
                                animationForChild.mStiffness = 10000.0f;
                                animationForChild.start(new Runnable[0]);
                                expandedAnimationController.mSpringToTouchOnNextMotionEvent = false;
                                expandedAnimationController.mSpringingBubbleToTouch = true;
                            } else if (expandedAnimationController.mSpringingBubbleToTouch) {
                                PhysicsAnimationLayout physicsAnimationLayout = expandedAnimationController.mLayout;
                                DynamicAnimation.AnonymousClass1 r1 = DynamicAnimation.TRANSLATION_X;
                                DynamicAnimation.ViewProperty[] viewPropertyArr = {r1, DynamicAnimation.TRANSLATION_Y};
                                Objects.requireNonNull(physicsAnimationLayout);
                                if (PhysicsAnimationLayout.arePropertiesAnimatingOnView(view, viewPropertyArr)) {
                                    ExpandedAnimationController.AnonymousClass1 r102 = expandedAnimationController.mMagnetizedBubbleDraggingOut;
                                    Objects.requireNonNull(r102);
                                    PhysicsAnimationLayout.PhysicsPropertyAnimator animationForChild2 = expandedAnimationController.animationForChild((View) r102.underlyingObject);
                                    animationForChild2.mPathAnimator = null;
                                    animationForChild2.property(r1, f7, new Runnable[0]);
                                    animationForChild2.translationY(f8, new Runnable[0]);
                                    animationForChild2.mStiffness = 10000.0f;
                                    animationForChild2.start(new Runnable[0]);
                                } else {
                                    expandedAnimationController.mSpringingBubbleToTouch = false;
                                }
                            }
                            if (!expandedAnimationController.mSpringingBubbleToTouch && !expandedAnimationController.mMagnetizedBubbleDraggingOut.getObjectStuckToTarget()) {
                                view.setTranslationX(f7);
                                view.setTranslationY(f8);
                            }
                            float expandedViewYTopAligned = expandedAnimationController.mPositioner.getExpandedViewYTopAligned();
                            float f9 = expandedAnimationController.mBubbleSizePx;
                            if (f8 <= expandedViewYTopAligned + f9 && f8 >= expandedViewYTopAligned - f9) {
                                z = false;
                            }
                            if (z != expandedAnimationController.mBubbleDraggedOutEnough) {
                                expandedAnimationController.updateBubblePositions();
                                expandedAnimationController.mBubbleDraggedOutEnough = z;
                            }
                        }
                    }
                }
            }
        }

        @Override // com.android.wm.shell.bubbles.RelativeTouchListener
        public final void onUp(View view, MotionEvent motionEvent, float f, float f2, float f3, float f4) {
            boolean z;
            BubbleStackView bubbleStackView = BubbleStackView.this;
            if (!bubbleStackView.mIsExpansionAnimating) {
                Objects.requireNonNull(bubbleStackView.mPositioner);
                BubbleStackView bubbleStackView2 = BubbleStackView.this;
                if (bubbleStackView2.mShowedUserEducationInTouchListenerActive) {
                    bubbleStackView2.mShowedUserEducationInTouchListenerActive = false;
                    return;
                }
                if (!BubbleStackView.m153$$Nest$mpassEventToMagnetizedObject(bubbleStackView2, motionEvent)) {
                    BubbleData bubbleData = BubbleStackView.this.mBubbleData;
                    Objects.requireNonNull(bubbleData);
                    if (bubbleData.mExpanded) {
                        BubbleStackView.this.mExpandedAnimationController.snapBubbleBack(view, f3, f4);
                        final BubbleStackView bubbleStackView3 = BubbleStackView.this;
                        Objects.requireNonNull(bubbleStackView3);
                        if (bubbleStackView3.mExpandedViewTemporarilyHidden) {
                            bubbleStackView3.mExpandedViewTemporarilyHidden = false;
                            AnimatableScaleMatrix animatableScaleMatrix = bubbleStackView3.mExpandedViewContainerMatrix;
                            Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
                            PhysicsAnimator instance = PhysicsAnimator.Companion.getInstance(animatableScaleMatrix);
                            instance.spring(AnimatableScaleMatrix.SCALE_X, 499.99997f, 0.0f, bubbleStackView3.mScaleOutSpringConfig);
                            instance.spring(AnimatableScaleMatrix.SCALE_Y, 499.99997f, 0.0f, bubbleStackView3.mScaleOutSpringConfig);
                            instance.updateListeners.add(new PhysicsAnimator.UpdateListener() { // from class: com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda11
                                @Override // com.android.wm.shell.animation.PhysicsAnimator.UpdateListener
                                public final void onAnimationUpdateForProperty(Object obj) {
                                    BubbleStackView bubbleStackView4 = BubbleStackView.this;
                                    AnimatableScaleMatrix animatableScaleMatrix2 = (AnimatableScaleMatrix) obj;
                                    Objects.requireNonNull(bubbleStackView4);
                                    bubbleStackView4.mExpandedViewContainer.setAnimationMatrix(bubbleStackView4.mExpandedViewContainerMatrix);
                                }
                            });
                            instance.start();
                            bubbleStackView3.mExpandedViewAlphaAnimator.start();
                        }
                    } else {
                        BubbleStackView bubbleStackView4 = BubbleStackView.this;
                        boolean z2 = bubbleStackView4.mStackOnLeftOrWillBe;
                        int i = (bubbleStackView4.mStackAnimationController.flingStackThenSpringToEdge(f + f2, f3, f4) > 0.0f ? 1 : (bubbleStackView4.mStackAnimationController.flingStackThenSpringToEdge(f + f2, f3, f4) == 0.0f ? 0 : -1));
                        boolean z3 = true;
                        if (i <= 0) {
                            z = true;
                        } else {
                            z = false;
                        }
                        bubbleStackView4.mStackOnLeftOrWillBe = z;
                        BubbleStackView bubbleStackView5 = BubbleStackView.this;
                        if (z2 == bubbleStackView5.mStackOnLeftOrWillBe) {
                            z3 = false;
                        }
                        bubbleStackView5.updateBadges(z3);
                        BubbleStackView.this.logBubbleEvent(null, 7);
                    }
                    BubbleStackView.this.mDismissView.hide();
                }
                BubbleStackView bubbleStackView6 = BubbleStackView.this;
                bubbleStackView6.mIsDraggingStack = false;
                bubbleStackView6.updateTemporarilyInvisibleAnimation(false);
            }
        }
    };
    public AnonymousClass8 mFlyoutClickListener = new View.OnClickListener() { // from class: com.android.wm.shell.bubbles.BubbleStackView.8
        {
            BubbleStackView.this = this;
        }

        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            if (BubbleStackView.this.maybeShowStackEdu()) {
                BubbleStackView.this.mBubbleToExpandAfterFlyoutCollapse = null;
            } else {
                BubbleStackView bubbleStackView = BubbleStackView.this;
                BubbleData bubbleData = bubbleStackView.mBubbleData;
                Objects.requireNonNull(bubbleData);
                bubbleStackView.mBubbleToExpandAfterFlyoutCollapse = bubbleData.mSelectedBubble;
            }
            BubbleStackView bubbleStackView2 = BubbleStackView.this;
            bubbleStackView2.mFlyout.removeCallbacks(bubbleStackView2.mHideFlyout);
            BubbleStackView.this.mHideFlyout.run();
        }
    };
    public AnonymousClass9 mFlyoutTouchListener = new RelativeTouchListener() { // from class: com.android.wm.shell.bubbles.BubbleStackView.9
        {
            BubbleStackView.this = this;
        }

        @Override // com.android.wm.shell.bubbles.RelativeTouchListener
        public final void onDown(View view, MotionEvent motionEvent) {
            BubbleStackView bubbleStackView = BubbleStackView.this;
            bubbleStackView.mFlyout.removeCallbacks(bubbleStackView.mHideFlyout);
        }

        @Override // com.android.wm.shell.bubbles.RelativeTouchListener
        public final void onMove(View view, MotionEvent motionEvent, float f, float f2, float f3, float f4) {
            BubbleStackView.this.setFlyoutStateForDragLength(f3);
        }

        @Override // com.android.wm.shell.bubbles.RelativeTouchListener
        public final void onUp(View view, MotionEvent motionEvent, float f, float f2, float f3, float f4) {
            boolean z;
            boolean z2;
            boolean z3;
            boolean isStackOnLeftSide = BubbleStackView.this.mStackAnimationController.isStackOnLeftSide();
            boolean z4 = true;
            if (!isStackOnLeftSide ? f3 <= 2000.0f : f3 >= -2000.0f) {
                z = false;
            } else {
                z = true;
            }
            if (!isStackOnLeftSide ? f2 <= BubbleStackView.this.mFlyout.getWidth() * 0.25f : f2 >= (-BubbleStackView.this.mFlyout.getWidth()) * 0.25f) {
                z2 = false;
            } else {
                z2 = true;
            }
            if (!isStackOnLeftSide ? f3 >= 0.0f : f3 <= 0.0f) {
                z3 = false;
            } else {
                z3 = true;
            }
            if (!z && (!z2 || z3)) {
                z4 = false;
            }
            BubbleStackView bubbleStackView = BubbleStackView.this;
            bubbleStackView.mFlyout.removeCallbacks(bubbleStackView.mHideFlyout);
            BubbleStackView.this.animateFlyoutCollapsed(z4, f3);
            BubbleStackView.this.maybeShowStackEdu();
        }
    };
    public boolean mShowingManage = false;
    public boolean mShowedUserEducationInTouchListenerActive = false;
    public PhysicsAnimator.SpringConfig mManageSpringConfig = new PhysicsAnimator.SpringConfig(1500.0f, 0.75f);
    public final KeyguardStatusView$$ExternalSyntheticLambda0 mAnimateTemporarilyInvisibleImmediate = new KeyguardStatusView$$ExternalSyntheticLambda0(this, 7);

    /* renamed from: com.android.wm.shell.bubbles.BubbleStackView$5 */
    /* loaded from: classes.dex */
    public class AnonymousClass5 implements MagnetizedObject.MagnetListener {
        public AnonymousClass5() {
            BubbleStackView.this = r1;
        }

        @Override // com.android.wm.shell.common.magnetictarget.MagnetizedObject.MagnetListener
        public final void onReleasedInTarget() {
            BubbleStackView bubbleStackView = BubbleStackView.this;
            final StackAnimationController stackAnimationController = bubbleStackView.mStackAnimationController;
            final float height = bubbleStackView.mDismissView.getHeight();
            KeyguardUpdateMonitor$$ExternalSyntheticLambda8 keyguardUpdateMonitor$$ExternalSyntheticLambda8 = new KeyguardUpdateMonitor$$ExternalSyntheticLambda8(this, 3);
            Objects.requireNonNull(stackAnimationController);
            stackAnimationController.animationsForChildrenFromIndex(new PhysicsAnimationLayout.PhysicsAnimationController.ChildAnimationConfigurator() { // from class: com.android.wm.shell.bubbles.animation.StackAnimationController$$ExternalSyntheticLambda2
                @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController.ChildAnimationConfigurator
                public final void configureAnimationForChildAtIndex(int i, PhysicsAnimationLayout.PhysicsPropertyAnimator physicsPropertyAnimator) {
                    StackAnimationController stackAnimationController2 = StackAnimationController.this;
                    float f = height;
                    Objects.requireNonNull(stackAnimationController2);
                    physicsPropertyAnimator.property(DynamicAnimation.SCALE_X, 0.0f, new Runnable[0]);
                    physicsPropertyAnimator.property(DynamicAnimation.SCALE_Y, 0.0f, new Runnable[0]);
                    physicsPropertyAnimator.property(DynamicAnimation.ALPHA, 0.0f, new Runnable[0]);
                    physicsPropertyAnimator.translationY(stackAnimationController2.mLayout.getChildAt(i).getTranslationY() + f, new Runnable[0]);
                    physicsPropertyAnimator.mStiffness = 10000.0f;
                }
            }).startAll(new Runnable[]{keyguardUpdateMonitor$$ExternalSyntheticLambda8});
            BubbleStackView.this.mDismissView.hide();
        }

        @Override // com.android.wm.shell.common.magnetictarget.MagnetizedObject.MagnetListener
        public final void onStuckToTarget() {
            BubbleStackView bubbleStackView = BubbleStackView.this;
            BubbleStackView.m151$$Nest$manimateDismissBubble(bubbleStackView, bubbleStackView.mBubbleContainer, true);
        }

        @Override // com.android.wm.shell.common.magnetictarget.MagnetizedObject.MagnetListener
        public final void onUnstuckFromTarget(float f, float f2, boolean z) {
            BubbleStackView bubbleStackView = BubbleStackView.this;
            BubbleStackView.m151$$Nest$manimateDismissBubble(bubbleStackView, bubbleStackView.mBubbleContainer, false);
            if (z) {
                StackAnimationController stackAnimationController = BubbleStackView.this.mStackAnimationController;
                Objects.requireNonNull(stackAnimationController);
                stackAnimationController.flingStackThenSpringToEdge(stackAnimationController.mStackPosition.x, f, f2);
                BubbleStackView.this.mDismissView.hide();
                return;
            }
            StackAnimationController stackAnimationController2 = BubbleStackView.this.mStackAnimationController;
            Objects.requireNonNull(stackAnimationController2);
            stackAnimationController2.mSpringToTouchOnNextMotionEvent = true;
        }
    }

    /* loaded from: classes.dex */
    public static class StackViewState {
        public int numberOfBubbles;
        public boolean onLeft;
    }

    /* loaded from: classes.dex */
    public interface SurfaceSynchronizer {
    }

    public final void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        internalInsetsInfo.setTouchableInsets(3);
        this.mTempRect.setEmpty();
        Rect rect = this.mTempRect;
        int i = 0;
        if (isStackEduShowing()) {
            rect.set(0, 0, getWidth(), getHeight());
        } else {
            if (this.mIsExpanded) {
                this.mBubbleContainer.getBoundsOnScreen(rect);
                int i2 = rect.bottom;
                BubblePositioner bubblePositioner = this.mPositioner;
                Objects.requireNonNull(bubblePositioner);
                if (bubblePositioner.mImeVisible) {
                    i = bubblePositioner.mImeHeight;
                }
                rect.bottom = i2 - i;
            } else if (getBubbleCount() > 0 || this.mBubbleData.isShowingOverflow()) {
                this.mBubbleContainer.getChildAt(0).getBoundsOnScreen(rect);
                int i3 = rect.top;
                int i4 = this.mBubbleTouchPadding;
                rect.top = i3 - i4;
                rect.left -= i4;
                rect.right += i4;
                rect.bottom += i4;
            }
            if (this.mFlyout.getVisibility() == 0) {
                Rect rect2 = new Rect();
                this.mFlyout.getBoundsOnScreen(rect2);
                rect.union(rect2);
            }
        }
        internalInsetsInfo.touchableRegion.set(this.mTempRect);
    }

    public final void setSelectedBubble(BubbleViewProvider bubbleViewProvider) {
        BubbleViewProvider bubbleViewProvider2;
        if (bubbleViewProvider == null) {
            BubbleData bubbleData = this.mBubbleData;
            Objects.requireNonNull(bubbleData);
            bubbleData.mShowingOverflow = false;
        } else if (this.mExpandedBubble != bubbleViewProvider) {
            if (bubbleViewProvider.getKey().equals("Overflow")) {
                BubbleData bubbleData2 = this.mBubbleData;
                Objects.requireNonNull(bubbleData2);
                bubbleData2.mShowingOverflow = true;
            } else {
                BubbleData bubbleData3 = this.mBubbleData;
                Objects.requireNonNull(bubbleData3);
                bubbleData3.mShowingOverflow = false;
            }
            if (this.mIsExpanded && this.mIsExpansionAnimating) {
                this.mDelayedAnimationExecutor.removeCallbacks(this.mDelayedAnimation);
                this.mIsExpansionAnimating = false;
                this.mIsBubbleSwitchAnimating = false;
                SurfaceView surfaceView = this.mAnimatingOutSurfaceView;
                Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
                PhysicsAnimator.Companion.getInstance(surfaceView).cancel();
                PhysicsAnimator.Companion.getInstance(this.mExpandedViewContainerMatrix).cancel();
                this.mExpandedViewContainer.setAnimationMatrix(null);
            }
            showManageMenu(false);
            if (!this.mIsExpanded || (bubbleViewProvider2 = this.mExpandedBubble) == null || bubbleViewProvider2.getExpandedView() == null || this.mExpandedViewTemporarilyHidden) {
                showNewlySelectedBubble(bubbleViewProvider);
                return;
            }
            BubbleViewProvider bubbleViewProvider3 = this.mExpandedBubble;
            if (!(bubbleViewProvider3 == null || bubbleViewProvider3.getExpandedView() == null)) {
                BubbleExpandedView expandedView = this.mExpandedBubble.getExpandedView();
                Objects.requireNonNull(expandedView);
                TaskView taskView = expandedView.mTaskView;
                if (taskView != null) {
                    taskView.setZOrderedOnTop(true, true);
                }
            }
            try {
                screenshotAnimatingOutBubbleIntoSurface(new BubbleStackView$$ExternalSyntheticLambda27(this, bubbleViewProvider));
            } catch (Exception e) {
                showNewlySelectedBubble(bubbleViewProvider);
                e.printStackTrace();
            }
        }
    }

    /* loaded from: classes.dex */
    public static class RelativeStackPosition {
        public boolean mOnLeft;
        public float mVerticalOffsetPercent;

        public RelativeStackPosition(PointF pointF, RectF rectF) {
            boolean z;
            if (pointF.x < rectF.width() / 2.0f) {
                z = true;
            } else {
                z = false;
            }
            this.mOnLeft = z;
            this.mVerticalOffsetPercent = Math.max(0.0f, Math.min(1.0f, (pointF.y - rectF.top) / rectF.height()));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v3, types: [com.android.wm.shell.bubbles.BubbleStackView$2] */
    /* JADX WARN: Type inference failed for: r10v4, types: [com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda8] */
    /* JADX WARN: Type inference failed for: r10v5, types: [com.android.wm.shell.bubbles.BubbleStackView$3, androidx.dynamicanimation.animation.FloatPropertyCompat] */
    /* JADX WARN: Type inference failed for: r12v0, types: [androidx.dynamicanimation.animation.DynamicAnimation$OnAnimationEndListener, com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda9] */
    /* JADX WARN: Type inference failed for: r13v0, types: [com.android.wm.shell.bubbles.BubbleStackView$4] */
    /* JADX WARN: Type inference failed for: r13v2, types: [com.android.wm.shell.bubbles.BubbleStackView$6] */
    /* JADX WARN: Type inference failed for: r13v3, types: [com.android.wm.shell.bubbles.BubbleStackView$7] */
    /* JADX WARN: Type inference failed for: r13v4, types: [com.android.wm.shell.bubbles.BubbleStackView$8] */
    /* JADX WARN: Type inference failed for: r13v5, types: [com.android.wm.shell.bubbles.BubbleStackView$9] */
    /* JADX WARN: Type inference failed for: r14v5, types: [com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda30] */
    /* JADX WARN: Type inference failed for: r1v31, types: [com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda7] */
    /* JADX WARN: Unknown variable types count: 2 */
    @android.annotation.SuppressLint({"ClickableViewAccessibility"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public BubbleStackView(android.content.Context r23, com.android.wm.shell.bubbles.BubbleController r24, com.android.wm.shell.bubbles.BubbleData r25, com.android.wm.shell.bubbles.BubbleStackView.SurfaceSynchronizer r26, com.android.wm.shell.common.FloatingContentCoordinator r27, com.android.wm.shell.common.ShellExecutor r28) {
        /*
            Method dump skipped, instructions count: 898
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.bubbles.BubbleStackView.<init>(android.content.Context, com.android.wm.shell.bubbles.BubbleController, com.android.wm.shell.bubbles.BubbleData, com.android.wm.shell.bubbles.BubbleStackView$SurfaceSynchronizer, com.android.wm.shell.common.FloatingContentCoordinator, com.android.wm.shell.common.ShellExecutor):void");
    }

    public final void animateFlyoutCollapsed(boolean z, float f) {
        float f2;
        float f3;
        boolean isStackOnLeftSide = this.mStackAnimationController.isStackOnLeftSide();
        SpringAnimation springAnimation = this.mFlyoutTransitionSpring;
        Objects.requireNonNull(springAnimation);
        SpringForce springForce = springAnimation.mSpring;
        if (this.mBubbleToExpandAfterFlyoutCollapse != null) {
            f2 = 1500.0f;
        } else {
            f2 = 200.0f;
        }
        springForce.setStiffness(f2);
        SpringAnimation springAnimation2 = this.mFlyoutTransitionSpring;
        float f4 = this.mFlyoutDragDeltaX;
        Objects.requireNonNull(springAnimation2);
        springAnimation2.mValue = f4;
        springAnimation2.mStartValueIsSet = true;
        springAnimation2.mVelocity = f;
        if (z) {
            int width = this.mFlyout.getWidth();
            if (isStackOnLeftSide) {
                width = -width;
            }
            f3 = width;
        } else {
            f3 = 0.0f;
        }
        springAnimation2.animateToFinalPosition(f3);
    }

    @VisibleForTesting
    public void animateInFlyoutForBubble(Bubble bubble) {
        boolean z;
        BadgedImageView.SuppressionFlag suppressionFlag = BadgedImageView.SuppressionFlag.FLYOUT_VISIBLE;
        Objects.requireNonNull(bubble);
        Bubble.FlyoutMessage flyoutMessage = bubble.mFlyoutMessage;
        BadgedImageView badgedImageView = bubble.mIconView;
        if (flyoutMessage == null || flyoutMessage.message == null || !bubble.showFlyout() || isStackEduShowing() || this.mIsExpanded || this.mIsExpansionAnimating || this.mIsGestureInProgress || this.mBubbleToExpandAfterFlyoutCollapse != null || badgedImageView == null) {
            if (!(badgedImageView == null || this.mFlyout.getVisibility() == 0)) {
                badgedImageView.removeDotSuppressionFlag(suppressionFlag);
            }
            z = false;
        } else {
            z = true;
        }
        if (z) {
            this.mFlyoutDragDeltaX = 0.0f;
            this.mFlyout.removeCallbacks(this.mAnimateInFlyout);
            ExecutorUtils$$ExternalSyntheticLambda0 executorUtils$$ExternalSyntheticLambda0 = this.mAfterFlyoutHidden;
            if (executorUtils$$ExternalSyntheticLambda0 != null) {
                executorUtils$$ExternalSyntheticLambda0.run();
                this.mAfterFlyoutHidden = null;
            }
            this.mAfterFlyoutHidden = new ExecutorUtils$$ExternalSyntheticLambda0(this, bubble, 4);
            BadgedImageView badgedImageView2 = bubble.mIconView;
            Objects.requireNonNull(badgedImageView2);
            if (badgedImageView2.mDotSuppressionFlags.add(suppressionFlag)) {
                badgedImageView2.updateDotVisibility(false);
            }
            post(new ExecutorUtils$$ExternalSyntheticLambda1(this, bubble, 4));
            this.mFlyout.removeCallbacks(this.mHideFlyout);
            this.mFlyout.postDelayed(this.mHideFlyout, 5000L);
            logBubbleEvent(bubble, 16);
        }
    }

    public final int getBubbleCount() {
        return this.mBubbleContainer.getChildCount() - 1;
    }

    public final int getBubbleIndex(BubbleViewProvider bubbleViewProvider) {
        if (bubbleViewProvider == null) {
            return 0;
        }
        return this.mBubbleContainer.indexOfChild(bubbleViewProvider.getIconView$1());
    }

    public final StackViewState getState() {
        this.mStackViewState.numberOfBubbles = this.mBubbleContainer.getChildCount();
        StackViewState stackViewState = this.mStackViewState;
        getBubbleIndex(this.mExpandedBubble);
        Objects.requireNonNull(stackViewState);
        StackViewState stackViewState2 = this.mStackViewState;
        stackViewState2.onLeft = this.mStackOnLeftOrWillBe;
        return stackViewState2;
    }

    public final void hideCurrentInputMethod() {
        BubblePositioner bubblePositioner = this.mPositioner;
        Objects.requireNonNull(bubblePositioner);
        bubblePositioner.mImeVisible = false;
        bubblePositioner.mImeHeight = 0;
        BubbleController bubbleController = this.mBubbleController;
        Objects.requireNonNull(bubbleController);
        try {
            bubbleController.mBarService.hideCurrentInputMethodForBubbles();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public final void hideFlyoutImmediate() {
        this.mFlyout.removeCallbacks(this.mAnimateInFlyout);
        ExecutorUtils$$ExternalSyntheticLambda0 executorUtils$$ExternalSyntheticLambda0 = this.mAfterFlyoutHidden;
        if (executorUtils$$ExternalSyntheticLambda0 != null) {
            executorUtils$$ExternalSyntheticLambda0.run();
            this.mAfterFlyoutHidden = null;
        }
        this.mFlyout.removeCallbacks(this.mAnimateInFlyout);
        this.mFlyout.removeCallbacks(this.mHideFlyout);
        BubbleFlyoutView bubbleFlyoutView = this.mFlyout;
        Objects.requireNonNull(bubbleFlyoutView);
        Runnable runnable = bubbleFlyoutView.mOnHide;
        if (runnable != null) {
            runnable.run();
            bubbleFlyoutView.mOnHide = null;
        }
        bubbleFlyoutView.setVisibility(8);
    }

    public final boolean isStackEduShowing() {
        StackEducationView stackEducationView = this.mStackEduView;
        if (stackEducationView == null || stackEducationView.getVisibility() != 0) {
            return false;
        }
        return true;
    }

    public final void logBubbleEvent(BubbleViewProvider bubbleViewProvider, int i) {
        String str;
        if (bubbleViewProvider == null || !(bubbleViewProvider instanceof Bubble)) {
            str = "null";
        } else {
            str = ((Bubble) bubbleViewProvider).mPackageName;
        }
        BubbleData bubbleData = this.mBubbleData;
        int bubbleCount = getBubbleCount();
        int bubbleIndex = getBubbleIndex(bubbleViewProvider);
        StackAnimationController stackAnimationController = this.mStackAnimationController;
        Objects.requireNonNull(stackAnimationController);
        float f = stackAnimationController.mStackPosition.x;
        BubblePositioner bubblePositioner = this.mPositioner;
        Objects.requireNonNull(bubblePositioner);
        BigDecimal bigDecimal = new BigDecimal(f / bubblePositioner.mPositionRect.width());
        RoundingMode roundingMode = RoundingMode.CEILING;
        float floatValue = bigDecimal.setScale(4, RoundingMode.HALF_UP).floatValue();
        StackAnimationController stackAnimationController2 = this.mStackAnimationController;
        Objects.requireNonNull(stackAnimationController2);
        float f2 = stackAnimationController2.mStackPosition.y;
        BubblePositioner bubblePositioner2 = this.mPositioner;
        Objects.requireNonNull(bubblePositioner2);
        BigDecimal bigDecimal2 = new BigDecimal(f2 / bubblePositioner2.mPositionRect.height());
        RoundingMode roundingMode2 = RoundingMode.CEILING;
        float floatValue2 = bigDecimal2.setScale(4, RoundingMode.HALF_UP).floatValue();
        if (bubbleViewProvider == null) {
            Objects.requireNonNull(bubbleData.mLogger);
            FrameworkStatsLog.write(149, str, (String) null, 0, 0, bubbleCount, i, floatValue, floatValue2, false, false, false);
            return;
        }
        Objects.requireNonNull(bubbleData);
        if (!bubbleViewProvider.getKey().equals("Overflow")) {
            Bubble bubble = (Bubble) bubbleViewProvider;
            Objects.requireNonNull(bubbleData.mLogger);
            FrameworkStatsLog.write(149, str, bubble.mChannelId, bubble.mNotificationId, bubbleIndex, bubbleCount, i, floatValue, floatValue2, bubble.showInShade(), false, false);
        } else if (i == 3) {
            BubbleLogger bubbleLogger = bubbleData.mLogger;
            int i2 = bubbleData.mCurrentUserId;
            Objects.requireNonNull(bubbleLogger);
            bubbleLogger.mUiEventLogger.log(BubbleLogger.Event.BUBBLE_OVERFLOW_SELECTED, i2, str);
        }
    }

    public final void releaseAnimatingOutBubbleBuffer() {
        SurfaceControl.ScreenshotHardwareBuffer screenshotHardwareBuffer = this.mAnimatingOutBubbleBuffer;
        if (screenshotHardwareBuffer != null && !screenshotHardwareBuffer.getHardwareBuffer().isClosed()) {
            this.mAnimatingOutBubbleBuffer.getHardwareBuffer().close();
        }
    }

    public final void requestUpdate() {
        if (!this.mViewUpdatedRequested && !this.mIsExpansionAnimating) {
            this.mViewUpdatedRequested = true;
            getViewTreeObserver().addOnPreDrawListener(this.mViewUpdater);
            invalidate();
        }
    }

    public final void screenshotAnimatingOutBubbleIntoSurface(BubbleStackView$$ExternalSyntheticLambda27 bubbleStackView$$ExternalSyntheticLambda27) {
        BubbleViewProvider bubbleViewProvider;
        int[] iArr;
        if (!this.mIsExpanded || (bubbleViewProvider = this.mExpandedBubble) == null || bubbleViewProvider.getExpandedView() == null) {
            bubbleStackView$$ExternalSyntheticLambda27.accept(Boolean.FALSE);
            return;
        }
        BubbleExpandedView expandedView = this.mExpandedBubble.getExpandedView();
        if (this.mAnimatingOutBubbleBuffer != null) {
            releaseAnimatingOutBubbleBuffer();
        }
        try {
            this.mAnimatingOutBubbleBuffer = expandedView.snapshotActivitySurface();
        } catch (Exception e) {
            Log.wtf("Bubbles", e);
            bubbleStackView$$ExternalSyntheticLambda27.accept(Boolean.FALSE);
        }
        SurfaceControl.ScreenshotHardwareBuffer screenshotHardwareBuffer = this.mAnimatingOutBubbleBuffer;
        if (screenshotHardwareBuffer == null || screenshotHardwareBuffer.getHardwareBuffer() == null) {
            bubbleStackView$$ExternalSyntheticLambda27.accept(Boolean.FALSE);
            return;
        }
        FrameLayout frameLayout = this.mAnimatingOutSurfaceContainer;
        Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
        PhysicsAnimator.Companion.getInstance(frameLayout).cancel();
        this.mAnimatingOutSurfaceContainer.setScaleX(1.0f);
        this.mAnimatingOutSurfaceContainer.setScaleY(1.0f);
        this.mAnimatingOutSurfaceContainer.setTranslationX(this.mExpandedViewContainer.getPaddingLeft());
        this.mAnimatingOutSurfaceContainer.setTranslationY(0.0f);
        BubbleExpandedView expandedView2 = this.mExpandedBubble.getExpandedView();
        Objects.requireNonNull(expandedView2);
        if (expandedView2.mIsOverflow) {
            iArr = expandedView2.mOverflowView.getLocationOnScreen();
        } else {
            TaskView taskView = expandedView2.mTaskView;
            if (taskView != null) {
                iArr = taskView.getLocationOnScreen();
            } else {
                iArr = new int[]{0, 0};
            }
        }
        this.mAnimatingOutSurfaceContainer.setTranslationY(iArr[1] - this.mAnimatingOutSurfaceView.getLocationOnScreen()[1]);
        this.mAnimatingOutSurfaceView.getLayoutParams().width = this.mAnimatingOutBubbleBuffer.getHardwareBuffer().getWidth();
        this.mAnimatingOutSurfaceView.getLayoutParams().height = this.mAnimatingOutBubbleBuffer.getHardwareBuffer().getHeight();
        this.mAnimatingOutSurfaceView.requestLayout();
        post(new BubbleStackView$$ExternalSyntheticLambda22(this, bubbleStackView$$ExternalSyntheticLambda27, 0));
    }

    public final void setBubbleSuppressed(Bubble bubble, boolean z) {
        if (z) {
            this.mBubbleContainer.removeViewAt(getBubbleIndex(bubble));
            updateExpandedView();
            return;
        }
        Objects.requireNonNull(bubble);
        BadgedImageView badgedImageView = bubble.mIconView;
        if (badgedImageView != null) {
            if (badgedImageView.getParent() != null) {
                Log.e("Bubbles", "Bubble is already added to parent. Can't unsuppress: " + bubble);
                return;
            }
            int indexOf = this.mBubbleData.getBubbles().indexOf(bubble);
            PhysicsAnimationLayout physicsAnimationLayout = this.mBubbleContainer;
            BadgedImageView badgedImageView2 = bubble.mIconView;
            BubblePositioner bubblePositioner = this.mPositioner;
            Objects.requireNonNull(bubblePositioner);
            int i = bubblePositioner.mBubbleSize;
            BubblePositioner bubblePositioner2 = this.mPositioner;
            Objects.requireNonNull(bubblePositioner2);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(i, bubblePositioner2.mBubbleSize);
            Objects.requireNonNull(physicsAnimationLayout);
            physicsAnimationLayout.addViewInternal(badgedImageView2, indexOf, layoutParams, false);
            updateBubbleShadows(false);
            requestUpdate();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v9, types: [com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda25, java.lang.Runnable] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setExpanded(final boolean r12) {
        /*
            Method dump skipped, instructions count: 764
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.bubbles.BubbleStackView.setExpanded(boolean):void");
    }

    public final void setFlyoutStateForDragLength(float f) {
        boolean z;
        float f2;
        int i;
        if (this.mFlyout.getWidth() > 0) {
            boolean isStackOnLeftSide = this.mStackAnimationController.isStackOnLeftSide();
            this.mFlyoutDragDeltaX = f;
            if (isStackOnLeftSide) {
                f = -f;
            }
            float width = f / this.mFlyout.getWidth();
            float f3 = 0.0f;
            this.mFlyout.setCollapsePercent(Math.min(1.0f, Math.max(0.0f, width)));
            int i2 = (width > 0.0f ? 1 : (width == 0.0f ? 0 : -1));
            if (i2 < 0 || width > 1.0f) {
                int i3 = (width > 1.0f ? 1 : (width == 1.0f ? 0 : -1));
                boolean z2 = false;
                int i4 = 1;
                if (i3 > 0) {
                    z = true;
                } else {
                    z = false;
                }
                if ((isStackOnLeftSide && i3 > 0) || (!isStackOnLeftSide && i2 < 0)) {
                    z2 = true;
                }
                if (z) {
                    f2 = width - 1.0f;
                } else {
                    f2 = width * (-1.0f);
                }
                if (z2) {
                    i = -1;
                } else {
                    i = 1;
                }
                float f4 = f2 * i;
                float width2 = this.mFlyout.getWidth();
                if (z) {
                    i4 = 2;
                }
                f3 = (width2 / (8.0f / i4)) * f4;
            }
            BubbleFlyoutView bubbleFlyoutView = this.mFlyout;
            Objects.requireNonNull(bubbleFlyoutView);
            bubbleFlyoutView.setTranslationX(bubbleFlyoutView.mRestingTranslationX + f3);
        }
    }

    public final void setUpDismissView() {
        DismissView dismissView = this.mDismissView;
        if (dismissView != null) {
            removeView(dismissView);
        }
        this.mDismissView = new DismissView(getContext());
        int dimensionPixelSize = getResources().getDimensionPixelSize(2131165417);
        addView(this.mDismissView);
        this.mDismissView.setElevation(dimensionPixelSize);
        int i = Settings.Secure.getInt(getContext().getContentResolver(), "bubble_dismiss_radius", this.mBubbleSize * 2);
        DismissView dismissView2 = this.mDismissView;
        Objects.requireNonNull(dismissView2);
        this.mMagneticTarget = new MagnetizedObject.MagneticTarget(dismissView2.circle, i);
        this.mBubbleContainer.bringToFront();
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public final void setUpFlyout() {
        BubbleFlyoutView bubbleFlyoutView = this.mFlyout;
        if (bubbleFlyoutView != null) {
            removeView(bubbleFlyoutView);
        }
        BubbleFlyoutView bubbleFlyoutView2 = new BubbleFlyoutView(getContext(), this.mPositioner);
        this.mFlyout = bubbleFlyoutView2;
        bubbleFlyoutView2.setVisibility(8);
        this.mFlyout.setOnClickListener(this.mFlyoutClickListener);
        this.mFlyout.setOnTouchListener(this.mFlyoutTouchListener);
        addView(this.mFlyout, new FrameLayout.LayoutParams(-2, -2));
    }

    public final void setUpManageMenu() {
        ViewGroup viewGroup = this.mManageMenu;
        if (viewGroup != null) {
            removeView(viewGroup);
        }
        ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(getContext()).inflate(2131624021, (ViewGroup) this, false);
        this.mManageMenu = viewGroup2;
        viewGroup2.setVisibility(4);
        PhysicsAnimator.getInstance(this.mManageMenu).defaultSpring = this.mManageSpringConfig;
        this.mManageMenu.setOutlineProvider(new ViewOutlineProvider() { // from class: com.android.wm.shell.bubbles.BubbleStackView.13
            {
                BubbleStackView.this = this;
            }

            @Override // android.view.ViewOutlineProvider
            public final void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), BubbleStackView.this.mCornerRadius);
            }
        });
        this.mManageMenu.setClipToOutline(true);
        this.mManageMenu.findViewById(2131427621).setOnClickListener(new BubbleStackView$$ExternalSyntheticLambda4(this, 0));
        this.mManageMenu.findViewById(2131427622).setOnClickListener(new BubbleStackView$$ExternalSyntheticLambda5(this, 0));
        this.mManageMenu.findViewById(2131427623).setOnClickListener(new BubbleStackView$$ExternalSyntheticLambda6(this, 0));
        this.mManageSettingsIcon = (ImageView) this.mManageMenu.findViewById(2131427624);
        this.mManageSettingsText = (TextView) this.mManageMenu.findViewById(2131427625);
        this.mManageMenu.setLayoutDirection(3);
        addView(this.mManageMenu);
    }

    public final void setupLocalMenu(AccessibilityNodeInfo accessibilityNodeInfo) {
        Resources resources = ((FrameLayout) this).mContext.getResources();
        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427440, resources.getString(2131952060)));
        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427441, resources.getString(2131952061)));
        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427431, resources.getString(2131952058)));
        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427432, resources.getString(2131952059)));
        accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_DISMISS);
        if (this.mIsExpanded) {
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_COLLAPSE);
        } else {
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_EXPAND);
        }
    }

    @VisibleForTesting
    public void showManageMenu(boolean z) {
        PathInterpolator pathInterpolator;
        float f;
        boolean z2;
        float f2;
        int i;
        ViewGroup viewGroup;
        Rect rect;
        Bubble bubbleInStackWithKey;
        this.mShowingManage = z;
        BubbleViewProvider bubbleViewProvider = this.mExpandedBubble;
        if (bubbleViewProvider == null || bubbleViewProvider.getExpandedView() == null) {
            this.mManageMenu.setVisibility(4);
            this.mManageMenuScrim.setVisibility(4);
            BubbleController bubbleController = this.mBubbleController;
            Objects.requireNonNull(bubbleController);
            BubblesManager.AnonymousClass5 r9 = (BubblesManager.AnonymousClass5) bubbleController.mSysuiProxy;
            Objects.requireNonNull(r9);
            r9.val$sysuiMainExecutor.execute(new ScrimView$$ExternalSyntheticLambda2(r9, r9.val$sysUiState, false, 2));
            return;
        }
        if (z) {
            this.mManageMenuScrim.setVisibility(0);
            this.mManageMenuScrim.setTranslationZ(this.mManageMenu.getElevation() - 1.0f);
        }
        BubbleStackView$$ExternalSyntheticLambda23 bubbleStackView$$ExternalSyntheticLambda23 = new BubbleStackView$$ExternalSyntheticLambda23(this, z, 0);
        BubbleController bubbleController2 = this.mBubbleController;
        Objects.requireNonNull(bubbleController2);
        BubblesManager.AnonymousClass5 r4 = (BubblesManager.AnonymousClass5) bubbleController2.mSysuiProxy;
        Objects.requireNonNull(r4);
        r4.val$sysuiMainExecutor.execute(new ScrimView$$ExternalSyntheticLambda2(r4, r4.val$sysUiState, z, 2));
        ViewPropertyAnimator animate = this.mManageMenuScrim.animate();
        if (z) {
            pathInterpolator = Interpolators.ALPHA_IN;
        } else {
            pathInterpolator = Interpolators.ALPHA_OUT;
        }
        ViewPropertyAnimator interpolator = animate.setInterpolator(pathInterpolator);
        if (z) {
            f = 0.6f;
        } else {
            f = 0.0f;
        }
        interpolator.alpha(f).withEndAction(bubbleStackView$$ExternalSyntheticLambda23).start();
        if (z && (bubbleInStackWithKey = this.mBubbleData.getBubbleInStackWithKey(this.mExpandedBubble.getKey())) != null) {
            this.mManageSettingsIcon.setImageBitmap(bubbleInStackWithKey.mRawBadgeBitmap);
            this.mManageSettingsText.setText(getResources().getString(2131952068, bubbleInStackWithKey.mAppName));
        }
        BubbleExpandedView expandedView = this.mExpandedBubble.getExpandedView();
        Objects.requireNonNull(expandedView);
        if (expandedView.mTaskView != null) {
            BubbleExpandedView expandedView2 = this.mExpandedBubble.getExpandedView();
            Objects.requireNonNull(expandedView2);
            TaskView taskView = expandedView2.mTaskView;
            if (this.mShowingManage) {
                rect = new Rect(0, 0, getWidth(), getHeight());
            } else {
                rect = null;
            }
            Objects.requireNonNull(taskView);
            taskView.mObscuredTouchRect = rect;
        }
        if (getResources().getConfiguration().getLayoutDirection() == 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        BubbleExpandedView expandedView3 = this.mExpandedBubble.getExpandedView();
        Rect rect2 = this.mTempRect;
        Objects.requireNonNull(expandedView3);
        expandedView3.mManageButton.getBoundsOnScreen(rect2);
        BubbleExpandedView expandedView4 = this.mExpandedBubble.getExpandedView();
        Objects.requireNonNull(expandedView4);
        float marginStart = ((LinearLayout.LayoutParams) expandedView4.mManageButton.getLayoutParams()).getMarginStart();
        if (z2) {
            f2 = this.mTempRect.left;
        } else {
            f2 = this.mTempRect.right + marginStart;
            marginStart = this.mManageMenu.getWidth();
        }
        float f3 = f2 - marginStart;
        float height = this.mTempRect.bottom - this.mManageMenu.getHeight();
        if (z2) {
            i = 1;
        } else {
            i = -1;
        }
        float width = (this.mManageMenu.getWidth() * i) / 4.0f;
        if (z) {
            this.mManageMenu.setScaleX(0.5f);
            this.mManageMenu.setScaleY(0.5f);
            this.mManageMenu.setTranslationX(f3 - width);
            this.mManageMenu.setTranslationY((viewGroup.getHeight() / 4.0f) + height);
            this.mManageMenu.setAlpha(0.0f);
            ViewGroup viewGroup2 = this.mManageMenu;
            Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
            PhysicsAnimator instance = PhysicsAnimator.Companion.getInstance(viewGroup2);
            instance.spring(DynamicAnimation.ALPHA, 1.0f);
            instance.spring(DynamicAnimation.SCALE_X, 1.0f);
            instance.spring(DynamicAnimation.SCALE_Y, 1.0f);
            instance.spring(DynamicAnimation.TRANSLATION_X, f3);
            instance.spring(DynamicAnimation.TRANSLATION_Y, height);
            instance.withEndActions(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda0(this, 6));
            instance.start();
            this.mManageMenu.setVisibility(0);
            return;
        }
        ViewGroup viewGroup3 = this.mManageMenu;
        Function1<Object, ? extends PhysicsAnimator<?>> function12 = PhysicsAnimator.instanceConstructor;
        PhysicsAnimator instance2 = PhysicsAnimator.Companion.getInstance(viewGroup3);
        instance2.spring(DynamicAnimation.ALPHA, 0.0f);
        instance2.spring(DynamicAnimation.SCALE_X, 0.5f);
        instance2.spring(DynamicAnimation.SCALE_Y, 0.5f);
        instance2.spring(DynamicAnimation.TRANSLATION_X, f3 - width);
        instance2.spring(DynamicAnimation.TRANSLATION_Y, (this.mManageMenu.getHeight() / 4.0f) + height);
        instance2.withEndActions(new BubbleStackView$$ExternalSyntheticLambda15(this, 0));
        instance2.start();
    }

    public final void showNewlySelectedBubble(final BubbleViewProvider bubbleViewProvider) {
        final BubbleViewProvider bubbleViewProvider2 = this.mExpandedBubble;
        this.mExpandedBubble = bubbleViewProvider;
        if (this.mIsExpanded) {
            hideCurrentInputMethod();
            this.mExpandedViewContainer.setAlpha(0.0f);
            SurfaceSynchronizer surfaceSynchronizer = this.mSurfaceSynchronizer;
            Runnable bubbleStackView$$ExternalSyntheticLambda24 = new Runnable() { // from class: com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda24
                @Override // java.lang.Runnable
                public final void run() {
                    BubbleStackView bubbleStackView = BubbleStackView.this;
                    BubbleViewProvider bubbleViewProvider3 = bubbleViewProvider2;
                    BubbleViewProvider bubbleViewProvider4 = bubbleViewProvider;
                    if (bubbleViewProvider3 != null) {
                        Objects.requireNonNull(bubbleStackView);
                        bubbleViewProvider3.setTaskViewVisibility();
                    }
                    bubbleStackView.updateExpandedBubble();
                    bubbleStackView.requestUpdate();
                    bubbleStackView.logBubbleEvent(bubbleViewProvider3, 4);
                    bubbleStackView.logBubbleEvent(bubbleViewProvider4, 3);
                    Bubbles.BubbleExpandListener bubbleExpandListener = bubbleStackView.mExpandListener;
                    if (!(bubbleExpandListener == null || bubbleViewProvider3 == null)) {
                        bubbleExpandListener.onBubbleExpandChanged(false, bubbleViewProvider3.getKey());
                    }
                    Bubbles.BubbleExpandListener bubbleExpandListener2 = bubbleStackView.mExpandListener;
                    if (bubbleExpandListener2 != null && bubbleViewProvider4 != null) {
                        bubbleExpandListener2.onBubbleExpandChanged(true, bubbleViewProvider4.getKey());
                    }
                }
            };
            Objects.requireNonNull((AnonymousClass1) surfaceSynchronizer);
            Choreographer.getInstance().postFrameCallback(new AnonymousClass1.Choreographer$FrameCallbackC00071(bubbleStackView$$ExternalSyntheticLambda24));
        }
    }

    public final void showScrim(boolean z) {
        if (z) {
            this.mScrim.animate().setInterpolator(Interpolators.ALPHA_IN).alpha(0.6f).start();
        } else {
            this.mScrim.animate().alpha(0.0f).setInterpolator(Interpolators.ALPHA_OUT).start();
        }
    }

    public final void updateExpandedBubble() {
        BubbleViewProvider bubbleViewProvider;
        this.mExpandedViewContainer.removeAllViews();
        if (this.mIsExpanded && (bubbleViewProvider = this.mExpandedBubble) != null && bubbleViewProvider.getExpandedView() != null) {
            BubbleExpandedView expandedView = this.mExpandedBubble.getExpandedView();
            expandedView.setContentVisibility(false);
            boolean z = !this.mIsExpansionAnimating;
            expandedView.mIsAlphaAnimating = z;
            if (!z) {
                expandedView.setContentVisibility(expandedView.mIsContentVisible);
            }
            this.mExpandedViewContainerMatrix.setScaleX(0.0f);
            this.mExpandedViewContainerMatrix.setScaleY(0.0f);
            this.mExpandedViewContainerMatrix.setTranslate(0.0f, 0.0f);
            this.mExpandedViewContainer.setVisibility(4);
            this.mExpandedViewContainer.setAlpha(0.0f);
            this.mExpandedViewContainer.addView(expandedView);
            expandedView.mManageButton.setOnClickListener(new BubbleStackView$$ExternalSyntheticLambda3(this, 0));
            if (!this.mIsExpansionAnimating) {
                SurfaceSynchronizer surfaceSynchronizer = this.mSurfaceSynchronizer;
                ScreenDecorations$$ExternalSyntheticLambda3 screenDecorations$$ExternalSyntheticLambda3 = new ScreenDecorations$$ExternalSyntheticLambda3(this, 4);
                Objects.requireNonNull((AnonymousClass1) surfaceSynchronizer);
                Choreographer.getInstance().postFrameCallback(new AnonymousClass1.Choreographer$FrameCallbackC00071(screenDecorations$$ExternalSyntheticLambda3));
            }
        }
    }

    public final void updateExpandedView() {
        boolean z;
        int[] iArr;
        float f;
        boolean z2;
        Drawable drawable;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        float f2;
        float f3;
        int i6;
        int i7;
        int i8;
        BubbleViewProvider bubbleViewProvider = this.mExpandedBubble;
        if (bubbleViewProvider == null || !"Overflow".equals(bubbleViewProvider.getKey())) {
            z = false;
        } else {
            z = true;
        }
        BubblePositioner bubblePositioner = this.mPositioner;
        boolean isStackOnLeftSide = this.mStackAnimationController.isStackOnLeftSide();
        Objects.requireNonNull(bubblePositioner);
        int i9 = bubblePositioner.mPointerHeight - bubblePositioner.mPointerOverlap;
        if (bubblePositioner.mIsLargeScreen) {
            iArr = bubblePositioner.mPaddings;
            if (isStackOnLeftSide) {
                i6 = bubblePositioner.mExpandedViewLargeScreenInset - i9;
            } else {
                i6 = bubblePositioner.mExpandedViewLargeScreenInset;
            }
            iArr[0] = i6;
            iArr[1] = 0;
            if (isStackOnLeftSide) {
                i7 = bubblePositioner.mExpandedViewLargeScreenInset;
            } else {
                i7 = bubblePositioner.mExpandedViewLargeScreenInset - i9;
            }
            iArr[2] = i7;
            if (z) {
                i8 = bubblePositioner.mExpandedViewPadding;
            } else {
                i8 = 0;
            }
            iArr[3] = i8;
        } else {
            Insets insets = bubblePositioner.mInsets;
            int i10 = insets.left;
            int i11 = bubblePositioner.mExpandedViewPadding;
            int i12 = i10 + i11;
            int i13 = insets.right + i11;
            if (z) {
                i4 = bubblePositioner.mOverflowWidth;
            } else {
                i4 = bubblePositioner.mExpandedViewLargeScreenWidth;
            }
            float f4 = i4;
            if (bubblePositioner.showBubblesVertically()) {
                if (!isStackOnLeftSide) {
                    i13 += bubblePositioner.mBubbleSize - i9;
                    float f5 = i12;
                    if (z) {
                        f3 = (bubblePositioner.mPositionRect.width() - i13) - f4;
                    } else {
                        f3 = 0.0f;
                    }
                    i12 = (int) (f5 + f3);
                } else {
                    i12 += bubblePositioner.mBubbleSize - i9;
                    float f6 = i13;
                    if (z) {
                        f2 = (bubblePositioner.mPositionRect.width() - i12) - f4;
                    } else {
                        f2 = 0.0f;
                    }
                    i13 = (int) (f6 + f2);
                }
            }
            int[] iArr2 = bubblePositioner.mPaddings;
            iArr2[0] = i12;
            if (bubblePositioner.showBubblesVertically()) {
                i5 = 0;
            } else {
                i5 = bubblePositioner.mPointerMargin;
            }
            iArr2[1] = i5;
            int[] iArr3 = bubblePositioner.mPaddings;
            iArr3[2] = i13;
            iArr3[3] = 0;
            iArr = iArr3;
        }
        this.mExpandedViewContainer.setPadding(iArr[0], iArr[1], iArr[2], iArr[3]);
        if (this.mIsExpansionAnimating) {
            FrameLayout frameLayout = this.mExpandedViewContainer;
            if (this.mIsExpanded) {
                i3 = 0;
            } else {
                i3 = 8;
            }
            frameLayout.setVisibility(i3);
        }
        BubbleViewProvider bubbleViewProvider2 = this.mExpandedBubble;
        if (!(bubbleViewProvider2 == null || bubbleViewProvider2.getExpandedView() == null)) {
            PointF expandedBubbleXY = this.mPositioner.getExpandedBubbleXY(getBubbleIndex(this.mExpandedBubble), getState());
            FrameLayout frameLayout2 = this.mExpandedViewContainer;
            BubblePositioner bubblePositioner2 = this.mPositioner;
            BubbleViewProvider bubbleViewProvider3 = this.mExpandedBubble;
            if (bubblePositioner2.showBubblesVertically()) {
                f = expandedBubbleXY.y;
            } else {
                f = expandedBubbleXY.x;
            }
            frameLayout2.setTranslationY(bubblePositioner2.getExpandedViewY(bubbleViewProvider3, f));
            this.mExpandedViewContainer.setTranslationX(0.0f);
            BubbleExpandedView expandedView = this.mExpandedBubble.getExpandedView();
            int[] locationOnScreen = this.mExpandedViewContainer.getLocationOnScreen();
            Objects.requireNonNull(expandedView);
            expandedView.mExpandedViewContainerLocation = locationOnScreen;
            expandedView.updateHeight();
            TaskView taskView = expandedView.mTaskView;
            if (taskView != null && taskView.getVisibility() == 0 && expandedView.mTaskView.isAttachedToWindow()) {
                expandedView.mTaskView.onLocationChanged();
            }
            if (expandedView.mIsOverflow) {
                BubbleOverflowContainerView bubbleOverflowContainerView = expandedView.mOverflowView;
                Objects.requireNonNull(bubbleOverflowContainerView);
                bubbleOverflowContainerView.requestFocus();
                int integer = bubbleOverflowContainerView.getResources().getInteger(2131492875);
                RecyclerView recyclerView = bubbleOverflowContainerView.mRecyclerView;
                bubbleOverflowContainerView.getContext();
                recyclerView.setLayoutManager(new BubbleOverflowContainerView.OverflowGridLayoutManager(integer));
                Context context = bubbleOverflowContainerView.getContext();
                ArrayList arrayList = bubbleOverflowContainerView.mOverflowBubbles;
                BubbleController bubbleController = bubbleOverflowContainerView.mController;
                Objects.requireNonNull(bubbleController);
                BubbleOverflowAdapter bubbleOverflowAdapter = new BubbleOverflowAdapter(context, arrayList, new NavigationBar$$ExternalSyntheticLambda13(bubbleController, 2), bubbleOverflowContainerView.mController.getPositioner());
                bubbleOverflowContainerView.mAdapter = bubbleOverflowAdapter;
                bubbleOverflowContainerView.mRecyclerView.setAdapter(bubbleOverflowAdapter);
                bubbleOverflowContainerView.mOverflowBubbles.clear();
                ArrayList arrayList2 = bubbleOverflowContainerView.mOverflowBubbles;
                BubbleController bubbleController2 = bubbleOverflowContainerView.mController;
                Objects.requireNonNull(bubbleController2);
                arrayList2.addAll(bubbleController2.mBubbleData.getOverflowBubbles());
                bubbleOverflowContainerView.mAdapter.notifyDataSetChanged();
                BubbleController bubbleController3 = bubbleOverflowContainerView.mController;
                BubbleOverflowContainerView.AnonymousClass1 r4 = bubbleOverflowContainerView.mDataListener;
                Objects.requireNonNull(bubbleController3);
                bubbleController3.mOverflowListener = r4;
                bubbleOverflowContainerView.updateEmptyStateVisibility();
                Resources resources = bubbleOverflowContainerView.getResources();
                if ((resources.getConfiguration().uiMode & 48) == 32) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                ImageView imageView = bubbleOverflowContainerView.mEmptyStateImage;
                if (z2) {
                    drawable = resources.getDrawable(2131231634);
                } else {
                    drawable = resources.getDrawable(2131231635);
                }
                imageView.setImageDrawable(drawable);
                View findViewById = bubbleOverflowContainerView.findViewById(2131427627);
                if (z2) {
                    i = resources.getColor(2131099758);
                } else {
                    i = resources.getColor(2131099760);
                }
                findViewById.setBackgroundColor(i);
                TypedArray obtainStyledAttributes = bubbleOverflowContainerView.getContext().obtainStyledAttributes(new int[]{16844002, 16842808});
                int i14 = -16777216;
                if (z2) {
                    i2 = -16777216;
                } else {
                    i2 = -1;
                }
                int color = obtainStyledAttributes.getColor(0, i2);
                if (z2) {
                    i14 = -1;
                }
                int ensureTextContrast = ContrastColorUtil.ensureTextContrast(obtainStyledAttributes.getColor(1, i14), color, z2);
                obtainStyledAttributes.recycle();
                bubbleOverflowContainerView.setBackgroundColor(color);
                bubbleOverflowContainerView.mEmptyStateTitle.setTextColor(ensureTextContrast);
                bubbleOverflowContainerView.mEmptyStateSubtitle.setTextColor(ensureTextContrast);
            }
            updatePointerPosition(false);
        }
        this.mStackOnLeftOrWillBe = this.mStackAnimationController.isStackOnLeftSide();
    }

    public final void updateOverflow() {
        BubbleOverflow bubbleOverflow = this.mBubbleOverflow;
        Objects.requireNonNull(bubbleOverflow);
        bubbleOverflow.updateResources();
        BubbleExpandedView bubbleExpandedView = bubbleOverflow.expandedView;
        if (bubbleExpandedView != null) {
            bubbleExpandedView.applyThemeAttrs();
        }
        BadgedImageView iconView = bubbleOverflow.getIconView$1();
        if (iconView != null) {
            iconView.mBubbleIcon.setImageResource(2131231636);
        }
        bubbleOverflow.updateBtnTheme();
        this.mBubbleContainer.reorderView(this.mBubbleOverflow.getIconView$1(), this.mBubbleContainer.getChildCount() - 1);
        updateOverflowVisibility();
    }

    public final void updateOverflowVisibility() {
        int i;
        BubbleOverflow bubbleOverflow = this.mBubbleOverflow;
        if (this.mIsExpanded || this.mBubbleData.isShowingOverflow()) {
            i = 0;
        } else {
            i = 8;
        }
        Objects.requireNonNull(bubbleOverflow);
        BadgedImageView badgedImageView = bubbleOverflow.overflowBtn;
        if (badgedImageView != null) {
            badgedImageView.setVisibility(i);
        }
    }

    public final void updatePointerPosition(final boolean z) {
        int bubbleIndex;
        float f;
        float f2;
        float f3;
        BubbleViewProvider bubbleViewProvider = this.mExpandedBubble;
        if (bubbleViewProvider != null && bubbleViewProvider.getExpandedView() != null && (bubbleIndex = getBubbleIndex(this.mExpandedBubble)) != -1) {
            PointF expandedBubbleXY = this.mPositioner.getExpandedBubbleXY(bubbleIndex, getState());
            if (this.mPositioner.showBubblesVertically()) {
                f = expandedBubbleXY.y;
            } else {
                f = expandedBubbleXY.x;
            }
            final BubbleExpandedView expandedView = this.mExpandedBubble.getExpandedView();
            final boolean z2 = this.mStackOnLeftOrWillBe;
            Objects.requireNonNull(expandedView);
            final boolean showBubblesVertically = expandedView.mPositioner.showBubblesVertically();
            float f4 = 0.0f;
            if (!showBubblesVertically || !z2) {
                f2 = 0.0f;
            } else {
                f2 = expandedView.mPointerHeight - expandedView.mPointerOverlap;
            }
            if (!showBubblesVertically || z2) {
                f3 = 0.0f;
            } else {
                f3 = expandedView.mPointerHeight - expandedView.mPointerOverlap;
            }
            if (!showBubblesVertically) {
                f4 = expandedView.mPointerHeight - expandedView.mPointerOverlap;
            }
            expandedView.setPadding((int) f2, (int) f4, (int) f3, 0);
            final float pointerPosition = expandedView.mPositioner.getPointerPosition(f);
            if (expandedView.mPositioner.showBubblesVertically()) {
                pointerPosition -= expandedView.mPositioner.getExpandedViewY(expandedView.mBubble, f);
            }
            expandedView.post(new Runnable() { // from class: com.android.wm.shell.bubbles.BubbleExpandedView$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    BubbleExpandedView.m150$r8$lambda$OgjLsfC5rVPmXxC3ADgwAd5PRI(BubbleExpandedView.this, showBubblesVertically, z2, pointerPosition, z);
                }
            });
        }
    }

    public final void updateTemporarilyInvisibleAnimation(boolean z) {
        boolean z2;
        long j;
        removeCallbacks(this.mAnimateTemporarilyInvisibleImmediate);
        if (!this.mIsDraggingStack) {
            if (!this.mTemporarilyInvisible || this.mFlyout.getVisibility() == 0) {
                z2 = false;
            } else {
                z2 = true;
            }
            KeyguardStatusView$$ExternalSyntheticLambda0 keyguardStatusView$$ExternalSyntheticLambda0 = this.mAnimateTemporarilyInvisibleImmediate;
            if (!z2 || z) {
                j = 0;
            } else {
                j = 1000;
            }
            postDelayed(keyguardStatusView$$ExternalSyntheticLambda0, j);
        }
    }

    public static void $r8$lambda$DjZKjrr94LsLQZplD_wFyam4Xpc(BubbleStackView bubbleStackView, Bubble bubble) {
        Objects.requireNonNull(bubbleStackView);
        bubbleStackView.mAfterFlyoutHidden = null;
        BubbleViewProvider bubbleViewProvider = bubbleStackView.mBubbleToExpandAfterFlyoutCollapse;
        if (bubbleViewProvider != null) {
            bubbleStackView.mBubbleData.setSelectedBubble(bubbleViewProvider);
            bubbleStackView.mBubbleData.setExpanded(true);
            bubbleStackView.mBubbleToExpandAfterFlyoutCollapse = null;
        }
        Objects.requireNonNull(bubble);
        BadgedImageView badgedImageView = bubble.mIconView;
        if (badgedImageView != null) {
            badgedImageView.removeDotSuppressionFlag(BadgedImageView.SuppressionFlag.FLYOUT_VISIBLE);
        }
        bubbleStackView.updateTemporarilyInvisibleAnimation(false);
    }

    public static void $r8$lambda$VRRlSoOy6XdJCiRiE6ME0w93vig(BubbleStackView bubbleStackView) {
        Objects.requireNonNull(bubbleStackView);
        bubbleStackView.showManageMenu(false);
        BubbleData bubbleData = bubbleStackView.mBubbleData;
        Objects.requireNonNull(bubbleData);
        BubbleViewProvider bubbleViewProvider = bubbleData.mSelectedBubble;
        if (bubbleViewProvider != null && bubbleStackView.mBubbleData.hasBubbleInStackWithKey(bubbleViewProvider.getKey())) {
            Bubble bubble = (Bubble) bubbleViewProvider;
            Context context = ((FrameLayout) bubbleStackView).mContext;
            Intent intent = new Intent("android.settings.APP_NOTIFICATION_BUBBLE_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", bubble.mPackageName);
            int i = bubble.mAppUid;
            if (i == -1) {
                PackageManager packageManagerForUser = BubbleController.getPackageManagerForUser(context, bubble.mUser.getIdentifier());
                if (packageManagerForUser != null) {
                    try {
                        i = packageManagerForUser.getApplicationInfo(bubble.mShortcutInfo.getPackage(), 0).uid;
                    } catch (PackageManager.NameNotFoundException e) {
                        Log.e("Bubble", "cannot find uid", e);
                    }
                }
                i = -1;
            }
            if (i != -1) {
                intent.putExtra("app_uid", i);
            }
            intent.addFlags(134217728);
            intent.addFlags(268435456);
            intent.addFlags(536870912);
            bubbleStackView.mBubbleData.setExpanded(false);
            ((FrameLayout) bubbleStackView).mContext.startActivityAsUser(intent, bubble.mUser);
            bubbleStackView.logBubbleEvent(bubbleViewProvider, 9);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x003b, code lost:
        if (r0 != false) goto L_0x003d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x003f, code lost:
        if (r4.mExpandedBubble != null) goto L_0x0043;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void $r8$lambda$k7rtryEI4rtW4AORrn7yrpeihz4(com.android.wm.shell.bubbles.BubbleStackView r4) {
        /*
            java.util.Objects.requireNonNull(r4)
            boolean r0 = r4.mIsExpanded
            if (r0 == 0) goto L_0x0063
            com.android.wm.shell.bubbles.BubbleViewProvider r0 = r4.mExpandedBubble
            com.android.wm.shell.bubbles.BubbleExpandedView r0 = r0.getExpandedView()
            if (r0 == 0) goto L_0x0063
            boolean r0 = android.app.ActivityManager.isRunningInTestHarness()
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x0018
            goto L_0x0042
        L_0x0018:
            android.content.Context r0 = r4.mContext
            java.lang.String r3 = r0.getPackageName()
            android.content.SharedPreferences r0 = r0.getSharedPreferences(r3, r2)
            java.lang.String r3 = "HasSeenBubblesManageOnboarding"
            boolean r0 = r0.getBoolean(r3, r2)
            if (r0 == 0) goto L_0x003d
            android.content.Context r0 = r4.mContext
            android.content.ContentResolver r0 = r0.getContentResolver()
            java.lang.String r3 = "force_show_bubbles_user_education"
            int r0 = android.provider.Settings.Secure.getInt(r0, r3, r2)
            if (r0 == 0) goto L_0x003a
            r0 = r1
            goto L_0x003b
        L_0x003a:
            r0 = r2
        L_0x003b:
            if (r0 == 0) goto L_0x0042
        L_0x003d:
            com.android.wm.shell.bubbles.BubbleViewProvider r0 = r4.mExpandedBubble
            if (r0 == 0) goto L_0x0042
            goto L_0x0043
        L_0x0042:
            r1 = r2
        L_0x0043:
            if (r1 != 0) goto L_0x0046
            goto L_0x0063
        L_0x0046:
            com.android.wm.shell.bubbles.ManageEducationView r0 = r4.mManageEduView
            if (r0 != 0) goto L_0x0058
            com.android.wm.shell.bubbles.ManageEducationView r0 = new com.android.wm.shell.bubbles.ManageEducationView
            android.content.Context r1 = r4.mContext
            com.android.wm.shell.bubbles.BubblePositioner r2 = r4.mPositioner
            r0.<init>(r1, r2)
            r4.mManageEduView = r0
            r4.addView(r0)
        L_0x0058:
            com.android.wm.shell.bubbles.ManageEducationView r0 = r4.mManageEduView
            com.android.wm.shell.bubbles.BubbleViewProvider r4 = r4.mExpandedBubble
            com.android.wm.shell.bubbles.BubbleExpandedView r4 = r4.getExpandedView()
            r0.show(r4)
        L_0x0063:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.bubbles.BubbleStackView.$r8$lambda$k7rtryEI4rtW4AORrn7yrpeihz4(com.android.wm.shell.bubbles.BubbleStackView):void");
    }

    /* renamed from: -$$Nest$manimateDismissBubble */
    public static void m151$$Nest$manimateDismissBubble(BubbleStackView bubbleStackView, View view, boolean z) {
        Objects.requireNonNull(bubbleStackView);
        bubbleStackView.mViewBeingDismissed = view;
        if (view != null) {
            if (z) {
                bubbleStackView.mDismissBubbleAnimator.removeAllListeners();
                bubbleStackView.mDismissBubbleAnimator.start();
                return;
            }
            bubbleStackView.mDismissBubbleAnimator.removeAllListeners();
            bubbleStackView.mDismissBubbleAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.bubbles.BubbleStackView.16
                {
                    BubbleStackView.this = bubbleStackView;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationCancel(Animator animator) {
                    super.onAnimationCancel(animator);
                    BubbleStackView.m154$$Nest$mresetDismissAnimator(BubbleStackView.this);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    BubbleStackView.m154$$Nest$mresetDismissAnimator(BubbleStackView.this);
                }
            });
            bubbleStackView.mDismissBubbleAnimator.reverse();
        }
    }

    /* renamed from: -$$Nest$mdismissMagnetizedObject */
    public static void m152$$Nest$mdismissMagnetizedObject(BubbleStackView bubbleStackView) {
        Objects.requireNonNull(bubbleStackView);
        if (bubbleStackView.mIsExpanded) {
            MagnetizedObject<?> magnetizedObject = bubbleStackView.mMagnetizedObject;
            Objects.requireNonNull(magnetizedObject);
            Bubble bubbleWithView = bubbleStackView.mBubbleData.getBubbleWithView((View) magnetizedObject.underlyingObject);
            if (bubbleWithView != null && bubbleStackView.mBubbleData.hasBubbleInStackWithKey(bubbleWithView.getKey())) {
                bubbleStackView.mBubbleData.dismissBubbleWithKey(bubbleWithView.getKey(), 1);
                return;
            }
            return;
        }
        bubbleStackView.mBubbleData.dismissAll(1);
    }

    /* renamed from: -$$Nest$mpassEventToMagnetizedObject */
    public static boolean m153$$Nest$mpassEventToMagnetizedObject(BubbleStackView bubbleStackView, MotionEvent motionEvent) {
        Objects.requireNonNull(bubbleStackView);
        MagnetizedObject<?> magnetizedObject = bubbleStackView.mMagnetizedObject;
        if (magnetizedObject == null || !magnetizedObject.maybeConsumeMotionEvent(motionEvent)) {
            return false;
        }
        return true;
    }

    /* renamed from: -$$Nest$mresetDismissAnimator */
    public static void m154$$Nest$mresetDismissAnimator(BubbleStackView bubbleStackView) {
        Objects.requireNonNull(bubbleStackView);
        bubbleStackView.mDismissBubbleAnimator.removeAllListeners();
        bubbleStackView.mDismissBubbleAnimator.cancel();
        View view = bubbleStackView.mViewBeingDismissed;
        if (view != null) {
            view.setAlpha(1.0f);
            bubbleStackView.mViewBeingDismissed = null;
        }
        DismissView dismissView = bubbleStackView.mDismissView;
        if (dismissView != null) {
            dismissView.circle.setScaleX(1.0f);
            DismissView dismissView2 = bubbleStackView.mDismissView;
            Objects.requireNonNull(dismissView2);
            dismissView2.circle.setScaleY(1.0f);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final boolean dispatchTouchEvent(MotionEvent motionEvent) {
        boolean z = false;
        if (motionEvent.getAction() != 0 && motionEvent.getActionIndex() != this.mPointerIndexDown) {
            return false;
        }
        if (motionEvent.getAction() == 0) {
            this.mPointerIndexDown = motionEvent.getActionIndex();
        } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            this.mPointerIndexDown = -1;
        }
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        if (!dispatchTouchEvent && !this.mIsExpanded && this.mIsGestureInProgress) {
            onTouch(this, motionEvent);
            dispatchTouchEvent = true;
        }
        if (!(motionEvent.getAction() == 1 || motionEvent.getAction() == 3)) {
            z = true;
        }
        this.mIsGestureInProgress = z;
        return dispatchTouchEvent;
    }

    public final boolean maybeShowStackEdu() {
        if (!shouldShowStackEdu() || this.mIsExpanded) {
            return false;
        }
        if (this.mStackEduView == null) {
            StackEducationView stackEducationView = new StackEducationView(((FrameLayout) this).mContext, this.mPositioner, this.mBubbleController);
            this.mStackEduView = stackEducationView;
            addView(stackEducationView);
        }
        this.mBubbleContainer.bringToFront();
        return this.mStackEduView.show(this.mPositioner.getDefaultStartPosition());
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mPositioner.update();
        getViewTreeObserver().addOnComputeInternalInsetsListener(this);
        getViewTreeObserver().addOnDrawListener(this.mSystemGestureExcludeUpdater);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnPreDrawListener(this.mViewUpdater);
        getViewTreeObserver().removeOnDrawListener(this.mSystemGestureExcludeUpdater);
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this);
        BubbleOverflow bubbleOverflow = this.mBubbleOverflow;
        if (bubbleOverflow != null) {
            BubbleExpandedView bubbleExpandedView = bubbleOverflow.expandedView;
            if (bubbleExpandedView != null) {
                bubbleExpandedView.cleanUpExpandedState();
            }
            bubbleOverflow.expandedView = null;
        }
    }

    public final void onDisplaySizeChanged() {
        boolean z;
        float f;
        updateOverflow();
        setUpManageMenu();
        setUpFlyout();
        setUpDismissView();
        updateUserEdu();
        BubblePositioner bubblePositioner = this.mPositioner;
        Objects.requireNonNull(bubblePositioner);
        this.mBubbleSize = bubblePositioner.mBubbleSize;
        for (Bubble bubble : this.mBubbleData.getBubbles()) {
            Objects.requireNonNull(bubble);
            BadgedImageView badgedImageView = bubble.mIconView;
            if (badgedImageView == null) {
                Log.d("Bubbles", "Display size changed. Icon null: " + bubble);
            } else {
                int i = this.mBubbleSize;
                badgedImageView.setLayoutParams(new FrameLayout.LayoutParams(i, i));
                BubbleExpandedView bubbleExpandedView = bubble.mExpandedView;
                if (bubbleExpandedView != null) {
                    bubbleExpandedView.updateDimensions();
                }
            }
        }
        BadgedImageView iconView = this.mBubbleOverflow.getIconView$1();
        int i2 = this.mBubbleSize;
        iconView.setLayoutParams(new FrameLayout.LayoutParams(i2, i2));
        this.mExpandedAnimationController.updateResources();
        StackAnimationController stackAnimationController = this.mStackAnimationController;
        Objects.requireNonNull(stackAnimationController);
        PhysicsAnimationLayout physicsAnimationLayout = stackAnimationController.mLayout;
        if (physicsAnimationLayout != null) {
            stackAnimationController.mBubblePaddingTop = physicsAnimationLayout.getContext().getResources().getDimensionPixelSize(2131165446);
        }
        DismissView dismissView = this.mDismissView;
        Objects.requireNonNull(dismissView);
        dismissView.updatePadding();
        dismissView.getLayoutParams().height = dismissView.getResources().getDimensionPixelSize(2131165727);
        int dimensionPixelSize = dismissView.getResources().getDimensionPixelSize(2131165666);
        dismissView.circle.getLayoutParams().width = dimensionPixelSize;
        dismissView.circle.getLayoutParams().height = dimensionPixelSize;
        dismissView.circle.requestLayout();
        MagnetizedObject.MagneticTarget magneticTarget = this.mMagneticTarget;
        Objects.requireNonNull(magneticTarget);
        magneticTarget.magneticFieldRadiusPx = this.mBubbleSize * 2;
        StackAnimationController stackAnimationController2 = this.mStackAnimationController;
        PointF restingPosition = this.mPositioner.getRestingPosition();
        RectF allowableStackPositionRegion = this.mStackAnimationController.getAllowableStackPositionRegion();
        if (restingPosition.x < allowableStackPositionRegion.width() / 2.0f) {
            z = true;
        } else {
            z = false;
        }
        float max = Math.max(0.0f, Math.min(1.0f, (restingPosition.y - allowableStackPositionRegion.top) / allowableStackPositionRegion.height()));
        Objects.requireNonNull(stackAnimationController2);
        RectF allowableStackPositionRegion2 = stackAnimationController2.getAllowableStackPositionRegion();
        if (z) {
            f = allowableStackPositionRegion2.left;
        } else {
            f = allowableStackPositionRegion2.right;
        }
        stackAnimationController2.setStackPosition(new PointF(f, (allowableStackPositionRegion2.height() * max) + allowableStackPositionRegion2.top));
        if (this.mIsExpanded) {
            updateExpandedView();
        }
    }

    public final void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfoInternal(accessibilityNodeInfo);
        setupLocalMenu(accessibilityNodeInfo);
    }

    public final boolean performAccessibilityActionInternal(int i, Bundle bundle) {
        if (super.performAccessibilityActionInternal(i, bundle)) {
            return true;
        }
        RectF allowableStackPositionRegion = this.mStackAnimationController.getAllowableStackPositionRegion();
        if (i == 1048576) {
            this.mBubbleData.dismissAll(6);
            announceForAccessibility(getResources().getString(2131951684));
            return true;
        } else if (i == 524288) {
            this.mBubbleData.setExpanded(false);
            return true;
        } else if (i == 262144) {
            this.mBubbleData.setExpanded(true);
            return true;
        } else if (i == 2131427440) {
            StackAnimationController stackAnimationController = this.mStackAnimationController;
            float f = allowableStackPositionRegion.left;
            float f2 = allowableStackPositionRegion.top;
            Objects.requireNonNull(stackAnimationController);
            stackAnimationController.springStack(f, f2, 700.0f);
            return true;
        } else if (i == 2131427441) {
            StackAnimationController stackAnimationController2 = this.mStackAnimationController;
            float f3 = allowableStackPositionRegion.right;
            float f4 = allowableStackPositionRegion.top;
            Objects.requireNonNull(stackAnimationController2);
            stackAnimationController2.springStack(f3, f4, 700.0f);
            return true;
        } else if (i == 2131427431) {
            StackAnimationController stackAnimationController3 = this.mStackAnimationController;
            float f5 = allowableStackPositionRegion.left;
            float f6 = allowableStackPositionRegion.bottom;
            Objects.requireNonNull(stackAnimationController3);
            stackAnimationController3.springStack(f5, f6, 700.0f);
            return true;
        } else if (i != 2131427432) {
            return false;
        } else {
            StackAnimationController stackAnimationController4 = this.mStackAnimationController;
            float f7 = allowableStackPositionRegion.right;
            float f8 = allowableStackPositionRegion.bottom;
            Objects.requireNonNull(stackAnimationController4);
            stackAnimationController4.springStack(f7, f8, 700.0f);
            return true;
        }
    }

    public final boolean shouldShowStackEdu() {
        boolean z;
        if (ActivityManager.isRunningInTestHarness()) {
            return false;
        }
        Context context = ((FrameLayout) this).mContext;
        if (context.getSharedPreferences(context.getPackageName(), 0).getBoolean("HasSeenBubblesOnboarding", false)) {
            if (Settings.Secure.getInt(((FrameLayout) this).mContext.getContentResolver(), "force_show_bubbles_user_education", 0) != 0) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                return false;
            }
        }
        return true;
    }

    public final void updateBadges(boolean z) {
        int bubbleCount = getBubbleCount();
        for (int i = 0; i < bubbleCount; i++) {
            BadgedImageView badgedImageView = (BadgedImageView) this.mBubbleContainer.getChildAt(i);
            boolean z2 = true;
            if (this.mIsExpanded) {
                if (!this.mPositioner.showBubblesVertically() || this.mStackOnLeftOrWillBe) {
                    z2 = false;
                }
                badgedImageView.showDotAndBadge(z2);
            } else if (z) {
                if (i == 0) {
                    badgedImageView.showDotAndBadge(!this.mStackOnLeftOrWillBe);
                } else {
                    badgedImageView.hideDotAndBadge(!this.mStackOnLeftOrWillBe);
                }
            }
        }
    }

    public final void updateBubbleShadows(boolean z) {
        boolean z2;
        int bubbleCount = getBubbleCount();
        for (int i = 0; i < bubbleCount; i++) {
            BubblePositioner bubblePositioner = this.mPositioner;
            Objects.requireNonNull(bubblePositioner);
            float f = (bubblePositioner.mMaxBubbles * this.mBubbleElevation) - i;
            BadgedImageView badgedImageView = (BadgedImageView) this.mBubbleContainer.getChildAt(i);
            MagnetizedObject<?> magnetizedObject = this.mMagnetizedObject;
            if (magnetizedObject != null) {
                Objects.requireNonNull(magnetizedObject);
                if (magnetizedObject.underlyingObject.equals(badgedImageView)) {
                    z2 = true;
                    if (!z || z2) {
                        badgedImageView.setZ(f);
                    } else {
                        if (i >= 2) {
                            f = 0.0f;
                        }
                        badgedImageView.setZ(f);
                    }
                }
            }
            z2 = false;
            if (!z) {
            }
            badgedImageView.setZ(f);
        }
    }

    public final void updateUserEdu() {
        if (isStackEduShowing()) {
            removeView(this.mStackEduView);
            StackEducationView stackEducationView = new StackEducationView(((FrameLayout) this).mContext, this.mPositioner, this.mBubbleController);
            this.mStackEduView = stackEducationView;
            addView(stackEducationView);
            this.mBubbleContainer.bringToFront();
            this.mStackEduView.show(this.mPositioner.getDefaultStartPosition());
        }
        ManageEducationView manageEducationView = this.mManageEduView;
        if (manageEducationView != null && manageEducationView.getVisibility() == 0) {
            removeView(this.mManageEduView);
            ManageEducationView manageEducationView2 = new ManageEducationView(((FrameLayout) this).mContext, this.mPositioner);
            this.mManageEduView = manageEducationView2;
            addView(manageEducationView2);
            this.mManageEduView.show(this.mExpandedBubble.getExpandedView());
        }
    }

    public final void updateZOrder() {
        float f;
        int bubbleCount = getBubbleCount();
        for (int i = 0; i < bubbleCount; i++) {
            BadgedImageView badgedImageView = (BadgedImageView) this.mBubbleContainer.getChildAt(i);
            if (i < 2) {
                BubblePositioner bubblePositioner = this.mPositioner;
                Objects.requireNonNull(bubblePositioner);
                f = (bubblePositioner.mMaxBubbles * this.mBubbleElevation) - i;
            } else {
                f = 0.0f;
            }
            badgedImageView.setZ(f);
        }
    }

    @VisibleForTesting
    public BubbleViewProvider getExpandedBubble() {
        return this.mExpandedBubble;
    }
}
