package com.android.settingslib.media;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes.dex */
public final class DeviceIconUtil {
    public final HashMap mAudioDeviceTypeToIconMap = new HashMap();
    public final HashMap mMediaRouteTypeToIconMap = new HashMap();

    /* loaded from: classes.dex */
    public static class Device {
        public final int mAudioDeviceType;
        public final int mIconDrawableRes;
        public final int mMediaRouteType;

        public Device(int i, int i2, int i3) {
            this.mAudioDeviceType = i;
            this.mMediaRouteType = i2;
            this.mIconDrawableRes = i3;
        }
    }

    public DeviceIconUtil() {
        List asList = Arrays.asList(new Device(11, 11, 2131232001), new Device(22, 22, 2131232001), new Device(12, 12, 2131232001), new Device(13, 13, 2131232001), new Device(9, 9, 2131232001), new Device(3, 3, 2131232001), new Device(4, 4, 2131232001), new Device(2, 2, 2131232271));
        for (int i = 0; i < asList.size(); i++) {
            Device device = (Device) asList.get(i);
            this.mAudioDeviceTypeToIconMap.put(Integer.valueOf(device.mAudioDeviceType), device);
            this.mMediaRouteTypeToIconMap.put(Integer.valueOf(device.mMediaRouteType), device);
        }
    }
}
