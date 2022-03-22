package com.android.systemui.doze;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: DozeLogger.kt */
/* loaded from: classes.dex */
final class DozeLogger$logPulseDropped$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logPulseDropped$2 INSTANCE = new DozeLogger$logPulseDropped$2();

    public DozeLogger$logPulseDropped$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Pulse dropped, pulsePending=");
        m.append(logMessage2.getBool1());
        m.append(" state=");
        m.append((Object) logMessage2.getStr1());
        m.append(" blocked=");
        m.append(logMessage2.getBool2());
        return m.toString();
    }
}
