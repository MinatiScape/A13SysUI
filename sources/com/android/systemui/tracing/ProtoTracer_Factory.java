package com.android.systemui.tracing;

import android.content.Context;
import android.hardware.display.AmbientDisplayConfiguration;
import com.android.systemui.dock.DockManager;
import com.android.systemui.doze.DozeDockHandler;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ProtoTracer_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;
    public final Provider dumpManagerProvider;

    public /* synthetic */ ProtoTracer_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.dumpManagerProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ProtoTracer((Context) this.contextProvider.mo144get(), (DumpManager) this.dumpManagerProvider.mo144get());
            default:
                return new DozeDockHandler((AmbientDisplayConfiguration) this.contextProvider.mo144get(), (DockManager) this.dumpManagerProvider.mo144get());
        }
    }
}
