package com.android.systemui.recents;

import android.app.ActivityManager;
import android.graphics.Rect;
import android.view.View;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.wm.shell.common.split.SplitLayout;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.splitscreen.StageCoordinator;
import com.android.wm.shell.util.StagedSplitBounds;
import com.google.android.systemui.lowlightclock.LowLightClockControllerImpl;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$$ExternalSyntheticLambda5 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ OverviewProxyService$$ExternalSyntheticLambda5(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        boolean z;
        int i;
        int i2;
        switch (this.$r8$classId) {
            case 0:
                OverviewProxyService overviewProxyService = (OverviewProxyService) this.f$0;
                Objects.requireNonNull(overviewProxyService);
                overviewProxyService.mInputFocusTransferStarted = false;
                ((StatusBar) obj).onInputFocusTransfer(false, true, 0.0f);
                return;
            case 1:
                ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) this.f$0;
                RecentTasksController recentTasksController = (RecentTasksController) obj;
                if (runningTaskInfo.isVisible) {
                    recentTasksController.removeSplitPair(runningTaskInfo.taskId);
                    return;
                }
                return;
            case 2:
                StageCoordinator stageCoordinator = (StageCoordinator) this.f$0;
                RecentTasksController recentTasksController2 = (RecentTasksController) obj;
                Objects.requireNonNull(stageCoordinator);
                SplitLayout splitLayout = stageCoordinator.mSplitLayout;
                Objects.requireNonNull(splitLayout);
                Rect rect = new Rect(splitLayout.mBounds1);
                SplitLayout splitLayout2 = stageCoordinator.mSplitLayout;
                Objects.requireNonNull(splitLayout2);
                Rect rect2 = new Rect(splitLayout2.mBounds2);
                int topVisibleChildTaskId = stageCoordinator.mMainStage.getTopVisibleChildTaskId();
                int topVisibleChildTaskId2 = stageCoordinator.mSideStage.getTopVisibleChildTaskId();
                if (stageCoordinator.mSideStagePosition == 0) {
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    i2 = topVisibleChildTaskId;
                    i = topVisibleChildTaskId2;
                } else {
                    i = topVisibleChildTaskId;
                    i2 = topVisibleChildTaskId2;
                }
                StagedSplitBounds stagedSplitBounds = new StagedSplitBounds(rect, rect2, i, i2);
                if (topVisibleChildTaskId != -1 && topVisibleChildTaskId2 != -1) {
                    if (topVisibleChildTaskId == topVisibleChildTaskId2) {
                        Objects.requireNonNull(recentTasksController2);
                        return;
                    } else if (recentTasksController2.mSplitTasks.get(topVisibleChildTaskId, -1) != topVisibleChildTaskId2 || !((StagedSplitBounds) recentTasksController2.mTaskSplitBoundsMap.get(Integer.valueOf(topVisibleChildTaskId))).equals(stagedSplitBounds)) {
                        recentTasksController2.removeSplitPair(topVisibleChildTaskId);
                        recentTasksController2.removeSplitPair(topVisibleChildTaskId2);
                        recentTasksController2.mTaskSplitBoundsMap.remove(Integer.valueOf(topVisibleChildTaskId));
                        recentTasksController2.mTaskSplitBoundsMap.remove(Integer.valueOf(topVisibleChildTaskId2));
                        recentTasksController2.mSplitTasks.put(topVisibleChildTaskId, topVisibleChildTaskId2);
                        recentTasksController2.mSplitTasks.put(topVisibleChildTaskId2, topVisibleChildTaskId);
                        recentTasksController2.mTaskSplitBoundsMap.put(Integer.valueOf(topVisibleChildTaskId), stagedSplitBounds);
                        recentTasksController2.mTaskSplitBoundsMap.put(Integer.valueOf(topVisibleChildTaskId2), stagedSplitBounds);
                        recentTasksController2.notifyRecentTasksChanged();
                        if (ShellProtoLogCache.WM_SHELL_RECENT_TASKS_enabled) {
                            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_RECENT_TASKS, 1423767195, 5, null, Long.valueOf(topVisibleChildTaskId), Long.valueOf(topVisibleChildTaskId2), String.valueOf(stagedSplitBounds));
                            return;
                        }
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            default:
                Map.Entry entry = (Map.Entry) obj;
                Objects.requireNonNull((LowLightClockControllerImpl) this.f$0);
                ((View) entry.getKey()).setAlpha(((Float) entry.getValue()).floatValue());
                return;
        }
    }
}
