package androidx.mediarouter;

import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.internal.ScopeCoroutine;
import kotlinx.coroutines.intrinsics.UndispatchedKt;
/* loaded from: classes.dex */
public final class R$dimen {
    public static final Object coroutineScope(Function2 function2, Continuation continuation) {
        ScopeCoroutine scopeCoroutine = new ScopeCoroutine(continuation.getContext(), continuation);
        return UndispatchedKt.startUndispatchedOrReturn(scopeCoroutine, scopeCoroutine, function2);
    }
}
