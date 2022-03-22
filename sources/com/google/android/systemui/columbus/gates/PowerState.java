package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.PowerManager;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import dagger.Lazy;
import java.util.Objects;
/* compiled from: PowerState.kt */
/* loaded from: classes.dex */
public final class PowerState extends Gate {
    public final PowerManager powerManager;
    public final Lazy<WakefulnessLifecycle> wakefulnessLifecycle;
    public final PowerState$wakefulnessLifecycleObserver$1 wakefulnessLifecycleObserver = new WakefulnessLifecycle.Observer() { // from class: com.google.android.systemui.columbus.gates.PowerState$wakefulnessLifecycleObserver$1
        @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
        public final void onFinishedGoingToSleep() {
            PowerState.this.updateBlocking();
        }

        @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
        public final void onStartedWakingUp() {
            PowerState.this.updateBlocking();
        }
    };

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onActivate() {
        this.wakefulnessLifecycle.get().addObserver(this.wakefulnessLifecycleObserver);
        updateBlocking();
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onDeactivate() {
        WakefulnessLifecycle wakefulnessLifecycle = this.wakefulnessLifecycle.get();
        PowerState$wakefulnessLifecycleObserver$1 powerState$wakefulnessLifecycleObserver$1 = this.wakefulnessLifecycleObserver;
        Objects.requireNonNull(wakefulnessLifecycle);
        wakefulnessLifecycle.mObservers.remove(powerState$wakefulnessLifecycleObserver$1);
    }

    public final void updateBlocking() {
        PowerManager powerManager = this.powerManager;
        boolean z = false;
        if (powerManager != null && !powerManager.isInteractive()) {
            z = true;
        }
        setBlocking(z);
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [com.google.android.systemui.columbus.gates.PowerState$wakefulnessLifecycleObserver$1] */
    public PowerState(Context context, Lazy<WakefulnessLifecycle> lazy) {
        super(context);
        this.wakefulnessLifecycle = lazy;
        this.powerManager = (PowerManager) context.getSystemService(GlobalActionsDialogLite.GLOBAL_ACTION_KEY_POWER);
    }
}
