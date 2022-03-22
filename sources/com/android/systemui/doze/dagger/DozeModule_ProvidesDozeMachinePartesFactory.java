package com.android.systemui.doze.dagger;

import com.android.systemui.doze.DozeAuthRemover;
import com.android.systemui.doze.DozeDockHandler;
import com.android.systemui.doze.DozeFalsingManagerAdapter;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.DozePauser;
import com.android.systemui.doze.DozeScreenBrightness;
import com.android.systemui.doze.DozeScreenState;
import com.android.systemui.doze.DozeTriggers;
import com.android.systemui.doze.DozeUi;
import com.android.systemui.doze.DozeWallpaperState;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeModule_ProvidesDozeMachinePartesFactory implements Factory<DozeMachine.Part[]> {
    public final Provider<DozeAuthRemover> dozeAuthRemoverProvider;
    public final Provider<DozeDockHandler> dozeDockHandlerProvider;
    public final Provider<DozeFalsingManagerAdapter> dozeFalsingManagerAdapterProvider;
    public final Provider<DozePauser> dozePauserProvider;
    public final Provider<DozeScreenBrightness> dozeScreenBrightnessProvider;
    public final Provider<DozeScreenState> dozeScreenStateProvider;
    public final Provider<DozeTriggers> dozeTriggersProvider;
    public final Provider<DozeUi> dozeUiProvider;
    public final Provider<DozeWallpaperState> dozeWallpaperStateProvider;

    public static DozeModule_ProvidesDozeMachinePartesFactory create(Provider<DozePauser> provider, Provider<DozeFalsingManagerAdapter> provider2, Provider<DozeTriggers> provider3, Provider<DozeUi> provider4, Provider<DozeScreenState> provider5, Provider<DozeScreenBrightness> provider6, Provider<DozeWallpaperState> provider7, Provider<DozeDockHandler> provider8, Provider<DozeAuthRemover> provider9) {
        return new DozeModule_ProvidesDozeMachinePartesFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DozeMachine.Part[]{this.dozePauserProvider.mo144get(), this.dozeFalsingManagerAdapterProvider.mo144get(), this.dozeTriggersProvider.mo144get(), this.dozeUiProvider.mo144get(), this.dozeScreenStateProvider.mo144get(), this.dozeScreenBrightnessProvider.mo144get(), this.dozeWallpaperStateProvider.mo144get(), this.dozeDockHandlerProvider.mo144get(), this.dozeAuthRemoverProvider.mo144get()};
    }

    public DozeModule_ProvidesDozeMachinePartesFactory(Provider<DozePauser> provider, Provider<DozeFalsingManagerAdapter> provider2, Provider<DozeTriggers> provider3, Provider<DozeUi> provider4, Provider<DozeScreenState> provider5, Provider<DozeScreenBrightness> provider6, Provider<DozeWallpaperState> provider7, Provider<DozeDockHandler> provider8, Provider<DozeAuthRemover> provider9) {
        this.dozePauserProvider = provider;
        this.dozeFalsingManagerAdapterProvider = provider2;
        this.dozeTriggersProvider = provider3;
        this.dozeUiProvider = provider4;
        this.dozeScreenStateProvider = provider5;
        this.dozeScreenBrightnessProvider = provider6;
        this.dozeWallpaperStateProvider = provider7;
        this.dozeDockHandlerProvider = provider8;
        this.dozeAuthRemoverProvider = provider9;
    }
}
