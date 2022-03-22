package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: DozeLogger.kt */
/* loaded from: classes.dex */
final class DozeLogger$logNotificationPulse$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logNotificationPulse$2 INSTANCE = new DozeLogger$logNotificationPulse$2();

    public DozeLogger$logNotificationPulse$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final /* bridge */ /* synthetic */ String invoke(LogMessage logMessage) {
        return "Notification pulse";
    }
}
