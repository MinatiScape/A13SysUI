package com.google.android.systemui.elmyra.sensors.config;

import android.os.PowerManager;
/* loaded from: classes.dex */
public final class ScreenStateAdjustment extends Adjustment {
    public final AnonymousClass1 mKeyguardUpdateMonitorCallback;
    public final PowerManager mPowerManager;
    public final float mScreenOffAdjustment;

    @Override // com.google.android.systemui.elmyra.sensors.config.Adjustment
    public final float adjustSensitivity(float f) {
        if (!this.mPowerManager.isInteractive()) {
            return f + this.mScreenOffAdjustment;
        }
        return f;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.systemui.elmyra.sensors.config.ScreenStateAdjustment$1, com.android.keyguard.KeyguardUpdateMonitorCallback] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ScreenStateAdjustment(android.content.Context r5) {
        /*
            r4 = this;
            r4.<init>(r5)
            com.google.android.systemui.elmyra.sensors.config.ScreenStateAdjustment$1 r0 = new com.google.android.systemui.elmyra.sensors.config.ScreenStateAdjustment$1
            r0.<init>()
            r4.mKeyguardUpdateMonitorCallback = r0
            java.lang.String r1 = "power"
            java.lang.Object r1 = r5.getSystemService(r1)
            android.os.PowerManager r1 = (android.os.PowerManager) r1
            r4.mPowerManager = r1
            android.util.TypedValue r1 = new android.util.TypedValue
            r1.<init>()
            android.content.res.Resources r5 = r5.getResources()
            r2 = 2131165704(0x7f070208, float:1.7945633E38)
            r3 = 1
            r5.getValue(r2, r1, r3)
            float r5 = r1.getFloat()
            r4.mScreenOffAdjustment = r5
            java.lang.Class<com.android.keyguard.KeyguardUpdateMonitor> r4 = com.android.keyguard.KeyguardUpdateMonitor.class
            java.lang.Object r4 = com.android.systemui.Dependency.get(r4)
            com.android.keyguard.KeyguardUpdateMonitor r4 = (com.android.keyguard.KeyguardUpdateMonitor) r4
            r4.registerCallback(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.elmyra.sensors.config.ScreenStateAdjustment.<init>(android.content.Context):void");
    }
}
