package kotlinx.coroutines.channels;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.core.graphics.Insets$$ExternalSyntheticOutline0;
import androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicInt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.internal.OnUndeliveredElementKt;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.internal.UndeliveredElementException;
/* compiled from: ArrayChannel.kt */
/* loaded from: classes.dex */
public final class ArrayChannel<E> extends AbstractChannel<E> {
    public Object[] buffer;
    public final int capacity;
    public int head;
    public final ReentrantLock lock;
    public final BufferOverflow onBufferOverflow;
    public final AtomicInt size;

    @Override // kotlinx.coroutines.channels.AbstractChannel
    public final boolean isBufferAlwaysEmpty() {
        return false;
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public final boolean isBufferAlwaysFull() {
        return false;
    }

    public final void enqueueElement(int i, E e) {
        int i2 = this.capacity;
        if (i < i2) {
            Object[] objArr = this.buffer;
            if (i >= objArr.length) {
                int min = Math.min(objArr.length * 2, i2);
                Object[] objArr2 = new Object[min];
                for (int i3 = 0; i3 < i; i3++) {
                    Object[] objArr3 = this.buffer;
                    objArr2[i3] = objArr3[(this.head + i3) % objArr3.length];
                }
                Arrays.fill(objArr2, i, min, AbstractChannelKt.EMPTY);
                this.buffer = objArr2;
                this.head = 0;
            }
            Object[] objArr4 = this.buffer;
            objArr4[(this.head + i) % objArr4.length] = e;
            return;
        }
        boolean z = DebugKt.DEBUG;
        Object[] objArr5 = this.buffer;
        int i4 = this.head;
        objArr5[i4 % objArr5.length] = null;
        objArr5[(i + i4) % objArr5.length] = e;
        this.head = (i4 + 1) % objArr5.length;
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
    public final Object enqueueSend(SendElement sendElement) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return super.enqueueSend(sendElement);
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public final String getBufferDebugString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("(buffer:capacity=");
        m.append(this.capacity);
        m.append(",size=");
        AtomicInt atomicInt = this.size;
        Objects.requireNonNull(atomicInt);
        return Insets$$ExternalSyntheticOutline0.m(m, atomicInt.value, ')');
    }

    @Override // kotlinx.coroutines.channels.AbstractChannel
    public final boolean isBufferEmpty() {
        AtomicInt atomicInt = this.size;
        Objects.requireNonNull(atomicInt);
        if (atomicInt.value == 0) {
            return true;
        }
        return false;
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public final boolean isBufferFull() {
        AtomicInt atomicInt = this.size;
        Objects.requireNonNull(atomicInt);
        if (atomicInt.value == this.capacity && this.onBufferOverflow == BufferOverflow.SUSPEND) {
            return true;
        }
        return false;
    }

    @Override // kotlinx.coroutines.channels.AbstractChannel
    public final boolean isClosedForReceive() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return super.isClosedForReceive();
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public final Object offerInternal(E e) {
        ReceiveOrClosed<E> takeFirstReceiveOrPeekClosed;
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            AtomicInt atomicInt = this.size;
            Objects.requireNonNull(atomicInt);
            int i = atomicInt.value;
            Closed<?> closedForSend = getClosedForSend();
            if (closedForSend != null) {
                return closedForSend;
            }
            Symbol symbol = null;
            if (i < this.capacity) {
                this.size.setValue(i + 1);
            } else {
                int ordinal = this.onBufferOverflow.ordinal();
                if (ordinal == 0) {
                    symbol = AbstractChannelKt.OFFER_FAILED;
                } else if (ordinal != 1) {
                    if (ordinal == 2) {
                        symbol = AbstractChannelKt.OFFER_SUCCESS;
                    } else {
                        throw new NoWhenBranchMatchedException();
                    }
                }
            }
            if (symbol != null) {
                return symbol;
            }
            if (i == 0) {
                do {
                    takeFirstReceiveOrPeekClosed = takeFirstReceiveOrPeekClosed();
                    if (takeFirstReceiveOrPeekClosed != null) {
                        if (takeFirstReceiveOrPeekClosed instanceof Closed) {
                            this.size.setValue(i);
                            return takeFirstReceiveOrPeekClosed;
                        }
                    }
                } while (takeFirstReceiveOrPeekClosed.tryResumeReceive(e) == null);
                boolean z = DebugKt.DEBUG;
                this.size.setValue(i);
                reentrantLock.unlock();
                takeFirstReceiveOrPeekClosed.completeResumeReceive(e);
                return takeFirstReceiveOrPeekClosed.getOfferResult();
            }
            enqueueElement(i, e);
            return AbstractChannelKt.OFFER_SUCCESS;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX WARN: Finally extract failed */
    @Override // kotlinx.coroutines.channels.AbstractChannel
    public final void onCancelIdempotent(boolean z) {
        Function1<E, Unit> function1 = this.onUndeliveredElement;
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            AtomicInt atomicInt = this.size;
            Objects.requireNonNull(atomicInt);
            int i = atomicInt.value;
            UndeliveredElementException undeliveredElementException = null;
            int i2 = 0;
            while (i2 < i) {
                i2++;
                Object obj = this.buffer[this.head];
                if (!(function1 == null || obj == AbstractChannelKt.EMPTY)) {
                    undeliveredElementException = OnUndeliveredElementKt.callUndeliveredElementCatchingException(function1, obj, undeliveredElementException);
                }
                Object[] objArr = this.buffer;
                int i3 = this.head;
                objArr[i3] = AbstractChannelKt.EMPTY;
                this.head = (i3 + 1) % objArr.length;
            }
            this.size.setValue(0);
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
            AtomicInt atomicInt = this.size;
            Objects.requireNonNull(atomicInt);
            int i = atomicInt.value;
            if (i == 0) {
                Object closedForSend = getClosedForSend();
                if (closedForSend == null) {
                    closedForSend = AbstractChannelKt.POLL_FAILED;
                }
                return closedForSend;
            }
            Object[] objArr = this.buffer;
            int i2 = this.head;
            Object obj = objArr[i2];
            Send send = null;
            objArr[i2] = null;
            this.size.setValue(i - 1);
            Object obj2 = AbstractChannelKt.POLL_FAILED;
            boolean z = false;
            if (i == this.capacity) {
                while (true) {
                    Send takeFirstSendOrPeekClosed = takeFirstSendOrPeekClosed();
                    if (takeFirstSendOrPeekClosed == null) {
                        break;
                    } else if (takeFirstSendOrPeekClosed.tryResumeSend() != null) {
                        boolean z2 = DebugKt.DEBUG;
                        obj2 = takeFirstSendOrPeekClosed.getPollResult();
                        send = takeFirstSendOrPeekClosed;
                        z = true;
                        break;
                    } else {
                        takeFirstSendOrPeekClosed.undeliveredElement();
                        send = takeFirstSendOrPeekClosed;
                    }
                }
            }
            if (obj2 != AbstractChannelKt.POLL_FAILED && !(obj2 instanceof Closed)) {
                this.size.setValue(i);
                Object[] objArr2 = this.buffer;
                objArr2[(this.head + i) % objArr2.length] = obj2;
            }
            this.head = (this.head + 1) % this.buffer.length;
            if (z) {
                Intrinsics.checkNotNull(send);
                send.completeResumeSend();
            }
            return obj;
        } finally {
            reentrantLock.unlock();
        }
    }

    public ArrayChannel(int i, BufferOverflow bufferOverflow, Function1<? super E, Unit> function1) {
        super(function1);
        this.capacity = i;
        this.onBufferOverflow = bufferOverflow;
        if (i < 1 ? false : true) {
            this.lock = new ReentrantLock();
            int min = Math.min(i, 8);
            Object[] objArr = new Object[min];
            Arrays.fill(objArr, 0, min, AbstractChannelKt.EMPTY);
            this.buffer = objArr;
            this.size = new AtomicInt();
            return;
        }
        throw new IllegalArgumentException(ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("ArrayChannel capacity must be at least 1, but ", i, " was specified").toString());
    }
}
