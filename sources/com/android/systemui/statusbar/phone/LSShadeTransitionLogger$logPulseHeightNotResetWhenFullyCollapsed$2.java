package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: LSShadeTransitionLogger.kt */
/* loaded from: classes.dex */
public final class LSShadeTransitionLogger$logPulseHeightNotResetWhenFullyCollapsed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final LSShadeTransitionLogger$logPulseHeightNotResetWhenFullyCollapsed$2 INSTANCE = new LSShadeTransitionLogger$logPulseHeightNotResetWhenFullyCollapsed$2();

    public LSShadeTransitionLogger$logPulseHeightNotResetWhenFullyCollapsed$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final /* bridge */ /* synthetic */ String invoke(LogMessage logMessage) {
        return "Pulse height stuck and reset after shade was fully collapsed";
    }
}
