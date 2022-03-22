package com.google.android.systemui.communal.dock.conditions;

import com.android.systemui.util.condition.Condition;
/* loaded from: classes.dex */
public final class TimeoutToUserZeroFeatureCondition extends Condition {
    public final boolean mEnabled;

    @Override // com.android.systemui.util.condition.Condition
    public final void stop() {
    }

    @Override // com.android.systemui.util.condition.Condition
    public final void start() {
        updateCondition(this.mEnabled);
    }

    public TimeoutToUserZeroFeatureCondition(boolean z) {
        this.mEnabled = z;
    }
}
