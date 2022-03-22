package com.google.android.systemui.columbus;

import android.content.Context;
import android.content.pm.PackageManager;
/* compiled from: ColumbusContext.kt */
/* loaded from: classes.dex */
public final class ColumbusContext {
    public final PackageManager packageManager;

    public ColumbusContext(Context context) {
        this.packageManager = context.getPackageManager();
    }
}
