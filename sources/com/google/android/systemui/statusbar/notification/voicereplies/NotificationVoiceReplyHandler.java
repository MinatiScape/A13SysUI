package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlinx.coroutines.flow.StateFlow;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
public interface NotificationVoiceReplyHandler {
    StateFlow<CtaState> getShowCta();

    int getUserId();

    void onNotifAvailableForQuickPhraseReplyChanged(boolean z);

    void onNotifAvailableForReplyChanged(boolean z);
}
