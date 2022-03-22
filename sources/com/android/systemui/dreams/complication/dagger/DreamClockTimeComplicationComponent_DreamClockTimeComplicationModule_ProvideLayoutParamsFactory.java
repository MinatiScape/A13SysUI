package com.android.systemui.dreams.complication.dagger;

import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class DreamClockTimeComplicationComponent_DreamClockTimeComplicationModule_ProvideLayoutParamsFactory implements Factory<ComplicationLayoutParams> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final DreamClockTimeComplicationComponent_DreamClockTimeComplicationModule_ProvideLayoutParamsFactory INSTANCE = new DreamClockTimeComplicationComponent_DreamClockTimeComplicationModule_ProvideLayoutParamsFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ComplicationLayoutParams(6, 1, 0);
    }
}
