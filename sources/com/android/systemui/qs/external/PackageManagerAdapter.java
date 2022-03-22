package com.android.systemui.qs.external;

import android.app.AppGlobals;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
/* loaded from: classes.dex */
public final class PackageManagerAdapter {
    public IPackageManager mIPackageManager = AppGlobals.getPackageManager();
    public PackageManager mPackageManager;

    public PackageManagerAdapter(Context context) {
        this.mPackageManager = context.getPackageManager();
    }
}
