package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotifBindPipelineLogger.kt */
/* loaded from: classes.dex */
final class NotifBindPipelineLogger$logRequestPipelineRowNotSet$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifBindPipelineLogger$logRequestPipelineRowNotSet$2 INSTANCE = new NotifBindPipelineLogger$logRequestPipelineRowNotSet$2();

    public NotifBindPipelineLogger$logRequestPipelineRowNotSet$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("Row is not set so pipeline will not run. notif = ", logMessage.getStr1());
    }
}
