package com.android.systemui.statusbar.policy;

import android.provider.DeviceConfig;
import com.android.systemui.qrcodescanner.controller.QRCodeScannerController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class LocationControllerImpl$$ExternalSyntheticLambda0 implements DeviceConfig.OnPropertiesChangedListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ LocationControllerImpl$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    public final void onPropertiesChanged(DeviceConfig.Properties properties) {
        boolean z = true;
        switch (this.$r8$classId) {
            case 0:
                LocationControllerImpl locationControllerImpl = (LocationControllerImpl) this.f$0;
                Objects.requireNonNull(locationControllerImpl);
                Objects.requireNonNull(locationControllerImpl.mDeviceConfigProxy);
                locationControllerImpl.mShouldDisplayAllAccesses = DeviceConfig.getBoolean("privacy", "location_indicators_small_enabled", false);
                if (locationControllerImpl.mSecureSettings.getInt("locationShowSystemOps", 0) != 1) {
                    z = false;
                }
                locationControllerImpl.mShowSystemAccessesFlag = z;
                locationControllerImpl.updateActiveLocationRequests();
                return;
            default:
                QRCodeScannerController qRCodeScannerController = (QRCodeScannerController) this.f$0;
                Objects.requireNonNull(qRCodeScannerController);
                if ("systemui".equals(properties.getNamespace()) && properties.getKeyset().contains("default_qr_code_scanner")) {
                    qRCodeScannerController.updateQRCodeScannerActivityDetails();
                    qRCodeScannerController.updateQRCodeScannerPreferenceDetails(true);
                    return;
                }
                return;
        }
    }
}
