package kotlinx.coroutines;
/* compiled from: Supervisor.kt */
/* loaded from: classes.dex */
public final class SupervisorJobImpl extends JobImpl {
    @Override // kotlinx.coroutines.JobSupport
    public final boolean childCancelled(Throwable th) {
        return false;
    }

    public SupervisorJobImpl(Job job) {
        super(job);
    }
}
