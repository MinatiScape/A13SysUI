package com.android.systemui;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final class InitController {
    public boolean mTasksExecuted = false;
    public final ArrayList<Runnable> mTasks = new ArrayList<>();
}
