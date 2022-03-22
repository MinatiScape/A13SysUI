package com.android.systemui.statusbar.notification.interruption;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationInterruptLogger.kt */
/* loaded from: classes.dex */
final class NotificationInterruptLogger$logNoBubbleNoMetadata$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationInterruptLogger$logNoBubbleNoMetadata$2 INSTANCE = new NotificationInterruptLogger$logNoBubbleNoMetadata$2();

    public NotificationInterruptLogger$logNoBubbleNoMetadata$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("No bubble up: notification: ");
        m.append((Object) logMessage.getStr1());
        m.append(" doesn't have valid metadata");
        return m.toString();
    }
}
