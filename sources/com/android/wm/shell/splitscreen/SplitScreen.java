package com.android.wm.shell.splitscreen;
/* loaded from: classes.dex */
public interface SplitScreen {

    /* loaded from: classes.dex */
    public interface SplitScreenListener {
        default void onSplitVisibilityChanged() {
        }

        default void onStagePositionChanged(int i, int i2) {
        }

        default void onTaskStageChanged(int i, int i2, boolean z) {
        }
    }

    default ISplitScreen createExternalInterface() {
        return null;
    }

    void onFinishedWakingUp();

    void onKeyguardVisibilityChanged(boolean z);
}
