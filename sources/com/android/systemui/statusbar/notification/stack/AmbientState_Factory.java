package com.android.systemui.statusbar.notification.stack;

import android.content.Context;
import com.android.systemui.qs.QuickQSPanelController_Factory;
import com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AmbientState_Factory implements Factory<AmbientState> {
    public final Provider<StackScrollAlgorithm.BypassController> bypassControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<StackScrollAlgorithm.SectionProvider> sectionProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new AmbientState(this.contextProvider.mo144get(), this.sectionProvider.mo144get(), this.bypassControllerProvider.mo144get());
    }

    public AmbientState_Factory(Provider provider, QuickQSPanelController_Factory quickQSPanelController_Factory, Provider provider2) {
        this.contextProvider = provider;
        this.sectionProvider = quickQSPanelController_Factory;
        this.bypassControllerProvider = provider2;
    }
}
