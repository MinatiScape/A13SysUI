package kotlinx.coroutines.scheduling;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import kotlinx.coroutines.DebugStringsKt;
/* compiled from: Tasks.kt */
/* loaded from: classes.dex */
public final class TaskImpl extends Task {
    public final Runnable block;

    @Override // java.lang.Runnable
    public final void run() {
        try {
            this.block.run();
        } finally {
            this.taskContext.afterTask();
        }
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Task[");
        m.append(DebugStringsKt.getClassSimpleName(this.block));
        m.append('@');
        m.append(DebugStringsKt.getHexAddress(this.block));
        m.append(", ");
        m.append(this.submissionTime);
        m.append(", ");
        m.append(this.taskContext);
        m.append(']');
        return m.toString();
    }

    public TaskImpl(Runnable runnable, long j, TaskContext taskContext) {
        super(j, taskContext);
        this.block = runnable;
    }
}
