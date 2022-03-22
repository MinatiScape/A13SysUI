package com.android.systemui.shared.system;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.content.pm.UserInfo;
import android.os.RemoteException;
import java.util.List;
/* loaded from: classes.dex */
public final class ActivityManagerWrapper {
    public static final ActivityManagerWrapper sInstance = new ActivityManagerWrapper();
    public final ActivityTaskManager mAtm = ActivityTaskManager.getInstance();

    public final ActivityManager.RunningTaskInfo getRunningTask() {
        List tasks = this.mAtm.getTasks(1, false);
        if (tasks.isEmpty()) {
            return null;
        }
        return (ActivityManager.RunningTaskInfo) tasks.get(0);
    }

    public static int getCurrentUserId() {
        try {
            UserInfo currentUser = ActivityManager.getService().getCurrentUser();
            if (currentUser != null) {
                return currentUser.id;
            }
            return 0;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean isScreenPinningActive() {
        if (ActivityTaskManager.getService().getLockTaskModeState() == 2) {
            return true;
        }
        return false;
    }
}
