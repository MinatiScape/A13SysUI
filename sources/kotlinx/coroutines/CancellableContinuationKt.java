package kotlinx.coroutines;

import java.util.Objects;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.internal.DispatchedContinuation;
import kotlinx.coroutines.internal.DispatchedContinuationKt;
/* compiled from: CancellableContinuation.kt */
/* loaded from: classes.dex */
public final class CancellableContinuationKt {
    public static final <T> CancellableContinuationImpl<T> getOrCreateCancellableContinuation(Continuation<? super T> continuation) {
        CancellableContinuationImpl<T> cancellableContinuationImpl;
        CancellableContinuationImpl<T> cancellableContinuationImpl2;
        boolean z = true;
        if (!(continuation instanceof DispatchedContinuation)) {
            return new CancellableContinuationImpl<>(continuation, 1);
        }
        DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) continuation;
        AtomicRef<Object> atomicRef = dispatchedContinuation._reusableCancellableContinuation;
        while (true) {
            Objects.requireNonNull(atomicRef);
            Object obj = atomicRef.value;
            cancellableContinuationImpl = null;
            if (obj == null) {
                dispatchedContinuation._reusableCancellableContinuation.setValue(DispatchedContinuationKt.REUSABLE_CLAIMED);
                cancellableContinuationImpl2 = null;
                break;
            } else if (obj instanceof CancellableContinuationImpl) {
                if (dispatchedContinuation._reusableCancellableContinuation.compareAndSet(obj, DispatchedContinuationKt.REUSABLE_CLAIMED)) {
                    cancellableContinuationImpl2 = (CancellableContinuationImpl) obj;
                    break;
                }
            } else if (obj != DispatchedContinuationKt.REUSABLE_CLAIMED && !(obj instanceof Throwable)) {
                throw new IllegalStateException(Intrinsics.stringPlus("Inconsistent state ", obj).toString());
            }
        }
        if (cancellableContinuationImpl2 != null) {
            boolean z2 = DebugKt.DEBUG;
            AtomicRef<Object> atomicRef2 = cancellableContinuationImpl2._state;
            Objects.requireNonNull(atomicRef2);
            Object obj2 = atomicRef2.value;
            if (!(obj2 instanceof CompletedContinuation) || ((CompletedContinuation) obj2).idempotentResume == null) {
                cancellableContinuationImpl2._decision.setValue(0);
                cancellableContinuationImpl2._state.setValue(Active.INSTANCE);
            } else {
                cancellableContinuationImpl2.detachChild$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
                z = false;
            }
            if (z) {
                cancellableContinuationImpl = cancellableContinuationImpl2;
            }
        }
        if (cancellableContinuationImpl == null) {
            return new CancellableContinuationImpl<>(continuation, 2);
        }
        return cancellableContinuationImpl;
    }
}
