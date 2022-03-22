package com.google.android.systemui.qs.dagger;

import android.content.Context;
import android.hardware.display.NightDisplayListener;
import android.os.Handler;
import com.android.systemui.qs.AutoAddTracker;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.ManagedProfileController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceControlsController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.WalletController;
import com.android.systemui.util.settings.SecureSettings;
import com.google.android.systemui.statusbar.phone.AutoTileManagerGoogle;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSModuleGoogle_ProvideAutoTileManagerFactory implements Factory<AutoTileManager> {
    public final Provider<AutoAddTracker.Builder> autoAddTrackerBuilderProvider;
    public final Provider<BatteryController> batteryControllerProvider;
    public final Provider<CastController> castControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DataSaverController> dataSaverControllerProvider;
    public final Provider<DeviceControlsController> deviceControlsControllerProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<QSTileHost> hostProvider;
    public final Provider<HotspotController> hotspotControllerProvider;
    public final Provider<Boolean> isReduceBrightColorsAvailableProvider;
    public final Provider<ManagedProfileController> managedProfileControllerProvider;
    public final Provider<NightDisplayListener> nightDisplayListenerProvider;
    public final Provider<ReduceBrightColorsController> reduceBrightColorsControllerProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<WalletController> walletControllerProvider;

    public static QSModuleGoogle_ProvideAutoTileManagerFactory create(Provider<Context> provider, Provider<AutoAddTracker.Builder> provider2, Provider<QSTileHost> provider3, Provider<Handler> provider4, Provider<SecureSettings> provider5, Provider<HotspotController> provider6, Provider<DataSaverController> provider7, Provider<ManagedProfileController> provider8, Provider<NightDisplayListener> provider9, Provider<CastController> provider10, Provider<BatteryController> provider11, Provider<ReduceBrightColorsController> provider12, Provider<DeviceControlsController> provider13, Provider<WalletController> provider14, Provider<Boolean> provider15) {
        return new QSModuleGoogle_ProvideAutoTileManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        AutoTileManagerGoogle autoTileManagerGoogle = new AutoTileManagerGoogle(this.contextProvider.mo144get(), this.autoAddTrackerBuilderProvider.mo144get(), this.hostProvider.mo144get(), this.handlerProvider.mo144get(), this.secureSettingsProvider.mo144get(), this.hotspotControllerProvider.mo144get(), this.dataSaverControllerProvider.mo144get(), this.managedProfileControllerProvider.mo144get(), this.nightDisplayListenerProvider.mo144get(), this.castControllerProvider.mo144get(), this.batteryControllerProvider.mo144get(), this.reduceBrightColorsControllerProvider.mo144get(), this.deviceControlsControllerProvider.mo144get(), this.walletControllerProvider.mo144get(), this.isReduceBrightColorsAvailableProvider.mo144get().booleanValue());
        autoTileManagerGoogle.init();
        return autoTileManagerGoogle;
    }

    public QSModuleGoogle_ProvideAutoTileManagerFactory(Provider<Context> provider, Provider<AutoAddTracker.Builder> provider2, Provider<QSTileHost> provider3, Provider<Handler> provider4, Provider<SecureSettings> provider5, Provider<HotspotController> provider6, Provider<DataSaverController> provider7, Provider<ManagedProfileController> provider8, Provider<NightDisplayListener> provider9, Provider<CastController> provider10, Provider<BatteryController> provider11, Provider<ReduceBrightColorsController> provider12, Provider<DeviceControlsController> provider13, Provider<WalletController> provider14, Provider<Boolean> provider15) {
        this.contextProvider = provider;
        this.autoAddTrackerBuilderProvider = provider2;
        this.hostProvider = provider3;
        this.handlerProvider = provider4;
        this.secureSettingsProvider = provider5;
        this.hotspotControllerProvider = provider6;
        this.dataSaverControllerProvider = provider7;
        this.managedProfileControllerProvider = provider8;
        this.nightDisplayListenerProvider = provider9;
        this.castControllerProvider = provider10;
        this.batteryControllerProvider = provider11;
        this.reduceBrightColorsControllerProvider = provider12;
        this.deviceControlsControllerProvider = provider13;
        this.walletControllerProvider = provider14;
        this.isReduceBrightColorsAvailableProvider = provider15;
    }
}
