package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import java.util.Objects;
/* loaded from: classes.dex */
public final class HidDeviceProfile implements LocalBluetoothProfile {
    public final CachedBluetoothDeviceManager mDeviceManager;
    public BluetoothHidDevice mService;

    /* loaded from: classes.dex */
    public final class HidDeviceServiceListener implements BluetoothProfile.ServiceListener {
        public HidDeviceServiceListener() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public final void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            BluetoothHidDevice bluetoothHidDevice = (BluetoothHidDevice) bluetoothProfile;
            HidDeviceProfile.this.mService = bluetoothHidDevice;
            for (BluetoothDevice bluetoothDevice : bluetoothHidDevice.getConnectedDevices()) {
                CachedBluetoothDevice findDevice = HidDeviceProfile.this.mDeviceManager.findDevice(bluetoothDevice);
                if (findDevice == null) {
                    Log.w("HidDeviceProfile", "HidProfile found new device: " + bluetoothDevice);
                    findDevice = HidDeviceProfile.this.mDeviceManager.addDevice(bluetoothDevice);
                }
                Log.d("HidDeviceProfile", "Connection status changed: " + findDevice);
                findDevice.onProfileStateChanged(HidDeviceProfile.this, 2);
                findDevice.refresh();
            }
            Objects.requireNonNull(HidDeviceProfile.this);
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public final void onServiceDisconnected(int i) {
            Objects.requireNonNull(HidDeviceProfile.this);
        }
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final boolean accessProfileEnabled() {
        return true;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302333;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final int getProfileId() {
        return 19;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        if (!z) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 0);
        }
        return false;
    }

    public final String toString() {
        return "HID DEVICE";
    }

    public final void finalize() {
        Log.d("HidDeviceProfile", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(19, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("HidDeviceProfile", "Error cleaning up HID proxy", th);
            }
        }
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothHidDevice bluetoothHidDevice = this.mService;
        if (bluetoothHidDevice == null) {
            return 0;
        }
        return bluetoothHidDevice.getConnectionState(bluetoothDevice);
    }

    public HidDeviceProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager) {
        this.mDeviceManager = cachedBluetoothDeviceManager;
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new HidDeviceServiceListener(), 19);
    }
}
