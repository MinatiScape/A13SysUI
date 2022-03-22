package com.android.systemui.dreams.complication.dagger;

import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class DreamWeatherComplicationComponent_DreamWeatherComplicationModule_ProvideLayoutParamsFactory implements Factory<ComplicationLayoutParams> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final DreamWeatherComplicationComponent_DreamWeatherComplicationModule_ProvideLayoutParamsFactory INSTANCE = new DreamWeatherComplicationComponent_DreamWeatherComplicationModule_ProvideLayoutParamsFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ComplicationLayoutParams(6, 8, 1);
    }
}
