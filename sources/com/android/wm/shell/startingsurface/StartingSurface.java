package com.android.wm.shell.startingsurface;

import android.app.ActivityManager;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda4;
/* loaded from: classes.dex */
public interface StartingSurface {

    /* loaded from: classes.dex */
    public interface SysuiProxy {
    }

    default IStartingWindow createExternalInterface() {
        return null;
    }

    default int getBackgroundColor(ActivityManager.RunningTaskInfo runningTaskInfo) {
        return -16777216;
    }

    void setSysuiProxy(StatusBar$$ExternalSyntheticLambda4 statusBar$$ExternalSyntheticLambda4);
}
