package com.android.wm.shell.splitscreen;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.view.WindowManager;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.launcher3.icons.IconProvider;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.apppairs.AppPair$$ExternalSyntheticLambda0;
import com.android.wm.shell.apppairs.AppPair$$ExternalSyntheticLambda1;
import com.android.wm.shell.apppairs.AppPair$$ExternalSyntheticLambda2;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.split.SplitDecorManager;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.splitscreen.StageCoordinator;
import com.android.wm.shell.splitscreen.StageTaskUnfoldController;
import com.android.wm.shell.transition.Transitions;
import java.io.PrintWriter;
import java.util.Objects;
/* loaded from: classes.dex */
public class StageTaskListener implements ShellTaskOrganizer.TaskListener {
    public static final int[] CONTROLLED_ACTIVITY_TYPES = {1};
    public static final int[] CONTROLLED_WINDOWING_MODES = {1, 0};
    public static final int[] CONTROLLED_WINDOWING_MODES_WHEN_ACTIVE = {1, 0, 6};
    public final StageListenerCallbacks mCallbacks;
    public final Context mContext;
    public SurfaceControl mDimLayer;
    public final IconProvider mIconProvider;
    public SurfaceControl mRootLeash;
    public ActivityManager.RunningTaskInfo mRootTaskInfo;
    public SplitDecorManager mSplitDecorManager;
    public final StageTaskUnfoldController mStageTaskUnfoldController;
    public final SurfaceSession mSurfaceSession;
    public final SyncTransactionQueue mSyncQueue;
    public SparseArray<ActivityManager.RunningTaskInfo> mChildrenTaskInfo = new SparseArray<>();
    public final SparseArray<SurfaceControl> mChildrenLeashes = new SparseArray<>();

