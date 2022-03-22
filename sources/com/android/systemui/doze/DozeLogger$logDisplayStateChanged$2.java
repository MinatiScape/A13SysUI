package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: DozeLogger.kt */
/* loaded from: classes.dex */
final class DozeLogger$logDisplayStateChanged$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logDisplayStateChanged$2 INSTANCE = new DozeLogger$logDisplayStateChanged$2();

    public DozeLogger$logDisplayStateChanged$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("Display state changed to ", logMessage.getStr1());
    }
}
