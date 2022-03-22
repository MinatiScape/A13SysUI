package com.android.systemui.statusbar.notification.interruption;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationInterruptLogger.kt */
/* loaded from: classes.dex */
public final class NotificationInterruptLogger$logNoAlertingSuppressedBy$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationInterruptLogger$logNoAlertingSuppressedBy$2 INSTANCE = new NotificationInterruptLogger$logNoAlertingSuppressedBy$2();

    public NotificationInterruptLogger$logNoAlertingSuppressedBy$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("No alerting: aborted by suppressor: ");
        m.append((Object) logMessage2.getStr2());
        m.append(" awake=");
        m.append(logMessage2.getBool1());
        m.append(" sbnKey=");
        m.append((Object) logMessage2.getStr1());
        return m.toString();
    }
}
