package com.android.systemui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Build;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import java.util.Objects;
/* loaded from: classes.dex */
public class LatencyTester extends CoreStartable {
    public final BiometricUnlockController mBiometricUnlockController;
    public final BroadcastDispatcher mBroadcastDispatcher;

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        if (Build.IS_DEBUGGABLE) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.android.systemui.latency.ACTION_FINGERPRINT_WAKE");
            intentFilter.addAction("com.android.systemui.latency.ACTION_FACE_WAKE");
            this.mBroadcastDispatcher.registerReceiver(new BroadcastReceiver() { // from class: com.android.systemui.LatencyTester.1
                @Override // android.content.BroadcastReceiver
                public final void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if ("com.android.systemui.latency.ACTION_FINGERPRINT_WAKE".equals(action)) {
                        LatencyTester latencyTester = LatencyTester.this;
                        BiometricSourceType biometricSourceType = BiometricSourceType.FINGERPRINT;
                        Objects.requireNonNull(latencyTester);
                        latencyTester.mBiometricUnlockController.onBiometricAcquired(biometricSourceType);
                        latencyTester.mBiometricUnlockController.onBiometricAuthenticated(KeyguardUpdateMonitor.getCurrentUser(), biometricSourceType, true);
                    } else if ("com.android.systemui.latency.ACTION_FACE_WAKE".equals(action)) {
                        LatencyTester latencyTester2 = LatencyTester.this;
                        BiometricSourceType biometricSourceType2 = BiometricSourceType.FACE;
                        Objects.requireNonNull(latencyTester2);
                        latencyTester2.mBiometricUnlockController.onBiometricAcquired(biometricSourceType2);
                        latencyTester2.mBiometricUnlockController.onBiometricAuthenticated(KeyguardUpdateMonitor.getCurrentUser(), biometricSourceType2, true);
                    }
                }
            }, intentFilter);
        }
    }

    public LatencyTester(Context context, BiometricUnlockController biometricUnlockController, BroadcastDispatcher broadcastDispatcher) {
        super(context);
        this.mBiometricUnlockController = biometricUnlockController;
        this.mBroadcastDispatcher = broadcastDispatcher;
    }
}
