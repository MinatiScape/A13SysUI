package kotlinx.coroutines.internal;

import kotlinx.coroutines.android.HandlerContext;
/* compiled from: MainDispatcherFactory.kt */
/* loaded from: classes.dex */
public interface MainDispatcherFactory {
    HandlerContext createDispatcher$1();

    void getLoadPriority();

    void hintOnError();
}
