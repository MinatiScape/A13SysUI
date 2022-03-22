package kotlinx.coroutines;

import androidx.preference.R$color;
import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.internal.DispatchedContinuationKt;
/* compiled from: Builders.common.kt */
/* loaded from: classes.dex */
public final class LazyStandaloneCoroutine extends StandaloneCoroutine {
    public final Continuation<Unit> continuation;

    public LazyStandaloneCoroutine(CoroutineContext coroutineContext, Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> function2) {
        super(coroutineContext, false);
        this.continuation = R$color.createCoroutineUnintercepted(function2, this, this);
    }

    @Override // kotlinx.coroutines.JobSupport
    public final void onStart() {
        try {
            DispatchedContinuationKt.resumeCancellableWith(R$color.intercepted(this.continuation), Unit.INSTANCE, null);
        } catch (Throwable th) {
            resumeWith(new Result.Failure(th));
            throw th;
        }
    }
}
