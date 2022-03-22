package kotlinx.coroutines.channels;

import kotlin.io.CloseableKt;
/* compiled from: Channel.kt */
/* loaded from: classes.dex */
public interface Channel<E> extends SendChannel<E>, ReceiveChannel<E> {
    public static final Factory Factory = Factory.$$INSTANCE;

    /* compiled from: Channel.kt */
    /* loaded from: classes.dex */
    public static final class Factory {
        public static final /* synthetic */ Factory $$INSTANCE = new Factory();
        public static final int CHANNEL_DEFAULT_CAPACITY = (int) CloseableKt.systemProp("kotlinx.coroutines.channels.defaultBuffer", 64, 1, 2147483646);
    }
}
