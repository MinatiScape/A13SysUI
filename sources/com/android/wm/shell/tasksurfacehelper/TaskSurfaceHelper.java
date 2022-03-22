package com.android.wm.shell.tasksurfacehelper;

import android.app.ActivityManager;
import android.graphics.Rect;
import com.google.android.systemui.gamedashboard.ShortcutBarView$$ExternalSyntheticLambda4;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public interface TaskSurfaceHelper {
    default void screenshotTask(ActivityManager.RunningTaskInfo runningTaskInfo, Rect rect, Executor executor, ShortcutBarView$$ExternalSyntheticLambda4 shortcutBarView$$ExternalSyntheticLambda4) {
    }

    default void setGameModeForTask(int i, int i2) {
    }
}
