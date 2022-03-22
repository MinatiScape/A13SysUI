package com.android.systemui.media.dialog;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3;
import com.android.settingslib.media.MediaDevice;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MediaOutputMetricLogger {
    public static final boolean DEBUG = Log.isLoggable("MediaOutputMetricLogger", 3);
    public int mConnectedBluetoothDeviceCount;
    public final Context mContext;
    public final String mPackageName;
    public int mRemoteDeviceCount;
    public MediaDevice mSourceDevice;
    public MediaDevice mTargetDevice;
    public int mWiredDeviceCount;

    public final void updateLoggingDeviceCount(List<MediaDevice> list) {
        this.mRemoteDeviceCount = 0;
        this.mConnectedBluetoothDeviceCount = 0;
        this.mWiredDeviceCount = 0;
        for (MediaDevice mediaDevice : list) {
            if (mediaDevice.isConnected()) {
                int i = mediaDevice.mType;
                if (i == 1 || i == 2) {
                    this.mWiredDeviceCount++;
                } else if (i == 4) {
                    this.mConnectedBluetoothDeviceCount++;
                } else if (i == 5 || i == 6) {
                    this.mRemoteDeviceCount++;
                }
            }
        }
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("connected devices: wired: ");
            m.append(this.mWiredDeviceCount);
            m.append(" bluetooth: ");
            m.append(this.mConnectedBluetoothDeviceCount);
            m.append(" remote: ");
            KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(m, this.mRemoteDeviceCount, "MediaOutputMetricLogger");
        }
    }

    public final String getLoggingPackageName() {
        String str = this.mPackageName;
        if (str == null || str.isEmpty()) {
            return "";
        }
        try {
            int i = this.mContext.getPackageManager().getApplicationInfo(this.mPackageName, 0).flags;
            if ((i & 1) == 0 && (i & 128) == 0) {
                return "";
            }
            return this.mPackageName;
        } catch (Exception unused) {
            Log.e("MediaOutputMetricLogger", this.mPackageName + " is invalid.");
            return "";
        }
    }

    public MediaOutputMetricLogger(Context context, String str) {
        this.mContext = context;
        this.mPackageName = str;
    }

    public static int getLoggingDeviceType(MediaDevice mediaDevice) {
        Objects.requireNonNull(mediaDevice);
        int i = mediaDevice.mType;
        if (i == 1) {
            return 200;
        }
        if (i == 2) {
            return 100;
        }
        if (i == 4) {
            return 300;
        }
        if (i == 5) {
            return 400;
        }
        if (i == 6) {
            return 500;
        }
        if (i != 7) {
            return 0;
        }
        return 1;
    }
}
