package com.android.systemui.statusbar.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.SimpleClock;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda1;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda3;
import com.android.systemui.power.PowerUI$$ExternalSyntheticLambda0;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.connectivity.AccessPointController;
import com.android.wifitrackerlib.MergedCarrierEntry;
import com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda0;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wifitrackerlib.WifiPickerTracker;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class AccessPointControllerImpl implements AccessPointController, WifiPickerTracker.WifiPickerTrackerCallback, LifecycleOwner {
    public static final boolean DEBUG = Log.isLoggable("AccessPointController", 3);
    public int mCurrentUser;
    public final Executor mMainExecutor;
    public final UserManager mUserManager;
    public final UserTracker mUserTracker;
    public WifiPickerTracker mWifiPickerTracker;
    public WifiPickerTrackerFactory mWifiPickerTrackerFactory;
    public final ArrayList<AccessPointController.AccessPointCallback> mCallbacks = new ArrayList<>();
    public final LifecycleRegistry mLifecycle = new LifecycleRegistry(this, true);

    /* loaded from: classes.dex */
    public static class WifiPickerTrackerFactory {
        public final AnonymousClass1 mClock = new SimpleClock(ZoneOffset.UTC) { // from class: com.android.systemui.statusbar.connectivity.AccessPointControllerImpl.WifiPickerTrackerFactory.1
            public final long millis() {
                return SystemClock.elapsedRealtime();
            }
        };
        public final ConnectivityManager mConnectivityManager;
        public final Context mContext;
        public final Handler mMainHandler;
        public final WifiManager mWifiManager;
        public final Handler mWorkerHandler;

        public WifiPickerTrackerFactory(Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, Handler handler, Handler handler2) {
            this.mContext = context;
            this.mWifiManager = wifiManager;
            this.mConnectivityManager = connectivityManager;
            this.mMainHandler = handler;
            this.mWorkerHandler = handler2;
        }
    }

    static {
        int i = WifiIcons.WIFI_LEVEL_COUNT;
    }

    @Override // com.android.systemui.statusbar.connectivity.AccessPointController
    public final void addAccessPointCallback(InternetDialogController internetDialogController) {
        if (!this.mCallbacks.contains(internetDialogController)) {
            if (DEBUG) {
                Log.d("AccessPointController", "addCallback " + internetDialogController);
            }
            this.mCallbacks.add(internetDialogController);
            if (this.mCallbacks.size() == 1) {
                this.mMainExecutor.execute(new StandardWifiEntry$$ExternalSyntheticLambda0(this, 5));
            }
        }
    }

    @Override // com.android.systemui.statusbar.connectivity.AccessPointController
    public final boolean canConfigMobileData() {
        if (this.mUserManager.hasUserRestriction("no_config_mobile_networks", UserHandle.of(this.mCurrentUser)) || !this.mUserTracker.getUserInfo().isAdmin()) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.connectivity.AccessPointController
    public final boolean canConfigWifi() {
        boolean z;
        WifiPickerTrackerFactory wifiPickerTrackerFactory = this.mWifiPickerTrackerFactory;
        Objects.requireNonNull(wifiPickerTrackerFactory);
        if (wifiPickerTrackerFactory.mWifiManager != null) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            return false;
        }
        return !this.mUserManager.hasUserRestriction("no_config_wifi", new UserHandle(this.mCurrentUser));
    }

    public final void finalize() throws Throwable {
        this.mMainExecutor.execute(new ScreenDecorations$$ExternalSyntheticLambda3(this, 2));
        super.finalize();
    }

    public final void fireAccessPointsCallback(List<WifiEntry> list) {
        Iterator<AccessPointController.AccessPointCallback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            it.next().onAccessPointsChanged(list);
        }
    }

    @Override // com.android.systemui.statusbar.connectivity.AccessPointController
    public final MergedCarrierEntry getMergedCarrierEntry() {
        WifiPickerTracker wifiPickerTracker = this.mWifiPickerTracker;
        if (wifiPickerTracker != null) {
            return wifiPickerTracker.mMergedCarrierEntry;
        }
        fireAccessPointsCallback(Collections.emptyList());
        return null;
    }

    @Override // com.android.systemui.statusbar.connectivity.AccessPointController
    public final void removeAccessPointCallback(InternetDialogController internetDialogController) {
        if (DEBUG) {
            Log.d("AccessPointController", "removeCallback " + internetDialogController);
        }
        this.mCallbacks.remove(internetDialogController);
        if (this.mCallbacks.isEmpty()) {
            this.mMainExecutor.execute(new ScreenDecorations$$ExternalSyntheticLambda1(this, 5));
        }
    }

    @Override // com.android.systemui.statusbar.connectivity.AccessPointController
    public final void scanForAccessPoints() {
        ArrayList arrayList;
        WifiPickerTracker wifiPickerTracker = this.mWifiPickerTracker;
        if (wifiPickerTracker == null) {
            fireAccessPointsCallback(Collections.emptyList());
            return;
        }
        synchronized (wifiPickerTracker.mLock) {
            arrayList = new ArrayList(wifiPickerTracker.mWifiEntries);
        }
        WifiPickerTracker wifiPickerTracker2 = this.mWifiPickerTracker;
        Objects.requireNonNull(wifiPickerTracker2);
        WifiEntry wifiEntry = wifiPickerTracker2.mConnectedWifiEntry;
        if (wifiEntry != null) {
            arrayList.add(0, wifiEntry);
        }
        fireAccessPointsCallback(arrayList);
    }

    public AccessPointControllerImpl(UserManager userManager, UserTracker userTracker, Executor executor, WifiPickerTrackerFactory wifiPickerTrackerFactory) {
        this.mUserManager = userManager;
        this.mUserTracker = userTracker;
        this.mCurrentUser = userTracker.getUserId();
        this.mMainExecutor = executor;
        this.mWifiPickerTrackerFactory = wifiPickerTrackerFactory;
        executor.execute(new PowerUI$$ExternalSyntheticLambda0(this, 2));
    }

    @Override // androidx.lifecycle.LifecycleOwner
    public final Lifecycle getLifecycle() {
        return this.mLifecycle;
    }
}
