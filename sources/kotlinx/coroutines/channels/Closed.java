package kotlinx.coroutines.channels;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.lifecycle.runtime.R$id;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.DebugStringsKt;
import kotlinx.coroutines.internal.Symbol;
/* compiled from: AbstractChannel.kt */
/* loaded from: classes.dex */
public final class Closed<E> extends Send implements ReceiveOrClosed<E> {
    public final Throwable closeCause;

    @Override // kotlinx.coroutines.channels.ReceiveOrClosed
    public final void completeResumeReceive(E e) {
    }

    @Override // kotlinx.coroutines.channels.Send
    public final void completeResumeSend() {
    }

    @Override // kotlinx.coroutines.channels.ReceiveOrClosed
    public final Object getOfferResult() {
        return this;
    }

    @Override // kotlinx.coroutines.channels.Send
    public final Object getPollResult() {
        return this;
    }

    public final Throwable getReceiveException() {
        Throwable th = this.closeCause;
        if (th == null) {
            return new ClosedReceiveChannelException("Channel was closed");
        }
        return th;
    }

    @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Closed@");
        m.append(DebugStringsKt.getHexAddress(this));
        m.append('[');
        m.append(this.closeCause);
        m.append(']');
        return m.toString();
    }

    public Closed(Throwable th) {
        this.closeCause = th;
    }

    @Override // kotlinx.coroutines.channels.Send
    public final void resumeSendClosed(Closed<?> closed) {
        boolean z = DebugKt.DEBUG;
    }

    @Override // kotlinx.coroutines.channels.ReceiveOrClosed
    public final Symbol tryResumeReceive(Object obj) {
        return R$id.RESUME_TOKEN;
    }

    @Override // kotlinx.coroutines.channels.Send
    public final Symbol tryResumeSend() {
        return R$id.RESUME_TOKEN;
    }
}
