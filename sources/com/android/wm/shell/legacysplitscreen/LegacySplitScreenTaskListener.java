package com.android.wm.shell.legacysplitscreen;

import android.app.ActivityManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.graphics.Point;
import android.graphics.Rect;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.util.SparseArray;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.window.WindowContainerTransaction;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.SurfaceUtils;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.Transitions;
import java.io.PrintWriter;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LegacySplitScreenTaskListener implements ShellTaskOrganizer.TaskListener {
    public ActivityManager.RunningTaskInfo mPrimary;
    public SurfaceControl mPrimaryDim;
    public SurfaceControl mPrimarySurface;
    public ActivityManager.RunningTaskInfo mSecondary;
    public SurfaceControl mSecondaryDim;
    public SurfaceControl mSecondarySurface;
    public final LegacySplitScreenController mSplitScreenController;
    public final LegacySplitScreenTransitions mSplitTransitions;
    public final SyncTransactionQueue mSyncQueue;
    public final ShellTaskOrganizer mTaskOrganizer;
    public final SparseArray<SurfaceControl> mLeashByTaskId = new SparseArray<>();
    public final SparseArray<Point> mPositionByTaskId = new SparseArray<>();
    public Rect mHomeBounds = new Rect();
    public boolean mSplitScreenSupported = false;
    public final SurfaceSession mSurfaceSession = new SurfaceSession();

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        synchronized (this) {
            try {
                if (runningTaskInfo.hasParentTask()) {
                    this.mLeashByTaskId.put(runningTaskInfo.taskId, surfaceControl);
                    this.mPositionByTaskId.put(runningTaskInfo.taskId, new Point(runningTaskInfo.positionInParent));
                    if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                        this.mSyncQueue.runInSync(new LegacySplitScreenTaskListener$$ExternalSyntheticLambda0(surfaceControl, runningTaskInfo.positionInParent, true));
                    }
                    return;
                }
                int windowingMode = runningTaskInfo.getWindowingMode();
                if (windowingMode == 3) {
                    if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                        ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -1362429294, 4, null, "LegacySplitScreenTaskListener", Long.valueOf(runningTaskInfo.taskId));
                    }
                    this.mPrimary = runningTaskInfo;
                    this.mPrimarySurface = surfaceControl;
                } else if (windowingMode == 4) {
                    if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                        ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 982027396, 4, null, "LegacySplitScreenTaskListener", Long.valueOf(runningTaskInfo.taskId));
                    }
                    this.mSecondary = runningTaskInfo;
                    this.mSecondarySurface = surfaceControl;
                } else if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -298656957, 20, null, "LegacySplitScreenTaskListener", Long.valueOf(runningTaskInfo.taskId), Long.valueOf(windowingMode));
                }
                if (!(this.mSplitScreenSupported || this.mPrimarySurface == null || this.mSecondarySurface == null)) {
                    this.mSplitScreenSupported = true;
                    LegacySplitScreenController legacySplitScreenController = this.mSplitScreenController;
                    Objects.requireNonNull(legacySplitScreenController);
                    WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                    legacySplitScreenController.mSplitLayout.resizeSplits(legacySplitScreenController.mSplitLayout.getSnapAlgorithm().getMiddleTarget().position, windowContainerTransaction);
                    legacySplitScreenController.mTaskOrganizer.applyTransaction(windowContainerTransaction);
                    if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                        ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 473543554, 0, null, "LegacySplitScreenTaskListener");
                    }
                    SurfaceControl.Transaction transaction = getTransaction();
                    this.mPrimaryDim = SurfaceUtils.makeDimLayer(transaction, this.mPrimarySurface, "Primary Divider Dim", this.mSurfaceSession);
                    this.mSecondaryDim = SurfaceUtils.makeDimLayer(transaction, this.mSecondarySurface, "Secondary Divider Dim", this.mSurfaceSession);
                    transaction.apply();
                    releaseTransaction(transaction);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        boolean z;
        synchronized (this) {
            this.mPositionByTaskId.remove(runningTaskInfo.taskId);
            if (runningTaskInfo.hasParentTask()) {
                this.mLeashByTaskId.remove(runningTaskInfo.taskId);
                return;
            }
            ActivityManager.RunningTaskInfo runningTaskInfo2 = this.mPrimary;
            boolean z2 = true;
            if (runningTaskInfo2 == null || !runningTaskInfo.token.equals(runningTaskInfo2.token)) {
                z = false;
            } else {
                z = true;
            }
            ActivityManager.RunningTaskInfo runningTaskInfo3 = this.mSecondary;
            if (runningTaskInfo3 == null || !runningTaskInfo.token.equals(runningTaskInfo3.token)) {
                z2 = false;
            }
            if (this.mSplitScreenSupported && (z || z2)) {
                this.mSplitScreenSupported = false;
                SurfaceControl.Transaction transaction = getTransaction();
                transaction.remove(this.mPrimaryDim);
                transaction.remove(this.mSecondaryDim);
                transaction.remove(this.mPrimarySurface);
                transaction.remove(this.mSecondarySurface);
                transaction.apply();
                releaseTransaction(transaction);
                LegacySplitScreenController legacySplitScreenController = this.mSplitScreenController;
                Objects.requireNonNull(legacySplitScreenController);
                legacySplitScreenController.removeDivider();
            }
        }
    }

    public final String toString() {
        return "LegacySplitScreenTaskListener";
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
        if (this.mLeashByTaskId.contains(i)) {
            builder.setParent(this.mLeashByTaskId.get(i));
            return;
        }
        throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("There is no surface for taskId=", i));
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void dump(PrintWriter printWriter, String str) {
        String m = SupportMenuInflater$$ExternalSyntheticOutline0.m(str, "  ");
        printWriter.println(str + this);
        printWriter.println(m + "mSplitScreenSupported=" + this.mSplitScreenSupported);
        if (this.mPrimary != null) {
            StringBuilder m2 = DebugInfo$$ExternalSyntheticOutline0.m(m, "mPrimary.taskId=");
            m2.append(this.mPrimary.taskId);
            printWriter.println(m2.toString());
        }
        if (this.mSecondary != null) {
            StringBuilder m3 = DebugInfo$$ExternalSyntheticOutline0.m(m, "mSecondary.taskId=");
            m3.append(this.mSecondary.taskId);
            printWriter.println(m3.toString());
        }
    }

    public final SurfaceControl.Transaction getTransaction() {
        return this.mSplitScreenController.mTransactionPool.acquire();
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x002e, code lost:
        if (r0.mHomeStackResizable != false) goto L_0x0033;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x008d, code lost:
        if (r1.mHomeStackResizable != false) goto L_0x0091;
     */
    /* JADX WARN: Removed duplicated region for block: B:15:0x003a  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x003c  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0059  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x006f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0070  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void handleTaskInfoChanged(android.app.ActivityManager.RunningTaskInfo r10) {
        /*
            Method dump skipped, instructions count: 337
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTaskListener.handleTaskInfoChanged(android.app.ActivityManager$RunningTaskInfo):void");
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        boolean z;
        if (runningTaskInfo.displayId == 0) {
            synchronized (this) {
                if (!runningTaskInfo.supportsMultiWindow) {
                    if (this.mSplitScreenController.isDividerVisible()) {
                        int i = runningTaskInfo.taskId;
                        int i2 = this.mPrimary.taskId;
                        if (!(i == i2 || runningTaskInfo.parentTaskId == i2)) {
                            LegacySplitScreenController legacySplitScreenController = this.mSplitScreenController;
                            if (!runningTaskInfo.isFocused) {
                                z = true;
                            } else {
                                z = false;
                            }
                            Objects.requireNonNull(legacySplitScreenController);
                            legacySplitScreenController.startDismissSplit(z, false);
                        }
                        LegacySplitScreenController legacySplitScreenController2 = this.mSplitScreenController;
                        boolean z2 = runningTaskInfo.isFocused;
                        Objects.requireNonNull(legacySplitScreenController2);
                        legacySplitScreenController2.startDismissSplit(z2, false);
                    }
                    return;
                }
                if (!runningTaskInfo.hasParentTask()) {
                    handleTaskInfoChanged(runningTaskInfo);
                } else if (!runningTaskInfo.positionInParent.equals(this.mPositionByTaskId.get(runningTaskInfo.taskId))) {
                    if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                        this.mSyncQueue.runInSync(new LegacySplitScreenTaskListener$$ExternalSyntheticLambda0(this.mLeashByTaskId.get(runningTaskInfo.taskId), runningTaskInfo.positionInParent, false));
                    }
                } else {
                    return;
                }
                this.mPositionByTaskId.put(runningTaskInfo.taskId, new Point(runningTaskInfo.positionInParent));
            }
        }
    }

    public final void releaseTransaction(SurfaceControl.Transaction transaction) {
        this.mSplitScreenController.mTransactionPool.release(transaction);
    }

    public LegacySplitScreenTaskListener(LegacySplitScreenController legacySplitScreenController, ShellTaskOrganizer shellTaskOrganizer, Transitions transitions, SyncTransactionQueue syncTransactionQueue) {
        this.mSplitScreenController = legacySplitScreenController;
        this.mTaskOrganizer = shellTaskOrganizer;
        LegacySplitScreenTransitions legacySplitScreenTransitions = new LegacySplitScreenTransitions(legacySplitScreenController.mTransactionPool, transitions, legacySplitScreenController, this);
        this.mSplitTransitions = legacySplitScreenTransitions;
        Objects.requireNonNull(transitions);
        transitions.mHandlers.add(legacySplitScreenTransitions);
        this.mSyncQueue = syncTransactionQueue;
    }
}
