package kotlinx.coroutines;

import java.util.concurrent.CancellationException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
/* compiled from: Job.kt */
/* loaded from: classes.dex */
public interface Job extends CoroutineContext.Element {

    /* compiled from: Job.kt */
    /* loaded from: classes.dex */
    public static final class DefaultImpls {
        public static /* synthetic */ DisposableHandle invokeOnCompletion$default(Job job, boolean z, JobNode jobNode, int i) {
            boolean z2 = false;
            if ((i & 1) != 0) {
                z = false;
            }
            if ((i & 2) != 0) {
                z2 = true;
            }
            return job.invokeOnCompletion(z, z2, jobNode);
        }
    }

    /* compiled from: Job.kt */
    /* loaded from: classes.dex */
    public static final class Key implements CoroutineContext.Key<Job> {
        public static final /* synthetic */ Key $$INSTANCE = new Key();
    }

    ChildHandle attachChild(JobSupport jobSupport);

    void cancel(CancellationException cancellationException);

    CancellationException getCancellationException();

    DisposableHandle invokeOnCompletion(boolean z, boolean z2, JobNode jobNode);

    boolean isActive();

    Object join(Continuation<? super Unit> continuation);

    boolean start();
}
