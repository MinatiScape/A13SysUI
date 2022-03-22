package com.google.android.systemui.statusbar.notification.voicereplies;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyLogger.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyLogger$logVoiceAuthStateChanged$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationVoiceReplyLogger$logVoiceAuthStateChanged$2 INSTANCE = new NotificationVoiceReplyLogger$logVoiceAuthStateChanged$2();

    public NotificationVoiceReplyLogger$logVoiceAuthStateChanged$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("BINDER: onVoiceAuthStateChanged [userId=");
        m.append(logMessage2.getInt1());
        m.append(", sessionToken=");
        m.append(logMessage2.getInt2());
        m.append(", newState=");
        m.append(logMessage2.getBool1());
        m.append(']');
        return m.toString();
    }
}
