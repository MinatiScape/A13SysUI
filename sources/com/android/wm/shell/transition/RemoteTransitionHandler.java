package com.android.wm.shell.transition;

import android.app.ActivityTaskManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.view.SurfaceControl;
import android.window.IRemoteTransition;
import android.window.IRemoteTransitionFinishedCallback;
import android.window.RemoteTransition;
import android.window.TransitionFilter;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda5;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda19;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.RemoteTransitionHandler;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class RemoteTransitionHandler implements Transitions.TransitionHandler {
    public final ShellExecutor mMainExecutor;
    public final ArrayMap<IBinder, RemoteTransition> mRequestedRemotes = new ArrayMap<>();
    public final ArrayList<Pair<TransitionFilter, RemoteTransition>> mFilters = new ArrayList<>();
    public final ArrayMap<IBinder, RemoteDeathHandler> mDeathHandlers = new ArrayMap<>();

    /* renamed from: com.android.wm.shell.transition.RemoteTransitionHandler$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends IRemoteTransitionFinishedCallback.Stub {
        public final /* synthetic */ Transitions.TransitionFinishCallback val$finishCallback;
        public final /* synthetic */ SurfaceControl.Transaction val$finishTransaction;
        public final /* synthetic */ RemoteTransition val$remote;
        public final /* synthetic */ IBinder val$transition;

        public AnonymousClass1(RemoteTransition remoteTransition, Transitions$$ExternalSyntheticLambda1 transitions$$ExternalSyntheticLambda1, SurfaceControl.Transaction transaction, IBinder iBinder) {
            this.val$remote = remoteTransition;
            this.val$finishCallback = transitions$$ExternalSyntheticLambda1;
            this.val$finishTransaction = transaction;
            this.val$transition = iBinder;
        }

        public final void onTransitionFinished(final WindowContainerTransaction windowContainerTransaction, final SurfaceControl.Transaction transaction) {
            RemoteTransitionHandler.this.unhandleDeath(this.val$remote.asBinder(), this.val$finishCallback);
            ShellExecutor shellExecutor = RemoteTransitionHandler.this.mMainExecutor;
            final SurfaceControl.Transaction transaction2 = this.val$finishTransaction;
            final IBinder iBinder = this.val$transition;
            final Transitions.TransitionFinishCallback transitionFinishCallback = this.val$finishCallback;
            shellExecutor.execute(new Runnable() { // from class: com.android.wm.shell.transition.RemoteTransitionHandler$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    RemoteTransitionHandler.AnonymousClass1 r0 = RemoteTransitionHandler.AnonymousClass1.this;
                    SurfaceControl.Transaction transaction3 = transaction;
                    SurfaceControl.Transaction transaction4 = transaction2;
                    IBinder iBinder2 = iBinder;
                    Transitions.TransitionFinishCallback transitionFinishCallback2 = transitionFinishCallback;
                    WindowContainerTransaction windowContainerTransaction2 = windowContainerTransaction;
                    if (transaction3 != null) {
                        Objects.requireNonNull(r0);
                        transaction4.merge(transaction3);
                    }
                    RemoteTransitionHandler.this.mRequestedRemotes.remove(iBinder2);
                    transitionFinishCallback2.onTransitionFinished(windowContainerTransaction2);
                }
            });
        }
    }

    /* renamed from: com.android.wm.shell.transition.RemoteTransitionHandler$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass2 extends IRemoteTransitionFinishedCallback.Stub {
        public final /* synthetic */ Transitions.TransitionFinishCallback val$finishCallback;
        public final /* synthetic */ IBinder val$mergeTarget;

        public AnonymousClass2(IBinder iBinder, Transitions$$ExternalSyntheticLambda0 transitions$$ExternalSyntheticLambda0) {
            this.val$mergeTarget = iBinder;
            this.val$finishCallback = transitions$$ExternalSyntheticLambda0;
        }

        public final void onTransitionFinished(final WindowContainerTransaction windowContainerTransaction, SurfaceControl.Transaction transaction) {
            ShellExecutor shellExecutor = RemoteTransitionHandler.this.mMainExecutor;
            final IBinder iBinder = this.val$mergeTarget;
            final Transitions.TransitionFinishCallback transitionFinishCallback = this.val$finishCallback;
            shellExecutor.execute(new Runnable() { // from class: com.android.wm.shell.transition.RemoteTransitionHandler$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    RemoteTransitionHandler.AnonymousClass2 r0 = RemoteTransitionHandler.AnonymousClass2.this;
                    IBinder iBinder2 = iBinder;
                    Transitions.TransitionFinishCallback transitionFinishCallback2 = transitionFinishCallback;
                    WindowContainerTransaction windowContainerTransaction2 = windowContainerTransaction;
                    Objects.requireNonNull(r0);
                    if (!RemoteTransitionHandler.this.mRequestedRemotes.containsKey(iBinder2)) {
                        Log.e("RemoteTransitionHandler", "Merged transition finished after it's mergeTarget (the transition it was supposed to merge into). This usually means that the mergeTarget's RemoteTransition impl erroneously accepted/ran the merge request after finishing the mergeTarget");
                    }
                    transitionFinishCallback2.onTransitionFinished(windowContainerTransaction2);
                }
            });
        }
    }

    /* loaded from: classes.dex */
    public class RemoteDeathHandler implements IBinder.DeathRecipient {
        public final IBinder mRemote;
        public final ArrayList<Transitions.TransitionFinishCallback> mPendingFinishCallbacks = new ArrayList<>();
        public int mUsers = 0;

        public RemoteDeathHandler(IBinder iBinder) {
            this.mRemote = iBinder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public final void binderDied() {
            RemoteTransitionHandler.this.mMainExecutor.execute(new TaskView$$ExternalSyntheticLambda5(this, 7));
        }
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public final boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback) {
        RemoteTransition remoteTransition = this.mRequestedRemotes.get(iBinder);
        if (remoteTransition == null) {
            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -1269886472, 0, "Transition %s doesn't have explicit remote, search filters for match for %s", String.valueOf(iBinder), String.valueOf(transitionInfo));
            }
            int size = this.mFilters.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 990371881, 0, " Checking filter %s", String.valueOf(this.mFilters.get(size)));
                }
                if (((TransitionFilter) this.mFilters.get(size).first).matches(transitionInfo)) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Found filter");
                    m.append(this.mFilters.get(size));
                    Slog.d("RemoteTransitionHandler", m.toString());
                    remoteTransition = (RemoteTransition) this.mFilters.get(size).second;
                    this.mRequestedRemotes.put(iBinder, remoteTransition);
                    break;
                }
                size--;
            }
        }
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -1671119352, 0, " Delegate animation for %s to %s", String.valueOf(iBinder), String.valueOf(remoteTransition));
        }
        if (remoteTransition == null) {
            return false;
        }
        AnonymousClass1 r13 = new AnonymousClass1(remoteTransition, (Transitions$$ExternalSyntheticLambda1) transitionFinishCallback, transaction2, iBinder);
        try {
            handleDeath(remoteTransition.asBinder(), (Transitions$$ExternalSyntheticLambda1) transitionFinishCallback);
            try {
                ActivityTaskManager.getService().setRunningRemoteTransitionDelegate(remoteTransition.getAppThread());
            } catch (SecurityException unused) {
                Log.e("ShellTransitions", "Unable to boost animation thread. This should only happen during unit tests");
            }
            remoteTransition.getRemoteTransition().startAnimation(iBinder, transitionInfo, transaction, r13);
        } catch (RemoteException e) {
            Log.e("ShellTransitions", "Error running remote transition.", e);
            unhandleDeath(remoteTransition.asBinder(), transitionFinishCallback);
            this.mRequestedRemotes.remove(iBinder);
            this.mMainExecutor.execute(new BubbleStackView$$ExternalSyntheticLambda19(transitionFinishCallback, 7));
        }
        return true;
    }

    public final void handleDeath(IBinder iBinder, Transitions$$ExternalSyntheticLambda1 transitions$$ExternalSyntheticLambda1) {
        synchronized (this.mDeathHandlers) {
            RemoteDeathHandler remoteDeathHandler = this.mDeathHandlers.get(iBinder);
            if (remoteDeathHandler == null) {
                remoteDeathHandler = new RemoteDeathHandler(iBinder);
                try {
                    iBinder.linkToDeath(remoteDeathHandler, 0);
                    this.mDeathHandlers.put(iBinder, remoteDeathHandler);
                } catch (RemoteException unused) {
                    Slog.e("RemoteTransitionHandler", "Failed to link to death");
                    return;
                }
            }
            if (transitions$$ExternalSyntheticLambda1 != null) {
                remoteDeathHandler.mPendingFinishCallbacks.add(transitions$$ExternalSyntheticLambda1);
            }
            remoteDeathHandler.mUsers++;
        }
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public final void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, Transitions$$ExternalSyntheticLambda0 transitions$$ExternalSyntheticLambda0) {
        IRemoteTransition remoteTransition = this.mRequestedRemotes.get(iBinder2).getRemoteTransition();
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -114556030, 0, " Attempt merge %s into %s", String.valueOf(iBinder), String.valueOf(remoteTransition));
        }
        if (remoteTransition != null) {
            try {
                remoteTransition.mergeAnimation(iBinder, transitionInfo, transaction, iBinder2, new AnonymousClass2(iBinder2, transitions$$ExternalSyntheticLambda0));
            } catch (RemoteException e) {
                Log.e("ShellTransitions", "Error attempting to merge remote transition.", e);
            }
        }
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public final void onTransitionMerged(IBinder iBinder) {
        this.mRequestedRemotes.remove(iBinder);
    }

    public final void unhandleDeath(IBinder iBinder, Transitions.TransitionFinishCallback transitionFinishCallback) {
        synchronized (this.mDeathHandlers) {
            RemoteDeathHandler remoteDeathHandler = this.mDeathHandlers.get(iBinder);
            if (remoteDeathHandler != null) {
                if (transitionFinishCallback != null) {
                    remoteDeathHandler.mPendingFinishCallbacks.remove(transitionFinishCallback);
                }
                int i = remoteDeathHandler.mUsers - 1;
                remoteDeathHandler.mUsers = i;
                if (i == 0) {
                    if (remoteDeathHandler.mPendingFinishCallbacks.isEmpty()) {
                        iBinder.unlinkToDeath(remoteDeathHandler, 0);
                        this.mDeathHandlers.remove(iBinder);
                    } else {
                        throw new IllegalStateException("Unhandling death for binder that still has pending finishCallback(s).");
                    }
                }
            }
        }
    }

    public RemoteTransitionHandler(ShellExecutor shellExecutor) {
        this.mMainExecutor = shellExecutor;
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public final WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        RemoteTransition remoteTransition = transitionRequestInfo.getRemoteTransition();
        if (remoteTransition == null) {
            return null;
        }
        this.mRequestedRemotes.put(iBinder, remoteTransition);
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 214412327, 0, "RemoteTransition directly requested for %s: %s", String.valueOf(iBinder), String.valueOf(remoteTransition));
        }
        return new WindowContainerTransaction();
    }
}
