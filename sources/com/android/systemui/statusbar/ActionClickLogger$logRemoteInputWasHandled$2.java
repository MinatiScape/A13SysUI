package com.android.systemui.statusbar;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ActionClickLogger.kt */
/* loaded from: classes.dex */
final class ActionClickLogger$logRemoteInputWasHandled$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ActionClickLogger$logRemoteInputWasHandled$2 INSTANCE = new ActionClickLogger$logRemoteInputWasHandled$2();

    public ActionClickLogger$logRemoteInputWasHandled$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("  [Action click] Triggered remote input (for ");
        m.append((Object) logMessage.getStr1());
        m.append("))");
        return m.toString();
    }
}
