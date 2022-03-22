package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationInterruptLogger.kt */
/* loaded from: classes.dex */
final class NotificationInterruptLogger$logNoAlertingGroupAlertBehavior$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationInterruptLogger$logNoAlertingGroupAlertBehavior$2 INSTANCE = new NotificationInterruptLogger$logNoAlertingGroupAlertBehavior$2();

    public NotificationInterruptLogger$logNoAlertingGroupAlertBehavior$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("No alerting: suppressed due to group alert behavior: ", logMessage.getStr1());
    }
}
