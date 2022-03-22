package kotlinx.coroutines;

import java.util.Objects;
/* compiled from: JobSupport.kt */
/* loaded from: classes.dex */
public abstract class JobNode extends CompletionHandlerBase implements DisposableHandle, Incomplete {
    public JobSupport job;

    @Override // kotlinx.coroutines.Incomplete
    public final NodeList getList() {
        return null;
    }

    @Override // kotlinx.coroutines.Incomplete
    public final boolean isActive() {
        return true;
    }

    public final JobSupport getJob() {
        JobSupport jobSupport = this.job;
        if (jobSupport != null) {
            return jobSupport;
        }
        return null;
    }

    @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
    public final String toString() {
        return DebugStringsKt.getClassSimpleName(this) + '@' + DebugStringsKt.getHexAddress(this) + "[job@" + DebugStringsKt.getHexAddress(getJob()) + ']';
    }

    @Override // kotlinx.coroutines.DisposableHandle
    public final void dispose() {
        Object state$external__kotlinx_coroutines__android_common__kotlinx_coroutines;
        JobSupport job = getJob();
        Objects.requireNonNull(job);
        do {
            state$external__kotlinx_coroutines__android_common__kotlinx_coroutines = job.getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
            if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof JobNode) {
                if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines != this) {
                    return;
                }
            } else if ((state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Incomplete) && ((Incomplete) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).getList() != null) {
                remove();
                return;
            } else {
                return;
            }
        } while (!job._state.compareAndSet(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines, JobSupportKt.EMPTY_ACTIVE));
    }
}
