package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothPbapClient;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PbapClientProfile implements LocalBluetoothProfile {
    public static final ParcelUuid[] SRC_UUIDS = {BluetoothUuid.PBAP_PSE};
    public final CachedBluetoothDeviceManager mDeviceManager;
    public BluetoothPbapClient mService;

    /* loaded from: classes.dex */
    public final class PbapClientServiceListener implements BluetoothProfile.ServiceListener {
        public PbapClientServiceListener() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public final void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            BluetoothPbapClient bluetoothPbapClient = (BluetoothPbapClient) bluetoothProfile;
            PbapClientProfile.this.mService = bluetoothPbapClient;
            List connectedDevices = bluetoothPbapClient.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = PbapClientProfile.this.mDeviceManager.findDevice(bluetoothDevice);
                if (findDevice == null) {
                    Log.w("PbapClientProfile", "PbapClientProfile found new device: " + bluetoothDevice);
                    findDevice = PbapClientProfile.this.mDeviceManager.addDevice(bluetoothDevice);
                }
                findDevice.onProfileStateChanged(PbapClientProfile.this, 2);
                findDevice.refresh();
            }
            Objects.requireNonNull(PbapClientProfile.this);
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public final void onServiceDisconnected(int i) {
            Objects.requireNonNull(PbapClientProfile.this);
        }
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final boolean accessProfileEnabled() {
        return true;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302810;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final int getProfileId() {
        return 17;
    }

    public final String toString() {
        return "PbapClient";
    }

    public final void finalize() {
        Log.d("PbapClientProfile", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(17, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("PbapClientProfile", "Error cleaning up PBAP Client proxy", th);
            }
        }
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothPbapClient bluetoothPbapClient = this.mService;
        if (bluetoothPbapClient == null) {
            return 0;
        }
        return bluetoothPbapClient.getConnectionState(bluetoothDevice);
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothPbapClient bluetoothPbapClient = this.mService;
        if (bluetoothPbapClient == null) {
            return false;
        }
        if (!z) {
            return bluetoothPbapClient.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothPbapClient.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public PbapClientProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager) {
        this.mDeviceManager = cachedBluetoothDeviceManager;
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new PbapClientServiceListener(), 17);
    }
}
