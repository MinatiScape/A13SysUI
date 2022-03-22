package com.android.systemui.statusbar.notification.stack;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationStackScrollLogger.kt */
/* loaded from: classes.dex */
final class NotificationStackScrollLogger$hunSkippedForUnexpectedState$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationStackScrollLogger$hunSkippedForUnexpectedState$2 INSTANCE = new NotificationStackScrollLogger$hunSkippedForUnexpectedState$2();

    public NotificationStackScrollLogger$hunSkippedForUnexpectedState$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("HUN animation skipped for unexpected hun state: key: ");
        m.append((Object) logMessage2.getStr1());
        m.append(" expected: ");
        m.append(logMessage2.getBool1());
        m.append(" actual: ");
        m.append(logMessage2.getBool2());
        return m.toString();
    }
}
