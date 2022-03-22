package com.google.android.systemui.elmyra.gates;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
/* loaded from: classes.dex */
public final class ChargingState extends TransientGate {
    public final AnonymousClass1 mPowerReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.elmyra.gates.ChargingState.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            ChargingState.this.block();
        }
    };

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onActivate() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        this.mContext.registerReceiver(this.mPowerReceiver, intentFilter);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onDeactivate() {
        this.mContext.unregisterReceiver(this.mPowerReceiver);
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [com.google.android.systemui.elmyra.gates.ChargingState$1] */
    public ChargingState(Context context) {
        super(context, context.getResources().getInteger(2131492921));
    }
}
