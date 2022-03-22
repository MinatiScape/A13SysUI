package com.android.systemui.usb;

import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.widget.CheckBox;
import java.util.Objects;
/* loaded from: classes.dex */
public class UsbConfirmActivity extends UsbDialogActivity {
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
        } else {
            UsbDialogHelper usbDialogHelper = this.mDialogHelper;
            Objects.requireNonNull(usbDialogHelper);
            int myUserId = UserHandle.myUserId();
            try {
                if (usbDialogHelper.mIsUsbDevice) {
                    usbDialogHelper.mUsbService.setDevicePackage(usbDialogHelper.mDevice, (String) null, myUserId);
                } else {
                    usbDialogHelper.mUsbService.setAccessoryPackage(usbDialogHelper.mAccessory, (String) null, myUserId);
                }
            } catch (RemoteException e) {
                Log.e("UsbDialogHelper", "IUsbService connection failed", e);
            }
        }
        this.mDialogHelper.confirmDialogStartActivity();
        finish();
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
                i = 2131953456;
            } else {
                i = 2131953455;
            }
        } else {
            i = 2131953442;
        }
        setAlertParams(i);
        if (!z) {
            addAlwaysUseCheckbox();
        }
        setupAlert();
    }
}
