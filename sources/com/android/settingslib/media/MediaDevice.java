package com.android.settingslib.media;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2Manager;
import android.text.TextUtils;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class MediaDevice implements Comparable<MediaDevice> {
    public int mConnectedRecord;
    public final Context mContext;
    public final String mPackageName;
    public final MediaRoute2Info mRouteInfo;
    public final MediaRouter2Manager mRouterManager;
    public int mState;
    public int mType;

    public abstract Drawable getIcon();

    public abstract Drawable getIconWithoutBackground();

    public abstract String getId();

    public abstract String getName();

    public boolean isCarKitDevice() {
        return false;
    }

    public abstract boolean isConnected();

    public boolean isFastPairDevice() {
        return false;
    }

    public boolean isMutingExpectedDevice() {
        return false;
    }

    @Override // java.lang.Comparable
    public final int compareTo(MediaDevice mediaDevice) {
        String str;
        MediaDevice mediaDevice2 = mediaDevice;
        if (!(isConnected() ^ mediaDevice2.isConnected())) {
            int i = this.mType;
            int i2 = mediaDevice2.mType;
            if (i == i2) {
                if (isMutingExpectedDevice()) {
                    return -1;
                }
                if (!mediaDevice2.isMutingExpectedDevice()) {
                    if (isFastPairDevice()) {
                        return -1;
                    }
                    if (!mediaDevice2.isFastPairDevice()) {
                        if (isCarKitDevice()) {
                            return -1;
                        }
                        if (!mediaDevice2.isCarKitDevice()) {
                            ConnectionRecordManager instance = ConnectionRecordManager.getInstance();
                            Objects.requireNonNull(instance);
                            synchronized (instance) {
                                str = instance.mLastSelectedDevice;
                            }
                            if (TextUtils.equals(str, getId())) {
                                return -1;
                            }
                            if (!TextUtils.equals(str, mediaDevice2.getId())) {
                                int i3 = this.mConnectedRecord;
                                int i4 = mediaDevice2.mConnectedRecord;
                                if (i3 == i4 || (i4 <= 0 && i3 <= 0)) {
                                    return getName().compareToIgnoreCase(mediaDevice2.getName());
                                }
                                return i4 - i3;
                            }
                        }
                    }
                }
            } else if (i < i2) {
                return -1;
            }
        } else if (isConnected()) {
            return -1;
        }
        return 1;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof MediaDevice)) {
            return false;
        }
        return ((MediaDevice) obj).getId().equals(getId());
    }

    public MediaDevice(Context context, MediaRouter2Manager mediaRouter2Manager, MediaRoute2Info mediaRoute2Info, String str) {
        this.mContext = context;
        this.mRouteInfo = mediaRoute2Info;
        this.mRouterManager = mediaRouter2Manager;
        this.mPackageName = str;
        if (mediaRoute2Info == null) {
            this.mType = 4;
            return;
        }
        int type = mediaRoute2Info.getType();
        if (type == 2) {
            this.mType = 7;
        } else if (type == 3 || type == 4) {
            this.mType = 2;
        } else {
            if (type != 8) {
                if (!(type == 9 || type == 22)) {
                    if (!(type == 23 || type == 26)) {
                        if (type != 2000) {
                            switch (type) {
                                case QSTileImpl.H.STALE /* 11 */:
                                case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                                case QS.VERSION /* 13 */:
                                    break;
                                default:
                                    this.mType = 5;
                                    return;
                            }
                        } else {
                            this.mType = 6;
                            return;
                        }
                    }
                }
                this.mType = 1;
                return;
            }
            this.mType = 4;
        }
    }

    public final void initDeviceRecord() {
        int i;
        ConnectionRecordManager instance = ConnectionRecordManager.getInstance();
        Context context = this.mContext;
        Objects.requireNonNull(instance);
        synchronized (instance) {
            instance.mLastSelectedDevice = context.getSharedPreferences("seamless_transfer_record", 0).getString("last_selected_device", null);
        }
        ConnectionRecordManager instance2 = ConnectionRecordManager.getInstance();
        Context context2 = this.mContext;
        String id = getId();
        Objects.requireNonNull(instance2);
        synchronized (instance2) {
            i = context2.getSharedPreferences("seamless_transfer_record", 0).getInt(id, 0);
        }
        this.mConnectedRecord = i;
    }
}
