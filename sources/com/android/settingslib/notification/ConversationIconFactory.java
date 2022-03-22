package com.android.settingslib.notification;

import android.content.Context;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import com.android.launcher3.icons.BaseIconFactory;
/* loaded from: classes.dex */
public final class ConversationIconFactory extends BaseIconFactory {
    public final LauncherApps mLauncherApps;
    public final PackageManager mPackageManager;

    static {
        Math.sqrt(288.0d);
    }

    public ConversationIconFactory(Context context, LauncherApps launcherApps, PackageManager packageManager, int i) {
        super(context, context.getResources().getConfiguration().densityDpi, i, false);
        this.mLauncherApps = launcherApps;
        this.mPackageManager = packageManager;
        context.getResources().getColor(2131099884, null);
    }
}
