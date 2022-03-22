package com.android.settingslib.media;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2Manager;
import android.media.RoutingSessionInfo;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.media.MediaManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/* loaded from: classes.dex */
public final class InfoMediaManager extends MediaManager {
    public static final boolean DEBUG = Log.isLoggable("InfoMediaManager", 3);
    public LocalBluetoothManager mBluetoothManager;
    public MediaDevice mCurrentConnectedDevice;
    @VisibleForTesting
    public String mPackageName;
    @VisibleForTesting
    public MediaRouter2Manager mRouterManager;
    @VisibleForTesting
    public final RouterManagerCallback mMediaRouterCallback = new RouterManagerCallback();
    @VisibleForTesting
    public final Executor mExecutor = Executors.newSingleThreadExecutor();

    /* loaded from: classes.dex */
    public class RouterManagerCallback implements MediaRouter2Manager.Callback {
        public RouterManagerCallback() {
        }

        public final void onPreferredFeaturesChanged(String str, List<String> list) {
            if (TextUtils.equals(InfoMediaManager.this.mPackageName, str)) {
                InfoMediaManager.this.refreshDevices();
            }
        }

        public final void onRequestFailed(int i) {
            InfoMediaManager infoMediaManager = InfoMediaManager.this;
            Objects.requireNonNull(infoMediaManager);
            Iterator it = new CopyOnWriteArrayList(infoMediaManager.mCallbacks).iterator();
            while (it.hasNext()) {
                ((MediaManager.MediaDeviceCallback) it.next()).onRequestFailed(i);
            }
        }

        public final void onRoutesAdded(List<MediaRoute2Info> list) {
            InfoMediaManager.this.refreshDevices();
        }

        public final void onRoutesChanged(List<MediaRoute2Info> list) {
            InfoMediaManager.this.refreshDevices();
        }

        public final void onRoutesRemoved(List<MediaRoute2Info> list) {
            InfoMediaManager.this.refreshDevices();
        }

        public final void onSessionUpdated(RoutingSessionInfo routingSessionInfo) {
            InfoMediaManager infoMediaManager = InfoMediaManager.this;
            Objects.requireNonNull(infoMediaManager);
            Iterator it = new CopyOnWriteArrayList(infoMediaManager.mCallbacks).iterator();
            while (it.hasNext()) {
                ((MediaManager.MediaDeviceCallback) it.next()).onDeviceAttributesChanged();
            }
        }

        public final void onTransferFailed(RoutingSessionInfo routingSessionInfo, MediaRoute2Info mediaRoute2Info) {
            InfoMediaManager infoMediaManager = InfoMediaManager.this;
            Objects.requireNonNull(infoMediaManager);
            Iterator it = new CopyOnWriteArrayList(infoMediaManager.mCallbacks).iterator();
            while (it.hasNext()) {
                ((MediaManager.MediaDeviceCallback) it.next()).onRequestFailed(0);
            }
        }

