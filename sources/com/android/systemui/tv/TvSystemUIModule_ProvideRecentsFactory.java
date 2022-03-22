package com.android.systemui.tv;

import android.content.Context;
import com.android.systemui.communal.CommunalSourceMonitor_Factory;
import com.android.systemui.recents.Recents;
import com.android.systemui.recents.RecentsImplementation;
import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TvSystemUIModule_ProvideRecentsFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider commandQueueProvider;
    public final Provider contextProvider;
    public final Provider recentsImplementationProvider;

    public /* synthetic */ TvSystemUIModule_ProvideRecentsFactory(Provider provider, CommunalSourceMonitor_Factory communalSourceMonitor_Factory, Provider provider2, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.recentsImplementationProvider = communalSourceMonitor_Factory;
        this.commandQueueProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Recents mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new Recents((Context) this.contextProvider.mo144get(), (RecentsImplementation) this.recentsImplementationProvider.mo144get(), (CommandQueue) this.commandQueueProvider.mo144get());
            default:
                return new Recents((Context) this.contextProvider.mo144get(), (RecentsImplementation) this.recentsImplementationProvider.mo144get(), (CommandQueue) this.commandQueueProvider.mo144get());
        }
    }
}
