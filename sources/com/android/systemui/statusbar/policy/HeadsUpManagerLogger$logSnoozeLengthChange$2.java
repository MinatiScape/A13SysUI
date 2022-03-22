package com.android.systemui.statusbar.policy;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: HeadsUpManagerLogger.kt */
/* loaded from: classes.dex */
final class HeadsUpManagerLogger$logSnoozeLengthChange$2 extends Lambda implements Function1<LogMessage, String> {
    public static final HeadsUpManagerLogger$logSnoozeLengthChange$2 INSTANCE = new HeadsUpManagerLogger$logSnoozeLengthChange$2();

    public HeadsUpManagerLogger$logSnoozeLengthChange$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("snooze length changed: ");
        m.append(logMessage.getInt1());
        m.append("ms");
        return m.toString();
    }
}