    /* loaded from: classes.dex */
    public interface StageListenerCallbacks {
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
        if (this.mRootTaskInfo.taskId == i) {
            builder.setParent(this.mRootLeash);
        } else if (this.mChildrenLeashes.contains(i)) {
            builder.setParent(this.mChildrenLeashes.get(i));
        } else {
            throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("There is no surface for taskId=", i));
        }
    }

    public final boolean containsTask(int i) {
        return this.mChildrenTaskInfo.contains(i);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + this);
    }

    public final void evictAllChildren(WindowContainerTransaction windowContainerTransaction) {
        int size = this.mChildrenTaskInfo.size();
        while (true) {
            size--;
            if (size >= 0) {
                windowContainerTransaction.reparent(this.mChildrenTaskInfo.valueAt(size).token, (WindowContainerToken) null, false);
            } else {
                return;
            }
        }
    }

    public final int getChildCount() {
        return this.mChildrenTaskInfo.size();
    }

    public final int getTopChildTaskUid() {
        for (int size = this.mChildrenTaskInfo.size() - 1; size >= 0; size--) {
            ActivityInfo activityInfo = this.mChildrenTaskInfo.valueAt(size).topActivityInfo;
            if (activityInfo != null) {
                return activityInfo.applicationInfo.uid;
            }
        }
        return 0;
    }

    public final int getTopVisibleChildTaskId() {
        for (int size = this.mChildrenTaskInfo.size() - 1; size >= 0; size--) {
            ActivityManager.RunningTaskInfo valueAt = this.mChildrenTaskInfo.valueAt(size);
            if (valueAt.isVisible) {
                return valueAt.taskId;
            }
        }
        return -1;
    }

    public final boolean isFocused() {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mRootTaskInfo;
        if (runningTaskInfo == null) {
            return false;
        }
        if (runningTaskInfo.isFocused) {
            return true;
        }
        for (int size = this.mChildrenTaskInfo.size() - 1; size >= 0; size--) {
            if (this.mChildrenTaskInfo.valueAt(size).isFocused) {
                return true;
            }
        }
        return false;
    }

    public final void onResized(SurfaceControl.Transaction transaction) {
        SplitDecorManager splitDecorManager = this.mSplitDecorManager;
        if (splitDecorManager != null && splitDecorManager.mResizingIconView != null) {
            SurfaceControl surfaceControl = splitDecorManager.mBackgroundLeash;
            if (surfaceControl != null) {
                transaction.remove(surfaceControl);
                splitDecorManager.mBackgroundLeash = null;
            }
            if (splitDecorManager.mIcon != null) {
                splitDecorManager.mResizingIconView.setVisibility(8);
                splitDecorManager.mResizingIconView.setImageDrawable(null);
                transaction.hide(splitDecorManager.mIconLeash);
                splitDecorManager.mIcon = null;
            }
        }
    }

    public final void onResizing(Rect rect, SurfaceControl.Transaction transaction) {
        ActivityManager.RunningTaskInfo runningTaskInfo;
        ActivityInfo activityInfo;
        SplitDecorManager splitDecorManager = this.mSplitDecorManager;
        if (splitDecorManager != null && (runningTaskInfo = this.mRootTaskInfo) != null && splitDecorManager.mResizingIconView != null) {
            if (splitDecorManager.mBackgroundLeash == null) {
                SurfaceControl build = new SurfaceControl.Builder(splitDecorManager.mSurfaceSession).setParent(splitDecorManager.mHostLeash).setColorLayer().setName("ResizingBackground").setCallsite("SurfaceUtils.makeColorLayer").build();
                splitDecorManager.mBackgroundLeash = build;
                int backgroundColor = runningTaskInfo.taskDescription.getBackgroundColor();
                if (backgroundColor == -1) {
                    backgroundColor = -1;
                }
                transaction.setColor(build, Color.valueOf(backgroundColor).getComponents()).setLayer(splitDecorManager.mBackgroundLeash, 29999).show(splitDecorManager.mBackgroundLeash);
            }
            if (splitDecorManager.mIcon == null && (activityInfo = runningTaskInfo.topActivityInfo) != null) {
                Drawable icon = splitDecorManager.mIconProvider.getIcon(activityInfo);
                splitDecorManager.mIcon = icon;
                splitDecorManager.mResizingIconView.setImageDrawable(icon);
                splitDecorManager.mResizingIconView.setVisibility(0);
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) splitDecorManager.mViewHost.getView().getLayoutParams();
                layoutParams.width = splitDecorManager.mIcon.getIntrinsicWidth();
                layoutParams.height = splitDecorManager.mIcon.getIntrinsicHeight();
                splitDecorManager.mViewHost.relayout(layoutParams);
                transaction.show(splitDecorManager.mIconLeash).setLayer(splitDecorManager.mIconLeash, 30000);
            }
            transaction.setPosition(splitDecorManager.mIconLeash, (rect.width() / 2) - (splitDecorManager.mIcon.getIntrinsicWidth() / 2), (rect.height() / 2) - (splitDecorManager.mIcon.getIntrinsicWidth() / 2));
        }
    }

    public final void onSplitScreenListenerRegistered(SplitScreenController.ISplitScreenImpl.AnonymousClass1 r4, int i) {
        int size = this.mChildrenTaskInfo.size();
        while (true) {
            size--;
            if (size >= 0) {
                int keyAt = this.mChildrenTaskInfo.keyAt(size);
                r4.onTaskStageChanged(keyAt, i, this.mChildrenTaskInfo.get(keyAt).isVisible);
            } else {
                return;
            }
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        if (this.mRootTaskInfo == null && !runningTaskInfo.hasParentTask()) {
            this.mRootLeash = surfaceControl;
            this.mRootTaskInfo = runningTaskInfo;
            this.mSplitDecorManager = new SplitDecorManager(this.mRootTaskInfo.configuration, this.mIconProvider, this.mSurfaceSession);
            StageCoordinator.StageListenerImpl stageListenerImpl = (StageCoordinator.StageListenerImpl) this.mCallbacks;
            Objects.requireNonNull(stageListenerImpl);
            stageListenerImpl.mHasRootTask = true;
            StageCoordinator stageCoordinator = StageCoordinator.this;
            Objects.requireNonNull(stageCoordinator);
            if (stageCoordinator.mMainStageListener.mHasRootTask && stageCoordinator.mSideStageListener.mHasRootTask) {
                WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                windowContainerTransaction.setAdjacentRoots(stageCoordinator.mMainStage.mRootTaskInfo.token, stageCoordinator.mSideStage.mRootTaskInfo.token, true);
                windowContainerTransaction.setLaunchAdjacentFlagRoot(stageCoordinator.mSideStage.mRootTaskInfo.token);
                stageCoordinator.mTaskOrganizer.applyTransaction(windowContainerTransaction);
            }
            sendStatusChanged();
            this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda1(this, 2));
        } else if (runningTaskInfo.parentTaskId == this.mRootTaskInfo.taskId) {
            int i = runningTaskInfo.taskId;
            this.mChildrenLeashes.put(i, surfaceControl);
            this.mChildrenTaskInfo.put(i, runningTaskInfo);
            this.mSyncQueue.runInSync(new StageTaskListener$$ExternalSyntheticLambda0(surfaceControl, runningTaskInfo.positionInParent, true));
            ((StageCoordinator.StageListenerImpl) this.mCallbacks).onChildTaskStatusChanged(i, true, runningTaskInfo.isVisible);
            if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                sendStatusChanged();
            } else {
                return;
            }
        } else {
            throw new IllegalArgumentException(this + "\n Unknown task: " + runningTaskInfo + "\n mRootTaskInfo: " + this.mRootTaskInfo);
        }
        StageTaskUnfoldController stageTaskUnfoldController = this.mStageTaskUnfoldController;
        if (stageTaskUnfoldController != null && runningTaskInfo.hasParentTask()) {
            stageTaskUnfoldController.mAnimationContextByTaskId.put(runningTaskInfo.taskId, new StageTaskUnfoldController.AnimationContext(surfaceControl));
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (!runningTaskInfo.supportsMultiWindow) {
            StageCoordinator.StageListenerImpl stageListenerImpl = (StageCoordinator.StageListenerImpl) this.mCallbacks;
            Objects.requireNonNull(stageListenerImpl);
            MainStage mainStage = StageCoordinator.this.mMainStage;
            Objects.requireNonNull(mainStage);
            if (mainStage.mIsActive) {
                if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                    StageCoordinator.this.exitSplitScreen(null, 1);
                }
                WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                StageCoordinator.this.prepareExitSplitScreen(-1, windowContainerTransaction);
                StageCoordinator stageCoordinator = StageCoordinator.this;
                stageCoordinator.mSplitTransitions.startDismissTransition(null, windowContainerTransaction, stageCoordinator, -1, 1);
                return;
            }
            return;
        }
        ActivityManager.RunningTaskInfo runningTaskInfo2 = this.mRootTaskInfo;
        int i = runningTaskInfo2.taskId;
        int i2 = runningTaskInfo.taskId;
        if (i == i2) {
            if (runningTaskInfo2.isVisible != runningTaskInfo.isVisible) {
                this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda2(this, runningTaskInfo, 1));
            }
            this.mRootTaskInfo = runningTaskInfo;
        } else if (runningTaskInfo.parentTaskId == i) {
            this.mChildrenTaskInfo.put(i2, runningTaskInfo);
            ((StageCoordinator.StageListenerImpl) this.mCallbacks).onChildTaskStatusChanged(runningTaskInfo.taskId, true, runningTaskInfo.isVisible);
            if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                this.mSyncQueue.runInSync(new StageTaskListener$$ExternalSyntheticLambda0(this.mChildrenLeashes.get(runningTaskInfo.taskId), runningTaskInfo.positionInParent, false));
            }
        } else {
            throw new IllegalArgumentException(this + "\n Unknown task: " + runningTaskInfo + "\n mRootTaskInfo: " + this.mRootTaskInfo);
        }
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            sendStatusChanged();
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        StageTaskListener stageTaskListener;
        int i = runningTaskInfo.taskId;
        if (this.mRootTaskInfo.taskId == i) {
            StageCoordinator.StageListenerImpl stageListenerImpl = (StageCoordinator.StageListenerImpl) this.mCallbacks;
            Objects.requireNonNull(stageListenerImpl);
            stageListenerImpl.mHasRootTask = false;
            stageListenerImpl.mVisible = false;
            stageListenerImpl.mHasChildren = false;
            StageCoordinator stageCoordinator = StageCoordinator.this;
            Objects.requireNonNull(stageCoordinator);
            if (stageListenerImpl == stageCoordinator.mMainStageListener || stageListenerImpl == stageCoordinator.mSideStageListener) {
                WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                windowContainerTransaction.clearLaunchAdjacentFlagRoot(stageCoordinator.mSideStage.mRootTaskInfo.token);
                MainStage mainStage = stageCoordinator.mMainStage;
                Objects.requireNonNull(mainStage);
                mainStage.deactivate(windowContainerTransaction, false);
                stageCoordinator.mTaskOrganizer.applyTransaction(windowContainerTransaction);
            }
            this.mRootTaskInfo = null;
            this.mSyncQueue.runInSync(new AppPair$$ExternalSyntheticLambda0(this, 1));
        } else if (this.mChildrenTaskInfo.contains(i)) {
            this.mChildrenTaskInfo.remove(i);
            this.mChildrenLeashes.remove(i);
            ((StageCoordinator.StageListenerImpl) this.mCallbacks).onChildTaskStatusChanged(i, false, runningTaskInfo.isVisible);
            if (runningTaskInfo.getWindowingMode() == 2) {
                StageCoordinator.StageListenerImpl stageListenerImpl2 = (StageCoordinator.StageListenerImpl) this.mCallbacks;
                Objects.requireNonNull(stageListenerImpl2);
                StageCoordinator stageCoordinator2 = StageCoordinator.this;
                Objects.requireNonNull(stageCoordinator2);
                if (stageListenerImpl2 == stageCoordinator2.mMainStageListener) {
                    stageTaskListener = stageCoordinator2.mMainStage;
                } else {
                    stageTaskListener = stageCoordinator2.mSideStage;
                }
                stageCoordinator2.exitSplitScreen(stageTaskListener, 9);
            }
            if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                sendStatusChanged();
            } else {
                return;
            }
        } else {
            throw new IllegalArgumentException(this + "\n Unknown task: " + runningTaskInfo + "\n mRootTaskInfo: " + this.mRootTaskInfo);
        }
        StageTaskUnfoldController stageTaskUnfoldController = this.mStageTaskUnfoldController;
        if (stageTaskUnfoldController != null && runningTaskInfo.hasParentTask()) {
            StageTaskUnfoldController.AnimationContext animationContext = stageTaskUnfoldController.mAnimationContextByTaskId.get(runningTaskInfo.taskId);
            if (animationContext != null) {
                SurfaceControl.Transaction acquire = stageTaskUnfoldController.mTransactionPool.acquire();
                acquire.setWindowCrop(animationContext.mLeash, null).setCornerRadius(animationContext.mLeash, 0.0f);
                acquire.apply();
                stageTaskUnfoldController.mTransactionPool.release(acquire);
            }
            stageTaskUnfoldController.mAnimationContextByTaskId.remove(runningTaskInfo.taskId);
        }
    }

    public final void sendStatusChanged() {
        boolean z;
        boolean z2;
        StageListenerCallbacks stageListenerCallbacks = this.mCallbacks;
        boolean z3 = this.mRootTaskInfo.isVisible;
        boolean z4 = false;
        if (this.mChildrenTaskInfo.size() > 0) {
            z = true;
        } else {
            z = false;
        }
        StageCoordinator.StageListenerImpl stageListenerImpl = (StageCoordinator.StageListenerImpl) stageListenerCallbacks;
        Objects.requireNonNull(stageListenerImpl);
        if (stageListenerImpl.mHasRootTask) {
            if (stageListenerImpl.mHasChildren != z) {
                stageListenerImpl.mHasChildren = z;
                final StageCoordinator stageCoordinator = StageCoordinator.this;
                Objects.requireNonNull(stageCoordinator);
                boolean z5 = stageListenerImpl.mHasChildren;
                StageCoordinator.StageListenerImpl stageListenerImpl2 = stageCoordinator.mSideStageListener;
                if (stageListenerImpl == stageListenerImpl2) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (!z5) {
                    if (z2 && stageCoordinator.mMainStageListener.mVisible) {
                        stageCoordinator.exitSplitScreen(stageCoordinator.mMainStage, 2);
                    } else if (!z2 && stageListenerImpl2.mVisible) {
                        stageCoordinator.exitSplitScreen(stageCoordinator.mSideStage, 2);
                    }
                } else if (z2) {
                    WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                    stageCoordinator.mSplitLayout.init();
                    stageCoordinator.prepareEnterSplitScreen(windowContainerTransaction, null, -1);
                    stageCoordinator.mSyncQueue.queue(windowContainerTransaction);
                    stageCoordinator.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.splitscreen.StageCoordinator$$ExternalSyntheticLambda3
                        @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                        public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                            StageCoordinator stageCoordinator2 = StageCoordinator.this;
                            Objects.requireNonNull(stageCoordinator2);
                            stageCoordinator2.updateSurfaceBounds(stageCoordinator2.mSplitLayout, transaction);
                            stageCoordinator2.setDividerVisibility(true, transaction);
                        }
                    });
                }
                if (stageCoordinator.mMainStageListener.mHasChildren && stageCoordinator.mSideStageListener.mHasChildren) {
                    stageCoordinator.mShouldUpdateRecents = true;
                    stageCoordinator.updateRecentTasksSplitPair();
                    SplitscreenEventLogger splitscreenEventLogger = stageCoordinator.mLogger;
                    Objects.requireNonNull(splitscreenEventLogger);
                    if (splitscreenEventLogger.mLoggerSessionId != null) {
                        z4 = true;
                    }
                    if (!z4) {
                        stageCoordinator.mLogger.logEnter(stageCoordinator.mSplitLayout.getDividerPositionAsFraction(), stageCoordinator.getMainStagePosition(), stageCoordinator.mMainStage.getTopChildTaskUid(), stageCoordinator.mSideStagePosition, stageCoordinator.mSideStage.getTopChildTaskUid(), stageCoordinator.mSplitLayout.isLandscape());
                    }
                }
            }
            if (stageListenerImpl.mVisible != z3) {
                stageListenerImpl.mVisible = z3;
                final StageCoordinator stageCoordinator2 = StageCoordinator.this;
                Objects.requireNonNull(stageCoordinator2);
                final boolean z6 = stageCoordinator2.mSideStageListener.mVisible;
                final boolean z7 = stageCoordinator2.mMainStageListener.mVisible;
                if (z7 == z6) {
                    if (!z7 && (stageCoordinator2.mExitSplitScreenOnHide || (!stageCoordinator2.mMainStage.mRootTaskInfo.isSleeping && !stageCoordinator2.mSideStage.mRootTaskInfo.isSleeping))) {
                        stageCoordinator2.exitSplitScreen(null, 5);
                    }
                    stageCoordinator2.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.splitscreen.StageCoordinator$$ExternalSyntheticLambda8
                        @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                        public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                            StageCoordinator stageCoordinator3 = StageCoordinator.this;
                            boolean z8 = z6;
                            boolean z9 = z7;
                            Objects.requireNonNull(stageCoordinator3);
                            transaction.setVisibility(stageCoordinator3.mSideStage.mRootLeash, z8).setVisibility(stageCoordinator3.mMainStage.mRootLeash, z9);
                            stageCoordinator3.setDividerVisibility(z9, transaction);
                        }
                    });
                }
            }
        }
    }

    public StageTaskListener(Context context, ShellTaskOrganizer shellTaskOrganizer, StageCoordinator.StageListenerImpl stageListenerImpl, SyncTransactionQueue syncTransactionQueue, SurfaceSession surfaceSession, IconProvider iconProvider, StageTaskUnfoldController stageTaskUnfoldController) {
        this.mContext = context;
        this.mCallbacks = stageListenerImpl;
        this.mSyncQueue = syncTransactionQueue;
        this.mSurfaceSession = surfaceSession;
        this.mIconProvider = iconProvider;
        this.mStageTaskUnfoldController = stageTaskUnfoldController;
        if (!context.getResources().getBoolean(17891798)) {
            shellTaskOrganizer.createRootTask(6, this);
        }
    }
}
