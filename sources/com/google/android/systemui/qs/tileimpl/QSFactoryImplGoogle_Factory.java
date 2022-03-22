package com.google.android.systemui.qs.tileimpl;

import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.tiles.AirplaneModeTile;
import com.android.systemui.qs.tiles.AlarmTile;
import com.android.systemui.qs.tiles.BluetoothTile;
import com.android.systemui.qs.tiles.CameraToggleTile;
import com.android.systemui.qs.tiles.CastTile;
import com.android.systemui.qs.tiles.CellularTile;
import com.android.systemui.qs.tiles.ColorCorrectionTile;
import com.android.systemui.qs.tiles.ColorCorrectionTile_Factory;
import com.android.systemui.qs.tiles.ColorInversionTile;
import com.android.systemui.qs.tiles.DataSaverTile;
import com.android.systemui.qs.tiles.DeviceControlsTile;
import com.android.systemui.qs.tiles.DndTile;
import com.android.systemui.qs.tiles.FlashlightTile;
import com.android.systemui.qs.tiles.HotspotTile;
import com.android.systemui.qs.tiles.InternetTile;
import com.android.systemui.qs.tiles.LocationTile;
import com.android.systemui.qs.tiles.MicrophoneToggleTile;
import com.android.systemui.qs.tiles.NfcTile;
import com.android.systemui.qs.tiles.NightDisplayTile;
import com.android.systemui.qs.tiles.OneHandedModeTile;
import com.android.systemui.qs.tiles.QRCodeScannerTile;
import com.android.systemui.qs.tiles.QuickAccessWalletTile;
import com.android.systemui.qs.tiles.ReduceBrightColorsTile;
import com.android.systemui.qs.tiles.ScreenRecordTile;
import com.android.systemui.qs.tiles.UiModeNightTile;
import com.android.systemui.qs.tiles.WifiTile;
import com.android.systemui.qs.tiles.WorkModeTile;
import com.android.systemui.util.leak.GarbageMonitor;
import com.google.android.systemui.qs.tiles.BatterySaverTileGoogle;
import com.google.android.systemui.qs.tiles.OverlayToggleTile;
import com.google.android.systemui.qs.tiles.ReverseChargingTile;
import com.google.android.systemui.qs.tiles.RotationLockTileGoogle;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFactoryImplGoogle_Factory implements Factory<QSFactoryImplGoogle> {
    public final Provider<AirplaneModeTile> airplaneModeTileProvider;
    public final Provider<AlarmTile> alarmTileProvider;
    public final Provider<BatterySaverTileGoogle> batterySaverTileGoogleProvider;
    public final Provider<BluetoothTile> bluetoothTileProvider;
    public final Provider<CameraToggleTile> cameraToggleTileProvider;
    public final Provider<CastTile> castTileProvider;
    public final Provider<CellularTile> cellularTileProvider;
    public final Provider<ColorCorrectionTile> colorCorrectionTileProvider;
    public final Provider<ColorInversionTile> colorInversionTileProvider;
    public final Provider<CustomTile.Builder> customTileBuilderProvider;
    public final Provider<DataSaverTile> dataSaverTileProvider;
    public final Provider<DeviceControlsTile> deviceControlsTileProvider;
    public final Provider<DndTile> dndTileProvider;
    public final Provider<FlashlightTile> flashlightTileProvider;
    public final Provider<HotspotTile> hotspotTileProvider;
    public final Provider<InternetTile> internetTileProvider;
    public final Provider<LocationTile> locationTileProvider;
    public final Provider<GarbageMonitor.MemoryTile> memoryTileProvider;
    public final Provider<MicrophoneToggleTile> microphoneToggleTileProvider;
    public final Provider<NfcTile> nfcTileProvider;
    public final Provider<NightDisplayTile> nightDisplayTileProvider;
    public final Provider<OneHandedModeTile> oneHandedModeTileProvider;
    public final Provider<OverlayToggleTile> overlayToggleTileProvider;
    public final Provider<QRCodeScannerTile> qrCodeScannerTileProvider;
    public final Provider<QSHost> qsHostLazyProvider;
    public final Provider<QuickAccessWalletTile> quickAccessWalletTileProvider;
    public final Provider<ReduceBrightColorsTile> reduceBrightColorsTileProvider;
    public final Provider<ReverseChargingTile> reverseChargingTileProvider;
    public final Provider<RotationLockTileGoogle> rotationLockTileGoogleProvider;
    public final Provider<ScreenRecordTile> screenRecordTileProvider;
    public final Provider<UiModeNightTile> uiModeNightTileProvider;
    public final Provider<WifiTile> wifiTileProvider;
    public final Provider<WorkModeTile> workModeTileProvider;

    public QSFactoryImplGoogle_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, Provider provider13, Provider provider14, Provider provider15, Provider provider16, Provider provider17, Provider provider18, Provider provider19, Provider provider20, Provider provider21, Provider provider22, Provider provider23, Provider provider24, Provider provider25, Provider provider26, Provider provider27, Provider provider28, Provider provider29, Provider provider30, Provider provider31, Provider provider32, ColorCorrectionTile_Factory colorCorrectionTile_Factory) {
        this.qsHostLazyProvider = provider;
        this.customTileBuilderProvider = provider2;
        this.wifiTileProvider = provider3;
        this.internetTileProvider = provider4;
        this.bluetoothTileProvider = provider5;
        this.cellularTileProvider = provider6;
        this.dndTileProvider = provider7;
        this.colorInversionTileProvider = provider8;
        this.airplaneModeTileProvider = provider9;
        this.workModeTileProvider = provider10;
        this.rotationLockTileGoogleProvider = provider11;
        this.flashlightTileProvider = provider12;
        this.locationTileProvider = provider13;
        this.castTileProvider = provider14;
        this.hotspotTileProvider = provider15;
        this.batterySaverTileGoogleProvider = provider16;
        this.dataSaverTileProvider = provider17;
        this.nightDisplayTileProvider = provider18;
        this.nfcTileProvider = provider19;
        this.memoryTileProvider = provider20;
        this.uiModeNightTileProvider = provider21;
        this.screenRecordTileProvider = provider22;
        this.reverseChargingTileProvider = provider23;
        this.reduceBrightColorsTileProvider = provider24;
        this.cameraToggleTileProvider = provider25;
        this.microphoneToggleTileProvider = provider26;
        this.deviceControlsTileProvider = provider27;
        this.alarmTileProvider = provider28;
        this.overlayToggleTileProvider = provider29;
        this.quickAccessWalletTileProvider = provider30;
        this.qrCodeScannerTileProvider = provider31;
        this.oneHandedModeTileProvider = provider32;
        this.colorCorrectionTileProvider = colorCorrectionTile_Factory;
    }

    public static QSFactoryImplGoogle_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, Provider provider13, Provider provider14, Provider provider15, Provider provider16, Provider provider17, Provider provider18, Provider provider19, Provider provider20, Provider provider21, Provider provider22, Provider provider23, Provider provider24, Provider provider25, Provider provider26, Provider provider27, Provider provider28, Provider provider29, Provider provider30, Provider provider31, Provider provider32, ColorCorrectionTile_Factory colorCorrectionTile_Factory) {
        return new QSFactoryImplGoogle_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29, provider30, provider31, provider32, colorCorrectionTile_Factory);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new QSFactoryImplGoogle(DoubleCheck.lazy(this.qsHostLazyProvider), this.customTileBuilderProvider, this.wifiTileProvider, this.internetTileProvider, this.bluetoothTileProvider, this.cellularTileProvider, this.dndTileProvider, this.colorInversionTileProvider, this.airplaneModeTileProvider, this.workModeTileProvider, this.rotationLockTileGoogleProvider, this.flashlightTileProvider, this.locationTileProvider, this.castTileProvider, this.hotspotTileProvider, this.batterySaverTileGoogleProvider, this.dataSaverTileProvider, this.nightDisplayTileProvider, this.nfcTileProvider, this.memoryTileProvider, this.uiModeNightTileProvider, this.screenRecordTileProvider, this.reverseChargingTileProvider, this.reduceBrightColorsTileProvider, this.cameraToggleTileProvider, this.microphoneToggleTileProvider, this.deviceControlsTileProvider, this.alarmTileProvider, this.overlayToggleTileProvider, this.quickAccessWalletTileProvider, this.qrCodeScannerTileProvider, this.oneHandedModeTileProvider, this.colorCorrectionTileProvider);
    }
}
