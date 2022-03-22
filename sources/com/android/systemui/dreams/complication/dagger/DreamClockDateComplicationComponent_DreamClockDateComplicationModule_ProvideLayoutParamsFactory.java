package com.android.systemui.dreams.complication.dagger;

import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class DreamClockDateComplicationComponent_DreamClockDateComplicationModule_ProvideLayoutParamsFactory implements Factory<ComplicationLayoutParams> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final DreamClockDateComplicationComponent_DreamClockDateComplicationModule_ProvideLayoutParamsFactory INSTANCE = new DreamClockDateComplicationComponent_DreamClockDateComplicationModule_ProvideLayoutParamsFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ComplicationLayoutParams(6, 8, 2);
    }
}
