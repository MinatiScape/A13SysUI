package com.android.systemui.statusbar;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ActionClickLogger.kt */
/* loaded from: classes.dex */
public final class ActionClickLogger$logKeyguardGone$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ActionClickLogger$logKeyguardGone$2 INSTANCE = new ActionClickLogger$logKeyguardGone$2();

    public ActionClickLogger$logKeyguardGone$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("  [Action click] Keyguard dismissed, calling default handler for intent ", logMessage.getStr1());
    }
}
