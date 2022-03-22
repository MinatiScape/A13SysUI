package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DebugNotificationVoiceReplyClient_Factory implements Factory<DebugNotificationVoiceReplyClient> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<NotificationLockscreenUserManager> lockscreenUserManagerProvider;
    public final Provider<NotificationVoiceReplyManager.Initializer> voiceReplyInitializerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DebugNotificationVoiceReplyClient(this.broadcastDispatcherProvider.mo144get(), this.lockscreenUserManagerProvider.mo144get(), this.voiceReplyInitializerProvider.mo144get());
    }

    public DebugNotificationVoiceReplyClient_Factory(Provider<BroadcastDispatcher> provider, Provider<NotificationLockscreenUserManager> provider2, Provider<NotificationVoiceReplyManager.Initializer> provider3) {
        this.broadcastDispatcherProvider = provider;
        this.lockscreenUserManagerProvider = provider2;
        this.voiceReplyInitializerProvider = provider3;
    }
}
