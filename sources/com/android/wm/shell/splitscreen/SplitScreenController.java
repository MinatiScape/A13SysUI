package com.android.wm.shell.splitscreen;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Slog;
import android.view.SurfaceControl;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda2;
import com.android.launcher3.icons.IconProvider;
import com.android.systemui.plugins.FalsingManager;
import com.android.wm.shell.RootTaskDisplayAreaOrganizer;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.RemoteCallable;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SingleInstanceRemoteListener;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.common.split.SplitLayout;
import com.android.wm.shell.draganddrop.DragAndDropPolicy;
import com.android.wm.shell.pip.PipMediaController$$ExternalSyntheticLambda1;
import com.android.wm.shell.pip.PipMediaController$$ExternalSyntheticLambda2;
import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.splitscreen.ISplitScreen;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.transition.LegacyTransitions$ILegacyTransition;
import com.android.wm.shell.transition.Transitions;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SplitScreenController implements DragAndDropPolicy.Starter, RemoteCallable<SplitScreenController> {
    public final Context mContext;
    public final DisplayController mDisplayController;
    public final DisplayImeController mDisplayImeController;
    public final DisplayInsetsController mDisplayInsetsController;
    public final IconProvider mIconProvider;
    public final SplitScreenImpl mImpl = new SplitScreenImpl();
    public final SplitscreenEventLogger mLogger = new SplitscreenEventLogger();
    public final ShellExecutor mMainExecutor;
    public final Optional<RecentTasksController> mRecentTasksOptional;
    public final RootTaskDisplayAreaOrganizer mRootTDAOrganizer;
    public SurfaceControl mSplitTasksContainerLayer;
    public StageCoordinator mStageCoordinator;
    public final SyncTransactionQueue mSyncQueue;
    public final ShellTaskOrganizer mTaskOrganizer;
    public final TransactionPool mTransactionPool;
    public final Transitions mTransitions;
    public final Provider<Optional<StageTaskUnfoldController>> mUnfoldControllerProvider;

    /* renamed from: com.android.wm.shell.splitscreen.SplitScreenController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass1 implements LegacyTransitions$ILegacyTransition {
        public final /* synthetic */ WindowContainerTransaction val$evictWct;

        public AnonymousClass1(WindowContainerTransaction windowContainerTransaction) {
            this.val$evictWct = windowContainerTransaction;
        }
    }

    /* loaded from: classes.dex */
    public class SplitScreenImpl implements SplitScreen {
        public final ArrayMap<SplitScreen.SplitScreenListener, Executor> mExecutors = new ArrayMap<>();
        public ISplitScreenImpl mISplitScreen;

        public SplitScreenImpl() {
        }

        @Override // com.android.wm.shell.splitscreen.SplitScreen
        public final ISplitScreen createExternalInterface() {
            ISplitScreenImpl iSplitScreenImpl = this.mISplitScreen;
            if (iSplitScreenImpl != null) {
                Objects.requireNonNull(iSplitScreenImpl);
                iSplitScreenImpl.mController = null;
            }
            ISplitScreenImpl iSplitScreenImpl2 = new ISplitScreenImpl(SplitScreenController.this);
            this.mISplitScreen = iSplitScreenImpl2;
            return iSplitScreenImpl2;
        }

        @Override // com.android.wm.shell.splitscreen.SplitScreen
        public final void onFinishedWakingUp() {
            SplitScreenController.this.mMainExecutor.execute(new LockIconViewController$$ExternalSyntheticLambda2(this, 10));
        }

        @Override // com.android.wm.shell.splitscreen.SplitScreen
        public final void onKeyguardVisibilityChanged(final boolean z) {
            SplitScreenController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.splitscreen.SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    int i;
                    StageTaskListener stageTaskListener;
                    SplitScreenController.SplitScreenImpl splitScreenImpl = SplitScreenController.SplitScreenImpl.this;
                    boolean z2 = z;
                    Objects.requireNonNull(splitScreenImpl);
                    SplitScreenController splitScreenController = SplitScreenController.this;
                    Objects.requireNonNull(splitScreenController);
                    StageCoordinator stageCoordinator = splitScreenController.mStageCoordinator;
                    Objects.requireNonNull(stageCoordinator);
                    MainStage mainStage = stageCoordinator.mMainStage;
                    Objects.requireNonNull(mainStage);
                    if (mainStage.mIsActive) {
                        boolean z3 = Transitions.ENABLE_SHELL_TRANSITIONS;
                        if (z3) {
                            stageCoordinator.setDividerVisibility(!z2, null);
                        }
                        if (!z2 && (i = stageCoordinator.mTopStageAfterFoldDismiss) != -1) {
                            if (z3) {
                                WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                                stageCoordinator.prepareExitSplitScreen(stageCoordinator.mTopStageAfterFoldDismiss, windowContainerTransaction);
                                stageCoordinator.mSplitTransitions.startDismissTransition(null, windowContainerTransaction, stageCoordinator, stageCoordinator.mTopStageAfterFoldDismiss, 3);
                                return;
                            }
                            if (i == 0) {
                                stageTaskListener = stageCoordinator.mMainStage;
                            } else {
                                stageTaskListener = stageCoordinator.mSideStage;
                            }
                            stageCoordinator.exitSplitScreen(stageTaskListener, 3);
                        }
                    }
                }
            });
        }
    }

    /* loaded from: classes.dex */
    public static class ISplitScreenImpl extends ISplitScreen.Stub {
        public static final /* synthetic */ int $r8$clinit = 0;
        public SplitScreenController mController;
        public final SingleInstanceRemoteListener<SplitScreenController, ISplitScreenListener> mListener;
        public final AnonymousClass1 mSplitScreenListener = new AnonymousClass1();

        /* renamed from: com.android.wm.shell.splitscreen.SplitScreenController$ISplitScreenImpl$1  reason: invalid class name */
        /* loaded from: classes.dex */
        public class AnonymousClass1 implements SplitScreen.SplitScreenListener {
            public AnonymousClass1() {
            }

            @Override // com.android.wm.shell.splitscreen.SplitScreen.SplitScreenListener
            public final void onStagePositionChanged(int i, int i2) {
                SingleInstanceRemoteListener<SplitScreenController, ISplitScreenListener> singleInstanceRemoteListener = ISplitScreenImpl.this.mListener;
                Objects.requireNonNull(singleInstanceRemoteListener);
                ISplitScreenListener iSplitScreenListener = singleInstanceRemoteListener.mListener;
                if (iSplitScreenListener == null) {
                    Slog.e("SingleInstanceRemoteListener", "Failed remote call on null listener");
                    return;
                }
                try {
                    iSplitScreenListener.onStagePositionChanged(i, i2);
                } catch (RemoteException e) {
                    Slog.e("SingleInstanceRemoteListener", "Failed remote call", e);
                }
            }

            @Override // com.android.wm.shell.splitscreen.SplitScreen.SplitScreenListener
            public final void onTaskStageChanged(int i, int i2, boolean z) {
                SingleInstanceRemoteListener<SplitScreenController, ISplitScreenListener> singleInstanceRemoteListener = ISplitScreenImpl.this.mListener;
                Objects.requireNonNull(singleInstanceRemoteListener);
                ISplitScreenListener iSplitScreenListener = singleInstanceRemoteListener.mListener;
                if (iSplitScreenListener == null) {
                    Slog.e("SingleInstanceRemoteListener", "Failed remote call on null listener");
                    return;
                }
                try {
                    iSplitScreenListener.onTaskStageChanged(i, i2, z);
                } catch (RemoteException e) {
                    Slog.e("SingleInstanceRemoteListener", "Failed remote call", e);
                }
            }
        }

        public ISplitScreenImpl(SplitScreenController splitScreenController) {
            this.mController = splitScreenController;
            this.mListener = new SingleInstanceRemoteListener<>(splitScreenController, new PipMediaController$$ExternalSyntheticLambda2(this, 1), new PipMediaController$$ExternalSyntheticLambda1(this, 3));
        }
    }

    public final void getStageBounds(Rect rect, Rect rect2) {
        StageCoordinator stageCoordinator = this.mStageCoordinator;
        Objects.requireNonNull(stageCoordinator);
        SplitLayout splitLayout = stageCoordinator.mSplitLayout;
        Objects.requireNonNull(splitLayout);
        rect.set(new Rect(splitLayout.mBounds1));
        SplitLayout splitLayout2 = stageCoordinator.mSplitLayout;
        Objects.requireNonNull(splitLayout2);
        rect2.set(new Rect(splitLayout2.mBounds2));
    }

    public final boolean isSplitScreenVisible() {
        StageCoordinator stageCoordinator = this.mStageCoordinator;
        Objects.requireNonNull(stageCoordinator);
        if (!stageCoordinator.mSideStageListener.mVisible || !stageCoordinator.mMainStageListener.mVisible) {
            return false;
        }
        return true;
    }

    public final void moveToStage(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
        StageTaskListener stageTaskListener;
        StageTaskListener stageTaskListener2;
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskOrganizer.getRunningTaskInfo(i);
        if (runningTaskInfo != null) {
            StageCoordinator stageCoordinator = this.mStageCoordinator;
            Objects.requireNonNull(stageCoordinator);
            if (i2 == 0) {
                stageTaskListener = stageCoordinator.mMainStage;
                if (i3 == 0) {
                    i3 = 1;
                } else if (i3 != 1) {
                    i3 = -1;
                } else {
                    i3 = 0;
                }
            } else if (i2 == 1) {
                stageTaskListener = stageCoordinator.mSideStage;
            } else {
                MainStage mainStage = stageCoordinator.mMainStage;
                Objects.requireNonNull(mainStage);
                if (mainStage.mIsActive) {
                    int i4 = stageCoordinator.mSideStagePosition;
                    if (i3 == i4) {
                        stageTaskListener2 = stageCoordinator.mSideStage;
                    } else {
                        stageTaskListener2 = stageCoordinator.mMainStage;
                    }
                    i3 = i4;
                    stageTaskListener = stageTaskListener2;
                } else {
                    stageTaskListener = stageCoordinator.mSideStage;
                }
            }
            stageCoordinator.setSideStagePosition(i3, windowContainerTransaction);
            WindowContainerTransaction windowContainerTransaction2 = new WindowContainerTransaction();
            stageTaskListener.evictAllChildren(windowContainerTransaction2);
            windowContainerTransaction.setWindowingMode(runningTaskInfo.token, 0).setBounds(runningTaskInfo.token, (Rect) null);
            windowContainerTransaction.reparent(runningTaskInfo.token, stageTaskListener.mRootTaskInfo.token, true);
            if (!windowContainerTransaction2.isEmpty()) {
                windowContainerTransaction.merge(windowContainerTransaction2, true);
            }
            if (Transitions.ENABLE_SHELL_TRANSITIONS) {
                stageCoordinator.prepareEnterSplitScreen(windowContainerTransaction, null, -1);
                stageCoordinator.mSplitTransitions.startEnterTransition(17, windowContainerTransaction, null, stageCoordinator);
                return;
            }
            stageCoordinator.mTaskOrganizer.applyTransaction(windowContainerTransaction);
            return;
        }
        throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("Unknown taskId", i));
    }

    public final boolean removeFromSideStage(int i) {
        WindowContainerToken windowContainerToken;
        StageCoordinator stageCoordinator = this.mStageCoordinator;
        Objects.requireNonNull(stageCoordinator);
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        SideStage sideStage = stageCoordinator.mSideStage;
        MainStage mainStage = stageCoordinator.mMainStage;
        Objects.requireNonNull(mainStage);
        if (mainStage.mIsActive) {
            windowContainerToken = stageCoordinator.mMainStage.mRootTaskInfo.token;
        } else {
            windowContainerToken = null;
        }
        Objects.requireNonNull(sideStage);
        ActivityManager.RunningTaskInfo runningTaskInfo = sideStage.mChildrenTaskInfo.get(i);
        boolean z = false;
        if (runningTaskInfo != null) {
            windowContainerTransaction.reparent(runningTaskInfo.token, windowContainerToken, false);
            z = true;
        }
        stageCoordinator.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        return z;
    }

    public final void setSideStageVisibility(boolean z) {
        StageCoordinator stageCoordinator = this.mStageCoordinator;
        Objects.requireNonNull(stageCoordinator);
        if (stageCoordinator.mSideStageListener.mVisible != z) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            SideStage sideStage = stageCoordinator.mSideStage;
            Objects.requireNonNull(sideStage);
            windowContainerTransaction.reorder(sideStage.mRootTaskInfo.token, z);
            stageCoordinator.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        }
    }

    @Override // com.android.wm.shell.draganddrop.DragAndDropPolicy.Starter
    public final void startIntent(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle) {
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            this.mStageCoordinator.prepareEvictChildTasks(i, windowContainerTransaction);
            AnonymousClass1 r2 = new AnonymousClass1(windowContainerTransaction);
            WindowContainerTransaction windowContainerTransaction2 = new WindowContainerTransaction();
            windowContainerTransaction2.sendPendingIntent(pendingIntent, intent, this.mStageCoordinator.resolveStartStage(-1, i, bundle, windowContainerTransaction2));
            SyncTransactionQueue syncTransactionQueue = this.mSyncQueue;
            Objects.requireNonNull(syncTransactionQueue);
            if (!windowContainerTransaction2.isEmpty()) {
                SyncTransactionQueue.SyncCallback syncCallback = new SyncTransactionQueue.SyncCallback(r2, windowContainerTransaction2);
                synchronized (syncTransactionQueue.mQueue) {
                    syncTransactionQueue.mQueue.add(syncCallback);
                    if (syncTransactionQueue.mQueue.size() == 1) {
                        syncCallback.send();
                    }
                }
                return;
            }
            return;
        }
        try {
            pendingIntent.send(this.mContext, 0, intent, null, null, null, this.mStageCoordinator.resolveStartStage(-1, i, bundle, null));
        } catch (PendingIntent.CanceledException e) {
            Slog.e("SplitScreenController", "Failed to launch task", e);
        }
    }

    @Override // com.android.wm.shell.draganddrop.DragAndDropPolicy.Starter
    public final void startShortcut(String str, String str2, int i, Bundle bundle, UserHandle userHandle) {
        Bundle resolveStartStage = this.mStageCoordinator.resolveStartStage(-1, i, bundle, null);
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        this.mStageCoordinator.prepareEvictChildTasks(i, windowContainerTransaction);
        try {
            ((LauncherApps) this.mContext.getSystemService(LauncherApps.class)).startShortcut(str, str2, null, resolveStartStage, userHandle);
            this.mSyncQueue.queue(windowContainerTransaction);
        } catch (ActivityNotFoundException e) {
            Slog.e("SplitScreenController", "Failed to launch shortcut", e);
        }
    }

    @Override // com.android.wm.shell.draganddrop.DragAndDropPolicy.Starter
    public final void startTask(int i, int i2, Bundle bundle) {
        Bundle resolveStartStage = this.mStageCoordinator.resolveStartStage(-1, i2, bundle, null);
        try {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            this.mStageCoordinator.prepareEvictChildTasks(i2, windowContainerTransaction);
            int startActivityFromRecents = ActivityTaskManager.getService().startActivityFromRecents(i, resolveStartStage);
            if (startActivityFromRecents == 0 || startActivityFromRecents == 2) {
                this.mSyncQueue.queue(windowContainerTransaction);
            }
        } catch (RemoteException e) {
            Slog.e("SplitScreenController", "Failed to launch task", e);
        }
    }

    public SplitScreenController(ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, Context context, RootTaskDisplayAreaOrganizer rootTaskDisplayAreaOrganizer, ShellExecutor shellExecutor, DisplayController displayController, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController, Transitions transitions, TransactionPool transactionPool, IconProvider iconProvider, Optional<RecentTasksController> optional, Provider<Optional<StageTaskUnfoldController>> provider) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mSyncQueue = syncTransactionQueue;
        this.mContext = context;
        this.mRootTDAOrganizer = rootTaskDisplayAreaOrganizer;
        this.mMainExecutor = shellExecutor;
        this.mDisplayController = displayController;
        this.mDisplayImeController = displayImeController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mTransitions = transitions;
        this.mTransactionPool = transactionPool;
        this.mUnfoldControllerProvider = provider;
        this.mIconProvider = iconProvider;
        this.mRecentTasksOptional = optional;
    }

    public static String exitReasonToString(int i) {
        switch (i) {
            case 0:
                return "UNKNOWN_EXIT";
            case 1:
                return "APP_DOES_NOT_SUPPORT_MULTIWINDOW";
            case 2:
                return "APP_FINISHED";
            case 3:
                return "DEVICE_FOLDED";
            case 4:
                return "DRAG_DIVIDER";
            case 5:
                return "RETURN_HOME";
            case FalsingManager.VERSION /* 6 */:
                return "ROOT_TASK_VANISHED";
            case 7:
                return "SCREEN_LOCKED";
            case 8:
                return "SCREEN_LOCKED_SHOW_ON_TOP";
            case 9:
                return "CHILD_TASK_ENTER_PIP";
            default:
                return VendorAtomValue$$ExternalSyntheticOutline0.m("unknown reason, reason int = ", i);
        }
    }

    public final ActivityManager.RunningTaskInfo getTaskInfo(int i) {
        int i2;
        if (!isSplitScreenVisible()) {
            return null;
        }
        StageCoordinator stageCoordinator = this.mStageCoordinator;
        Objects.requireNonNull(stageCoordinator);
        if (stageCoordinator.mSideStagePosition == i) {
            i2 = stageCoordinator.mSideStage.getTopVisibleChildTaskId();
        } else {
            i2 = stageCoordinator.mMainStage.getTopVisibleChildTaskId();
        }
        return this.mTaskOrganizer.getRunningTaskInfo(i2);
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public final Context getContext() {
        return this.mContext;
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public final ShellExecutor getRemoteCallExecutor() {
        return this.mMainExecutor;
    }
}
