package com.google.android.systemui.columbus.gates;

import android.content.Context;
import com.google.android.systemui.columbus.gates.Gate;
import java.util.Objects;
/* compiled from: KeyguardProximity.kt */
/* loaded from: classes.dex */
public final class KeyguardProximity extends Gate {
    public boolean isListening;
    public final KeyguardVisibility keyguardGate;
    public final Proximity proximity;
    public final KeyguardProximity$keyguardListener$1 keyguardListener = new Gate.Listener() { // from class: com.google.android.systemui.columbus.gates.KeyguardProximity$keyguardListener$1
        @Override // com.google.android.systemui.columbus.gates.Gate.Listener
        public final void onGateChanged(Gate gate) {
            KeyguardProximity.this.updateProximityListener();
        }
    };
    public final KeyguardProximity$proximityListener$1 proximityListener = new Gate.Listener() { // from class: com.google.android.systemui.columbus.gates.KeyguardProximity$proximityListener$1
        @Override // com.google.android.systemui.columbus.gates.Gate.Listener
        public final void onGateChanged(Gate gate) {
            boolean z;
            KeyguardProximity keyguardProximity = KeyguardProximity.this;
            Objects.requireNonNull(keyguardProximity);
            if (!keyguardProximity.isListening || !keyguardProximity.proximity.isBlocking()) {
                z = false;
            } else {
                z = true;
            }
            keyguardProximity.setBlocking(z);
        }
    };

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onActivate() {
        this.keyguardGate.registerListener(this.keyguardListener);
        updateProximityListener();
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onDeactivate() {
        this.keyguardGate.unregisterListener(this.keyguardListener);
        updateProximityListener();
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final String toString() {
        return super.toString() + " [isListening -> " + this.isListening + "; proximityBlocked -> " + this.proximity.isBlocking() + ']';
    }

    public final void updateProximityListener() {
        boolean z = true;
        if (this.active) {
            KeyguardVisibility keyguardVisibility = this.keyguardGate;
            Objects.requireNonNull(keyguardVisibility);
            if (keyguardVisibility.keyguardStateController.get().isShowing()) {
                KeyguardVisibility keyguardVisibility2 = this.keyguardGate;
                Objects.requireNonNull(keyguardVisibility2);
                if (!keyguardVisibility2.keyguardStateController.get().isOccluded()) {
                    if (!this.isListening) {
                        this.proximity.registerListener(this.proximityListener);
                        this.isListening = true;
                    }
                    if (this.isListening || !this.proximity.isBlocking()) {
                        z = false;
                    }
                    setBlocking(z);
                }
            }
        }
        if (this.isListening) {
            this.proximity.unregisterListener(this.proximityListener);
            this.isListening = false;
        }
        if (this.isListening) {
        }
        z = false;
        setBlocking(z);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.google.android.systemui.columbus.gates.KeyguardProximity$keyguardListener$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.google.android.systemui.columbus.gates.KeyguardProximity$proximityListener$1] */
    public KeyguardProximity(Context context, KeyguardVisibility keyguardVisibility, Proximity proximity) {
        super(context);
        this.keyguardGate = keyguardVisibility;
        this.proximity = proximity;
        updateProximityListener();
    }
}
