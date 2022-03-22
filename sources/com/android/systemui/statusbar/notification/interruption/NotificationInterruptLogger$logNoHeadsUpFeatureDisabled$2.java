package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationInterruptLogger.kt */
/* loaded from: classes.dex */
final class NotificationInterruptLogger$logNoHeadsUpFeatureDisabled$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationInterruptLogger$logNoHeadsUpFeatureDisabled$2 INSTANCE = new NotificationInterruptLogger$logNoHeadsUpFeatureDisabled$2();

    public NotificationInterruptLogger$logNoHeadsUpFeatureDisabled$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final /* bridge */ /* synthetic */ String invoke(LogMessage logMessage) {
        return "No heads up: no huns";
    }
}
