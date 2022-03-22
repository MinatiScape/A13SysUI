package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import java.util.Objects;
@Deprecated
/* loaded from: classes.dex */
public final class LocalBluetoothAdapter {
    public static LocalBluetoothAdapter sInstance;
    public final BluetoothAdapter mAdapter;
    public LocalBluetoothProfileManager mProfileManager;
    public int mState = Integer.MIN_VALUE;

    public final void setBluetoothStateInt(int i) {
        LocalBluetoothProfileManager localBluetoothProfileManager;
        synchronized (this) {
            if (this.mState != i) {
                this.mState = i;
                if (i == 12 && (localBluetoothProfileManager = this.mProfileManager) != null) {
                    Objects.requireNonNull(localBluetoothProfileManager);
                    localBluetoothProfileManager.updateLocalProfiles();
                    localBluetoothProfileManager.mEventManager.readPairedDevices();
                }
            }
        }
    }

    public LocalBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.mAdapter = bluetoothAdapter;
    }
}
