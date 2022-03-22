package com.android.wm.shell.tasksurfacehelper;

import android.app.ActivityManager;
import android.graphics.Rect;
import android.view.SurfaceControl;
import android.window.TaskAppearedInfo;
import com.android.systemui.keyguard.KeyguardViewMediator$9$$ExternalSyntheticLambda0;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.ScreenshotUtils;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController;
import com.google.android.systemui.gamedashboard.ShortcutBarView$$ExternalSyntheticLambda4;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class TaskSurfaceHelperController {
    public final TaskSurfaceHelperImpl mImpl = new TaskSurfaceHelperImpl();
    public final ShellExecutor mMainExecutor;
    public final ShellTaskOrganizer mTaskOrganizer;

    /* loaded from: classes.dex */
    public class TaskSurfaceHelperImpl implements TaskSurfaceHelper {
        public TaskSurfaceHelperImpl() {
        }

        @Override // com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper
        public final void screenshotTask(final ActivityManager.RunningTaskInfo runningTaskInfo, final Rect rect, final Executor executor, final ShortcutBarView$$ExternalSyntheticLambda4 shortcutBarView$$ExternalSyntheticLambda4) {
            TaskSurfaceHelperController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController$TaskSurfaceHelperImpl$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    TaskSurfaceHelperController.TaskSurfaceHelperImpl taskSurfaceHelperImpl = TaskSurfaceHelperController.TaskSurfaceHelperImpl.this;
                    ActivityManager.RunningTaskInfo runningTaskInfo2 = runningTaskInfo;
                    Rect rect2 = rect;
                    final Executor executor2 = executor;
                    final Consumer consumer = shortcutBarView$$ExternalSyntheticLambda4;
                    Objects.requireNonNull(taskSurfaceHelperImpl);
                    TaskSurfaceHelperController taskSurfaceHelperController = TaskSurfaceHelperController.this;
                    Consumer taskSurfaceHelperController$TaskSurfaceHelperImpl$$ExternalSyntheticLambda2 = new Consumer() { // from class: com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController$TaskSurfaceHelperImpl$$ExternalSyntheticLambda2
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            executor2.execute(new KeyguardViewMediator$9$$ExternalSyntheticLambda0(consumer, (SurfaceControl.ScreenshotHardwareBuffer) obj, 3));
                        }
                    };
                    Objects.requireNonNull(taskSurfaceHelperController);
                    ShellTaskOrganizer shellTaskOrganizer = taskSurfaceHelperController.mTaskOrganizer;
                    Objects.requireNonNull(shellTaskOrganizer);
                    TaskAppearedInfo taskAppearedInfo = shellTaskOrganizer.mTasks.get(runningTaskInfo2.taskId);
                    if (taskAppearedInfo != null) {
                        ScreenshotUtils.captureLayer(taskAppearedInfo.getLeash(), rect2, taskSurfaceHelperController$TaskSurfaceHelperImpl$$ExternalSyntheticLambda2);
                    }
                }
            });
        }

        @Override // com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper
        public final void setGameModeForTask(final int i, final int i2) {
            TaskSurfaceHelperController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController$TaskSurfaceHelperImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    TaskSurfaceHelperController.TaskSurfaceHelperImpl taskSurfaceHelperImpl = TaskSurfaceHelperController.TaskSurfaceHelperImpl.this;
                    int i3 = i;
                    int i4 = i2;
                    Objects.requireNonNull(taskSurfaceHelperImpl);
                    TaskSurfaceHelperController taskSurfaceHelperController = TaskSurfaceHelperController.this;
                    Objects.requireNonNull(taskSurfaceHelperController);
                    ShellTaskOrganizer shellTaskOrganizer = taskSurfaceHelperController.mTaskOrganizer;
                    Objects.requireNonNull(shellTaskOrganizer);
                    synchronized (shellTaskOrganizer.mLock) {
                        TaskAppearedInfo taskAppearedInfo = shellTaskOrganizer.mTasks.get(i3);
                        if (!(taskAppearedInfo == null || taskAppearedInfo.getLeash() == null)) {
                            SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                            transaction.setMetadata(taskAppearedInfo.getLeash(), 8, i4);
                            transaction.apply();
                        }
                    }
                }
            });
        }
    }

    public TaskSurfaceHelperController(ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mMainExecutor = shellExecutor;
    }
}
