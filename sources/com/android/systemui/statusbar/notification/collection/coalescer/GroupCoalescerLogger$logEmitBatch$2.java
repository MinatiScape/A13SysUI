package com.android.systemui.statusbar.notification.collection.coalescer;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: GroupCoalescerLogger.kt */
/* loaded from: classes.dex */
final class GroupCoalescerLogger$logEmitBatch$2 extends Lambda implements Function1<LogMessage, String> {
    public static final GroupCoalescerLogger$logEmitBatch$2 INSTANCE = new GroupCoalescerLogger$logEmitBatch$2();

    public GroupCoalescerLogger$logEmitBatch$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Emitting batch for group ");
        m.append((Object) logMessage2.getStr1());
        m.append(" size=");
        m.append(logMessage2.getInt1());
        m.append(" age=");
        m.append(logMessage2.getLong1());
        m.append("ms");
        return m.toString();
    }
}
