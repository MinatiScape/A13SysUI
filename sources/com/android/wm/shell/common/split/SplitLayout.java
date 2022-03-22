package com.android.wm.shell.common.split;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Binder;
import android.view.Display;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import android.view.LayoutInflater;
import android.view.RoundedCorner;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowlessWindowManager;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.DividerSnapAlgorithm;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.split.SplitWindowManager;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SplitLayout implements DisplayInsetsController.OnInsetsChangedListener {
    public Context mContext;
    public final DismissingEffectPolicy mDismissingEffectPolicy;
    public final DisplayImeController mDisplayImeController;
    public int mDividePosition;
    public final int mDividerInsets;
    public final int mDividerSize;
    @VisibleForTesting
    public DividerSnapAlgorithm mDividerSnapAlgorithm;
    public final int mDividerWindowWidth;
    public final ImePositionProcessor mImePositionProcessor;
    public int mOrientation;
    public final Rect mRootBounds;
    public int mRotation;
    public final SplitLayoutHandler mSplitLayoutHandler;
    public final SplitWindowManager mSplitWindowManager;
    public final ShellTaskOrganizer mTaskOrganizer;
    public WindowContainerToken mWinToken1;
    public WindowContainerToken mWinToken2;
    public final Rect mTempRect = new Rect();
    public final Rect mDividerBounds = new Rect();
    public final Rect mBounds1 = new Rect();
    public final Rect mBounds2 = new Rect();
    public final Rect mWinBounds1 = new Rect();
    public final Rect mWinBounds2 = new Rect();
    public final InsetsState mInsetsState = new InsetsState();
    public boolean mInitialized = false;

    /* loaded from: classes.dex */
    public class DismissingEffectPolicy {
        public final boolean mApplyParallax;
        public int mDismissingSide = -1;
        public final Point mDismissingParallaxOffset = new Point();
        public float mDismissingDimValue = 0.0f;

        public DismissingEffectPolicy(boolean z) {
            this.mApplyParallax = z;
        }
    }

    /* loaded from: classes.dex */
    public class ImePositionProcessor implements DisplayImeController.ImePositionProcessor {
        public float mDimValue1;
        public float mDimValue2;
        public final int mDisplayId;
        public int mEndImeTop;
        public boolean mImeShown;
        public float mLastDim1;
        public float mLastDim2;
        public int mLastYOffset;
        public int mStartImeTop;
        public float mTargetDim1;
        public float mTargetDim2;
        public int mTargetYOffset;
        public int mYOffsetForIme;

        public ImePositionProcessor(int i) {
            this.mDisplayId = i;
        }

        @Override // com.android.wm.shell.common.DisplayImeController.ImePositionProcessor
        public final void onImeControlTargetChanged(int i, boolean z) {
            if (i == this.mDisplayId && !z && this.mImeShown) {
                this.mImeShown = false;
                this.mTargetYOffset = 0;
                this.mLastYOffset = 0;
                this.mYOffsetForIme = 0;
                this.mTargetDim1 = 0.0f;
                this.mLastDim1 = 0.0f;
                this.mDimValue1 = 0.0f;
                this.mTargetDim2 = 0.0f;
                this.mLastDim2 = 0.0f;
                this.mDimValue2 = 0.0f;
                SplitLayout.this.mSplitWindowManager.setInteractive(true);
                SplitLayout splitLayout = SplitLayout.this;
                splitLayout.mSplitLayoutHandler.onLayoutPositionChanging(splitLayout);
            }
        }

        @Override // com.android.wm.shell.common.DisplayImeController.ImePositionProcessor
        public final void onImeEndPositioning(int i, boolean z, SurfaceControl.Transaction transaction) {
            if (i == this.mDisplayId && !z) {
                onProgress(1.0f);
                SplitLayout splitLayout = SplitLayout.this;
                splitLayout.mSplitLayoutHandler.onLayoutPositionChanging(splitLayout);
            }
        }

        @Override // com.android.wm.shell.common.DisplayImeController.ImePositionProcessor
        public final void onImePositionChanged(int i, int i2, SurfaceControl.Transaction transaction) {
            int i3;
            if (i == this.mDisplayId) {
                onProgress((i2 - this.mStartImeTop) / (this.mEndImeTop - i3));
                SplitLayout splitLayout = SplitLayout.this;
                splitLayout.mSplitLayoutHandler.onLayoutPositionChanging(splitLayout);
            }
        }

        @Override // com.android.wm.shell.common.DisplayImeController.ImePositionProcessor
        public final int onImeStartPositioning(int i, int i2, int i3, boolean z, boolean z2) {
            int i4;
            float f;
            int i5;
            int i6;
            int i7 = this.mDisplayId;
            boolean z3 = false;
            if (i != i7) {
                return 0;
            }
            int splitItemPosition = SplitLayout.this.mSplitLayoutHandler.getSplitItemPosition(SplitLayout.this.mTaskOrganizer.getImeTarget(i7));
            SplitLayout splitLayout = SplitLayout.this;
            if (!splitLayout.mInitialized || splitItemPosition == -1) {
                return 0;
            }
            if (z) {
                i4 = i2;
            } else {
                i4 = i3;
            }
            this.mStartImeTop = i4;
            if (z) {
                i2 = i3;
            }
            this.mEndImeTop = i2;
            this.mImeShown = z;
            this.mLastDim1 = this.mDimValue1;
            float f2 = 0.3f;
            if (splitItemPosition != 1 || !z) {
                f = 0.0f;
            } else {
                f = 0.3f;
            }
            this.mTargetDim1 = f;
            this.mLastDim2 = this.mDimValue2;
            if (splitItemPosition != 0 || !z) {
                f2 = 0.0f;
            }
            this.mTargetDim2 = f2;
            this.mLastYOffset = this.mYOffsetForIme;
            if (splitItemPosition != 1 || z2 || SplitLayout.isLandscape(splitLayout.mRootBounds) || !z) {
                i5 = 0;
            } else {
                i5 = 1;
            }
            if (i5 != 0) {
                i6 = -Math.min(Math.abs(this.mEndImeTop - this.mStartImeTop), (int) (SplitLayout.this.mBounds1.height() * 0.7f));
            } else {
                i6 = 0;
            }
            this.mTargetYOffset = i6;
            int i8 = this.mLastYOffset;
            if (i6 != i8) {
                if (i6 == 0) {
                    SplitLayout splitLayout2 = SplitLayout.this;
                    splitLayout2.mSplitLayoutHandler.setLayoutOffsetTarget(0, splitLayout2);
                } else {
                    SplitLayout splitLayout3 = SplitLayout.this;
                    splitLayout3.mSplitLayoutHandler.setLayoutOffsetTarget(i6 - i8, splitLayout3);
                }
            }
            SplitWindowManager splitWindowManager = SplitLayout.this.mSplitWindowManager;
            if (!z || splitItemPosition == -1) {
                z3 = true;
            }
            splitWindowManager.setInteractive(z3);
            return i5;
        }

        public final void onProgress(float f) {
            float f2 = this.mLastDim1;
            this.mDimValue1 = MotionController$$ExternalSyntheticOutline0.m(this.mTargetDim1, f2, f, f2);
            float f3 = this.mLastDim2;
            this.mDimValue2 = MotionController$$ExternalSyntheticOutline0.m(this.mTargetDim2, f3, f, f3);
            float f4 = this.mLastYOffset;
            this.mYOffsetForIme = (int) MotionController$$ExternalSyntheticOutline0.m(this.mTargetYOffset, f4, f, f4);
        }
    }

    /* loaded from: classes.dex */
    public interface SplitLayoutHandler {
        int getSplitItemPosition(WindowContainerToken windowContainerToken);

        default void onDoubleTappedDivider() {
        }

        void onLayoutPositionChanging(SplitLayout splitLayout);

        void onLayoutSizeChanged(SplitLayout splitLayout);

        void onLayoutSizeChanging(SplitLayout splitLayout);

        void onSnappedToDismiss(boolean z);

        void setLayoutOffsetTarget(int i, SplitLayout splitLayout);
    }

    public static boolean isLandscape(Rect rect) {
        return rect.width() > rect.height();
    }

    public final void applyLayoutOffsetTarget(WindowContainerTransaction windowContainerTransaction, int i, ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
        if (i == 0) {
            windowContainerTransaction.setBounds(runningTaskInfo.token, this.mBounds1);
            windowContainerTransaction.setAppBounds(runningTaskInfo.token, (Rect) null);
            windowContainerTransaction.setScreenSizeDp(runningTaskInfo.token, 0, 0);
            windowContainerTransaction.setBounds(runningTaskInfo2.token, this.mBounds2);
            windowContainerTransaction.setAppBounds(runningTaskInfo2.token, (Rect) null);
            windowContainerTransaction.setScreenSizeDp(runningTaskInfo2.token, 0, 0);
            return;
        }
        this.mTempRect.set(runningTaskInfo.configuration.windowConfiguration.getBounds());
        this.mTempRect.offset(0, i);
        windowContainerTransaction.setBounds(runningTaskInfo.token, this.mTempRect);
        this.mTempRect.set(runningTaskInfo.configuration.windowConfiguration.getAppBounds());
        this.mTempRect.offset(0, i);
        windowContainerTransaction.setAppBounds(runningTaskInfo.token, this.mTempRect);
        WindowContainerToken windowContainerToken = runningTaskInfo.token;
        Configuration configuration = runningTaskInfo.configuration;
        windowContainerTransaction.setScreenSizeDp(windowContainerToken, configuration.screenWidthDp, configuration.screenHeightDp);
        this.mTempRect.set(runningTaskInfo2.configuration.windowConfiguration.getBounds());
        this.mTempRect.offset(0, i);
        windowContainerTransaction.setBounds(runningTaskInfo2.token, this.mTempRect);
        this.mTempRect.set(runningTaskInfo2.configuration.windowConfiguration.getAppBounds());
        this.mTempRect.offset(0, i);
        windowContainerTransaction.setAppBounds(runningTaskInfo2.token, this.mTempRect);
        WindowContainerToken windowContainerToken2 = runningTaskInfo2.token;
        Configuration configuration2 = runningTaskInfo2.configuration;
        windowContainerTransaction.setScreenSizeDp(windowContainerToken2, configuration2.screenWidthDp, configuration2.screenHeightDp);
    }

    public final void applySurfaceChanges(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, SurfaceControl surfaceControl2, SurfaceControl surfaceControl3, SurfaceControl surfaceControl4) {
        SurfaceControl surfaceControl5;
        boolean z;
        boolean z2;
        Rect rect;
        boolean z3;
        boolean z4;
        SplitWindowManager splitWindowManager = this.mSplitWindowManager;
        if (splitWindowManager == null) {
            surfaceControl5 = null;
        } else {
            surfaceControl5 = splitWindowManager.mLeash;
        }
        if (surfaceControl5 != null) {
            Rect rect2 = this.mDividerBounds;
            transaction.setPosition(surfaceControl5, rect2.left, rect2.top);
            transaction.setLayer(surfaceControl5, 30000);
        }
        Rect rect3 = this.mBounds1;
        transaction.setPosition(surfaceControl, rect3.left, rect3.top).setWindowCrop(surfaceControl, this.mBounds1.width(), this.mBounds1.height());
        Rect rect4 = this.mBounds2;
        transaction.setPosition(surfaceControl2, rect4.left, rect4.top).setWindowCrop(surfaceControl2, this.mBounds2.width(), this.mBounds2.height());
        ImePositionProcessor imePositionProcessor = this.mImePositionProcessor;
        Objects.requireNonNull(imePositionProcessor);
        boolean z5 = false;
        if (imePositionProcessor.mDimValue1 > 0.001f || imePositionProcessor.mDimValue2 > 0.001f) {
            z = true;
        } else {
            z = false;
        }
        if (imePositionProcessor.mYOffsetForIme != 0) {
            if (surfaceControl5 != null) {
                SplitLayout splitLayout = SplitLayout.this;
                splitLayout.mTempRect.set(splitLayout.mDividerBounds);
                SplitLayout.this.mTempRect.offset(0, imePositionProcessor.mYOffsetForIme);
                Rect rect5 = SplitLayout.this.mTempRect;
                transaction.setPosition(surfaceControl5, rect5.left, rect5.top);
            }
            SplitLayout splitLayout2 = SplitLayout.this;
            splitLayout2.mTempRect.set(splitLayout2.mBounds1);
            SplitLayout.this.mTempRect.offset(0, imePositionProcessor.mYOffsetForIme);
            Rect rect6 = SplitLayout.this.mTempRect;
            transaction.setPosition(surfaceControl, rect6.left, rect6.top);
            SplitLayout splitLayout3 = SplitLayout.this;
            splitLayout3.mTempRect.set(splitLayout3.mBounds2);
            SplitLayout.this.mTempRect.offset(0, imePositionProcessor.mYOffsetForIme);
            Rect rect7 = SplitLayout.this.mTempRect;
            transaction.setPosition(surfaceControl2, rect7.left, rect7.top);
            z2 = true;
        } else {
            z2 = false;
        }
        if (z) {
            SurfaceControl.Transaction alpha = transaction.setAlpha(surfaceControl3, imePositionProcessor.mDimValue1);
            if (imePositionProcessor.mDimValue1 > 0.001f) {
                z3 = true;
            } else {
                z3 = false;
            }
            alpha.setVisibility(surfaceControl3, z3);
            SurfaceControl.Transaction alpha2 = transaction.setAlpha(surfaceControl4, imePositionProcessor.mDimValue2);
            if (imePositionProcessor.mDimValue2 > 0.001f) {
                z4 = true;
            } else {
                z4 = false;
            }
            alpha2.setVisibility(surfaceControl4, z4);
            z2 = true;
        }
        if (!z2) {
            DismissingEffectPolicy dismissingEffectPolicy = this.mDismissingEffectPolicy;
            Objects.requireNonNull(dismissingEffectPolicy);
            int i = dismissingEffectPolicy.mDismissingSide;
            if (i == 1 || i == 2) {
                SplitLayout splitLayout4 = SplitLayout.this;
                splitLayout4.mTempRect.set(splitLayout4.mBounds1);
            } else if (i == 3 || i == 4) {
                SplitLayout splitLayout5 = SplitLayout.this;
                splitLayout5.mTempRect.set(splitLayout5.mBounds2);
                surfaceControl = surfaceControl2;
                surfaceControl3 = surfaceControl4;
            } else {
                transaction.setAlpha(surfaceControl3, 0.0f).hide(surfaceControl3);
                transaction.setAlpha(surfaceControl4, 0.0f).hide(surfaceControl4);
                return;
            }
            if (dismissingEffectPolicy.mApplyParallax) {
                int i2 = SplitLayout.this.mTempRect.left;
                Point point = dismissingEffectPolicy.mDismissingParallaxOffset;
                transaction.setPosition(surfaceControl, i2 + point.x, rect.top + point.y);
                Rect rect8 = SplitLayout.this.mTempRect;
                Point point2 = dismissingEffectPolicy.mDismissingParallaxOffset;
                rect8.offsetTo(-point2.x, -point2.y);
                transaction.setWindowCrop(surfaceControl, SplitLayout.this.mTempRect);
            }
            SurfaceControl.Transaction alpha3 = transaction.setAlpha(surfaceControl3, dismissingEffectPolicy.mDismissingDimValue);
            if (dismissingEffectPolicy.mDismissingDimValue > 0.001f) {
                z5 = true;
            }
            alpha3.setVisibility(surfaceControl3, z5);
        }
    }

    public final void applyTaskChanges(WindowContainerTransaction windowContainerTransaction, ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
        ImePositionProcessor imePositionProcessor = this.mImePositionProcessor;
        WindowContainerToken windowContainerToken = runningTaskInfo.token;
        WindowContainerToken windowContainerToken2 = runningTaskInfo2.token;
        Objects.requireNonNull(imePositionProcessor);
        boolean z = false;
        if (imePositionProcessor.mYOffsetForIme != 0) {
            SplitLayout splitLayout = SplitLayout.this;
            splitLayout.mTempRect.set(splitLayout.mBounds1);
            SplitLayout.this.mTempRect.offset(0, imePositionProcessor.mYOffsetForIme);
            windowContainerTransaction.setBounds(windowContainerToken, SplitLayout.this.mTempRect);
            SplitLayout splitLayout2 = SplitLayout.this;
            splitLayout2.mTempRect.set(splitLayout2.mBounds2);
            SplitLayout.this.mTempRect.offset(0, imePositionProcessor.mYOffsetForIme);
            windowContainerTransaction.setBounds(windowContainerToken2, SplitLayout.this.mTempRect);
            z = true;
        }
        if (!z) {
            if (!this.mBounds1.equals(this.mWinBounds1) || !runningTaskInfo.token.equals(this.mWinToken1)) {
                windowContainerTransaction.setBounds(runningTaskInfo.token, this.mBounds1);
                this.mWinBounds1.set(this.mBounds1);
                this.mWinToken1 = runningTaskInfo.token;
            }
            if (!this.mBounds2.equals(this.mWinBounds2) || !runningTaskInfo2.token.equals(this.mWinToken2)) {
                windowContainerTransaction.setBounds(runningTaskInfo2.token, this.mBounds2);
                this.mWinBounds2.set(this.mBounds2);
                this.mWinToken2 = runningTaskInfo2.token;
            }
        }
    }

    @VisibleForTesting
    public void flingDividePosition(int i, final int i2, final Runnable runnable) {
        if (i == i2) {
            this.mSplitLayoutHandler.onLayoutSizeChanged(this);
            return;
        }
        ValueAnimator duration = ValueAnimator.ofInt(i, i2).setDuration(250L);
        duration.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.wm.shell.common.split.SplitLayout$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SplitLayout splitLayout = SplitLayout.this;
                Objects.requireNonNull(splitLayout);
                splitLayout.updateBounds(((Integer) valueAnimator.getAnimatedValue()).intValue());
                splitLayout.mSplitLayoutHandler.onLayoutSizeChanging(splitLayout);
            }
        });
        duration.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.common.split.SplitLayout.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                SplitLayout splitLayout = SplitLayout.this;
                int i3 = i2;
                Objects.requireNonNull(splitLayout);
                splitLayout.mDividePosition = i3;
                splitLayout.updateBounds(i3);
                splitLayout.mSplitLayoutHandler.onLayoutSizeChanged(splitLayout);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                Runnable runnable2 = runnable;
                if (runnable2 != null) {
                    runnable2.run();
                }
            }
        });
        duration.start();
    }

    public final Rect getDividerBounds() {
        return new Rect(this.mDividerBounds);
    }

    public final void init() {
        if (!this.mInitialized) {
            this.mInitialized = true;
            SplitWindowManager splitWindowManager = this.mSplitWindowManager;
            InsetsState insetsState = this.mInsetsState;
            Objects.requireNonNull(splitWindowManager);
            if (splitWindowManager.mDividerView == null && splitWindowManager.mViewHost == null) {
                Context context = splitWindowManager.mContext;
                splitWindowManager.mViewHost = new SurfaceControlViewHost(context, context.getDisplay(), (WindowlessWindowManager) splitWindowManager);
                splitWindowManager.mDividerView = (DividerView) LayoutInflater.from(splitWindowManager.mContext).inflate(2131624516, (ViewGroup) null);
                Rect dividerBounds = getDividerBounds();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(dividerBounds.width(), dividerBounds.height(), 2034, 545521704, -3);
                layoutParams.token = new Binder();
                layoutParams.setTitle(splitWindowManager.mWindowName);
                layoutParams.privateFlags |= 536870976;
                splitWindowManager.mViewHost.setView(splitWindowManager.mDividerView, layoutParams);
                DividerView dividerView = splitWindowManager.mDividerView;
                SurfaceControlViewHost surfaceControlViewHost = splitWindowManager.mViewHost;
                Objects.requireNonNull(dividerView);
                dividerView.mSplitLayout = this;
                dividerView.mSplitWindowManager = splitWindowManager;
                dividerView.mViewHost = surfaceControlViewHost;
                dividerView.mDividerBounds.set(getDividerBounds());
                dividerView.onInsetsChanged(insetsState, false);
                this.mDisplayImeController.addPositionProcessor(this.mImePositionProcessor);
                return;
            }
            throw new UnsupportedOperationException("Try to inflate divider view again without release first");
        }
    }

    public final void initDividerPosition(Rect rect) {
        int i;
        int i2;
        float f = this.mDividePosition;
        if (isLandscape(rect)) {
            i = rect.width();
        } else {
            i = rect.height();
        }
        float f2 = f / i;
        if (isLandscape()) {
            i2 = this.mRootBounds.width();
        } else {
            i2 = this.mRootBounds.height();
        }
        int i3 = this.mDividerSnapAlgorithm.calculateNonDismissingSnapTarget((int) (i2 * f2)).position;
        this.mDividePosition = i3;
        updateBounds(i3);
    }

    @Override // com.android.wm.shell.common.DisplayInsetsController.OnInsetsChangedListener
    public final void insetsChanged(InsetsState insetsState) {
        this.mInsetsState.set(insetsState);
        if (this.mInitialized) {
            SplitWindowManager splitWindowManager = this.mSplitWindowManager;
            Objects.requireNonNull(splitWindowManager);
            DividerView dividerView = splitWindowManager.mDividerView;
            if (dividerView != null) {
                dividerView.onInsetsChanged(insetsState, true);
            }
        }
    }

    @Override // com.android.wm.shell.common.DisplayInsetsController.OnInsetsChangedListener
    public final void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
        if (!this.mInsetsState.equals(insetsState)) {
            insetsChanged(insetsState);
        }
    }

    public final boolean isLandscape() {
        return isLandscape(this.mRootBounds);
    }

    public final void release() {
        if (this.mInitialized) {
            this.mInitialized = false;
            SplitWindowManager splitWindowManager = this.mSplitWindowManager;
            Objects.requireNonNull(splitWindowManager);
            if (splitWindowManager.mDividerView != null) {
                splitWindowManager.mDividerView = null;
            }
            SurfaceControlViewHost surfaceControlViewHost = splitWindowManager.mViewHost;
            if (surfaceControlViewHost != null) {
                surfaceControlViewHost.release();
                splitWindowManager.mViewHost = null;
            }
            if (splitWindowManager.mLeash != null) {
                new SurfaceControl.Transaction().remove(splitWindowManager.mLeash).apply();
                splitWindowManager.mLeash = null;
            }
            DisplayImeController displayImeController = this.mDisplayImeController;
            ImePositionProcessor imePositionProcessor = this.mImePositionProcessor;
            Objects.requireNonNull(displayImeController);
            synchronized (displayImeController.mPositionProcessors) {
                displayImeController.mPositionProcessors.remove(imePositionProcessor);
            }
            ImePositionProcessor imePositionProcessor2 = this.mImePositionProcessor;
            Objects.requireNonNull(imePositionProcessor2);
            imePositionProcessor2.mImeShown = false;
            imePositionProcessor2.mTargetYOffset = 0;
            imePositionProcessor2.mLastYOffset = 0;
            imePositionProcessor2.mYOffsetForIme = 0;
            imePositionProcessor2.mTargetDim1 = 0.0f;
            imePositionProcessor2.mLastDim1 = 0.0f;
            imePositionProcessor2.mDimValue1 = 0.0f;
            imePositionProcessor2.mTargetDim2 = 0.0f;
            imePositionProcessor2.mLastDim2 = 0.0f;
            imePositionProcessor2.mDimValue2 = 0.0f;
        }
    }

    public final void resetDividerPosition() {
        int i = this.mDividerSnapAlgorithm.getMiddleTarget().position;
        this.mDividePosition = i;
        updateBounds(i);
        this.mWinToken1 = null;
        this.mWinToken2 = null;
        this.mWinBounds1.setEmpty();
        this.mWinBounds2.setEmpty();
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x00c9  */
    /* JADX WARN: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateBounds(int r8) {
        /*
            Method dump skipped, instructions count: 261
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.common.split.SplitLayout.updateBounds(int):void");
    }

    public final boolean updateConfiguration(Configuration configuration) {
        int rotation = configuration.windowConfiguration.getRotation();
        Rect bounds = configuration.windowConfiguration.getBounds();
        int i = configuration.orientation;
        if (this.mOrientation == i && rotation == this.mRotation && this.mRootBounds.equals(bounds)) {
            return false;
        }
        this.mContext = this.mContext.createConfigurationContext(configuration);
        this.mSplitWindowManager.setConfiguration(configuration);
        this.mOrientation = i;
        this.mTempRect.set(this.mRootBounds);
        this.mRootBounds.set(bounds);
        this.mRotation = rotation;
        this.mDividerSnapAlgorithm = getSnapAlgorithm(this.mContext, this.mRootBounds, null);
        initDividerPosition(this.mTempRect);
        if (!this.mInitialized) {
            return true;
        }
        release();
        init();
        return true;
    }

    public SplitLayout(String str, Context context, Configuration configuration, SplitLayoutHandler splitLayoutHandler, SplitWindowManager.ParentContainerCallbacks parentContainerCallbacks, DisplayImeController displayImeController, ShellTaskOrganizer shellTaskOrganizer, boolean z) {
        Rect rect = new Rect();
        this.mRootBounds = rect;
        int i = 0;
        this.mContext = context.createConfigurationContext(configuration);
        this.mOrientation = configuration.orientation;
        this.mRotation = configuration.windowConfiguration.getRotation();
        this.mSplitLayoutHandler = splitLayoutHandler;
        this.mDisplayImeController = displayImeController;
        this.mSplitWindowManager = new SplitWindowManager(str, this.mContext, configuration, parentContainerCallbacks);
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mImePositionProcessor = new ImePositionProcessor(this.mContext.getDisplayId());
        this.mDismissingEffectPolicy = new DismissingEffectPolicy(z);
        Resources resources = context.getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(2131167037);
        this.mDividerSize = dimensionPixelSize;
        Display display = context.getDisplay();
        int dimensionPixelSize2 = resources.getDimensionPixelSize(17105202);
        RoundedCorner roundedCorner = display.getRoundedCorner(0);
        i = roundedCorner != null ? Math.max(0, roundedCorner.getRadius()) : i;
        RoundedCorner roundedCorner2 = display.getRoundedCorner(1);
        i = roundedCorner2 != null ? Math.max(i, roundedCorner2.getRadius()) : i;
        RoundedCorner roundedCorner3 = display.getRoundedCorner(2);
        i = roundedCorner3 != null ? Math.max(i, roundedCorner3.getRadius()) : i;
        RoundedCorner roundedCorner4 = display.getRoundedCorner(3);
        int max = Math.max(dimensionPixelSize2, roundedCorner4 != null ? Math.max(i, roundedCorner4.getRadius()) : i);
        this.mDividerInsets = max;
        this.mDividerWindowWidth = (max * 2) + dimensionPixelSize;
        rect.set(configuration.windowConfiguration.getBounds());
        this.mDividerSnapAlgorithm = getSnapAlgorithm(this.mContext, rect, null);
        resetDividerPosition();
    }

    public final float getDividerPositionAsFraction() {
        int i;
        float f;
        if (isLandscape()) {
            int i2 = this.mBounds1.right;
            Rect rect = this.mBounds2;
            f = (i2 + rect.left) / 2.0f;
            i = rect.right;
        } else {
            int i3 = this.mBounds1.bottom;
            Rect rect2 = this.mBounds2;
            f = (i3 + rect2.top) / 2.0f;
            i = rect2.bottom;
        }
        return Math.min(1.0f, Math.max(0.0f, f / i));
    }

    public final DividerSnapAlgorithm getSnapAlgorithm(Context context, Rect rect, Rect rect2) {
        int i;
        boolean isLandscape = isLandscape(rect);
        Resources resources = context.getResources();
        int width = rect.width();
        int height = rect.height();
        int i2 = this.mDividerSize;
        boolean z = !isLandscape;
        if (rect2 == null) {
            rect2 = ((WindowManager) context.getSystemService(WindowManager.class)).getMaximumWindowMetrics().getWindowInsets().getInsets(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout()).toRect();
        }
        if (isLandscape) {
            i = 1;
        } else {
            i = 2;
        }
        return new DividerSnapAlgorithm(resources, width, height, i2, z, rect2, i);
    }

    public final void setDivideRatio(float f) {
        int i;
        int i2;
        if (isLandscape()) {
            Rect rect = this.mRootBounds;
            i = rect.left;
            i2 = rect.width();
        } else {
            Rect rect2 = this.mRootBounds;
            i = rect2.top;
            i2 = rect2.height();
        }
        int i3 = this.mDividerSnapAlgorithm.calculateNonDismissingSnapTarget(i + ((int) (i2 * f))).position;
        this.mDividePosition = i3;
        updateBounds(i3);
    }
}
