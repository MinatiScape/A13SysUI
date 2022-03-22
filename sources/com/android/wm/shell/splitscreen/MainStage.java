package com.android.wm.shell.splitscreen;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Rect;
import android.view.SurfaceSession;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.launcher3.icons.IconProvider;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.splitscreen.StageCoordinator;
/* loaded from: classes.dex */
public final class MainStage extends StageTaskListener {
    public boolean mIsActive = false;

    public final void activate(Rect rect, WindowContainerTransaction windowContainerTransaction, boolean z) {
        if (!this.mIsActive) {
            WindowContainerToken windowContainerToken = this.mRootTaskInfo.token;
            windowContainerTransaction.setBounds(windowContainerToken, rect).reorder(windowContainerToken, true);
            if (z) {
                windowContainerTransaction.reparentTasks((WindowContainerToken) null, windowContainerToken, StageTaskListener.CONTROLLED_WINDOWING_MODES, StageTaskListener.CONTROLLED_ACTIVITY_TYPES, true, true);
            }
            this.mIsActive = true;
        }
    }

    public final void deactivate(WindowContainerTransaction windowContainerTransaction, boolean z) {
        if (this.mIsActive) {
            this.mIsActive = false;
            ActivityManager.RunningTaskInfo runningTaskInfo = this.mRootTaskInfo;
            if (runningTaskInfo != null) {
                WindowContainerToken windowContainerToken = runningTaskInfo.token;
                windowContainerTransaction.reparentTasks(windowContainerToken, (WindowContainerToken) null, StageTaskListener.CONTROLLED_WINDOWING_MODES_WHEN_ACTIVE, StageTaskListener.CONTROLLED_ACTIVITY_TYPES, z).reorder(windowContainerToken, false);
            }
        }
    }

    public MainStage(Context context, ShellTaskOrganizer shellTaskOrganizer, StageCoordinator.StageListenerImpl stageListenerImpl, SyncTransactionQueue syncTransactionQueue, SurfaceSession surfaceSession, IconProvider iconProvider, StageTaskUnfoldController stageTaskUnfoldController) {
        super(context, shellTaskOrganizer, stageListenerImpl, syncTransactionQueue, surfaceSession, iconProvider, stageTaskUnfoldController);
    }
}
