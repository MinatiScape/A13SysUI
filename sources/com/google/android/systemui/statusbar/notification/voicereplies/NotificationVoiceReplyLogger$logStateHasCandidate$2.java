package com.google.android.systemui.statusbar.notification.voicereplies;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyLogger.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyLogger$logStateHasCandidate$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationVoiceReplyLogger$logStateHasCandidate$2 INSTANCE = new NotificationVoiceReplyLogger$logStateHasCandidate$2();

    public NotificationVoiceReplyLogger$logStateHasCandidate$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("NEW STATE: HasCandidate(notifKey=");
        m.append((Object) logMessage2.getStr1());
        m.append(", ctaVis=");
        m.append((Object) logMessage2.getStr2());
        m.append(", cta=");
        m.append((Object) logMessage2.getStr3());
        m.append(')');
        return m.toString();
    }
}
