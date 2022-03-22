package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotifBindPipelineLogger.kt */
/* loaded from: classes.dex */
final class NotifBindPipelineLogger$logManagedRow$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifBindPipelineLogger$logManagedRow$2 INSTANCE = new NotifBindPipelineLogger$logManagedRow$2();

    public NotifBindPipelineLogger$logManagedRow$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("Row set for notif: ", logMessage.getStr1());
    }
}
