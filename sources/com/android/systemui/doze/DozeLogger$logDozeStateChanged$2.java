package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: DozeLogger.kt */
/* loaded from: classes.dex */
final class DozeLogger$logDozeStateChanged$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logDozeStateChanged$2 INSTANCE = new DozeLogger$logDozeStateChanged$2();

    public DozeLogger$logDozeStateChanged$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("Doze state changed to ", logMessage.getStr1());
    }
}
