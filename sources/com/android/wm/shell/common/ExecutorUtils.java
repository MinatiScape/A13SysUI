package com.android.wm.shell.common;

import android.util.Slog;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class ExecutorUtils {
    public static <T> void executeRemoteCallWithTaskPermission(RemoteCallable<T> remoteCallable, String str, Consumer<T> consumer, boolean z) {
        if (remoteCallable != null) {
            remoteCallable.getContext().enforceCallingPermission("android.permission.MANAGE_ACTIVITY_TASKS", str);
            if (z) {
                try {
                    remoteCallable.getRemoteCallExecutor().executeBlocking$1(new ExecutorUtils$$ExternalSyntheticLambda0(consumer, remoteCallable, 0));
                } catch (InterruptedException e) {
                    Slog.e("ExecutorUtils", "Remote call failed", e);
                }
            } else {
                remoteCallable.getRemoteCallExecutor().execute(new ExecutorUtils$$ExternalSyntheticLambda1(consumer, remoteCallable, 0));
            }
        }
    }
}
