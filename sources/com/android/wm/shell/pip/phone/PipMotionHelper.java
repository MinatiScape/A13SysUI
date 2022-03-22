package com.android.wm.shell.pip.phone;

import android.content.Context;
import android.graphics.Rect;
import android.os.Looper;
import android.util.Log;
import android.view.Choreographer;
import android.view.SurfaceControl;
import androidx.dynamicanimation.animation.AnimationHandler;
import com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda0;
import com.android.systemui.doze.DozeScreenState$$ExternalSyntheticLambda0;
import com.android.systemui.qs.QSFgsManagerFooter$$ExternalSyntheticLambda0;
import com.android.wm.shell.ShellTaskOrganizer$$ExternalSyntheticLambda1;
import com.android.wm.shell.animation.FloatProperties;
import com.android.wm.shell.animation.FloatProperties$Companion$RECT_X$1;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionState;
import com.android.wm.shell.pip.phone.PipAppOpsListener;
import java.util.Objects;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public final class PipMotionHelper implements PipAppOpsListener.Callback, FloatingContentCoordinator.FloatingContent {
    public final Context mContext;
    public PhysicsAnimator.FlingConfig mFlingConfigX;
    public PhysicsAnimator.FlingConfig mFlingConfigY;
    public FloatingContentCoordinator mFloatingContentCoordinator;
    public AnonymousClass3 mMagnetizedPip;
    public PhonePipMenuController mMenuController;
    public PipBoundsState mPipBoundsState;
    public final PipTaskOrganizer mPipTaskOrganizer;
    public final AnonymousClass2 mPipTransitionCallback;
    public Runnable mPostPipTransitionCallback;
    public PipSnapAlgorithm mSnapAlgorithm;
    public PhysicsAnimator.FlingConfig mStashConfigX;
    public PhysicsAnimator<Rect> mTemporaryBoundsPhysicsAnimator;
    public final Rect mFloatingAllowedArea = new Rect();
    public ThreadLocal<AnimationHandler> mSfAnimationHandlerThreadLocal = ThreadLocal.withInitial(new Supplier() { // from class: com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            Objects.requireNonNull(PipMotionHelper.this);
            final Looper myLooper = Looper.myLooper();
            return new AnimationHandler(new AnimationHandler.FrameCallbackScheduler() { // from class: com.android.wm.shell.pip.phone.PipMotionHelper.1
                @Override // androidx.dynamicanimation.animation.AnimationHandler.FrameCallbackScheduler
                public final boolean isCurrentThread() {
                    if (Looper.myLooper() == myLooper) {
                        return true;
                    }
                    return false;
                }

                @Override // androidx.dynamicanimation.animation.AnimationHandler.FrameCallbackScheduler
                public final void postFrameCallback(ImageWallpaper$GLEngine$$ExternalSyntheticLambda0 imageWallpaper$GLEngine$$ExternalSyntheticLambda0) {
                    Choreographer.getSfInstance().postFrameCallback(new PipMotionHelper$1$$ExternalSyntheticLambda0(imageWallpaper$GLEngine$$ExternalSyntheticLambda0, 0));
                }
            });
        }
    });
    public final PhysicsAnimator.SpringConfig mSpringConfig = new PhysicsAnimator.SpringConfig(700.0f, 1.0f);
    public final PhysicsAnimator.SpringConfig mAnimateToDismissSpringConfig = new PhysicsAnimator.SpringConfig(1500.0f, 1.0f);
    public final PhysicsAnimator.SpringConfig mCatchUpSpringConfig = new PhysicsAnimator.SpringConfig(5000.0f, 1.0f);
    public final PhysicsAnimator.SpringConfig mConflictResolutionSpringConfig = new PhysicsAnimator.SpringConfig(200.0f, 1.0f);
    public final PipMotionHelper$$ExternalSyntheticLambda1 mUpdateBoundsCallback = new PipMotionHelper$$ExternalSyntheticLambda1(this, 0);
    public boolean mSpringingToTouch = false;
    public boolean mDismissalPending = false;
    public final PipMotionHelper$$ExternalSyntheticLambda0 mResizePipUpdateListener = new PhysicsAnimator.UpdateListener() { // from class: com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda0
        @Override // com.android.wm.shell.animation.PhysicsAnimator.UpdateListener
        public final void onAnimationUpdateForProperty(Object obj) {
            PipMotionHelper pipMotionHelper = PipMotionHelper.this;
            Rect rect = (Rect) obj;
            Objects.requireNonNull(pipMotionHelper);
            PipBoundsState pipBoundsState = pipMotionHelper.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState);
            PipBoundsState.MotionBoundsState motionBoundsState = pipBoundsState.mMotionBoundsState;
            Objects.requireNonNull(motionBoundsState);
            if (!motionBoundsState.mBoundsInMotion.isEmpty()) {
                PipTaskOrganizer pipTaskOrganizer = pipMotionHelper.mPipTaskOrganizer;
                Rect bounds = pipMotionHelper.getBounds();
                PipBoundsState pipBoundsState2 = pipMotionHelper.mPipBoundsState;
                Objects.requireNonNull(pipBoundsState2);
                PipBoundsState.MotionBoundsState motionBoundsState2 = pipBoundsState2.mMotionBoundsState;
                Objects.requireNonNull(motionBoundsState2);
                Rect rect2 = motionBoundsState2.mBoundsInMotion;
                Objects.requireNonNull(pipTaskOrganizer);
                pipTaskOrganizer.scheduleUserResizePip(bounds, rect2, 0.0f, null);
            }
        }
    };

    public final void animateToUnexpandedState(Rect rect, float f, Rect rect2, Rect rect3, boolean z) {
        if (f < 0.0f) {
            PipSnapAlgorithm pipSnapAlgorithm = this.mSnapAlgorithm;
            Rect rect4 = new Rect(getBounds());
            PipBoundsState pipBoundsState = this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState);
            f = pipSnapAlgorithm.getSnapFraction(rect4, rect3, pipBoundsState.mStashedState);
        }
        PipSnapAlgorithm pipSnapAlgorithm2 = this.mSnapAlgorithm;
        PipBoundsState pipBoundsState2 = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState2);
        int i = pipBoundsState2.mStashedState;
        PipBoundsState pipBoundsState3 = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState3);
        int i2 = pipBoundsState3.mStashOffset;
        Rect displayBounds = this.mPipBoundsState.getDisplayBounds();
        PipBoundsState pipBoundsState4 = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState4);
        DisplayLayout displayLayout = pipBoundsState4.mDisplayLayout;
        Objects.requireNonNull(displayLayout);
        Rect rect5 = displayLayout.mStableInsets;
        Objects.requireNonNull(pipSnapAlgorithm2);
        PipSnapAlgorithm.applySnapFraction(rect, rect2, f, i, i2, displayBounds, rect5);
        if (z) {
            movePip(rect, false);
            return;
        }
        this.mPipTaskOrganizer.scheduleAnimateResizePip(rect, 250, 8, null);
        PipBoundsState pipBoundsState5 = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState5);
        PipBoundsState.MotionBoundsState motionBoundsState = pipBoundsState5.mMotionBoundsState;
        Objects.requireNonNull(motionBoundsState);
        motionBoundsState.mAnimatingToBounds.set(rect);
        this.mFloatingContentCoordinator.onContentMoved(this);
    }

    public final void movetoTarget(float f, float f2, Runnable runnable, boolean z) {
        PhysicsAnimator.FlingConfig flingConfig;
        int i;
        int i2;
        PipBoundsState.MotionBoundsState motionBoundsState;
        this.mSpringingToTouch = false;
        PhysicsAnimator<Rect> physicsAnimator = this.mTemporaryBoundsPhysicsAnimator;
        PhysicsAnimator.SpringConfig springConfig = this.mSpringConfig;
        Objects.requireNonNull(physicsAnimator);
        physicsAnimator.spring(FloatProperties.RECT_WIDTH, getBounds().width(), 0.0f, springConfig);
        physicsAnimator.spring(FloatProperties.RECT_HEIGHT, getBounds().height(), 0.0f, this.mSpringConfig);
        FloatProperties$Companion$RECT_X$1 floatProperties$Companion$RECT_X$1 = FloatProperties.RECT_X;
        if (z) {
            flingConfig = this.mStashConfigX;
        } else {
            flingConfig = this.mFlingConfigX;
        }
        physicsAnimator.flingThenSpring(floatProperties$Companion$RECT_X$1, f, flingConfig, this.mSpringConfig, true);
        physicsAnimator.flingThenSpring(FloatProperties.RECT_Y, f2, this.mFlingConfigY, this.mSpringConfig, false);
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState);
        DisplayLayout displayLayout = pipBoundsState.mDisplayLayout;
        Objects.requireNonNull(displayLayout);
        Rect rect = displayLayout.mStableInsets;
        if (z) {
            PipBoundsState pipBoundsState2 = this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState2);
            i = (pipBoundsState2.mStashOffset - this.mPipBoundsState.getBounds().width()) + rect.left;
        } else {
            PipBoundsState pipBoundsState3 = this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState3);
            i = pipBoundsState3.mMovementBounds.left;
        }
        float f3 = i;
        if (z) {
            int i3 = this.mPipBoundsState.getDisplayBounds().right;
            PipBoundsState pipBoundsState4 = this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState4);
            i2 = (i3 - pipBoundsState4.mStashOffset) - rect.right;
        } else {
            PipBoundsState pipBoundsState5 = this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState5);
            i2 = pipBoundsState5.mMovementBounds.right;
        }
        float f4 = i2;
        if (f >= 0.0f) {
            f3 = f4;
        }
        PipBoundsState pipBoundsState6 = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState6);
        Objects.requireNonNull(pipBoundsState6.mMotionBoundsState);
        PhysicsAnimator.FlingConfig flingConfig2 = this.mFlingConfigY;
        startBoundsAnimator(f3, Math.min(flingConfig2.max, Math.max(flingConfig2.min, motionBoundsState.mBoundsInMotion.top + (f2 / (flingConfig2.friction * 4.2f)))), runnable);
    }

    public final float animateToExpandedState(Rect rect, Rect rect2, Rect rect3, Runnable runnable) {
        PipSnapAlgorithm pipSnapAlgorithm = this.mSnapAlgorithm;
        Rect rect4 = new Rect(getBounds());
        Objects.requireNonNull(pipSnapAlgorithm);
        float snapFraction = pipSnapAlgorithm.getSnapFraction(rect4, rect2, 0);
        Objects.requireNonNull(this.mSnapAlgorithm);
        PipSnapAlgorithm.applySnapFraction(rect, rect3, snapFraction);
        this.mPostPipTransitionCallback = runnable;
        this.mPipTaskOrganizer.scheduleAnimateResizePip(rect, 250, 8, null);
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState);
        PipBoundsState.MotionBoundsState motionBoundsState = pipBoundsState.mMotionBoundsState;
        Objects.requireNonNull(motionBoundsState);
        motionBoundsState.mAnimatingToBounds.set(rect);
        this.mFloatingContentCoordinator.onContentMoved(this);
        return snapFraction;
    }

    public final void cancelPhysicsAnimation() {
        this.mTemporaryBoundsPhysicsAnimator.cancel();
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState);
        PipBoundsState.MotionBoundsState motionBoundsState = pipBoundsState.mMotionBoundsState;
        Objects.requireNonNull(motionBoundsState);
        motionBoundsState.mAnimatingToBounds.setEmpty();
        this.mSpringingToTouch = false;
    }

    public final Rect getBounds() {
        return this.mPipBoundsState.getBounds();
    }

    @Override // com.android.wm.shell.common.FloatingContentCoordinator.FloatingContent
    public final Rect getFloatingBoundsOnScreen() {
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState);
        PipBoundsState.MotionBoundsState motionBoundsState = pipBoundsState.mMotionBoundsState;
        Objects.requireNonNull(motionBoundsState);
        if (motionBoundsState.mAnimatingToBounds.isEmpty()) {
            return getBounds();
        }
        PipBoundsState pipBoundsState2 = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState2);
        PipBoundsState.MotionBoundsState motionBoundsState2 = pipBoundsState2.mMotionBoundsState;
        Objects.requireNonNull(motionBoundsState2);
        return motionBoundsState2.mAnimatingToBounds;
    }

    public final void movePip(Rect rect, boolean z) {
        if (!z) {
            this.mFloatingContentCoordinator.onContentMoved(this);
        }
        if (!this.mSpringingToTouch) {
            cancelPhysicsAnimation();
            if (!z) {
                if (!rect.equals(getBounds())) {
                    PipTaskOrganizer pipTaskOrganizer = this.mPipTaskOrganizer;
                    PipMotionHelper$$ExternalSyntheticLambda1 pipMotionHelper$$ExternalSyntheticLambda1 = this.mUpdateBoundsCallback;
                    Objects.requireNonNull(pipTaskOrganizer);
                    if (pipTaskOrganizer.mToken == null || pipTaskOrganizer.mLeash == null) {
                        Log.w("PipTaskOrganizer", "Abort animation, invalid leash");
                    } else {
                        pipTaskOrganizer.mPipBoundsState.setBounds(rect);
                        Objects.requireNonNull(pipTaskOrganizer.mSurfaceControlTransactionFactory);
                        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                        PipSurfaceTransactionHelper pipSurfaceTransactionHelper = pipTaskOrganizer.mSurfaceTransactionHelper;
                        pipSurfaceTransactionHelper.crop(transaction, pipTaskOrganizer.mLeash, rect);
                        pipSurfaceTransactionHelper.round(transaction, pipTaskOrganizer.mLeash, pipTaskOrganizer.mPipTransitionState.isInPip());
                        if (pipTaskOrganizer.mPipMenuController.isMenuVisible()) {
                            pipTaskOrganizer.mPipMenuController.resizePipMenu(pipTaskOrganizer.mLeash, transaction, rect);
                        } else {
                            transaction.apply();
                        }
                        if (pipMotionHelper$$ExternalSyntheticLambda1 != null) {
                            pipMotionHelper$$ExternalSyntheticLambda1.accept(rect);
                        }
                    }
                }
                this.mPipBoundsState.setBounds(rect);
                return;
            }
            PipBoundsState pipBoundsState = this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState);
            PipBoundsState.MotionBoundsState motionBoundsState = pipBoundsState.mMotionBoundsState;
            Objects.requireNonNull(motionBoundsState);
            motionBoundsState.mBoundsInMotion.set(rect);
            PipTaskOrganizer pipTaskOrganizer2 = this.mPipTaskOrganizer;
            Rect bounds = getBounds();
            ShellTaskOrganizer$$ExternalSyntheticLambda1 shellTaskOrganizer$$ExternalSyntheticLambda1 = new ShellTaskOrganizer$$ExternalSyntheticLambda1(this, 2);
            Objects.requireNonNull(pipTaskOrganizer2);
            pipTaskOrganizer2.scheduleUserResizePip(bounds, rect, 0.0f, shellTaskOrganizer$$ExternalSyntheticLambda1);
            return;
        }
        PhysicsAnimator<Rect> physicsAnimator = this.mTemporaryBoundsPhysicsAnimator;
        PhysicsAnimator.SpringConfig springConfig = this.mCatchUpSpringConfig;
        Objects.requireNonNull(physicsAnimator);
        physicsAnimator.spring(FloatProperties.RECT_WIDTH, getBounds().width(), 0.0f, springConfig);
        physicsAnimator.spring(FloatProperties.RECT_HEIGHT, getBounds().height(), 0.0f, this.mCatchUpSpringConfig);
        physicsAnimator.spring(FloatProperties.RECT_X, rect.left, 0.0f, this.mCatchUpSpringConfig);
        physicsAnimator.spring(FloatProperties.RECT_Y, rect.top, 0.0f, this.mCatchUpSpringConfig);
        startBoundsAnimator(rect.left, rect.top, null);
    }

    @Override // com.android.wm.shell.common.FloatingContentCoordinator.FloatingContent
    public final void moveToBounds(Rect rect) {
        PhysicsAnimator.SpringConfig springConfig = this.mConflictResolutionSpringConfig;
        if (!this.mTemporaryBoundsPhysicsAnimator.isRunning()) {
            PipBoundsState pipBoundsState = this.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState);
            PipBoundsState.MotionBoundsState motionBoundsState = pipBoundsState.mMotionBoundsState;
            Rect bounds = getBounds();
            Objects.requireNonNull(motionBoundsState);
            motionBoundsState.mBoundsInMotion.set(bounds);
        }
        PhysicsAnimator<Rect> physicsAnimator = this.mTemporaryBoundsPhysicsAnimator;
        Objects.requireNonNull(physicsAnimator);
        physicsAnimator.spring(FloatProperties.RECT_X, rect.left, 0.0f, springConfig);
        physicsAnimator.spring(FloatProperties.RECT_Y, rect.top, 0.0f, springConfig);
        startBoundsAnimator(rect.left, rect.top, null);
    }

    public final void startBoundsAnimator(float f, float f2, Runnable runnable) {
        if (!this.mSpringingToTouch) {
            cancelPhysicsAnimation();
        }
        int i = (int) f;
        int i2 = (int) f2;
        Rect rect = new Rect(i, i2, getBounds().width() + i, getBounds().height() + i2);
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState);
        PipBoundsState.MotionBoundsState motionBoundsState = pipBoundsState.mMotionBoundsState;
        Objects.requireNonNull(motionBoundsState);
        motionBoundsState.mAnimatingToBounds.set(rect);
        this.mFloatingContentCoordinator.onContentMoved(this);
        if (!this.mTemporaryBoundsPhysicsAnimator.isRunning()) {
            if (runnable != null) {
                PhysicsAnimator<Rect> physicsAnimator = this.mTemporaryBoundsPhysicsAnimator;
                PipMotionHelper$$ExternalSyntheticLambda0 pipMotionHelper$$ExternalSyntheticLambda0 = this.mResizePipUpdateListener;
                Objects.requireNonNull(physicsAnimator);
                physicsAnimator.updateListeners.add(pipMotionHelper$$ExternalSyntheticLambda0);
                physicsAnimator.withEndActions(new QSFgsManagerFooter$$ExternalSyntheticLambda0(this, 10), runnable);
            } else {
                PhysicsAnimator<Rect> physicsAnimator2 = this.mTemporaryBoundsPhysicsAnimator;
                PipMotionHelper$$ExternalSyntheticLambda0 pipMotionHelper$$ExternalSyntheticLambda02 = this.mResizePipUpdateListener;
                Objects.requireNonNull(physicsAnimator2);
                physicsAnimator2.updateListeners.add(pipMotionHelper$$ExternalSyntheticLambda02);
                physicsAnimator2.withEndActions(new DozeScreenState$$ExternalSyntheticLambda0(this, 8));
            }
        }
        this.mTemporaryBoundsPhysicsAnimator.start();
    }

    public static void $r8$lambda$QFpQr4PSFRGfS8YBsx6HKEKo4u4(PipMotionHelper pipMotionHelper) {
        Objects.requireNonNull(pipMotionHelper);
        if (!pipMotionHelper.mDismissalPending && !pipMotionHelper.mSpringingToTouch && !pipMotionHelper.mMagnetizedPip.getObjectStuckToTarget()) {
            PipBoundsState pipBoundsState = pipMotionHelper.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState);
            PipBoundsState.MotionBoundsState motionBoundsState = pipBoundsState.mMotionBoundsState;
            Objects.requireNonNull(motionBoundsState);
            pipBoundsState.setBounds(motionBoundsState.mBoundsInMotion);
            PipBoundsState pipBoundsState2 = pipMotionHelper.mPipBoundsState;
            Objects.requireNonNull(pipBoundsState2);
            PipBoundsState.MotionBoundsState motionBoundsState2 = pipBoundsState2.mMotionBoundsState;
            Objects.requireNonNull(motionBoundsState2);
            motionBoundsState2.mBoundsInMotion.setEmpty();
            if (!pipMotionHelper.mDismissalPending) {
                PipTaskOrganizer pipTaskOrganizer = pipMotionHelper.mPipTaskOrganizer;
                Rect bounds = pipMotionHelper.getBounds();
                Objects.requireNonNull(pipTaskOrganizer);
                pipTaskOrganizer.scheduleFinishResizePip(bounds, 0, null);
            }
        }
        PipBoundsState pipBoundsState3 = pipMotionHelper.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState3);
        PipBoundsState.MotionBoundsState motionBoundsState3 = pipBoundsState3.mMotionBoundsState;
        Objects.requireNonNull(motionBoundsState3);
        motionBoundsState3.mAnimatingToBounds.setEmpty();
        pipMotionHelper.mSpringingToTouch = false;
        pipMotionHelper.mDismissalPending = false;
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [com.android.wm.shell.pip.phone.PipMotionHelper$2, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r4v2, types: [com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda0] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public PipMotionHelper(android.content.Context r4, com.android.wm.shell.pip.PipBoundsState r5, com.android.wm.shell.pip.PipTaskOrganizer r6, com.android.wm.shell.pip.phone.PhonePipMenuController r7, com.android.wm.shell.pip.PipSnapAlgorithm r8, com.android.wm.shell.pip.PipTransitionController r9, com.android.wm.shell.common.FloatingContentCoordinator r10) {
        /*
            r3 = this;
            r3.<init>()
            android.graphics.Rect r0 = new android.graphics.Rect
            r0.<init>()
            r3.mFloatingAllowedArea = r0
            com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda2 r0 = new com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda2
            r0.<init>()
            java.lang.ThreadLocal r0 = java.lang.ThreadLocal.withInitial(r0)
            r3.mSfAnimationHandlerThreadLocal = r0
            com.android.wm.shell.animation.PhysicsAnimator$SpringConfig r0 = new com.android.wm.shell.animation.PhysicsAnimator$SpringConfig
            r1 = 1143930880(0x442f0000, float:700.0)
            r2 = 1065353216(0x3f800000, float:1.0)
            r0.<init>(r1, r2)
            r3.mSpringConfig = r0
            com.android.wm.shell.animation.PhysicsAnimator$SpringConfig r0 = new com.android.wm.shell.animation.PhysicsAnimator$SpringConfig
            r1 = 1153138688(0x44bb8000, float:1500.0)
            r0.<init>(r1, r2)
            r3.mAnimateToDismissSpringConfig = r0
            com.android.wm.shell.animation.PhysicsAnimator$SpringConfig r0 = new com.android.wm.shell.animation.PhysicsAnimator$SpringConfig
            r1 = 1167867904(0x459c4000, float:5000.0)
            r0.<init>(r1, r2)
            r3.mCatchUpSpringConfig = r0
            com.android.wm.shell.animation.PhysicsAnimator$SpringConfig r0 = new com.android.wm.shell.animation.PhysicsAnimator$SpringConfig
            r1 = 1128792064(0x43480000, float:200.0)
            r0.<init>(r1, r2)
            r3.mConflictResolutionSpringConfig = r0
            com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda1 r0 = new com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda1
            r1 = 0
            r0.<init>(r3, r1)
            r3.mUpdateBoundsCallback = r0
            r3.mSpringingToTouch = r1
            r3.mDismissalPending = r1
            com.android.wm.shell.pip.phone.PipMotionHelper$2 r0 = new com.android.wm.shell.pip.phone.PipMotionHelper$2
            r0.<init>()
            r3.mPipTransitionCallback = r0
            r3.mContext = r4
            r3.mPipTaskOrganizer = r6
            r3.mPipBoundsState = r5
            r3.mMenuController = r7
            r3.mSnapAlgorithm = r8
            r3.mFloatingContentCoordinator = r10
            java.util.Objects.requireNonNull(r9)
            java.util.ArrayList r4 = r9.mPipTransitionCallbacks
            r4.add(r0)
            com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda0 r4 = new com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda0
            r4.<init>()
            r3.mResizePipUpdateListener = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.pip.phone.PipMotionHelper.<init>(android.content.Context, com.android.wm.shell.pip.PipBoundsState, com.android.wm.shell.pip.PipTaskOrganizer, com.android.wm.shell.pip.phone.PhonePipMenuController, com.android.wm.shell.pip.PipSnapAlgorithm, com.android.wm.shell.pip.PipTransitionController, com.android.wm.shell.common.FloatingContentCoordinator):void");
    }

    public final void animateToOffset(Rect rect, int i) {
        boolean z;
        cancelPhysicsAnimation();
        PipTaskOrganizer pipTaskOrganizer = this.mPipTaskOrganizer;
        PipMotionHelper$$ExternalSyntheticLambda1 pipMotionHelper$$ExternalSyntheticLambda1 = this.mUpdateBoundsCallback;
        Objects.requireNonNull(pipTaskOrganizer);
        PipTransitionState pipTransitionState = pipTaskOrganizer.mPipTransitionState;
        Objects.requireNonNull(pipTransitionState);
        int i2 = pipTransitionState.mState;
        if (i2 < 3 || i2 == 5) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            if (pipTaskOrganizer.mWaitForFixedRotation) {
                Log.d("PipTaskOrganizer", "skip scheduleOffsetPip, entering pip deferred");
                return;
            }
            if (pipTaskOrganizer.mTaskInfo == null) {
                Log.w("PipTaskOrganizer", "mTaskInfo is not set");
            } else {
                Rect rect2 = new Rect(rect);
                rect2.offset(0, i);
                pipTaskOrganizer.animateResizePip(rect, rect2, null, 1, 300, 0.0f);
            }
            Rect rect3 = new Rect(rect);
            rect3.offset(0, i);
            if (pipMotionHelper$$ExternalSyntheticLambda1 != null) {
                pipMotionHelper$$ExternalSyntheticLambda1.accept(rect3);
            }
        }
    }

    public final void dismissPip() {
        cancelPhysicsAnimation();
        this.mMenuController.hideMenu(2);
        this.mPipTaskOrganizer.removePip();
    }

    public final void expandLeavePip(boolean z, boolean z2) {
        cancelPhysicsAnimation();
        int i = 0;
        this.mMenuController.hideMenu(0);
        PipTaskOrganizer pipTaskOrganizer = this.mPipTaskOrganizer;
        if (!z) {
            i = 300;
        }
        pipTaskOrganizer.exitPip(i, z2);
    }

    public final void synchronizePinnedStackBounds() {
        cancelPhysicsAnimation();
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState);
        PipBoundsState.MotionBoundsState motionBoundsState = pipBoundsState.mMotionBoundsState;
        Objects.requireNonNull(motionBoundsState);
        motionBoundsState.mBoundsInMotion.setEmpty();
        if (this.mPipTaskOrganizer.isInPip()) {
            this.mFloatingContentCoordinator.onContentMoved(this);
        }
    }

    @Override // com.android.wm.shell.common.FloatingContentCoordinator.FloatingContent
    public final Rect getAllowedFloatingBoundsRegion() {
        return this.mFloatingAllowedArea;
    }
}
