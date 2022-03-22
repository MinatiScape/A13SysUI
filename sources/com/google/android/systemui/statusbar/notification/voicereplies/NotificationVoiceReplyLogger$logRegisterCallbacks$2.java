package com.google.android.systemui.statusbar.notification.voicereplies;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyLogger.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyLogger$logRegisterCallbacks$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationVoiceReplyLogger$logRegisterCallbacks$2 INSTANCE = new NotificationVoiceReplyLogger$logRegisterCallbacks$2();

    public NotificationVoiceReplyLogger$logRegisterCallbacks$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("BINDER: registerCallbacks [userId=");
        m.append(logMessage.getInt1());
        m.append(']');
        return m.toString();
    }
}
