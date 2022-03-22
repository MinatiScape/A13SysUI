package com.android.systemui.dreams.complication;

import com.android.systemui.dreams.complication.dagger.DreamWeatherComplicationComponent$Factory;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DreamWeatherComplication_Factory implements Factory<DreamWeatherComplication> {
    public final Provider<DreamWeatherComplicationComponent$Factory> componentFactoryProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DreamWeatherComplication(this.componentFactoryProvider.mo144get());
    }

    public DreamWeatherComplication_Factory(Provider<DreamWeatherComplicationComponent$Factory> provider) {
        this.componentFactoryProvider = provider;
    }
}
