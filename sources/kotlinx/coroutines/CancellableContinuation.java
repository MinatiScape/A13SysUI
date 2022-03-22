package kotlinx.coroutines;

import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.internal.Symbol;
/* compiled from: CancellableContinuation.kt */
/* loaded from: classes.dex */
public interface CancellableContinuation<T> extends Continuation<T> {
    void completeResume();

    Symbol tryResume(Object obj, Object obj2);

    Symbol tryResume(Object obj, Function1 function1);

    Symbol tryResumeWithException(Throwable th);
}
