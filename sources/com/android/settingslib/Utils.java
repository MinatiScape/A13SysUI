package com.android.settingslib;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.Signature;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.NetworkCapabilities;
import android.net.vcn.VcnTransportInfo;
import android.net.wifi.WifiInfo;
import android.os.UserHandle;
import com.android.internal.annotations.VisibleForTesting;
import com.android.launcher3.icons.BaseIconFactory;
import com.android.launcher3.icons.FastBitmapDrawable;
import com.android.launcher3.icons.IconFactory;
/* loaded from: classes.dex */
public final class Utils {
    @VisibleForTesting
    public static final String STORAGE_MANAGER_ENABLED_PROPERTY = "ro.storage_manager.enabled";
    public static String sPermissionControllerPackageName;
    public static String sServicesSystemSharedLibPackageName;
    public static String sSharedSystemSharedLibPackageName;
    public static Signature[] sSystemSignature;

    public static ColorStateList getColorAttr(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        try {
            return obtainStyledAttributes.getColorStateList(0);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public static int getColorAttrDefaultColor(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        return color;
    }

    public static int getThemeAttr(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        obtainStyledAttributes.recycle();
        return resourceId;
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0057, code lost:
        if (r7.equals(r1) == false) goto L_0x0059;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean isSystemPackage(android.content.res.Resources r6, android.content.pm.PackageManager r7, android.content.pm.PackageInfo r8) {
        /*
            android.content.pm.Signature[] r0 = com.android.settingslib.Utils.sSystemSignature
            r1 = 0
            r2 = 0
            r3 = 1
            if (r0 != 0) goto L_0x0022
            android.content.pm.Signature[] r0 = new android.content.pm.Signature[r3]
            java.lang.String r4 = "android"
            r5 = 64
            android.content.pm.PackageInfo r4 = r7.getPackageInfo(r4, r5)     // Catch: NameNotFoundException -> 0x001d
            if (r4 == 0) goto L_0x001d
            android.content.pm.Signature[] r4 = r4.signatures     // Catch: NameNotFoundException -> 0x001d
            if (r4 == 0) goto L_0x001d
            int r5 = r4.length     // Catch: NameNotFoundException -> 0x001d
            if (r5 <= 0) goto L_0x001d
            r4 = r4[r2]     // Catch: NameNotFoundException -> 0x001d
            goto L_0x001e
        L_0x001d:
            r4 = r1
        L_0x001e:
            r0[r2] = r4
            com.android.settingslib.Utils.sSystemSignature = r0
        L_0x0022:
            java.lang.String r0 = com.android.settingslib.Utils.sPermissionControllerPackageName
            if (r0 != 0) goto L_0x002c
            java.lang.String r0 = r7.getPermissionControllerPackageName()
            com.android.settingslib.Utils.sPermissionControllerPackageName = r0
        L_0x002c:
            java.lang.String r0 = com.android.settingslib.Utils.sServicesSystemSharedLibPackageName
            if (r0 != 0) goto L_0x0036
            java.lang.String r0 = r7.getServicesSystemSharedLibraryPackageName()
            com.android.settingslib.Utils.sServicesSystemSharedLibPackageName = r0
        L_0x0036:
            java.lang.String r0 = com.android.settingslib.Utils.sSharedSystemSharedLibPackageName
            if (r0 != 0) goto L_0x0040
            java.lang.String r7 = r7.getSharedSystemSharedLibraryPackageName()
            com.android.settingslib.Utils.sSharedSystemSharedLibPackageName = r7
        L_0x0040:
            android.content.pm.Signature[] r7 = com.android.settingslib.Utils.sSystemSignature
            r0 = r7[r2]
            if (r0 == 0) goto L_0x0059
            r7 = r7[r2]
            if (r8 == 0) goto L_0x0053
            android.content.pm.Signature[] r0 = r8.signatures
            if (r0 == 0) goto L_0x0053
            int r4 = r0.length
            if (r4 <= 0) goto L_0x0053
            r1 = r0[r2]
        L_0x0053:
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x0097
        L_0x0059:
            java.lang.String r7 = r8.packageName
            java.lang.String r0 = com.android.settingslib.Utils.sPermissionControllerPackageName
            boolean r7 = r7.equals(r0)
            if (r7 != 0) goto L_0x0097
            java.lang.String r7 = r8.packageName
            java.lang.String r0 = com.android.settingslib.Utils.sServicesSystemSharedLibPackageName
            boolean r7 = r7.equals(r0)
            if (r7 != 0) goto L_0x0097
            java.lang.String r7 = r8.packageName
            java.lang.String r0 = com.android.settingslib.Utils.sSharedSystemSharedLibPackageName
            boolean r7 = r7.equals(r0)
            if (r7 != 0) goto L_0x0097
            java.lang.String r7 = r8.packageName
            java.lang.String r0 = "com.android.printspooler"
            boolean r7 = r7.equals(r0)
            if (r7 != 0) goto L_0x0097
            java.lang.String r7 = r8.packageName
            r8 = 17039940(0x1040244, float:2.4246196E-38)
            java.lang.String r6 = r6.getString(r8)
            if (r6 == 0) goto L_0x0094
            boolean r6 = r6.equals(r7)
            if (r6 == 0) goto L_0x0094
            r6 = r3
            goto L_0x0095
        L_0x0094:
            r6 = r2
        L_0x0095:
            if (r6 == 0) goto L_0x0098
        L_0x0097:
            r2 = r3
        L_0x0098:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.Utils.isSystemPackage(android.content.res.Resources, android.content.pm.PackageManager, android.content.pm.PackageInfo):boolean");
    }

    public static int applyAlpha(int i) {
        return Color.argb((int) (0.3f * Color.alpha(i)), Color.red(i), Color.green(i), Color.blue(i));
    }

    public static FastBitmapDrawable getBadgedIcon(Context context, ApplicationInfo applicationInfo) {
        IconFactory iconFactory;
        Drawable loadUnbadgedIcon = applicationInfo.loadUnbadgedIcon(context.getPackageManager());
        UserHandle userHandleForUid = UserHandle.getUserHandleForUid(applicationInfo.uid);
        synchronized (IconFactory.sPoolSync) {
            iconFactory = IconFactory.sPool;
            if (iconFactory != null) {
                IconFactory.sPool = iconFactory.next;
                iconFactory.next = null;
            } else {
                iconFactory = new IconFactory(context, context.getResources().getConfiguration().densityDpi, context.getResources().getDimensionPixelSize(2131165605));
            }
        }
        try {
            BaseIconFactory.IconOptions iconOptions = new BaseIconFactory.IconOptions();
            iconOptions.mUserHandle = userHandleForUid;
            FastBitmapDrawable newIcon = iconFactory.createBadgedIconBitmap(loadUnbadgedIcon, iconOptions).newIcon(context);
            iconFactory.close();
            return newIcon;
        } catch (Throwable th) {
            try {
                iconFactory.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public static int getColorStateListDefaultColor(Context context, int i) {
        return context.getResources().getColorStateList(i, context.getTheme()).getDefaultColor();
    }

    public static WifiInfo tryGetWifiInfoForVcn(NetworkCapabilities networkCapabilities) {
        if (networkCapabilities.getTransportInfo() == null || !(networkCapabilities.getTransportInfo() instanceof VcnTransportInfo)) {
            return null;
        }
        return networkCapabilities.getTransportInfo().getWifiInfo();
    }
}
