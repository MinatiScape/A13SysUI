package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHearingAid;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class HearingAidDeviceManager {
    public final LocalBluetoothManager mBtManager;
    public final List<CachedBluetoothDevice> mCachedDevices;

    public final boolean onProfileConnectionStateChangedIfProcessed(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        if (i == 0) {
            CachedBluetoothDevice findMainDevice = findMainDevice(cachedBluetoothDevice);
            if (cachedBluetoothDevice.mUnpairing) {
                return true;
            }
            if (findMainDevice != null) {
                findMainDevice.refresh();
                return true;
            }
            CachedBluetoothDevice cachedBluetoothDevice2 = cachedBluetoothDevice.mSubDevice;
            if (cachedBluetoothDevice2 == null || !cachedBluetoothDevice2.isConnected()) {
                return false;
            }
            LocalBluetoothManager localBluetoothManager = this.mBtManager;
            Objects.requireNonNull(localBluetoothManager);
            localBluetoothManager.mEventManager.dispatchDeviceRemoved(cachedBluetoothDevice);
            cachedBluetoothDevice.switchSubDeviceContent();
            cachedBluetoothDevice.refresh();
            LocalBluetoothManager localBluetoothManager2 = this.mBtManager;
            Objects.requireNonNull(localBluetoothManager2);
            localBluetoothManager2.mEventManager.dispatchDeviceAdded(cachedBluetoothDevice);
            return true;
        } else if (i != 2) {
            return false;
        } else {
            onHiSyncIdChanged(cachedBluetoothDevice.mHiSyncId);
            CachedBluetoothDevice findMainDevice2 = findMainDevice(cachedBluetoothDevice);
            if (findMainDevice2 == null) {
                return false;
            }
            if (findMainDevice2.isConnected()) {
                findMainDevice2.refresh();
                return true;
            }
            LocalBluetoothManager localBluetoothManager3 = this.mBtManager;
            Objects.requireNonNull(localBluetoothManager3);
            localBluetoothManager3.mEventManager.dispatchDeviceRemoved(findMainDevice2);
            findMainDevice2.switchSubDeviceContent();
            findMainDevice2.refresh();
            LocalBluetoothManager localBluetoothManager4 = this.mBtManager;
            Objects.requireNonNull(localBluetoothManager4);
            localBluetoothManager4.mEventManager.dispatchDeviceAdded(findMainDevice2);
            return true;
        }
    }

    public final CachedBluetoothDevice findMainDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        boolean z;
        CachedBluetoothDevice cachedBluetoothDevice2;
        for (CachedBluetoothDevice cachedBluetoothDevice3 : this.mCachedDevices) {
            Objects.requireNonNull(cachedBluetoothDevice3);
            if (cachedBluetoothDevice3.mHiSyncId != 0) {
                z = true;
            } else {
                z = false;
            }
            if (z && (cachedBluetoothDevice2 = cachedBluetoothDevice3.mSubDevice) != null && cachedBluetoothDevice2.equals(cachedBluetoothDevice)) {
                return cachedBluetoothDevice3;
            }
        }
        return null;
    }

    @VisibleForTesting
    public void onHiSyncIdChanged(long j) {
        CachedBluetoothDevice cachedBluetoothDevice;
        int size = this.mCachedDevices.size() - 1;
        int i = -1;
        while (size >= 0) {
            CachedBluetoothDevice cachedBluetoothDevice2 = this.mCachedDevices.get(size);
            Objects.requireNonNull(cachedBluetoothDevice2);
            if (cachedBluetoothDevice2.mHiSyncId == j) {
                if (i == -1) {
                    i = size;
                } else {
                    if (cachedBluetoothDevice2.isConnected()) {
                        cachedBluetoothDevice = this.mCachedDevices.get(i);
                        size = i;
                    } else {
                        cachedBluetoothDevice2 = this.mCachedDevices.get(i);
                        cachedBluetoothDevice = cachedBluetoothDevice2;
                    }
                    Objects.requireNonNull(cachedBluetoothDevice2);
                    cachedBluetoothDevice2.mSubDevice = cachedBluetoothDevice;
                    this.mCachedDevices.remove(size);
                    Log.d("HearingAidDeviceManager", "onHiSyncIdChanged: removed from UI device =" + cachedBluetoothDevice + ", with hiSyncId=" + j);
                    LocalBluetoothManager localBluetoothManager = this.mBtManager;
                    Objects.requireNonNull(localBluetoothManager);
                    localBluetoothManager.mEventManager.dispatchDeviceRemoved(cachedBluetoothDevice);
                    return;
                }
            }
            size--;
        }
    }

    public final void updateHearingAidsDevices() {
        boolean z;
        long j;
        BluetoothHearingAid bluetoothHearingAid;
        HashSet hashSet = new HashSet();
        for (CachedBluetoothDevice cachedBluetoothDevice : this.mCachedDevices) {
            Objects.requireNonNull(cachedBluetoothDevice);
            boolean z2 = true;
            if (cachedBluetoothDevice.mHiSyncId != 0) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                BluetoothDevice bluetoothDevice = cachedBluetoothDevice.mDevice;
                LocalBluetoothManager localBluetoothManager = this.mBtManager;
                Objects.requireNonNull(localBluetoothManager);
                LocalBluetoothProfileManager localBluetoothProfileManager = localBluetoothManager.mProfileManager;
                Objects.requireNonNull(localBluetoothProfileManager);
                HearingAidProfile hearingAidProfile = localBluetoothProfileManager.mHearingAidProfile;
                if (hearingAidProfile == null || (bluetoothHearingAid = hearingAidProfile.mService) == null || bluetoothDevice == null) {
                    j = 0;
                } else {
                    j = bluetoothHearingAid.getHiSyncId(bluetoothDevice);
                }
                if (j == 0) {
                    z2 = false;
                }
                if (z2) {
                    cachedBluetoothDevice.mHiSyncId = j;
                    hashSet.add(Long.valueOf(j));
                }
            }
        }
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            onHiSyncIdChanged(((Long) it.next()).longValue());
        }
    }

    public HearingAidDeviceManager(LocalBluetoothManager localBluetoothManager, ArrayList arrayList) {
        this.mBtManager = localBluetoothManager;
        this.mCachedDevices = arrayList;
    }
}
