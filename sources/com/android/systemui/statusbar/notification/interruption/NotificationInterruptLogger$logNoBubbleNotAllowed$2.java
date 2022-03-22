package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationInterruptLogger.kt */
/* loaded from: classes.dex */
final class NotificationInterruptLogger$logNoBubbleNotAllowed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationInterruptLogger$logNoBubbleNotAllowed$2 INSTANCE = new NotificationInterruptLogger$logNoBubbleNotAllowed$2();

    public NotificationInterruptLogger$logNoBubbleNotAllowed$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("No bubble up: not allowed to bubble: ", logMessage.getStr1());
    }
}
