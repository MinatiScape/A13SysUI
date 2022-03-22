package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DozeLogger.kt */
/* loaded from: classes.dex */
public final class DozeLogger$logPulseStart$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logPulseStart$2 INSTANCE = new DozeLogger$logPulseStart$2();

    public DozeLogger$logPulseStart$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("Pulse start, reason=", DozeLog.reasonToString(logMessage.getInt1()));
    }
}
