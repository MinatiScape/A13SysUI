package com.android.systemui.media.dream.dagger;

import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class MediaComplicationComponent_MediaComplicationModule_ProvideLayoutParamsFactory implements Factory<ComplicationLayoutParams> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final MediaComplicationComponent_MediaComplicationModule_ProvideLayoutParamsFactory INSTANCE = new MediaComplicationComponent_MediaComplicationModule_ProvideLayoutParamsFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ComplicationLayoutParams(0, 5, 2, 0, true);
    }
}
