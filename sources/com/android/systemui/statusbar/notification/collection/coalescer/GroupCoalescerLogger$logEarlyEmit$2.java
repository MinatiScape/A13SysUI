package com.android.systemui.statusbar.notification.collection.coalescer;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: GroupCoalescerLogger.kt */
/* loaded from: classes.dex */
public final class GroupCoalescerLogger$logEarlyEmit$2 extends Lambda implements Function1<LogMessage, String> {
    public static final GroupCoalescerLogger$logEarlyEmit$2 INSTANCE = new GroupCoalescerLogger$logEarlyEmit$2();

    public GroupCoalescerLogger$logEarlyEmit$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Modification of notif ");
        m.append((Object) logMessage2.getStr1());
        m.append(" triggered early emit of batched group ");
        m.append((Object) logMessage2.getStr2());
        return m.toString();
    }
}
