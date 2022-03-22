package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.BootCompleteCache;
import com.android.systemui.appops.AppOpItem;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.Utils;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda10;
import com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda2;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LocationControllerImpl extends BroadcastReceiver implements LocationController, AppOpsController.Callback {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final AppOpsController mAppOpsController;
    public boolean mAreActiveLocationRequests;
    public final Handler mBackgroundHandler;
    public final BootCompleteCache mBootCompleteCache;
    public final AnonymousClass1 mContentObserver;
    public final Context mContext;
    public final DeviceConfigProxy mDeviceConfigProxy;
    public final H mHandler;
    public final PackageManager mPackageManager;
    public final SecureSettings mSecureSettings;
    public boolean mShouldDisplayAllAccesses = DeviceConfig.getBoolean("privacy", "location_indicators_small_enabled", false);
    public boolean mShowSystemAccessesFlag = DeviceConfig.getBoolean("privacy", "location_indicators_show_system", false);
    public boolean mShowSystemAccessesSetting;
    public final UiEventLogger mUiEventLogger;
    public final UserTracker mUserTracker;

    /* loaded from: classes.dex */
    public final class H extends Handler {
        public static final /* synthetic */ int $r8$clinit = 0;
        public ArrayList<LocationController.LocationChangeCallback> mSettingsChangeCallbacks = new ArrayList<>();

        public H(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                Utils.safeForeach(this.mSettingsChangeCallbacks, new LocationControllerImpl$H$$ExternalSyntheticLambda0(LocationControllerImpl.this.isLocationEnabled(), 0));
            } else if (i == 2) {
                Utils.safeForeach(this.mSettingsChangeCallbacks, new ShellCommandHandlerImpl$$ExternalSyntheticLambda2(this, 3));
            } else if (i == 3) {
                this.mSettingsChangeCallbacks.add((LocationController.LocationChangeCallback) message.obj);
            } else if (i == 4) {
                this.mSettingsChangeCallbacks.remove((LocationController.LocationChangeCallback) message.obj);
            }
        }
    }

    /* loaded from: classes.dex */
    public enum LocationIndicatorEvent implements UiEventLogger.UiEventEnum {
        LOCATION_INDICATOR_MONITOR_HIGH_POWER(935),
        LOCATION_INDICATOR_SYSTEM_APP(936),
        LOCATION_INDICATOR_NON_SYSTEM_APP(937);
        
        private final int mId;

        LocationIndicatorEvent(int i) {
            this.mId = i;
        }

        public final int getId() {
            return this.mId;
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(LocationController.LocationChangeCallback locationChangeCallback) {
        this.mHandler.obtainMessage(3, locationChangeCallback).sendToTarget();
        this.mHandler.sendEmptyMessage(1);
    }

    public boolean areActiveHighPowerLocationRequests() {
        ArrayList activeAppOps = this.mAppOpsController.getActiveAppOps();
        int size = activeAppOps.size();
        for (int i = 0; i < size; i++) {
            AppOpItem appOpItem = (AppOpItem) activeAppOps.get(i);
            Objects.requireNonNull(appOpItem);
            if (appOpItem.mCode == 42) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00a2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void areActiveLocationRequests() {
        /*
            Method dump skipped, instructions count: 228
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.LocationControllerImpl.areActiveLocationRequests():void");
    }

    @Override // com.android.systemui.statusbar.policy.LocationController
    public final boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) this.mContext.getSystemService("location");
        if (!this.mBootCompleteCache.isBootComplete() || !locationManager.isLocationEnabledForUser(this.mUserTracker.getUserHandle())) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(LocationController.LocationChangeCallback locationChangeCallback) {
        this.mHandler.obtainMessage(4, locationChangeCallback).sendToTarget();
    }

    @Override // com.android.systemui.statusbar.policy.LocationController
    public final boolean setLocationEnabled(boolean z) {
        int userId = this.mUserTracker.getUserId();
        if (((UserManager) this.mContext.getSystemService("user")).hasUserRestriction("no_share_location", UserHandle.of(userId))) {
            return false;
        }
        Context context = this.mContext;
        Settings.Secure.putIntForUser(context.getContentResolver(), "location_changer", 2, userId);
        ((LocationManager) context.getSystemService(LocationManager.class)).setLocationEnabledForUser(z, UserHandle.of(userId));
        return true;
    }

    public final void updateActiveLocationRequests() {
        if (this.mShouldDisplayAllAccesses) {
            this.mBackgroundHandler.post(new VolumeDialogImpl$$ExternalSyntheticLambda10(this, 6));
            return;
        }
        boolean z = this.mAreActiveLocationRequests;
        boolean areActiveHighPowerLocationRequests = areActiveHighPowerLocationRequests();
        this.mAreActiveLocationRequests = areActiveHighPowerLocationRequests;
        if (areActiveHighPowerLocationRequests != z) {
            this.mHandler.sendEmptyMessage(2);
            if (this.mAreActiveLocationRequests) {
                this.mUiEventLogger.log(LocationIndicatorEvent.LOCATION_INDICATOR_MONITOR_HIGH_POWER);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v5, types: [com.android.systemui.statusbar.policy.LocationControllerImpl$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public LocationControllerImpl(android.content.Context r1, com.android.systemui.appops.AppOpsController r2, com.android.systemui.util.DeviceConfigProxy r3, android.os.Looper r4, android.os.Handler r5, com.android.systemui.broadcast.BroadcastDispatcher r6, com.android.systemui.BootCompleteCache r7, com.android.systemui.settings.UserTracker r8, android.content.pm.PackageManager r9, com.android.internal.logging.UiEventLogger r10, com.android.systemui.util.settings.SecureSettings r11) {
        /*
            r0 = this;
            r0.<init>()
            r0.mContext = r1
            r0.mAppOpsController = r2
            r0.mDeviceConfigProxy = r3
            r0.mBootCompleteCache = r7
            com.android.systemui.statusbar.policy.LocationControllerImpl$H r1 = new com.android.systemui.statusbar.policy.LocationControllerImpl$H
            r1.<init>(r4)
            r0.mHandler = r1
            r0.mUserTracker = r8
            r0.mUiEventLogger = r10
            r0.mSecureSettings = r11
            r0.mBackgroundHandler = r5
            r0.mPackageManager = r9
            java.util.Objects.requireNonNull(r3)
            java.lang.String r4 = "privacy"
            java.lang.String r7 = "location_indicators_small_enabled"
            r8 = 0
            boolean r7 = android.provider.DeviceConfig.getBoolean(r4, r7, r8)
            r0.mShouldDisplayAllAccesses = r7
            java.util.Objects.requireNonNull(r3)
            java.lang.String r3 = "location_indicators_show_system"
            boolean r3 = android.provider.DeviceConfig.getBoolean(r4, r3, r8)
            r0.mShowSystemAccessesFlag = r3
            java.lang.String r3 = "locationShowSystemOps"
            int r3 = r11.getInt(r3, r8)
            r7 = 1
            if (r3 != r7) goto L_0x003f
            goto L_0x0040
        L_0x003f:
            r7 = r8
        L_0x0040:
            r0.mShowSystemAccessesSetting = r7
            com.android.systemui.statusbar.policy.LocationControllerImpl$1 r3 = new com.android.systemui.statusbar.policy.LocationControllerImpl$1
            r3.<init>(r5)
            r0.mContentObserver = r3
            r11.registerContentObserver(r3)
            java.util.Objects.requireNonNull(r5)
            androidx.mediarouter.media.MediaRoute2Provider$$ExternalSyntheticLambda0 r3 = new androidx.mediarouter.media.MediaRoute2Provider$$ExternalSyntheticLambda0
            r3.<init>(r5)
            com.android.systemui.statusbar.policy.LocationControllerImpl$$ExternalSyntheticLambda0 r7 = new com.android.systemui.statusbar.policy.LocationControllerImpl$$ExternalSyntheticLambda0
            r7.<init>(r0, r8)
            android.provider.DeviceConfig.addOnPropertiesChangedListener(r4, r3, r7)
            android.content.IntentFilter r3 = new android.content.IntentFilter
            r3.<init>()
            java.lang.String r4 = "android.location.MODE_CHANGED"
            r3.addAction(r4)
            android.os.UserHandle r4 = android.os.UserHandle.ALL
            r6.registerReceiverWithHandler(r0, r3, r1, r4)
            r1 = 3
            int[] r1 = new int[r1]
            r1 = {x0080: FILL_ARRAY_DATA  , data: [0, 1, 42} // fill-array
            r2.addCallback(r1, r0)
            com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda0 r1 = new com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda0
            r2 = 9
            r1.<init>(r0, r2)
            r5.post(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.LocationControllerImpl.<init>(android.content.Context, com.android.systemui.appops.AppOpsController, com.android.systemui.util.DeviceConfigProxy, android.os.Looper, android.os.Handler, com.android.systemui.broadcast.BroadcastDispatcher, com.android.systemui.BootCompleteCache, com.android.systemui.settings.UserTracker, android.content.pm.PackageManager, com.android.internal.logging.UiEventLogger, com.android.systemui.util.settings.SecureSettings):void");
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        if ("android.location.MODE_CHANGED".equals(intent.getAction())) {
            H h = this.mHandler;
            int i = H.$r8$clinit;
            Objects.requireNonNull(h);
            Utils.safeForeach(h.mSettingsChangeCallbacks, new LocationControllerImpl$H$$ExternalSyntheticLambda0(LocationControllerImpl.this.isLocationEnabled(), 0));
        }
    }

    @Override // com.android.systemui.appops.AppOpsController.Callback
    public final void onActiveStateChanged(int i, int i2, String str, boolean z) {
        updateActiveLocationRequests();
    }

    @Override // com.android.systemui.statusbar.policy.LocationController
    public final boolean isLocationActive() {
        return this.mAreActiveLocationRequests;
    }
}
