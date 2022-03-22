package com.android.wifitrackerlib;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.UserManager;
import android.util.ArraySet;
/* loaded from: classes.dex */
public final class WifiTrackerInjector {
    public final DevicePolicyManager mDevicePolicyManager;
    public final ArraySet mNoAttributionAnnotationPackages = new ArraySet();
    public final UserManager mUserManager;

    public WifiTrackerInjector(Context context) {
        UserManager.isDeviceInDemoMode(context);
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        this.mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        for (String str : context.getString(2131953619).split(",")) {
            this.mNoAttributionAnnotationPackages.add(str);
        }
    }
}
