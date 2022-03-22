package com.android.systemui.dreams.complication;

import android.content.Context;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.complication.DreamWeatherComplication;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DreamWeatherComplication_Registrant_Factory implements Factory<DreamWeatherComplication.Registrant> {
    public final Provider<Context> contextProvider;
    public final Provider<DreamOverlayStateController> dreamOverlayStateControllerProvider;
    public final Provider<DreamWeatherComplication> dreamWeatherComplicationProvider;
    public final Provider<LockscreenSmartspaceController> smartspaceControllerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DreamWeatherComplication.Registrant(this.contextProvider.mo144get(), this.smartspaceControllerProvider.mo144get(), this.dreamOverlayStateControllerProvider.mo144get(), this.dreamWeatherComplicationProvider.mo144get());
    }

    public DreamWeatherComplication_Registrant_Factory(Provider<Context> provider, Provider<LockscreenSmartspaceController> provider2, Provider<DreamOverlayStateController> provider3, Provider<DreamWeatherComplication> provider4) {
        this.contextProvider = provider;
        this.smartspaceControllerProvider = provider2;
        this.dreamOverlayStateControllerProvider = provider3;
        this.dreamWeatherComplicationProvider = provider4;
    }
}
