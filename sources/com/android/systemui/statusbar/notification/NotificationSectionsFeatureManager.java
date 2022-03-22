package com.android.systemui.statusbar.notification;

import android.content.Context;
import android.provider.DeviceConfig;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$anim;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.Utils;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationSectionsFeatureManager.kt */
/* loaded from: classes.dex */
public final class NotificationSectionsFeatureManager {
    public final Context context;
    public final DeviceConfigProxy proxy;

    @VisibleForTesting
    public final void clearCache() {
        R$anim.sUsePeopleFiltering = null;
    }

    public final boolean isFilteringEnabled() {
        DeviceConfigProxy deviceConfigProxy = this.proxy;
        if (R$anim.sUsePeopleFiltering == null) {
            Objects.requireNonNull(deviceConfigProxy);
            R$anim.sUsePeopleFiltering = Boolean.valueOf(DeviceConfig.getBoolean("systemui", "notifications_use_people_filtering", true));
        }
        Boolean bool = R$anim.sUsePeopleFiltering;
        Intrinsics.checkNotNull(bool);
        return bool.booleanValue();
    }

    public NotificationSectionsFeatureManager(DeviceConfigProxy deviceConfigProxy, Context context) {
        this.proxy = deviceConfigProxy;
        this.context = context;
    }

    public final int[] getNotificationBuckets() {
        if (isFilteringEnabled() && Utils.useQsMediaPlayer(this.context)) {
            return new int[]{2, 3, 1, 4, 5, 6};
        }
        if (!isFilteringEnabled() && Utils.useQsMediaPlayer(this.context)) {
            return new int[]{2, 3, 1, 5, 6};
        }
        if (!isFilteringEnabled() || Utils.useQsMediaPlayer(this.context)) {
            return new int[]{5, 6};
        }
        return new int[]{2, 3, 4, 5, 6};
    }
}
