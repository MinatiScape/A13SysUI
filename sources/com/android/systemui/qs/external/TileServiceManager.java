package com.android.systemui.qs.external;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.android.systemui.settings.UserTracker;
import java.util.Objects;
/* loaded from: classes.dex */
public final class TileServiceManager {
    public static final String PREFS_FILE = "CustomTileModes";
    public boolean mBindAllowed;
    public boolean mBindRequested;
    public boolean mBound;
    public final Handler mHandler;
    public boolean mJustBound;
    public long mLastUpdate;
    public int mPriority;
    public final TileServices mServices;
    public boolean mShowingDialog;
    public final TileLifecycleManager mStateManager;
    public final AnonymousClass3 mUninstallReceiver;
    public final UserTracker mUserTracker;
    public boolean mPendingBind = true;
    public boolean mStarted = false;
    public final AnonymousClass1 mUnbind = new Runnable() { // from class: com.android.systemui.qs.external.TileServiceManager.1
        @Override // java.lang.Runnable
        public final void run() {
            TileServiceManager tileServiceManager = TileServiceManager.this;
            boolean z = tileServiceManager.mBound;
            if (z && !tileServiceManager.mBindRequested) {
                if (!z) {
                    Log.e("TileServiceManager", "Service not bound");
                    return;
                }
                tileServiceManager.mBound = false;
                tileServiceManager.mJustBound = false;
                tileServiceManager.mStateManager.setBindService(false);
            }
        }
    };
    public final Runnable mJustBoundOver = new Runnable() { // from class: com.android.systemui.qs.external.TileServiceManager.2
        @Override // java.lang.Runnable
        public final void run() {
            TileServiceManager tileServiceManager = TileServiceManager.this;
            tileServiceManager.mJustBound = false;
            tileServiceManager.mServices.recalculateBindAllowance();
        }
    };

    public final void bindService() {
        if (this.mBound) {
            Log.e("TileServiceManager", "Service already bound");
            return;
        }
        this.mPendingBind = true;
        this.mBound = true;
        this.mJustBound = true;
        this.mHandler.postDelayed(this.mJustBoundOver, 5000L);
        this.mStateManager.setBindService(true);
    }

    public final boolean isActiveTile() {
        TileLifecycleManager tileLifecycleManager = this.mStateManager;
        Objects.requireNonNull(tileLifecycleManager);
        try {
            PackageManagerAdapter packageManagerAdapter = tileLifecycleManager.mPackageManagerAdapter;
            ComponentName component = tileLifecycleManager.mIntent.getComponent();
            Objects.requireNonNull(packageManagerAdapter);
            Bundle bundle = packageManagerAdapter.mPackageManager.getServiceInfo(component, 794752).metaData;
            if (bundle == null) {
                return false;
            }
            if (bundle.getBoolean("android.service.quicksettings.ACTIVE_TILE", false)) {
                return true;
            }
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public final boolean isToggleableTile() {
        TileLifecycleManager tileLifecycleManager = this.mStateManager;
        Objects.requireNonNull(tileLifecycleManager);
        try {
            PackageManagerAdapter packageManagerAdapter = tileLifecycleManager.mPackageManagerAdapter;
            ComponentName component = tileLifecycleManager.mIntent.getComponent();
            Objects.requireNonNull(packageManagerAdapter);
            Bundle bundle = packageManagerAdapter.mPackageManager.getServiceInfo(component, 794752).metaData;
            if (bundle == null) {
                return false;
            }
            if (bundle.getBoolean("android.service.quicksettings.TOGGLEABLE_TILE", false)) {
                return true;
            }
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public final void setBindAllowed(boolean z) {
        boolean z2;
        if (this.mBindAllowed != z) {
            this.mBindAllowed = z;
            if (z || !(z2 = this.mBound)) {
                if (z && this.mBindRequested && !this.mBound) {
                    bindService();
                }
            } else if (!z2) {
                Log.e("TileServiceManager", "Service not bound");
            } else {
                this.mBound = false;
                this.mJustBound = false;
                this.mStateManager.setBindService(false);
            }
        }
    }

    public final void setBindRequested(boolean z) {
        if (this.mBindRequested != z) {
            this.mBindRequested = z;
            if (!this.mBindAllowed || !z || this.mBound) {
                this.mServices.recalculateBindAllowance();
            } else {
                this.mHandler.removeCallbacks(this.mUnbind);
                bindService();
            }
            if (this.mBound && !this.mBindRequested) {
                this.mHandler.postDelayed(this.mUnbind, 30000L);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.qs.external.TileServiceManager$1] */
    /* JADX WARN: Type inference failed for: r2v0, types: [com.android.systemui.qs.external.TileServiceManager$3, android.content.BroadcastReceiver] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public TileServiceManager(com.android.systemui.qs.external.TileServices r9, android.os.Handler r10, com.android.systemui.settings.UserTracker r11, com.android.systemui.qs.external.TileLifecycleManager r12) {
        /*
            r8 = this;
            r8.<init>()
            r0 = 1
            r8.mPendingBind = r0
            r0 = 0
            r8.mStarted = r0
            com.android.systemui.qs.external.TileServiceManager$1 r0 = new com.android.systemui.qs.external.TileServiceManager$1
            r0.<init>()
            r8.mUnbind = r0
            com.android.systemui.qs.external.TileServiceManager$2 r0 = new com.android.systemui.qs.external.TileServiceManager$2
            r0.<init>()
            r8.mJustBoundOver = r0
            com.android.systemui.qs.external.TileServiceManager$3 r2 = new com.android.systemui.qs.external.TileServiceManager$3
            r2.<init>()
            r8.mUninstallReceiver = r2
            r8.mServices = r9
            r8.mHandler = r10
            r8.mStateManager = r12
            r8.mUserTracker = r11
            android.content.IntentFilter r4 = new android.content.IntentFilter
            r4.<init>()
            java.lang.String r8 = "android.intent.action.PACKAGE_REMOVED"
            r4.addAction(r8)
            java.lang.String r8 = "package"
            r4.addDataScheme(r8)
            java.util.Objects.requireNonNull(r9)
            android.content.Context r1 = r9.mContext
            android.os.UserHandle r3 = r11.getUserHandle()
            r5 = 0
            r7 = 2
            r6 = r10
            r1.registerReceiverAsUser(r2, r3, r4, r5, r6, r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.external.TileServiceManager.<init>(com.android.systemui.qs.external.TileServices, android.os.Handler, com.android.systemui.settings.UserTracker, com.android.systemui.qs.external.TileLifecycleManager):void");
    }
}
