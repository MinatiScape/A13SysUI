package com.android.systemui.statusbar.policy;

import android.app.admin.DeviceAdminInfo;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.net.NetworkRequest;
import android.net.VpnManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.net.LegacyVpnInfo;
import com.android.internal.net.VpnConfig;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.policy.SecurityController;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Executor;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public final class SecurityControllerImpl extends CurrentUserTracker implements SecurityController {
    public static final boolean DEBUG = Log.isLoggable("SecurityController", 3);
    public static final NetworkRequest REQUEST = new NetworkRequest.Builder().clearCapabilities().build();
    public final Executor mBgExecutor;
    public final AnonymousClass2 mBroadcastReceiver;
    public final Context mContext;
    public int mCurrentUserId;
    public final DevicePolicyManager mDevicePolicyManager;
    public final AnonymousClass1 mNetworkCallback;
    public final PackageManager mPackageManager;
    public final UserManager mUserManager;
    public final VpnManager mVpnManager;
    public int mVpnUserId;
    @GuardedBy({"mCallbacks"})
    public final ArrayList<SecurityController.SecurityControllerCallback> mCallbacks = new ArrayList<>();
    public SparseArray<VpnConfig> mCurrentVpns = new SparseArray<>();
    public ArrayMap<Integer, Boolean> mHasCACerts = new ArrayMap<>();

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(SecurityController.SecurityControllerCallback securityControllerCallback) {
        SecurityController.SecurityControllerCallback securityControllerCallback2 = securityControllerCallback;
        synchronized (this.mCallbacks) {
            if (securityControllerCallback2 != null) {
                if (!this.mCallbacks.contains(securityControllerCallback2)) {
                    if (DEBUG) {
                        Log.d("SecurityController", "addCallback " + securityControllerCallback2);
                    }
                    this.mCallbacks.add(securityControllerCallback2);
                }
            }
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("SecurityController state:");
        printWriter.print("  mCurrentVpns={");
        for (int i = 0; i < this.mCurrentVpns.size(); i++) {
            if (i > 0) {
                printWriter.print(", ");
            }
            printWriter.print(this.mCurrentVpns.keyAt(i));
            printWriter.print('=');
            printWriter.print(this.mCurrentVpns.valueAt(i).user);
        }
        printWriter.println("}");
    }

    public final void fireCallbacks() {
        synchronized (this.mCallbacks) {
            Iterator<SecurityController.SecurityControllerCallback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onStateChanged();
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final DeviceAdminInfo getDeviceAdminInfo() {
        ComponentName profileOwnerOrDeviceOwnerSupervisionComponent = this.mDevicePolicyManager.getProfileOwnerOrDeviceOwnerSupervisionComponent(new UserHandle(this.mCurrentUserId));
        try {
            ResolveInfo resolveInfo = new ResolveInfo();
            resolveInfo.activityInfo = this.mPackageManager.getReceiverInfo(profileOwnerOrDeviceOwnerSupervisionComponent, 128);
            return new DeviceAdminInfo(this.mContext, resolveInfo);
        } catch (PackageManager.NameNotFoundException | IOException | XmlPullParserException unused) {
            return null;
        }
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final ComponentName getDeviceOwnerComponentOnAnyUser() {
        return this.mDevicePolicyManager.getDeviceOwnerComponentOnAnyUser();
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final CharSequence getDeviceOwnerOrganizationName() {
        return this.mDevicePolicyManager.getDeviceOwnerOrganizationName();
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final int getDeviceOwnerType(ComponentName componentName) {
        return this.mDevicePolicyManager.getDeviceOwnerType(componentName);
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final Drawable getIcon(DeviceAdminInfo deviceAdminInfo) {
        if (deviceAdminInfo == null) {
            return null;
        }
        return deviceAdminInfo.loadIcon(this.mPackageManager);
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final CharSequence getLabel(DeviceAdminInfo deviceAdminInfo) {
        if (deviceAdminInfo == null) {
            return null;
        }
        return deviceAdminInfo.loadLabel(this.mPackageManager);
    }

    public final String getNameForVpnConfig(VpnConfig vpnConfig, UserHandle userHandle) {
        if (vpnConfig.legacy) {
            return this.mContext.getString(2131952654);
        }
        String str = vpnConfig.user;
        try {
            Context context = this.mContext;
            return VpnConfig.getVpnLabel(context.createPackageContextAsUser(context.getPackageName(), 0, userHandle), str).toString();
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("SecurityController", "Package " + str + " is not present", e);
            return null;
        }
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final String getPrimaryVpnName() {
        VpnConfig vpnConfig = this.mCurrentVpns.get(this.mVpnUserId);
        if (vpnConfig != null) {
            return getNameForVpnConfig(vpnConfig, new UserHandle(this.mVpnUserId));
        }
        return null;
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final CharSequence getWorkProfileOrganizationName() {
        int workProfileUserId = getWorkProfileUserId(this.mCurrentUserId);
        if (workProfileUserId == -10000) {
            return null;
        }
        return this.mDevicePolicyManager.getOrganizationNameForUser(workProfileUserId);
    }

    public final int getWorkProfileUserId(int i) {
        for (UserInfo userInfo : this.mUserManager.getProfiles(i)) {
            if (userInfo.isManagedProfile()) {
                return userInfo.id;
            }
        }
        return -10000;
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final String getWorkProfileVpnName() {
        VpnConfig vpnConfig;
        int workProfileUserId = getWorkProfileUserId(this.mVpnUserId);
        if (workProfileUserId == -10000 || (vpnConfig = this.mCurrentVpns.get(workProfileUserId)) == null) {
            return null;
        }
        return getNameForVpnConfig(vpnConfig, UserHandle.of(workProfileUserId));
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final boolean hasCACertInCurrentUser() {
        Boolean bool = this.mHasCACerts.get(Integer.valueOf(this.mCurrentUserId));
        if (bool == null || !bool.booleanValue()) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final boolean hasCACertInWorkProfile() {
        Boolean bool;
        int workProfileUserId = getWorkProfileUserId(this.mCurrentUserId);
        if (workProfileUserId == -10000 || (bool = this.mHasCACerts.get(Integer.valueOf(workProfileUserId))) == null || !bool.booleanValue()) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final boolean hasWorkProfile() {
        if (getWorkProfileUserId(this.mCurrentUserId) != -10000) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final boolean isDeviceManaged() {
        return this.mDevicePolicyManager.isDeviceManaged();
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final boolean isNetworkLoggingEnabled() {
        return this.mDevicePolicyManager.isNetworkLoggingEnabled(null);
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final boolean isParentalControlsEnabled() {
        if (this.mDevicePolicyManager.getProfileOwnerOrDeviceOwnerSupervisionComponent(new UserHandle(this.mCurrentUserId)) != null) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final boolean isProfileOwnerOfOrganizationOwnedDevice() {
        return this.mDevicePolicyManager.isOrganizationOwnedDeviceWithManagedProfile();
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final boolean isVpnBranded() {
        String str;
        VpnConfig vpnConfig = this.mCurrentVpns.get(this.mVpnUserId);
        if (vpnConfig == null) {
            return false;
        }
        if (vpnConfig.legacy) {
            str = null;
        } else {
            str = vpnConfig.user;
        }
        if (str == null) {
            return false;
        }
        try {
            ApplicationInfo applicationInfo = this.mPackageManager.getApplicationInfo(str, 128);
            if (!(applicationInfo == null || applicationInfo.metaData == null || !applicationInfo.isSystemApp())) {
                return applicationInfo.metaData.getBoolean("com.android.systemui.IS_BRANDED", false);
            }
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final boolean isVpnEnabled() {
        for (int i : this.mUserManager.getProfileIdsWithDisabled(this.mVpnUserId)) {
            if (this.mCurrentVpns.get(i) != null) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public final boolean isWorkProfileOn() {
        UserHandle of = UserHandle.of(getWorkProfileUserId(this.mCurrentUserId));
        if (of == null || this.mUserManager.isQuietModeEnabled(of)) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.settings.CurrentUserTracker
    public final void onUserSwitched(int i) {
        this.mCurrentUserId = i;
        UserInfo userInfo = this.mUserManager.getUserInfo(i);
        if (userInfo.isRestricted()) {
            this.mVpnUserId = userInfo.restrictedProfileParentId;
        } else {
            this.mVpnUserId = this.mCurrentUserId;
        }
        fireCallbacks();
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(SecurityController.SecurityControllerCallback securityControllerCallback) {
        SecurityController.SecurityControllerCallback securityControllerCallback2 = securityControllerCallback;
        synchronized (this.mCallbacks) {
            if (securityControllerCallback2 != null) {
                if (DEBUG) {
                    Log.d("SecurityController", "removeCallback " + securityControllerCallback2);
                }
                this.mCallbacks.remove(securityControllerCallback2);
            }
        }
    }

    /* renamed from: -$$Nest$mupdateState  reason: not valid java name */
    public static void m115$$Nest$mupdateState(SecurityControllerImpl securityControllerImpl) {
        LegacyVpnInfo legacyVpnInfo;
        Objects.requireNonNull(securityControllerImpl);
        SparseArray<VpnConfig> sparseArray = new SparseArray<>();
        for (UserInfo userInfo : securityControllerImpl.mUserManager.getUsers()) {
            VpnConfig vpnConfig = securityControllerImpl.mVpnManager.getVpnConfig(userInfo.id);
            if (vpnConfig != null && (!vpnConfig.legacy || ((legacyVpnInfo = securityControllerImpl.mVpnManager.getLegacyVpnInfo(userInfo.id)) != null && legacyVpnInfo.state == 3))) {
                sparseArray.put(userInfo.id, vpnConfig);
            }
        }
        securityControllerImpl.mCurrentVpns = sparseArray;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.systemui.statusbar.policy.SecurityControllerImpl$1, android.net.ConnectivityManager$NetworkCallback] */
    /* JADX WARN: Type inference failed for: r1v0, types: [com.android.systemui.statusbar.policy.SecurityControllerImpl$2, android.content.BroadcastReceiver] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public SecurityControllerImpl(android.content.Context r5, android.os.Handler r6, com.android.systemui.broadcast.BroadcastDispatcher r7, java.util.concurrent.Executor r8, com.android.systemui.dump.DumpManager r9) {
        /*
            r4 = this;
            r4.<init>(r7)
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r4.mCallbacks = r0
            android.util.SparseArray r0 = new android.util.SparseArray
            r0.<init>()
            r4.mCurrentVpns = r0
            android.util.ArrayMap r0 = new android.util.ArrayMap
            r0.<init>()
            r4.mHasCACerts = r0
            com.android.systemui.statusbar.policy.SecurityControllerImpl$1 r0 = new com.android.systemui.statusbar.policy.SecurityControllerImpl$1
            r0.<init>()
            r4.mNetworkCallback = r0
            com.android.systemui.statusbar.policy.SecurityControllerImpl$2 r1 = new com.android.systemui.statusbar.policy.SecurityControllerImpl$2
            r1.<init>()
            r4.mBroadcastReceiver = r1
            r4.mContext = r5
            java.lang.String r2 = "device_policy"
            java.lang.Object r2 = r5.getSystemService(r2)
            android.app.admin.DevicePolicyManager r2 = (android.app.admin.DevicePolicyManager) r2
            r4.mDevicePolicyManager = r2
            java.lang.String r2 = "connectivity"
            java.lang.Object r2 = r5.getSystemService(r2)
            android.net.ConnectivityManager r2 = (android.net.ConnectivityManager) r2
            java.lang.Class<android.net.VpnManager> r3 = android.net.VpnManager.class
            java.lang.Object r3 = r5.getSystemService(r3)
            android.net.VpnManager r3 = (android.net.VpnManager) r3
            r4.mVpnManager = r3
            android.content.pm.PackageManager r3 = r5.getPackageManager()
            r4.mPackageManager = r3
            java.lang.String r3 = "user"
            java.lang.Object r5 = r5.getSystemService(r3)
            android.os.UserManager r5 = (android.os.UserManager) r5
            r4.mUserManager = r5
            r4.mBgExecutor = r8
            java.lang.String r5 = "SecurityControllerImpl"
            r9.registerDumpable(r5, r4)
            android.content.IntentFilter r5 = new android.content.IntentFilter
            r5.<init>()
            java.lang.String r8 = "android.security.action.TRUST_STORE_CHANGED"
            r5.addAction(r8)
            java.lang.String r8 = "android.intent.action.USER_UNLOCKED"
            r5.addAction(r8)
            android.os.UserHandle r8 = android.os.UserHandle.ALL
            r7.registerReceiverWithHandler(r1, r5, r6, r8)
            android.net.NetworkRequest r5 = com.android.systemui.statusbar.policy.SecurityControllerImpl.REQUEST
            r2.registerNetworkCallback(r5, r0)
            int r5 = android.app.ActivityManager.getCurrentUser()
            r4.onUserSwitched(r5)
            r4.startTracking()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.SecurityControllerImpl.<init>(android.content.Context, android.os.Handler, com.android.systemui.broadcast.BroadcastDispatcher, java.util.concurrent.Executor, com.android.systemui.dump.DumpManager):void");
    }
}
