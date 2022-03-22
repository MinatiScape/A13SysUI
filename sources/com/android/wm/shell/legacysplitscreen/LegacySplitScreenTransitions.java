package com.android.wm.shell.legacysplitscreen;

import android.animation.Animator;
import android.app.ActivityManager;
import android.os.IBinder;
import android.view.SurfaceControl;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LegacySplitScreenTransitions implements Transitions.TransitionHandler {
    public SurfaceControl.Transaction mFinishTransaction;
    public final LegacySplitScreenTaskListener mListener;
    public final LegacySplitScreenController mSplitScreen;
    public final TransactionPool mTransactionPool;
    public final Transitions mTransitions;
    public IBinder mPendingDismiss = null;
    public boolean mDismissFromSnap = false;
    public IBinder mPendingEnter = null;
    public IBinder mAnimatingTransition = null;
    public final ArrayList<Animator> mAnimations = new ArrayList<>();
    public Transitions.TransitionFinishCallback mFinishCallback = null;

    public final void onFinish() {
        if (this.mAnimations.isEmpty()) {
            this.mFinishTransaction.apply();
            this.mTransactionPool.release(this.mFinishTransaction);
            this.mFinishTransaction = null;
            this.mFinishCallback.onTransitionFinished(null);
            this.mFinishCallback = null;
            IBinder iBinder = this.mAnimatingTransition;
            if (iBinder == this.mPendingEnter) {
                this.mPendingEnter = null;
            }
            if (iBinder == this.mPendingDismiss) {
                this.mSplitScreen.onDismissSplit();
                this.mPendingDismiss = null;
            }
            this.mDismissFromSnap = false;
            this.mAnimatingTransition = null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:73:0x020e, code lost:
        if (r10 == 4) goto L_0x0212;
     */
    /* JADX WARN: Type inference failed for: r11v4, types: [com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda3] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean startAnimation(android.os.IBinder r19, android.window.TransitionInfo r20, android.view.SurfaceControl.Transaction r21, android.view.SurfaceControl.Transaction r22, com.android.wm.shell.transition.Transitions.TransitionFinishCallback r23) {
        /*
            Method dump skipped, instructions count: 680
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions.startAnimation(android.os.IBinder, android.window.TransitionInfo, android.view.SurfaceControl$Transaction, android.view.SurfaceControl$Transaction, com.android.wm.shell.transition.Transitions$TransitionFinishCallback):boolean");
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda2] */
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
            com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda0 r1 = new com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda0
            r1.<init>()
            r9.addUpdateListener(r1)
            com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda2 r0 = new com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda2
            r1 = r0
            r2 = r7
            r4 = r8
            r6 = r9
            r1.<init>()
            com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$1 r8 = new com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$1
            r8.<init>()
            r9.addListener(r8)
            java.util.ArrayList<android.animation.Animator> r8 = r7.mAnimations
            r8.add(r9)
            com.android.wm.shell.transition.Transitions r7 = r7.mTransitions
            java.util.Objects.requireNonNull(r7)
            com.android.wm.shell.common.ShellExecutor r7 = r7.mAnimExecutor
            com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda0 r8 = new com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda0
            r0 = 7
            r8.<init>(r9, r0)
            r7.execute(r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions.startExampleAnimation(android.view.SurfaceControl, boolean):void");
    }

    public LegacySplitScreenTransitions(TransactionPool transactionPool, Transitions transitions, LegacySplitScreenController legacySplitScreenController, LegacySplitScreenTaskListener legacySplitScreenTaskListener) {
        this.mTransactionPool = transactionPool;
        this.mTransitions = transitions;
        this.mSplitScreen = legacySplitScreenController;
        this.mListener = legacySplitScreenTaskListener;
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public final WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        boolean z;
        ActivityManager.RunningTaskInfo triggerTask = transitionRequestInfo.getTriggerTask();
        int type = transitionRequestInfo.getType();
        if (this.mSplitScreen.isDividerVisible()) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            if (triggerTask == null) {
                return windowContainerTransaction;
            }
            if (((type == 2 || type == 4) && triggerTask.parentTaskId == this.mListener.mPrimary.taskId) || ((type == 1 || type == 3) && !triggerTask.supportsMultiWindow)) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                return windowContainerTransaction;
            }
            LegacySplitScreenTaskListener legacySplitScreenTaskListener = this.mListener;
            LegacySplitScreenController legacySplitScreenController = this.mSplitScreen;
            Objects.requireNonNull(legacySplitScreenController);
            WindowManagerProxy.buildDismissSplit(windowContainerTransaction, legacySplitScreenTaskListener, legacySplitScreenController.mSplitLayout, true);
            if (type == 1 || type == 3) {
                windowContainerTransaction.reorder(triggerTask.token, true);
            }
            this.mPendingDismiss = iBinder;
            return windowContainerTransaction;
        } else if (triggerTask == null || ((type != 1 && type != 3) || triggerTask.configuration.windowConfiguration.getWindowingMode() != 3)) {
            return null;
        } else {
            WindowContainerTransaction windowContainerTransaction2 = new WindowContainerTransaction();
            LegacySplitScreenController legacySplitScreenController2 = this.mSplitScreen;
            Objects.requireNonNull(legacySplitScreenController2);
            WindowManagerProxy windowManagerProxy = legacySplitScreenController2.mWindowManagerProxy;
            LegacySplitScreenTaskListener legacySplitScreenTaskListener2 = legacySplitScreenController2.mSplits;
            LegacySplitDisplayLayout legacySplitDisplayLayout = legacySplitScreenController2.mRotateSplitLayout;
            if (legacySplitDisplayLayout == null) {
                legacySplitDisplayLayout = legacySplitScreenController2.mSplitLayout;
            }
            legacySplitScreenController2.mHomeStackResizable = windowManagerProxy.buildEnterSplit(windowContainerTransaction2, legacySplitScreenTaskListener2, legacySplitDisplayLayout);
            this.mPendingEnter = iBinder;
            return windowContainerTransaction2;
        }
    }
}
