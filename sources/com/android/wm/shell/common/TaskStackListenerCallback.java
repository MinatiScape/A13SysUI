package com.android.wm.shell.common;

import android.app.ActivityManager;
/* loaded from: classes.dex */
public interface TaskStackListenerCallback {
    default void onActivityDismissingDockedStack() {
    }

    default void onActivityForcedResizable(String str, int i, int i2) {
    }

    default void onActivityLaunchOnSecondaryDisplayFailed() {
    }

    default void onActivityPinned(String str) {
    }

    default void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2) {
    }

    default void onActivityUnpinned() {
    }

    default void onRecentTaskListUpdated() {
    }

    default void onTaskCreated() {
    }

    default void onTaskMovedToFront(int i) {
    }

    default void onTaskStackChanged() {
    }

    default void onTaskMovedToFront(ActivityManager.RunningTaskInfo runningTaskInfo) {
        onTaskMovedToFront(runningTaskInfo.taskId);
    }

    default void onActivityLaunchOnSecondaryDisplayFailed$1() {
        onActivityLaunchOnSecondaryDisplayFailed();
    }
}
