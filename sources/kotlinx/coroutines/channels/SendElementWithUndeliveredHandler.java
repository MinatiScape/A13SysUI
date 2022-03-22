package kotlinx.coroutines.channels;

import kotlin.Unit;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CoroutineExceptionHandlerKt;
import kotlinx.coroutines.internal.OnUndeliveredElementKt;
import kotlinx.coroutines.internal.UndeliveredElementException;
/* compiled from: AbstractChannel.kt */
/* loaded from: classes.dex */
public final class SendElementWithUndeliveredHandler<E> extends SendElement<E> {
    public final Function1<E, Unit> onUndeliveredElement;

    @Override // kotlinx.coroutines.channels.Send
    public final void undeliveredElement() {
        Function1<E, Unit> function1 = this.onUndeliveredElement;
        E e = this.pollResult;
        CoroutineContext context = this.cont.getContext();
        UndeliveredElementException callUndeliveredElementCatchingException = OnUndeliveredElementKt.callUndeliveredElementCatchingException(function1, e, null);
        if (callUndeliveredElementCatchingException != null) {
            CoroutineExceptionHandlerKt.handleCoroutineException(context, callUndeliveredElementCatchingException);
        }
    }

    public SendElementWithUndeliveredHandler(Object obj, CancellableContinuationImpl cancellableContinuationImpl, Function1 function1) {
        super(obj, cancellableContinuationImpl);
        this.onUndeliveredElement = function1;
    }

    @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
    public final boolean remove() {
        if (!super.remove()) {
            return false;
        }
        undeliveredElement();
        return true;
    }
}
