package com.android.systemui.unfold.config;

import android.content.Context;
import android.os.SystemProperties;
/* compiled from: ResourceUnfoldTransitionConfig.kt */
/* loaded from: classes.dex */
public final class ResourceUnfoldTransitionConfig implements UnfoldTransitionConfig {
    public final Context context;

    @Override // com.android.systemui.unfold.config.UnfoldTransitionConfig
    public final boolean isEnabled() {
        boolean z;
        if (this.context.getResources().getBoolean(17891788)) {
            if (SystemProperties.getInt("persist.unfold.transition_enabled", 1) == 1) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.systemui.unfold.config.UnfoldTransitionConfig
    public final boolean isHingeAngleEnabled() {
        return this.context.getResources().getBoolean(17891789);
    }

    public ResourceUnfoldTransitionConfig(Context context) {
        this.context = context;
    }
}
