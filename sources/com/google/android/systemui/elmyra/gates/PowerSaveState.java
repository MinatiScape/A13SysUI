package com.google.android.systemui.elmyra.gates;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import com.android.internal.annotations.GuardedBy;
import com.android.systemui.Dependency;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PowerSaveState extends Gate {
    @GuardedBy({"mLock"})
    public boolean mBatterySaverEnabled;
    @GuardedBy({"mLock"})
    public boolean mIsDeviceInteractive;
    public final PowerManager mPowerManager;
    public final Object mLock = new Object();
    public final AnonymousClass1 mReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.elmyra.gates.PowerSaveState.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            PowerSaveState powerSaveState = PowerSaveState.this;
            Objects.requireNonNull(powerSaveState);
            synchronized (powerSaveState.mLock) {
                powerSaveState.mBatterySaverEnabled = powerSaveState.mPowerManager.getPowerSaveState(13).batterySaverEnabled;
                powerSaveState.mIsDeviceInteractive = powerSaveState.mPowerManager.isInteractive();
            }
            PowerSaveState.this.notifyListener();
        }
    };
    public BroadcastDispatcher mBroadcastDispatcher = (BroadcastDispatcher) Dependency.get(BroadcastDispatcher.class);

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final boolean isBlocked() {
        boolean z;
        synchronized (this.mLock) {
            if (!this.mBatterySaverEnabled || this.mIsDeviceInteractive) {
                z = false;
            } else {
                z = true;
            }
        }
        return z;
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onActivate() {
        IntentFilter intentFilter = new IntentFilter("android.os.action.POWER_SAVE_MODE_CHANGED");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.mBroadcastDispatcher.registerReceiver(this.mReceiver, intentFilter);
        synchronized (this.mLock) {
            this.mBatterySaverEnabled = this.mPowerManager.getPowerSaveState(13).batterySaverEnabled;
            this.mIsDeviceInteractive = this.mPowerManager.isInteractive();
        }
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onDeactivate() {
        this.mBroadcastDispatcher.unregisterReceiver(this.mReceiver);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.android.systemui.elmyra.gates.PowerSaveState$1] */
    public PowerSaveState(Context context) {
        super(context);
        this.mPowerManager = (PowerManager) context.getSystemService(GlobalActionsDialogLite.GLOBAL_ACTION_KEY_POWER);
    }
}
