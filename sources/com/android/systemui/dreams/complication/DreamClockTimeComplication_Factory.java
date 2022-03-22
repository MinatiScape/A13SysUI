package com.android.systemui.dreams.complication;

import com.android.systemui.dreams.complication.dagger.DreamClockTimeComplicationComponent$Factory;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DreamClockTimeComplication_Factory implements Factory<DreamClockTimeComplication> {
    public final Provider<DreamClockTimeComplicationComponent$Factory> componentFactoryProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DreamClockTimeComplication(this.componentFactoryProvider.mo144get());
    }

    public DreamClockTimeComplication_Factory(Provider<DreamClockTimeComplicationComponent$Factory> provider) {
        this.componentFactoryProvider = provider;
    }
}
