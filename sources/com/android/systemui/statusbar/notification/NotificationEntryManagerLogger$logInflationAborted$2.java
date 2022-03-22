package com.android.systemui.statusbar.notification;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationEntryManagerLogger.kt */
/* loaded from: classes.dex */
public final class NotificationEntryManagerLogger$logInflationAborted$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationEntryManagerLogger$logInflationAborted$2 INSTANCE = new NotificationEntryManagerLogger$logInflationAborted$2();

    public NotificationEntryManagerLogger$logInflationAborted$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("NOTIF INFLATION ABORTED ");
        m.append((Object) logMessage2.getStr1());
        m.append(" notifStatus=");
        m.append((Object) logMessage2.getStr2());
        m.append(" reason=");
        m.append((Object) logMessage2.getStr3());
        return m.toString();
    }
}
