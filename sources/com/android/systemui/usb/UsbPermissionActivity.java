package com.android.systemui.usb;

import android.widget.CheckBox;
import java.util.Objects;
/* loaded from: classes.dex */
public class UsbPermissionActivity extends UsbDialogActivity {
    public boolean mPermissionGranted = false;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.systemui.usb.UsbDialogActivity
    public final void onConfirm() {
        boolean z;
        this.mDialogHelper.grantUidAccessPermission();
        CheckBox checkBox = this.mAlwaysUse;
        if (checkBox == null || !checkBox.isChecked()) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            this.mDialogHelper.setDefaultPackage();
        }
        this.mPermissionGranted = true;
        finish();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.systemui.usb.UsbDialogActivity
    public final void onPause() {
        if (isFinishing()) {
            this.mDialogHelper.sendPermissionDialogResponse(this.mPermissionGranted);
        }
        super.onPause();
    }

    @Override // com.android.systemui.usb.UsbDialogActivity
    public final void onResume() {
        int i;
        super.onResume();
        UsbDialogHelper usbDialogHelper = this.mDialogHelper;
        Objects.requireNonNull(usbDialogHelper);
        boolean z = false;
        if (usbDialogHelper.mIsUsbDevice) {
            if (this.mDialogHelper.deviceHasAudioCapture() && !this.mDialogHelper.packageHasAudioRecordingPermission()) {
                z = true;
            }
            if (z) {
                i = 2131953458;
            } else {
                i = 2131953457;
            }
        } else {
            i = 2131953443;
        }
        setAlertParams(i);
        if (!z) {
            UsbDialogHelper usbDialogHelper2 = this.mDialogHelper;
            Objects.requireNonNull(usbDialogHelper2);
            if (usbDialogHelper2.mCanBeDefault) {
                addAlwaysUseCheckbox();
            }
        }
        setupAlert();
    }
}
