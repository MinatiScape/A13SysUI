package com.android.settingslib.devicestate;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import com.android.internal.annotations.VisibleForTesting;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class DeviceStateRotationLockSettingsManager {
    public static DeviceStateRotationLockSettingsManager sSingleton;
    public final ContentResolver mContentResolver;
    public final String[] mDeviceStateRotationLockDefaults;
    public SparseIntArray mDeviceStateRotationLockFallbackSettings;
    public SparseIntArray mDeviceStateRotationLockSettings;
    public final HashSet mListeners = new HashSet();

    /* loaded from: classes.dex */
    public interface DeviceStateRotationLockSettingsListener {
        void onSettingsChanged();
    }

    public final int getRotationLockSetting(int i) {
        int i2 = 0;
        int i3 = this.mDeviceStateRotationLockSettings.get(i, 0);
        if (i3 != 0) {
            return i3;
        }
        int indexOfKey = this.mDeviceStateRotationLockFallbackSettings.indexOfKey(i);
        if (indexOfKey < 0) {
            Log.w("DSRotLockSettingsMngr", "Setting is ignored, but no fallback was specified.");
        } else {
            i2 = this.mDeviceStateRotationLockSettings.get(this.mDeviceStateRotationLockFallbackSettings.valueAt(indexOfKey), 0);
        }
        return i2;
    }

    public final void initializeInMemoryMap() {
        String stringForUser = Settings.Secure.getStringForUser(this.mContentResolver, "device_state_rotation_lock", -2);
        if (TextUtils.isEmpty(stringForUser)) {
            loadDefaults();
            persistSettings();
            return;
        }
        String[] split = stringForUser.split(":");
        if (split.length % 2 != 0) {
            Log.wtf("DSRotLockSettingsMngr", "Can't deserialize saved settings, falling back on defaults");
            loadDefaults();
            persistSettings();
            return;
        }
        this.mDeviceStateRotationLockSettings = new SparseIntArray(split.length / 2);
        int i = 0;
        while (i < split.length - 1) {
            int i2 = i + 1;
            try {
                int i3 = i2 + 1;
                this.mDeviceStateRotationLockSettings.put(Integer.parseInt(split[i]), Integer.parseInt(split[i2]));
                i = i3;
            } catch (NumberFormatException e) {
                Log.wtf("DSRotLockSettingsMngr", "Error deserializing one of the saved settings", e);
                loadDefaults();
                persistSettings();
                return;
            }
        }
    }

    public final void loadDefaults() {
        String[] strArr;
        this.mDeviceStateRotationLockSettings = new SparseIntArray(this.mDeviceStateRotationLockDefaults.length);
        this.mDeviceStateRotationLockFallbackSettings = new SparseIntArray(1);
        for (String str : this.mDeviceStateRotationLockDefaults) {
            String[] split = str.split(":");
            try {
                int parseInt = Integer.parseInt(split[0]);
                int parseInt2 = Integer.parseInt(split[1]);
                if (parseInt2 == 0) {
                    if (split.length == 3) {
                        this.mDeviceStateRotationLockFallbackSettings.put(parseInt, Integer.parseInt(split[2]));
                    } else {
                        Log.w("DSRotLockSettingsMngr", "Rotation lock setting is IGNORED, but values have unexpected size of " + split.length);
                    }
                }
                this.mDeviceStateRotationLockSettings.put(parseInt, parseInt2);
            } catch (NumberFormatException e) {
                Log.wtf("DSRotLockSettingsMngr", "Error parsing settings entry. Entry was: " + str, e);
                return;
            }
        }
    }

    public final void persistSettings() {
        if (this.mDeviceStateRotationLockSettings.size() == 0) {
            Settings.Secure.putStringForUser(this.mContentResolver, "device_state_rotation_lock", "", -2);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.mDeviceStateRotationLockSettings.keyAt(0));
        sb.append(":");
        sb.append(this.mDeviceStateRotationLockSettings.valueAt(0));
        for (int i = 1; i < this.mDeviceStateRotationLockSettings.size(); i++) {
            sb.append(":");
            sb.append(this.mDeviceStateRotationLockSettings.keyAt(i));
            sb.append(":");
            sb.append(this.mDeviceStateRotationLockSettings.valueAt(i));
        }
        Settings.Secure.putStringForUser(this.mContentResolver, "device_state_rotation_lock", sb.toString(), -2);
    }

    public DeviceStateRotationLockSettingsManager(Context context) {
        Handler main = Handler.getMain();
        this.mContentResolver = context.getContentResolver();
        this.mDeviceStateRotationLockDefaults = context.getResources().getStringArray(17236100);
        loadDefaults();
        initializeInMemoryMap();
        context.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("device_state_rotation_lock"), false, new ContentObserver(main) { // from class: com.android.settingslib.devicestate.DeviceStateRotationLockSettingsManager.1
            @Override // android.database.ContentObserver
            public final void onChange(boolean z) {
                DeviceStateRotationLockSettingsManager.this.onPersistedSettingsChanged();
            }
        }, -2);
    }

    @VisibleForTesting
    public void onPersistedSettingsChanged() {
        initializeInMemoryMap();
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            ((DeviceStateRotationLockSettingsListener) it.next()).onSettingsChanged();
        }
    }
}
