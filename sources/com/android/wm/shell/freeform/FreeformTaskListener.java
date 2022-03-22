package com.android.wm.shell.freeform;

import android.app.ActivityManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Slog;
import android.util.SparseArray;
import android.view.SurfaceControl;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public final class FreeformTaskListener implements ShellTaskOrganizer.TaskListener {
    public final SyncTransactionQueue mSyncQueue;
    public final SparseArray<State> mTasks = new SparseArray<>();

    /* loaded from: classes.dex */
    public static class State {
        public SurfaceControl mLeash;

        public State() {
        }

        public State(int i) {
        }
    }

    public final String toString() {
        return "FreeformTaskListener";
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void dump(PrintWriter printWriter, String str) {
        String m = SupportMenuInflater$$ExternalSyntheticOutline0.m(str, "  ");
        printWriter.println(str + this);
        printWriter.println(m + this.mTasks.size() + " tasks");
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onTaskAppeared(final ActivityManager.RunningTaskInfo runningTaskInfo, final SurfaceControl surfaceControl) {
        if (this.mTasks.get(runningTaskInfo.taskId) == null) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -166960725, 1, null, Long.valueOf(runningTaskInfo.taskId));
            }
            State state = new State(0);
            state.mLeash = surfaceControl;
            this.mTasks.put(runningTaskInfo.taskId, state);
            final Rect bounds = runningTaskInfo.configuration.windowConfiguration.getBounds();
            this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.freeform.FreeformTaskListener$$ExternalSyntheticLambda0
                @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                    ActivityManager.RunningTaskInfo runningTaskInfo2 = runningTaskInfo;
                    SurfaceControl surfaceControl2 = surfaceControl;
                    Rect rect = bounds;
                    Point point = runningTaskInfo2.positionInParent;
                    transaction.setPosition(surfaceControl2, point.x, point.y).setWindowCrop(surfaceControl2, rect.width(), rect.height()).show(surfaceControl2);
                }
            });
            return;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Task appeared more than once: #");
        m.append(runningTaskInfo.taskId);
        throw new RuntimeException(m.toString());
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onTaskInfoChanged(final ActivityManager.RunningTaskInfo runningTaskInfo) {
        State state = this.mTasks.get(runningTaskInfo.taskId);
        if (state != null) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -272049475, 1, null, Long.valueOf(runningTaskInfo.taskId));
            }
            final Rect bounds = runningTaskInfo.configuration.windowConfiguration.getBounds();
            final SurfaceControl surfaceControl = state.mLeash;
            this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.freeform.FreeformTaskListener$$ExternalSyntheticLambda1
                @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                    ActivityManager.RunningTaskInfo runningTaskInfo2 = runningTaskInfo;
                    SurfaceControl surfaceControl2 = surfaceControl;
                    Rect rect = bounds;
                    Point point = runningTaskInfo2.positionInParent;
                    transaction.setPosition(surfaceControl2, point.x, point.y).setWindowCrop(surfaceControl2, rect.width(), rect.height()).show(surfaceControl2);
                }
            });
            return;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Task info changed before appearing: #");
        m.append(runningTaskInfo.taskId);
        throw new RuntimeException(m.toString());
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (this.mTasks.get(runningTaskInfo.taskId) == null) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Task already vanished: #");
            m.append(runningTaskInfo.taskId);
            Slog.e("FreeformTaskListener", m.toString());
            return;
        }
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 1899149317, 1, null, Long.valueOf(runningTaskInfo.taskId));
        }
        this.mTasks.remove(runningTaskInfo.taskId);
    }

    public FreeformTaskListener(SyncTransactionQueue syncTransactionQueue) {
        this.mSyncQueue = syncTransactionQueue;
    }
}
