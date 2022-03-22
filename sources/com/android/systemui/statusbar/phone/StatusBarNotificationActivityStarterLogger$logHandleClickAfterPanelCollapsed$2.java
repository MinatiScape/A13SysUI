package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: StatusBarNotificationActivityStarterLogger.kt */
/* loaded from: classes.dex */
final class StatusBarNotificationActivityStarterLogger$logHandleClickAfterPanelCollapsed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final StatusBarNotificationActivityStarterLogger$logHandleClickAfterPanelCollapsed$2 INSTANCE = new StatusBarNotificationActivityStarterLogger$logHandleClickAfterPanelCollapsed$2();

    public StatusBarNotificationActivityStarterLogger$logHandleClickAfterPanelCollapsed$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("(3/4) handleNotificationClickAfterPanelCollapsed: ", logMessage.getStr1());
    }
}
