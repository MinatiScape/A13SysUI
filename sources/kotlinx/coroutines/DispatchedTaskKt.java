package kotlinx.coroutines;

import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.internal.DispatchedContinuation;
import kotlinx.coroutines.internal.ThreadContextKt;
/* compiled from: DispatchedTask.kt */
/* loaded from: classes.dex */
public final class DispatchedTaskKt {
    public static final <T> void resume(DispatchedTask<? super T> dispatchedTask, Continuation<? super T> continuation, boolean z) {
        Object obj;
        UndispatchedCoroutine<?> undispatchedCoroutine;
        Object takeState$external__kotlinx_coroutines__android_common__kotlinx_coroutines = dispatchedTask.takeState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        Throwable exceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines = dispatchedTask.getExceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(takeState$external__kotlinx_coroutines__android_common__kotlinx_coroutines);
        if (exceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines != null) {
            obj = new Result.Failure(exceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines);
        } else {
            obj = dispatchedTask.getSuccessfulResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(takeState$external__kotlinx_coroutines__android_common__kotlinx_coroutines);
        }
        if (z) {
            DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) continuation;
            Continuation<T> continuation2 = dispatchedContinuation.continuation;
            Object obj2 = dispatchedContinuation.countOrElement;
            CoroutineContext context = continuation2.getContext();
            Object updateThreadContext = ThreadContextKt.updateThreadContext(context, obj2);
            if (updateThreadContext != ThreadContextKt.NO_THREAD_ELEMENTS) {
                undispatchedCoroutine = CoroutineContextKt.updateUndispatchedCompletion(continuation2, context, updateThreadContext);
            } else {
                undispatchedCoroutine = null;
            }
            try {
                dispatchedContinuation.continuation.resumeWith(obj);
            } finally {
                if (undispatchedCoroutine == null || undispatchedCoroutine.clearThreadContext()) {
                    ThreadContextKt.restoreThreadContext(context, updateThreadContext);
                }
            }
        } else {
            continuation.resumeWith(obj);
        }
    }
}
