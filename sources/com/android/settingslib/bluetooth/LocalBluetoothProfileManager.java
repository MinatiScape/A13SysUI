package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothCsipSetCoordinator;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHearingAid;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.ParcelUuid;
import android.util.Log;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.CollectionUtils;
import com.android.settingslib.bluetooth.BluetoothEventManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public final class LocalBluetoothProfileManager {
    public A2dpProfile mA2dpProfile;
    public A2dpSinkProfile mA2dpSinkProfile;
    public final Context mContext;
    public CsipSetCoordinatorProfile mCsipSetCoordinatorProfile;
    public final CachedBluetoothDeviceManager mDeviceManager;
    public final BluetoothEventManager mEventManager;
    public HeadsetProfile mHeadsetProfile;
    public HearingAidProfile mHearingAidProfile;
    public HfpClientProfile mHfpClientProfile;
    public HidDeviceProfile mHidDeviceProfile;
    public HidProfile mHidProfile;
    public LeAudioProfile mLeAudioProfile;
    public MapClientProfile mMapClientProfile;
    public MapProfile mMapProfile;
    public OppProfile mOppProfile;
    public PanProfile mPanProfile;
    public PbapClientProfile mPbapClientProfile;
    public PbapServerProfile mPbapProfile;
    public SapProfile mSapProfile;
    public VolumeControlProfile mVolumeControlProfile;
    public final HashMap mProfileNameMap = new HashMap();
    public final CopyOnWriteArrayList mServiceListeners = new CopyOnWriteArrayList();

    /* loaded from: classes.dex */
    public class HeadsetStateChangeHandler extends StateChangedHandler {
        public final String mAudioChangeAction;
        public final int mAudioDisconnectedState;

        @Override // com.android.settingslib.bluetooth.LocalBluetoothProfileManager.StateChangedHandler
        public final void onReceiveInternal(Intent intent, CachedBluetoothDevice cachedBluetoothDevice) {
            if (this.mAudioChangeAction.equals(intent.getAction())) {
                if (intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0) != this.mAudioDisconnectedState) {
                    cachedBluetoothDevice.onProfileStateChanged(this.mProfile, 2);
                }
                cachedBluetoothDevice.refresh();
                return;
            }
            super.onReceiveInternal(intent, cachedBluetoothDevice);
        }

        public HeadsetStateChangeHandler(LocalBluetoothProfileManager localBluetoothProfileManager, LocalBluetoothProfile localBluetoothProfile, String str, int i) {
            super(localBluetoothProfile);
            this.mAudioChangeAction = str;
            this.mAudioDisconnectedState = i;
        }
    }

    /* loaded from: classes.dex */
    public class PanStateChangedHandler extends StateChangedHandler {
        @Override // com.android.settingslib.bluetooth.LocalBluetoothProfileManager.StateChangedHandler, com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public final void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            PanProfile panProfile = (PanProfile) this.mProfile;
            int intExtra = intent.getIntExtra("android.bluetooth.pan.extra.LOCAL_ROLE", 0);
            Objects.requireNonNull(panProfile);
            panProfile.mDeviceRoleMap.put(bluetoothDevice, Integer.valueOf(intExtra));
            super.onReceive(context, intent, bluetoothDevice);
        }

        public PanStateChangedHandler(LocalBluetoothProfileManager localBluetoothProfileManager, PanProfile panProfile) {
            super(panProfile);
        }
    }

    /* loaded from: classes.dex */
    public interface ServiceListener {
        void onServiceConnected();

        void onServiceDisconnected();
    }

    /* loaded from: classes.dex */
    public class StateChangedHandler implements BluetoothEventManager.Handler {
        public final LocalBluetoothProfile mProfile;

        public StateChangedHandler(LocalBluetoothProfile localBluetoothProfile) {
            this.mProfile = localBluetoothProfile;
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            CachedBluetoothDevice findDevice = LocalBluetoothProfileManager.this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice == null) {
                Log.w("LocalBluetoothProfileManager", "StateChangedHandler found new device: " + bluetoothDevice);
                findDevice = LocalBluetoothProfileManager.this.mDeviceManager.addDevice(bluetoothDevice);
            }
            onReceiveInternal(intent, findDevice);
        }

        public void onReceiveInternal(Intent intent, CachedBluetoothDevice cachedBluetoothDevice) {
            Map map;
            long j;
            boolean z = false;
            int intExtra = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
            int intExtra2 = intent.getIntExtra("android.bluetooth.profile.extra.PREVIOUS_STATE", 0);
            boolean z2 = true;
            if (intExtra == 0 && intExtra2 == 1) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Failed to connect ");
                m.append(this.mProfile);
                m.append(" device");
                Log.i("LocalBluetoothProfileManager", m.toString());
            }
            LocalBluetoothProfileManager localBluetoothProfileManager = LocalBluetoothProfileManager.this;
            Objects.requireNonNull(localBluetoothProfileManager);
            if (localBluetoothProfileManager.mHearingAidProfile != null && (this.mProfile instanceof HearingAidProfile) && intExtra == 2 && cachedBluetoothDevice.mHiSyncId == 0) {
                LocalBluetoothProfileManager localBluetoothProfileManager2 = LocalBluetoothProfileManager.this;
                Objects.requireNonNull(localBluetoothProfileManager2);
                HearingAidProfile hearingAidProfile = localBluetoothProfileManager2.mHearingAidProfile;
                BluetoothDevice bluetoothDevice = cachedBluetoothDevice.mDevice;
                Objects.requireNonNull(hearingAidProfile);
                BluetoothHearingAid bluetoothHearingAid = hearingAidProfile.mService;
                if (bluetoothHearingAid == null || bluetoothDevice == null) {
                    j = 0;
                } else {
                    j = bluetoothHearingAid.getHiSyncId(bluetoothDevice);
                }
                if (j != 0) {
                    cachedBluetoothDevice.mHiSyncId = j;
                }
            }
            LocalBluetoothProfileManager localBluetoothProfileManager3 = LocalBluetoothProfileManager.this;
            Objects.requireNonNull(localBluetoothProfileManager3);
            if (localBluetoothProfileManager3.mCsipSetCoordinatorProfile != null && (this.mProfile instanceof CsipSetCoordinatorProfile) && intExtra == 2 && cachedBluetoothDevice.mGroupId == -1) {
                LocalBluetoothProfileManager localBluetoothProfileManager4 = LocalBluetoothProfileManager.this;
                Objects.requireNonNull(localBluetoothProfileManager4);
                CsipSetCoordinatorProfile csipSetCoordinatorProfile = localBluetoothProfileManager4.mCsipSetCoordinatorProfile;
                BluetoothDevice bluetoothDevice2 = cachedBluetoothDevice.mDevice;
                Objects.requireNonNull(csipSetCoordinatorProfile);
                BluetoothCsipSetCoordinator bluetoothCsipSetCoordinator = csipSetCoordinatorProfile.mService;
                if (bluetoothCsipSetCoordinator == null || bluetoothDevice2 == null) {
                    map = null;
                } else {
                    map = bluetoothCsipSetCoordinator.getGroupUuidMapByDevice(bluetoothDevice2);
                }
                if (map != null) {
                    Iterator it = map.entrySet().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        Map.Entry entry = (Map.Entry) it.next();
                        if (((ParcelUuid) entry.getValue()).equals(BluetoothUuid.CAP)) {
                            cachedBluetoothDevice.mGroupId = ((Integer) entry.getKey()).intValue();
                            break;
                        }
                    }
                }
            }
            cachedBluetoothDevice.onProfileStateChanged(this.mProfile, intExtra);
            if (!(cachedBluetoothDevice.mHiSyncId == 0 && cachedBluetoothDevice.mGroupId == -1)) {
                CachedBluetoothDeviceManager cachedBluetoothDeviceManager = LocalBluetoothProfileManager.this.mDeviceManager;
                int profileId = this.mProfile.getProfileId();
                Objects.requireNonNull(cachedBluetoothDeviceManager);
                synchronized (cachedBluetoothDeviceManager) {
                    if (profileId == 21) {
                        z = cachedBluetoothDeviceManager.mHearingAidDeviceManager.onProfileConnectionStateChangedIfProcessed(cachedBluetoothDevice, intExtra);
                    } else if (profileId == 25) {
                        z = cachedBluetoothDeviceManager.mCsipDeviceManager.onProfileConnectionStateChangedIfProcessed(cachedBluetoothDevice, intExtra);
                    }
                }
                z2 = true ^ z;
            }
            if (z2) {
                cachedBluetoothDevice.refresh();
                BluetoothEventManager bluetoothEventManager = LocalBluetoothProfileManager.this.mEventManager;
                int profileId2 = this.mProfile.getProfileId();
                Objects.requireNonNull(bluetoothEventManager);
                Iterator it2 = bluetoothEventManager.mCallbacks.iterator();
                while (it2.hasNext()) {
                    ((BluetoothCallback) it2.next()).onProfileConnectionStateChanged(cachedBluetoothDevice, intExtra, profileId2);
                }
            }
        }
    }

    public final synchronized void updateProfiles(ParcelUuid[] parcelUuidArr, ParcelUuid[] parcelUuidArr2, Collection<LocalBluetoothProfile> collection, Collection<LocalBluetoothProfile> collection2, boolean z, BluetoothDevice bluetoothDevice) {
        LeAudioProfile leAudioProfile;
        HearingAidProfile hearingAidProfile;
        HidProfile hidProfile;
        OppProfile oppProfile;
        A2dpSinkProfile a2dpSinkProfile;
        A2dpProfile a2dpProfile;
        collection2.clear();
        collection2.addAll(collection);
        Log.d("LocalBluetoothProfileManager", "Current Profiles" + collection.toString());
        collection.clear();
        if (this.mHeadsetProfile != null && ((ArrayUtils.contains(parcelUuidArr2, BluetoothUuid.HSP_AG) && ArrayUtils.contains(parcelUuidArr, BluetoothUuid.HSP)) || (ArrayUtils.contains(parcelUuidArr2, BluetoothUuid.HFP_AG) && ArrayUtils.contains(parcelUuidArr, BluetoothUuid.HFP)))) {
            collection.add(this.mHeadsetProfile);
            collection2.remove(this.mHeadsetProfile);
        }
        if (this.mHfpClientProfile != null && ArrayUtils.contains(parcelUuidArr, BluetoothUuid.HFP_AG) && ArrayUtils.contains(parcelUuidArr2, BluetoothUuid.HFP)) {
            collection.add(this.mHfpClientProfile);
            collection2.remove(this.mHfpClientProfile);
        }
        if (BluetoothUuid.containsAnyUuid(parcelUuidArr, A2dpProfile.SINK_UUIDS) && (a2dpProfile = this.mA2dpProfile) != null) {
            collection.add(a2dpProfile);
            collection2.remove(this.mA2dpProfile);
        }
        if (BluetoothUuid.containsAnyUuid(parcelUuidArr, A2dpSinkProfile.SRC_UUIDS) && (a2dpSinkProfile = this.mA2dpSinkProfile) != null) {
            collection.add(a2dpSinkProfile);
            collection2.remove(this.mA2dpSinkProfile);
        }
        if (ArrayUtils.contains(parcelUuidArr, BluetoothUuid.OBEX_OBJECT_PUSH) && (oppProfile = this.mOppProfile) != null) {
            collection.add(oppProfile);
            collection2.remove(this.mOppProfile);
        }
        if ((ArrayUtils.contains(parcelUuidArr, BluetoothUuid.HID) || ArrayUtils.contains(parcelUuidArr, BluetoothUuid.HOGP)) && (hidProfile = this.mHidProfile) != null) {
            collection.add(hidProfile);
            collection2.remove(this.mHidProfile);
        }
        HidDeviceProfile hidDeviceProfile = this.mHidDeviceProfile;
        if (!(hidDeviceProfile == null || hidDeviceProfile.getConnectionStatus(bluetoothDevice) == 0)) {
            collection.add(this.mHidDeviceProfile);
            collection2.remove(this.mHidDeviceProfile);
        }
        if (z) {
            Log.d("LocalBluetoothProfileManager", "Valid PAN-NAP connection exists.");
        }
        if ((ArrayUtils.contains(parcelUuidArr, BluetoothUuid.NAP) && this.mPanProfile != null) || z) {
            collection.add(this.mPanProfile);
            collection2.remove(this.mPanProfile);
        }
        MapProfile mapProfile = this.mMapProfile;
        if (mapProfile != null && mapProfile.getConnectionStatus(bluetoothDevice) == 2) {
            collection.add(this.mMapProfile);
            collection2.remove(this.mMapProfile);
            this.mMapProfile.setEnabled(bluetoothDevice, true);
        }
        PbapServerProfile pbapServerProfile = this.mPbapProfile;
        if (pbapServerProfile != null && pbapServerProfile.getConnectionStatus(bluetoothDevice) == 2) {
            collection.add(this.mPbapProfile);
            collection2.remove(this.mPbapProfile);
            this.mPbapProfile.setEnabled(bluetoothDevice, true);
        }
        if (this.mMapClientProfile != null && BluetoothUuid.containsAnyUuid(parcelUuidArr, MapClientProfile.UUIDS)) {
            collection.add(this.mMapClientProfile);
            collection2.remove(this.mMapClientProfile);
        }
        if (this.mPbapClientProfile != null && BluetoothUuid.containsAnyUuid(parcelUuidArr, PbapClientProfile.SRC_UUIDS)) {
            collection.add(this.mPbapClientProfile);
            collection2.remove(this.mPbapClientProfile);
        }
        if (ArrayUtils.contains(parcelUuidArr, BluetoothUuid.HEARING_AID) && (hearingAidProfile = this.mHearingAidProfile) != null) {
            collection.add(hearingAidProfile);
            collection2.remove(this.mHearingAidProfile);
        }
        if (ArrayUtils.contains(parcelUuidArr, BluetoothUuid.LE_AUDIO) && (leAudioProfile = this.mLeAudioProfile) != null) {
            collection.add(leAudioProfile);
            collection2.remove(this.mLeAudioProfile);
        }
        if (this.mSapProfile != null && ArrayUtils.contains(parcelUuidArr, BluetoothUuid.SAP)) {
            collection.add(this.mSapProfile);
            collection2.remove(this.mSapProfile);
        }
        if (this.mVolumeControlProfile != null && ArrayUtils.contains(parcelUuidArr, BluetoothUuid.VOLUME_CONTROL)) {
            collection.add(this.mVolumeControlProfile);
            collection2.remove(this.mVolumeControlProfile);
        }
        if (this.mCsipSetCoordinatorProfile != null && ArrayUtils.contains(parcelUuidArr, BluetoothUuid.COORDINATED_SET)) {
            collection.add(this.mCsipSetCoordinatorProfile);
            collection2.remove(this.mCsipSetCoordinatorProfile);
        }
        Log.d("LocalBluetoothProfileManager", "New Profiles" + collection.toString());
    }

    public final void addProfile(LocalBluetoothProfile localBluetoothProfile, String str, String str2) {
        this.mEventManager.addProfileHandler(str2, new StateChangedHandler(localBluetoothProfile));
        this.mProfileNameMap.put(str, localBluetoothProfile);
    }

    public final void callServiceConnectedListeners() {
        Iterator it = new ArrayList(this.mServiceListeners).iterator();
        while (it.hasNext()) {
            ((ServiceListener) it.next()).onServiceConnected();
        }
    }

    public final void callServiceDisconnectedListeners() {
        Iterator it = new ArrayList(this.mServiceListeners).iterator();
        while (it.hasNext()) {
            ((ServiceListener) it.next()).onServiceDisconnected();
        }
    }

    public LocalBluetoothProfileManager(Context context, LocalBluetoothAdapter localBluetoothAdapter, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, BluetoothEventManager bluetoothEventManager) {
        this.mContext = context;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mEventManager = bluetoothEventManager;
        localBluetoothAdapter.mProfileManager = this;
        Log.d("LocalBluetoothProfileManager", "LocalBluetoothProfileManager construction complete");
    }

    public final void updateLocalProfiles() {
        List supportedProfiles = BluetoothAdapter.getDefaultAdapter().getSupportedProfiles();
        if (CollectionUtils.isEmpty(supportedProfiles)) {
            Log.d("LocalBluetoothProfileManager", "supportedList is null");
            return;
        }
        if (this.mA2dpProfile == null && supportedProfiles.contains(2)) {
            Log.d("LocalBluetoothProfileManager", "Adding local A2DP profile");
            A2dpProfile a2dpProfile = new A2dpProfile(this.mContext, this.mDeviceManager, this);
            this.mA2dpProfile = a2dpProfile;
            addProfile(a2dpProfile, "A2DP", "android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mA2dpSinkProfile == null && supportedProfiles.contains(11)) {
            Log.d("LocalBluetoothProfileManager", "Adding local A2DP SINK profile");
            A2dpSinkProfile a2dpSinkProfile = new A2dpSinkProfile(this.mContext, this.mDeviceManager);
            this.mA2dpSinkProfile = a2dpSinkProfile;
            addProfile(a2dpSinkProfile, "A2DPSink", "android.bluetooth.a2dp-sink.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mHeadsetProfile == null && supportedProfiles.contains(1)) {
            Log.d("LocalBluetoothProfileManager", "Adding local HEADSET profile");
            HeadsetProfile headsetProfile = new HeadsetProfile(this.mContext, this.mDeviceManager, this);
            this.mHeadsetProfile = headsetProfile;
            HeadsetStateChangeHandler headsetStateChangeHandler = new HeadsetStateChangeHandler(this, headsetProfile, "android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED", 10);
            this.mEventManager.addProfileHandler("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED", headsetStateChangeHandler);
            this.mEventManager.addProfileHandler("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED", headsetStateChangeHandler);
            this.mProfileNameMap.put("HEADSET", headsetProfile);
        }
        if (this.mHfpClientProfile == null && supportedProfiles.contains(16)) {
            Log.d("LocalBluetoothProfileManager", "Adding local HfpClient profile");
            HfpClientProfile hfpClientProfile = new HfpClientProfile(this.mContext, this.mDeviceManager);
            this.mHfpClientProfile = hfpClientProfile;
            HeadsetStateChangeHandler headsetStateChangeHandler2 = new HeadsetStateChangeHandler(this, hfpClientProfile, "android.bluetooth.headsetclient.profile.action.AUDIO_STATE_CHANGED", 0);
            this.mEventManager.addProfileHandler("android.bluetooth.headsetclient.profile.action.CONNECTION_STATE_CHANGED", headsetStateChangeHandler2);
            this.mEventManager.addProfileHandler("android.bluetooth.headsetclient.profile.action.AUDIO_STATE_CHANGED", headsetStateChangeHandler2);
            this.mProfileNameMap.put("HEADSET_CLIENT", hfpClientProfile);
        }
        if (this.mMapClientProfile == null && supportedProfiles.contains(18)) {
            Log.d("LocalBluetoothProfileManager", "Adding local MAP CLIENT profile");
            MapClientProfile mapClientProfile = new MapClientProfile(this.mContext, this.mDeviceManager, this);
            this.mMapClientProfile = mapClientProfile;
            addProfile(mapClientProfile, "MAP Client", "android.bluetooth.mapmce.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mMapProfile == null && supportedProfiles.contains(9)) {
            Log.d("LocalBluetoothProfileManager", "Adding local MAP profile");
            MapProfile mapProfile = new MapProfile(this.mContext, this.mDeviceManager, this);
            this.mMapProfile = mapProfile;
            addProfile(mapProfile, "MAP", "android.bluetooth.map.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mOppProfile == null && supportedProfiles.contains(20)) {
            Log.d("LocalBluetoothProfileManager", "Adding local OPP profile");
            OppProfile oppProfile = new OppProfile();
            this.mOppProfile = oppProfile;
            this.mProfileNameMap.put("OPP", oppProfile);
        }
        if (this.mHearingAidProfile == null && supportedProfiles.contains(21)) {
            Log.d("LocalBluetoothProfileManager", "Adding local Hearing Aid profile");
            HearingAidProfile hearingAidProfile = new HearingAidProfile(this.mContext, this.mDeviceManager, this);
            this.mHearingAidProfile = hearingAidProfile;
            addProfile(hearingAidProfile, "HearingAid", "android.bluetooth.hearingaid.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mHidProfile == null && supportedProfiles.contains(4)) {
            Log.d("LocalBluetoothProfileManager", "Adding local HID_HOST profile");
            HidProfile hidProfile = new HidProfile(this.mContext, this.mDeviceManager);
            this.mHidProfile = hidProfile;
            addProfile(hidProfile, "HID", "android.bluetooth.input.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mHidDeviceProfile == null && supportedProfiles.contains(19)) {
            Log.d("LocalBluetoothProfileManager", "Adding local HID_DEVICE profile");
            HidDeviceProfile hidDeviceProfile = new HidDeviceProfile(this.mContext, this.mDeviceManager);
            this.mHidDeviceProfile = hidDeviceProfile;
            addProfile(hidDeviceProfile, "HID DEVICE", "android.bluetooth.hiddevice.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mPanProfile == null && supportedProfiles.contains(5)) {
            Log.d("LocalBluetoothProfileManager", "Adding local PAN profile");
            PanProfile panProfile = new PanProfile(this.mContext);
            this.mPanProfile = panProfile;
            this.mEventManager.addProfileHandler("android.bluetooth.pan.profile.action.CONNECTION_STATE_CHANGED", new PanStateChangedHandler(this, panProfile));
            this.mProfileNameMap.put("PAN", panProfile);
        }
        if (this.mPbapProfile == null && supportedProfiles.contains(6)) {
            Log.d("LocalBluetoothProfileManager", "Adding local PBAP profile");
            PbapServerProfile pbapServerProfile = new PbapServerProfile(this.mContext);
            this.mPbapProfile = pbapServerProfile;
            addProfile(pbapServerProfile, PbapServerProfile.NAME, "android.bluetooth.pbap.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mPbapClientProfile == null && supportedProfiles.contains(17)) {
            Log.d("LocalBluetoothProfileManager", "Adding local PBAP Client profile");
            PbapClientProfile pbapClientProfile = new PbapClientProfile(this.mContext, this.mDeviceManager);
            this.mPbapClientProfile = pbapClientProfile;
            addProfile(pbapClientProfile, "PbapClient", "android.bluetooth.pbapclient.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mSapProfile == null && supportedProfiles.contains(10)) {
            Log.d("LocalBluetoothProfileManager", "Adding local SAP profile");
            SapProfile sapProfile = new SapProfile(this.mContext, this.mDeviceManager, this);
            this.mSapProfile = sapProfile;
            addProfile(sapProfile, "SAP", "android.bluetooth.sap.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mVolumeControlProfile == null && supportedProfiles.contains(23)) {
            Log.d("LocalBluetoothProfileManager", "Adding local Volume Control profile");
            VolumeControlProfile volumeControlProfile = new VolumeControlProfile();
            this.mVolumeControlProfile = volumeControlProfile;
            this.mProfileNameMap.put("VCP", volumeControlProfile);
        }
        if (this.mLeAudioProfile == null && supportedProfiles.contains(22)) {
            Log.d("LocalBluetoothProfileManager", "Adding local LE_AUDIO profile");
            LeAudioProfile leAudioProfile = new LeAudioProfile(this.mContext, this.mDeviceManager, this);
            this.mLeAudioProfile = leAudioProfile;
            addProfile(leAudioProfile, "LE_AUDIO", "android.bluetooth.action.LE_AUDIO_CONNECTION_STATE_CHANGED");
        }
        if (this.mCsipSetCoordinatorProfile == null && supportedProfiles.contains(25)) {
            Log.d("LocalBluetoothProfileManager", "Adding local CSIP set coordinator profile");
            CsipSetCoordinatorProfile csipSetCoordinatorProfile = new CsipSetCoordinatorProfile(this.mContext, this.mDeviceManager, this);
            this.mCsipSetCoordinatorProfile = csipSetCoordinatorProfile;
            addProfile(csipSetCoordinatorProfile, "CSIP Set Coordinator", "android.bluetooth.action.CSIS_CONNECTION_STATE_CHANGED");
        }
        this.mEventManager.registerProfileIntentReceiver();
    }

    public HidDeviceProfile getHidDeviceProfile() {
        return this.mHidDeviceProfile;
    }

    public HidProfile getHidProfile() {
        return this.mHidProfile;
    }
}
