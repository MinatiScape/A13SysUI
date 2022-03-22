package com.android.systemui.power;

import com.android.settingslib.fuelgauge.Estimate;
/* loaded from: classes.dex */
public final class EnhancedEstimatesImpl implements EnhancedEstimates {
    @Override // com.android.systemui.power.EnhancedEstimates
    public final boolean getLowWarningEnabled() {
        return true;
    }

    @Override // com.android.systemui.power.EnhancedEstimates
    public final long getLowWarningThreshold() {
        return 0L;
    }

    @Override // com.android.systemui.power.EnhancedEstimates
    public final long getSevereWarningThreshold() {
        return 0L;
    }

    @Override // com.android.systemui.power.EnhancedEstimates
    public final boolean isHybridNotificationEnabled() {
        return false;
    }

    @Override // com.android.systemui.power.EnhancedEstimates
    public final Estimate getEstimate() {
        return new Estimate(-1L, false, -1L);
    }
}
