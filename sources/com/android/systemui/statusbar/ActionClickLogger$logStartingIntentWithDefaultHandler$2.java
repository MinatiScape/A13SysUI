package com.android.systemui.statusbar;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ActionClickLogger.kt */
/* loaded from: classes.dex */
final class ActionClickLogger$logStartingIntentWithDefaultHandler$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ActionClickLogger$logStartingIntentWithDefaultHandler$2 INSTANCE = new ActionClickLogger$logStartingIntentWithDefaultHandler$2();

    public ActionClickLogger$logStartingIntentWithDefaultHandler$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("  [Action click] Launching intent ");
        m.append((Object) logMessage2.getStr2());
        m.append(" via default handler (for ");
        m.append((Object) logMessage2.getStr1());
        m.append(')');
        return m.toString();
    }
}
