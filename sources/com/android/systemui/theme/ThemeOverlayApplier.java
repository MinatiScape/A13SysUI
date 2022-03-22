package com.android.systemui.theme;

import android.content.om.OverlayIdentifier;
import android.content.om.OverlayInfo;
import android.content.om.OverlayManager;
import android.content.om.OverlayManagerTransaction;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.google.android.collect.Lists;
import com.google.android.collect.Sets;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class ThemeOverlayApplier implements Dumpable {
    public static final String ANDROID_PACKAGE = "android";
    public static final String SETTINGS_PACKAGE = "com.android.settings";
    public static final String SYSUI_PACKAGE = "com.android.systemui";
    public final Executor mBgExecutor;
    public final ArrayMap mCategoryToTargetPackage;
    public final String mLauncherPackage;
    public final Executor mMainExecutor;
    public final OverlayManager mOverlayManager;
    public final ArrayMap mTargetPackageToCategories;
    public final String mThemePickerPackage;
    public static final boolean DEBUG = Log.isLoggable("ThemeOverlayApplier", 3);
    public static final String OVERLAY_CATEGORY_ICON_LAUNCHER = "android.theme.customization.icon_pack.launcher";
    public static final String OVERLAY_CATEGORY_SHAPE = "android.theme.customization.adaptive_icon_shape";
    public static final String OVERLAY_CATEGORY_FONT = "android.theme.customization.font";
    public static final String OVERLAY_CATEGORY_ICON_ANDROID = "android.theme.customization.icon_pack.android";
    public static final String OVERLAY_CATEGORY_ICON_SYSUI = "android.theme.customization.icon_pack.systemui";
    public static final String OVERLAY_CATEGORY_ICON_SETTINGS = "android.theme.customization.icon_pack.settings";
    public static final String OVERLAY_CATEGORY_ICON_THEME_PICKER = "android.theme.customization.icon_pack.themepicker";
    public static final ArrayList THEME_CATEGORIES = Lists.newArrayList(new String[]{"android.theme.customization.system_palette", OVERLAY_CATEGORY_ICON_LAUNCHER, OVERLAY_CATEGORY_SHAPE, OVERLAY_CATEGORY_FONT, "android.theme.customization.accent_color", OVERLAY_CATEGORY_ICON_ANDROID, OVERLAY_CATEGORY_ICON_SYSUI, OVERLAY_CATEGORY_ICON_SETTINGS, OVERLAY_CATEGORY_ICON_THEME_PICKER});
    public static final Set<String> SYSTEM_USER_CATEGORIES = Sets.newHashSet(new String[]{"android.theme.customization.system_palette", "android.theme.customization.accent_color", OVERLAY_CATEGORY_FONT, OVERLAY_CATEGORY_SHAPE, OVERLAY_CATEGORY_ICON_ANDROID, OVERLAY_CATEGORY_ICON_SYSUI});

    public ThemeOverlayApplier(OverlayManager overlayManager, Executor executor, Executor executor2, String str, String str2, DumpManager dumpManager) {
        ArrayMap arrayMap = new ArrayMap();
        this.mTargetPackageToCategories = arrayMap;
        ArrayMap arrayMap2 = new ArrayMap();
        this.mCategoryToTargetPackage = arrayMap2;
        this.mOverlayManager = overlayManager;
        this.mBgExecutor = executor;
        this.mMainExecutor = executor2;
        this.mLauncherPackage = str;
        this.mThemePickerPackage = str2;
        arrayMap.put(ANDROID_PACKAGE, Sets.newHashSet(new String[]{"android.theme.customization.system_palette", "android.theme.customization.accent_color", OVERLAY_CATEGORY_FONT, OVERLAY_CATEGORY_SHAPE, OVERLAY_CATEGORY_ICON_ANDROID}));
        arrayMap.put(SYSUI_PACKAGE, Sets.newHashSet(new String[]{OVERLAY_CATEGORY_ICON_SYSUI}));
        arrayMap.put(SETTINGS_PACKAGE, Sets.newHashSet(new String[]{OVERLAY_CATEGORY_ICON_SETTINGS}));
        arrayMap.put(str, Sets.newHashSet(new String[]{OVERLAY_CATEGORY_ICON_LAUNCHER}));
        arrayMap.put(str2, Sets.newHashSet(new String[]{OVERLAY_CATEGORY_ICON_THEME_PICKER}));
        arrayMap2.put("android.theme.customization.accent_color", ANDROID_PACKAGE);
        arrayMap2.put(OVERLAY_CATEGORY_FONT, ANDROID_PACKAGE);
        arrayMap2.put(OVERLAY_CATEGORY_SHAPE, ANDROID_PACKAGE);
        arrayMap2.put(OVERLAY_CATEGORY_ICON_ANDROID, ANDROID_PACKAGE);
        arrayMap2.put(OVERLAY_CATEGORY_ICON_SYSUI, SYSUI_PACKAGE);
        arrayMap2.put(OVERLAY_CATEGORY_ICON_SETTINGS, SETTINGS_PACKAGE);
        arrayMap2.put(OVERLAY_CATEGORY_ICON_LAUNCHER, str);
        arrayMap2.put(OVERLAY_CATEGORY_ICON_THEME_PICKER, str2);
        dumpManager.registerDumpable("ThemeOverlayApplier", this);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("mTargetPackageToCategories=");
        m.append(this.mTargetPackageToCategories);
        printWriter.println(m.toString());
        printWriter.println("mCategoryToTargetPackage=" + this.mCategoryToTargetPackage);
    }

    public OverlayManagerTransaction.Builder getTransactionBuilder() {
        return new OverlayManagerTransaction.Builder();
    }

    public final void setEnabled(OverlayManagerTransaction.Builder builder, OverlayIdentifier overlayIdentifier, String str, int i, Set<UserHandle> set, boolean z, boolean z2) {
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("setEnabled: ");
            m.append(overlayIdentifier.getPackageName());
            m.append(" category: ");
            m.append(str);
            m.append(": ");
            KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0.m(m, z, "ThemeOverlayApplier");
        }
        if (this.mOverlayManager.getOverlayInfo(overlayIdentifier, UserHandle.of(i)) != null || z2) {
            builder.setEnabled(overlayIdentifier, z, i);
            if (i != UserHandle.SYSTEM.getIdentifier() && SYSTEM_USER_CATEGORIES.contains(str)) {
                builder.setEnabled(overlayIdentifier, z, UserHandle.SYSTEM.getIdentifier());
            }
            OverlayInfo overlayInfo = this.mOverlayManager.getOverlayInfo(overlayIdentifier, UserHandle.SYSTEM);
            if (!(overlayInfo == null || overlayInfo.targetPackageName.equals(this.mLauncherPackage) || overlayInfo.targetPackageName.equals(this.mThemePickerPackage))) {
                for (UserHandle userHandle : set) {
                    builder.setEnabled(overlayIdentifier, z, userHandle.getIdentifier());
                }
                return;
            }
            return;
        }
        Log.i("ThemeOverlayApplier", "Won't enable " + overlayIdentifier + ", it doesn't exist for user" + i);
    }
}
