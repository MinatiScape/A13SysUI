package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothLeAudio;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LeAudioProfile implements LocalBluetoothProfile {
    public final BluetoothAdapter mBluetoothAdapter;
    public final CachedBluetoothDeviceManager mDeviceManager;
    public final LocalBluetoothProfileManager mProfileManager;
    public BluetoothLeAudio mService;

    /* loaded from: classes.dex */
    public final class LeAudioServiceListener implements BluetoothProfile.ServiceListener {
        public LeAudioServiceListener() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public final void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            Log.d("LeAudioProfile", "Bluetooth service connected");
            BluetoothLeAudio bluetoothLeAudio = (BluetoothLeAudio) bluetoothProfile;
            LeAudioProfile.this.mService = bluetoothLeAudio;
            List connectedDevices = bluetoothLeAudio.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = LeAudioProfile.this.mDeviceManager.findDevice(bluetoothDevice);
                if (findDevice == null) {
                    Log.d("LeAudioProfile", "LeAudioProfile found new device: " + bluetoothDevice);
                    findDevice = LeAudioProfile.this.mDeviceManager.addDevice(bluetoothDevice);
                }
                findDevice.onProfileStateChanged(LeAudioProfile.this, 2);
                findDevice.refresh();
            }
            LeAudioProfile.this.mProfileManager.callServiceConnectedListeners();
            Objects.requireNonNull(LeAudioProfile.this);
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public final void onServiceDisconnected(int i) {
            Log.d("LeAudioProfile", "Bluetooth service disconnected");
            LeAudioProfile.this.mProfileManager.callServiceDisconnectedListeners();
            Objects.requireNonNull(LeAudioProfile.this);
        }
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final boolean accessProfileEnabled() {
        return true;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final int getDrawableResource(BluetoothClass bluetoothClass) {
        return 2131231774;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final int getProfileId() {
        return 22;
    }

    public final String toString() {
        return "LE_AUDIO";
    }

    public final void finalize() {
        Log.d("LeAudioProfile", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(22, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("LeAudioProfile", "Error cleaning up LeAudio proxy", th);
            }
        }
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothLeAudio bluetoothLeAudio = this.mService;
        if (bluetoothLeAudio == null) {
            return 0;
        }
        return bluetoothLeAudio.getConnectionState(bluetoothDevice);
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothLeAudio bluetoothLeAudio = this.mService;
        if (bluetoothLeAudio == null || bluetoothDevice == null) {
            return false;
        }
        if (!z) {
            return bluetoothLeAudio.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothLeAudio.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public LeAudioProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothAdapter = defaultAdapter;
        defaultAdapter.getProfileProxy(context, new LeAudioServiceListener(), 22);
    }
}
