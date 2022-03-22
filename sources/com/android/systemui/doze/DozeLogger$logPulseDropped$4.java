package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: DozeLogger.kt */
/* loaded from: classes.dex */
final class DozeLogger$logPulseDropped$4 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logPulseDropped$4 INSTANCE = new DozeLogger$logPulseDropped$4();

    public DozeLogger$logPulseDropped$4() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("Pulse dropped, why=", logMessage.getStr1());
    }
}
