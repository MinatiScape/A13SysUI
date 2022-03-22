package com.android.systemui.statusbar;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ActionClickLogger.kt */
/* loaded from: classes.dex */
public final class ActionClickLogger$logWaitingToCloseKeyguard$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ActionClickLogger$logWaitingToCloseKeyguard$2 INSTANCE = new ActionClickLogger$logWaitingToCloseKeyguard$2();

    public ActionClickLogger$logWaitingToCloseKeyguard$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("  [Action click] Intent ");
        m.append((Object) logMessage.getStr1());
        m.append(" launches an activity, dismissing keyguard first...");
        return m.toString();
    }
}
