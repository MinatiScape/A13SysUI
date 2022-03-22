package com.android.systemui.statusbar.notification;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationClickerLogger.kt */
/* loaded from: classes.dex */
final class NotificationClickerLogger$logOnClick$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationClickerLogger$logOnClick$2 INSTANCE = new NotificationClickerLogger$logOnClick$2();

    public NotificationClickerLogger$logOnClick$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("CLICK ");
        m.append((Object) logMessage2.getStr1());
        m.append(" (channel=");
        m.append((Object) logMessage2.getStr2());
        m.append(')');
        return m.toString();
    }
}
