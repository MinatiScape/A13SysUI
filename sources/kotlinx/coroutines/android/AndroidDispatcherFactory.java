package kotlinx.coroutines.android;

import android.os.Looper;
import kotlinx.coroutines.internal.MainDispatcherFactory;
/* compiled from: HandlerDispatcher.kt */
/* loaded from: classes.dex */
public final class AndroidDispatcherFactory implements MainDispatcherFactory {
    @Override // kotlinx.coroutines.internal.MainDispatcherFactory
    public final void getLoadPriority() {
    }

    @Override // kotlinx.coroutines.internal.MainDispatcherFactory
    public final void hintOnError() {
    }

    @Override // kotlinx.coroutines.internal.MainDispatcherFactory
    public final HandlerContext createDispatcher$1() {
        return new HandlerContext(HandlerDispatcherKt.asHandler(Looper.getMainLooper(), true));
    }
}
