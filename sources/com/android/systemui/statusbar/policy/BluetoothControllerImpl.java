package com.android.systemui.statusbar.policy;

import android.app.ActivityManager;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import com.android.internal.annotations.GuardedBy;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.BluetoothEventManager;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothAdapter;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfile;
import com.android.settingslib.bluetooth.LocalBluetoothProfileManager;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.BluetoothController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class BluetoothControllerImpl implements BluetoothController, BluetoothCallback, CachedBluetoothDevice.Callback, LocalBluetoothProfileManager.ServiceListener {
    public static final boolean DEBUG = Log.isLoggable("BluetoothController", 3);
    public boolean mAudioProfileOnly;
    public final WeakHashMap<CachedBluetoothDevice, Object> mCachedState = new WeakHashMap<>();
    @GuardedBy({"mConnectedDevices"})
    public final ArrayList mConnectedDevices = new ArrayList();
    public int mConnectionState = 0;
    public final int mCurrentUser;
    public boolean mEnabled;
    public final H mHandler;
    public boolean mIsActive;
    public final LocalBluetoothManager mLocalBluetoothManager;
    public int mState;
    public final UserManager mUserManager;

    /* loaded from: classes.dex */
    public final class H extends Handler {
        public final ArrayList<BluetoothController.Callback> mCallbacks = new ArrayList<>();

        public H(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                Iterator<BluetoothController.Callback> it = this.mCallbacks.iterator();
                while (it.hasNext()) {
                    it.next().onBluetoothDevicesChanged();
                }
            } else if (i == 2) {
                Iterator<BluetoothController.Callback> it2 = this.mCallbacks.iterator();
                while (it2.hasNext()) {
                    boolean z = BluetoothControllerImpl.this.mEnabled;
                    it2.next().onBluetoothStateChange();
                }
            } else if (i == 3) {
                this.mCallbacks.add((BluetoothController.Callback) message.obj);
            } else if (i == 4) {
                this.mCallbacks.remove((BluetoothController.Callback) message.obj);
            }
        }
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfileManager.ServiceListener
    public final void onServiceDisconnected() {
    }

    public static String stateToString(int i) {
        if (i == 0) {
            return "DISCONNECTED";
        }
        if (i == 1) {
            return "CONNECTING";
        }
        if (i == 2) {
            return "CONNECTED";
        }
        if (i != 3) {
            return ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("UNKNOWN(", i, ")");
        }
        return "DISCONNECTING";
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(BluetoothController.Callback callback) {
        this.mHandler.obtainMessage(3, callback).sendToTarget();
        this.mHandler.sendEmptyMessage(2);
    }

    @Override // com.android.systemui.statusbar.policy.BluetoothController
    public final boolean canConfigBluetooth() {
        if (this.mUserManager.hasUserRestriction("no_config_bluetooth", UserHandle.of(this.mCurrentUser)) || this.mUserManager.hasUserRestriction("no_bluetooth", UserHandle.of(this.mCurrentUser))) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("BluetoothController state:");
        printWriter.print("  mLocalBluetoothManager=");
        printWriter.println(this.mLocalBluetoothManager);
        if (this.mLocalBluetoothManager != null) {
            printWriter.print("  mEnabled=");
            printWriter.println(this.mEnabled);
            printWriter.print("  mConnectionState=");
            printWriter.println(stateToString(this.mConnectionState));
            printWriter.print("  mAudioProfileOnly=");
            printWriter.println(this.mAudioProfileOnly);
            printWriter.print("  mIsActive=");
            printWriter.println(this.mIsActive);
            printWriter.print("  mConnectedDevices=");
            printWriter.println(getConnectedDevices());
            printWriter.print("  mCallbacks.size=");
            printWriter.println(this.mHandler.mCallbacks.size());
            printWriter.println("  Bluetooth Devices:");
            for (CachedBluetoothDevice cachedBluetoothDevice : getDevices()) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("    ");
                m.append(cachedBluetoothDevice.getName() + " " + cachedBluetoothDevice.getBondState() + " " + cachedBluetoothDevice.isConnected());
                printWriter.println(m.toString());
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.BluetoothController
    public final String getConnectedDeviceName() {
        synchronized (this.mConnectedDevices) {
            if (this.mConnectedDevices.size() != 1) {
                return null;
            }
            return ((CachedBluetoothDevice) this.mConnectedDevices.get(0)).getName();
        }
    }

    @Override // com.android.systemui.statusbar.policy.BluetoothController
    public final ArrayList getConnectedDevices() {
        ArrayList arrayList;
        synchronized (this.mConnectedDevices) {
            arrayList = new ArrayList(this.mConnectedDevices);
        }
        return arrayList;
    }

    public final ArrayList getDevices() {
        LocalBluetoothManager localBluetoothManager = this.mLocalBluetoothManager;
        if (localBluetoothManager != null) {
            return localBluetoothManager.mCachedDeviceManager.getCachedDevicesCopy();
        }
        return null;
    }

    @Override // com.android.systemui.statusbar.policy.BluetoothController
    public final boolean isBluetoothConnected() {
        if (this.mConnectionState == 2) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.policy.BluetoothController
    public final boolean isBluetoothConnecting() {
        if (this.mConnectionState == 1) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.policy.BluetoothController
    public final boolean isBluetoothSupported() {
        if (this.mLocalBluetoothManager != null) {
            return true;
        }
        return false;
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public final void onAclConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ACLConnectionStateChanged=");
            m.append(cachedBluetoothDevice.getAddress());
            m.append(" ");
            m.append(stateToString(i));
            Log.d("BluetoothController", m.toString());
        }
        this.mCachedState.remove(cachedBluetoothDevice);
        updateConnected();
        this.mHandler.sendEmptyMessage(2);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public final void onActiveDeviceChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ActiveDeviceChanged=");
            m.append(cachedBluetoothDevice.getAddress());
            m.append(" profileId=");
            m.append(i);
            Log.d("BluetoothController", m.toString());
        }
        boolean z = false;
        for (CachedBluetoothDevice cachedBluetoothDevice2 : getDevices()) {
            boolean z2 = true;
            if (!cachedBluetoothDevice2.isActiveDevice(1) && !cachedBluetoothDevice2.isActiveDevice(2) && !cachedBluetoothDevice2.isActiveDevice(21)) {
                z2 = false;
            }
            z |= z2;
        }
        if (this.mIsActive != z) {
            this.mIsActive = z;
            this.mHandler.sendEmptyMessage(2);
        }
        this.mHandler.sendEmptyMessage(2);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public final void onBluetoothStateChanged(int i) {
        boolean z;
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("BluetoothStateChanged=");
            m.append(stateToString(i));
            Log.d("BluetoothController", m.toString());
        }
        if (i == 12 || i == 11) {
            z = true;
        } else {
            z = false;
        }
        this.mEnabled = z;
        this.mState = i;
        updateConnected();
        this.mHandler.sendEmptyMessage(2);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public final void onConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ConnectionStateChanged=");
            m.append(cachedBluetoothDevice.getAddress());
            m.append(" ");
            m.append(stateToString(i));
            Log.d("BluetoothController", m.toString());
        }
        this.mCachedState.remove(cachedBluetoothDevice);
        updateConnected();
        this.mHandler.sendEmptyMessage(2);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public final void onDeviceAdded(CachedBluetoothDevice cachedBluetoothDevice) {
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("DeviceAdded=");
            m.append(cachedBluetoothDevice.getAddress());
            Log.d("BluetoothController", m.toString());
        }
        cachedBluetoothDevice.mCallbacks.add(this);
        updateConnected();
        this.mHandler.sendEmptyMessage(1);
    }

    @Override // com.android.settingslib.bluetooth.CachedBluetoothDevice.Callback
    public final void onDeviceAttributesChanged() {
        if (DEBUG) {
            Log.d("BluetoothController", "DeviceAttributesChanged");
        }
        updateConnected();
        this.mHandler.sendEmptyMessage(1);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public final void onDeviceBondStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("DeviceBondStateChanged=");
            m.append(cachedBluetoothDevice.getAddress());
            Log.d("BluetoothController", m.toString());
        }
        this.mCachedState.remove(cachedBluetoothDevice);
        updateConnected();
        this.mHandler.sendEmptyMessage(1);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public final void onDeviceDeleted(CachedBluetoothDevice cachedBluetoothDevice) {
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("DeviceDeleted=");
            m.append(cachedBluetoothDevice.getAddress());
            Log.d("BluetoothController", m.toString());
        }
        this.mCachedState.remove(cachedBluetoothDevice);
        updateConnected();
        this.mHandler.sendEmptyMessage(1);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public final void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ProfileConnectionStateChanged=");
            m.append(cachedBluetoothDevice.getAddress());
            m.append(" ");
            m.append(stateToString(i));
            m.append(" profileId=");
            m.append(i2);
            Log.d("BluetoothController", m.toString());
        }
        this.mCachedState.remove(cachedBluetoothDevice);
        updateConnected();
        this.mHandler.sendEmptyMessage(2);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(BluetoothController.Callback callback) {
        this.mHandler.obtainMessage(4, callback).sendToTarget();
    }

    @Override // com.android.systemui.statusbar.policy.BluetoothController
    public final void setBluetoothEnabled(boolean z) {
        boolean z2;
        int i;
        LocalBluetoothManager localBluetoothManager = this.mLocalBluetoothManager;
        if (localBluetoothManager != null) {
            LocalBluetoothAdapter localBluetoothAdapter = localBluetoothManager.mLocalAdapter;
            Objects.requireNonNull(localBluetoothAdapter);
            if (z) {
                z2 = localBluetoothAdapter.mAdapter.enable();
            } else {
                z2 = localBluetoothAdapter.mAdapter.disable();
            }
            if (z2) {
                if (z) {
                    i = 11;
                } else {
                    i = 13;
                }
                localBluetoothAdapter.setBluetoothStateInt(i);
            } else if (localBluetoothAdapter.mAdapter.getState() != localBluetoothAdapter.mState) {
                localBluetoothAdapter.setBluetoothStateInt(localBluetoothAdapter.mAdapter.getState());
            }
        }
    }

    public final void updateConnected() {
        boolean z;
        boolean z2;
        int i;
        int i2;
        LocalBluetoothManager localBluetoothManager = this.mLocalBluetoothManager;
        Objects.requireNonNull(localBluetoothManager);
        LocalBluetoothAdapter localBluetoothAdapter = localBluetoothManager.mLocalAdapter;
        Objects.requireNonNull(localBluetoothAdapter);
        int connectionState = localBluetoothAdapter.mAdapter.getConnectionState();
        ArrayList arrayList = new ArrayList();
        Iterator it = getDevices().iterator();
        while (true) {
            z = false;
            if (!it.hasNext()) {
                break;
            }
            CachedBluetoothDevice cachedBluetoothDevice = (CachedBluetoothDevice) it.next();
            Objects.requireNonNull(cachedBluetoothDevice);
            synchronized (cachedBluetoothDevice.mProfileLock) {
                try {
                    Iterator it2 = new ArrayList(cachedBluetoothDevice.mProfiles).iterator();
                    i = 0;
                    while (it2.hasNext()) {
                        LocalBluetoothProfile localBluetoothProfile = (LocalBluetoothProfile) it2.next();
                        if (localBluetoothProfile != null) {
                            i2 = localBluetoothProfile.getConnectionStatus(cachedBluetoothDevice.mDevice);
                        } else {
                            i2 = 0;
                        }
                        if (i2 > i) {
                            i = i2;
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (i > connectionState) {
                connectionState = i;
            }
            if (cachedBluetoothDevice.isConnected()) {
                arrayList.add(cachedBluetoothDevice);
            }
        }
        if (arrayList.isEmpty() && connectionState == 2) {
            connectionState = 0;
        }
        synchronized (this.mConnectedDevices) {
            this.mConnectedDevices.clear();
            this.mConnectedDevices.addAll(arrayList);
        }
        if (connectionState != this.mConnectionState) {
            this.mConnectionState = connectionState;
            this.mHandler.sendEmptyMessage(2);
        }
        boolean z3 = false;
        boolean z4 = false;
        for (CachedBluetoothDevice cachedBluetoothDevice2 : getDevices()) {
            Objects.requireNonNull(cachedBluetoothDevice2);
            Iterator it3 = new ArrayList(cachedBluetoothDevice2.mProfiles).iterator();
            while (it3.hasNext()) {
                LocalBluetoothProfile localBluetoothProfile2 = (LocalBluetoothProfile) it3.next();
                int profileId = localBluetoothProfile2.getProfileId();
                if (localBluetoothProfile2.getConnectionStatus(cachedBluetoothDevice2.mDevice) == 2) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (profileId == 1 || profileId == 2 || profileId == 21) {
                    z3 |= z2;
                } else {
                    z4 |= z2;
                }
            }
        }
        if (z3 && !z4) {
            z = true;
        }
        if (z != this.mAudioProfileOnly) {
            this.mAudioProfileOnly = z;
            this.mHandler.sendEmptyMessage(2);
        }
    }

    public BluetoothControllerImpl(Context context, DumpManager dumpManager, Looper looper, Looper looper2, LocalBluetoothManager localBluetoothManager) {
        int i;
        this.mLocalBluetoothManager = localBluetoothManager;
        new Handler(looper);
        this.mHandler = new H(looper2);
        if (localBluetoothManager != null) {
            BluetoothEventManager bluetoothEventManager = localBluetoothManager.mEventManager;
            Objects.requireNonNull(bluetoothEventManager);
            bluetoothEventManager.mCallbacks.add(this);
            LocalBluetoothProfileManager localBluetoothProfileManager = localBluetoothManager.mProfileManager;
            Objects.requireNonNull(localBluetoothProfileManager);
            localBluetoothProfileManager.mServiceListeners.add(this);
            LocalBluetoothAdapter localBluetoothAdapter = localBluetoothManager.mLocalAdapter;
            Objects.requireNonNull(localBluetoothAdapter);
            synchronized (localBluetoothAdapter) {
                if (localBluetoothAdapter.mAdapter.getState() != localBluetoothAdapter.mState) {
                    localBluetoothAdapter.setBluetoothStateInt(localBluetoothAdapter.mAdapter.getState());
                }
                i = localBluetoothAdapter.mState;
            }
            onBluetoothStateChanged(i);
        }
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mCurrentUser = ActivityManager.getCurrentUser();
        dumpManager.registerDumpable("BluetoothController", this);
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfileManager.ServiceListener
    public final void onServiceConnected() {
        updateConnected();
        this.mHandler.sendEmptyMessage(1);
    }

    @Override // com.android.systemui.statusbar.policy.BluetoothController
    public final int getBluetoothState() {
        return this.mState;
    }

    @Override // com.android.systemui.statusbar.policy.BluetoothController
    public final boolean isBluetoothAudioActive() {
        return this.mIsActive;
    }

    @Override // com.android.systemui.statusbar.policy.BluetoothController
    public final boolean isBluetoothAudioProfileOnly() {
        return this.mAudioProfileOnly;
    }

    @Override // com.android.systemui.statusbar.policy.BluetoothController
    public final boolean isBluetoothEnabled() {
        return this.mEnabled;
    }
}
