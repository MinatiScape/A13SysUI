package com.android.systemui.dreams;

import android.content.Context;
import com.android.systemui.dreams.SmartSpaceComplication;
import com.android.systemui.statusbar.VibratorHelper_Factory;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SmartSpaceComplication_Registrant_Factory implements Factory<SmartSpaceComplication.Registrant> {
    public final Provider<Context> contextProvider;
    public final Provider<DreamOverlayStateController> dreamOverlayStateControllerProvider;
    public final Provider<SmartSpaceComplication> smartSpaceComplicationProvider;
    public final Provider<LockscreenSmartspaceController> smartSpaceControllerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new SmartSpaceComplication.Registrant(this.contextProvider.mo144get(), this.dreamOverlayStateControllerProvider.mo144get(), this.smartSpaceComplicationProvider.mo144get(), this.smartSpaceControllerProvider.mo144get());
    }

    public SmartSpaceComplication_Registrant_Factory(Provider provider, Provider provider2, VibratorHelper_Factory vibratorHelper_Factory, Provider provider3) {
        this.contextProvider = provider;
        this.dreamOverlayStateControllerProvider = provider2;
        this.smartSpaceComplicationProvider = vibratorHelper_Factory;
        this.smartSpaceControllerProvider = provider3;
    }
}
