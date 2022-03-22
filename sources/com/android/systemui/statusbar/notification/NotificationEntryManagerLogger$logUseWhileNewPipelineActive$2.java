package com.android.systemui.statusbar.notification;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationEntryManagerLogger.kt */
/* loaded from: classes.dex */
public final class NotificationEntryManagerLogger$logUseWhileNewPipelineActive$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationEntryManagerLogger$logUseWhileNewPipelineActive$2 INSTANCE = new NotificationEntryManagerLogger$logUseWhileNewPipelineActive$2();

    public NotificationEntryManagerLogger$logUseWhileNewPipelineActive$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("While running New Pipeline: ");
        m.append((Object) logMessage2.getStr1());
        m.append("(reason=");
        m.append((Object) logMessage2.getStr2());
        m.append(')');
        return m.toString();
    }
}
