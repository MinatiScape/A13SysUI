package com.android.systemui.statusbar.notification;

import com.android.systemui.qs.logging.QSLogger_Factory;
import com.android.systemui.statusbar.notification.NotificationClicker;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationClicker_Builder_Factory implements Factory<NotificationClicker.Builder> {
    public final Provider<NotificationClickerLogger> loggerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NotificationClicker.Builder(this.loggerProvider.mo144get());
    }

    public NotificationClicker_Builder_Factory(QSLogger_Factory qSLogger_Factory) {
        this.loggerProvider = qSLogger_Factory;
    }
}
