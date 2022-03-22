package com.google.android.systemui.statusbar;

import com.google.android.systemui.statusbar.notification.voicereplies.CtaState;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowImpl;
/* compiled from: NotificationVoiceReplyManagerService.kt */
/* loaded from: classes.dex */
public final class CallbacksHandler implements NotificationVoiceReplyHandler {
    public final INotificationVoiceReplyServiceCallbacks callbacks;
    public final StateFlow<CtaState> showCta;
    public final int userId;

    @Override // com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler
    public final void onNotifAvailableForQuickPhraseReplyChanged(boolean z) {
        this.callbacks.onNotifAvailableForQuickPhraseChanged(z);
    }

    @Override // com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler
    public final void onNotifAvailableForReplyChanged(boolean z) {
        this.callbacks.onNotifAvailableForReplyChanged(z);
    }

    public CallbacksHandler(int i, INotificationVoiceReplyServiceCallbacks iNotificationVoiceReplyServiceCallbacks, StateFlowImpl stateFlowImpl) {
        this.userId = i;
        this.callbacks = iNotificationVoiceReplyServiceCallbacks;
        this.showCta = stateFlowImpl;
    }

    @Override // com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler
    public final StateFlow<CtaState> getShowCta() {
        return this.showCta;
    }

    @Override // com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler
    public final int getUserId() {
        return this.userId;
    }
}
