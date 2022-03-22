package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: StatusBarNotificationActivityStarterLogger.kt */
/* loaded from: classes.dex */
final class StatusBarNotificationActivityStarterLogger$logSendingIntentFailed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final StatusBarNotificationActivityStarterLogger$logSendingIntentFailed$2 INSTANCE = new StatusBarNotificationActivityStarterLogger$logSendingIntentFailed$2();

    public StatusBarNotificationActivityStarterLogger$logSendingIntentFailed$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("Sending contentIntentFailed: ", logMessage.getStr1());
    }
}
