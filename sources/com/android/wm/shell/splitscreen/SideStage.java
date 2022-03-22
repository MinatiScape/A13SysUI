package com.android.wm.shell.splitscreen;

import android.content.Context;
import android.view.SurfaceSession;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.launcher3.icons.IconProvider;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.splitscreen.StageCoordinator;
/* loaded from: classes.dex */
public final class SideStage extends StageTaskListener {
    public final boolean removeAllTasks(WindowContainerTransaction windowContainerTransaction, boolean z) {
        windowContainerTransaction.reorder(this.mRootTaskInfo.token, false);
        if (this.mChildrenTaskInfo.size() == 0) {
            return false;
        }
        windowContainerTransaction.reparentTasks(this.mRootTaskInfo.token, (WindowContainerToken) null, StageTaskListener.CONTROLLED_WINDOWING_MODES_WHEN_ACTIVE, StageTaskListener.CONTROLLED_ACTIVITY_TYPES, z);
        return true;
    }

    public SideStage(Context context, ShellTaskOrganizer shellTaskOrganizer, StageCoordinator.StageListenerImpl stageListenerImpl, SyncTransactionQueue syncTransactionQueue, SurfaceSession surfaceSession, IconProvider iconProvider, StageTaskUnfoldController stageTaskUnfoldController) {
        super(context, shellTaskOrganizer, stageListenerImpl, syncTransactionQueue, surfaceSession, iconProvider, stageTaskUnfoldController);
    }
}
