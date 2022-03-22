package com.android.systemui.statusbar.notification.row.dagger;

import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import dagger.internal.Factory;
import dagger.internal.InstanceFactory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory implements Factory<StatusBarNotification> {
    public final Provider<NotificationEntry> notificationEntryProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        NotificationEntry notificationEntry = this.notificationEntryProvider.mo144get();
        Objects.requireNonNull(notificationEntry);
        StatusBarNotification statusBarNotification = notificationEntry.mSbn;
        Objects.requireNonNull(statusBarNotification, "Cannot return null from a non-@Nullable @Provides method");
        return statusBarNotification;
    }

    public ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory(InstanceFactory instanceFactory) {
        this.notificationEntryProvider = instanceFactory;
    }
}
