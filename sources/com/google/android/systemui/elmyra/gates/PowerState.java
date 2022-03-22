package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import android.os.PowerManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dependency;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
/* loaded from: classes.dex */
public class PowerState extends Gate {
    public final AnonymousClass1 mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.google.android.systemui.elmyra.gates.PowerState.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onFinishedGoingToSleep(int i) {
            PowerState.this.notifyListener();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onStartedWakingUp() {
            PowerState.this.notifyListener();
        }
    };
    public final PowerManager mPowerManager;

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public boolean isBlocked() {
        return !this.mPowerManager.isInteractive();
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public void onActivate() {
        ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).registerCallback(this.mKeyguardUpdateMonitorCallback);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public void onDeactivate() {
        ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).removeCallback(this.mKeyguardUpdateMonitorCallback);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.systemui.elmyra.gates.PowerState$1] */
    public PowerState(Context context) {
        super(context);
        this.mPowerManager = (PowerManager) context.getSystemService(GlobalActionsDialogLite.GLOBAL_ACTION_KEY_POWER);
    }
}
