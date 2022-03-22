package com.android.systemui.statusbar.notification.row.dagger;

import android.service.notification.StatusBarNotification;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideNotificationKeyFactory implements Factory<String> {
    public final Provider<StatusBarNotification> statusBarNotificationProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        String key = this.statusBarNotificationProvider.mo144get().getKey();
        Objects.requireNonNull(key, "Cannot return null from a non-@Nullable @Provides method");
        return key;
    }

    public ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideNotificationKeyFactory(ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory expandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory) {
        this.statusBarNotificationProvider = expandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory;
    }
}
