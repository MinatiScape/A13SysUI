package com.android.wm.shell.protolog;

import com.android.internal.protolog.common.IProtoLogGroup;
/* loaded from: classes.dex */
public enum ShellProtoLogGroup implements IProtoLogGroup {
    WM_SHELL_TASK_ORG(true, true, false, "WindowManagerShell"),
    WM_SHELL_TRANSITIONS(true, true, true, "WindowManagerShell"),
    WM_SHELL_DRAG_AND_DROP(true, true, false, "WindowManagerShell"),
    WM_SHELL_STARTING_WINDOW(true, true, false, "ShellStartingWindow"),
    WM_SHELL_BACK_PREVIEW(true, true, true, "ShellBackPreview"),
    WM_SHELL_RECENT_TASKS(true, true, false, "WindowManagerShell"),
    TEST_GROUP(true, true, false, "WindowManagerShellProtoLogTest");
    
    private final boolean mEnabled;
    private volatile boolean mLogToLogcat;
    private volatile boolean mLogToProto;
    private final String mTag;

    public boolean isLogToAny() {
        if (this.mLogToLogcat || this.mLogToProto) {
            return true;
        }
        return false;
    }

    ShellProtoLogGroup(boolean z, boolean z2, boolean z3, String str) {
        this.mEnabled = z;
        this.mLogToProto = z2;
        this.mLogToLogcat = z3;
        this.mTag = str;
    }

    public void setLogToLogcat(boolean z) {
        this.mLogToLogcat = z;
    }

    public void setLogToProto(boolean z) {
        this.mLogToProto = z;
    }

    public String getTag() {
        return this.mTag;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    @Override // com.android.internal.protolog.common.IProtoLogGroup
    public boolean isLogToLogcat() {
        return this.mLogToLogcat;
    }

    @Override // com.android.internal.protolog.common.IProtoLogGroup
    public boolean isLogToProto() {
        return this.mLogToProto;
    }
}
