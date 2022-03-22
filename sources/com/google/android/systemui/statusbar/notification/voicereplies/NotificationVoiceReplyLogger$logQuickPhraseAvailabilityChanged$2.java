package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyLogger.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyLogger$logQuickPhraseAvailabilityChanged$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationVoiceReplyLogger$logQuickPhraseAvailabilityChanged$2 INSTANCE = new NotificationVoiceReplyLogger$logQuickPhraseAvailabilityChanged$2();

    public NotificationVoiceReplyLogger$logQuickPhraseAvailabilityChanged$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        return ((Object) logMessage2.getStr1()) + " quick phrase for userId=" + logMessage2.getInt1();
    }
}
