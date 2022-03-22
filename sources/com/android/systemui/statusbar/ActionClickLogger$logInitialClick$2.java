package com.android.systemui.statusbar;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ActionClickLogger.kt */
/* loaded from: classes.dex */
final class ActionClickLogger$logInitialClick$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ActionClickLogger$logInitialClick$2 INSTANCE = new ActionClickLogger$logInitialClick$2();

    public ActionClickLogger$logInitialClick$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ACTION CLICK ");
        m.append((Object) logMessage2.getStr1());
        m.append(" (channel=");
        m.append((Object) logMessage2.getStr2());
        m.append(") for pending intent ");
        m.append((Object) logMessage2.getStr3());
        return m.toString();
    }
}
