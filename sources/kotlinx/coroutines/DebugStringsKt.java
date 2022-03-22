package kotlinx.coroutines;

import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.internal.DispatchedContinuation;
/* compiled from: DebugStrings.kt */
/* loaded from: classes.dex */
public final class DebugStringsKt {
    public static final String toDebugString(Continuation<?> continuation) {
        Object obj;
        if (continuation instanceof DispatchedContinuation) {
            return continuation.toString();
        }
        try {
            obj = continuation + '@' + getHexAddress(continuation);
        } catch (Throwable th) {
            obj = new Result.Failure(th);
        }
        if (Result.m181exceptionOrNullimpl(obj) != null) {
            obj = ((Object) continuation.getClass().getName()) + '@' + getHexAddress(continuation);
        }
        return (String) obj;
    }

    public static final String getClassSimpleName(Object obj) {
        return obj.getClass().getSimpleName();
    }

    public static final String getHexAddress(Object obj) {
        return Integer.toHexString(System.identityHashCode(obj));
    }
}
