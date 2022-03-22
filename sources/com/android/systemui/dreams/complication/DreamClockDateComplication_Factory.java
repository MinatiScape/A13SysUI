package com.android.systemui.dreams.complication;

import com.android.systemui.dreams.complication.dagger.DreamClockDateComplicationComponent$Factory;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DreamClockDateComplication_Factory implements Factory<DreamClockDateComplication> {
    public final Provider<DreamClockDateComplicationComponent$Factory> componentFactoryProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DreamClockDateComplication(this.componentFactoryProvider.mo144get());
    }

    public DreamClockDateComplication_Factory(Provider<DreamClockDateComplicationComponent$Factory> provider) {
        this.componentFactoryProvider = provider;
    }
}
