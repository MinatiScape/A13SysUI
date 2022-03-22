package kotlin.coroutines;

import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlinx.coroutines.internal.DispatchedContinuation;
/* compiled from: ContinuationInterceptor.kt */
/* loaded from: classes.dex */
public interface ContinuationInterceptor extends CoroutineContext.Element {
    public static final /* synthetic */ int $r8$clinit = 0;

    /* compiled from: ContinuationInterceptor.kt */
    /* loaded from: classes.dex */
    public static final class Key implements CoroutineContext.Key<ContinuationInterceptor> {
        public static final /* synthetic */ Key $$INSTANCE = new Key();
    }

    DispatchedContinuation interceptContinuation(ContinuationImpl continuationImpl);

    void releaseInterceptedContinuation(Continuation<?> continuation);
}
