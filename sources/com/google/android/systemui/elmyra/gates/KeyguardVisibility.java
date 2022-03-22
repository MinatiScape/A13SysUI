package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import com.android.systemui.Dependency;
import com.android.systemui.statusbar.policy.KeyguardStateController;
/* loaded from: classes.dex */
public final class KeyguardVisibility extends Gate {
    public final AnonymousClass1 mKeyguardMonitorCallback = new KeyguardStateController.Callback() { // from class: com.google.android.systemui.elmyra.gates.KeyguardVisibility.1
        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public final void onKeyguardShowingChanged() {
            KeyguardVisibility.this.notifyListener();
        }
    };
    public final KeyguardStateController mKeyguardStateController = (KeyguardStateController) Dependency.get(KeyguardStateController.class);

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final boolean isBlocked() {
        return this.mKeyguardStateController.isShowing();
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onActivate() {
        this.mKeyguardStateController.addCallback(this.mKeyguardMonitorCallback);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onDeactivate() {
        this.mKeyguardStateController.removeCallback(this.mKeyguardMonitorCallback);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.google.android.systemui.elmyra.gates.KeyguardVisibility$1] */
    public KeyguardVisibility(Context context) {
        super(context);
    }
}
