package com.android.systemui.assist;

import android.provider.DeviceConfig;
import com.android.systemui.DejankUtils;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public final class DeviceConfigHelper {
    public static long getLong(final String str, final long j) {
        return ((Long) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.systemui.assist.DeviceConfigHelper$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return Long.valueOf(DeviceConfig.getLong("systemui", str, j));
            }
        })).longValue();
    }
}
