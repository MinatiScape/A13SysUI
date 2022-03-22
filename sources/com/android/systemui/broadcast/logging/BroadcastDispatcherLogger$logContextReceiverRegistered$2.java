package com.android.systemui.broadcast.logging;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlin.text.StringsKt__IndentKt;
/* compiled from: BroadcastDispatcherLogger.kt */
/* loaded from: classes.dex */
public final class BroadcastDispatcherLogger$logContextReceiverRegistered$2 extends Lambda implements Function1<LogMessage, String> {
    public static final BroadcastDispatcherLogger$logContextReceiverRegistered$2 INSTANCE = new BroadcastDispatcherLogger$logContextReceiverRegistered$2();

    public BroadcastDispatcherLogger$logContextReceiverRegistered$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("\n                Receiver registered with Context for user ");
        m.append(logMessage2.getInt1());
        m.append(". Flags=");
        m.append((Object) logMessage2.getStr2());
        m.append("\n                ");
        m.append((Object) logMessage2.getStr1());
        m.append("\n            ");
        return StringsKt__IndentKt.trimIndent(m.toString());
    }
}
