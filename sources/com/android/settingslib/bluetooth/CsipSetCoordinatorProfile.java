package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothCsipSetCoordinator;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class CsipSetCoordinatorProfile implements LocalBluetoothProfile {
    public final CachedBluetoothDeviceManager mDeviceManager;
    public final LocalBluetoothProfileManager mProfileManager;
    public BluetoothCsipSetCoordinator mService;

    /* loaded from: classes.dex */
    public final class CoordinatedSetServiceListener implements BluetoothProfile.ServiceListener {
        public CoordinatedSetServiceListener() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public final void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            Log.d("CsipSetCoordinatorProfile", "Bluetooth service connected");
            BluetoothCsipSetCoordinator bluetoothCsipSetCoordinator = (BluetoothCsipSetCoordinator) bluetoothProfile;
            CsipSetCoordinatorProfile.this.mService = bluetoothCsipSetCoordinator;
            List connectedDevices = bluetoothCsipSetCoordinator.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = CsipSetCoordinatorProfile.this.mDeviceManager.findDevice(bluetoothDevice);
                if (findDevice == null) {
                    Log.d("CsipSetCoordinatorProfile", "CsipSetCoordinatorProfile found new device: " + bluetoothDevice);
                    findDevice = CsipSetCoordinatorProfile.this.mDeviceManager.addDevice(bluetoothDevice);
                }
                findDevice.onProfileStateChanged(CsipSetCoordinatorProfile.this, 2);
                findDevice.refresh();
            }
            CachedBluetoothDeviceManager cachedBluetoothDeviceManager = CsipSetCoordinatorProfile.this.mDeviceManager;
            Objects.requireNonNull(cachedBluetoothDeviceManager);
            synchronized (cachedBluetoothDeviceManager) {
                cachedBluetoothDeviceManager.mCsipDeviceManager.updateCsipDevices();
            }
            CsipSetCoordinatorProfile.this.mProfileManager.callServiceConnectedListeners();
            Objects.requireNonNull(CsipSetCoordinatorProfile.this);
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public final void onServiceDisconnected(int i) {
            Log.d("CsipSetCoordinatorProfile", "Bluetooth service disconnected");
            CsipSetCoordinatorProfile.this.mProfileManager.callServiceDisconnectedListeners();
            Objects.requireNonNull(CsipSetCoordinatorProfile.this);
        }
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final boolean accessProfileEnabled() {
        return false;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final int getDrawableResource(BluetoothClass bluetoothClass) {
        return 0;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final int getProfileId() {
        return 25;
    }

    public final String toString() {
        return "CSIP Set Coordinator";
    }

    public final void finalize() {
        Log.d("CsipSetCoordinatorProfile", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(25, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("CsipSetCoordinatorProfile", "Error cleaning up CSIP Set Coordinator proxy", th);
            }
        }
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothCsipSetCoordinator bluetoothCsipSetCoordinator = this.mService;
        if (bluetoothCsipSetCoordinator == null) {
            return 0;
        }
        return bluetoothCsipSetCoordinator.getConnectionState(bluetoothDevice);
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public final boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothCsipSetCoordinator bluetoothCsipSetCoordinator = this.mService;
        if (bluetoothCsipSetCoordinator == null || bluetoothDevice == null) {
            return false;
        }
        if (!z) {
            return bluetoothCsipSetCoordinator.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothCsipSetCoordinator.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public CsipSetCoordinatorProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new CoordinatedSetServiceListener(), 25);
    }
}
