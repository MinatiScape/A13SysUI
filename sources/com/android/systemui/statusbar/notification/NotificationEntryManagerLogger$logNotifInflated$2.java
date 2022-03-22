package com.android.systemui.statusbar.notification;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationEntryManagerLogger.kt */
/* loaded from: classes.dex */
final class NotificationEntryManagerLogger$logNotifInflated$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationEntryManagerLogger$logNotifInflated$2 INSTANCE = new NotificationEntryManagerLogger$logNotifInflated$2();

    public NotificationEntryManagerLogger$logNotifInflated$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("NOTIF INFLATED ");
        m.append((Object) logMessage2.getStr1());
        m.append(" isNew=");
        m.append(logMessage2.getBool1());
        m.append('}');
        return m.toString();
    }
}
