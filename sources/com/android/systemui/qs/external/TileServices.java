package com.android.systemui.qs.external;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.quicksettings.IQSService;
import android.service.quicksettings.Tile;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda0;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda3;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class TileServices extends IQSService.Stub {
    public static final AnonymousClass3 SERVICE_SORT = new Comparator<TileServiceManager>() { // from class: com.android.systemui.qs.external.TileServices.3
        @Override // java.util.Comparator
        public final int compare(TileServiceManager tileServiceManager, TileServiceManager tileServiceManager2) {
            TileServiceManager tileServiceManager3 = tileServiceManager;
            TileServiceManager tileServiceManager4 = tileServiceManager2;
            Objects.requireNonNull(tileServiceManager3);
            int i = tileServiceManager3.mPriority;
            Objects.requireNonNull(tileServiceManager4);
            return -Integer.compare(i, tileServiceManager4.mPriority);
        }
    };
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final Context mContext;
    public final Handler mHandler;
    public final QSTileHost mHost;
    public final KeyguardStateController mKeyguardStateController;
    public final AnonymousClass2 mRequestListeningReceiver;
    public final UserTracker mUserTracker;
    public final ArrayMap<CustomTile, TileServiceManager> mServices = new ArrayMap<>();
    public final ArrayMap<ComponentName, CustomTile> mTiles = new ArrayMap<>();
    public final ArrayMap<IBinder, CustomTile> mTokenMap = new ArrayMap<>();
    public int mMaxBound = 3;
    public final Handler mMainHandler = new Handler(Looper.getMainLooper());

    public final CustomTile getTileForToken(IBinder iBinder) {
        CustomTile customTile;
        synchronized (this.mServices) {
            customTile = this.mTokenMap.get(iBinder);
        }
        return customTile;
    }

    public final boolean isLocked() {
        return this.mKeyguardStateController.isShowing();
    }

    public final boolean isSecure() {
        if (!this.mKeyguardStateController.isMethodSecure() || !this.mKeyguardStateController.isShowing()) {
            return false;
        }
        return true;
    }

    public final void recalculateBindAllowance() {
        ArrayList arrayList;
        boolean contains;
        synchronized (this.mServices) {
            arrayList = new ArrayList(this.mServices.values());
        }
        int size = arrayList.size();
        if (size > this.mMaxBound) {
            long currentTimeMillis = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                TileServiceManager tileServiceManager = (TileServiceManager) arrayList.get(i);
                Objects.requireNonNull(tileServiceManager);
                TileLifecycleManager tileLifecycleManager = tileServiceManager.mStateManager;
                Objects.requireNonNull(tileLifecycleManager);
                synchronized (tileLifecycleManager.mQueuedMessages) {
                    contains = tileLifecycleManager.mQueuedMessages.contains(2);
                }
                if (contains) {
                    tileServiceManager.mPriority = Integer.MAX_VALUE;
                } else if (tileServiceManager.mShowingDialog) {
                    tileServiceManager.mPriority = 2147483646;
                } else if (tileServiceManager.mJustBound) {
                    tileServiceManager.mPriority = 2147483645;
                } else if (!tileServiceManager.mBindRequested) {
                    tileServiceManager.mPriority = Integer.MIN_VALUE;
                } else {
                    long j = currentTimeMillis - tileServiceManager.mLastUpdate;
                    if (j > 2147483644) {
                        tileServiceManager.mPriority = 2147483644;
                    } else {
                        tileServiceManager.mPriority = (int) j;
                    }
                }
            }
            Collections.sort(arrayList, SERVICE_SORT);
        }
        int i2 = 0;
        while (i2 < this.mMaxBound && i2 < size) {
            ((TileServiceManager) arrayList.get(i2)).setBindAllowed(true);
            i2++;
        }
        while (i2 < size) {
            ((TileServiceManager) arrayList.get(i2)).setBindAllowed(false);
            i2++;
        }
    }

    public final void verifyCaller(CustomTile customTile) {
        try {
            if (Binder.getCallingUid() != this.mContext.getPackageManager().getPackageUidAsUser(customTile.mComponent.getPackageName(), Binder.getCallingUserHandle().getIdentifier())) {
                throw new SecurityException("Component outside caller's uid");
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new SecurityException(e);
        }
    }

    /* renamed from: -$$Nest$mrequestListening  reason: not valid java name */
    public static void m76$$Nest$mrequestListening(TileServices tileServices, ComponentName componentName) {
        CustomTile customTile;
        Objects.requireNonNull(tileServices);
        synchronized (tileServices.mServices) {
            synchronized (tileServices.mServices) {
                customTile = tileServices.mTiles.get(componentName);
            }
            if (customTile == null) {
                Log.d("TileServices", "Couldn't find tile for " + componentName);
                return;
            }
            TileServiceManager tileServiceManager = tileServices.mServices.get(customTile);
            if (tileServiceManager == null) {
                Log.e("TileServices", "No TileServiceManager found in requestListening for tile " + customTile.mTileSpec);
            } else if (tileServiceManager.isActiveTile()) {
                tileServiceManager.setBindRequested(true);
                try {
                    tileServiceManager.mStateManager.onStartListening();
                } catch (RemoteException unused) {
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v4, types: [com.android.systemui.qs.external.TileServices$2, android.content.BroadcastReceiver] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public TileServices(com.android.systemui.qs.QSTileHost r2, android.os.Looper r3, com.android.systemui.broadcast.BroadcastDispatcher r4, com.android.systemui.settings.UserTracker r5, com.android.systemui.statusbar.policy.KeyguardStateController r6) {
        /*
            r1 = this;
            r1.<init>()
            android.util.ArrayMap r0 = new android.util.ArrayMap
            r0.<init>()
            r1.mServices = r0
            android.util.ArrayMap r0 = new android.util.ArrayMap
            r0.<init>()
            r1.mTiles = r0
            android.util.ArrayMap r0 = new android.util.ArrayMap
            r0.<init>()
            r1.mTokenMap = r0
            r0 = 3
            r1.mMaxBound = r0
            com.android.systemui.qs.external.TileServices$2 r0 = new com.android.systemui.qs.external.TileServices$2
            r0.<init>()
            r1.mRequestListeningReceiver = r0
            r1.mHost = r2
            r1.mKeyguardStateController = r6
            java.util.Objects.requireNonNull(r2)
            android.content.Context r2 = r2.mContext
            r1.mContext = r2
            r1.mBroadcastDispatcher = r4
            android.os.Handler r2 = new android.os.Handler
            r2.<init>(r3)
            r1.mHandler = r2
            android.os.Handler r2 = new android.os.Handler
            android.os.Looper r3 = android.os.Looper.getMainLooper()
            r2.<init>(r3)
            r1.mMainHandler = r2
            r1.mUserTracker = r5
            android.content.IntentFilter r1 = new android.content.IntentFilter
            java.lang.String r2 = "android.service.quicksettings.action.REQUEST_LISTENING"
            r1.<init>(r2)
            android.os.UserHandle r2 = android.os.UserHandle.ALL
            r3 = 0
            r4.registerReceiver(r0, r1, r3, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.external.TileServices.<init>(com.android.systemui.qs.QSTileHost, android.os.Looper, com.android.systemui.broadcast.BroadcastDispatcher, com.android.systemui.settings.UserTracker, com.android.systemui.statusbar.policy.KeyguardStateController):void");
    }

    public final Tile getTile(IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken == null) {
            return null;
        }
        verifyCaller(tileForToken);
        tileForToken.updateDefaultTileAndIcon();
        return tileForToken.mTile;
    }

    public final void onDialogHidden(IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            TileServiceManager tileServiceManager = this.mServices.get(tileForToken);
            Objects.requireNonNull(tileServiceManager);
            tileServiceManager.mShowingDialog = false;
            tileForToken.mIsShowingDialog = false;
            try {
                tileForToken.mWindowManager.removeWindowToken(tileForToken.mToken, 0);
            } catch (RemoteException unused) {
            }
        }
    }

    public final void onShowDialog(IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            tileForToken.mIsShowingDialog = true;
            QSTileHost qSTileHost = this.mHost;
            Objects.requireNonNull(qSTileHost);
            qSTileHost.mStatusBarOptional.ifPresent(QSTileHost$$ExternalSyntheticLambda3.INSTANCE);
            TileServiceManager tileServiceManager = this.mServices.get(tileForToken);
            Objects.requireNonNull(tileServiceManager);
            tileServiceManager.mShowingDialog = true;
        }
    }

    public final void onStartActivity(IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            QSTileHost qSTileHost = this.mHost;
            Objects.requireNonNull(qSTileHost);
            qSTileHost.mStatusBarOptional.ifPresent(QSTileHost$$ExternalSyntheticLambda3.INSTANCE);
        }
    }

    public final void onStartSuccessful(IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            synchronized (this.mServices) {
                TileServiceManager tileServiceManager = this.mServices.get(tileForToken);
                if (tileServiceManager != null && tileServiceManager.mStarted) {
                    tileServiceManager.mPendingBind = false;
                    tileForToken.refreshState(null);
                    return;
                }
                Log.e("TileServices", "TileServiceManager not started for " + tileForToken.mComponent, new IllegalStateException());
            }
        }
    }

    public final void startUnlockAndRun(IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            tileForToken.mActivityStarter.postQSRunnableDismissingKeyguard(new AccessPoint$$ExternalSyntheticLambda0(tileForToken, 5));
        }
    }

    public final void updateQsTile(Tile tile, IBinder iBinder) {
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            synchronized (this.mServices) {
                TileServiceManager tileServiceManager = this.mServices.get(tileForToken);
                if (tileServiceManager != null && tileServiceManager.mStarted) {
                    tileServiceManager.mPendingBind = false;
                    tileServiceManager.mLastUpdate = System.currentTimeMillis();
                    if (tileServiceManager.mBound && tileServiceManager.isActiveTile()) {
                        tileServiceManager.mStateManager.onStopListening();
                        tileServiceManager.setBindRequested(false);
                    }
                    tileServiceManager.mServices.recalculateBindAllowance();
                    tileForToken.mHandler.post(new CustomTile$$ExternalSyntheticLambda0(tileForToken, tile, 0));
                    tileForToken.refreshState(null);
                    return;
                }
                Log.e("TileServices", "TileServiceManager not started for " + tileForToken.mComponent, new IllegalStateException());
            }
        }
    }

    public final void updateStatusIcon(IBinder iBinder, Icon icon, String str) {
        final StatusBarIcon statusBarIcon;
        CustomTile tileForToken = getTileForToken(iBinder);
        if (tileForToken != null) {
            verifyCaller(tileForToken);
            try {
                final ComponentName componentName = tileForToken.mComponent;
                String packageName = componentName.getPackageName();
                UserHandle callingUserHandle = Binder.getCallingUserHandle();
                if (this.mContext.getPackageManager().getPackageInfoAsUser(packageName, 0, callingUserHandle.getIdentifier()).applicationInfo.isSystemApp()) {
                    if (icon != null) {
                        statusBarIcon = new StatusBarIcon(callingUserHandle, packageName, icon, 0, 0, str);
                    } else {
                        statusBarIcon = null;
                    }
                    this.mMainHandler.post(new Runnable() { // from class: com.android.systemui.qs.external.TileServices.1
                        @Override // java.lang.Runnable
                        public final void run() {
                            QSTileHost qSTileHost = TileServices.this.mHost;
                            Objects.requireNonNull(qSTileHost);
                            StatusBarIconController statusBarIconController = qSTileHost.mIconController;
                            statusBarIconController.setIcon(componentName.getClassName(), statusBarIcon);
                            statusBarIconController.setExternalIcon(componentName.getClassName());
                        }
                    });
                }
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
    }
}
