package com.android.settingslib.applications;

import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.util.IconDrawableFactory;
import androidx.loader.app.LoaderManager;
/* loaded from: classes.dex */
public class DefaultAppInfo extends LoaderManager {
    public final ComponentName componentName;
    public final Context mContext;
    public final PackageManager mPm;
    public final int userId;

    public final Drawable loadIcon() {
        IconDrawableFactory newInstance = IconDrawableFactory.newInstance(this.mContext);
        if (this.componentName == null) {
            return null;
        }
        try {
            ComponentInfo componentInfo = getComponentInfo();
            ApplicationInfo applicationInfoAsUser = this.mPm.getApplicationInfoAsUser(this.componentName.getPackageName(), 0, this.userId);
            if (componentInfo != null) {
                return newInstance.getBadgedIcon(componentInfo, applicationInfoAsUser, this.userId);
            }
            return newInstance.getBadgedIcon(applicationInfoAsUser);
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public final CharSequence loadLabel() {
        if (this.componentName == null) {
            return null;
        }
        try {
            ComponentInfo componentInfo = getComponentInfo();
            if (componentInfo != null) {
                return componentInfo.loadLabel(this.mPm);
            }
            return this.mPm.getApplicationInfoAsUser(this.componentName.getPackageName(), 0, this.userId).loadLabel(this.mPm);
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public DefaultAppInfo(Context context, PackageManager packageManager, int i, ComponentName componentName) {
        this.mContext = context;
        this.mPm = packageManager;
        this.userId = i;
        this.componentName = componentName;
    }

    public final ComponentInfo getComponentInfo() {
        try {
            ActivityInfo activityInfo = AppGlobals.getPackageManager().getActivityInfo(this.componentName, 0L, this.userId);
            if (activityInfo == null) {
                return AppGlobals.getPackageManager().getServiceInfo(this.componentName, 0L, this.userId);
            }
            return activityInfo;
        } catch (RemoteException unused) {
            return null;
        }
    }
}
