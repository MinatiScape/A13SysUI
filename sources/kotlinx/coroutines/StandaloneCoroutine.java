package kotlinx.coroutines;

import kotlin.Unit;
import kotlin.coroutines.CoroutineContext;
/* compiled from: Builders.common.kt */
/* loaded from: classes.dex */
public class StandaloneCoroutine extends AbstractCoroutine<Unit> {
    @Override // kotlinx.coroutines.JobSupport
    public final boolean handleJobException(Throwable th) {
        CoroutineExceptionHandlerKt.handleCoroutineException(this.context, th);
        return true;
    }

    public StandaloneCoroutine(CoroutineContext coroutineContext, boolean z) {
        super(coroutineContext, z);
    }
}
