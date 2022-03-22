package com.android.systemui.statusbar.notification.stack;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationStackScrollLogger.kt */
/* loaded from: classes.dex */
final class NotificationStackScrollLogger$hunAnimationEventAdded$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationStackScrollLogger$hunAnimationEventAdded$2 INSTANCE = new NotificationStackScrollLogger$hunAnimationEventAdded$2();

    public NotificationStackScrollLogger$hunAnimationEventAdded$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("heads up animation added: ");
        m.append((Object) logMessage2.getStr1());
        m.append(" with type ");
        m.append((Object) logMessage2.getStr2());
        return m.toString();
    }
}
