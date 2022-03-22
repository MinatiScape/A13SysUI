package com.android.systemui.broadcast.logging;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: BroadcastDispatcherLogger.kt */
/* loaded from: classes.dex */
public final class BroadcastDispatcherLogger$logBroadcastDispatched$2 extends Lambda implements Function1<LogMessage, String> {
    public static final BroadcastDispatcherLogger$logBroadcastDispatched$2 INSTANCE = new BroadcastDispatcherLogger$logBroadcastDispatched$2();

    public BroadcastDispatcherLogger$logBroadcastDispatched$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Broadcast ");
        m.append(logMessage2.getInt1());
        m.append(" (");
        m.append((Object) logMessage2.getStr1());
        m.append(") dispatched to ");
        m.append((Object) logMessage2.getStr2());
        return m.toString();
    }
}
