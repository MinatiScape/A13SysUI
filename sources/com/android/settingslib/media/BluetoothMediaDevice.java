package com.android.settingslib.media;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2Manager;
import com.android.settingslib.bluetooth.BluetoothUtils;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BluetoothMediaDevice extends MediaDevice {
    public final AudioManager mAudioManager;
    public CachedBluetoothDevice mCachedDevice;

    @Override // com.android.settingslib.media.MediaDevice
    public final Drawable getIcon() {
        return (Drawable) BluetoothUtils.getBtDrawableWithDescription(this.mContext, this.mCachedDevice).first;
    }

    @Override // com.android.settingslib.media.MediaDevice
    public final Drawable getIconWithoutBackground() {
        return (Drawable) BluetoothUtils.getBtClassDrawableWithDescription(this.mContext, this.mCachedDevice).first;
    }

    @Override // com.android.settingslib.media.MediaDevice
    public final String getId() {
        boolean z;
        CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevice;
        Objects.requireNonNull(cachedBluetoothDevice);
        long j = cachedBluetoothDevice.mHiSyncId;
        if (j != 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return Long.toString(j);
        }
        return cachedBluetoothDevice.getAddress();
    }

    @Override // com.android.settingslib.media.MediaDevice
    public final String getName() {
        return this.mCachedDevice.getName();
    }

    @Override // com.android.settingslib.media.MediaDevice
    public final boolean isCarKitDevice() {
        CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevice;
        Objects.requireNonNull(cachedBluetoothDevice);
        BluetoothClass bluetoothClass = cachedBluetoothDevice.mDevice.getBluetoothClass();
        if (bluetoothClass == null) {
            return false;
        }
        int deviceClass = bluetoothClass.getDeviceClass();
        if (deviceClass == 1032 || deviceClass == 1056) {
            return true;
        }
        return false;
    }

    @Override // com.android.settingslib.media.MediaDevice
    public final boolean isConnected() {
        if (this.mCachedDevice.getBondState() != 12 || !this.mCachedDevice.isConnected()) {
            return false;
        }
        return true;
    }

    @Override // com.android.settingslib.media.MediaDevice
    public final boolean isFastPairDevice() {
        boolean z;
        byte[] metadata;
        CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevice;
        if (cachedBluetoothDevice == null) {
            return false;
        }
        BluetoothDevice bluetoothDevice = cachedBluetoothDevice.mDevice;
        if (bluetoothDevice == null || (metadata = bluetoothDevice.getMetadata(6)) == null) {
            z = false;
        } else {
            z = Boolean.parseBoolean(new String(metadata));
        }
        if (z) {
            return true;
        }
        return false;
    }

    @Override // com.android.settingslib.media.MediaDevice
    public final boolean isMutingExpectedDevice() {
        if (this.mAudioManager.getMutingExpectedDevice() == null || !this.mCachedDevice.getAddress().equals(this.mAudioManager.getMutingExpectedDevice().getAddress())) {
            return false;
        }
        return true;
    }

    public BluetoothMediaDevice(Context context, CachedBluetoothDevice cachedBluetoothDevice, MediaRouter2Manager mediaRouter2Manager, MediaRoute2Info mediaRoute2Info, String str) {
        super(context, mediaRouter2Manager, mediaRoute2Info, str);
        this.mCachedDevice = cachedBluetoothDevice;
        this.mAudioManager = (AudioManager) context.getSystemService(AudioManager.class);
        initDeviceRecord();
    }
}
