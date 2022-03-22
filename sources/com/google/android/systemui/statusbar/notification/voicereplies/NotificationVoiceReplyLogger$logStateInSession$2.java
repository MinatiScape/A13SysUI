package com.google.android.systemui.statusbar.notification.voicereplies;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyLogger.kt */
/* loaded from: classes.dex */
final class NotificationVoiceReplyLogger$logStateInSession$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationVoiceReplyLogger$logStateInSession$2 INSTANCE = new NotificationVoiceReplyLogger$logStateInSession$2();

    public NotificationVoiceReplyLogger$logStateInSession$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("NEW STATE: InSession(notifKey=");
        m.append((Object) logMessage.getStr1());
        m.append(')');
        return m.toString();
    }
}
