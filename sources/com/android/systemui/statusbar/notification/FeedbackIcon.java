package com.android.systemui.statusbar.notification;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.core.graphics.Insets$$ExternalSyntheticOutline0;
/* compiled from: FeedbackIcon.kt */
/* loaded from: classes.dex */
public final class FeedbackIcon {
    public final int contentDescRes;
    public final int iconRes;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FeedbackIcon)) {
            return false;
        }
        FeedbackIcon feedbackIcon = (FeedbackIcon) obj;
        return this.iconRes == feedbackIcon.iconRes && this.contentDescRes == feedbackIcon.contentDescRes;
    }

    public final int hashCode() {
        return Integer.hashCode(this.contentDescRes) + (Integer.hashCode(this.iconRes) * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("FeedbackIcon(iconRes=");
        m.append(this.iconRes);
        m.append(", contentDescRes=");
        return Insets$$ExternalSyntheticOutline0.m(m, this.contentDescRes, ')');
    }

    public FeedbackIcon(int i, int i2) {
        this.iconRes = i;
        this.contentDescRes = i2;
    }
}
