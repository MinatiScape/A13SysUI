package com.android.systemui.util.concurrency;
/* compiled from: PendingTasksContainer.kt */
/* loaded from: classes.dex */
public final class PendingTasksContainer$registerTask$1 implements Runnable {
    public final /* synthetic */ PendingTasksContainer this$0;

    public PendingTasksContainer$registerTask$1(PendingTasksContainer pendingTasksContainer) {
        this.this$0 = pendingTasksContainer;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Runnable andSet;
        if (this.this$0.pendingTasksCount.decrementAndGet() == 0 && (andSet = this.this$0.completionCallback.getAndSet(null)) != null) {
            andSet.run();
        }
    }
}
