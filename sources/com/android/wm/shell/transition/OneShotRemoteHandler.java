package com.android.wm.shell.transition;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.SurfaceControl;
import android.window.IRemoteTransition;
import android.window.IRemoteTransitionFinishedCallback;
import android.window.RemoteTransition;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.Transitions;
/* loaded from: classes.dex */
public final class OneShotRemoteHandler implements Transitions.TransitionHandler {
    public final ShellExecutor mMainExecutor;
    public final RemoteTransition mRemote;
    public IBinder mTransition = null;

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public final void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, final Transitions$$ExternalSyntheticLambda0 transitions$$ExternalSyntheticLambda0) {
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1649273831, 0, "Using registered One-shot remote transition %s for %s.", String.valueOf(this.mRemote), String.valueOf(iBinder));
        }
        try {
            this.mRemote.getRemoteTransition().mergeAnimation(iBinder, transitionInfo, transaction, iBinder2, new IRemoteTransitionFinishedCallback.Stub() { // from class: com.android.wm.shell.transition.OneShotRemoteHandler.2
                public final void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, SurfaceControl.Transaction transaction2) {
                    OneShotRemoteHandler.this.mMainExecutor.execute(new OneShotRemoteHandler$2$$ExternalSyntheticLambda0(transitions$$ExternalSyntheticLambda0, windowContainerTransaction, 0));
                }
            });
        } catch (RemoteException e) {
            Log.e("ShellTransitions", "Error merging remote transition.", e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2, types: [android.os.IBinder$DeathRecipient, com.android.wm.shell.transition.OneShotRemoteHandler$$ExternalSyntheticLambda0] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean startAnimation(android.os.IBinder r9, android.window.TransitionInfo r10, android.view.SurfaceControl.Transaction r11, final android.view.SurfaceControl.Transaction r12, final com.android.wm.shell.transition.Transitions.TransitionFinishCallback r13) {
        /*
            r8 = this;
            java.lang.String r0 = "ShellTransitions"
            android.os.IBinder r1 = r8.mTransition
            r2 = 0
            if (r1 == r9) goto L_0x0008
            return r2
        L_0x0008:
            boolean r1 = com.android.wm.shell.protolog.ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled
            r3 = 1
            if (r1 == 0) goto L_0x0028
            android.window.RemoteTransition r1 = r8.mRemote
            java.lang.String r1 = java.lang.String.valueOf(r1)
            java.lang.String r4 = java.lang.String.valueOf(r9)
            com.android.wm.shell.protolog.ShellProtoLogGroup r5 = com.android.wm.shell.protolog.ShellProtoLogGroup.WM_SHELL_TRANSITIONS
            r6 = 1649273831(0x624debe7, float:9.496453E20)
            r7 = 2
            java.lang.Object[] r7 = new java.lang.Object[r7]
            r7[r2] = r1
            r7[r3] = r4
            java.lang.String r1 = "Using registered One-shot remote transition %s for %s."
            com.android.wm.shell.protolog.ShellProtoLogImpl.v(r5, r6, r2, r1, r7)
        L_0x0028:
            com.android.wm.shell.transition.OneShotRemoteHandler$$ExternalSyntheticLambda0 r1 = new com.android.wm.shell.transition.OneShotRemoteHandler$$ExternalSyntheticLambda0
            r1.<init>()
            com.android.wm.shell.transition.OneShotRemoteHandler$1 r4 = new com.android.wm.shell.transition.OneShotRemoteHandler$1
            r4.<init>()
            android.window.RemoteTransition r12 = r8.mRemote     // Catch: RemoteException -> 0x0060
            android.os.IBinder r12 = r12.asBinder()     // Catch: RemoteException -> 0x0060
            if (r12 == 0) goto L_0x0043
            android.window.RemoteTransition r12 = r8.mRemote     // Catch: RemoteException -> 0x0060
            android.os.IBinder r12 = r12.asBinder()     // Catch: RemoteException -> 0x0060
            r12.linkToDeath(r1, r2)     // Catch: RemoteException -> 0x0060
        L_0x0043:
            android.app.IActivityTaskManager r12 = android.app.ActivityTaskManager.getService()     // Catch: SecurityException -> 0x0051, RemoteException -> 0x0060
            android.window.RemoteTransition r5 = r8.mRemote     // Catch: SecurityException -> 0x0051, RemoteException -> 0x0060
            android.app.IApplicationThread r5 = r5.getAppThread()     // Catch: SecurityException -> 0x0051, RemoteException -> 0x0060
            r12.setRunningRemoteTransitionDelegate(r5)     // Catch: SecurityException -> 0x0051, RemoteException -> 0x0060
            goto L_0x0056
        L_0x0051:
            java.lang.String r12 = "Unable to boost animation thread. This should only happen during unit tests"
            android.util.Slog.e(r0, r12)     // Catch: RemoteException -> 0x0060
        L_0x0056:
            android.window.RemoteTransition r12 = r8.mRemote     // Catch: RemoteException -> 0x0060
            android.window.IRemoteTransition r12 = r12.getRemoteTransition()     // Catch: RemoteException -> 0x0060
            r12.startAnimation(r9, r10, r11, r4)     // Catch: RemoteException -> 0x0060
            goto L_0x007b
        L_0x0060:
            r9 = move-exception
            java.lang.String r10 = "Error running remote transition."
            android.util.Log.e(r0, r10, r9)
            android.window.RemoteTransition r9 = r8.mRemote
            android.os.IBinder r9 = r9.asBinder()
            if (r9 == 0) goto L_0x0077
            android.window.RemoteTransition r8 = r8.mRemote
            android.os.IBinder r8 = r8.asBinder()
            r8.unlinkToDeath(r1, r2)
        L_0x0077:
            r8 = 0
            r13.onTransitionFinished(r8)
        L_0x007b:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.transition.OneShotRemoteHandler.startAnimation(android.os.IBinder, android.window.TransitionInfo, android.view.SurfaceControl$Transaction, android.view.SurfaceControl$Transaction, com.android.wm.shell.transition.Transitions$TransitionFinishCallback):boolean");
    }

    public OneShotRemoteHandler(ShellExecutor shellExecutor, RemoteTransition remoteTransition) {
        this.mMainExecutor = shellExecutor;
        this.mRemote = remoteTransition;
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public final WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        IRemoteTransition iRemoteTransition;
        RemoteTransition remoteTransition = transitionRequestInfo.getRemoteTransition();
        if (remoteTransition != null) {
            iRemoteTransition = remoteTransition.getRemoteTransition();
        } else {
            iRemoteTransition = null;
        }
        if (iRemoteTransition != this.mRemote.getRemoteTransition()) {
            return null;
        }
        this.mTransition = iBinder;
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 967375804, 0, "RemoteTransition directly requested for %s: %s", String.valueOf(iBinder), String.valueOf(remoteTransition));
        }
        return new WindowContainerTransaction();
    }
}
