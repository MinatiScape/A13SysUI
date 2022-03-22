package com.android.systemui.doze;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DozeLogger.kt */
/* loaded from: classes.dex */
public final class DozeLogger$logWakeDisplay$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logWakeDisplay$2 INSTANCE = new DozeLogger$logWakeDisplay$2();

    public DozeLogger$logWakeDisplay$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Display wakefulness changed, isAwake=");
        m.append(logMessage2.getBool1());
        m.append(", reason=");
        m.append((Object) DozeLog.reasonToString(logMessage2.getInt1()));
        return m.toString();
    }
}
