package com.android.systemui.statusbar.notification;

import android.util.Log;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
/* compiled from: NotifPipelineFlags.kt */
/* loaded from: classes.dex */
public final class NotifPipelineFlags {
    public final FeatureFlags featureFlags;

    public final boolean isNewPipelineEnabled() {
        return this.featureFlags.isEnabled(Flags.NEW_NOTIFICATION_PIPELINE_RENDERING);
    }

    public NotifPipelineFlags(FeatureFlags featureFlags) {
        this.featureFlags = featureFlags;
    }

    public final boolean checkLegacyPipelineEnabled() {
        if (!isNewPipelineEnabled()) {
            return true;
        }
        if (!this.featureFlags.isEnabled(Flags.NEW_PIPELINE_CRASH_ON_CALL_TO_OLD_PIPELINE)) {
            Log.d("NotifPipeline", "Old pipeline code running with new pipeline enabled", new Exception());
            return false;
        }
        throw new RuntimeException("Old pipeline code running with new pipeline enabled");
    }
}
