package com.google.android.systemui.statusbar.notification.voicereplies;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyLogger.kt */
/* loaded from: classes.dex */
final class NotificationVoiceReplyLogger$logStartSessionNoCandidate$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationVoiceReplyLogger$logStartSessionNoCandidate$2 INSTANCE = new NotificationVoiceReplyLogger$logStartSessionNoCandidate$2();

    public NotificationVoiceReplyLogger$logStartSessionNoCandidate$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Can't start session with no candidate, notifying handler of failure [handlerId=");
        m.append(logMessage.getInt1());
        m.append(']');
        return m.toString();
    }
}
