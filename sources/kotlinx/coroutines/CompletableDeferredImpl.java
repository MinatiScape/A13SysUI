package kotlinx.coroutines;

import kotlin.Unit;
/* compiled from: CompletableDeferred.kt */
/* loaded from: classes.dex */
public final class CompletableDeferredImpl<T> extends JobSupport implements CompletableDeferred<T> {
    public CompletableDeferredImpl(Job job) {
        super(true);
        initParentJob(job);
    }

    @Override // kotlinx.coroutines.CompletableDeferred
    public final boolean complete() {
        Object tryMakeCompleting;
        Unit unit = Unit.INSTANCE;
        do {
            tryMakeCompleting = tryMakeCompleting(getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines(), unit);
            if (tryMakeCompleting == JobSupportKt.COMPLETING_ALREADY) {
                return false;
            }
            if (tryMakeCompleting == JobSupportKt.COMPLETING_WAITING_CHILDREN) {
                break;
            }
        } while (tryMakeCompleting == JobSupportKt.COMPLETING_RETRY);
        return true;
    }
}
