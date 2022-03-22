package com.android.wm.shell.pip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.PictureInPictureParams;
import android.app.TaskInfo;
import android.content.Context;
import android.content.res.Configuration;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Rect;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.util.Rational;
import android.util.RotationUtils;
import android.util.Size;
import android.view.SurfaceControl;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.fragment.app.DialogFragment$$ExternalSyntheticOutline0;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3;
import com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda1;
import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda0;
import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda1;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda0;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.ShellTaskOrganizer$$ExternalSyntheticLambda1;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.apppairs.AppPair$$ExternalSyntheticLambda1;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ScreenshotUtils;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenTaskListener;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.transition.Transitions;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
/* loaded from: classes.dex */
public final class PipTaskOrganizer implements ShellTaskOrganizer.TaskListener, DisplayController.OnDisplaysChangedListener, ShellTaskOrganizer.FocusListener {
    public final Context mContext;
    public final int mCrossFadeAnimationDuration;
    public int mCurrentRotation;
    public SurfaceControl.Transaction mDeferredAnimEndTransaction;
    public ActivityManager.RunningTaskInfo mDeferredTaskInfo;
    public final int mEnterAnimationDuration;
    public final int mExitAnimationDuration;
    public boolean mHasFadeOut;
    public long mLastOneShotAlphaAnimationTime;
    public SurfaceControl mLeash;
    public final Optional<LegacySplitScreenController> mLegacySplitScreenOptional;
    public final ShellExecutor mMainExecutor;
    public int mNextRotation;
    public IntConsumer mOnDisplayIdChangeCallback;
    public PictureInPictureParams mPictureInPictureParams;
    public final PipAnimationController mPipAnimationController;
    public final PipBoundsAlgorithm mPipBoundsAlgorithm;
    public final PipBoundsState mPipBoundsState;
    public final PipMenuController mPipMenuController;
    public final PipTransitionController mPipTransitionController;
    public PipTransitionState mPipTransitionState;
    public final PipUiEventLogger mPipUiEventLoggerLogger;
    public final Optional<SplitScreenController> mSplitScreenOptional;
    public final PipSurfaceTransactionHelper mSurfaceTransactionHelper;
    public SurfaceControl mSwipePipToHomeOverlay;
    public final SyncTransactionQueue mSyncTransactionQueue;
    public ActivityManager.RunningTaskInfo mTaskInfo;
    public final ShellTaskOrganizer mTaskOrganizer;
    public WindowContainerToken mToken;
    public boolean mWaitForFixedRotation;
    public final AnonymousClass1 mPipAnimationCallback = new PipAnimationController.PipAnimationCallback() { // from class: com.android.wm.shell.pip.PipTaskOrganizer.1
        @Override // com.android.wm.shell.pip.PipAnimationController.PipAnimationCallback
        public final void onPipAnimationCancel(PipAnimationController.PipTransitionAnimator pipTransitionAnimator) {
            SurfaceControl surfaceControl;
            int transitionDirection = pipTransitionAnimator.getTransitionDirection();
            if (PipAnimationController.isInPipDirection(transitionDirection) && (surfaceControl = pipTransitionAnimator.mContentOverlay) != null) {
                PipTaskOrganizer.this.fadeOutAndRemoveOverlay(surfaceControl, new QSTileImpl$$ExternalSyntheticLambda0(pipTransitionAnimator, 8), true);
            }
            PipTaskOrganizer pipTaskOrganizer = PipTaskOrganizer.this;
            Objects.requireNonNull(pipTaskOrganizer);
            pipTaskOrganizer.mPipTransitionController.sendOnPipTransitionCancelled(transitionDirection);
        }

        @Override // com.android.wm.shell.pip.PipAnimationController.PipAnimationCallback
        public final void onPipAnimationEnd(TaskInfo taskInfo, SurfaceControl.Transaction transaction, PipAnimationController.PipTransitionAnimator pipTransitionAnimator) {
            boolean z;
            SurfaceControl surfaceControl;
            int transitionDirection = pipTransitionAnimator.getTransitionDirection();
            int animationType = pipTransitionAnimator.getAnimationType();
            Rect rect = pipTransitionAnimator.mDestinationBounds;
            boolean z2 = true;
            if (PipAnimationController.isInPipDirection(transitionDirection) && (surfaceControl = pipTransitionAnimator.mContentOverlay) != null) {
                PipTaskOrganizer.this.fadeOutAndRemoveOverlay(surfaceControl, new QSTileImpl$$ExternalSyntheticLambda1(pipTransitionAnimator, 5), true);
            }
            if (PipTaskOrganizer.this.mWaitForFixedRotation && animationType == 0 && transitionDirection == 2) {
                WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                windowContainerTransaction.scheduleFinishEnterPip(PipTaskOrganizer.this.mToken, rect);
                PipTaskOrganizer.this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
                PipTaskOrganizer pipTaskOrganizer = PipTaskOrganizer.this;
                pipTaskOrganizer.mSurfaceTransactionHelper.round(transaction, pipTaskOrganizer.mLeash, pipTaskOrganizer.isInPip());
                PipTaskOrganizer.this.mDeferredAnimEndTransaction = transaction;
                return;
            }
            if (!PipAnimationController.isOutPipDirection(transitionDirection)) {
                if (transitionDirection == 5) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z) {
                    z2 = false;
                }
            }
            PipTransitionState pipTransitionState = PipTaskOrganizer.this.mPipTransitionState;
            Objects.requireNonNull(pipTransitionState);
            if (pipTransitionState.mState != 5 || z2) {
                PipTaskOrganizer.this.finishResize(transaction, rect, transitionDirection, animationType);
                PipTaskOrganizer.this.sendOnPipTransitionFinished(transitionDirection);
            }
        }