        public final void onTransferred(RoutingSessionInfo routingSessionInfo, RoutingSessionInfo routingSessionInfo2) {
            if (InfoMediaManager.DEBUG) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onTransferred() oldSession : ");
                m.append((Object) routingSessionInfo.getName());
                m.append(", newSession : ");
                m.append((Object) routingSessionInfo2.getName());
                Log.d("InfoMediaManager", m.toString());
            }
            InfoMediaManager.this.mMediaDevices.clear();
            InfoMediaManager infoMediaManager = InfoMediaManager.this;
            String str = null;
            infoMediaManager.mCurrentConnectedDevice = null;
            if (TextUtils.isEmpty(infoMediaManager.mPackageName)) {
                InfoMediaManager.this.buildAllRoutes();
            } else {
                InfoMediaManager.this.buildAvailableRoutes();
            }
            MediaDevice mediaDevice = InfoMediaManager.this.mCurrentConnectedDevice;
            if (mediaDevice != null) {
                str = mediaDevice.getId();
            }
            InfoMediaManager infoMediaManager2 = InfoMediaManager.this;
            Objects.requireNonNull(infoMediaManager2);
            Iterator it = new CopyOnWriteArrayList(infoMediaManager2.mCallbacks).iterator();
            while (it.hasNext()) {
                ((MediaManager.MediaDeviceCallback) it.next()).onConnectedDeviceChanged(str);
            }
        }
    }

    public final RoutingSessionInfo getRoutingSessionInfo() {
        return getRoutingSessionInfo(this.mPackageName);
    }

    public final void buildAllRoutes() {
        for (MediaRoute2Info mediaRoute2Info : this.mRouterManager.getAllRoutes()) {
            if (DEBUG) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("buildAllRoutes() route : ");
                m.append((Object) mediaRoute2Info.getName());
                m.append(", volume : ");
                m.append(mediaRoute2Info.getVolume());
                m.append(", type : ");
                m.append(mediaRoute2Info.getType());
                Log.d("InfoMediaManager", m.toString());
            }
            if (mediaRoute2Info.isSystemRoute()) {
                addMediaDevice(mediaRoute2Info);
            }
        }
    }

    public final void buildAvailableRoutes() {
        String str = this.mPackageName;
        ArrayList arrayList = new ArrayList();
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo(str);
        if (routingSessionInfo != null) {
            arrayList.addAll(this.mRouterManager.getSelectedRoutes(routingSessionInfo));
        }
        for (MediaRoute2Info mediaRoute2Info : this.mRouterManager.getTransferableRoutes(str)) {
            boolean z = false;
            Iterator it = arrayList.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (TextUtils.equals(mediaRoute2Info.getId(), ((MediaRoute2Info) it.next()).getId())) {
                        z = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (!z) {
                arrayList.add(mediaRoute2Info);
            }
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            MediaRoute2Info mediaRoute2Info2 = (MediaRoute2Info) it2.next();
            if (DEBUG) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("buildAvailableRoutes() route : ");
                m.append((Object) mediaRoute2Info2.getName());
                m.append(", volume : ");
                m.append(mediaRoute2Info2.getVolume());
                m.append(", type : ");
                m.append(mediaRoute2Info2.getType());
                Log.d("InfoMediaManager", m.toString());
            }
            addMediaDevice(mediaRoute2Info2);
        }
    }

    public final RoutingSessionInfo getRoutingSessionInfo(String str) {
        List routingSessions = this.mRouterManager.getRoutingSessions(str);
        if (routingSessions == null || routingSessions.isEmpty()) {
            return null;
        }
        return (RoutingSessionInfo) routingSessions.get(routingSessions.size() - 1);
    }

    public final void refreshDevices() {
        this.mMediaDevices.clear();
        this.mCurrentConnectedDevice = null;
        if (TextUtils.isEmpty(this.mPackageName)) {
            buildAllRoutes();
        } else {
            buildAvailableRoutes();
        }
        Iterator it = new CopyOnWriteArrayList(this.mCallbacks).iterator();
        while (it.hasNext()) {
            ((MediaManager.MediaDeviceCallback) it.next()).onDeviceListAdded(new ArrayList(this.mMediaDevices));
        }
    }

    public InfoMediaManager(Context context, String str, LocalBluetoothManager localBluetoothManager) {
        super(context);
        this.mRouterManager = MediaRouter2Manager.getInstance(context);
        this.mBluetoothManager = localBluetoothManager;
        if (!TextUtils.isEmpty(str)) {
            this.mPackageName = str;
        }
        context.getResources().getBoolean(17891813);
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x009c  */
    /* JADX WARN: Removed duplicated region for block: B:43:? A[RETURN, SYNTHETIC] */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void addMediaDevice(android.media.MediaRoute2Info r9) {
        /*
            r8 = this;
            int r0 = r9.getType()
            if (r0 == 0) goto L_0x006f
            r1 = 26
            if (r0 == r1) goto L_0x0046
            r1 = 2000(0x7d0, float:2.803E-42)
            if (r0 == r1) goto L_0x006f
            r1 = 2
            if (r0 == r1) goto L_0x003a
            r1 = 3
            if (r0 == r1) goto L_0x003a
            r1 = 4
            if (r0 == r1) goto L_0x003a
            r1 = 8
            if (r0 == r1) goto L_0x0046
            r1 = 9
            if (r0 == r1) goto L_0x003a
            r1 = 22
            if (r0 == r1) goto L_0x003a
            r1 = 23
            if (r0 == r1) goto L_0x0046
            r1 = 1001(0x3e9, float:1.403E-42)
            if (r0 == r1) goto L_0x006f
            r1 = 1002(0x3ea, float:1.404E-42)
            if (r0 == r1) goto L_0x006f
            switch(r0) {
                case 11: goto L_0x003a;
                case 12: goto L_0x003a;
                case 13: goto L_0x003a;
                default: goto L_0x0032;
            }
        L_0x0032:
            java.lang.String r9 = "addMediaDevice() unknown device type : "
            java.lang.String r1 = "InfoMediaManager"
            androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline1.m(r9, r0, r1)
            goto L_0x006d
        L_0x003a:
            com.android.settingslib.media.PhoneMediaDevice r0 = new com.android.settingslib.media.PhoneMediaDevice
            android.content.Context r1 = r8.mContext
            android.media.MediaRouter2Manager r2 = r8.mRouterManager
            java.lang.String r3 = r8.mPackageName
            r0.<init>(r1, r2, r9, r3)
            goto L_0x009a
        L_0x0046:
            android.bluetooth.BluetoothAdapter r0 = android.bluetooth.BluetoothAdapter.getDefaultAdapter()
            java.lang.String r1 = r9.getAddress()
            android.bluetooth.BluetoothDevice r0 = r0.getRemoteDevice(r1)
            com.android.settingslib.bluetooth.LocalBluetoothManager r1 = r8.mBluetoothManager
            java.util.Objects.requireNonNull(r1)
            com.android.settingslib.bluetooth.CachedBluetoothDeviceManager r1 = r1.mCachedDeviceManager
            com.android.settingslib.bluetooth.CachedBluetoothDevice r4 = r1.findDevice(r0)
            if (r4 == 0) goto L_0x006d
            com.android.settingslib.media.BluetoothMediaDevice r0 = new com.android.settingslib.media.BluetoothMediaDevice
            android.content.Context r3 = r8.mContext
            android.media.MediaRouter2Manager r5 = r8.mRouterManager
            java.lang.String r7 = r8.mPackageName
            r2 = r0
            r6 = r9
            r2.<init>(r3, r4, r5, r6, r7)
            goto L_0x009a
        L_0x006d:
            r0 = 0
            goto L_0x009a
        L_0x006f:
            com.android.settingslib.media.InfoMediaDevice r0 = new com.android.settingslib.media.InfoMediaDevice
            android.content.Context r1 = r8.mContext
            android.media.MediaRouter2Manager r2 = r8.mRouterManager
            java.lang.String r3 = r8.mPackageName
            r0.<init>(r1, r2, r9, r3)
            java.lang.String r1 = r8.mPackageName
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 != 0) goto L_0x009a
            android.media.RoutingSessionInfo r1 = r8.getRoutingSessionInfo()
            java.util.List r1 = r1.getSelectedRoutes()
            java.lang.String r9 = r9.getId()
            boolean r9 = r1.contains(r9)
            if (r9 == 0) goto L_0x009a
            com.android.settingslib.media.MediaDevice r9 = r8.mCurrentConnectedDevice
            if (r9 != 0) goto L_0x009a
            r8.mCurrentConnectedDevice = r0
        L_0x009a:
            if (r0 == 0) goto L_0x00a1
            java.util.ArrayList r8 = r8.mMediaDevices
            r8.add(r0)
        L_0x00a1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.media.InfoMediaManager.addMediaDevice(android.media.MediaRoute2Info):void");
    }
}
