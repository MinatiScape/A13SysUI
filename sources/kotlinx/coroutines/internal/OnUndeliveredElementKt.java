package kotlinx.coroutines.internal;

import kotlin.ExceptionsKt;
import kotlin.Unit;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: OnUndeliveredElement.kt */
/* loaded from: classes.dex */
public final class OnUndeliveredElementKt {
    public static final <E> Function1<Throwable, Unit> bindCancellationFun(Function1<? super E, Unit> function1, E e, CoroutineContext coroutineContext) {
        return new OnUndeliveredElementKt$bindCancellationFun$1(function1, e, coroutineContext);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <E> UndeliveredElementException callUndeliveredElementCatchingException(Function1<? super E, Unit> function1, E e, UndeliveredElementException undeliveredElementException) {
        try {
            function1.invoke(e);
        } catch (Throwable th) {
            if (undeliveredElementException == null || undeliveredElementException.getCause() == th) {
                return new UndeliveredElementException(Intrinsics.stringPlus("Exception in undelivered element handler for ", e), th);
            }
            ExceptionsKt.addSuppressed(undeliveredElementException, th);
        }
        return undeliveredElementException;
    }
}
