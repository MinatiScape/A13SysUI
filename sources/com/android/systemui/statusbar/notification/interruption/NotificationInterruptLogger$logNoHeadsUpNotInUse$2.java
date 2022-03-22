package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationInterruptLogger.kt */
/* loaded from: classes.dex */
final class NotificationInterruptLogger$logNoHeadsUpNotInUse$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationInterruptLogger$logNoHeadsUpNotInUse$2 INSTANCE = new NotificationInterruptLogger$logNoHeadsUpNotInUse$2();

    public NotificationInterruptLogger$logNoHeadsUpNotInUse$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("No heads up: not in use: ", logMessage.getStr1());
    }
}
