package com.google.android.systemui.communal.dock.callbacks.mediashell.dagger;

import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class MediaShellModule_ProvidesComplicationLayoutParamsFactory implements Factory<ComplicationLayoutParams> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final MediaShellModule_ProvidesComplicationLayoutParamsFactory INSTANCE = new MediaShellModule_ProvidesComplicationLayoutParamsFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ComplicationLayoutParams(-2, 6, 8, 0, true);
    }
}
