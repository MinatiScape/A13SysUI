package com.google.android.setupcompat.partnerconfig;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
/* loaded from: classes.dex */
public final class ResourceEntry {
    public static final String KEY_FALLBACK_CONFIG = "fallbackConfig";
    public static final String KEY_PACKAGE_NAME = "packageName";
    public static final String KEY_RESOURCE_ID = "resourceId";
    public static final String KEY_RESOURCE_NAME = "resourceName";
    public final int resourceId;
    public final Resources resources;

    public static ResourceEntry fromBundle(Context context, Bundle bundle) {
        if (!bundle.containsKey(KEY_PACKAGE_NAME) || !bundle.containsKey(KEY_RESOURCE_NAME) || !bundle.containsKey(KEY_RESOURCE_ID)) {
            return null;
        }
        String string = bundle.getString(KEY_PACKAGE_NAME);
        String string2 = bundle.getString(KEY_RESOURCE_NAME);
        int i = bundle.getInt(KEY_RESOURCE_ID);
        try {
            PackageManager packageManager = context.getPackageManager();
            return new ResourceEntry(i, packageManager.getResourcesForApplication(packageManager.getApplicationInfo(string, 512)));
        } catch (PackageManager.NameNotFoundException unused) {
            Bundle bundle2 = bundle.getBundle("fallbackConfig");
            if (bundle2 == null) {
                return null;
            }
            Log.w("ResourceEntry", string + " not found, " + string2 + " fallback to default value");
            return fromBundle(context, bundle2);
        }
    }

    public ResourceEntry(int i, Resources resources) {
        this.resourceId = i;
        this.resources = resources;
    }
}
