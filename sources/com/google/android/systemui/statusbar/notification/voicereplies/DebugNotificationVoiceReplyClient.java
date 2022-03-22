package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManager;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.ExecutorCoroutineDispatcher;
import kotlinx.coroutines.GlobalScope;
import kotlinx.coroutines.internal.MainDispatcherLoader;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
public final class DebugNotificationVoiceReplyClient implements NotificationVoiceReplyClient {
    public final BroadcastDispatcher broadcastDispatcher;
    public final NotificationLockscreenUserManager lockscreenUserManager;
    public final NotificationVoiceReplyManager.Initializer voiceReplyInitializer;

    @Override // com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyClient
    public final SafeSubscription startClient() {
        GlobalScope globalScope = GlobalScope.INSTANCE;
        ExecutorCoroutineDispatcher executorCoroutineDispatcher = Dispatchers.Default;
        return new SafeSubscription(new DebugNotificationVoiceReplyClient$startClient$1(BuildersKt.launch$default(globalScope, MainDispatcherLoader.dispatcher, null, new DebugNotificationVoiceReplyClient$startClient$job$1(this, null), 2)));
    }

    public DebugNotificationVoiceReplyClient(BroadcastDispatcher broadcastDispatcher, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationVoiceReplyManager.Initializer initializer) {
        this.broadcastDispatcher = broadcastDispatcher;
        this.lockscreenUserManager = notificationLockscreenUserManager;
        this.voiceReplyInitializer = initializer;
    }
}
