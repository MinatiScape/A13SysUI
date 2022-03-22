package com.android.systemui.usb.tv;

import com.android.systemui.usb.UsbDialogHelper;
import java.util.Objects;
/* loaded from: classes.dex */
public class TvUsbConfirmActivity extends TvUsbDialogActivity {
    @Override // com.android.systemui.usb.tv.TvUsbDialogActivity
    public final void onConfirm() {
        this.mDialogHelper.grantUidAccessPermission();
        this.mDialogHelper.confirmDialogStartActivity();
        finish();
    }

    @Override // com.android.systemui.usb.tv.TvUsbDialogActivity, android.app.Activity
    public final void onResume() {
        int i;
        boolean z;
        super.onResume();
        UsbDialogHelper usbDialogHelper = this.mDialogHelper;
        Objects.requireNonNull(usbDialogHelper);
        if (usbDialogHelper.mIsUsbDevice) {
            if (!this.mDialogHelper.deviceHasAudioCapture() || this.mDialogHelper.packageHasAudioRecordingPermission()) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                i = 2131953456;
            } else {
                i = 2131953455;
            }
        } else {
            i = 2131953442;
        }
        UsbDialogHelper usbDialogHelper2 = this.mDialogHelper;
        Objects.requireNonNull(usbDialogHelper2);
        String string = getString(i, usbDialogHelper2.mAppName, this.mDialogHelper.getDeviceDescription());
        UsbDialogHelper usbDialogHelper3 = this.mDialogHelper;
        Objects.requireNonNull(usbDialogHelper3);
        initUI(usbDialogHelper3.mAppName, string);
    }
}
