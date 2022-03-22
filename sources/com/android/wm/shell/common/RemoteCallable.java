package com.android.wm.shell.common;

import android.content.Context;
/* loaded from: classes.dex */
public interface RemoteCallable<T> {
    Context getContext();

    ShellExecutor getRemoteCallExecutor();
}
