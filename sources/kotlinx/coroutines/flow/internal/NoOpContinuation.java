package kotlinx.coroutines.flow.internal;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
/* compiled from: SafeCollector.kt */
/* loaded from: classes.dex */
public final class NoOpContinuation implements Continuation<Object> {
    public static final NoOpContinuation INSTANCE = new NoOpContinuation();
    public static final EmptyCoroutineContext context = EmptyCoroutineContext.INSTANCE;

    @Override // kotlin.coroutines.Continuation
    public final void resumeWith(Object obj) {
    }

    @Override // kotlin.coroutines.Continuation
    public final CoroutineContext getContext() {
        return context;
    }
}
