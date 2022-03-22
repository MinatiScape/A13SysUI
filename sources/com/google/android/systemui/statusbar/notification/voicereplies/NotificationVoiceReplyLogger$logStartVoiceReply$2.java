package com.google.android.systemui.statusbar.notification.voicereplies;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyLogger.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyLogger$logStartVoiceReply$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationVoiceReplyLogger$logStartVoiceReply$2 INSTANCE = new NotificationVoiceReplyLogger$logStartVoiceReply$2();

    public NotificationVoiceReplyLogger$logStartVoiceReply$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("BINDER: startVoiceReply [userId=");
        m.append(logMessage2.getInt1());
        m.append(", sessionToken=");
        m.append(logMessage2.getInt2());
        m.append(", hasContent=");
        m.append(logMessage2.getBool1());
        m.append(']');
        return m.toString();
    }
}
