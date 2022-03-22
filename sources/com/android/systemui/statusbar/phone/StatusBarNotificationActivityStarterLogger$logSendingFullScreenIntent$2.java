package com.android.systemui.statusbar.phone;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: StatusBarNotificationActivityStarterLogger.kt */
/* loaded from: classes.dex */
final class StatusBarNotificationActivityStarterLogger$logSendingFullScreenIntent$2 extends Lambda implements Function1<LogMessage, String> {
    public static final StatusBarNotificationActivityStarterLogger$logSendingFullScreenIntent$2 INSTANCE = new StatusBarNotificationActivityStarterLogger$logSendingFullScreenIntent$2();

    public StatusBarNotificationActivityStarterLogger$logSendingFullScreenIntent$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Notification ");
        m.append((Object) logMessage2.getStr1());
        m.append(" has fullScreenIntent; sending fullScreenIntent ");
        m.append((Object) logMessage2.getStr2());
        return m.toString();
    }
}
