package com.android.systemui.log;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: LogBuffer.kt */
/* loaded from: classes.dex */
public final class LogBuffer$freeze$2 extends Lambda implements Function1<LogMessage, String> {
    public static final LogBuffer$freeze$2 INSTANCE = new LogBuffer$freeze$2();

    public LogBuffer$freeze$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus(logMessage.getStr1(), " frozen");
    }
}
