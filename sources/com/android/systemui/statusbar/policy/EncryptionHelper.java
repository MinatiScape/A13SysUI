package com.android.systemui.statusbar.policy;

import android.sysprop.VoldProperties;
/* loaded from: classes.dex */
public final class EncryptionHelper {
    public static final boolean IS_DATA_ENCRYPTED;

    static {
        boolean z;
        String str = (String) VoldProperties.decrypt().orElse("");
        if ("1".equals(str) || "trigger_restart_min_framework".equals(str)) {
            z = true;
        } else {
            z = false;
        }
        IS_DATA_ENCRYPTED = z;
    }
}
