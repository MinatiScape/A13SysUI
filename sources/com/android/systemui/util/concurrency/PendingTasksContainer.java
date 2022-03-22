package com.android.systemui.util.concurrency;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
/* compiled from: PendingTasksContainer.kt */
/* loaded from: classes.dex */
public final class PendingTasksContainer {
    public AtomicInteger pendingTasksCount = new AtomicInteger(0);
    public AtomicReference<Runnable> completionCallback = new AtomicReference<>();
}
