package com.google.android.systemui.statusbar.notification.voicereplies;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyLogger.kt */
/* loaded from: classes.dex */
final class NotificationVoiceReplyLogger$logUserIdMismatch$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationVoiceReplyLogger$logUserIdMismatch$2 INSTANCE = new NotificationVoiceReplyLogger$logUserIdMismatch$2();

    public NotificationVoiceReplyLogger$logUserIdMismatch$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("UserId mismatch, notifying handler of failure [handerId=");
        m.append(logMessage2.getInt1());
        m.append(", candidateId=");
        m.append(logMessage2.getInt2());
        m.append(']');
        return m.toString();
    }
}
