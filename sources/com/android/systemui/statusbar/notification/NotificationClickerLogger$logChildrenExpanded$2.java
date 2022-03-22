package com.android.systemui.statusbar.notification;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationClickerLogger.kt */
/* loaded from: classes.dex */
final class NotificationClickerLogger$logChildrenExpanded$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationClickerLogger$logChildrenExpanded$2 INSTANCE = new NotificationClickerLogger$logChildrenExpanded$2();

    public NotificationClickerLogger$logChildrenExpanded$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Ignoring click on ");
        m.append((Object) logMessage.getStr1());
        m.append("; children are expanded");
        return m.toString();
    }
}
