package kotlinx.coroutines.channels;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.internal.OnUndeliveredElementKt;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.internal.UndeliveredElementException;
/* compiled from: ConflatedChannel.kt */
/* loaded from: classes.dex */
public final class ConflatedChannel<E> extends AbstractChannel<E> {
    public final ReentrantLock lock = new ReentrantLock();
    public Object value = AbstractChannelKt.EMPTY;

    @Override // kotlinx.coroutines.channels.AbstractChannel
    public final boolean isBufferAlwaysEmpty() {
        return false;
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public final boolean isBufferAlwaysFull() {
        return false;
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public final boolean isBufferFull() {
        return false;
    }

    @Override // kotlinx.coroutines.channels.AbstractChannel
    public final boolean enqueueReceiveInternal(Receive<? super E> receive) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return super.enqueueReceiveInternal(receive);
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public final String getBufferDebugString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("(value=");
        m.append(this.value);
        m.append(')');
        return m.toString();
    }

    @Override // kotlinx.coroutines.channels.AbstractChannel
    public final boolean isBufferEmpty() {
        if (this.value == AbstractChannelKt.EMPTY) {
            return true;
        }
        return false;
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public final Object offerInternal(E e) {
        Function1<E, Unit> function1;
        ReceiveOrClosed<E> takeFirstReceiveOrPeekClosed;
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Closed<?> closedForSend = getClosedForSend();
            if (closedForSend != null) {
                return closedForSend;
            }
            if (this.value == AbstractChannelKt.EMPTY) {
                do {
                    takeFirstReceiveOrPeekClosed = takeFirstReceiveOrPeekClosed();
                    if (takeFirstReceiveOrPeekClosed != null) {
                        if (takeFirstReceiveOrPeekClosed instanceof Closed) {
                            return takeFirstReceiveOrPeekClosed;
                        }
                    }
                } while (takeFirstReceiveOrPeekClosed.tryResumeReceive(e) == null);
                boolean z = DebugKt.DEBUG;
                reentrantLock.unlock();
                takeFirstReceiveOrPeekClosed.completeResumeReceive(e);
                return takeFirstReceiveOrPeekClosed.getOfferResult();
            }
            Object obj = this.value;
            UndeliveredElementException undeliveredElementException = null;
            if (!(obj == AbstractChannelKt.EMPTY || (function1 = this.onUndeliveredElement) == null)) {
                undeliveredElementException = OnUndeliveredElementKt.callUndeliveredElementCatchingException(function1, obj, null);
            }
            this.value = e;
            if (undeliveredElementException == null) {
                return AbstractChannelKt.OFFER_SUCCESS;
            }
            throw undeliveredElementException;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX WARN: Finally extract failed */
    @Override // kotlinx.coroutines.channels.AbstractChannel
    public final void onCancelIdempotent(boolean z) {
        Function1<E, Unit> function1;
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Symbol symbol = AbstractChannelKt.EMPTY;
            Object obj = this.value;
            UndeliveredElementException undeliveredElementException = null;
            if (!(obj == symbol || (function1 = this.onUndeliveredElement) == null)) {
                undeliveredElementException = OnUndeliveredElementKt.callUndeliveredElementCatchingException(function1, obj, null);
            }
            this.value = symbol;
            reentrantLock.unlock();
            super.onCancelIdempotent(z);
            if (undeliveredElementException != null) {
                throw undeliveredElementException;
            }
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    @Override // kotlinx.coroutines.channels.AbstractChannel
    public final Object pollInternal() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object obj = this.value;
            Symbol symbol = AbstractChannelKt.EMPTY;
            if (obj == symbol) {
                Object closedForSend = getClosedForSend();
                if (closedForSend == null) {
                    closedForSend = AbstractChannelKt.POLL_FAILED;
                }
                return closedForSend;
            }
            this.value = symbol;
            return obj;
        } finally {
            reentrantLock.unlock();
        }
    }

    public ConflatedChannel(Function1<? super E, Unit> function1) {
        super(function1);
    }
}
