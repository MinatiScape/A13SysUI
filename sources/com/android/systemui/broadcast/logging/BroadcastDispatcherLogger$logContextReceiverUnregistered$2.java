package com.android.systemui.broadcast.logging;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: BroadcastDispatcherLogger.kt */
/* loaded from: classes.dex */
public final class BroadcastDispatcherLogger$logContextReceiverUnregistered$2 extends Lambda implements Function1<LogMessage, String> {
    public static final BroadcastDispatcherLogger$logContextReceiverUnregistered$2 INSTANCE = new BroadcastDispatcherLogger$logContextReceiverUnregistered$2();

    public BroadcastDispatcherLogger$logContextReceiverUnregistered$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Receiver unregistered with Context for user ");
        m.append(logMessage2.getInt1());
        m.append(", action ");
        m.append((Object) logMessage2.getStr1());
        return m.toString();
    }
}
