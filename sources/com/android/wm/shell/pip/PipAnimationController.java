package com.android.wm.shell.pip;

import android.animation.AnimationHandler;
import android.animation.Animator;
import android.animation.RectEvaluator;
import android.animation.ValueAnimator;
import android.app.TaskInfo;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.RotationUtils;
import android.view.Choreographer;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import androidx.fragment.app.DialogFragment$$ExternalSyntheticOutline0;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PipAnimationController {
    public PipTransitionAnimator mCurrentAnimator;
    public final ThreadLocal<AnimationHandler> mSfAnimationHandlerThreadLocal = ThreadLocal.withInitial(PipAnimationController$$ExternalSyntheticLambda0.INSTANCE);
    public final PipSurfaceTransactionHelper mSurfaceTransactionHelper;

    /* loaded from: classes.dex */
    public static class PipAnimationCallback {
        public void onPipAnimationCancel(PipTransitionAnimator pipTransitionAnimator) {
            throw null;
        }

        public void onPipAnimationEnd(TaskInfo taskInfo, SurfaceControl.Transaction transaction, PipTransitionAnimator pipTransitionAnimator) {
            throw null;
        }

        public void onPipAnimationStart(PipTransitionAnimator pipTransitionAnimator) {
            throw null;
        }
    }

    /* loaded from: classes.dex */
    public static class PipTransactionHandler {
    }

    /* loaded from: classes.dex */
    public static abstract class PipTransitionAnimator<T> extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
        public static final /* synthetic */ int $r8$clinit = 0;
        public final int mAnimationType;
        public T mBaseValue;
        public SurfaceControl mContentOverlay;
        public T mCurrentValue;
        public final Rect mDestinationBounds;
        public T mEndValue;
        public final SurfaceControl mLeash;
        public PipAnimationCallback mPipAnimationCallback;
        public PipTransactionHandler mPipTransactionHandler;
        public T mStartValue;
        public PipSurfaceTransactionHelper.SurfaceControlTransactionFactory mSurfaceControlTransactionFactory;
        public PipSurfaceTransactionHelper mSurfaceTransactionHelper;
        public final TaskInfo mTaskInfo;
        public int mTransitionDirection;

        public PipTransitionAnimator() {
            throw null;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public PipTransitionAnimator(TaskInfo taskInfo, SurfaceControl surfaceControl, int i, Rect rect, Object obj, Object obj2, Object obj3) {
            Rect rect2 = new Rect();
            this.mDestinationBounds = rect2;
            this.mTaskInfo = taskInfo;
            this.mLeash = surfaceControl;
            this.mAnimationType = i;
            rect2.set(rect);
            this.mBaseValue = obj;
            this.mStartValue = obj2;
            this.mEndValue = obj3;
            addListener(this);
            addUpdateListener(this);
            this.mSurfaceControlTransactionFactory = DialogFragment$$ExternalSyntheticOutline0.INSTANCE;
            this.mTransitionDirection = 0;
        }

        public abstract void applySurfaceControlTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, float f);

        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationRepeat(Animator animator) {
        }

        public void onEndTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, int i) {
        }

        public void onStartTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        }

        @VisibleForTesting
        public PipTransitionAnimator<T> setTransitionDirection(int i) {
            if (i != 1) {
                this.mTransitionDirection = i;
            }
            return this;
        }

        /* JADX WARN: Type inference failed for: r21v0, types: [com.android.wm.shell.pip.PipAnimationController$PipTransitionAnimator$2] */
        public static AnonymousClass2 ofBounds(TaskInfo taskInfo, SurfaceControl surfaceControl, Rect rect, Rect rect2, Rect rect3, Rect rect4, int i, float f, int i2) {
            Rect rect5;
            Rect rect6;
            Rect rect7;
            Rect rect8;
            Rect rect9;
            Rect rect10;
            boolean isOutPipDirection = PipAnimationController.isOutPipDirection(i);
            if (isOutPipDirection) {
                rect5 = new Rect(rect3);
            } else {
                rect5 = new Rect(rect);
            }
            if (i2 == 1 || i2 == 3) {
                Rect rect11 = new Rect(rect3);
                Rect rect12 = new Rect(rect3);
                RotationUtils.rotateBounds(rect12, rect5, i2);
                if (isOutPipDirection) {
                    rect10 = rect12;
                } else {
                    rect10 = rect5;
                }
                rect6 = rect11;
                rect8 = rect12;
                rect7 = rect10;
            } else {
                rect8 = null;
                rect6 = null;
                rect7 = rect5;
            }
            if (rect4 == null) {
                rect9 = null;
            } else {
                rect9 = new Rect(rect4.left - rect7.left, rect4.top - rect7.top, rect7.right - rect4.right, rect7.bottom - rect4.bottom);
            }
            return new PipTransitionAnimator<Rect>(taskInfo, surfaceControl, rect3, new Rect(rect), new Rect(rect2), new Rect(rect3), f, rect8, f, rect4, isOutPipDirection, rect5, rect7, rect6, rect3, i2, rect9, new Rect(0, 0, 0, 0), i) { // from class: com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator.2
                public final /* synthetic */ int val$direction;
                public final /* synthetic */ Rect val$endValue;
                public final /* synthetic */ Rect val$initialContainerRect;
                public final /* synthetic */ Rect val$initialSourceValue;
                public final /* synthetic */ boolean val$isOutPipDirection;
                public final /* synthetic */ Rect val$lastEndRect;
                public final /* synthetic */ Rect val$rotatedEndRect;
                public final /* synthetic */ int val$rotationDelta;
                public final /* synthetic */ Rect val$sourceHintRect;
                public final /* synthetic */ Rect val$sourceHintRectInsets;
                public final /* synthetic */ float val$startingAngle;
                public final /* synthetic */ Rect val$zeroInsets;
                public final RectEvaluator mRectEvaluator = new RectEvaluator(new Rect());
                public final RectEvaluator mInsetsEvaluator = new RectEvaluator(new Rect());

                {
                    this.val$rotatedEndRect = rect8;
                    this.val$startingAngle = f;
                    this.val$sourceHintRect = rect4;
                    this.val$isOutPipDirection = isOutPipDirection;
                    this.val$initialSourceValue = rect5;
                    this.val$initialContainerRect = rect7;
                    this.val$lastEndRect = rect6;
                    this.val$endValue = rect3;
                    this.val$rotationDelta = i2;
                    this.val$sourceHintRectInsets = rect9;
                    this.val$zeroInsets = r21;
                    this.val$direction = i;
                }

                /* JADX WARN: Multi-variable type inference failed */
                /* JADX WARN: Removed duplicated region for block: B:75:0x01e7  */
                /* JADX WARN: Removed duplicated region for block: B:76:0x01f6  */
                /* JADX WARN: Removed duplicated region for block: B:79:0x020c  */
                /* JADX WARN: Removed duplicated region for block: B:83:0x0222  */
                /* JADX WARN: Removed duplicated region for block: B:85:? A[RETURN, SYNTHETIC] */
                /* JADX WARN: Type inference failed for: r15v0, types: [android.graphics.Rect, T] */
                /* JADX WARN: Type inference failed for: r9v0, types: [android.graphics.Rect, T] */
                /* JADX WARN: Unknown variable types count: 2 */
                @Override // com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final void applySurfaceControlTransaction(android.view.SurfaceControl r20, android.view.SurfaceControl.Transaction r21, float r22) {
                    /*
                        Method dump skipped, instructions count: 550
                        To view this dump add '--comments-level debug' option
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator.AnonymousClass2.applySurfaceControlTransaction(android.view.SurfaceControl, android.view.SurfaceControl$Transaction, float):void");
                }

                @Override // com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator
                public final void onEndTransaction(SurfaceControl surfaceControl2, SurfaceControl.Transaction transaction, int i3) {
                    Rect rect13 = this.mDestinationBounds;
                    this.mSurfaceTransactionHelper.resetScale(transaction, surfaceControl2, rect13);
                    if (PipAnimationController.isOutPipDirection(i3)) {
                        transaction.setMatrix(surfaceControl2, 1.0f, 0.0f, 0.0f, 1.0f);
                        transaction.setPosition(surfaceControl2, 0.0f, 0.0f);
                        transaction.setWindowCrop(surfaceControl2, 0, 0);
                        return;
                    }
                    this.mSurfaceTransactionHelper.crop(transaction, surfaceControl2, rect13);
                }

                @Override // com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator
                public final void onStartTransaction(SurfaceControl surfaceControl2, SurfaceControl.Transaction transaction) {
                    PipSurfaceTransactionHelper pipSurfaceTransactionHelper = this.mSurfaceTransactionHelper;
                    Objects.requireNonNull(pipSurfaceTransactionHelper);
                    transaction.setAlpha(surfaceControl2, 1.0f);
                    pipSurfaceTransactionHelper.round(transaction, surfaceControl2, !PipAnimationController.isOutPipDirection(this.mTransitionDirection));
                    if (PipAnimationController.isInPipDirection(this.val$direction)) {
                        transaction.setWindowCrop(surfaceControl2, (Rect) this.mStartValue);
                    }
                    transaction.show(surfaceControl2);
                    transaction.apply();
                }

                @Override // com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator
                public final void updateEndValue(Rect rect13) {
                    T t;
                    this.mEndValue = (T) rect13;
                    T t2 = this.mStartValue;
                    if (t2 != null && (t = this.mCurrentValue) != null) {
                        ((Rect) t2).set((Rect) t);
                    }
                }
            };
        }

        public final SurfaceControl.Transaction newSurfaceControlTransaction() {
            SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
            transaction.setFrameTimelineVsync(Choreographer.getSfInstance().getVsyncId());
            return transaction;
        }

        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationCancel(Animator animator) {
            PipAnimationCallback pipAnimationCallback = this.mPipAnimationCallback;
            if (pipAnimationCallback != null) {
                pipAnimationCallback.onPipAnimationCancel(this);
            }
            this.mTransitionDirection = 0;
        }

        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            this.mCurrentValue = this.mEndValue;
            SurfaceControl.Transaction newSurfaceControlTransaction = newSurfaceControlTransaction();
            onEndTransaction(this.mLeash, newSurfaceControlTransaction, this.mTransitionDirection);
            PipAnimationCallback pipAnimationCallback = this.mPipAnimationCallback;
            if (pipAnimationCallback != null) {
                pipAnimationCallback.onPipAnimationEnd(this.mTaskInfo, newSurfaceControlTransaction, this);
            }
            this.mTransitionDirection = 0;
        }

        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationStart(Animator animator) {
            this.mCurrentValue = this.mStartValue;
            onStartTransaction(this.mLeash, newSurfaceControlTransaction());
            PipAnimationCallback pipAnimationCallback = this.mPipAnimationCallback;
            if (pipAnimationCallback != null) {
                pipAnimationCallback.onPipAnimationStart(this);
            }
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            applySurfaceControlTransaction(this.mLeash, newSurfaceControlTransaction(), valueAnimator.getAnimatedFraction());
        }

        public final void setDestinationBounds(Rect rect) {
            this.mDestinationBounds.set(rect);
            if (this.mAnimationType == 1) {
                onStartTransaction(this.mLeash, newSurfaceControlTransaction());
            }
        }

        /* JADX WARN: Finally extract failed */
        public final PipTransitionAnimator<T> setUseContentOverlay(Context context) {
            SurfaceControl.Transaction newSurfaceControlTransaction = newSurfaceControlTransaction();
            SurfaceControl surfaceControl = this.mContentOverlay;
            if (surfaceControl != null) {
                newSurfaceControlTransaction.remove(surfaceControl);
                newSurfaceControlTransaction.apply();
            }
            SurfaceControl build = new SurfaceControl.Builder(new SurfaceSession()).setCallsite("PipAnimation").setName("PipContentOverlay").setColorLayer().build();
            this.mContentOverlay = build;
            newSurfaceControlTransaction.show(build);
            newSurfaceControlTransaction.setLayer(this.mContentOverlay, Integer.MAX_VALUE);
            SurfaceControl surfaceControl2 = this.mContentOverlay;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{16842801});
            try {
                int color = obtainStyledAttributes.getColor(0, 0);
                float[] fArr = {Color.red(color) / 255.0f, Color.green(color) / 255.0f, Color.blue(color) / 255.0f};
                obtainStyledAttributes.recycle();
                newSurfaceControlTransaction.setColor(surfaceControl2, fArr);
                newSurfaceControlTransaction.setAlpha(this.mContentOverlay, 0.0f);
                newSurfaceControlTransaction.reparent(this.mContentOverlay, this.mLeash);
                newSurfaceControlTransaction.apply();
                return this;
            } catch (Throwable th) {
                obtainStyledAttributes.recycle();
                throw th;
            }
        }

        @VisibleForTesting
        public PipTransitionAnimator<T> setPipAnimationCallback(PipAnimationCallback pipAnimationCallback) {
            this.mPipAnimationCallback = pipAnimationCallback;
            return this;
        }

        @VisibleForTesting
        public void setSurfaceControlTransactionFactory(PipSurfaceTransactionHelper.SurfaceControlTransactionFactory surfaceControlTransactionFactory) {
            this.mSurfaceControlTransactionFactory = surfaceControlTransactionFactory;
        }

        public void updateEndValue(T t) {
            this.mEndValue = t;
        }

        @VisibleForTesting
        public int getAnimationType() {
            return this.mAnimationType;
        }

        @VisibleForTesting
        public T getEndValue() {
            return this.mEndValue;
        }

        @VisibleForTesting
        public int getTransitionDirection() {
            return this.mTransitionDirection;
        }
    }

    public static boolean isInPipDirection(int i) {
        return i == 2;
    }

    public static boolean isOutPipDirection(int i) {
        return i == 3 || i == 4;
    }

    @VisibleForTesting
    public PipTransitionAnimator getAnimator(TaskInfo taskInfo, SurfaceControl surfaceControl, Rect rect, float f, float f2) {
        PipTransitionAnimator pipTransitionAnimator = this.mCurrentAnimator;
        if (pipTransitionAnimator == null) {
            int i = PipTransitionAnimator.$r8$clinit;
            PipTransitionAnimator<Float> pipTransitionAnimator2 = new PipTransitionAnimator<Float>(taskInfo, surfaceControl, rect, Float.valueOf(f), Float.valueOf(f), Float.valueOf(f2)) { // from class: com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator.1
                @Override // com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator
                public final void applySurfaceControlTransaction(SurfaceControl surfaceControl2, SurfaceControl.Transaction transaction, float f3) {
                    float floatValue = (getEndValue().floatValue() * f3) + ((1.0f - f3) * ((Float) this.mStartValue).floatValue());
                    this.mCurrentValue = (T) Float.valueOf(floatValue);
                    PipSurfaceTransactionHelper pipSurfaceTransactionHelper = this.mSurfaceTransactionHelper;
                    Objects.requireNonNull(pipSurfaceTransactionHelper);
                    transaction.setAlpha(surfaceControl2, floatValue);
                    pipSurfaceTransactionHelper.round(transaction, surfaceControl2, !PipAnimationController.isOutPipDirection(this.mTransitionDirection));
                    transaction.apply();
                }

                @Override // com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator
                public final void updateEndValue(Float f3) {
                    this.mEndValue = (T) f3;
                    this.mStartValue = this.mCurrentValue;
                }

                @Override // com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator
                public final void onStartTransaction(SurfaceControl surfaceControl2, SurfaceControl.Transaction transaction) {
                    if (getTransitionDirection() != 5) {
                        PipSurfaceTransactionHelper pipSurfaceTransactionHelper = this.mSurfaceTransactionHelper;
                        pipSurfaceTransactionHelper.resetScale(transaction, surfaceControl2, this.mDestinationBounds);
                        pipSurfaceTransactionHelper.crop(transaction, surfaceControl2, this.mDestinationBounds);
                        pipSurfaceTransactionHelper.round(transaction, surfaceControl2, !PipAnimationController.isOutPipDirection(this.mTransitionDirection));
                        transaction.show(surfaceControl2);
                        transaction.apply();
                    }
                }
            };
            setupPipTransitionAnimator(pipTransitionAnimator2);
            this.mCurrentAnimator = pipTransitionAnimator2;
        } else {
            if (pipTransitionAnimator.getAnimationType() == 1) {
                PipTransitionAnimator pipTransitionAnimator3 = this.mCurrentAnimator;
                Objects.requireNonNull(pipTransitionAnimator3);
                if (Objects.equals(rect, pipTransitionAnimator3.mDestinationBounds) && this.mCurrentAnimator.isRunning()) {
                    this.mCurrentAnimator.updateEndValue(Float.valueOf(f2));
                }
            }
            this.mCurrentAnimator.cancel();
            PipTransitionAnimator<Float> pipTransitionAnimator4 = new PipTransitionAnimator<Float>(taskInfo, surfaceControl, rect, Float.valueOf(f), Float.valueOf(f), Float.valueOf(f2)) { // from class: com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator.1
                @Override // com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator
                public final void applySurfaceControlTransaction(SurfaceControl surfaceControl2, SurfaceControl.Transaction transaction, float f3) {
                    float floatValue = (getEndValue().floatValue() * f3) + ((1.0f - f3) * ((Float) this.mStartValue).floatValue());
                    this.mCurrentValue = (T) Float.valueOf(floatValue);
                    PipSurfaceTransactionHelper pipSurfaceTransactionHelper = this.mSurfaceTransactionHelper;
                    Objects.requireNonNull(pipSurfaceTransactionHelper);
                    transaction.setAlpha(surfaceControl2, floatValue);
                    pipSurfaceTransactionHelper.round(transaction, surfaceControl2, !PipAnimationController.isOutPipDirection(this.mTransitionDirection));
                    transaction.apply();
                }

                @Override // com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator
                public final void updateEndValue(Float f3) {
                    this.mEndValue = (T) f3;
                    this.mStartValue = this.mCurrentValue;
                }

                @Override // com.android.wm.shell.pip.PipAnimationController.PipTransitionAnimator
                public final void onStartTransaction(SurfaceControl surfaceControl2, SurfaceControl.Transaction transaction) {
                    if (getTransitionDirection() != 5) {
                        PipSurfaceTransactionHelper pipSurfaceTransactionHelper = this.mSurfaceTransactionHelper;
                        pipSurfaceTransactionHelper.resetScale(transaction, surfaceControl2, this.mDestinationBounds);
                        pipSurfaceTransactionHelper.crop(transaction, surfaceControl2, this.mDestinationBounds);
                        pipSurfaceTransactionHelper.round(transaction, surfaceControl2, !PipAnimationController.isOutPipDirection(this.mTransitionDirection));
                        transaction.show(surfaceControl2);
                        transaction.apply();
                    }
                }
            };
            setupPipTransitionAnimator(pipTransitionAnimator4);
            this.mCurrentAnimator = pipTransitionAnimator4;
        }
        return this.mCurrentAnimator;
    }

    public final PipTransitionAnimator setupPipTransitionAnimator(PipTransitionAnimator pipTransitionAnimator) {
        pipTransitionAnimator.mSurfaceTransactionHelper = this.mSurfaceTransactionHelper;
        pipTransitionAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        pipTransitionAnimator.setFloatValues(0.0f, 1.0f);
        pipTransitionAnimator.setAnimationHandler(this.mSfAnimationHandlerThreadLocal.get());
        return pipTransitionAnimator;
    }

    public PipAnimationController(PipSurfaceTransactionHelper pipSurfaceTransactionHelper) {
        this.mSurfaceTransactionHelper = pipSurfaceTransactionHelper;
    }

    @VisibleForTesting
    public PipTransitionAnimator getAnimator(TaskInfo taskInfo, SurfaceControl surfaceControl, Rect rect, Rect rect2, Rect rect3, Rect rect4, int i, float f, int i2) {
        PipTransitionAnimator pipTransitionAnimator = this.mCurrentAnimator;
        if (pipTransitionAnimator == null) {
            PipTransitionAnimator.AnonymousClass2 ofBounds = PipTransitionAnimator.ofBounds(taskInfo, surfaceControl, rect2, rect2, rect3, rect4, i, 0.0f, i2);
            setupPipTransitionAnimator(ofBounds);
            this.mCurrentAnimator = ofBounds;
        } else if (pipTransitionAnimator.getAnimationType() == 1 && this.mCurrentAnimator.isRunning()) {
            this.mCurrentAnimator.setDestinationBounds(rect3);
        } else if (this.mCurrentAnimator.getAnimationType() != 0 || !this.mCurrentAnimator.isRunning()) {
            this.mCurrentAnimator.cancel();
            PipTransitionAnimator.AnonymousClass2 ofBounds2 = PipTransitionAnimator.ofBounds(taskInfo, surfaceControl, rect, rect2, rect3, rect4, i, f, i2);
            setupPipTransitionAnimator(ofBounds2);
            this.mCurrentAnimator = ofBounds2;
        } else {
            this.mCurrentAnimator.setDestinationBounds(rect3);
            this.mCurrentAnimator.updateEndValue(new Rect(rect3));
        }
        return this.mCurrentAnimator;
    }
}
