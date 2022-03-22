package com.android.settingslib.applications;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.Resources;
/* loaded from: classes.dex */
public final class InterestingConfigChanges {
    public final int mFlags;
    public final Configuration mLastConfiguration = new Configuration();
    public int mLastDensity;

    @SuppressLint({"NewApi"})
    public final boolean applyNewConfig(Resources resources) {
        boolean z;
        Configuration configuration = this.mLastConfiguration;
        int updateFrom = configuration.updateFrom(Configuration.generateDelta(configuration, resources.getConfiguration()));
        if (this.mLastDensity != resources.getDisplayMetrics().densityDpi) {
            z = true;
        } else {
            z = false;
        }
        if (!z && (updateFrom & this.mFlags) == 0) {
            return false;
        }
        this.mLastDensity = resources.getDisplayMetrics().densityDpi;
        return true;
    }

    public InterestingConfigChanges(int i) {
        this.mFlags = i;
    }
}
