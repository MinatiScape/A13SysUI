package com.android.systemui.shared.system;

import android.app.ActivityManager;
import android.content.ComponentName;
/* loaded from: classes.dex */
public abstract class TaskStackChangeListener {
    public void onActivityPinned() {
    }

    public void onActivityRequestedOrientationChanged(int i) {
    }

    public void onActivityUnpinned() {
    }

    public void onLockTaskModeChanged(int i) {
    }

    public void onTaskCreated(ComponentName componentName) {
    }

    public void onTaskMovedToFront() {
    }

    public void onTaskMovedToFront(ActivityManager.RunningTaskInfo runningTaskInfo) {
        int i = runningTaskInfo.taskId;
        onTaskMovedToFront();
    }

    public void onTaskProfileLocked(int i, int i2) {
    }

    public void onTaskRemoved() {
    }

    public void onTaskStackChanged() {
    }

    public void onTaskStackChangedBackground() {
    }
}
