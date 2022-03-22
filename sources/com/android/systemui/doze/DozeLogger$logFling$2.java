package com.android.systemui.doze;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: DozeLogger.kt */
/* loaded from: classes.dex */
final class DozeLogger$logFling$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logFling$2 INSTANCE = new DozeLogger$logFling$2();

    public DozeLogger$logFling$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Fling expand=");
        m.append(logMessage2.getBool1());
        m.append(" aboveThreshold=");
        m.append(logMessage2.getBool2());
        m.append(" thresholdNeeded=");
        m.append(logMessage2.getBool3());
        m.append(" screenOnFromTouch=");
        m.append(logMessage2.getBool4());
        return m.toString();
    }
}
