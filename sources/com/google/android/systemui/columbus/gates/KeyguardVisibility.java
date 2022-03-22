package com.google.android.systemui.columbus.gates;

import android.content.Context;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.Lazy;
/* compiled from: KeyguardVisibility.kt */
/* loaded from: classes.dex */
public final class KeyguardVisibility extends Gate {
    public final KeyguardVisibility$keyguardMonitorCallback$1 keyguardMonitorCallback = new KeyguardVisibility$keyguardMonitorCallback$1(this);
    public final Lazy<KeyguardStateController> keyguardStateController;

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onActivate() {
        this.keyguardMonitorCallback.onKeyguardShowingChanged();
        this.keyguardStateController.get().addCallback(this.keyguardMonitorCallback);
        setBlocking(this.keyguardStateController.get().isShowing());
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onDeactivate() {
        this.keyguardStateController.get().removeCallback(this.keyguardMonitorCallback);
    }

    public KeyguardVisibility(Context context, Lazy<KeyguardStateController> lazy) {
        super(context);
        this.keyguardStateController = lazy;
    }
}
