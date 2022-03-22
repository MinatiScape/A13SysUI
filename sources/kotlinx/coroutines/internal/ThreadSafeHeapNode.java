package kotlinx.coroutines.internal;

import kotlinx.coroutines.EventLoopImplBase;
/* compiled from: ThreadSafeHeap.kt */
/* loaded from: classes.dex */
public interface ThreadSafeHeapNode {
    void setHeap(EventLoopImplBase.DelayedTaskQueue delayedTaskQueue);

    void setIndex(int i);
}
