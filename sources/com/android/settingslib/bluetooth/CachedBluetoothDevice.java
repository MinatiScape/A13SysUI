package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.content.SharedPreferences;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.util.LruCache;
import androidx.recyclerview.R$dimen;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline1;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda16;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public final class CachedBluetoothDevice implements Comparable<CachedBluetoothDevice> {
    public long mConnectAttempted;
    public final Context mContext;
    public BluetoothDevice mDevice;
    public LruCache<String, BitmapDrawable> mDrawableCache;
    public int mGroupId;
    public long mHiSyncId;
    public boolean mJustDiscovered;
    public boolean mLocalNapRoleConnected;
    public final LocalBluetoothProfileManager mProfileManager;
    public short mRssi;
    public CachedBluetoothDevice mSubDevice;
    public boolean mUnpairing;
    public final Object mProfileLock = new Object();
    public final CopyOnWriteArrayList mProfiles = new CopyOnWriteArrayList();
    public final CopyOnWriteArrayList mRemovedProfiles = new CopyOnWriteArrayList();
    public final CopyOnWriteArrayList mCallbacks = new CopyOnWriteArrayList();
    public boolean mIsActiveDeviceA2dp = false;
    public boolean mIsActiveDeviceHeadset = false;
    public boolean mIsActiveDeviceHearingAid = false;
    public boolean mIsActiveDeviceLeAudio = false;
    public HashSet mMemberDevices = new HashSet();
    public final AnonymousClass1 mHandler = new Handler(Looper.getMainLooper()) { // from class: com.android.settingslib.bluetooth.CachedBluetoothDevice.1
        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                Objects.requireNonNull(CachedBluetoothDevice.this);
            } else if (i == 2) {
                Objects.requireNonNull(CachedBluetoothDevice.this);
            } else if (i == 21) {
                Objects.requireNonNull(CachedBluetoothDevice.this);
            } else if (i != 22) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("handleMessage(): unknown message : ");
                m.append(message.what);
                Log.w("CachedBluetoothDevice", m.toString());
            } else {
                Objects.requireNonNull(CachedBluetoothDevice.this);
            }
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Connect to profile : ");
            m2.append(message.what);
            m2.append(" timeout, show error message !");
            Log.w("CachedBluetoothDevice", m2.toString());
            CachedBluetoothDevice.this.refresh();
        }
    };
    public final BluetoothAdapter mLocalAdapter = BluetoothAdapter.getDefaultAdapter();

    /* loaded from: classes.dex */
    public interface Callback {
        void onDeviceAttributesChanged();
    }

    public boolean isActiveDevice(int i) {
        if (i == 1) {
            return this.mIsActiveDeviceHeadset;
        }
        if (i == 2) {
            return this.mIsActiveDeviceA2dp;
        }
        if (i == 21) {
            return this.mIsActiveDeviceHearingAid;
        }
        if (i == 22) {
            return this.mIsActiveDeviceLeAudio;
        }
        GridLayoutManager$$ExternalSyntheticOutline1.m("getActiveDevice: unknown profile ", i, "CachedBluetoothDevice");
        return false;
    }

    public void setProfileConnectedStatus(int i, boolean z) {
        if (i != 1 && i != 2 && i != 21 && i != 22) {
            GridLayoutManager$$ExternalSyntheticOutline1.m("setProfileConnectedStatus(): unknown profile id : ", i, "CachedBluetoothDevice");
        }
    }

    @Override // java.lang.Comparable
    public final int compareTo(CachedBluetoothDevice cachedBluetoothDevice) {
        int i;
        CachedBluetoothDevice cachedBluetoothDevice2 = cachedBluetoothDevice;
        int i2 = (cachedBluetoothDevice2.isConnected() ? 1 : 0) - (isConnected() ? 1 : 0);
        if (i2 != 0) {
            return i2;
        }
        int i3 = 1;
        if (cachedBluetoothDevice2.getBondState() == 12) {
            i = 1;
        } else {
            i = 0;
        }
        if (getBondState() != 12) {
            i3 = 0;
        }
        int i4 = i - i3;
        if (i4 != 0) {
            return i4;
        }
        int i5 = (cachedBluetoothDevice2.mJustDiscovered ? 1 : 0) - (this.mJustDiscovered ? 1 : 0);
        if (i5 != 0) {
            return i5;
        }
        int i6 = cachedBluetoothDevice2.mRssi - this.mRssi;
        if (i6 != 0) {
            return i6;
        }
        return getName().compareTo(cachedBluetoothDevice2.getName());
    }

    public final void connectDevice() {
        synchronized (this.mProfileLock) {
            if (this.mProfiles.isEmpty()) {
                Log.d("CachedBluetoothDevice", "No profiles. Maybe we will connect later for device " + this.mDevice);
                return;
            }
            this.mDevice.connect();
        }
    }

    public final void dispatchAttributesChanged() {
        Iterator it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            ((Callback) it.next()).onDeviceAttributesChanged();
        }
    }

    public final boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CachedBluetoothDevice)) {
            return false;
        }
        return this.mDevice.equals(((CachedBluetoothDevice) obj).mDevice);
    }

    public final void fetchActiveDevices() {
        List list;
        List list2;
        BluetoothDevice bluetoothDevice;
        LocalBluetoothProfileManager localBluetoothProfileManager = this.mProfileManager;
        Objects.requireNonNull(localBluetoothProfileManager);
        A2dpProfile a2dpProfile = localBluetoothProfileManager.mA2dpProfile;
        BluetoothDevice bluetoothDevice2 = null;
        if (a2dpProfile != null) {
            BluetoothDevice bluetoothDevice3 = this.mDevice;
            BluetoothAdapter bluetoothAdapter = a2dpProfile.mBluetoothAdapter;
            if (bluetoothAdapter != null) {
                List activeDevices = bluetoothAdapter.getActiveDevices(2);
                if (activeDevices.size() > 0) {
                    bluetoothDevice = (BluetoothDevice) activeDevices.get(0);
                    this.mIsActiveDeviceA2dp = bluetoothDevice3.equals(bluetoothDevice);
                }
            }
            bluetoothDevice = null;
            this.mIsActiveDeviceA2dp = bluetoothDevice3.equals(bluetoothDevice);
        }
        LocalBluetoothProfileManager localBluetoothProfileManager2 = this.mProfileManager;
        Objects.requireNonNull(localBluetoothProfileManager2);
        HeadsetProfile headsetProfile = localBluetoothProfileManager2.mHeadsetProfile;
        if (headsetProfile != null) {
            BluetoothDevice bluetoothDevice4 = this.mDevice;
            BluetoothAdapter bluetoothAdapter2 = headsetProfile.mBluetoothAdapter;
            if (bluetoothAdapter2 != null) {
                List activeDevices2 = bluetoothAdapter2.getActiveDevices(1);
                if (activeDevices2.size() > 0) {
                    bluetoothDevice2 = (BluetoothDevice) activeDevices2.get(0);
                }
            }
            this.mIsActiveDeviceHeadset = bluetoothDevice4.equals(bluetoothDevice2);
        }
        LocalBluetoothProfileManager localBluetoothProfileManager3 = this.mProfileManager;
        Objects.requireNonNull(localBluetoothProfileManager3);
        HearingAidProfile hearingAidProfile = localBluetoothProfileManager3.mHearingAidProfile;
        if (hearingAidProfile != null) {
            BluetoothAdapter bluetoothAdapter3 = hearingAidProfile.mBluetoothAdapter;
            if (bluetoothAdapter3 == null) {
                list2 = new ArrayList();
            } else {
                list2 = bluetoothAdapter3.getActiveDevices(21);
            }
            this.mIsActiveDeviceHearingAid = list2.contains(this.mDevice);
        }
        LocalBluetoothProfileManager localBluetoothProfileManager4 = this.mProfileManager;
        Objects.requireNonNull(localBluetoothProfileManager4);
        LeAudioProfile leAudioProfile = localBluetoothProfileManager4.mLeAudioProfile;
        if (leAudioProfile != null) {
            BluetoothAdapter bluetoothAdapter4 = leAudioProfile.mBluetoothAdapter;
            if (bluetoothAdapter4 == null) {
                list = new ArrayList();
            } else {
                list = bluetoothAdapter4.getActiveDevices(22);
            }
            this.mIsActiveDeviceLeAudio = list.contains(this.mDevice);
        }
    }

    public final String getAddress() {
        return this.mDevice.getAddress();
    }

    public final int getBondState() {
        return this.mDevice.getBondState();
    }

    public final String getName() {
        String alias = this.mDevice.getAlias();
        if (TextUtils.isEmpty(alias)) {
            return getAddress();
        }
        return alias;
    }

    public final int hashCode() {
        return this.mDevice.getAddress().hashCode();
    }

    public final boolean isBusy() {
        int i;
        synchronized (this.mProfileLock) {
            Iterator it = this.mProfiles.iterator();
            do {
                boolean z = true;
                i = 0;
                if (it.hasNext()) {
                    LocalBluetoothProfile localBluetoothProfile = (LocalBluetoothProfile) it.next();
                    if (localBluetoothProfile != null) {
                        i = localBluetoothProfile.getConnectionStatus(this.mDevice);
                    }
                    if (i == 1) {
                        break;
                    }
                } else {
                    if (getBondState() != 11) {
                        z = false;
                    }
                    return z;
                }
            } while (i != 3);
            return true;
        }
    }

    public final boolean isConnected() {
        int i;
        synchronized (this.mProfileLock) {
            Iterator it = this.mProfiles.iterator();
            do {
                i = 0;
                if (!it.hasNext()) {
                    return false;
                }
                LocalBluetoothProfile localBluetoothProfile = (LocalBluetoothProfile) it.next();
                if (localBluetoothProfile != null) {
                    i = localBluetoothProfile.getConnectionStatus(this.mDevice);
                }
            } while (i != 2);
            return true;
        }
    }

    public final void onProfileStateChanged(LocalBluetoothProfile localBluetoothProfile, int i) {
        Log.d("CachedBluetoothDevice", "onProfileStateChanged: profile " + localBluetoothProfile + ", device " + this.mDevice.getAnonymizedAddress() + ", newProfileState " + i);
        if (this.mLocalAdapter.getState() == 13) {
            Log.d("CachedBluetoothDevice", " BT Turninig Off...Profile conn state change ignored...");
            return;
        }
        synchronized (this.mProfileLock) {
            try {
                boolean z = true;
                boolean z2 = false;
                if ((localBluetoothProfile instanceof A2dpProfile) || (localBluetoothProfile instanceof HeadsetProfile) || (localBluetoothProfile instanceof HearingAidProfile)) {
                    setProfileConnectedStatus(localBluetoothProfile.getProfileId(), false);
                    if (i != 0) {
                        if (i == 1) {
                            sendEmptyMessageDelayed(localBluetoothProfile.getProfileId(), 60000L);
                        } else if (i == 2) {
                            removeMessages(localBluetoothProfile.getProfileId());
                        } else if (i != 3) {
                            Log.w("CachedBluetoothDevice", "onProfileStateChanged(): unknown profile state : " + i);
                        } else if (hasMessages(localBluetoothProfile.getProfileId())) {
                            removeMessages(localBluetoothProfile.getProfileId());
                        }
                    } else if (hasMessages(localBluetoothProfile.getProfileId())) {
                        removeMessages(localBluetoothProfile.getProfileId());
                        setProfileConnectedStatus(localBluetoothProfile.getProfileId(), true);
                    }
                }
                if (i == 2) {
                    if (localBluetoothProfile instanceof MapProfile) {
                        localBluetoothProfile.setEnabled(this.mDevice, true);
                    }
                    if (!this.mProfiles.contains(localBluetoothProfile)) {
                        this.mRemovedProfiles.remove(localBluetoothProfile);
                        this.mProfiles.add(localBluetoothProfile);
                        if (localBluetoothProfile instanceof PanProfile) {
                            PanProfile panProfile = (PanProfile) localBluetoothProfile;
                            BluetoothDevice bluetoothDevice = this.mDevice;
                            Objects.requireNonNull(panProfile);
                            if (panProfile.mDeviceRoleMap.containsKey(bluetoothDevice) && panProfile.mDeviceRoleMap.get(bluetoothDevice).intValue() == 1) {
                                z2 = true;
                            }
                            if (z2) {
                                this.mLocalNapRoleConnected = true;
                            }
                        }
                    }
                } else if ((localBluetoothProfile instanceof MapProfile) && i == 0) {
                    localBluetoothProfile.setEnabled(this.mDevice, false);
                } else if (this.mLocalNapRoleConnected && (localBluetoothProfile instanceof PanProfile)) {
                    PanProfile panProfile2 = (PanProfile) localBluetoothProfile;
                    BluetoothDevice bluetoothDevice2 = this.mDevice;
                    Objects.requireNonNull(panProfile2);
                    if (!panProfile2.mDeviceRoleMap.containsKey(bluetoothDevice2) || panProfile2.mDeviceRoleMap.get(bluetoothDevice2).intValue() != 1) {
                        z = false;
                    }
                    if (z && i == 0) {
                        Log.d("CachedBluetoothDevice", "Removing PanProfile from device after NAP disconnect");
                        this.mProfiles.remove(localBluetoothProfile);
                        this.mRemovedProfiles.add(localBluetoothProfile);
                        this.mLocalNapRoleConnected = false;
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        fetchActiveDevices();
    }

    public final void refresh() {
        R$dimen.postOnBackgroundThread(new BubbleStackView$$ExternalSyntheticLambda16(this, 1));
    }

    public final boolean startPairing() {
        if (this.mLocalAdapter.isDiscovering()) {
            this.mLocalAdapter.cancelDiscovery();
        }
        if (!this.mDevice.createBond()) {
            return false;
        }
        return true;
    }

    public final void switchMemberDeviceContent(CachedBluetoothDevice cachedBluetoothDevice, CachedBluetoothDevice cachedBluetoothDevice2) {
        BluetoothDevice bluetoothDevice = this.mDevice;
        short s = this.mRssi;
        boolean z = this.mJustDiscovered;
        this.mDevice = cachedBluetoothDevice2.mDevice;
        this.mRssi = cachedBluetoothDevice2.mRssi;
        this.mJustDiscovered = cachedBluetoothDevice2.mJustDiscovered;
        this.mMemberDevices.add(cachedBluetoothDevice);
        this.mMemberDevices.remove(cachedBluetoothDevice2);
        cachedBluetoothDevice2.mDevice = bluetoothDevice;
        cachedBluetoothDevice2.mRssi = s;
        cachedBluetoothDevice2.mJustDiscovered = z;
        fetchActiveDevices();
    }

    public final void switchSubDeviceContent() {
        BluetoothDevice bluetoothDevice = this.mDevice;
        short s = this.mRssi;
        boolean z = this.mJustDiscovered;
        CachedBluetoothDevice cachedBluetoothDevice = this.mSubDevice;
        this.mDevice = cachedBluetoothDevice.mDevice;
        this.mRssi = cachedBluetoothDevice.mRssi;
        this.mJustDiscovered = cachedBluetoothDevice.mJustDiscovered;
        cachedBluetoothDevice.mDevice = bluetoothDevice;
        cachedBluetoothDevice.mRssi = s;
        cachedBluetoothDevice.mJustDiscovered = z;
        fetchActiveDevices();
    }

    public final String toString() {
        return this.mDevice.toString();
    }

    public final boolean updateProfiles() {
        ParcelUuid[] uuids;
        ParcelUuid[] uuids2 = this.mDevice.getUuids();
        if (uuids2 == null || (uuids = this.mLocalAdapter.getUuids()) == null) {
            return false;
        }
        if (this.mDevice.getBondState() == 12 && BluetoothUuid.containsAnyUuid(this.mDevice.getUuids(), PbapServerProfile.PBAB_CLIENT_UUIDS)) {
            BluetoothClass bluetoothClass = this.mDevice.getBluetoothClass();
            if (this.mDevice.getPhonebookAccessPermission() == 0) {
                if (bluetoothClass != null && (bluetoothClass.getDeviceClass() == 1032 || bluetoothClass.getDeviceClass() == 1028)) {
                    EventLog.writeEvent(1397638484, "138529441", -1, "");
                }
                this.mDevice.setPhonebookAccessPermission(2);
            }
        }
        synchronized (this.mProfileLock) {
            this.mProfileManager.updateProfiles(uuids2, uuids, this.mProfiles, this.mRemovedProfiles, this.mLocalNapRoleConnected, this.mDevice);
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("updating profiles for ");
        m.append(this.mDevice.getAnonymizedAddress());
        Log.d("CachedBluetoothDevice", m.toString());
        BluetoothClass bluetoothClass2 = this.mDevice.getBluetoothClass();
        if (bluetoothClass2 != null) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Class: ");
            m2.append(bluetoothClass2.toString());
            Log.v("CachedBluetoothDevice", m2.toString());
        }
        Log.v("CachedBluetoothDevice", "UUID:");
        for (ParcelUuid parcelUuid : uuids2) {
            Log.v("CachedBluetoothDevice", "  " + parcelUuid);
        }
        return true;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.settingslib.bluetooth.CachedBluetoothDevice$1] */
    public CachedBluetoothDevice(Context context, LocalBluetoothProfileManager localBluetoothProfileManager, BluetoothDevice bluetoothDevice) {
        this.mContext = context;
        this.mProfileManager = localBluetoothProfileManager;
        this.mDevice = bluetoothDevice;
        updateProfiles();
        fetchActiveDevices();
        SharedPreferences sharedPreferences = context.getSharedPreferences("bluetooth_phonebook_permission", 0);
        if (sharedPreferences.contains(this.mDevice.getAddress())) {
            if (this.mDevice.getPhonebookAccessPermission() == 0) {
                int i = sharedPreferences.getInt(this.mDevice.getAddress(), 0);
                if (i == 1) {
                    this.mDevice.setPhonebookAccessPermission(1);
                } else if (i == 2) {
                    this.mDevice.setPhonebookAccessPermission(2);
                }
            }
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(this.mDevice.getAddress());
            edit.commit();
        }
        SharedPreferences sharedPreferences2 = context.getSharedPreferences("bluetooth_message_permission", 0);
        if (sharedPreferences2.contains(this.mDevice.getAddress())) {
            if (this.mDevice.getMessageAccessPermission() == 0) {
                int i2 = sharedPreferences2.getInt(this.mDevice.getAddress(), 0);
                if (i2 == 1) {
                    this.mDevice.setMessageAccessPermission(1);
                } else if (i2 == 2) {
                    this.mDevice.setMessageAccessPermission(2);
                }
            }
            SharedPreferences.Editor edit2 = sharedPreferences2.edit();
            edit2.remove(this.mDevice.getAddress());
            edit2.commit();
        }
        dispatchAttributesChanged();
        this.mHiSyncId = 0L;
        this.mGroupId = -1;
        this.mDrawableCache = new LruCache<String, BitmapDrawable>(((int) (Runtime.getRuntime().maxMemory() / 1024)) / 8) { // from class: com.android.settingslib.bluetooth.CachedBluetoothDevice.2
            @Override // android.util.LruCache
            public final int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return bitmapDrawable.getBitmap().getByteCount() / 1024;
            }
        };
        this.mUnpairing = false;
    }

    public final void connect$1() {
        boolean z;
        if (getBondState() == 10) {
            startPairing();
            z = false;
        } else {
            z = true;
        }
        if (z) {
            this.mConnectAttempted = SystemClock.elapsedRealtime();
            connectDevice();
        }
    }

    public final void unpair() {
        BluetoothDevice bluetoothDevice;
        int bondState = getBondState();
        if (bondState == 11) {
            this.mDevice.cancelBondProcess();
        }
        if (bondState != 10 && (bluetoothDevice = this.mDevice) != null) {
            this.mUnpairing = true;
            if (bluetoothDevice.removeBond()) {
                this.mDrawableCache.evictAll();
                StringBuilder sb = new StringBuilder();
                sb.append("Command sent successfully:REMOVE_BOND ");
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Address:");
                m.append(this.mDevice);
                sb.append(m.toString());
                Log.d("CachedBluetoothDevice", sb.toString());
            }
        }
    }
}
