package com.android.settingslib.bluetooth;

import android.content.Context;
import android.os.Handler;
import android.os.UserHandle;
/* loaded from: classes.dex */
public final class LocalBluetoothManager {
    public final CachedBluetoothDeviceManager mCachedDeviceManager;
    public final BluetoothEventManager mEventManager;
    public final LocalBluetoothAdapter mLocalAdapter;
    public final LocalBluetoothProfileManager mProfileManager;

    public LocalBluetoothManager(LocalBluetoothAdapter localBluetoothAdapter, Context context, Handler handler, UserHandle userHandle) {
        Context applicationContext = context.getApplicationContext();
        this.mLocalAdapter = localBluetoothAdapter;
        CachedBluetoothDeviceManager cachedBluetoothDeviceManager = new CachedBluetoothDeviceManager(applicationContext, this);
        this.mCachedDeviceManager = cachedBluetoothDeviceManager;
        BluetoothEventManager bluetoothEventManager = new BluetoothEventManager(localBluetoothAdapter, cachedBluetoothDeviceManager, applicationContext, handler, userHandle);
        this.mEventManager = bluetoothEventManager;
        LocalBluetoothProfileManager localBluetoothProfileManager = new LocalBluetoothProfileManager(applicationContext, localBluetoothAdapter, cachedBluetoothDeviceManager, bluetoothEventManager);
        this.mProfileManager = localBluetoothProfileManager;
        localBluetoothProfileManager.updateLocalProfiles();
        bluetoothEventManager.readPairedDevices();
    }
}
