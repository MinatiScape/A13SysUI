package com.android.wm.shell.apppairs;

import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AppPairsPool {
    @VisibleForTesting
    public final AppPairsController mController;
    public final ArrayList<AppPair> mPool = new ArrayList<>();

    @VisibleForTesting
    public void incrementPool() {
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 1079041527, 1, null, Long.valueOf(this.mPool.size()));
        }
        AppPair appPair = new AppPair(this.mController);
        AppPairsController appPairsController = this.mController;
        Objects.requireNonNull(appPairsController);
        appPairsController.mTaskOrganizer.createRootTask(1, appPair);
        this.mPool.add(appPair);
    }

    @VisibleForTesting
    public int poolSize() {
        return this.mPool.size();
    }

    public final void release(AppPair appPair) {
        this.mPool.add(appPair);
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 1891981945, 16, null, String.valueOf(appPair.getRootTaskId()), String.valueOf(appPair), Long.valueOf(this.mPool.size()));
        }
    }

    public final String toString() {
        StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m("AppPairsPool", "#");
        m.append(this.mPool.size());
        return m.toString();
    }

    public AppPairsPool(AppPairsController appPairsController) {
        this.mController = appPairsController;
        incrementPool();
    }
}
