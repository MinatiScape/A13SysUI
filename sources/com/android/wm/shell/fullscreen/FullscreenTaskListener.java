package com.android.wm.shell.fullscreen;

import android.app.ActivityManager;
import android.app.TaskInfo;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Point;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.SurfaceControl;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import com.android.systemui.recents.OverviewProxyService$$ExternalSyntheticLambda5;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.fullscreen.FullscreenTaskListener;
import com.android.wm.shell.fullscreen.FullscreenUnfoldController;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.transition.Transitions;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class FullscreenTaskListener implements ShellTaskOrganizer.TaskListener {
    public final FullscreenUnfoldController mFullscreenUnfoldController;
    public final Optional<RecentTasksController> mRecentTasksOptional;
    public final SyncTransactionQueue mSyncQueue;
    public final SparseArray<TaskData> mDataByTaskId = new SparseArray<>();
    public final AnimatableTasksListener mAnimatableTasksListener = new AnimatableTasksListener();

    /* loaded from: classes.dex */
    public class AnimatableTasksListener {
        public final SparseBooleanArray mTaskIds = new SparseBooleanArray();

        public AnimatableTasksListener() {
        }
    }

    /* loaded from: classes.dex */
    public static class TaskData {
        public final Point positionInParent;
        public final SurfaceControl surface;

        public TaskData(SurfaceControl surfaceControl, Point point) {
            this.surface = surfaceControl;
            this.positionInParent = point;
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
        if (this.mDataByTaskId.contains(i)) {
            builder.setParent(this.mDataByTaskId.get(i).surface);
            return;
        }
        throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("There is no surface for taskId=", i));
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void dump(PrintWriter printWriter, String str) {
        String m = SupportMenuInflater$$ExternalSyntheticOutline0.m(str, "  ");
        printWriter.println(str + this);
        printWriter.println(m + this.mDataByTaskId.size() + " Tasks");
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, final SurfaceControl surfaceControl) {
        if (this.mDataByTaskId.get(runningTaskInfo.taskId) == null) {
            boolean z = false;
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 111706912, 1, null, Long.valueOf(runningTaskInfo.taskId));
            }
            final Point point = runningTaskInfo.positionInParent;
            this.mDataByTaskId.put(runningTaskInfo.taskId, new TaskData(surfaceControl, point));
            if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.fullscreen.FullscreenTaskListener$$ExternalSyntheticLambda0
                    @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                        SurfaceControl surfaceControl2 = surfaceControl;
                        Point point2 = point;
                        transaction.setWindowCrop(surfaceControl2, null);
                        transaction.setPosition(surfaceControl2, point2.x, point2.y);
                        transaction.setAlpha(surfaceControl2, 1.0f);
                        transaction.setMatrix(surfaceControl2, 1.0f, 0.0f, 0.0f, 1.0f);
                        transaction.show(surfaceControl2);
                    }
                });
                AnimatableTasksListener animatableTasksListener = this.mAnimatableTasksListener;
                Objects.requireNonNull(animatableTasksListener);
                if (((TaskInfo) runningTaskInfo).isVisible && runningTaskInfo.getConfiguration().windowConfiguration.getActivityType() != 2) {
                    z = true;
                }
                if (z) {
                    animatableTasksListener.mTaskIds.put(runningTaskInfo.taskId, true);
                    FullscreenTaskListener fullscreenTaskListener = FullscreenTaskListener.this;
                    if (fullscreenTaskListener.mFullscreenUnfoldController != null) {
                        SurfaceControl surfaceControl2 = fullscreenTaskListener.mDataByTaskId.get(runningTaskInfo.taskId).surface;
                        FullscreenUnfoldController fullscreenUnfoldController = FullscreenTaskListener.this.mFullscreenUnfoldController;
                        Objects.requireNonNull(fullscreenUnfoldController);
                        fullscreenUnfoldController.mAnimationContextByTaskId.put(runningTaskInfo.taskId, new FullscreenUnfoldController.AnimationContext(surfaceControl2, fullscreenUnfoldController.mTaskbarInsetsSource, runningTaskInfo));
                    }
                }
                this.mRecentTasksOptional.ifPresent(new OverviewProxyService$$ExternalSyntheticLambda5(runningTaskInfo, 1));
                return;
            }
            return;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Task appeared more than once: #");
        m.append(runningTaskInfo.taskId);
        throw new IllegalStateException(m.toString());
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        boolean z;
        FullscreenUnfoldController.AnimationContext animationContext;
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            AnimatableTasksListener animatableTasksListener = this.mAnimatableTasksListener;
            Objects.requireNonNull(animatableTasksListener);
            boolean z2 = animatableTasksListener.mTaskIds.get(runningTaskInfo.taskId);
            if (!((TaskInfo) runningTaskInfo).isVisible || runningTaskInfo.getConfiguration().windowConfiguration.getActivityType() == 2) {
                z = false;
            } else {
                z = true;
            }
            if (z2) {
                if (z) {
                    FullscreenUnfoldController fullscreenUnfoldController = FullscreenTaskListener.this.mFullscreenUnfoldController;
                    if (!(fullscreenUnfoldController == null || (animationContext = fullscreenUnfoldController.mAnimationContextByTaskId.get(runningTaskInfo.taskId)) == null)) {
                        animationContext.update(fullscreenUnfoldController.mTaskbarInsetsSource, runningTaskInfo);
                    }
                } else {
                    FullscreenUnfoldController fullscreenUnfoldController2 = FullscreenTaskListener.this.mFullscreenUnfoldController;
                    if (fullscreenUnfoldController2 != null) {
                        fullscreenUnfoldController2.onTaskVanished(runningTaskInfo);
                    }
                    animatableTasksListener.mTaskIds.put(runningTaskInfo.taskId, false);
                }
            } else if (z) {
                animatableTasksListener.mTaskIds.put(runningTaskInfo.taskId, true);
                FullscreenTaskListener fullscreenTaskListener = FullscreenTaskListener.this;
                if (fullscreenTaskListener.mFullscreenUnfoldController != null) {
                    SurfaceControl surfaceControl = fullscreenTaskListener.mDataByTaskId.get(runningTaskInfo.taskId).surface;
                    FullscreenUnfoldController fullscreenUnfoldController3 = FullscreenTaskListener.this.mFullscreenUnfoldController;
                    Objects.requireNonNull(fullscreenUnfoldController3);
                    fullscreenUnfoldController3.mAnimationContextByTaskId.put(runningTaskInfo.taskId, new FullscreenUnfoldController.AnimationContext(surfaceControl, fullscreenUnfoldController3.mTaskbarInsetsSource, runningTaskInfo));
                }
            }
            this.mRecentTasksOptional.ifPresent(new OverviewProxyService$$ExternalSyntheticLambda5(runningTaskInfo, 1));
            final TaskData taskData = this.mDataByTaskId.get(runningTaskInfo.taskId);
            final Point point = runningTaskInfo.positionInParent;
            if (!point.equals(taskData.positionInParent)) {
                taskData.positionInParent.set(point.x, point.y);
                this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.fullscreen.FullscreenTaskListener$$ExternalSyntheticLambda1
                    @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                        FullscreenTaskListener.TaskData taskData2 = FullscreenTaskListener.TaskData.this;
                        Point point2 = point;
                        transaction.setPosition(taskData2.surface, point2.x, point2.y);
                    }
                });
            }
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        FullscreenUnfoldController fullscreenUnfoldController;
        if (this.mDataByTaskId.get(runningTaskInfo.taskId) == null) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Task already vanished: #");
            m.append(runningTaskInfo.taskId);
            Slog.e("FullscreenTaskListener", m.toString());
            return;
        }
        AnimatableTasksListener animatableTasksListener = this.mAnimatableTasksListener;
        Objects.requireNonNull(animatableTasksListener);
        if (animatableTasksListener.mTaskIds.get(runningTaskInfo.taskId) && (fullscreenUnfoldController = FullscreenTaskListener.this.mFullscreenUnfoldController) != null) {
            fullscreenUnfoldController.onTaskVanished(runningTaskInfo);
        }
        animatableTasksListener.mTaskIds.put(runningTaskInfo.taskId, false);
        this.mDataByTaskId.remove(runningTaskInfo.taskId);
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -2117150342, 1, null, Long.valueOf(runningTaskInfo.taskId));
        }
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("FullscreenTaskListener:");
        m.append(ShellTaskOrganizer.taskListenerTypeToString(-2));
        return m.toString();
    }

    public FullscreenTaskListener(SyncTransactionQueue syncTransactionQueue, Optional<FullscreenUnfoldController> optional, Optional<RecentTasksController> optional2) {
        this.mSyncQueue = syncTransactionQueue;
        this.mFullscreenUnfoldController = optional.orElse(null);
        this.mRecentTasksOptional = optional2;
    }
}
