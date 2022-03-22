package com.google.android.systemui.qs.tileimpl;

import com.android.systemui.qs.tileimpl.QSFactoryImpl;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.google.android.systemui.qs.tiles.OverlayToggleTile;
import com.google.android.systemui.qs.tiles.ReverseChargingTile;
import com.google.android.systemui.qs.tiles.RotationLockTileGoogle;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFactoryImplGoogle extends QSFactoryImpl {
    public final Provider<OverlayToggleTile> mOverlayToggleTileProvider;
    public final Provider<ReverseChargingTile> mReverseChargingTileProvider;
    public final Provider<RotationLockTileGoogle> mRotationLockTileGoogleProvider;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public QSFactoryImplGoogle(dagger.Lazy<com.android.systemui.qs.QSHost> r34, javax.inject.Provider<com.android.systemui.qs.external.CustomTile.Builder> r35, javax.inject.Provider<com.android.systemui.qs.tiles.WifiTile> r36, javax.inject.Provider<com.android.systemui.qs.tiles.InternetTile> r37, javax.inject.Provider<com.android.systemui.qs.tiles.BluetoothTile> r38, javax.inject.Provider<com.android.systemui.qs.tiles.CellularTile> r39, javax.inject.Provider<com.android.systemui.qs.tiles.DndTile> r40, javax.inject.Provider<com.android.systemui.qs.tiles.ColorInversionTile> r41, javax.inject.Provider<com.android.systemui.qs.tiles.AirplaneModeTile> r42, javax.inject.Provider<com.android.systemui.qs.tiles.WorkModeTile> r43, final javax.inject.Provider<com.google.android.systemui.qs.tiles.RotationLockTileGoogle> r44, javax.inject.Provider<com.android.systemui.qs.tiles.FlashlightTile> r45, javax.inject.Provider<com.android.systemui.qs.tiles.LocationTile> r46, javax.inject.Provider<com.android.systemui.qs.tiles.CastTile> r47, javax.inject.Provider<com.android.systemui.qs.tiles.HotspotTile> r48, final javax.inject.Provider<com.google.android.systemui.qs.tiles.BatterySaverTileGoogle> r49, javax.inject.Provider<com.android.systemui.qs.tiles.DataSaverTile> r50, javax.inject.Provider<com.android.systemui.qs.tiles.NightDisplayTile> r51, javax.inject.Provider<com.android.systemui.qs.tiles.NfcTile> r52, javax.inject.Provider<com.android.systemui.util.leak.GarbageMonitor.MemoryTile> r53, javax.inject.Provider<com.android.systemui.qs.tiles.UiModeNightTile> r54, javax.inject.Provider<com.android.systemui.qs.tiles.ScreenRecordTile> r55, javax.inject.Provider<com.google.android.systemui.qs.tiles.ReverseChargingTile> r56, javax.inject.Provider<com.android.systemui.qs.tiles.ReduceBrightColorsTile> r57, javax.inject.Provider<com.android.systemui.qs.tiles.CameraToggleTile> r58, javax.inject.Provider<com.android.systemui.qs.tiles.MicrophoneToggleTile> r59, javax.inject.Provider<com.android.systemui.qs.tiles.DeviceControlsTile> r60, javax.inject.Provider<com.android.systemui.qs.tiles.AlarmTile> r61, javax.inject.Provider<com.google.android.systemui.qs.tiles.OverlayToggleTile> r62, javax.inject.Provider<com.android.systemui.qs.tiles.QuickAccessWalletTile> r63, javax.inject.Provider<com.android.systemui.qs.tiles.QRCodeScannerTile> r64, javax.inject.Provider<com.android.systemui.qs.tiles.OneHandedModeTile> r65, javax.inject.Provider<com.android.systemui.qs.tiles.ColorCorrectionTile> r66) {
        /*
            r33 = this;
            r11 = r33
            r15 = r44
            r0 = r33
            r1 = r34
            r2 = r35
            r3 = r36
            r4 = r37
            r5 = r38
            r6 = r39
            r7 = r40
            r8 = r41
            r9 = r42
            r10 = r43
            r12 = r45
            r13 = r46
            r14 = r47
            r32 = r0
            r0 = r15
            r15 = r48
            r17 = r50
            r18 = r51
            r19 = r52
            r20 = r53
            r21 = r54
            r22 = r55
            r23 = r57
            r24 = r58
            r25 = r59
            r26 = r60
            r27 = r61
            r28 = r63
            r29 = r64
            r30 = r65
            r31 = r66
            java.util.Objects.requireNonNull(r44)
            com.google.android.systemui.qs.tileimpl.QSFactoryImplGoogle$$ExternalSyntheticLambda1 r1 = new com.google.android.systemui.qs.tileimpl.QSFactoryImplGoogle$$ExternalSyntheticLambda1
            r11 = r1
            r1.<init>()
            java.util.Objects.requireNonNull(r49)
            com.google.android.systemui.qs.tileimpl.QSFactoryImplGoogle$$ExternalSyntheticLambda0 r1 = new com.google.android.systemui.qs.tileimpl.QSFactoryImplGoogle$$ExternalSyntheticLambda0
            r16 = r1
            r0 = r49
            r1.<init>()
            r1 = r34
            r0 = r32
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27, r28, r29, r30, r31)
            r0 = r33
            r1 = r56
            r0.mReverseChargingTileProvider = r1
            r1 = r62
            r0.mOverlayToggleTileProvider = r1
            r1 = r44
            r0.mRotationLockTileGoogleProvider = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.qs.tileimpl.QSFactoryImplGoogle.<init>(dagger.Lazy, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider, javax.inject.Provider):void");
    }

    @Override // com.android.systemui.qs.tileimpl.QSFactoryImpl
    public final QSTileImpl createTileInternal(String str) {
        Objects.requireNonNull(str);
        char c = 65535;
        switch (str.hashCode()) {
            case -40300674:
                if (str.equals("rotation")) {
                    c = 0;
                    break;
                }
                break;
            case 110383:
                if (str.equals("ott")) {
                    c = 1;
                    break;
                }
                break;
            case 1099846370:
                if (str.equals("reverse")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return this.mRotationLockTileGoogleProvider.mo144get();
            case 1:
                return this.mOverlayToggleTileProvider.mo144get();
            case 2:
                return this.mReverseChargingTileProvider.mo144get();
            default:
                return super.createTileInternal(str);
        }
    }
}
