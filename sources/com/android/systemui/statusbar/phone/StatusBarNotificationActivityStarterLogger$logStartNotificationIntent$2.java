package com.android.systemui.statusbar.phone;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: StatusBarNotificationActivityStarterLogger.kt */
/* loaded from: classes.dex */
final class StatusBarNotificationActivityStarterLogger$logStartNotificationIntent$2 extends Lambda implements Function1<LogMessage, String> {
    public static final StatusBarNotificationActivityStarterLogger$logStartNotificationIntent$2 INSTANCE = new StatusBarNotificationActivityStarterLogger$logStartNotificationIntent$2();

    public StatusBarNotificationActivityStarterLogger$logStartNotificationIntent$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("(4/4) Starting ");
        m.append((Object) logMessage2.getStr2());
        m.append(" for notification ");
        m.append((Object) logMessage2.getStr1());
        return m.toString();
    }
}
