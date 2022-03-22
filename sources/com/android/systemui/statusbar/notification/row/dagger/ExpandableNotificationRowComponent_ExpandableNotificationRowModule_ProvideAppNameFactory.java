package com.android.systemui.statusbar.notification.row.dagger;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideAppNameFactory implements Factory<String> {
    public final Provider<Context> contextProvider;
    public final Provider<StatusBarNotification> statusBarNotificationProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        StatusBarNotification statusBarNotification = this.statusBarNotificationProvider.mo144get();
        PackageManager packageManagerForUser = StatusBar.getPackageManagerForUser(this.contextProvider.mo144get(), statusBarNotification.getUser().getIdentifier());
        String packageName = statusBarNotification.getPackageName();
        try {
            ApplicationInfo applicationInfo = packageManagerForUser.getApplicationInfo(packageName, 8704);
            if (applicationInfo != null) {
                packageName = String.valueOf(packageManagerForUser.getApplicationLabel(applicationInfo));
            }
        } catch (PackageManager.NameNotFoundException unused) {
        }
        Objects.requireNonNull(packageName, "Cannot return null from a non-@Nullable @Provides method");
        return packageName;
    }

    public ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideAppNameFactory(Provider provider, ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory expandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory) {
        this.contextProvider = provider;
        this.statusBarNotificationProvider = expandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory;
    }
}
