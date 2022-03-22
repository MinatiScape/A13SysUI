package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyLogger.kt */
/* loaded from: classes.dex */
final class NotificationVoiceReplyLogger$logRemovalDropped$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationVoiceReplyLogger$logRemovalDropped$2 INSTANCE = new NotificationVoiceReplyLogger$logRemovalDropped$2();

    public NotificationVoiceReplyLogger$logRemovalDropped$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("dropping STATE UPDATE: Removal - key=", logMessage.getStr1());
    }
}
