package com.android.wm.shell.pip;

import android.app.ActivityManager;
import android.graphics.Rect;
import android.view.SurfaceControl;
import android.view.WindowManager;
/* loaded from: classes.dex */
public interface PipMenuController {
    void attach(SurfaceControl surfaceControl);

    void detach();

    boolean isMenuVisible();

    default void movePipMenu(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
    }

    default void onFocusTaskChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default void resizePipMenu(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
    }

    default void updateMenuBounds(Rect rect) {
    }

    static WindowManager.LayoutParams getPipMenuLayoutParams(int i, int i2) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(i, i2, 2038, 545521680, -3);
        layoutParams.privateFlags |= 536870912;
        layoutParams.setTitle("PipMenuView");
        return layoutParams;
    }
}
