package com.android.systemui.qs.tileimpl;

import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.tiles.AirplaneModeTile;
import com.android.systemui.qs.tiles.AlarmTile;
import com.android.systemui.qs.tiles.BatterySaverTile;
import com.android.systemui.qs.tiles.BluetoothTile;
import com.android.systemui.qs.tiles.CameraToggleTile;
import com.android.systemui.qs.tiles.CastTile;
import com.android.systemui.qs.tiles.CellularTile;
import com.android.systemui.qs.tiles.ColorCorrectionTile;
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
import com.android.systemui.qs.tiles.RotationLockTile;
import com.android.systemui.qs.tiles.ScreenRecordTile;
import com.android.systemui.qs.tiles.UiModeNightTile;
import com.android.systemui.qs.tiles.WifiTile;
import com.android.systemui.qs.tiles.WorkModeTile;
import com.android.systemui.util.leak.GarbageMonitor;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFactoryImpl_Factory implements Factory<QSFactoryImpl> {
    public final Provider<AirplaneModeTile> airplaneModeTileProvider;
    public final Provider<AlarmTile> alarmTileProvider;
    public final Provider<BatterySaverTile> batterySaverTileProvider;
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
    public final Provider<QRCodeScannerTile> qrCodeScannerTileProvider;
    public final Provider<QSHost> qsHostLazyProvider;
    public final Provider<QuickAccessWalletTile> quickAccessWalletTileProvider;
    public final Provider<ReduceBrightColorsTile> reduceBrightColorsTileProvider;
    public final Provider<RotationLockTile> rotationLockTileProvider;
    public final Provider<ScreenRecordTile> screenRecordTileProvider;
    public final Provider<UiModeNightTile> uiModeNightTileProvider;
    public final Provider<WifiTile> wifiTileProvider;
    public final Provider<WorkModeTile> workModeTileProvider;

    public QSFactoryImpl_Factory(Provider<QSHost> provider, Provider<CustomTile.Builder> provider2, Provider<WifiTile> provider3, Provider<InternetTile> provider4, Provider<BluetoothTile> provider5, Provider<CellularTile> provider6, Provider<DndTile> provider7, Provider<ColorInversionTile> provider8, Provider<AirplaneModeTile> provider9, Provider<WorkModeTile> provider10, Provider<RotationLockTile> provider11, Provider<FlashlightTile> provider12, Provider<LocationTile> provider13, Provider<CastTile> provider14, Provider<HotspotTile> provider15, Provider<BatterySaverTile> provider16, Provider<DataSaverTile> provider17, Provider<NightDisplayTile> provider18, Provider<NfcTile> provider19, Provider<GarbageMonitor.MemoryTile> provider20, Provider<UiModeNightTile> provider21, Provider<ScreenRecordTile> provider22, Provider<ReduceBrightColorsTile> provider23, Provider<CameraToggleTile> provider24, Provider<MicrophoneToggleTile> provider25, Provider<DeviceControlsTile> provider26, Provider<AlarmTile> provider27, Provider<QuickAccessWalletTile> provider28, Provider<QRCodeScannerTile> provider29, Provider<OneHandedModeTile> provider30, Provider<ColorCorrectionTile> provider31) {
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
        this.rotationLockTileProvider = provider11;
        this.flashlightTileProvider = provider12;
        this.locationTileProvider = provider13;
        this.castTileProvider = provider14;
        this.hotspotTileProvider = provider15;
        this.batterySaverTileProvider = provider16;
        this.dataSaverTileProvider = provider17;
        this.nightDisplayTileProvider = provider18;
        this.nfcTileProvider = provider19;
        this.memoryTileProvider = provider20;
        this.uiModeNightTileProvider = provider21;
        this.screenRecordTileProvider = provider22;
        this.reduceBrightColorsTileProvider = provider23;
        this.cameraToggleTileProvider = provider24;
        this.microphoneToggleTileProvider = provider25;
        this.deviceControlsTileProvider = provider26;
        this.alarmTileProvider = provider27;
        this.quickAccessWalletTileProvider = provider28;
        this.qrCodeScannerTileProvider = provider29;
        this.oneHandedModeTileProvider = provider30;
        this.colorCorrectionTileProvider = provider31;
    }

    public static QSFactoryImpl_Factory create(Provider<QSHost> provider, Provider<CustomTile.Builder> provider2, Provider<WifiTile> provider3, Provider<InternetTile> provider4, Provider<BluetoothTile> provider5, Provider<CellularTile> provider6, Provider<DndTile> provider7, Provider<ColorInversionTile> provider8, Provider<AirplaneModeTile> provider9, Provider<WorkModeTile> provider10, Provider<RotationLockTile> provider11, Provider<FlashlightTile> provider12, Provider<LocationTile> provider13, Provider<CastTile> provider14, Provider<HotspotTile> provider15, Provider<BatterySaverTile> provider16, Provider<DataSaverTile> provider17, Provider<NightDisplayTile> provider18, Provider<NfcTile> provider19, Provider<GarbageMonitor.MemoryTile> provider20, Provider<UiModeNightTile> provider21, Provider<ScreenRecordTile> provider22, Provider<ReduceBrightColorsTile> provider23, Provider<CameraToggleTile> provider24, Provider<MicrophoneToggleTile> provider25, Provider<DeviceControlsTile> provider26, Provider<AlarmTile> provider27, Provider<QuickAccessWalletTile> provider28, Provider<QRCodeScannerTile> provider29, Provider<OneHandedModeTile> provider30, Provider<ColorCorrectionTile> provider31) {
        return new QSFactoryImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29, provider30, provider31);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new QSFactoryImpl(DoubleCheck.lazy(this.qsHostLazyProvider), this.customTileBuilderProvider, this.wifiTileProvider, this.internetTileProvider, this.bluetoothTileProvider, this.cellularTileProvider, this.dndTileProvider, this.colorInversionTileProvider, this.airplaneModeTileProvider, this.workModeTileProvider, this.rotationLockTileProvider, this.flashlightTileProvider, this.locationTileProvider, this.castTileProvider, this.hotspotTileProvider, this.batterySaverTileProvider, this.dataSaverTileProvider, this.nightDisplayTileProvider, this.nfcTileProvider, this.memoryTileProvider, this.uiModeNightTileProvider, this.screenRecordTileProvider, this.reduceBrightColorsTileProvider, this.cameraToggleTileProvider, this.microphoneToggleTileProvider, this.deviceControlsTileProvider, this.alarmTileProvider, this.quickAccessWalletTileProvider, this.qrCodeScannerTileProvider, this.oneHandedModeTileProvider, this.colorCorrectionTileProvider);
    }
}
