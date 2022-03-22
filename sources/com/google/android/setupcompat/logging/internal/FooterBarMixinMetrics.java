package com.google.android.setupcompat.logging.internal;

import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public final class FooterBarMixinMetrics {
    public static final String EXTRA_PRIMARY_BUTTON_VISIBILITY = "PrimaryButtonVisibility";
    public static final String EXTRA_SECONDARY_BUTTON_VISIBILITY = "SecondaryButtonVisibility";
    public String primaryButtonVisibility = "Unknown";
    public String secondaryButtonVisibility = "Unknown";

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface FooterButtonVisibility {
    }

    public static String updateButtonVisibilityState(String str, boolean z) {
        if (!"VisibleUsingXml".equals(str) && !"Visible".equals(str) && !"Invisible".equals(str)) {
            throw new IllegalStateException(SupportMenuInflater$$ExternalSyntheticOutline0.m("Illegal visibility state: ", str));
        } else if (z && "Invisible".equals(str)) {
            return "Invisible_to_Visible";
        } else {
            if (z) {
                return str;
            }
            if ("VisibleUsingXml".equals(str)) {
                return "VisibleUsingXml_to_Invisible";
            }
            if ("Visible".equals(str)) {
                return "Visible_to_Invisible";
            }
            return str;
        }
    }
}
