package com.android.keyguard.clock;

import android.provider.Settings;
import android.util.Range;
import com.android.systemui.plugins.ClockPlugin;
import com.google.android.systemui.elmyra.sensors.config.GestureConfiguration;
import java.util.Objects;
import java.util.function.Supplier;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ClockManager$AvailableClocks$$ExternalSyntheticLambda0 implements Supplier {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ClockManager$AvailableClocks$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        switch (this.$r8$classId) {
            case 0:
                return ((ClockPlugin) this.f$0).getTitle();
            default:
                GestureConfiguration gestureConfiguration = (GestureConfiguration) this.f$0;
                Range<Float> range = GestureConfiguration.SENSITIVITY_RANGE;
                Objects.requireNonNull(gestureConfiguration);
                return Float.valueOf(Settings.Secure.getFloatForUser(gestureConfiguration.mContext.getContentResolver(), "assist_gesture_sensitivity", 0.5f, -2));
        }
    }
}
