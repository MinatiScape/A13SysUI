package com.android.wm.shell.back;

import android.view.MotionEvent;
/* loaded from: classes.dex */
public interface BackAnimation {
    default IBackAnimation createExternalInterface() {
        return null;
    }

    void onBackMotion(MotionEvent motionEvent, int i);

    void setSwipeThresholds(float f, float f2);

    void setTriggerBack(boolean z);
}
