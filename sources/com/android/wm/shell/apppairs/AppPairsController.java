package com.android.wm.shell.apppairs;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.util.SparseArray;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.protolog.BaseProtoLogImpl;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.apppairs.AppPair;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.split.SplitLayout;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AppPairsController {
    public final DisplayController mDisplayController;
    public final DisplayImeController mDisplayImeController;
    public final DisplayInsetsController mDisplayInsetsController;
    public final ShellExecutor mMainExecutor;
    public AppPairsPool mPairsPool;
    public final SyncTransactionQueue mSyncQueue;
    public final ShellTaskOrganizer mTaskOrganizer;
    public final AppPairsImpl mImpl = new AppPairsImpl();
    public final SparseArray<AppPair> mActiveAppPairs = new SparseArray<>();

    /* loaded from: classes.dex */
    public class AppPairsImpl {
    }

    @VisibleForTesting
    public AppPair pairInner(ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
        boolean z;
        AppPair appPair;
        AppPairsPool appPairsPool = this.mPairsPool;
        Objects.requireNonNull(appPairsPool);
        ArrayList<AppPair> arrayList = appPairsPool.mPool;
        AppPair remove = arrayList.remove(arrayList.size() - 1);
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 2006473416, 16, null, String.valueOf(remove.getRootTaskId()), String.valueOf(remove), Long.valueOf(appPairsPool.mPool.size()));
        }
        if (appPairsPool.mPool.size() == 0) {
            appPairsPool.incrementPool();
        }
        Objects.requireNonNull(remove);
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -742394458, 5, null, Long.valueOf(runningTaskInfo.taskId), Long.valueOf(runningTaskInfo2.taskId), String.valueOf(remove));
        }
        boolean z2 = runningTaskInfo.supportsMultiWindow;
        if (!z2 || !runningTaskInfo2.supportsMultiWindow) {
            z = false;
            appPair = null;
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.getSingleInstance().log(BaseProtoLogImpl.LogLevel.ERROR, ShellProtoLogGroup.WM_SHELL_TASK_ORG, -553798917, 15, (String) null, new Object[]{Boolean.valueOf(z2), Boolean.valueOf(runningTaskInfo2.supportsMultiWindow)});
            }
        } else {
            remove.mTaskInfo1 = runningTaskInfo;
            remove.mTaskInfo2 = runningTaskInfo2;
            Context displayContext = remove.mDisplayController.getDisplayContext(remove.mRootTaskInfo.displayId);
            Configuration configuration = remove.mRootTaskInfo.configuration;
            AppPair.AnonymousClass1 r12 = remove.mParentContainerCallbacks;
            DisplayImeController displayImeController = remove.mDisplayImeController;
            AppPairsController appPairsController = remove.mController;
            Objects.requireNonNull(appPairsController);
            ShellTaskOrganizer shellTaskOrganizer = appPairsController.mTaskOrganizer;
            appPair = null;
            SplitLayout splitLayout = new SplitLayout("AppPairSplitDivider", displayContext, configuration, remove, r12, displayImeController, shellTaskOrganizer, true);
            remove.mSplitLayout = splitLayout;
            remove.mDisplayInsetsController.addInsetsChangedListener(remove.mRootTaskInfo.displayId, splitLayout);
            WindowContainerToken windowContainerToken = runningTaskInfo.token;
            WindowContainerToken windowContainerToken2 = runningTaskInfo2.token;
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            WindowContainerTransaction windowingMode = windowContainerTransaction.setHidden(remove.mRootTaskInfo.token, false).reparent(windowContainerToken, remove.mRootTaskInfo.token, true).reparent(windowContainerToken2, remove.mRootTaskInfo.token, true).setWindowingMode(windowContainerToken, 6).setWindowingMode(windowContainerToken2, 6);
            SplitLayout splitLayout2 = remove.mSplitLayout;
            Objects.requireNonNull(splitLayout2);
            WindowContainerTransaction bounds = windowingMode.setBounds(windowContainerToken, new Rect(splitLayout2.mBounds1));
            SplitLayout splitLayout3 = remove.mSplitLayout;
            Objects.requireNonNull(splitLayout3);
            bounds.setBounds(windowContainerToken2, new Rect(splitLayout3.mBounds2)).reorder(remove.mRootTaskInfo.token, true);
            AppPairsController appPairsController2 = remove.mController;
            Objects.requireNonNull(appPairsController2);
            appPairsController2.mTaskOrganizer.applyTransaction(windowContainerTransaction);
            z = true;
        }
        if (!z) {
            this.mPairsPool.release(remove);
            return appPair;
        }
        this.mActiveAppPairs.put(remove.getRootTaskId(), remove);
        return remove;
    }

    public final String toString() {
        StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m("AppPairsController", "#");
        m.append(this.mActiveAppPairs.size());
        return m.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0041 A[LOOP:0: B:5:0x0013->B:21:0x0041, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x003f A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void unpair(int r10, boolean r11) {
        /*
            r9 = this;
            android.util.SparseArray<com.android.wm.shell.apppairs.AppPair> r0 = r9.mActiveAppPairs
            java.lang.Object r0 = r0.get(r10)
            com.android.wm.shell.apppairs.AppPair r0 = (com.android.wm.shell.apppairs.AppPair) r0
            r1 = 0
            r2 = 1
            if (r0 != 0) goto L_0x0044
            android.util.SparseArray<com.android.wm.shell.apppairs.AppPair> r3 = r9.mActiveAppPairs
            int r3 = r3.size()
            int r3 = r3 - r2
        L_0x0013:
            if (r3 < 0) goto L_0x0044
            android.util.SparseArray<com.android.wm.shell.apppairs.AppPair> r4 = r9.mActiveAppPairs
            java.lang.Object r4 = r4.valueAt(r3)
            com.android.wm.shell.apppairs.AppPair r4 = (com.android.wm.shell.apppairs.AppPair) r4
            java.util.Objects.requireNonNull(r4)
            int r5 = r4.getRootTaskId()
            if (r10 == r5) goto L_0x003c
            android.app.ActivityManager$RunningTaskInfo r5 = r4.mTaskInfo1
            r6 = -1
            if (r5 == 0) goto L_0x002e
            int r5 = r5.taskId
            goto L_0x002f
        L_0x002e:
            r5 = r6
        L_0x002f:
            if (r10 == r5) goto L_0x003c
            android.app.ActivityManager$RunningTaskInfo r5 = r4.mTaskInfo2
            if (r5 == 0) goto L_0x0037
            int r6 = r5.taskId
        L_0x0037:
            if (r10 != r6) goto L_0x003a
            goto L_0x003c
        L_0x003a:
            r5 = r1
            goto L_0x003d
        L_0x003c:
            r5 = r2
        L_0x003d:
            if (r5 == 0) goto L_0x0041
            r0 = r4
            goto L_0x0044
        L_0x0041:
            int r3 = r3 + (-1)
            goto L_0x0013
        L_0x0044:
            r3 = 0
            if (r0 != 0) goto L_0x005d
            boolean r9 = com.android.wm.shell.protolog.ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled
            if (r9 == 0) goto L_0x005c
            long r9 = (long) r10
            com.android.wm.shell.protolog.ShellProtoLogGroup r11 = com.android.wm.shell.protolog.ShellProtoLogGroup.WM_SHELL_TASK_ORG
            r0 = 950299522(0x38a46b82, float:7.840154E-5)
            java.lang.Object[] r4 = new java.lang.Object[r2]
            java.lang.Long r9 = java.lang.Long.valueOf(r9)
            r4[r1] = r9
            com.android.wm.shell.protolog.ShellProtoLogImpl.v(r11, r0, r2, r3, r4)
        L_0x005c:
            return
        L_0x005d:
            boolean r4 = com.android.wm.shell.protolog.ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled
            if (r4 == 0) goto L_0x0079
            long r4 = (long) r10
            java.lang.String r10 = java.lang.String.valueOf(r0)
            com.android.wm.shell.protolog.ShellProtoLogGroup r6 = com.android.wm.shell.protolog.ShellProtoLogGroup.WM_SHELL_TASK_ORG
            r7 = -234284913(0xfffffffff209188f, float:-2.7154647E30)
            r8 = 2
            java.lang.Object[] r8 = new java.lang.Object[r8]
            java.lang.Long r4 = java.lang.Long.valueOf(r4)
            r8[r1] = r4
            r8[r2] = r10
            com.android.wm.shell.protolog.ShellProtoLogImpl.v(r6, r7, r2, r3, r8)
        L_0x0079:
            android.util.SparseArray<com.android.wm.shell.apppairs.AppPair> r10 = r9.mActiveAppPairs
            int r1 = r0.getRootTaskId()
            r10.remove(r1)
            r0.unpair(r3)
            if (r11 == 0) goto L_0x008c
            com.android.wm.shell.apppairs.AppPairsPool r9 = r9.mPairsPool
            r9.release(r0)
        L_0x008c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.apppairs.AppPairsController.unpair(int, boolean):void");
    }

    public AppPairsController(ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, DisplayController displayController, ShellExecutor shellExecutor, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mSyncQueue = syncTransactionQueue;
        this.mDisplayController = displayController;
        this.mDisplayImeController = displayImeController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mMainExecutor = shellExecutor;
    }

    @VisibleForTesting
    public void setPairsPool(AppPairsPool appPairsPool) {
        this.mPairsPool = appPairsPool;
    }
}
