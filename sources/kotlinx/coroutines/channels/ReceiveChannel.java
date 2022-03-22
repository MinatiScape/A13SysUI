package kotlinx.coroutines.channels;

import java.util.concurrent.CancellationException;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.channels.AbstractChannel;
/* compiled from: Channel.kt */
/* loaded from: classes.dex */
public interface ReceiveChannel<E> {
    void cancel(CancellationException cancellationException);

    AbstractChannel.Itr iterator();

    /* renamed from: receiveCatching-JP2dKIU */
    Object mo183receiveCatchingJP2dKIU(Continuation<? super ChannelResult<? extends E>> continuation);

    /* renamed from: tryReceive-PtdJZtk */
    Object mo184tryReceivePtdJZtk();
}
