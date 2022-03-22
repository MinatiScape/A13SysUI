package com.android.systemui.doze;

import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.doze.DozeMachine;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DozeAuthRemover implements DozeMachine.Part {
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;

    @Override // com.android.systemui.doze.DozeMachine.Part
    public final void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        if (state2 == DozeMachine.State.DOZE || state2 == DozeMachine.State.DOZE_AOD) {
            if (this.mKeyguardUpdateMonitor.getUserUnlockedWithBiometric(KeyguardUpdateMonitor.getCurrentUser())) {
                KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
                Objects.requireNonNull(keyguardUpdateMonitor);
                keyguardUpdateMonitor.clearBiometricRecognized(-10000);
            }
        }
    }

    public DozeAuthRemover(KeyguardUpdateMonitor keyguardUpdateMonitor) {
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
    }
}
