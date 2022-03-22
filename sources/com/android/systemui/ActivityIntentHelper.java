package com.android.systemui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import java.util.List;
/* loaded from: classes.dex */
public final class ActivityIntentHelper {
    public final Context mContext;

    public static boolean wouldLaunchResolverActivity(ResolveInfo resolveInfo, List list) {
        for (int i = 0; i < list.size(); i++) {
            ResolveInfo resolveInfo2 = (ResolveInfo) list.get(i);
            if (resolveInfo2.activityInfo.name.equals(resolveInfo.activityInfo.name) && resolveInfo2.activityInfo.packageName.equals(resolveInfo.activityInfo.packageName)) {
                return false;
            }
        }
        return true;
    }

    public final ActivityInfo getTargetActivityInfo(Intent intent, int i, boolean z) {
        int i2;
        ResolveInfo resolveActivityAsUser;
        PackageManager packageManager = this.mContext.getPackageManager();
        if (!z) {
            i2 = 851968;
        } else {
            i2 = 65536;
        }
        List queryIntentActivitiesAsUser = packageManager.queryIntentActivitiesAsUser(intent, i2, i);
        if (queryIntentActivitiesAsUser.size() == 0 || (resolveActivityAsUser = packageManager.resolveActivityAsUser(intent, i2 | 128, i)) == null || wouldLaunchResolverActivity(resolveActivityAsUser, queryIntentActivitiesAsUser)) {
            return null;
        }
        return resolveActivityAsUser.activityInfo;
    }

    public ActivityIntentHelper(Context context) {
        this.mContext = context;
    }
}
