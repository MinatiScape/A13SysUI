package com.android.systemui.statusbar.notification.logging;

import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationLogger_ExpansionStateLogger_Factory implements Factory<NotificationLogger.ExpansionStateLogger> {
    public final Provider<Executor> uiBgExecutorProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NotificationLogger.ExpansionStateLogger(this.uiBgExecutorProvider.mo144get());
    }

    public NotificationLogger_ExpansionStateLogger_Factory(Provider<Executor> provider) {
        this.uiBgExecutorProvider = provider;
    }
}
