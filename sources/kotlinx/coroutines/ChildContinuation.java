package kotlinx.coroutines;

import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.internal.DispatchedContinuation;
import kotlinx.coroutines.internal.DispatchedContinuationKt;
import kotlinx.coroutines.internal.Symbol;
/* compiled from: JobSupport.kt */
/* loaded from: classes.dex */
public final class ChildContinuation extends JobCancellingNode {
    public final CancellableContinuationImpl<?> child;

    @Override // kotlin.jvm.functions.Function1
    public final /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
        invoke2(th);
        return Unit.INSTANCE;
    }

    @Override // kotlinx.coroutines.CompletionHandlerBase
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(Throwable th) {
        boolean z;
        CancellableContinuationImpl<?> cancellableContinuationImpl = this.child;
        Throwable continuationCancellationCause = cancellableContinuationImpl.getContinuationCancellationCause(getJob());
        if (cancellableContinuationImpl.isReusable()) {
            DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) cancellableContinuationImpl.delegate;
            Objects.requireNonNull(dispatchedContinuation);
            AtomicRef<Object> atomicRef = dispatchedContinuation._reusableCancellableContinuation;
            while (true) {
                Objects.requireNonNull(atomicRef);
                Object obj = atomicRef.value;
                Symbol symbol = DispatchedContinuationKt.REUSABLE_CLAIMED;
                z = true;
                if (!Intrinsics.areEqual(obj, symbol)) {
                    if (obj instanceof Throwable) {
                        break;
                    } else if (dispatchedContinuation._reusableCancellableContinuation.compareAndSet(obj, null)) {
                        break;
                    }
                } else if (dispatchedContinuation._reusableCancellableContinuation.compareAndSet(symbol, continuationCancellationCause)) {
                    break;
                }
            }
        }
        z = false;
        if (!z) {
            cancellableContinuationImpl.cancel(continuationCancellationCause);
            if (!cancellableContinuationImpl.isReusable()) {
                cancellableContinuationImpl.detachChild$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
            }
        }
    }

    public ChildContinuation(CancellableContinuationImpl<?> cancellableContinuationImpl) {
        this.child = cancellableContinuationImpl;
    }
}
