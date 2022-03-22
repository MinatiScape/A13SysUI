package com.google.android.systemui.statusbar;

import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationVoiceReplyManagerService_Factory implements Factory<NotificationVoiceReplyManagerService> {
    public final Provider<NotificationVoiceReplyLogger> loggerProvider;
    public final Provider<NotificationVoiceReplyManager.Initializer> managerInitializerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NotificationVoiceReplyManagerService(this.managerInitializerProvider.mo144get(), this.loggerProvider.mo144get());
    }

    public NotificationVoiceReplyManagerService_Factory(Provider<NotificationVoiceReplyManager.Initializer> provider, Provider<NotificationVoiceReplyLogger> provider2) {
        this.managerInitializerProvider = provider;
        this.loggerProvider = provider2;
    }
}
