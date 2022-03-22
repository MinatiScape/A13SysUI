package com.android.systemui.navigationbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.om.IOverlayManager;
import android.content.pm.PackageManager;
import android.content.res.ApkAssets;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class NavigationModeController implements Dumpable {
    public final Context mContext;
    public Context mCurrentUserContext;
    public final AnonymousClass1 mDeviceProvisionedCallback;
    public final Executor mUiBgExecutor;
    public ArrayList<ModeChangedListener> mListeners = new ArrayList<>();
    public AnonymousClass2 mReceiver = new BroadcastReceiver() { // from class: com.android.systemui.navigationbar.NavigationModeController.2
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            Log.d("NavigationModeController", "ACTION_OVERLAY_CHANGED");
            NavigationModeController.this.updateCurrentInteractionMode(true);
        }
    };
    public final IOverlayManager mOverlayManager = IOverlayManager.Stub.asInterface(ServiceManager.getService("overlay"));

    /* loaded from: classes.dex */
    public interface ModeChangedListener {
        void onNavigationModeChanged(int i);
    }

    public final int addListener(ModeChangedListener modeChangedListener) {
        this.mListeners.add(modeChangedListener);
        return getCurrentInteractionMode(this.mCurrentUserContext);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "NavigationModeController:", "  mode=");
        m.append(getCurrentInteractionMode(this.mCurrentUserContext));
        printWriter.println(m.toString());
        try {
            str = String.join(", ", this.mOverlayManager.getDefaultOverlayPackages());
        } catch (RemoteException unused) {
            str = "failed_to_fetch";
        }
        printWriter.println("  defaultOverlays=" + str);
        dumpAssetPaths(this.mCurrentUserContext);
    }

    public final void dumpAssetPaths(Context context) {
        ApkAssets[] apkAssets;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("  contextUser=");
        m.append(this.mCurrentUserContext.getUserId());
        Log.d("NavigationModeController", m.toString());
        Log.d("NavigationModeController", "  assetPaths=");
        for (ApkAssets apkAssets2 : context.getResources().getAssets().getApkAssets()) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("    ");
            m2.append(apkAssets2.getDebugName());
            Log.d("NavigationModeController", m2.toString());
        }
    }

    public final Context getCurrentUserContext() {
        Objects.requireNonNull(ActivityManagerWrapper.sInstance);
        int currentUserId = ActivityManagerWrapper.getCurrentUserId();
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("getCurrentUserContext: contextUser=");
        m.append(this.mContext.getUserId());
        m.append(" currentUser=");
        m.append(currentUserId);
        Log.d("NavigationModeController", m.toString());
        if (this.mContext.getUserId() == currentUserId) {
            return this.mContext;
        }
        try {
            Context context = this.mContext;
            return context.createPackageContextAsUser(context.getPackageName(), 0, UserHandle.of(currentUserId));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("NavigationModeController", "Failed to create package context", e);
            return null;
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.navigationbar.NavigationModeController$1, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r1v0, types: [com.android.systemui.navigationbar.NavigationModeController$2] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public NavigationModeController(android.content.Context r8, com.android.systemui.statusbar.policy.DeviceProvisionedController r9, com.android.systemui.statusbar.policy.ConfigurationController r10, java.util.concurrent.Executor r11, com.android.systemui.dump.DumpManager r12) {
        /*
            r7 = this;
            r7.<init>()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r7.mListeners = r0
            com.android.systemui.navigationbar.NavigationModeController$1 r0 = new com.android.systemui.navigationbar.NavigationModeController$1
            r0.<init>()
            r7.mDeviceProvisionedCallback = r0
            com.android.systemui.navigationbar.NavigationModeController$2 r1 = new com.android.systemui.navigationbar.NavigationModeController$2
            r1.<init>()
            r7.mReceiver = r1
            r7.mContext = r8
            r7.mCurrentUserContext = r8
            java.lang.String r1 = "overlay"
            android.os.IBinder r1 = android.os.ServiceManager.getService(r1)
            android.content.om.IOverlayManager r1 = android.content.om.IOverlayManager.Stub.asInterface(r1)
            r7.mOverlayManager = r1
            r7.mUiBgExecutor = r11
            java.lang.String r11 = "NavigationModeController"
            r12.registerDumpable(r11, r7)
            r9.addCallback(r0)
            android.content.IntentFilter r4 = new android.content.IntentFilter
            java.lang.String r9 = "android.intent.action.OVERLAY_CHANGED"
            r4.<init>(r9)
            java.lang.String r9 = "package"
            r4.addDataScheme(r9)
            java.lang.String r9 = "android"
            r11 = 0
            r4.addDataSchemeSpecificPart(r9, r11)
            com.android.systemui.navigationbar.NavigationModeController$2 r2 = r7.mReceiver
            android.os.UserHandle r3 = android.os.UserHandle.ALL
            r5 = 0
            r6 = 0
            r1 = r8
            r1.registerReceiverAsUser(r2, r3, r4, r5, r6)
            com.android.systemui.navigationbar.NavigationModeController$3 r8 = new com.android.systemui.navigationbar.NavigationModeController$3
            r8.<init>()
            r10.addCallback(r8)
            r7.updateCurrentInteractionMode(r11)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.navigationbar.NavigationModeController.<init>(android.content.Context, com.android.systemui.statusbar.policy.DeviceProvisionedController, com.android.systemui.statusbar.policy.ConfigurationController, java.util.concurrent.Executor, com.android.systemui.dump.DumpManager):void");
    }

    public static int getCurrentInteractionMode(Context context) {
        int integer = context.getResources().getInteger(17694878);
        StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("getCurrentInteractionMode: mode=", integer, " contextUser=");
        m.append(context.getUserId());
        Log.d("NavigationModeController", m.toString());
        return integer;
    }

    public final void updateCurrentInteractionMode(boolean z) {
        Context currentUserContext = getCurrentUserContext();
        this.mCurrentUserContext = currentUserContext;
        final int currentInteractionMode = getCurrentInteractionMode(currentUserContext);
        this.mUiBgExecutor.execute(new Runnable() { // from class: com.android.systemui.navigationbar.NavigationModeController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                NavigationModeController navigationModeController = NavigationModeController.this;
                int i = currentInteractionMode;
                Objects.requireNonNull(navigationModeController);
                Settings.Secure.putString(navigationModeController.mCurrentUserContext.getContentResolver(), "navigation_mode", String.valueOf(i));
            }
        });
        Log.d("NavigationModeController", "updateCurrentInteractionMode: mode=" + currentInteractionMode);
        dumpAssetPaths(this.mCurrentUserContext);
        if (z) {
            for (int i = 0; i < this.mListeners.size(); i++) {
                this.mListeners.get(i).onNavigationModeChanged(currentInteractionMode);
            }
        }
    }
}
