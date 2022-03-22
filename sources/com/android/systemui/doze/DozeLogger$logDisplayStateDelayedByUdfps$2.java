package com.android.systemui.doze;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: DozeLogger.kt */
/* loaded from: classes.dex */
final class DozeLogger$logDisplayStateDelayedByUdfps$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logDisplayStateDelayedByUdfps$2 INSTANCE = new DozeLogger$logDisplayStateDelayedByUdfps$2();

    public DozeLogger$logDisplayStateDelayedByUdfps$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Delaying display state change to: ");
        m.append((Object) logMessage.getStr1());
        m.append(" due to UDFPS activity");
        return m.toString();
    }
}
