package com.google.android.systemui.columbus.gates;

import android.content.Context;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.sensors.ThresholdSensor;
import com.android.systemui.util.sensors.ThresholdSensorEvent;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Proximity.kt */
/* loaded from: classes.dex */
public final class Proximity extends Gate {
    public final Proximity$proximityListener$1 proximityListener = new ThresholdSensor.Listener() { // from class: com.google.android.systemui.columbus.gates.Proximity$proximityListener$1
        @Override // com.android.systemui.util.sensors.ThresholdSensor.Listener
        public final void onThresholdCrossed(ThresholdSensorEvent thresholdSensorEvent) {
            Proximity proximity = Proximity.this;
            Objects.requireNonNull(proximity);
            proximity.setBlocking(!Intrinsics.areEqual(proximity.proximitySensor.isNear(), Boolean.FALSE));
        }
    };
    public final ProximitySensor proximitySensor;

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onActivate() {
        this.proximitySensor.register(this.proximityListener);
        setBlocking(!Intrinsics.areEqual(this.proximitySensor.isNear(), Boolean.FALSE));
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onDeactivate() {
        this.proximitySensor.unregister(this.proximityListener);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.google.android.systemui.columbus.gates.Proximity$proximityListener$1] */
    public Proximity(Context context, ProximitySensor proximitySensor) {
        super(context);
        this.proximitySensor = proximitySensor;
        proximitySensor.setTag("Columbus/Proximity");
    }
}
