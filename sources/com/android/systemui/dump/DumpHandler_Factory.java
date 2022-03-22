package com.android.systemui.dump;

import android.content.Context;
import com.android.systemui.CoreStartable;
import dagger.internal.Factory;
import java.util.Map;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DumpHandler_Factory implements Factory<DumpHandler> {
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<LogBufferEulogizer> logBufferEulogizerProvider;
    public final Provider<Map<Class<?>, Provider<CoreStartable>>> startablesProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DumpHandler(this.contextProvider.mo144get(), this.dumpManagerProvider.mo144get(), this.logBufferEulogizerProvider.mo144get(), this.startablesProvider.mo144get());
    }

    public DumpHandler_Factory(Provider<Context> provider, Provider<DumpManager> provider2, Provider<LogBufferEulogizer> provider3, Provider<Map<Class<?>, Provider<CoreStartable>>> provider4) {
        this.contextProvider = provider;
        this.dumpManagerProvider = provider2;
        this.logBufferEulogizerProvider = provider3;
        this.startablesProvider = provider4;
    }
}
