package com.android.wm.shell.splitscreen;

import android.animation.Animator;
import android.os.IBinder;
import android.view.SurfaceControl;
import android.window.RemoteTransition;
import android.window.WindowContainerTransaction;
import androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.splitscreen.StageCoordinator;
import com.android.wm.shell.transition.OneShotRemoteHandler;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SplitScreenTransitions {
    public SurfaceControl.Transaction mFinishTransaction;
    public final Runnable mOnFinish;
    public final StageCoordinator mStageCoordinator;
    public final TransactionPool mTransactionPool;
    public final Transitions mTransitions;
    public DismissTransition mPendingDismiss = null;
    public IBinder mPendingEnter = null;
    public IBinder mPendingRecent = null;
    public IBinder mAnimatingTransition = null;
    public OneShotRemoteHandler mPendingRemoteHandler = null;
    public OneShotRemoteHandler mActiveRemoteHandler = null;
    public final SplitScreenTransitions$$ExternalSyntheticLambda2 mRemoteFinishCB = new Transitions.TransitionFinishCallback() { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda2
        @Override // com.android.wm.shell.transition.Transitions.TransitionFinishCallback
        public final void onTransitionFinished(WindowContainerTransaction windowContainerTransaction) {
            SplitScreenTransitions.this.onFinish(windowContainerTransaction);
        }
    };
    public final ArrayList<Animator> mAnimations = new ArrayList<>();
    public Transitions.TransitionFinishCallback mFinishCallback = null;

    public final IBinder startDismissTransition(IBinder iBinder, WindowContainerTransaction windowContainerTransaction, Transitions.TransitionHandler transitionHandler, int i, int i2) {
        int i3;
        String str;
        if (i2 == 4) {
            i3 = 18;
        } else {
            i3 = 19;
        }
        if (iBinder == null) {
            iBinder = this.mTransitions.startTransition(i3, windowContainerTransaction, transitionHandler);
        }
        this.mPendingDismiss = new DismissTransition(iBinder, i2, i);
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            String valueOf = String.valueOf(SplitScreenController.exitReasonToString(i2));
            if (i == -1) {
                str = "UNDEFINED";
            } else if (i == 0) {
                str = "MAIN";
            } else if (i != 1) {
                str = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("UNKNOWN(", i, ")");
            } else {
                str = "SIDE";
            }
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1852066478, 0, "  splitTransition  deduced Dismiss due to %s. toTop=%s", valueOf, String.valueOf(str));
        }
        return iBinder;
    }

    /* loaded from: classes.dex */
    public static class DismissTransition {
        public int mDismissTop;
        public int mReason;
        public IBinder mTransition;

        public DismissTransition(IBinder iBinder, int i, int i2) {
            this.mTransition = iBinder;
            this.mReason = i;
            this.mDismissTop = i2;
        }
    }

    public final void onFinish(WindowContainerTransaction windowContainerTransaction) {
        boolean z;
        if (this.mAnimations.isEmpty()) {
            this.mOnFinish.run();
            SurfaceControl.Transaction transaction = this.mFinishTransaction;
            if (transaction != null) {
                transaction.apply();
                this.mTransactionPool.release(this.mFinishTransaction);
                this.mFinishTransaction = null;
            }
            Transitions.TransitionFinishCallback transitionFinishCallback = this.mFinishCallback;
            if (transitionFinishCallback != null) {
                transitionFinishCallback.onTransitionFinished(windowContainerTransaction);
                this.mFinishCallback = null;
            }
            IBinder iBinder = this.mAnimatingTransition;
            if (iBinder == this.mPendingEnter) {
                this.mPendingEnter = null;
            }
            DismissTransition dismissTransition = this.mPendingDismiss;
            if (dismissTransition != null && dismissTransition.mTransition == iBinder) {
                this.mPendingDismiss = null;
            }
            if (iBinder == this.mPendingRecent) {
                if (windowContainerTransaction == null) {
                    z = true;
                } else {
                    z = false;
                }
                StageCoordinator stageCoordinator = this.mStageCoordinator;
                Objects.requireNonNull(stageCoordinator);
                MainStage mainStage = stageCoordinator.mMainStage;
                Objects.requireNonNull(mainStage);
                if (!mainStage.mIsActive) {
                    StageCoordinator.StageListenerImpl stageListenerImpl = stageCoordinator.mMainStageListener;
                    StageCoordinator.StageListenerImpl stageListenerImpl2 = stageCoordinator.mSideStageListener;
                    stageListenerImpl2.mVisible = false;
                    stageListenerImpl.mVisible = false;
                    stageListenerImpl2.mHasChildren = false;
                    stageListenerImpl.mHasChildren = false;
                } else if (z) {
                    WindowContainerTransaction windowContainerTransaction2 = new WindowContainerTransaction();
                    stageCoordinator.prepareExitSplitScreen(-1, windowContainerTransaction2);
                    stageCoordinator.mSplitTransitions.startDismissTransition(null, windowContainerTransaction2, stageCoordinator, -1, 5);
                } else {
                    stageCoordinator.setDividerVisibility(true, null);
                }
                this.mPendingRecent = null;
            }
            this.mPendingRemoteHandler = null;
            this.mActiveRemoteHandler = null;
            this.mAnimatingTransition = null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x014d, code lost:
        if (r23.equals(r12.getContainer()) != false) goto L_0x0152;
     */
    /* JADX WARN: Type inference failed for: r9v3, types: [com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda4] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void playAnimation(android.os.IBinder r17, android.window.TransitionInfo r18, android.view.SurfaceControl.Transaction r19, android.view.SurfaceControl.Transaction r20, com.android.wm.shell.transition.Transitions$$ExternalSyntheticLambda1 r21, android.window.WindowContainerToken r22, android.window.WindowContainerToken r23) {
        /*
            Method dump skipped, instructions count: 468
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.splitscreen.SplitScreenTransitions.playAnimation(android.os.IBinder, android.window.TransitionInfo, android.view.SurfaceControl$Transaction, android.view.SurfaceControl$Transaction, com.android.wm.shell.transition.Transitions$$ExternalSyntheticLambda1, android.window.WindowContainerToken, android.window.WindowContainerToken):void");
    }

    public final IBinder startEnterTransition(int i, WindowContainerTransaction windowContainerTransaction, RemoteTransition remoteTransition, StageCoordinator stageCoordinator) {
        IBinder startTransition = this.mTransitions.startTransition(i, windowContainerTransaction, stageCoordinator);
        this.mPendingEnter = startTransition;
        if (remoteTransition != null) {
            Transitions transitions = this.mTransitions;
            Objects.requireNonNull(transitions);
            OneShotRemoteHandler oneShotRemoteHandler = new OneShotRemoteHandler(transitions.mMainExecutor, remoteTransition);
            this.mPendingRemoteHandler = oneShotRemoteHandler;
            oneShotRemoteHandler.mTransition = startTransition;
        }
        return startTransition;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda3] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void startExampleAnimation(final android.view.SurfaceControl r8, boolean r9) {
        /*
            r7 = this;
            r0 = 1065353216(0x3f800000, float:1.0)
            if (r9 == 0) goto L_0x0006
            r5 = r0
            goto L_0x0008
        L_0x0006:
            r9 = 0
            r5 = r9
        L_0x0008:
            float r0 = r0 - r5
            com.android.wm.shell.common.TransactionPool r9 = r7.mTransactionPool
            android.view.SurfaceControl$Transaction r3 = r9.acquire()
            r9 = 2
            float[] r9 = new float[r9]
            r1 = 0
            r9[r1] = r0
            r1 = 1
            r9[r1] = r5
            android.animation.ValueAnimator r9 = android.animation.ValueAnimator.ofFloat(r9)
            r1 = 500(0x1f4, double:2.47E-321)
            r9.setDuration(r1)
            com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda0 r1 = new com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda0
            r1.<init>()
            r9.addUpdateListener(r1)
            com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda3 r0 = new com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda3
            r1 = r0
            r2 = r7
            r4 = r8
            r6 = r9
            r1.<init>()
            com.android.wm.shell.splitscreen.SplitScreenTransitions$1 r8 = new com.android.wm.shell.splitscreen.SplitScreenTransitions$1
            r8.<init>()
            r9.addListener(r8)
            java.util.ArrayList<android.animation.Animator> r8 = r7.mAnimations
            r8.add(r9)
            com.android.wm.shell.transition.Transitions r7 = r7.mTransitions
            java.util.Objects.requireNonNull(r7)
            com.android.wm.shell.common.ShellExecutor r7 = r7.mAnimExecutor
            com.android.wifitrackerlib.BaseWifiTracker$$ExternalSyntheticLambda1 r8 = new com.android.wifitrackerlib.BaseWifiTracker$$ExternalSyntheticLambda1
            r0 = 9
            r8.<init>(r9, r0)
            r7.execute(r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.splitscreen.SplitScreenTransitions.startExampleAnimation(android.view.SurfaceControl, boolean):void");
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda2] */
    public SplitScreenTransitions(TransactionPool transactionPool, Transitions transitions, Runnable runnable, StageCoordinator stageCoordinator) {
        this.mTransactionPool = transactionPool;
        this.mTransitions = transitions;
        this.mOnFinish = runnable;
        this.mStageCoordinator = stageCoordinator;
    }
}
