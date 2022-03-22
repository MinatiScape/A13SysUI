package kotlinx.coroutines.flow.internal;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.internal.ScopeCoroutine;
/* compiled from: FlowCoroutine.kt */
/* loaded from: classes.dex */
public final class FlowCoroutine<T> extends ScopeCoroutine<T> {
    @Override // kotlinx.coroutines.JobSupport
    public final boolean childCancelled(Throwable th) {
        return cancelImpl$external__kotlinx_coroutines__android_common__kotlinx_coroutines(th);
    }

    public FlowCoroutine(CoroutineContext coroutineContext, Continuation<? super T> continuation) {
        super(coroutineContext, continuation);
    }
}
