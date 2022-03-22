package com.android.systemui.statusbar.tv.notifications;

import android.content.Context;
import com.android.systemui.statusbar.NotificationListener;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TvNotificationHandler_Factory implements Factory<TvNotificationHandler> {
    public final Provider<Context> contextProvider;
    public final Provider<NotificationListener> notificationListenerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new TvNotificationHandler(this.contextProvider.mo144get(), this.notificationListenerProvider.mo144get());
    }

    public TvNotificationHandler_Factory(Provider<Context> provider, Provider<NotificationListener> provider2) {
        this.contextProvider = provider;
        this.notificationListenerProvider = provider2;
    }
}
