package com.google.android.systemui.columbus.gates;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
/* compiled from: UsbState.kt */
/* loaded from: classes.dex */
public final class UsbState extends TransientGate {
    public final long gateDuration;
    public boolean usbConnected;
    public final UsbState$usbReceiver$1 usbReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.columbus.gates.UsbState$usbReceiver$1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if (intent != null) {
                UsbState usbState = UsbState.this;
                boolean booleanExtra = intent.getBooleanExtra("connected", false);
                if (booleanExtra != usbState.usbConnected) {
                    usbState.usbConnected = booleanExtra;
                    usbState.blockForMillis(usbState.gateDuration);
                }
            }
        }
    };

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onActivate() {
        IntentFilter intentFilter = new IntentFilter("android.hardware.usb.action.USB_STATE");
        Intent registerReceiver = this.context.registerReceiver(null, intentFilter);
        if (registerReceiver != null) {
            this.usbConnected = registerReceiver.getBooleanExtra("connected", false);
        }
        this.context.registerReceiver(this.usbReceiver, intentFilter);
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onDeactivate() {
        this.context.unregisterReceiver(this.usbReceiver);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.google.android.systemui.columbus.gates.UsbState$usbReceiver$1] */
    public UsbState(Context context, Handler handler, long j) {
        super(context, handler);
        this.gateDuration = j;
    }
}
