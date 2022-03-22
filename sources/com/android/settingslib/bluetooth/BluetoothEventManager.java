package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothUuid;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Log;
import android.util.Pair;
import androidx.constraintlayout.motion.widget.MotionLayout$$ExternalSyntheticOutline0;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline1;
import com.android.internal.util.ArrayUtils;
import com.android.settingslib.bluetooth.BluetoothUtils;
import com.android.settingslib.wifi.WifiTracker;
import com.android.systemui.keyboard.KeyboardUI;
import com.android.systemui.plugins.FalsingManager;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public final class BluetoothEventManager {
    public static final boolean DEBUG = Log.isLoggable("BluetoothEventManager", 3);
    public final Context mContext;
    public final CachedBluetoothDeviceManager mDeviceManager;
    public final LocalBluetoothAdapter mLocalAdapter;
    public final android.os.Handler mReceiverHandler;
    public final UserHandle mUserHandle;
    public final BluetoothBroadcastReceiver mBroadcastReceiver = new BluetoothBroadcastReceiver();
    public final BluetoothBroadcastReceiver mProfileBroadcastReceiver = new BluetoothBroadcastReceiver();
    public final CopyOnWriteArrayList mCallbacks = new CopyOnWriteArrayList();
    public final IntentFilter mAdapterIntentFilter = new IntentFilter();
    public final IntentFilter mProfileIntentFilter = new IntentFilter();
    public final HashMap mHandlerMap = new HashMap();

    /* loaded from: classes.dex */
    public class AclStateChangedHandler implements Handler {
        public AclStateChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public final void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            int i;
            boolean z;
            if (bluetoothDevice == null) {
                Log.w("BluetoothEventManager", "AclStateChangedHandler: device is null");
                return;
            }
            CachedBluetoothDeviceManager cachedBluetoothDeviceManager = BluetoothEventManager.this.mDeviceManager;
            Objects.requireNonNull(cachedBluetoothDeviceManager);
            synchronized (cachedBluetoothDeviceManager) {
                Iterator<CachedBluetoothDevice> it = cachedBluetoothDeviceManager.mCachedDevices.iterator();
                loop0: while (true) {
                    i = 0;
                    if (!it.hasNext()) {
                        z = false;
                        break;
                    }
                    CachedBluetoothDevice next = it.next();
                    Objects.requireNonNull(next);
                    if (!next.mDevice.equals(bluetoothDevice)) {
                        HashSet<CachedBluetoothDevice> hashSet = next.mMemberDevices;
                        z = true;
                        if (hashSet.isEmpty()) {
                            CachedBluetoothDevice cachedBluetoothDevice = next.mSubDevice;
                            if (cachedBluetoothDevice != null && cachedBluetoothDevice.mDevice.equals(bluetoothDevice)) {
                                break;
                            }
                        } else {
                            for (CachedBluetoothDevice cachedBluetoothDevice2 : hashSet) {
                                Objects.requireNonNull(cachedBluetoothDevice2);
                                if (cachedBluetoothDevice2.mDevice.equals(bluetoothDevice)) {
                                    break loop0;
                                }
                            }
                            continue;
                        }
                    }
                }
            }
            if (!z) {
                String action = intent.getAction();
                if (action == null) {
                    Log.w("BluetoothEventManager", "AclStateChangedHandler: action is null");
                    return;
                }
                CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
                if (findDevice == null) {
                    Log.w("BluetoothEventManager", "AclStateChangedHandler: activeDevice is null");
                    return;
                }
                if (action.equals("android.bluetooth.device.action.ACL_CONNECTED")) {
                    i = 2;
                } else if (!action.equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    MotionLayout$$ExternalSyntheticOutline0.m("ActiveDeviceChangedHandler: unknown action ", action, "BluetoothEventManager");
                    return;
                }
                BluetoothEventManager bluetoothEventManager = BluetoothEventManager.this;
                Objects.requireNonNull(bluetoothEventManager);
                Iterator it2 = bluetoothEventManager.mCallbacks.iterator();
                while (it2.hasNext()) {
                    ((BluetoothCallback) it2.next()).onAclConnectionStateChanged(findDevice, i);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public class ActiveDeviceChangedHandler implements Handler {
        public ActiveDeviceChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public final void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            int i;
            String action = intent.getAction();
            if (action == null) {
                Log.w("BluetoothEventManager", "ActiveDeviceChangedHandler: action is null");
                return;
            }
            CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
            if (action.equals("android.bluetooth.a2dp.profile.action.ACTIVE_DEVICE_CHANGED")) {
                i = 2;
            } else if (action.equals("android.bluetooth.headset.profile.action.ACTIVE_DEVICE_CHANGED")) {
                i = 1;
            } else if (action.equals("android.bluetooth.hearingaid.profile.action.ACTIVE_DEVICE_CHANGED")) {
                i = 21;
            } else if (action.equals("android.bluetooth.action.LE_AUDIO_ACTIVE_DEVICE_CHANGED")) {
                i = 22;
            } else {
                MotionLayout$$ExternalSyntheticOutline0.m("ActiveDeviceChangedHandler: unknown action ", action, "BluetoothEventManager");
                return;
            }
            BluetoothEventManager.this.dispatchActiveDeviceChanged(findDevice, i);
        }
    }

    /* loaded from: classes.dex */
    public class AdapterStateChangedHandler implements Handler {
        public AdapterStateChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public final void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
            BluetoothEventManager.this.mLocalAdapter.setBluetoothStateInt(intExtra);
            Iterator it = BluetoothEventManager.this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((BluetoothCallback) it.next()).onBluetoothStateChanged(intExtra);
            }
            CachedBluetoothDeviceManager cachedBluetoothDeviceManager = BluetoothEventManager.this.mDeviceManager;
            Objects.requireNonNull(cachedBluetoothDeviceManager);
            synchronized (cachedBluetoothDeviceManager) {
                if (intExtra == 13) {
                    int size = cachedBluetoothDeviceManager.mCachedDevices.size();
                    while (true) {
                        size--;
                        if (size < 0) {
                            break;
                        }
                        CachedBluetoothDevice cachedBluetoothDevice = cachedBluetoothDeviceManager.mCachedDevices.get(size);
                        Objects.requireNonNull(cachedBluetoothDevice);
                        HashSet<CachedBluetoothDevice> hashSet = cachedBluetoothDevice.mMemberDevices;
                        if (!hashSet.isEmpty()) {
                            for (CachedBluetoothDevice cachedBluetoothDevice2 : hashSet) {
                                if (cachedBluetoothDevice2.getBondState() != 12) {
                                    cachedBluetoothDevice.mMemberDevices.remove(cachedBluetoothDevice2);
                                }
                            }
                        } else {
                            CachedBluetoothDevice cachedBluetoothDevice3 = cachedBluetoothDevice.mSubDevice;
                            if (!(cachedBluetoothDevice3 == null || cachedBluetoothDevice3.getBondState() == 12)) {
                                cachedBluetoothDevice.mSubDevice = null;
                            }
                        }
                        if (cachedBluetoothDevice.getBondState() != 12) {
                            if (cachedBluetoothDevice.mJustDiscovered) {
                                cachedBluetoothDevice.mJustDiscovered = false;
                                cachedBluetoothDevice.dispatchAttributesChanged();
                            }
                            cachedBluetoothDeviceManager.mCachedDevices.remove(size);
                        }
                    }
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public class AudioModeChangedHandler implements Handler {
        public AudioModeChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public final void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            if (intent.getAction() == null) {
                Log.w("BluetoothEventManager", "AudioModeChangedHandler() action is null");
                return;
            }
            BluetoothEventManager bluetoothEventManager = BluetoothEventManager.this;
            Objects.requireNonNull(bluetoothEventManager);
            Iterator it = bluetoothEventManager.mDeviceManager.getCachedDevicesCopy().iterator();
            while (it.hasNext()) {
                CachedBluetoothDevice cachedBluetoothDevice = (CachedBluetoothDevice) it.next();
                Objects.requireNonNull(cachedBluetoothDevice);
                cachedBluetoothDevice.dispatchAttributesChanged();
            }
            Iterator it2 = bluetoothEventManager.mCallbacks.iterator();
            while (it2.hasNext()) {
                Objects.requireNonNull((BluetoothCallback) it2.next());
            }
        }
    }

    /* loaded from: classes.dex */
    public class BatteryLevelChangedHandler implements Handler {
        public BatteryLevelChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public final void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice != null) {
                findDevice.refresh();
            }
        }
    }

    /* loaded from: classes.dex */
    public class BluetoothBroadcastReceiver extends BroadcastReceiver {
        public BluetoothBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            Handler handler = (Handler) BluetoothEventManager.this.mHandlerMap.get(action);
            if (handler != null) {
                handler.onReceive(context, intent, bluetoothDevice);
            }
        }
    }

    /* loaded from: classes.dex */
    public class BondStateChangedHandler implements Handler {
        public BondStateChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public final void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            int i;
            if (bluetoothDevice == null) {
                Log.e("BluetoothEventManager", "ACTION_BOND_STATE_CHANGED with no EXTRA_DEVICE");
                return;
            }
            int intExtra = intent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", Integer.MIN_VALUE);
            CachedBluetoothDeviceManager cachedBluetoothDeviceManager = BluetoothEventManager.this.mDeviceManager;
            Objects.requireNonNull(cachedBluetoothDeviceManager);
            synchronized (cachedBluetoothDeviceManager) {
            }
            CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice == null) {
                Log.w("BluetoothEventManager", "Got bonding state changed for " + bluetoothDevice + ", but we have no record of that device.");
                findDevice = BluetoothEventManager.this.mDeviceManager.addDevice(bluetoothDevice);
            }
            Iterator it = BluetoothEventManager.this.mCallbacks.iterator();
            while (it.hasNext()) {
                ((BluetoothCallback) it.next()).onDeviceBondStateChanged(findDevice, intExtra);
            }
            if (intExtra == 10) {
                synchronized (findDevice.mProfileLock) {
                    findDevice.mProfiles.clear();
                }
                findDevice.mDevice.setPhonebookAccessPermission(0);
                findDevice.mDevice.setMessageAccessPermission(0);
                findDevice.mDevice.setSimAccessPermission(0);
            }
            findDevice.refresh();
            if (intExtra == 12 && findDevice.mDevice.isBondingInitiatedLocally()) {
                findDevice.connect$1();
            }
            if (intExtra == 10) {
                if (!(findDevice.mGroupId == -1 && findDevice.mHiSyncId == 0)) {
                    CachedBluetoothDeviceManager cachedBluetoothDeviceManager2 = BluetoothEventManager.this.mDeviceManager;
                    Objects.requireNonNull(cachedBluetoothDeviceManager2);
                    synchronized (cachedBluetoothDeviceManager2) {
                        CachedBluetoothDevice findMainDevice = cachedBluetoothDeviceManager2.mCsipDeviceManager.findMainDevice(findDevice);
                        HashSet<CachedBluetoothDevice> hashSet = findDevice.mMemberDevices;
                        if (!hashSet.isEmpty()) {
                            for (CachedBluetoothDevice cachedBluetoothDevice : hashSet) {
                                cachedBluetoothDevice.unpair();
                                findDevice.mMemberDevices.remove(cachedBluetoothDevice);
                            }
                        } else if (findMainDevice != null) {
                            findMainDevice.unpair();
                        }
                        CachedBluetoothDevice findMainDevice2 = cachedBluetoothDeviceManager2.mHearingAidDeviceManager.findMainDevice(findDevice);
                        CachedBluetoothDevice cachedBluetoothDevice2 = findDevice.mSubDevice;
                        if (cachedBluetoothDevice2 != null) {
                            cachedBluetoothDevice2.unpair();
                            findDevice.mSubDevice = null;
                        } else if (findMainDevice2 != null) {
                            findMainDevice2.unpair();
                            findMainDevice2.mSubDevice = null;
                        }
                    }
                }
                int intExtra2 = intent.getIntExtra("android.bluetooth.device.extra.REASON", Integer.MIN_VALUE);
                String name = findDevice.getName();
                if (BluetoothEventManager.DEBUG) {
                    Log.d("BluetoothEventManager", "showUnbondMessage() name : " + name + ", reason : " + intExtra2);
                }
                switch (intExtra2) {
                    case 1:
                        i = 2131952005;
                        break;
                    case 2:
                        i = 2131952006;
                        break;
                    case 3:
                    default:
                        GridLayoutManager$$ExternalSyntheticOutline1.m("showUnbondMessage: Not displaying any message for reason: ", intExtra2, "BluetoothEventManager");
                        return;
                    case 4:
                        i = 2131952003;
                        break;
                    case 5:
                    case FalsingManager.VERSION /* 6 */:
                    case 7:
                    case 8:
                        i = 2131952004;
                        break;
                }
                BluetoothUtils.ErrorListener errorListener = BluetoothUtils.sErrorListener;
                if (errorListener != null) {
                    KeyboardUI.this.mHandler.obtainMessage(11, i, 0, new Pair(context, name)).sendToTarget();
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public class ClassChangedHandler implements Handler {
        public ClassChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public final void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice != null) {
                findDevice.refresh();
            }
        }
    }

    /* loaded from: classes.dex */
    public class ConnectionStateChangedHandler implements Handler {
        public ConnectionStateChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public final void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
            int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.CONNECTION_STATE", Integer.MIN_VALUE);
            BluetoothEventManager bluetoothEventManager = BluetoothEventManager.this;
            Objects.requireNonNull(bluetoothEventManager);
            Iterator it = bluetoothEventManager.mCallbacks.iterator();
            while (it.hasNext()) {
                ((BluetoothCallback) it.next()).onConnectionStateChanged(findDevice, intExtra);
            }
        }
    }

    /* loaded from: classes.dex */
    public class DeviceFoundHandler implements Handler {
        public DeviceFoundHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public final void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            short shortExtra = intent.getShortExtra("android.bluetooth.device.extra.RSSI", Short.MIN_VALUE);
            intent.getStringExtra("android.bluetooth.device.extra.NAME");
            intent.getBooleanExtra("android.bluetooth.extra.IS_COORDINATED_SET_MEMBER", false);
            CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice == null) {
                findDevice = BluetoothEventManager.this.mDeviceManager.addDevice(bluetoothDevice);
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("DeviceFoundHandler created new CachedBluetoothDevice ");
                m.append(findDevice.mDevice.getAnonymizedAddress());
                Log.d("BluetoothEventManager", m.toString());
            } else if (findDevice.getBondState() == 12 && !findDevice.mDevice.isConnected()) {
                BluetoothEventManager.this.dispatchDeviceAdded(findDevice);
            }
            if (findDevice.mRssi != shortExtra) {
                findDevice.mRssi = shortExtra;
                findDevice.dispatchAttributesChanged();
            }
            if (!findDevice.mJustDiscovered) {
                findDevice.mJustDiscovered = true;
                findDevice.dispatchAttributesChanged();
            }
        }
    }

    /* loaded from: classes.dex */
    public interface Handler {
        void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice);
    }

    /* loaded from: classes.dex */
    public class NameChangedHandler implements Handler {
        public NameChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public final void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            CachedBluetoothDeviceManager cachedBluetoothDeviceManager = BluetoothEventManager.this.mDeviceManager;
            Objects.requireNonNull(cachedBluetoothDeviceManager);
            CachedBluetoothDevice findDevice = cachedBluetoothDeviceManager.findDevice(bluetoothDevice);
            if (findDevice != null) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Device name: ");
                m.append(findDevice.getName());
                Log.d("CachedBluetoothDevice", m.toString());
                findDevice.dispatchAttributesChanged();
            }
        }
    }

    /* loaded from: classes.dex */
    public class ScanningStateChangedHandler implements Handler {
        public final boolean mStarted;

        public ScanningStateChangedHandler(boolean z) {
            this.mStarted = z;
        }

        /* JADX WARN: Code restructure failed: missing block: B:18:0x004d, code lost:
            r2 = r0.iterator();
         */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x0055, code lost:
            if (r2.hasNext() == false) goto L_0x006a;
         */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x0057, code lost:
            r4 = (com.android.settingslib.bluetooth.CachedBluetoothDevice) r2.next();
            java.util.Objects.requireNonNull(r4);
         */
        /* JADX WARN: Code restructure failed: missing block: B:22:0x0062, code lost:
            if (r4.mJustDiscovered == false) goto L_0x0051;
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x0064, code lost:
            r4.mJustDiscovered = false;
            r4.dispatchAttributesChanged();
         */
        /* JADX WARN: Code restructure failed: missing block: B:51:?, code lost:
            return;
         */
        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onReceive(android.content.Context r3, android.content.Intent r4, android.bluetooth.BluetoothDevice r5) {
            /*
                r2 = this;
                com.android.settingslib.bluetooth.BluetoothEventManager r3 = com.android.settingslib.bluetooth.BluetoothEventManager.this
                java.util.concurrent.CopyOnWriteArrayList r3 = r3.mCallbacks
                java.util.Iterator r3 = r3.iterator()
            L_0x0008:
                boolean r4 = r3.hasNext()
                if (r4 == 0) goto L_0x0018
                java.lang.Object r4 = r3.next()
                com.android.settingslib.bluetooth.BluetoothCallback r4 = (com.android.settingslib.bluetooth.BluetoothCallback) r4
                java.util.Objects.requireNonNull(r4)
                goto L_0x0008
            L_0x0018:
                com.android.settingslib.bluetooth.BluetoothEventManager r3 = com.android.settingslib.bluetooth.BluetoothEventManager.this
                com.android.settingslib.bluetooth.CachedBluetoothDeviceManager r3 = r3.mDeviceManager
                boolean r2 = r2.mStarted
                java.util.Objects.requireNonNull(r3)
                monitor-enter(r3)
                if (r2 != 0) goto L_0x0026
                monitor-exit(r3)
                goto L_0x007b
            L_0x0026:
                java.util.List<com.android.settingslib.bluetooth.CachedBluetoothDevice> r2 = r3.mCachedDevices     // Catch: all -> 0x007c
                int r2 = r2.size()     // Catch: all -> 0x007c
            L_0x002c:
                int r2 = r2 + (-1)
                if (r2 < 0) goto L_0x007a
                java.util.List<com.android.settingslib.bluetooth.CachedBluetoothDevice> r4 = r3.mCachedDevices     // Catch: all -> 0x007c
                java.lang.Object r4 = r4.get(r2)     // Catch: all -> 0x007c
                com.android.settingslib.bluetooth.CachedBluetoothDevice r4 = (com.android.settingslib.bluetooth.CachedBluetoothDevice) r4     // Catch: all -> 0x007c
                r5 = 0
                java.util.Objects.requireNonNull(r4)     // Catch: all -> 0x007c
                boolean r0 = r4.mJustDiscovered     // Catch: all -> 0x007c
                if (r0 == 0) goto L_0x0045
                r4.mJustDiscovered = r5     // Catch: all -> 0x007c
                r4.dispatchAttributesChanged()     // Catch: all -> 0x007c
            L_0x0045:
                java.util.HashSet r0 = r4.mMemberDevices     // Catch: all -> 0x007c
                boolean r1 = r0.isEmpty()     // Catch: all -> 0x007c
                if (r1 != 0) goto L_0x006c
                java.util.Iterator r2 = r0.iterator()     // Catch: all -> 0x007c
            L_0x0051:
                boolean r4 = r2.hasNext()     // Catch: all -> 0x007c
                if (r4 == 0) goto L_0x006a
                java.lang.Object r4 = r2.next()     // Catch: all -> 0x007c
                com.android.settingslib.bluetooth.CachedBluetoothDevice r4 = (com.android.settingslib.bluetooth.CachedBluetoothDevice) r4     // Catch: all -> 0x007c
                java.util.Objects.requireNonNull(r4)     // Catch: all -> 0x007c
                boolean r0 = r4.mJustDiscovered     // Catch: all -> 0x007c
                if (r0 == 0) goto L_0x0051
                r4.mJustDiscovered = r5     // Catch: all -> 0x007c
                r4.dispatchAttributesChanged()     // Catch: all -> 0x007c
                goto L_0x0051
            L_0x006a:
                monitor-exit(r3)
                goto L_0x007b
            L_0x006c:
                com.android.settingslib.bluetooth.CachedBluetoothDevice r4 = r4.mSubDevice     // Catch: all -> 0x007c
                if (r4 == 0) goto L_0x002c
                boolean r0 = r4.mJustDiscovered     // Catch: all -> 0x007c
                if (r0 == 0) goto L_0x002c
                r4.mJustDiscovered = r5     // Catch: all -> 0x007c
                r4.dispatchAttributesChanged()     // Catch: all -> 0x007c
                goto L_0x002c
            L_0x007a:
                monitor-exit(r3)
            L_0x007b:
                return
            L_0x007c:
                r2 = move-exception
                monitor-exit(r3)
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.BluetoothEventManager.ScanningStateChangedHandler.onReceive(android.content.Context, android.content.Intent, android.bluetooth.BluetoothDevice):void");
        }
    }

    /* loaded from: classes.dex */
    public class UuidChangedHandler implements Handler {
        public UuidChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public final void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice != null) {
                findDevice.updateProfiles();
                ParcelUuid[] uuids = findDevice.mDevice.getUuids();
                long j = 30000;
                if (!ArrayUtils.contains(uuids, BluetoothUuid.HOGP)) {
                    if (ArrayUtils.contains(uuids, BluetoothUuid.HEARING_AID)) {
                        j = WifiTracker.MAX_SCAN_RESULT_AGE_MILLIS;
                    } else if (!ArrayUtils.contains(uuids, BluetoothUuid.LE_AUDIO)) {
                        j = 5000;
                    }
                }
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onUuidChanged: Time since last connect=");
                m.append(SystemClock.elapsedRealtime() - findDevice.mConnectAttempted);
                Log.d("CachedBluetoothDevice", m.toString());
                if (findDevice.mConnectAttempted + j > SystemClock.elapsedRealtime()) {
                    Log.d("CachedBluetoothDevice", "onUuidChanged: triggering connectDevice");
                    findDevice.connectDevice();
                }
                findDevice.dispatchAttributesChanged();
            }
        }
    }

    public void addHandler(String str, Handler handler) {
        this.mHandlerMap.put(str, handler);
        this.mAdapterIntentFilter.addAction(str);
    }

    public void addProfileHandler(String str, Handler handler) {
        this.mHandlerMap.put(str, handler);
        this.mProfileIntentFilter.addAction(str);
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x0071 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x000a A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void dispatchActiveDeviceChanged(com.android.settingslib.bluetooth.CachedBluetoothDevice r7, int r8) {
        /*
            r6 = this;
            com.android.settingslib.bluetooth.CachedBluetoothDeviceManager r0 = r6.mDeviceManager
            java.util.ArrayList r0 = r0.getCachedDevicesCopy()
            java.util.Iterator r0 = r0.iterator()
        L_0x000a:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0075
            java.lang.Object r1 = r0.next()
            com.android.settingslib.bluetooth.CachedBluetoothDevice r1 = (com.android.settingslib.bluetooth.CachedBluetoothDevice) r1
            boolean r2 = java.util.Objects.equals(r1, r7)
            r3 = 1
            r4 = 0
            if (r8 == r3) goto L_0x0066
            r5 = 2
            if (r8 == r5) goto L_0x005d
            r5 = 21
            if (r8 == r5) goto L_0x0054
            r5 = 22
            if (r8 == r5) goto L_0x004b
            java.util.Objects.requireNonNull(r1)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "onActiveDeviceChanged: unknown profile "
            r3.append(r5)
            r3.append(r8)
            java.lang.String r5 = " isActive "
            r3.append(r5)
            r3.append(r2)
            java.lang.String r2 = r3.toString()
            java.lang.String r3 = "CachedBluetoothDevice"
            android.util.Log.w(r3, r2)
            goto L_0x006f
        L_0x004b:
            boolean r5 = r1.mIsActiveDeviceLeAudio
            if (r5 == r2) goto L_0x0050
            goto L_0x0051
        L_0x0050:
            r3 = r4
        L_0x0051:
            r1.mIsActiveDeviceLeAudio = r2
            goto L_0x006e
        L_0x0054:
            boolean r5 = r1.mIsActiveDeviceHearingAid
            if (r5 == r2) goto L_0x0059
            goto L_0x005a
        L_0x0059:
            r3 = r4
        L_0x005a:
            r1.mIsActiveDeviceHearingAid = r2
            goto L_0x006e
        L_0x005d:
            boolean r5 = r1.mIsActiveDeviceA2dp
            if (r5 == r2) goto L_0x0062
            goto L_0x0063
        L_0x0062:
            r3 = r4
        L_0x0063:
            r1.mIsActiveDeviceA2dp = r2
            goto L_0x006e
        L_0x0066:
            boolean r5 = r1.mIsActiveDeviceHeadset
            if (r5 == r2) goto L_0x006b
            goto L_0x006c
        L_0x006b:
            r3 = r4
        L_0x006c:
            r1.mIsActiveDeviceHeadset = r2
        L_0x006e:
            r4 = r3
        L_0x006f:
            if (r4 == 0) goto L_0x000a
            r1.dispatchAttributesChanged()
            goto L_0x000a
        L_0x0075:
            java.util.concurrent.CopyOnWriteArrayList r6 = r6.mCallbacks
            java.util.Iterator r6 = r6.iterator()
        L_0x007b:
            boolean r0 = r6.hasNext()
            if (r0 == 0) goto L_0x008b
            java.lang.Object r0 = r6.next()
            com.android.settingslib.bluetooth.BluetoothCallback r0 = (com.android.settingslib.bluetooth.BluetoothCallback) r0
            r0.onActiveDeviceChanged(r7, r8)
            goto L_0x007b
        L_0x008b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.BluetoothEventManager.dispatchActiveDeviceChanged(com.android.settingslib.bluetooth.CachedBluetoothDevice, int):void");
    }

    public final void dispatchDeviceAdded(CachedBluetoothDevice cachedBluetoothDevice) {
        Iterator it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            ((BluetoothCallback) it.next()).onDeviceAdded(cachedBluetoothDevice);
        }
    }

    public final void dispatchDeviceRemoved(CachedBluetoothDevice cachedBluetoothDevice) {
        Iterator it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            ((BluetoothCallback) it.next()).onDeviceDeleted(cachedBluetoothDevice);
        }
    }

    public final boolean readPairedDevices() {
        LocalBluetoothAdapter localBluetoothAdapter = this.mLocalAdapter;
        Objects.requireNonNull(localBluetoothAdapter);
        Set<BluetoothDevice> bondedDevices = localBluetoothAdapter.mAdapter.getBondedDevices();
        boolean z = false;
        if (bondedDevices == null) {
            return false;
        }
        for (BluetoothDevice bluetoothDevice : bondedDevices) {
            if (this.mDeviceManager.findDevice(bluetoothDevice) == null) {
                this.mDeviceManager.addDevice(bluetoothDevice);
                z = true;
            }
        }
        return z;
    }

    public void registerAdapterIntentReceiver() {
        BluetoothBroadcastReceiver bluetoothBroadcastReceiver = this.mBroadcastReceiver;
        IntentFilter intentFilter = this.mAdapterIntentFilter;
        UserHandle userHandle = this.mUserHandle;
        if (userHandle == null) {
            this.mContext.registerReceiver(bluetoothBroadcastReceiver, intentFilter, null, this.mReceiverHandler, 2);
        } else {
            this.mContext.registerReceiverAsUser(bluetoothBroadcastReceiver, userHandle, intentFilter, null, this.mReceiverHandler, 2);
        }
    }

    public void registerProfileIntentReceiver() {
        BluetoothBroadcastReceiver bluetoothBroadcastReceiver = this.mProfileBroadcastReceiver;
        IntentFilter intentFilter = this.mProfileIntentFilter;
        UserHandle userHandle = this.mUserHandle;
        if (userHandle == null) {
            this.mContext.registerReceiver(bluetoothBroadcastReceiver, intentFilter, null, this.mReceiverHandler, 2);
        } else {
            this.mContext.registerReceiverAsUser(bluetoothBroadcastReceiver, userHandle, intentFilter, null, this.mReceiverHandler, 2);
        }
    }

    public BluetoothEventManager(LocalBluetoothAdapter localBluetoothAdapter, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, Context context, android.os.Handler handler, UserHandle userHandle) {
        this.mLocalAdapter = localBluetoothAdapter;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mContext = context;
        this.mUserHandle = userHandle;
        this.mReceiverHandler = handler;
        addHandler("android.bluetooth.adapter.action.STATE_CHANGED", new AdapterStateChangedHandler());
        addHandler("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED", new ConnectionStateChangedHandler());
        addHandler("android.bluetooth.adapter.action.DISCOVERY_STARTED", new ScanningStateChangedHandler(true));
        addHandler("android.bluetooth.adapter.action.DISCOVERY_FINISHED", new ScanningStateChangedHandler(false));
        addHandler("android.bluetooth.device.action.FOUND", new DeviceFoundHandler());
        addHandler("android.bluetooth.device.action.NAME_CHANGED", new NameChangedHandler());
        addHandler("android.bluetooth.device.action.ALIAS_CHANGED", new NameChangedHandler());
        addHandler("android.bluetooth.device.action.BOND_STATE_CHANGED", new BondStateChangedHandler());
        addHandler("android.bluetooth.device.action.CLASS_CHANGED", new ClassChangedHandler());
        addHandler("android.bluetooth.device.action.UUID", new UuidChangedHandler());
        addHandler("android.bluetooth.device.action.BATTERY_LEVEL_CHANGED", new BatteryLevelChangedHandler());
        addHandler("android.bluetooth.a2dp.profile.action.ACTIVE_DEVICE_CHANGED", new ActiveDeviceChangedHandler());
        addHandler("android.bluetooth.headset.profile.action.ACTIVE_DEVICE_CHANGED", new ActiveDeviceChangedHandler());
        addHandler("android.bluetooth.hearingaid.profile.action.ACTIVE_DEVICE_CHANGED", new ActiveDeviceChangedHandler());
        addHandler("android.bluetooth.action.LE_AUDIO_ACTIVE_DEVICE_CHANGED", new ActiveDeviceChangedHandler());
        addHandler("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED", new AudioModeChangedHandler());
        addHandler("android.intent.action.PHONE_STATE", new AudioModeChangedHandler());
        addHandler("android.bluetooth.device.action.ACL_CONNECTED", new AclStateChangedHandler());
        addHandler("android.bluetooth.device.action.ACL_DISCONNECTED", new AclStateChangedHandler());
        registerAdapterIntentReceiver();
    }
}
