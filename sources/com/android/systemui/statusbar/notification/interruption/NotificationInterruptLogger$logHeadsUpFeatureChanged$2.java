package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationInterruptLogger.kt */
/* loaded from: classes.dex */
final class NotificationInterruptLogger$logHeadsUpFeatureChanged$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationInterruptLogger$logHeadsUpFeatureChanged$2 INSTANCE = new NotificationInterruptLogger$logHeadsUpFeatureChanged$2();

    public NotificationInterruptLogger$logHeadsUpFeatureChanged$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("heads up is enabled=", Boolean.valueOf(logMessage.getBool1()));
    }
}
