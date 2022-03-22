package com.google.android.systemui.statusbar.notification.voicereplies;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyLogger.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyLogger$logRejectCandidate$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationVoiceReplyLogger$logRejectCandidate$2 INSTANCE = new NotificationVoiceReplyLogger$logRejectCandidate$2();

    public NotificationVoiceReplyLogger$logRejectCandidate$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("rejecting candidate [");
        m.append((Object) logMessage2.getStr1());
        m.append("]: ");
        m.append((Object) logMessage2.getStr2());
        return m.toString();
    }
}
