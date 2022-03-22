package com.android.wm.shell.transition;

import android.os.RemoteException;
import android.util.Slog;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import android.window.IWindowContainerTransactionCallback;
import com.android.wm.shell.splitscreen.SplitScreenController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LegacyTransitions$LegacyTransition {
    public RemoteAnimationTarget[] mApps;
    public final LegacyTransitions$ILegacyTransition mLegacyTransition;
    public SurfaceControl.Transaction mTransaction;
    public int mSyncId = -1;
    public IRemoteAnimationFinishedCallback mFinishCallback = null;
    public boolean mCancelled = false;
    public final SyncCallback mSyncCallback = new SyncCallback();
    public final RemoteAnimationAdapter mAdapter = new RemoteAnimationAdapter(new RemoteAnimationWrapper(), 0, 0);
    public int mTransit = 1;

    /* loaded from: classes.dex */
    public class RemoteAnimationWrapper extends IRemoteAnimationRunner.Stub {
        public RemoteAnimationWrapper() {
        }

        public final void onAnimationCancelled() throws RemoteException {
            LegacyTransitions$LegacyTransition legacyTransitions$LegacyTransition = LegacyTransitions$LegacyTransition.this;
            legacyTransitions$LegacyTransition.mCancelled = true;
            legacyTransitions$LegacyTransition.mApps = null;
            LegacyTransitions$LegacyTransition.m158$$Nest$mcheckApply(legacyTransitions$LegacyTransition);
        }

        public final void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) throws RemoteException {
            LegacyTransitions$LegacyTransition legacyTransitions$LegacyTransition = LegacyTransitions$LegacyTransition.this;
            legacyTransitions$LegacyTransition.mTransit = i;
            legacyTransitions$LegacyTransition.mApps = remoteAnimationTargetArr;
            legacyTransitions$LegacyTransition.mFinishCallback = iRemoteAnimationFinishedCallback;
            LegacyTransitions$LegacyTransition.m158$$Nest$mcheckApply(legacyTransitions$LegacyTransition);
        }
    }

    /* loaded from: classes.dex */
    public class SyncCallback extends IWindowContainerTransactionCallback.Stub {
        public SyncCallback() {
        }

        public final void onTransactionReady(int i, SurfaceControl.Transaction transaction) throws RemoteException {
            LegacyTransitions$LegacyTransition legacyTransitions$LegacyTransition = LegacyTransitions$LegacyTransition.this;
            legacyTransitions$LegacyTransition.mSyncId = i;
            legacyTransitions$LegacyTransition.mTransaction = transaction;
            LegacyTransitions$LegacyTransition.m158$$Nest$mcheckApply(legacyTransitions$LegacyTransition);
        }
    }

    /* renamed from: -$$Nest$mcheckApply  reason: not valid java name */
    public static void m158$$Nest$mcheckApply(LegacyTransitions$LegacyTransition legacyTransitions$LegacyTransition) {
        Objects.requireNonNull(legacyTransitions$LegacyTransition);
        if (legacyTransitions$LegacyTransition.mSyncId >= 0) {
            IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback = legacyTransitions$LegacyTransition.mFinishCallback;
            if (iRemoteAnimationFinishedCallback != null || legacyTransitions$LegacyTransition.mCancelled) {
                LegacyTransitions$ILegacyTransition legacyTransitions$ILegacyTransition = legacyTransitions$LegacyTransition.mLegacyTransition;
                RemoteAnimationTarget[] remoteAnimationTargetArr = legacyTransitions$LegacyTransition.mApps;
                SurfaceControl.Transaction transaction = legacyTransitions$LegacyTransition.mTransaction;
                SplitScreenController.AnonymousClass1 r1 = (SplitScreenController.AnonymousClass1) legacyTransitions$ILegacyTransition;
                Objects.requireNonNull(r1);
                if (remoteAnimationTargetArr == null || remoteAnimationTargetArr.length == 0) {
                    transaction.apply();
                    return;
                }
                SplitScreenController.this.mStageCoordinator.updateSurfaceBounds(null, transaction);
                for (int i = 0; i < remoteAnimationTargetArr.length; i++) {
                    if (remoteAnimationTargetArr[i].mode == 0) {
                        transaction.show(remoteAnimationTargetArr[i].leash);
                    }
                }
                transaction.apply();
                if (iRemoteAnimationFinishedCallback != null) {
                    try {
                        iRemoteAnimationFinishedCallback.onAnimationFinished();
                    } catch (RemoteException e) {
                        Slog.e("SplitScreenController", "Error finishing legacy transition: ", e);
                    }
                }
                SplitScreenController.this.mSyncQueue.queue(r1.val$evictWct);
            }
        }
    }

    public LegacyTransitions$LegacyTransition(SplitScreenController.AnonymousClass1 r8) {
        this.mLegacyTransition = r8;
    }
}