        @Override // com.android.wm.shell.pip.PipAnimationController.PipAnimationCallback
        public final void onPipAnimationStart(PipAnimationController.PipTransitionAnimator pipTransitionAnimator) {
            int transitionDirection = pipTransitionAnimator.getTransitionDirection();
            PipTaskOrganizer pipTaskOrganizer = PipTaskOrganizer.this;
            Objects.requireNonNull(pipTaskOrganizer);
            if (transitionDirection == 2) {
                PipTransitionState pipTransitionState = pipTaskOrganizer.mPipTransitionState;
                Objects.requireNonNull(pipTransitionState);
                pipTransitionState.mState = 3;
            }
            pipTaskOrganizer.mPipTransitionController.sendOnPipTransitionStarted(transitionDirection);
        }
    };
    public final AnonymousClass2 mPipTransactionHandler = new AnonymousClass2();
    public int mOneShotAnimationType = 0;
    public DialogFragment$$ExternalSyntheticOutline0 mSurfaceControlTransactionFactory = DialogFragment$$ExternalSyntheticOutline0.INSTANCE;

    /* renamed from: com.android.wm.shell.pip.PipTaskOrganizer$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 extends PipAnimationController.PipTransactionHandler {
        public AnonymousClass2() {
        }
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [com.android.wm.shell.pip.PipTaskOrganizer$1] */
    public PipTaskOrganizer(Context context, SyncTransactionQueue syncTransactionQueue, PipTransitionState pipTransitionState, PipBoundsState pipBoundsState, PipBoundsAlgorithm pipBoundsAlgorithm, PipMenuController pipMenuController, PipAnimationController pipAnimationController, PipSurfaceTransactionHelper pipSurfaceTransactionHelper, PipTransitionController pipTransitionController, Optional<LegacySplitScreenController> optional, Optional<SplitScreenController> optional2, DisplayController displayController, PipUiEventLogger pipUiEventLogger, ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mSyncTransactionQueue = syncTransactionQueue;
        this.mPipTransitionState = pipTransitionState;
        this.mPipBoundsState = pipBoundsState;
        this.mPipBoundsAlgorithm = pipBoundsAlgorithm;
        this.mPipMenuController = pipMenuController;
        this.mPipTransitionController = pipTransitionController;
        this.mEnterAnimationDuration = context.getResources().getInteger(2131492900);
        this.mExitAnimationDuration = context.getResources().getInteger(2131492901);
        this.mCrossFadeAnimationDuration = context.getResources().getInteger(2131492899);
        this.mSurfaceTransactionHelper = pipSurfaceTransactionHelper;
        this.mPipAnimationController = pipAnimationController;
        this.mPipUiEventLoggerLogger = pipUiEventLogger;
        this.mLegacySplitScreenOptional = optional;
        this.mSplitScreenOptional = optional2;
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mMainExecutor = shellExecutor;
        shellExecutor.execute(new WMShell$7$$ExternalSyntheticLambda0(this, 8));
        Objects.requireNonNull(shellTaskOrganizer);
        synchronized (shellTaskOrganizer.mLock) {
            shellTaskOrganizer.mFocusListeners.add(this);
            ActivityManager.RunningTaskInfo runningTaskInfo = shellTaskOrganizer.mLastFocusedTaskInfo;
            if (runningTaskInfo != null) {
                onFocusTaskChanged(runningTaskInfo);
            }
        }
        Objects.requireNonNull(pipTransitionController);
        pipTransitionController.mPipOrganizer = this;
        displayController.addDisplayWindowListener(this);
    }

    public final PipAnimationController.PipTransitionAnimator<?> animateResizePip(Rect rect, Rect rect2, Rect rect3, int i, int i2, float f) {
        int i3;
        Rect rect4;
        Rect rect5;
        Rect rect6 = rect3;
        if (this.mToken == null || this.mLeash == null) {
            Log.w("PipTaskOrganizer", "Abort animation, invalid leash");
            return null;
        }
        boolean z = false;
        if (this.mWaitForFixedRotation) {
            i3 = RotationUtils.deltaRotation(this.mCurrentRotation, this.mNextRotation);
        } else {
            i3 = 0;
        }
        if (i3 != 0) {
            if (i == 2) {
                PipBoundsState pipBoundsState = this.mPipBoundsState;
                Objects.requireNonNull(pipBoundsState);
                pipBoundsState.mDisplayLayout.rotateTo(this.mContext.getResources(), this.mNextRotation);
                Rect displayBounds = this.mPipBoundsState.getDisplayBounds();
                rect2.set(this.mPipBoundsAlgorithm.getEntryDestinationBounds());
                RotationUtils.rotateBounds(rect2, displayBounds, this.mNextRotation, this.mCurrentRotation);
                if (!(rect6 == null || (rect5 = this.mTaskInfo.displayCutoutInsets) == null || i3 != 3)) {
                    rect6.offset(rect5.left, rect5.top);
                }
            } else if (i == 3) {
                Rect rect7 = new Rect(rect2);
                RotationUtils.rotateBounds(rect7, this.mPipBoundsState.getDisplayBounds(), i3);
                rect6 = PipBoundsAlgorithm.getValidSourceHintRect(this.mPictureInPictureParams, rect7);
            }
        }
        if (i == 6) {
            rect4 = this.mPipBoundsState.getBounds();
        } else {
            rect4 = rect;
        }
        PipAnimationController pipAnimationController = this.mPipAnimationController;
        Objects.requireNonNull(pipAnimationController);
        if (pipAnimationController.mCurrentAnimator != null) {
            PipAnimationController pipAnimationController2 = this.mPipAnimationController;
            Objects.requireNonNull(pipAnimationController2);
            if (pipAnimationController2.mCurrentAnimator.isRunning()) {
                z = true;
            }
        }
        PipAnimationController.PipTransitionAnimator<?> animator = this.mPipAnimationController.getAnimator(this.mTaskInfo, this.mLeash, rect4, rect, rect2, rect6, i, f, i3);
        PipAnimationController.PipTransitionAnimator<?> transitionDirection = animator.setTransitionDirection(i);
        AnonymousClass2 r3 = this.mPipTransactionHandler;
        Objects.requireNonNull(transitionDirection);
        transitionDirection.mPipTransactionHandler = r3;
        transitionDirection.setDuration(i2);
        if (!z) {
            animator.setPipAnimationCallback(this.mPipAnimationCallback);
        }
        if (PipAnimationController.isInPipDirection(i)) {
            if (rect6 == null) {
                animator.setUseContentOverlay(this.mContext);
            }
            if (i3 != 0) {
                animator.setDestinationBounds(this.mPipBoundsAlgorithm.getEntryDestinationBounds());
            }
        }
        animator.start();
        return animator;
    }

    public final void applyFinishBoundsResize(final WindowContainerTransaction windowContainerTransaction, int i, final boolean z) {
        if (i == 4) {
            this.mSplitScreenOptional.ifPresent(new Consumer() { // from class: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda8
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    int i2;
                    PipTaskOrganizer pipTaskOrganizer = PipTaskOrganizer.this;
                    boolean z2 = z;
                    WindowContainerTransaction windowContainerTransaction2 = windowContainerTransaction;
                    SplitScreenController splitScreenController = (SplitScreenController) obj;
                    Objects.requireNonNull(pipTaskOrganizer);
                    int i3 = pipTaskOrganizer.mTaskInfo.taskId;
                    Objects.requireNonNull(splitScreenController);
                    if (splitScreenController.isSplitScreenVisible()) {
                        i2 = -1;
                    } else {
                        i2 = 1;
                    }
                    splitScreenController.moveToStage(i3, i2, !z2 ? 1 : 0, windowContainerTransaction2);
                }
            });
        } else {
            this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        }
    }

    public final void onExitPipFinished(TaskInfo taskInfo) {
        IntConsumer intConsumer;
        this.mWaitForFixedRotation = false;
        this.mDeferredAnimEndTransaction = null;
        PipTransitionState pipTransitionState = this.mPipTransitionState;
        Objects.requireNonNull(pipTransitionState);
        pipTransitionState.mInSwipePipToHomeTransition = false;
        this.mPictureInPictureParams = null;
        PipTransitionState pipTransitionState2 = this.mPipTransitionState;
        Objects.requireNonNull(pipTransitionState2);
        pipTransitionState2.mState = 0;
        this.mPipBoundsState.setBounds(new Rect());
        this.mPipUiEventLoggerLogger.setTaskInfo(null);
        this.mPipMenuController.detach();
        if (taskInfo.displayId != 0 && (intConsumer = this.mOnDisplayIdChangeCallback) != null) {
            intConsumer.accept(0);
        }
    }

    public final void scheduleAnimateResizePip(Rect rect, int i, int i2, QSTileHost$$ExternalSyntheticLambda1 qSTileHost$$ExternalSyntheticLambda1) {
        if (this.mWaitForFixedRotation) {
            Log.d("PipTaskOrganizer", "skip scheduleAnimateResizePip, entering pip deferred");
        } else {
            scheduleAnimateResizePip(this.mPipBoundsState.getBounds(), rect, 0.0f, null, i2, i, qSTileHost$$ExternalSyntheticLambda1);
        }
    }

    @VisibleForTesting
    public void sendOnPipTransitionFinished(int i) {
        ActivityManager.RunningTaskInfo runningTaskInfo;
        if (i == 2) {
            PipTransitionState pipTransitionState = this.mPipTransitionState;
            Objects.requireNonNull(pipTransitionState);
            pipTransitionState.mState = 4;
        }
        this.mPipTransitionController.sendOnPipTransitionFinished(i);
        if (i == 2 && (runningTaskInfo = this.mDeferredTaskInfo) != null) {
            onTaskInfoChanged(runningTaskInfo);
            this.mDeferredTaskInfo = null;
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final boolean supportCompatUI() {
        return false;
    }

    public final void applyEnterPipSyncTransaction(Rect rect, Runnable runnable, SurfaceControl.Transaction transaction) {
        this.mPipMenuController.attach(this.mLeash);
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        windowContainerTransaction.setActivityWindowingMode(this.mToken, 0);
        windowContainerTransaction.setBounds(this.mToken, rect);
        if (transaction != null) {
            windowContainerTransaction.setBoundsChangeTransaction(this.mToken, transaction);
        }
        this.mSyncTransactionQueue.queue(windowContainerTransaction);
        this.mSyncTransactionQueue.runInSync(new AppPair$$ExternalSyntheticLambda1(runnable, 1));
    }

    public final SurfaceControl.Transaction createFinishResizeSurfaceTransaction(Rect rect) {
        Objects.requireNonNull(this.mSurfaceControlTransactionFactory);
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        PipSurfaceTransactionHelper pipSurfaceTransactionHelper = this.mSurfaceTransactionHelper;
        pipSurfaceTransactionHelper.crop(transaction, this.mLeash, rect);
        pipSurfaceTransactionHelper.resetScale(transaction, this.mLeash, rect);
        pipSurfaceTransactionHelper.round(transaction, this.mLeash, this.mPipTransitionState.isInPip());
        return transaction;
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void dump(PrintWriter printWriter, String str) {
        IBinder iBinder;
        String m = SupportMenuInflater$$ExternalSyntheticOutline0.m(str, "  ");
        printWriter.println(str + "PipTaskOrganizer");
        printWriter.println(m + "mTaskInfo=" + this.mTaskInfo);
        StringBuilder sb = new StringBuilder();
        sb.append(m);
        sb.append("mToken=");
        sb.append(this.mToken);
        sb.append(" binder=");
        WindowContainerToken windowContainerToken = this.mToken;
        if (windowContainerToken != null) {
            iBinder = windowContainerToken.asBinder();
        } else {
            iBinder = null;
        }
        sb.append(iBinder);
        printWriter.println(sb.toString());
        printWriter.println(m + "mLeash=" + this.mLeash);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(m);
        sb2.append("mState=");
        PipTransitionState pipTransitionState = this.mPipTransitionState;
        Objects.requireNonNull(pipTransitionState);
        sb2.append(pipTransitionState.mState);
        printWriter.println(sb2.toString());
        printWriter.println(m + "mOneShotAnimationType=" + this.mOneShotAnimationType);
        printWriter.println(m + "mPictureInPictureParams=" + this.mPictureInPictureParams);
    }

    @VisibleForTesting
    public void enterPipWithAlphaAnimation(final Rect rect, final long j) {
        Objects.requireNonNull(this.mSurfaceControlTransactionFactory);
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        transaction.setAlpha(this.mLeash, 0.0f);
        transaction.apply();
        PipTransitionState pipTransitionState = this.mPipTransitionState;
        Objects.requireNonNull(pipTransitionState);
        pipTransitionState.mState = 2;
        applyEnterPipSyncTransaction(rect, new Runnable() { // from class: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                PipTaskOrganizer pipTaskOrganizer = PipTaskOrganizer.this;
                Rect rect2 = rect;
                long j2 = j;
                Objects.requireNonNull(pipTaskOrganizer);
                PipAnimationController.PipTransitionAnimator pipAnimationCallback = pipTaskOrganizer.mPipAnimationController.getAnimator(pipTaskOrganizer.mTaskInfo, pipTaskOrganizer.mLeash, rect2, 0.0f, 1.0f).setTransitionDirection(2).setPipAnimationCallback(pipTaskOrganizer.mPipAnimationCallback);
                PipTaskOrganizer.AnonymousClass2 r1 = pipTaskOrganizer.mPipTransactionHandler;
                Objects.requireNonNull(pipAnimationCallback);
                pipAnimationCallback.mPipTransactionHandler = r1;
                pipAnimationCallback.setDuration(j2).start();
                PipTransitionState pipTransitionState2 = pipTaskOrganizer.mPipTransitionState;
                Objects.requireNonNull(pipTransitionState2);
                pipTransitionState2.mState = 3;
            }
        }, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x00d0  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00d2  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0129  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0131  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void exitPip(final int r17, boolean r18) {
        /*
            Method dump skipped, instructions count: 359
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.pip.PipTaskOrganizer.exitPip(int, boolean):void");
    }

    public final void fadeExistingPip(boolean z) {
        float f;
        float f2;
        int i;
        SurfaceControl surfaceControl = this.mLeash;
        if (surfaceControl == null || !surfaceControl.isValid()) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid leash on fadeExistingPip: ");
            m.append(this.mLeash);
            Log.w("PipTaskOrganizer", m.toString());
            return;
        }
        if (z) {
            f = 0.0f;
        } else {
            f = 1.0f;
        }
        if (z) {
            f2 = 1.0f;
        } else {
            f2 = 0.0f;
        }
        PipAnimationController.PipTransitionAnimator transitionDirection = this.mPipAnimationController.getAnimator(this.mTaskInfo, this.mLeash, this.mPipBoundsState.getBounds(), f, f2).setTransitionDirection(1);
        AnonymousClass2 r2 = this.mPipTransactionHandler;
        Objects.requireNonNull(transitionDirection);
        transitionDirection.mPipTransactionHandler = r2;
        if (z) {
            i = this.mEnterAnimationDuration;
        } else {
            i = this.mExitAnimationDuration;
        }
        transitionDirection.setDuration(i).start();
        this.mHasFadeOut = !z;
    }

    public final void fadeOutAndRemoveOverlay(final SurfaceControl surfaceControl, final Runnable runnable, boolean z) {
        long j;
        if (surfaceControl != null) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
            ofFloat.setDuration(this.mCrossFadeAnimationDuration);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    PipTaskOrganizer pipTaskOrganizer = PipTaskOrganizer.this;
                    SurfaceControl surfaceControl2 = surfaceControl;
                    Objects.requireNonNull(pipTaskOrganizer);
                    PipTransitionState pipTransitionState = pipTaskOrganizer.mPipTransitionState;
                    Objects.requireNonNull(pipTransitionState);
                    if (pipTransitionState.mState == 0) {
                        Log.d("PipTaskOrganizer", "Task vanished, skip fadeOutAndRemoveOverlay");
                        valueAnimator.removeAllListeners();
                        valueAnimator.removeAllUpdateListeners();
                        valueAnimator.cancel();
                        return;
                    }
                    float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    Objects.requireNonNull(pipTaskOrganizer.mSurfaceControlTransactionFactory);
                    SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                    transaction.setAlpha(surfaceControl2, floatValue);
                    transaction.apply();
                }
            });
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.pip.PipTaskOrganizer.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    PipTaskOrganizer pipTaskOrganizer = PipTaskOrganizer.this;
                    SurfaceControl surfaceControl2 = surfaceControl;
                    Runnable runnable2 = runnable;
                    Objects.requireNonNull(pipTaskOrganizer);
                    PipTransitionState pipTransitionState = pipTaskOrganizer.mPipTransitionState;
                    Objects.requireNonNull(pipTransitionState);
                    if (pipTransitionState.mState != 0) {
                        Objects.requireNonNull(pipTaskOrganizer.mSurfaceControlTransactionFactory);
                        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                        transaction.remove(surfaceControl2);
                        transaction.apply();
                        if (runnable2 != null) {
                            runnable2.run();
                        }
                    }
                }
            });
            if (z) {
                j = 500;
            } else {
                j = 0;
            }
            ofFloat.setStartDelay(j);
            ofFloat.start();
        }
    }

    public final void finishResize(SurfaceControl.Transaction transaction, Rect rect, int i, int i2) {
        boolean z;
        PictureInPictureParams pictureInPictureParams;
        final Rect rect2 = new Rect(this.mPipBoundsState.getBounds());
        boolean isPipTopLeft = isPipTopLeft();
        this.mPipBoundsState.setBounds(rect);
        boolean z2 = true;
        if (i == 5) {
            if (Transitions.ENABLE_SHELL_TRANSITIONS) {
                WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                windowContainerTransaction.setBounds(this.mToken, (Rect) null);
                windowContainerTransaction.setWindowingMode(this.mToken, 0);
                windowContainerTransaction.reorder(this.mToken, false);
                this.mPipTransitionController.startExitTransition(15, windowContainerTransaction, null);
                return;
            }
            try {
                WindowContainerTransaction windowContainerTransaction2 = new WindowContainerTransaction();
                windowContainerTransaction2.setBounds(this.mToken, (Rect) null);
                this.mTaskOrganizer.applyTransaction(windowContainerTransaction2);
                ActivityTaskManager.getService().removeRootTasksInWindowingModes(new int[]{2});
            } catch (RemoteException e) {
                Log.e("PipTaskOrganizer", "Failed to remove PiP", e);
            }
        } else if (!PipAnimationController.isInPipDirection(i) || i2 != 1) {
            WindowContainerTransaction windowContainerTransaction3 = new WindowContainerTransaction();
            prepareFinishResizeTransaction(rect, i, transaction, windowContainerTransaction3);
            if (i == 7 || i == 6 || i == 8) {
                z = true;
            } else {
                z = false;
            }
            if (!z || (pictureInPictureParams = this.mPictureInPictureParams) == null || pictureInPictureParams.isSeamlessResizeEnabled()) {
                z2 = false;
            }
            if (z2) {
                rect2.offsetTo(0, 0);
                final Rect rect3 = new Rect(0, 0, rect.width(), rect.height());
                Objects.requireNonNull(this.mSurfaceControlTransactionFactory);
                SurfaceControl.Transaction transaction2 = new SurfaceControl.Transaction();
                SurfaceControl surfaceControl = this.mLeash;
                ScreenshotUtils.BufferConsumer bufferConsumer = new ScreenshotUtils.BufferConsumer(transaction2, surfaceControl);
                ScreenshotUtils.captureLayer(surfaceControl, rect2, bufferConsumer);
                final SurfaceControl surfaceControl2 = bufferConsumer.mScreenshot;
                if (surfaceControl2 != null) {
                    this.mSyncTransactionQueue.queue(windowContainerTransaction3);
                    this.mSyncTransactionQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda2
                        @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                        public final void runWithTransaction(SurfaceControl.Transaction transaction3) {
                            PipTaskOrganizer pipTaskOrganizer = PipTaskOrganizer.this;
                            SurfaceControl surfaceControl3 = surfaceControl2;
                            Rect rect4 = rect2;
                            Rect rect5 = rect3;
                            Objects.requireNonNull(pipTaskOrganizer);
                            PipSurfaceTransactionHelper pipSurfaceTransactionHelper = pipTaskOrganizer.mSurfaceTransactionHelper;
                            Objects.requireNonNull(pipSurfaceTransactionHelper);
                            pipSurfaceTransactionHelper.scale(transaction3, surfaceControl3, rect4, rect5, 0.0f);
                            pipTaskOrganizer.fadeOutAndRemoveOverlay(surfaceControl3, null, false);
                        }
                    });
                } else {
                    applyFinishBoundsResize(windowContainerTransaction3, i, isPipTopLeft);
                }
            } else {
                applyFinishBoundsResize(windowContainerTransaction3, i, isPipTopLeft);
            }
            finishResizeForMenu(rect);
        } else {
            finishResizeForMenu(rect);
        }
    }

    public final boolean isInPip() {
        return this.mPipTransitionState.isInPip();
    }

    public final boolean isPipTopLeft() {
        if (!this.mSplitScreenOptional.isPresent()) {
            return false;
        }
        Rect rect = new Rect();
        this.mSplitScreenOptional.get().getStageBounds(rect, new Rect());
        return rect.contains(this.mPipBoundsState.getBounds());
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public final void onDisplayConfigurationChanged(int i, Configuration configuration) {
        this.mCurrentRotation = configuration.windowConfiguration.getRotation();
    }

    public final void onEndOfSwipePipToHomeTransition() {
        if (Transitions.ENABLE_SHELL_TRANSITIONS) {
            this.mSwipePipToHomeOverlay = null;
            return;
        }
        Rect bounds = this.mPipBoundsState.getBounds();
        SurfaceControl surfaceControl = this.mSwipePipToHomeOverlay;
        Objects.requireNonNull(this.mSurfaceControlTransactionFactory);
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        PipSurfaceTransactionHelper pipSurfaceTransactionHelper = this.mSurfaceTransactionHelper;
        pipSurfaceTransactionHelper.resetScale(transaction, this.mLeash, bounds);
        pipSurfaceTransactionHelper.crop(transaction, this.mLeash, bounds);
        pipSurfaceTransactionHelper.round(transaction, this.mLeash, isInPip());
        applyEnterPipSyncTransaction(bounds, new PipTaskOrganizer$$ExternalSyntheticLambda5(this, bounds, surfaceControl, 0), transaction);
        PipTransitionState pipTransitionState = this.mPipTransitionState;
        Objects.requireNonNull(pipTransitionState);
        pipTransitionState.mInSwipePipToHomeTransition = false;
        this.mSwipePipToHomeOverlay = null;
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public final void onFixedRotationFinished() {
        if (this.mWaitForFixedRotation) {
            if (Transitions.ENABLE_SHELL_TRANSITIONS) {
                this.mWaitForFixedRotation = false;
                this.mDeferredAnimEndTransaction = null;
                return;
            }
            PipTransitionState pipTransitionState = this.mPipTransitionState;
            Objects.requireNonNull(pipTransitionState);
            if (pipTransitionState.mState == 1) {
                PipTransitionState pipTransitionState2 = this.mPipTransitionState;
                Objects.requireNonNull(pipTransitionState2);
                if (pipTransitionState2.mInSwipePipToHomeTransition) {
                    onEndOfSwipePipToHomeTransition();
                } else {
                    enterPipWithAlphaAnimation(this.mPipBoundsAlgorithm.getEntryDestinationBounds(), this.mEnterAnimationDuration);
                }
            } else {
                PipTransitionState pipTransitionState3 = this.mPipTransitionState;
                Objects.requireNonNull(pipTransitionState3);
                if (pipTransitionState3.mState != 4 || !this.mHasFadeOut) {
                    PipTransitionState pipTransitionState4 = this.mPipTransitionState;
                    Objects.requireNonNull(pipTransitionState4);
                    if (pipTransitionState4.mState == 3 && this.mDeferredAnimEndTransaction != null) {
                        PipAnimationController pipAnimationController = this.mPipAnimationController;
                        Objects.requireNonNull(pipAnimationController);
                        PipAnimationController.PipTransitionAnimator pipTransitionAnimator = pipAnimationController.mCurrentAnimator;
                        Objects.requireNonNull(pipTransitionAnimator);
                        Rect rect = pipTransitionAnimator.mDestinationBounds;
                        this.mPipBoundsState.setBounds(rect);
                        applyEnterPipSyncTransaction(rect, new PipTaskOrganizer$$ExternalSyntheticLambda4(this, rect, 0), this.mDeferredAnimEndTransaction);
                    }
                } else {
                    fadeExistingPip(true);
                }
            }
            this.mWaitForFixedRotation = false;
            this.mDeferredAnimEndTransaction = null;
        }
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public final void onFixedRotationStarted(int i) {
        this.mNextRotation = i;
        this.mWaitForFixedRotation = true;
        if (Transitions.ENABLE_SHELL_TRANSITIONS) {
            this.mPipTransitionController.onFixedRotationStarted();
        } else if (this.mPipTransitionState.isInPip()) {
            fadeExistingPip(false);
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.FocusListener
    public final void onFocusTaskChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        this.mPipMenuController.onFocusTaskChanged(runningTaskInfo);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        IntConsumer intConsumer;
        Objects.requireNonNull(runningTaskInfo, "Requires RunningTaskInfo");
        this.mTaskInfo = runningTaskInfo;
        this.mToken = runningTaskInfo.token;
        PipTransitionState pipTransitionState = this.mPipTransitionState;
        Objects.requireNonNull(pipTransitionState);
        pipTransitionState.mState = 1;
        this.mLeash = surfaceControl;
        ActivityManager.RunningTaskInfo runningTaskInfo2 = this.mTaskInfo;
        PictureInPictureParams pictureInPictureParams = runningTaskInfo2.pictureInPictureParams;
        this.mPictureInPictureParams = pictureInPictureParams;
        this.mPipBoundsState.setBoundsStateForEntry(runningTaskInfo2.topActivity, runningTaskInfo2.topActivityInfo, pictureInPictureParams, this.mPipBoundsAlgorithm);
        this.mPipUiEventLoggerLogger.setTaskInfo(this.mTaskInfo);
        this.mPipUiEventLoggerLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_ENTER);
        int i = runningTaskInfo.displayId;
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState);
        if (!(i == pipBoundsState.mDisplayId || (intConsumer = this.mOnDisplayIdChangeCallback) == null)) {
            intConsumer.accept(runningTaskInfo.displayId);
        }
        PipTransitionState pipTransitionState2 = this.mPipTransitionState;
        Objects.requireNonNull(pipTransitionState2);
        if (!pipTransitionState2.mInSwipePipToHomeTransition) {
            if (this.mOneShotAnimationType == 1 && SystemClock.uptimeMillis() - this.mLastOneShotAlphaAnimationTime > 1000) {
                Log.d("PipTaskOrganizer", "Alpha animation is expired. Use bounds animation.");
                this.mOneShotAnimationType = 0;
            }
            if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                if (!this.mWaitForFixedRotation) {
                    Rect entryDestinationBounds = this.mPipBoundsAlgorithm.getEntryDestinationBounds();
                    Objects.requireNonNull(entryDestinationBounds, "Missing destination bounds");
                    Rect bounds = this.mTaskInfo.configuration.windowConfiguration.getBounds();
                    int i2 = this.mOneShotAnimationType;
                    if (i2 == 0) {
                        this.mPipMenuController.attach(this.mLeash);
                        scheduleAnimateResizePip(bounds, entryDestinationBounds, 0.0f, PipBoundsAlgorithm.getValidSourceHintRect(runningTaskInfo.pictureInPictureParams, bounds), 2, this.mEnterAnimationDuration, null);
                        PipTransitionState pipTransitionState3 = this.mPipTransitionState;
                        Objects.requireNonNull(pipTransitionState3);
                        pipTransitionState3.mState = 3;
                    } else if (i2 == 1) {
                        enterPipWithAlphaAnimation(entryDestinationBounds, this.mEnterAnimationDuration);
                        this.mOneShotAnimationType = 0;
                    } else {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Unrecognized animation type: ");
                        m.append(this.mOneShotAnimationType);
                        throw new RuntimeException(m.toString());
                    }
                } else if (this.mOneShotAnimationType == 1) {
                    Log.d("PipTaskOrganizer", "Defer entering PiP alpha animation, fixed rotation is ongoing");
                    Objects.requireNonNull(this.mSurfaceControlTransactionFactory);
                    SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                    transaction.setAlpha(this.mLeash, 0.0f);
                    transaction.show(this.mLeash);
                    transaction.apply();
                    this.mOneShotAnimationType = 0;
                } else {
                    Rect bounds2 = this.mTaskInfo.configuration.windowConfiguration.getBounds();
                    animateResizePip(bounds2, this.mPipBoundsAlgorithm.getEntryDestinationBounds(), PipBoundsAlgorithm.getValidSourceHintRect(this.mPictureInPictureParams, bounds2), 2, this.mEnterAnimationDuration, 0.0f);
                    PipTransitionState pipTransitionState4 = this.mPipTransitionState;
                    Objects.requireNonNull(pipTransitionState4);
                    pipTransitionState4.mState = 3;
                }
            }
        } else if (!this.mWaitForFixedRotation) {
            onEndOfSwipePipToHomeTransition();
        } else {
            Log.d("PipTaskOrganizer", "Defer onTaskAppeared-SwipePipToHome until end of fixed rotation.");
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        Rational rational;
        Runnable runnable;
        Objects.requireNonNull(this.mToken, "onTaskInfoChanged requires valid existing mToken");
        PipTransitionState pipTransitionState = this.mPipTransitionState;
        Objects.requireNonNull(pipTransitionState);
        if (pipTransitionState.mState != 4) {
            PipTransitionState pipTransitionState2 = this.mPipTransitionState;
            Objects.requireNonNull(pipTransitionState2);
            if (pipTransitionState2.mState != 5) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Defer onTaskInfoChange in current state: ");
                PipTransitionState pipTransitionState3 = this.mPipTransitionState;
                Objects.requireNonNull(pipTransitionState3);
                KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(m, pipTransitionState3.mState, "PipTaskOrganizer");
                this.mDeferredTaskInfo = runningTaskInfo;
                return;
            }
        }
        this.mPipBoundsState.setLastPipComponentName(runningTaskInfo.topActivity);
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        Size minimalSize = this.mPipBoundsAlgorithm.getMinimalSize(runningTaskInfo.topActivityInfo);
        Objects.requireNonNull(pipBoundsState);
        boolean z = !Objects.equals(minimalSize, pipBoundsState.mOverrideMinSize);
        pipBoundsState.mOverrideMinSize = minimalSize;
        if (z && (runnable = pipBoundsState.mOnMinimalSizeChangeCallback) != null) {
            runnable.run();
        }
        PictureInPictureParams pictureInPictureParams = runningTaskInfo.pictureInPictureParams;
        if (pictureInPictureParams != null) {
            PictureInPictureParams pictureInPictureParams2 = this.mPictureInPictureParams;
            if (pictureInPictureParams2 != null) {
                rational = pictureInPictureParams2.getAspectRatioRational();
            } else {
                rational = null;
            }
            boolean z2 = !Objects.equals(rational, pictureInPictureParams.getAspectRatioRational());
            this.mPictureInPictureParams = pictureInPictureParams;
            if (z2) {
                PipBoundsState pipBoundsState2 = this.mPipBoundsState;
                float aspectRatio = pictureInPictureParams.getAspectRatio();
                Objects.requireNonNull(pipBoundsState2);
                pipBoundsState2.mAspectRatio = aspectRatio;
            }
            if (z2) {
                PipBoundsAlgorithm pipBoundsAlgorithm = this.mPipBoundsAlgorithm;
                Rect bounds = this.mPipBoundsState.getBounds();
                PipBoundsState pipBoundsState3 = this.mPipBoundsState;
                Objects.requireNonNull(pipBoundsState3);
                Rect adjustedDestinationBounds = pipBoundsAlgorithm.getAdjustedDestinationBounds(bounds, pipBoundsState3.mAspectRatio);
                Objects.requireNonNull(adjustedDestinationBounds, "Missing destination bounds");
                scheduleAnimateResizePip(adjustedDestinationBounds, this.mEnterAnimationDuration, 0, null);
                return;
            }
        }
        Log.d("PipTaskOrganizer", "Ignored onTaskInfoChanged with PiP param: " + pictureInPictureParams);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        PipTransitionState pipTransitionState = this.mPipTransitionState;
        Objects.requireNonNull(pipTransitionState);
        if (pipTransitionState.mState != 0) {
            boolean z = Transitions.ENABLE_SHELL_TRANSITIONS;
            if (z) {
                PipTransitionState pipTransitionState2 = this.mPipTransitionState;
                Objects.requireNonNull(pipTransitionState2);
                if (pipTransitionState2.mState == 5) {
                    return;
                }
            }
            WindowContainerToken windowContainerToken = runningTaskInfo.token;
            Objects.requireNonNull(windowContainerToken, "Requires valid WindowContainerToken");
            if (windowContainerToken.asBinder() != this.mToken.asBinder()) {
                Log.wtf("PipTaskOrganizer", "Unrecognized token: " + windowContainerToken);
                return;
            }
            onExitPipFinished(runningTaskInfo);
            if (z) {
                this.mPipTransitionController.forceFinishTransition();
            }
            PipAnimationController pipAnimationController = this.mPipAnimationController;
            Objects.requireNonNull(pipAnimationController);
            PipAnimationController.PipTransitionAnimator pipTransitionAnimator = pipAnimationController.mCurrentAnimator;
            if (pipTransitionAnimator != null) {
                SurfaceControl surfaceControl = pipTransitionAnimator.mContentOverlay;
                if (surfaceControl != null) {
                    PipTaskOrganizer$$ExternalSyntheticLambda3 pipTaskOrganizer$$ExternalSyntheticLambda3 = new PipTaskOrganizer$$ExternalSyntheticLambda3(pipTransitionAnimator, 0);
                    PipTransitionState pipTransitionState3 = this.mPipTransitionState;
                    Objects.requireNonNull(pipTransitionState3);
                    if (pipTransitionState3.mState != 0) {
                        Objects.requireNonNull(this.mSurfaceControlTransactionFactory);
                        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                        transaction.remove(surfaceControl);
                        transaction.apply();
                        pipTaskOrganizer$$ExternalSyntheticLambda3.run();
                    }
                }
                pipTransitionAnimator.removeAllUpdateListeners();
                pipTransitionAnimator.removeAllListeners();
                pipTransitionAnimator.cancel();
            }
        }
    }

    public final void removePip() {
        if (!this.mPipTransitionState.isInPip() || this.mToken == null) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Not allowed to removePip in current state mState=");
            PipTransitionState pipTransitionState = this.mPipTransitionState;
            Objects.requireNonNull(pipTransitionState);
            m.append(pipTransitionState.mState);
            m.append(" mToken=");
            m.append(this.mToken);
            Log.wtf("PipTaskOrganizer", m.toString());
            return;
        }
        PipAnimationController.PipTransitionAnimator transitionDirection = this.mPipAnimationController.getAnimator(this.mTaskInfo, this.mLeash, this.mPipBoundsState.getBounds(), 1.0f, 0.0f).setTransitionDirection(5);
        AnonymousClass2 r2 = this.mPipTransactionHandler;
        Objects.requireNonNull(transitionDirection);
        transitionDirection.mPipTransactionHandler = r2;
        PipAnimationController.PipTransitionAnimator pipAnimationCallback = transitionDirection.setPipAnimationCallback(this.mPipAnimationCallback);
        pipAnimationCallback.setDuration(this.mExitAnimationDuration);
        pipAnimationCallback.setInterpolator(Interpolators.ALPHA_OUT);
        pipAnimationCallback.start();
        PipTransitionState pipTransitionState2 = this.mPipTransitionState;
        Objects.requireNonNull(pipTransitionState2);
        pipTransitionState2.mState = 5;
    }

    public final void scheduleFinishResizePip(Rect rect, int i, Consumer<Rect> consumer) {
        boolean z;
        PipTransitionState pipTransitionState = this.mPipTransitionState;
        Objects.requireNonNull(pipTransitionState);
        int i2 = pipTransitionState.mState;
        if (i2 < 3 || i2 == 5) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            finishResize(createFinishResizeSurfaceTransaction(rect), rect, i, -1);
            if (consumer != null) {
                consumer.accept(rect);
            }
        }
    }

    public final void scheduleUserResizePip(Rect rect, Rect rect2, float f, ShellTaskOrganizer$$ExternalSyntheticLambda1 shellTaskOrganizer$$ExternalSyntheticLambda1) {
        if (this.mToken == null || this.mLeash == null) {
            Log.w("PipTaskOrganizer", "Abort animation, invalid leash");
        } else if (rect.isEmpty() || rect2.isEmpty()) {
            Log.w("PipTaskOrganizer", "Attempted to user resize PIP to or from empty bounds, aborting.");
        } else {
            Objects.requireNonNull(this.mSurfaceControlTransactionFactory);
            SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
            PipSurfaceTransactionHelper pipSurfaceTransactionHelper = this.mSurfaceTransactionHelper;
            pipSurfaceTransactionHelper.scale(transaction, this.mLeash, rect, rect2, f);
            pipSurfaceTransactionHelper.round(transaction, this.mLeash, rect, rect2);
            if (this.mPipMenuController.isMenuVisible()) {
                this.mPipMenuController.movePipMenu(this.mLeash, transaction, rect2);
            } else {
                transaction.apply();
            }
            if (shellTaskOrganizer$$ExternalSyntheticLambda1 != null) {
                shellTaskOrganizer$$ExternalSyntheticLambda1.accept(rect2);
            }
        }
    }

    public final String toString() {
        StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m("PipTaskOrganizer", ":");
        m.append(ShellTaskOrganizer.taskListenerTypeToString(-4));
        return m.toString();
    }

    public final void finishResizeForMenu(Rect rect) {
        if (isInPip()) {
            this.mPipMenuController.movePipMenu(null, null, rect);
            this.mPipMenuController.updateMenuBounds(rect);
        }
    }

    public final void prepareFinishResizeTransaction(Rect rect, final int i, SurfaceControl.Transaction transaction, final WindowContainerTransaction windowContainerTransaction) {
        if (PipAnimationController.isInPipDirection(i)) {
            windowContainerTransaction.setActivityWindowingMode(this.mToken, 0);
        } else if (PipAnimationController.isOutPipDirection(i)) {
            rect = null;
            windowContainerTransaction.setWindowingMode(this.mToken, 0);
            windowContainerTransaction.setActivityWindowingMode(this.mToken, 0);
            this.mLegacySplitScreenOptional.ifPresent(new Consumer() { // from class: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda7
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    WindowContainerToken windowContainerToken;
                    ActivityManager.RunningTaskInfo runningTaskInfo;
                    PipTaskOrganizer pipTaskOrganizer = PipTaskOrganizer.this;
                    int i2 = i;
                    WindowContainerTransaction windowContainerTransaction2 = windowContainerTransaction;
                    LegacySplitScreenController legacySplitScreenController = (LegacySplitScreenController) obj;
                    if (i2 == 4) {
                        WindowContainerToken windowContainerToken2 = pipTaskOrganizer.mToken;
                        Objects.requireNonNull(legacySplitScreenController);
                        LegacySplitScreenTaskListener legacySplitScreenTaskListener = legacySplitScreenController.mSplits;
                        if (legacySplitScreenTaskListener == null || (runningTaskInfo = legacySplitScreenTaskListener.mSecondary) == null) {
                            windowContainerToken = null;
                        } else {
                            windowContainerToken = runningTaskInfo.token;
                        }
                        windowContainerTransaction2.reparent(windowContainerToken2, windowContainerToken, true);
                        return;
                    }
                    Objects.requireNonNull(pipTaskOrganizer);
                }
            });
        }
        this.mSurfaceTransactionHelper.round(transaction, this.mLeash, isInPip());
        windowContainerTransaction.setBounds(this.mToken, rect);
        windowContainerTransaction.setBoundsChangeTransaction(this.mToken, transaction);
    }

    public final PipAnimationController.PipTransitionAnimator<?> scheduleAnimateResizePip(Rect rect, Rect rect2, float f, Rect rect3, int i, int i2, Consumer<Rect> consumer) {
        if (!this.mPipTransitionState.isInPip()) {
            return null;
        }
        PipAnimationController.PipTransitionAnimator<?> animateResizePip = animateResizePip(rect, rect2, rect3, i, i2, f);
        if (consumer != null) {
            consumer.accept(rect2);
        }
        return animateResizePip;
    }
}
