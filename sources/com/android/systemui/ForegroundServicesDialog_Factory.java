package com.android.systemui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.hdmi.HdmiCecSetMenuLanguageActivity;
import com.android.systemui.hdmi.HdmiCecSetMenuLanguageHelper;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationPresenterExtensions;
import com.android.systemui.util.concurrency.ExecutorImpl;
import com.android.wm.shell.ShellCommandHandlerImpl;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ForegroundServicesDialog_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider metricsLoggerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ForegroundServicesDialog((MetricsLogger) this.metricsLoggerProvider.mo144get());
            case 1:
                Resources resources = ((Context) this.metricsLoggerProvider.mo144get()).getResources();
                Objects.requireNonNull(resources, "Cannot return null from a non-@Nullable @Provides method");
                return resources;
            case 2:
                return new HdmiCecSetMenuLanguageActivity((HdmiCecSetMenuLanguageHelper) this.metricsLoggerProvider.mo144get());
            case 3:
                return mo144get();
            case 4:
                return new LegacyNotificationPresenterExtensions((NotificationEntryManager) this.metricsLoggerProvider.mo144get());
            case 5:
                return new ExecutorImpl((Looper) this.metricsLoggerProvider.mo144get());
            case FalsingManager.VERSION /* 6 */:
                ShellCommandHandlerImpl shellCommandHandlerImpl = (ShellCommandHandlerImpl) this.metricsLoggerProvider.mo144get();
                Objects.requireNonNull(shellCommandHandlerImpl);
                Optional of = Optional.of(shellCommandHandlerImpl.mImpl);
                Objects.requireNonNull(of, "Cannot return null from a non-@Nullable @Provides method");
                return of;
            case 7:
                NotificationManager notificationManager = (NotificationManager) ((Context) this.metricsLoggerProvider.mo144get()).getSystemService(NotificationManager.class);
                Objects.requireNonNull(notificationManager, "Cannot return null from a non-@Nullable @Provides method");
                return notificationManager;
            default:
                return mo144get();
        }
    }

    public /* synthetic */ ForegroundServicesDialog_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.metricsLoggerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final LogBuffer mo144get() {
        switch (this.$r8$classId) {
            case 3:
                return ((LogBufferFactory) this.metricsLoggerProvider.mo144get()).create("BroadcastDispatcherLog", 500, 10, false);
            default:
                return ((LogBufferFactory) this.metricsLoggerProvider.mo144get()).create("NotifVoiceReplyLog", 500);
        }
    }
}
