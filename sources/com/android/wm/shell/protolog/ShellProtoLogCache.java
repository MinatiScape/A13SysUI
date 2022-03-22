package com.android.wm.shell.protolog;

import com.android.internal.protolog.BaseProtoLogImpl;
/* loaded from: classes.dex */
public final class ShellProtoLogCache {
    public static boolean WM_SHELL_BACK_PREVIEW_enabled = false;
    public static boolean WM_SHELL_DRAG_AND_DROP_enabled = false;
    public static boolean WM_SHELL_RECENT_TASKS_enabled = false;
    public static boolean WM_SHELL_STARTING_WINDOW_enabled = false;
    public static boolean WM_SHELL_TASK_ORG_enabled = false;
    public static boolean WM_SHELL_TRANSITIONS_enabled = false;

    static {
        BaseProtoLogImpl.sCacheUpdater = ShellProtoLogCache$$ExternalSyntheticLambda0.INSTANCE;
        update();
    }

    public static void update() {
        WM_SHELL_TASK_ORG_enabled = ShellProtoLogImpl.isEnabled(ShellProtoLogGroup.WM_SHELL_TASK_ORG);
        WM_SHELL_TRANSITIONS_enabled = ShellProtoLogImpl.isEnabled(ShellProtoLogGroup.WM_SHELL_TRANSITIONS);
        WM_SHELL_DRAG_AND_DROP_enabled = ShellProtoLogImpl.isEnabled(ShellProtoLogGroup.WM_SHELL_DRAG_AND_DROP);
        WM_SHELL_STARTING_WINDOW_enabled = ShellProtoLogImpl.isEnabled(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW);
        WM_SHELL_BACK_PREVIEW_enabled = ShellProtoLogImpl.isEnabled(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW);
        WM_SHELL_RECENT_TASKS_enabled = ShellProtoLogImpl.isEnabled(ShellProtoLogGroup.WM_SHELL_RECENT_TASKS);
        ShellProtoLogImpl.isEnabled(ShellProtoLogGroup.TEST_GROUP);
    }
}
