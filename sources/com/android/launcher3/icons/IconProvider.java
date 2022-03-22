package com.android.launcher3.icons;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import com.android.systemui.theme.ThemeOverlayApplier;
/* loaded from: classes.dex */
public final class IconProvider {
    public static final boolean ATLEAST_T;
    public final ComponentName mCalendar;
    public final ComponentName mClock;
    public final Context mContext;

    public final Drawable getIcon(ActivityInfo activityInfo) {
        return getIcon(activityInfo, this.mContext.getResources().getConfiguration().densityDpi);
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00aa  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.graphics.drawable.Drawable getIcon(android.content.pm.ActivityInfo r7, final int r8) {
        /*
            r6 = this;
            android.content.pm.ApplicationInfo r0 = r7.applicationInfo
            java.lang.String r0 = r0.packageName
            android.content.ComponentName r1 = r6.mCalendar
            r2 = 8320(0x2080, float:1.1659E-41)
            r3 = 0
            if (r1 == 0) goto L_0x006f
            java.lang.String r1 = r1.getPackageName()
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x006f
            android.content.Context r0 = r6.mContext
            android.content.pm.PackageManager r0 = r0.getPackageManager()
            android.content.ComponentName r1 = r6.mCalendar     // Catch: NameNotFoundException -> 0x00a7
            android.content.pm.ActivityInfo r1 = r0.getActivityInfo(r1, r2)     // Catch: NameNotFoundException -> 0x00a7
            android.os.Bundle r1 = r1.metaData     // Catch: NameNotFoundException -> 0x00a7
            android.content.ComponentName r2 = r6.mCalendar     // Catch: NameNotFoundException -> 0x00a7
            java.lang.String r2 = r2.getPackageName()     // Catch: NameNotFoundException -> 0x00a7
            android.content.res.Resources r0 = r0.getResourcesForApplication(r2)     // Catch: NameNotFoundException -> 0x00a7
            r2 = 0
            if (r1 != 0) goto L_0x0031
            goto L_0x0062
        L_0x0031:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: NameNotFoundException -> 0x00a7
            r4.<init>()     // Catch: NameNotFoundException -> 0x00a7
            android.content.ComponentName r5 = r6.mCalendar     // Catch: NameNotFoundException -> 0x00a7
            java.lang.String r5 = r5.getPackageName()     // Catch: NameNotFoundException -> 0x00a7
            r4.append(r5)     // Catch: NameNotFoundException -> 0x00a7
            java.lang.String r5 = ".dynamic_icons"
            r4.append(r5)     // Catch: NameNotFoundException -> 0x00a7
            java.lang.String r4 = r4.toString()     // Catch: NameNotFoundException -> 0x00a7
            int r1 = r1.getInt(r4, r2)     // Catch: NameNotFoundException -> 0x00a7
            if (r1 != 0) goto L_0x004f
            goto L_0x0062
        L_0x004f:
            android.content.res.TypedArray r1 = r0.obtainTypedArray(r1)     // Catch: NotFoundException -> 0x0062, NameNotFoundException -> 0x00a7
            java.util.Calendar r4 = java.util.Calendar.getInstance()     // Catch: NotFoundException -> 0x0062, NameNotFoundException -> 0x00a7
            r5 = 5
            int r4 = r4.get(r5)     // Catch: NotFoundException -> 0x0062, NameNotFoundException -> 0x00a7
            int r4 = r4 + (-1)
            int r2 = r1.getResourceId(r4, r2)     // Catch: NotFoundException -> 0x0062, NameNotFoundException -> 0x00a7
        L_0x0062:
            if (r2 == 0) goto L_0x00a7
            android.graphics.drawable.Drawable r0 = r0.getDrawableForDensity(r2, r8, r3)     // Catch: NameNotFoundException -> 0x00a7
            boolean r1 = com.android.launcher3.icons.IconProvider.ATLEAST_T     // Catch: NameNotFoundException -> 0x00a7
            if (r1 == 0) goto L_0x00a7
            boolean r0 = r0 instanceof android.graphics.drawable.AdaptiveIconDrawable     // Catch: NameNotFoundException -> 0x00a7
            goto L_0x00a7
        L_0x006f:
            android.content.ComponentName r1 = r6.mClock
            if (r1 == 0) goto L_0x00a7
            java.lang.String r1 = r1.getPackageName()
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x00a7
            android.content.Context r0 = r6.mContext
            android.content.ComponentName r1 = r6.mClock
            java.lang.String r1 = r1.getPackageName()
            int r4 = com.android.launcher3.icons.ClockDrawableWrapper.$r8$clinit
            android.content.pm.PackageManager r0 = r0.getPackageManager()     // Catch: Exception -> 0x009f
            android.content.pm.ApplicationInfo r1 = r0.getApplicationInfo(r1, r2)     // Catch: Exception -> 0x009f
            android.content.res.Resources r0 = r0.getResourcesForApplication(r1)     // Catch: Exception -> 0x009f
            android.os.Bundle r1 = r1.metaData     // Catch: Exception -> 0x009f
            com.android.launcher3.icons.ClockDrawableWrapper$$ExternalSyntheticLambda0 r2 = new com.android.launcher3.icons.ClockDrawableWrapper$$ExternalSyntheticLambda0     // Catch: Exception -> 0x009f
            r2.<init>()     // Catch: Exception -> 0x009f
            com.android.launcher3.icons.ClockDrawableWrapper r0 = com.android.launcher3.icons.ClockDrawableWrapper.forExtras(r1, r2)     // Catch: Exception -> 0x009f
            goto L_0x00a8
        L_0x009f:
            r0 = move-exception
            java.lang.String r1 = "ClockDrawableWrapper"
            java.lang.String r2 = "Unable to load clock drawable info"
            android.util.Log.d(r1, r2, r0)
        L_0x00a7:
            r0 = r3
        L_0x00a8:
            if (r0 != 0) goto L_0x00da
            java.util.Objects.requireNonNull(r6)
            int r0 = r7.getIconResource()
            if (r8 == 0) goto L_0x00c5
            if (r0 == 0) goto L_0x00c5
            android.content.Context r1 = r6.mContext     // Catch: NameNotFoundException | NotFoundException -> 0x00c5
            android.content.pm.PackageManager r1 = r1.getPackageManager()     // Catch: NameNotFoundException | NotFoundException -> 0x00c5
            android.content.pm.ApplicationInfo r2 = r7.applicationInfo     // Catch: NameNotFoundException | NotFoundException -> 0x00c5
            android.content.res.Resources r1 = r1.getResourcesForApplication(r2)     // Catch: NameNotFoundException | NotFoundException -> 0x00c5
            android.graphics.drawable.Drawable r3 = r1.getDrawableForDensity(r0, r8)     // Catch: NameNotFoundException | NotFoundException -> 0x00c5
        L_0x00c5:
            if (r3 != 0) goto L_0x00d3
            android.content.Context r6 = r6.mContext
            android.content.pm.PackageManager r6 = r6.getPackageManager()
            android.graphics.drawable.Drawable r6 = r7.loadIcon(r6)
            r0 = r6
            goto L_0x00d4
        L_0x00d3:
            r0 = r3
        L_0x00d4:
            boolean r6 = com.android.launcher3.icons.IconProvider.ATLEAST_T
            if (r6 == 0) goto L_0x00da
            boolean r6 = r0 instanceof android.graphics.drawable.AdaptiveIconDrawable
        L_0x00da:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.launcher3.icons.IconProvider.getIcon(android.content.pm.ActivityInfo, int):android.graphics.drawable.Drawable");
    }

    static {
        Resources.getSystem().getIdentifier("config_icon_mask", "string", ThemeOverlayApplier.ANDROID_PACKAGE);
        String str = Build.VERSION.CODENAME;
        boolean z = false;
        if (!"REL".equals(str) && str.compareTo("T") >= 0) {
            z = true;
        }
        ATLEAST_T = z;
    }

    public IconProvider(Context context) {
        ComponentName componentName;
        this.mContext = context;
        String string = context.getString(2131952086);
        ComponentName componentName2 = null;
        if (TextUtils.isEmpty(string)) {
            componentName = null;
        } else {
            componentName = ComponentName.unflattenFromString(string);
        }
        this.mCalendar = componentName;
        String string2 = context.getString(2131952121);
        this.mClock = !TextUtils.isEmpty(string2) ? ComponentName.unflattenFromString(string2) : componentName2;
    }
}
