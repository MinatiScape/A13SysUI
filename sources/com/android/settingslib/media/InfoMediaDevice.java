package com.android.settingslib.media;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2Manager;
import java.util.List;
/* loaded from: classes.dex */
public final class InfoMediaDevice extends MediaDevice {
    @Override // com.android.settingslib.media.MediaDevice
    public final boolean isConnected() {
        return true;
    }

    public int getDrawableResId() {
        int type = this.mRouteInfo.getType();
        if (type == 1001) {
            return 2131232036;
        }
        if (type != 2000) {
            return 2131232048;
        }
        return 2131232037;
    }

    public int getDrawableResIdByFeature() {
        List<String> features = this.mRouteInfo.getFeatures();
        if (features.contains("android.media.route.feature.REMOTE_GROUP_PLAYBACK")) {
            return 2131232037;
        }
        if (features.contains("android.media.route.feature.REMOTE_VIDEO_PLAYBACK")) {
            return 2131232036;
        }
        return 2131232048;
    }

    @Override // com.android.settingslib.media.MediaDevice
    public final Drawable getIconWithoutBackground() {
        return this.mContext.getDrawable(getDrawableResIdByFeature());
    }

    @Override // com.android.settingslib.media.MediaDevice
    public final String getId() {
        return this.mRouteInfo.getId();
    }

    @Override // com.android.settingslib.media.MediaDevice
    public final String getName() {
        return this.mRouteInfo.getName().toString();
    }

    public InfoMediaDevice(Context context, MediaRouter2Manager mediaRouter2Manager, MediaRoute2Info mediaRoute2Info, String str) {
        super(context, mediaRouter2Manager, mediaRoute2Info, str);
        initDeviceRecord();
    }

    @Override // com.android.settingslib.media.MediaDevice
    public final Drawable getIcon() {
        return getIconWithoutBackground();
    }
}
