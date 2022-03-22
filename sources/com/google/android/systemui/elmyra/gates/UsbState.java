package com.google.android.systemui.elmyra.gates;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
/* loaded from: classes.dex */
public final class UsbState extends TransientGate {
    public boolean mUsbConnected;
    public final AnonymousClass1 mUsbReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.elmyra.gates.UsbState.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            boolean booleanExtra = intent.getBooleanExtra("connected", false);
            UsbState usbState = UsbState.this;
            if (booleanExtra != usbState.mUsbConnected) {
                usbState.mUsbConnected = booleanExtra;
                usbState.block();
            }
        }
    };

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onActivate() {
        IntentFilter intentFilter = new IntentFilter("android.hardware.usb.action.USB_STATE");
        Intent registerReceiver = this.mContext.registerReceiver(null, intentFilter);
        if (registerReceiver != null) {
            this.mUsbConnected = registerReceiver.getBooleanExtra("connected", false);
        }
        this.mContext.registerReceiver(this.mUsbReceiver, intentFilter);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onDeactivate() {
        this.mContext.unregisterReceiver(this.mUsbReceiver);
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [com.google.android.systemui.elmyra.gates.UsbState$1] */
    public UsbState(Context context) {
        super(context, context.getResources().getInteger(2131492931));
    }
}
