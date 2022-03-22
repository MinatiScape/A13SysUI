package com.android.systemui.broadcast.logging;

import com.airbnb.lottie.parser.moshi.JsonReader$$ExternalSyntheticOutline0;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: BroadcastDispatcherLogger.kt */
/* loaded from: classes.dex */
public final class BroadcastDispatcherLogger$logBroadcastReceived$2 extends Lambda implements Function1<LogMessage, String> {
    public static final BroadcastDispatcherLogger$logBroadcastReceived$2 INSTANCE = new BroadcastDispatcherLogger$logBroadcastReceived$2();

    public BroadcastDispatcherLogger$logBroadcastReceived$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = JsonReader$$ExternalSyntheticOutline0.m('[');
        m.append(logMessage2.getInt1());
        m.append("] Broadcast received for user ");
        m.append(logMessage2.getInt2());
        m.append(": ");
        m.append((Object) logMessage2.getStr1());
        return m.toString();
    }
}
