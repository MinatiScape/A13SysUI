package com.google.android.systemui.statusbar.notification.voicereplies;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyLogger.kt */
/* loaded from: classes.dex */
final class NotificationVoiceReplyLogger$logSessionAlreadyInProgress$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationVoiceReplyLogger$logSessionAlreadyInProgress$2 INSTANCE = new NotificationVoiceReplyLogger$logSessionAlreadyInProgress$2();

    public NotificationVoiceReplyLogger$logSessionAlreadyInProgress$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Session already in progress, notifying handler of failure [handlerId=");
        m.append(logMessage.getInt1());
        m.append(']');
        return m.toString();
    }
}
