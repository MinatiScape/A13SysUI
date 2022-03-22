package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothCsipSetCoordinator;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothUuid;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public final class CsipDeviceManager {
    public final LocalBluetoothManager mBtManager;
    public final List<CachedBluetoothDevice> mCachedDevices;

    public final CachedBluetoothDevice findMainDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        boolean z;
        List<CachedBluetoothDevice> list = this.mCachedDevices;
        if (list == null) {
            return null;
        }
        for (CachedBluetoothDevice cachedBluetoothDevice2 : list) {
            Objects.requireNonNull(cachedBluetoothDevice2);
            if (cachedBluetoothDevice2.mGroupId != -1) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                HashSet<CachedBluetoothDevice> hashSet = cachedBluetoothDevice2.mMemberDevices;
                if (hashSet.isEmpty()) {
                    continue;
                } else {
                    for (CachedBluetoothDevice cachedBluetoothDevice3 : hashSet) {
                        if (cachedBluetoothDevice3 != null && cachedBluetoothDevice3.equals(cachedBluetoothDevice)) {
                            return cachedBluetoothDevice2;
                        }
                    }
                    continue;
                }
            }
        }
        return null;
    }

    public final int getBaseGroupId(BluetoothDevice bluetoothDevice) {
        Map map;
        LocalBluetoothManager localBluetoothManager = this.mBtManager;
        Objects.requireNonNull(localBluetoothManager);
        LocalBluetoothProfileManager localBluetoothProfileManager = localBluetoothManager.mProfileManager;
        Objects.requireNonNull(localBluetoothProfileManager);
        CsipSetCoordinatorProfile csipSetCoordinatorProfile = localBluetoothProfileManager.mCsipSetCoordinatorProfile;
        if (csipSetCoordinatorProfile != null) {
            BluetoothCsipSetCoordinator bluetoothCsipSetCoordinator = csipSetCoordinatorProfile.mService;
            if (bluetoothCsipSetCoordinator == null || bluetoothDevice == null) {
                map = null;
            } else {
                map = bluetoothCsipSetCoordinator.getGroupUuidMapByDevice(bluetoothDevice);
            }
            if (map == null) {
                return -1;
            }
            for (Map.Entry entry : map.entrySet()) {
                if (((ParcelUuid) entry.getValue()).equals(BluetoothUuid.CAP)) {
                    return ((Integer) entry.getKey()).intValue();
                }
            }
        }
        return -1;
    }

    public final void initCsipDeviceIfNeeded(CachedBluetoothDevice cachedBluetoothDevice) {
        boolean z;
        int baseGroupId = getBaseGroupId(cachedBluetoothDevice.mDevice);
        if (baseGroupId != -1) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            Log.d("CsipDeviceManager", "initCsipDeviceIfNeeded: " + cachedBluetoothDevice + " (group: " + baseGroupId + ")");
            cachedBluetoothDevice.mGroupId = baseGroupId;
        }
    }

    @VisibleForTesting
    public void onGroupIdChanged(int i) {
        CachedBluetoothDevice cachedBluetoothDevice = null;
        int i2 = -1;
        for (int size = this.mCachedDevices.size() - 1; size >= 0; size--) {
            CachedBluetoothDevice cachedBluetoothDevice2 = this.mCachedDevices.get(size);
            Objects.requireNonNull(cachedBluetoothDevice2);
            if (cachedBluetoothDevice2.mGroupId == i) {
                if (i2 == -1) {
                    i2 = size;
                    cachedBluetoothDevice = cachedBluetoothDevice2;
                } else {
                    Log.d("CsipDeviceManager", "onGroupIdChanged: removed from UI device =" + cachedBluetoothDevice2 + ", with groupId=" + i + " firstMatchedIndex=" + i2);
                    Objects.requireNonNull(cachedBluetoothDevice);
                    cachedBluetoothDevice.mMemberDevices.add(cachedBluetoothDevice2);
                    this.mCachedDevices.remove(size);
                    LocalBluetoothManager localBluetoothManager = this.mBtManager;
                    Objects.requireNonNull(localBluetoothManager);
                    localBluetoothManager.mEventManager.dispatchDeviceRemoved(cachedBluetoothDevice2);
                    return;
                }
            }
        }
    }

    public final boolean onProfileConnectionStateChangedIfProcessed(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        Log.d("CsipDeviceManager", "onProfileConnectionStateChangedIfProcessed: " + cachedBluetoothDevice + ", state: " + i);
        if (i == 0) {
            CachedBluetoothDevice findMainDevice = findMainDevice(cachedBluetoothDevice);
            if (findMainDevice != null) {
                findMainDevice.refresh();
                return true;
            }
            HashSet<CachedBluetoothDevice> hashSet = cachedBluetoothDevice.mMemberDevices;
            if (hashSet.isEmpty()) {
                return false;
            }
            for (CachedBluetoothDevice cachedBluetoothDevice2 : hashSet) {
                if (cachedBluetoothDevice2.isConnected()) {
                    LocalBluetoothManager localBluetoothManager = this.mBtManager;
                    Objects.requireNonNull(localBluetoothManager);
                    localBluetoothManager.mEventManager.dispatchDeviceRemoved(cachedBluetoothDevice);
                    cachedBluetoothDevice.switchMemberDeviceContent(cachedBluetoothDevice2, cachedBluetoothDevice);
                    cachedBluetoothDevice.refresh();
                    LocalBluetoothManager localBluetoothManager2 = this.mBtManager;
                    Objects.requireNonNull(localBluetoothManager2);
                    localBluetoothManager2.mEventManager.dispatchDeviceAdded(cachedBluetoothDevice);
                    return true;
                }
            }
            return false;
        } else if (i != 2) {
            return false;
        } else {
            onGroupIdChanged(cachedBluetoothDevice.mGroupId);
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
            findMainDevice2.switchMemberDeviceContent(findMainDevice2, cachedBluetoothDevice);
            findMainDevice2.refresh();
            LocalBluetoothManager localBluetoothManager4 = this.mBtManager;
            Objects.requireNonNull(localBluetoothManager4);
            localBluetoothManager4.mEventManager.dispatchDeviceAdded(findMainDevice2);
            return true;
        }
    }

    public final boolean setMemberDeviceIfNeeded(CachedBluetoothDevice cachedBluetoothDevice) {
        boolean z;
        CachedBluetoothDevice cachedBluetoothDevice2;
        int i = cachedBluetoothDevice.mGroupId;
        if (i != -1) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            int size = this.mCachedDevices.size();
            while (true) {
                size--;
                if (size < 0) {
                    cachedBluetoothDevice2 = null;
                    break;
                }
                cachedBluetoothDevice2 = this.mCachedDevices.get(size);
                Objects.requireNonNull(cachedBluetoothDevice2);
                if (cachedBluetoothDevice2.mGroupId == i) {
                    break;
                }
            }
            Log.d("CsipDeviceManager", "setMemberDeviceIfNeeded, main: " + cachedBluetoothDevice2 + ", member: " + cachedBluetoothDevice);
            if (cachedBluetoothDevice2 != null) {
                cachedBluetoothDevice2.mMemberDevices.add(cachedBluetoothDevice);
                String name = cachedBluetoothDevice2.getName();
                if (name != null && !TextUtils.equals(name, cachedBluetoothDevice.getName())) {
                    cachedBluetoothDevice.mDevice.setAlias(name);
                    cachedBluetoothDevice.dispatchAttributesChanged();
                }
                return true;
            }
        }
        return false;
    }

    public final void updateCsipDevices() {
        boolean z;
        HashSet hashSet = new HashSet();
        for (CachedBluetoothDevice cachedBluetoothDevice : this.mCachedDevices) {
            Objects.requireNonNull(cachedBluetoothDevice);
            boolean z2 = true;
            if (cachedBluetoothDevice.mGroupId != -1) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                int baseGroupId = getBaseGroupId(cachedBluetoothDevice.mDevice);
                if (baseGroupId == -1) {
                    z2 = false;
                }
                if (z2) {
                    cachedBluetoothDevice.mGroupId = baseGroupId;
                    hashSet.add(Integer.valueOf(baseGroupId));
                }
            }
        }
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            onGroupIdChanged(((Integer) it.next()).intValue());
        }
    }

    public CsipDeviceManager(LocalBluetoothManager localBluetoothManager, ArrayList arrayList) {
        this.mBtManager = localBluetoothManager;
        this.mCachedDevices = arrayList;
    }
}
