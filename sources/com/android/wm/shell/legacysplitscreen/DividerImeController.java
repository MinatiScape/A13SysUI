package com.android.wm.shell.legacysplitscreen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.view.Choreographer;
import android.view.SurfaceControl;
import android.window.TaskOrganizer;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DividerImeController implements DisplayImeController.ImePositionProcessor {
    public final ShellExecutor mMainExecutor;
    public final LegacySplitScreenTaskListener mSplits;
    public final TaskOrganizer mTaskOrganizer;
    public final TransactionPool mTransactionPool;
    public int mHiddenTop = 0;
    public int mShownTop = 0;
    public boolean mTargetAdjusted = false;
    public boolean mTargetShown = false;
    public float mTargetPrimaryDim = 0.0f;
    public float mTargetSecondaryDim = 0.0f;
    public boolean mSecondaryHasFocus = false;
    public float mLastPrimaryDim = 0.0f;
    public float mLastSecondaryDim = 0.0f;
    public int mLastAdjustTop = -1;
    public boolean mImeWasShown = false;
    public boolean mAdjusted = false;
    public ValueAnimator mAnimation = null;
    public boolean mPaused = true;
    public boolean mPausedTargetAdjusted = false;

    public final boolean isDividerHidden() {
        boolean z;
        LegacySplitScreenController legacySplitScreenController = this.mSplits.mSplitScreenController;
        Objects.requireNonNull(legacySplitScreenController);
        DividerView dividerView = legacySplitScreenController.mView;
        if (dividerView != null) {
            if (dividerView.getVisibility() != 0 || dividerView.mSurfaceHidden) {
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

    public final void onEnd(boolean z, SurfaceControl.Transaction transaction) {
        int i;
        if (!z) {
            onProgress(1.0f, transaction);
            boolean z2 = this.mTargetAdjusted;
            this.mAdjusted = z2;
            this.mImeWasShown = this.mTargetShown;
            if (z2) {
                i = this.mShownTop;
            } else {
                i = this.mHiddenTop;
            }
            this.mLastAdjustTop = i;
            this.mLastPrimaryDim = this.mTargetPrimaryDim;
            this.mLastSecondaryDim = this.mTargetSecondaryDim;
        }
    }

    @Override // com.android.wm.shell.common.DisplayImeController.ImePositionProcessor
    public final void onImeControlTargetChanged(int i, boolean z) {
        if (!z && this.mTargetShown) {
            this.mPaused = false;
            this.mTargetShown = false;
            this.mTargetAdjusted = false;
            this.mTargetSecondaryDim = 0.0f;
            this.mTargetPrimaryDim = 0.0f;
            updateImeAdjustState(true);
            startAsyncAnimation();
        }
    }

    @Override // com.android.wm.shell.common.DisplayImeController.ImePositionProcessor
    public final void onImeEndPositioning(int i, boolean z, SurfaceControl.Transaction transaction) {
        if (this.mAnimation == null && !isDividerHidden() && !this.mPaused) {
            onEnd(z, transaction);
        }
    }

    @Override // com.android.wm.shell.common.DisplayImeController.ImePositionProcessor
    public final void onImePositionChanged(int i, int i2, SurfaceControl.Transaction transaction) {
        int i3;
        if (this.mAnimation == null && !isDividerHidden() && !this.mPaused) {
            float f = (i2 - this.mHiddenTop) / (this.mShownTop - i3);
            if (!this.mTargetShown) {
                f = 1.0f - f;
            }
            onProgress(f, transaction);
        }
    }

    public final void onProgress(float f, SurfaceControl.Transaction transaction) {
        float f2;
        LegacySplitScreenController legacySplitScreenController = this.mSplits.mSplitScreenController;
        Objects.requireNonNull(legacySplitScreenController);
        DividerView dividerView = legacySplitScreenController.mView;
        if (this.mTargetAdjusted != this.mAdjusted && !this.mPaused) {
            LegacySplitScreenController legacySplitScreenController2 = this.mSplits.mSplitScreenController;
            Objects.requireNonNull(legacySplitScreenController2);
            LegacySplitDisplayLayout legacySplitDisplayLayout = legacySplitScreenController2.mSplitLayout;
            if (this.mTargetAdjusted) {
                f2 = f;
            } else {
                f2 = 1.0f - f;
            }
            int i = this.mShownTop;
            int i2 = this.mHiddenTop;
            int i3 = (int) (((1.0f - f2) * i2) + (i * f2));
            this.mLastAdjustTop = i3;
            legacySplitDisplayLayout.updateAdjustedBounds(i3, i2, i);
            Rect rect = legacySplitDisplayLayout.mAdjustedPrimary;
            Rect rect2 = legacySplitDisplayLayout.mAdjustedSecondary;
            Objects.requireNonNull(dividerView);
            dividerView.resizeSplitSurfaces(transaction, rect, null, rect2, null);
        }
        float f3 = 1.0f - f;
        dividerView.setResizeDimLayer(transaction, true, (this.mTargetPrimaryDim * f) + (this.mLastPrimaryDim * f3));
        dividerView.setResizeDimLayer(transaction, false, (f * this.mTargetSecondaryDim) + (this.mLastSecondaryDim * f3));
    }

    public final void setDimsHidden(SurfaceControl.Transaction transaction, boolean z) {
        LegacySplitScreenController legacySplitScreenController = this.mSplits.mSplitScreenController;
        Objects.requireNonNull(legacySplitScreenController);
        DividerView dividerView = legacySplitScreenController.mView;
        if (z) {
            dividerView.setResizeDimLayer(transaction, true, 0.0f);
            dividerView.setResizeDimLayer(transaction, false, 0.0f);
            return;
        }
        updateDimTargets();
        dividerView.setResizeDimLayer(transaction, true, this.mTargetPrimaryDim);
        dividerView.setResizeDimLayer(transaction, false, this.mTargetSecondaryDim);
    }

    public final void startAsyncAnimation() {
        ValueAnimator valueAnimator = this.mAnimation;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mAnimation = ofFloat;
        ofFloat.setDuration(275L);
        boolean z = this.mTargetAdjusted;
        if (z != this.mAdjusted) {
            int i = this.mHiddenTop;
            float f = (this.mLastAdjustTop - i) / (this.mShownTop - i);
            if (!z) {
                f = 1.0f - f;
            }
            this.mAnimation.setCurrentFraction(f);
        }
        this.mAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.wm.shell.legacysplitscreen.DividerImeController$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                DividerImeController dividerImeController = DividerImeController.this;
                Objects.requireNonNull(dividerImeController);
                SurfaceControl.Transaction acquire = dividerImeController.mTransactionPool.acquire();
                dividerImeController.onProgress(((Float) valueAnimator2.getAnimatedValue()).floatValue(), acquire);
                acquire.setFrameTimelineVsync(Choreographer.getSfInstance().getVsyncId());
                acquire.apply();
                dividerImeController.mTransactionPool.release(acquire);
            }
        });
        this.mAnimation.setInterpolator(DisplayImeController.INTERPOLATOR);
        this.mAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.legacysplitscreen.DividerImeController.1
            public boolean mCancel = false;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                this.mCancel = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                SurfaceControl.Transaction acquire = DividerImeController.this.mTransactionPool.acquire();
                DividerImeController.this.onEnd(this.mCancel, acquire);
                acquire.apply();
                DividerImeController.this.mTransactionPool.release(acquire);
                DividerImeController.this.mAnimation = null;
            }
        });
        this.mAnimation.start();
    }

    public final void updateDimTargets() {
        boolean z;
        float f;
        LegacySplitScreenController legacySplitScreenController = this.mSplits.mSplitScreenController;
        Objects.requireNonNull(legacySplitScreenController);
        DividerView dividerView = legacySplitScreenController.mView;
        Objects.requireNonNull(dividerView);
        if (dividerView.getVisibility() != 0 || dividerView.mSurfaceHidden) {
            z = true;
        } else {
            z = false;
        }
        boolean z2 = !z;
        boolean z3 = this.mSecondaryHasFocus;
        float f2 = 0.3f;
        if (!z3 || !this.mTargetShown || !z2) {
            f = 0.0f;
        } else {
            f = 0.3f;
        }
        this.mTargetPrimaryDim = f;
        if (z3 || !this.mTargetShown || !z2) {
            f2 = 0.0f;
        }
        this.mTargetSecondaryDim = f2;
    }

    public final void updateImeAdjustState(boolean z) {
        long j;
        boolean z2 = false;
        if (this.mAdjusted != this.mTargetAdjusted || z) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            LegacySplitScreenController legacySplitScreenController = this.mSplits.mSplitScreenController;
            Objects.requireNonNull(legacySplitScreenController);
            LegacySplitDisplayLayout legacySplitDisplayLayout = legacySplitScreenController.mSplitLayout;
            if (this.mTargetAdjusted) {
                int i = this.mShownTop;
                legacySplitDisplayLayout.updateAdjustedBounds(i, this.mHiddenTop, i);
                windowContainerTransaction.setBounds(this.mSplits.mSecondary.token, legacySplitDisplayLayout.mAdjustedSecondary);
                Rect rect = new Rect(this.mSplits.mSecondary.configuration.windowConfiguration.getAppBounds());
                rect.offset(0, legacySplitDisplayLayout.mAdjustedSecondary.top - legacySplitDisplayLayout.mSecondary.top);
                windowContainerTransaction.setAppBounds(this.mSplits.mSecondary.token, rect);
                ActivityManager.RunningTaskInfo runningTaskInfo = this.mSplits.mSecondary;
                WindowContainerToken windowContainerToken = runningTaskInfo.token;
                Configuration configuration = runningTaskInfo.configuration;
                windowContainerTransaction.setScreenSizeDp(windowContainerToken, configuration.screenWidthDp, configuration.screenHeightDp);
                windowContainerTransaction.setBounds(this.mSplits.mPrimary.token, legacySplitDisplayLayout.mAdjustedPrimary);
                Rect rect2 = new Rect(this.mSplits.mPrimary.configuration.windowConfiguration.getAppBounds());
                rect2.offset(0, legacySplitDisplayLayout.mAdjustedPrimary.top - legacySplitDisplayLayout.mPrimary.top);
                windowContainerTransaction.setAppBounds(this.mSplits.mPrimary.token, rect2);
                ActivityManager.RunningTaskInfo runningTaskInfo2 = this.mSplits.mPrimary;
                WindowContainerToken windowContainerToken2 = runningTaskInfo2.token;
                Configuration configuration2 = runningTaskInfo2.configuration;
                windowContainerTransaction.setScreenSizeDp(windowContainerToken2, configuration2.screenWidthDp, configuration2.screenHeightDp);
            } else {
                windowContainerTransaction.setBounds(this.mSplits.mSecondary.token, legacySplitDisplayLayout.mSecondary);
                windowContainerTransaction.setAppBounds(this.mSplits.mSecondary.token, (Rect) null);
                windowContainerTransaction.setScreenSizeDp(this.mSplits.mSecondary.token, 0, 0);
                windowContainerTransaction.setBounds(this.mSplits.mPrimary.token, legacySplitDisplayLayout.mPrimary);
                windowContainerTransaction.setAppBounds(this.mSplits.mPrimary.token, (Rect) null);
                windowContainerTransaction.setScreenSizeDp(this.mSplits.mPrimary.token, 0, 0);
            }
            LegacySplitScreenController legacySplitScreenController2 = this.mSplits.mSplitScreenController;
            Objects.requireNonNull(legacySplitScreenController2);
            if (!legacySplitScreenController2.mWindowManagerProxy.queueSyncTransactionIfWaiting(windowContainerTransaction)) {
                this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
            }
        }
        if (!this.mPaused) {
            LegacySplitScreenController legacySplitScreenController3 = this.mSplits.mSplitScreenController;
            Objects.requireNonNull(legacySplitScreenController3);
            DividerView dividerView = legacySplitScreenController3.mView;
            if (dividerView != null) {
                boolean z3 = this.mTargetShown;
                if (z3) {
                    j = 275;
                } else {
                    j = 340;
                }
                dividerView.setAdjustedForIme(z3, j);
            }
        }
        LegacySplitScreenController legacySplitScreenController4 = this.mSplits.mSplitScreenController;
        if (this.mTargetShown && !this.mPaused) {
            z2 = true;
        }
        Objects.requireNonNull(legacySplitScreenController4);
        if (legacySplitScreenController4.mAdjustedForIme != z2) {
            legacySplitScreenController4.mAdjustedForIme = z2;
            legacySplitScreenController4.updateTouchable();
        }
    }

    public DividerImeController(LegacySplitScreenTaskListener legacySplitScreenTaskListener, TransactionPool transactionPool, ShellExecutor shellExecutor, TaskOrganizer taskOrganizer) {
        this.mSplits = legacySplitScreenTaskListener;
        this.mTransactionPool = transactionPool;
        this.mMainExecutor = shellExecutor;
        this.mTaskOrganizer = taskOrganizer;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x007e  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0088  */
    @Override // com.android.wm.shell.common.DisplayImeController.ImePositionProcessor
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int onImeStartPositioning(int r4, int r5, int r6, boolean r7, boolean r8) {
        /*
            r3 = this;
            boolean r0 = r3.isDividerHidden()
            r1 = 0
            if (r0 == 0) goto L_0x0008
            return r1
        L_0x0008:
            r3.mHiddenTop = r5
            r3.mShownTop = r6
            r3.mTargetShown = r7
            android.window.TaskOrganizer r0 = r3.mTaskOrganizer
            android.window.WindowContainerToken r4 = r0.getImeTarget(r4)
            r0 = 1
            if (r4 == 0) goto L_0x0029
            android.os.IBinder r4 = r4.asBinder()
            com.android.wm.shell.legacysplitscreen.LegacySplitScreenTaskListener r2 = r3.mSplits
            android.app.ActivityManager$RunningTaskInfo r2 = r2.mSecondary
            android.window.WindowContainerToken r2 = r2.token
            android.os.IBinder r2 = r2.asBinder()
            if (r4 != r2) goto L_0x0029
            r4 = r0
            goto L_0x002a
        L_0x0029:
            r4 = r1
        L_0x002a:
            r3.mSecondaryHasFocus = r4
            if (r7 == 0) goto L_0x0050
            if (r4 == 0) goto L_0x0050
            if (r8 != 0) goto L_0x0050
            com.android.wm.shell.legacysplitscreen.LegacySplitScreenTaskListener r4 = r3.mSplits
            com.android.wm.shell.legacysplitscreen.LegacySplitScreenController r4 = r4.mSplitScreenController
            java.util.Objects.requireNonNull(r4)
            com.android.wm.shell.legacysplitscreen.LegacySplitDisplayLayout r4 = r4.mSplitLayout
            com.android.wm.shell.common.DisplayLayout r4 = r4.mDisplayLayout
            boolean r4 = r4.isLandscape()
            if (r4 != 0) goto L_0x0050
            com.android.wm.shell.legacysplitscreen.LegacySplitScreenTaskListener r4 = r3.mSplits
            com.android.wm.shell.legacysplitscreen.LegacySplitScreenController r4 = r4.mSplitScreenController
            java.util.Objects.requireNonNull(r4)
            boolean r4 = r4.mMinimized
            if (r4 != 0) goto L_0x0050
            r4 = r0
            goto L_0x0051
        L_0x0050:
            r4 = r1
        L_0x0051:
            int r8 = r3.mLastAdjustTop
            if (r8 >= 0) goto L_0x005c
            if (r7 == 0) goto L_0x0058
            goto L_0x0059
        L_0x0058:
            r5 = r6
        L_0x0059:
            r3.mLastAdjustTop = r5
            goto L_0x007a
        L_0x005c:
            if (r7 == 0) goto L_0x0061
            int r5 = r3.mShownTop
            goto L_0x0063
        L_0x0061:
            int r5 = r3.mHiddenTop
        L_0x0063:
            if (r8 == r5) goto L_0x007a
            boolean r5 = r3.mTargetAdjusted
            if (r5 == r4) goto L_0x0070
            boolean r6 = r3.mAdjusted
            if (r4 != r6) goto L_0x0070
            r3.mAdjusted = r5
            goto L_0x007a
        L_0x0070:
            if (r4 == 0) goto L_0x007a
            if (r5 == 0) goto L_0x007a
            boolean r5 = r3.mAdjusted
            if (r5 == 0) goto L_0x007a
            r3.mAdjusted = r1
        L_0x007a:
            boolean r5 = r3.mPaused
            if (r5 == 0) goto L_0x0088
            r3.mPausedTargetAdjusted = r4
            if (r4 != 0) goto L_0x0086
            boolean r3 = r3.mAdjusted
            if (r3 == 0) goto L_0x0087
        L_0x0086:
            r1 = r0
        L_0x0087:
            return r1
        L_0x0088:
            r3.mTargetAdjusted = r4
            r3.updateDimTargets()
            android.animation.ValueAnimator r4 = r3.mAnimation
            if (r4 != 0) goto L_0x009d
            boolean r4 = r3.mImeWasShown
            if (r4 == 0) goto L_0x00a0
            if (r7 == 0) goto L_0x00a0
            boolean r4 = r3.mTargetAdjusted
            boolean r5 = r3.mAdjusted
            if (r4 == r5) goto L_0x00a0
        L_0x009d:
            r3.startAsyncAnimation()
        L_0x00a0:
            r3.updateImeAdjustState(r1)
            boolean r4 = r3.mTargetAdjusted
            if (r4 != 0) goto L_0x00ab
            boolean r3 = r3.mAdjusted
            if (r3 == 0) goto L_0x00ac
        L_0x00ab:
            r1 = r0
        L_0x00ac:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.legacysplitscreen.DividerImeController.onImeStartPositioning(int, int, int, boolean, boolean):int");
    }
}
