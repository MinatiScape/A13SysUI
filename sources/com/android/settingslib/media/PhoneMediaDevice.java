package com.android.settingslib.media;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2Manager;
import com.android.settingslib.media.DeviceIconUtil;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PhoneMediaDevice extends MediaDevice {
    public final DeviceIconUtil mDeviceIconUtil = new DeviceIconUtil();

    @Override // com.android.settingslib.media.MediaDevice
    public final boolean isConnected() {
        return true;
    }

    public int getDrawableResId() {
        DeviceIconUtil deviceIconUtil = this.mDeviceIconUtil;
        int type = this.mRouteInfo.getType();
        Objects.requireNonNull(deviceIconUtil);
        if (deviceIconUtil.mMediaRouteTypeToIconMap.containsKey(Integer.valueOf(type))) {
            return ((DeviceIconUtil.Device) deviceIconUtil.mMediaRouteTypeToIconMap.get(Integer.valueOf(type))).mIconDrawableRes;
        }
        return 2131232271;
    }

    @Override // com.android.settingslib.media.MediaDevice
    public final Drawable getIconWithoutBackground() {
        return this.mContext.getDrawable(getDrawableResId());
    }

    @Override // com.android.settingslib.media.MediaDevice
    public final String getId() {
        int type = this.mRouteInfo.getType();
        if (type == 3 || type == 4) {
            return "wired_headset_media_device_id";
        }
        if (!(type == 9 || type == 22)) {
            switch (type) {
                case QSTileImpl.H.STALE /* 11 */:
                case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                case QS.VERSION /* 13 */:
                    break;
                default:
                    return "phone_media_device_id";
            }
        }
        return "usb_headset_media_device_id";
    }

    @Override // com.android.settingslib.media.MediaDevice
    public final String getName() {
        CharSequence charSequence;
        int type = this.mRouteInfo.getType();
        if (!(type == 3 || type == 4)) {
            if (type != 9) {
                if (type != 22) {
                    switch (type) {
                        case QSTileImpl.H.STALE /* 11 */:
                        case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                            break;
                        case QS.VERSION /* 13 */:
                            break;
                        default:
                            charSequence = this.mContext.getString(2131952752);
                            break;
                    }
                    return charSequence.toString();
                }
            }
            charSequence = this.mRouteInfo.getName();
            return charSequence.toString();
        }
        charSequence = this.mContext.getString(2131952756);
        return charSequence.toString();
    }

    public PhoneMediaDevice(Context context, MediaRouter2Manager mediaRouter2Manager, MediaRoute2Info mediaRoute2Info, String str) {
        super(context, mediaRouter2Manager, mediaRoute2Info, str);
        initDeviceRecord();
    }

    @Override // com.android.settingslib.media.MediaDevice
    public final Drawable getIcon() {
        return getIconWithoutBackground();
    }
}
