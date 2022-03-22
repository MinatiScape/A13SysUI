package com.android.systemui.dreams.complication;

import android.content.Context;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.complication.DreamClockDateComplication;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DreamClockDateComplication_Registrant_Factory implements Factory<DreamClockDateComplication.Registrant> {
    public final Provider<Context> contextProvider;
    public final Provider<DreamClockDateComplication> dreamClockDateComplicationProvider;
    public final Provider<DreamOverlayStateController> dreamOverlayStateControllerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DreamClockDateComplication.Registrant(this.contextProvider.mo144get(), this.dreamOverlayStateControllerProvider.mo144get(), this.dreamClockDateComplicationProvider.mo144get());
    }

    public DreamClockDateComplication_Registrant_Factory(Provider<Context> provider, Provider<DreamOverlayStateController> provider2, Provider<DreamClockDateComplication> provider3) {
        this.contextProvider = provider;
        this.dreamOverlayStateControllerProvider = provider2;
        this.dreamClockDateComplicationProvider = provider3;
    }
}
