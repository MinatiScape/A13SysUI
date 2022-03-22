package kotlinx.coroutines;

import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
/* compiled from: CompletionState.kt */
/* loaded from: classes.dex */
public final class CompletionStateKt {
    public static final <T> Object recoverResult(Object obj, Continuation<? super T> continuation) {
        if (!(obj instanceof CompletedExceptionally)) {
            return obj;
        }
        Throwable th = ((CompletedExceptionally) obj).cause;
        if (DebugKt.RECOVER_STACK_TRACES && (continuation instanceof CoroutineStackFrame)) {
            th = StackTraceRecoveryKt.access$recoverFromStackFrame(th, (CoroutineStackFrame) continuation);
        }
        return new Result.Failure(th);
    }
}
