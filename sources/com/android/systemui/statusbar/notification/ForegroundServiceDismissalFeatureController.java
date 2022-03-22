package com.android.systemui.statusbar.notification;

import android.provider.DeviceConfig;
import androidx.leanback.R$layout;
import com.android.systemui.util.DeviceConfigProxy;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ForegroundServiceDismissalFeatureController.kt */
/* loaded from: classes.dex */
public final class ForegroundServiceDismissalFeatureController {
    public final DeviceConfigProxy proxy;

    public final boolean isForegroundServiceDismissalEnabled() {
        DeviceConfigProxy deviceConfigProxy = this.proxy;
        if (R$layout.sIsEnabled == null) {
            Objects.requireNonNull(deviceConfigProxy);
            R$layout.sIsEnabled = Boolean.valueOf(DeviceConfig.getBoolean("systemui", "notifications_allow_fgs_dismissal", false));
        }
        Boolean bool = R$layout.sIsEnabled;
        Intrinsics.checkNotNull(bool);
        return bool.booleanValue();
    }

    public ForegroundServiceDismissalFeatureController(DeviceConfigProxy deviceConfigProxy) {
        this.proxy = deviceConfigProxy;
    }
}
