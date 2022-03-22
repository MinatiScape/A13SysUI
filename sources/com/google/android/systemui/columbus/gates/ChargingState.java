package com.google.android.systemui.columbus.gates;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
/* compiled from: ChargingState.kt */
/* loaded from: classes.dex */
public final class ChargingState extends TransientGate {
    public final long gateDuration;
    public final ChargingState$powerReceiver$1 powerReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.columbus.gates.ChargingState$powerReceiver$1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            ChargingState chargingState = ChargingState.this;
            chargingState.blockForMillis(chargingState.gateDuration);
        }
    };

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onActivate() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        this.context.registerReceiver(this.powerReceiver, intentFilter);
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onDeactivate() {
        this.context.unregisterReceiver(this.powerReceiver);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.google.android.systemui.columbus.gates.ChargingState$powerReceiver$1] */
    public ChargingState(Context context, Handler handler, long j) {
        super(context, handler);
        this.gateDuration = j;
    }
}
