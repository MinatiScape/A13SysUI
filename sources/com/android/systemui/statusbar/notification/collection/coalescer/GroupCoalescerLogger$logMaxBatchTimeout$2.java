package com.android.systemui.statusbar.notification.collection.coalescer;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: GroupCoalescerLogger.kt */
/* loaded from: classes.dex */
public final class GroupCoalescerLogger$logMaxBatchTimeout$2 extends Lambda implements Function1<LogMessage, String> {
    public static final GroupCoalescerLogger$logMaxBatchTimeout$2 INSTANCE = new GroupCoalescerLogger$logMaxBatchTimeout$2();

    public GroupCoalescerLogger$logMaxBatchTimeout$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Modification of notif ");
        m.append((Object) logMessage2.getStr1());
        m.append(" triggered TIMEOUT emit of batched group ");
        m.append((Object) logMessage2.getStr2());
        return m.toString();
    }
}
