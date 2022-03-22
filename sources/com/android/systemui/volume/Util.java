package com.android.systemui.volume;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
/* loaded from: classes.dex */
public final class Util extends com.android.settingslib.volume.Util {
    public static String ringerModeToString(int i) {
        if (i == 0) {
            return "RINGER_MODE_SILENT";
        }
        if (i == 1) {
            return "RINGER_MODE_VIBRATE";
        }
        if (i != 2) {
            return VendorAtomValue$$ExternalSyntheticOutline0.m("RINGER_MODE_UNKNOWN_", i);
        }
        return "RINGER_MODE_NORMAL";
    }

    public static String logTag(Class<?> cls) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("vol.");
        m.append(cls.getSimpleName());
        String sb = m.toString();
        if (sb.length() < 23) {
            return sb;
        }
        return sb.substring(0, 23);
    }
}
