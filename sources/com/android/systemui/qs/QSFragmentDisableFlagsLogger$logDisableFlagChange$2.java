package com.android.systemui.qs;

import com.android.systemui.log.LogMessage;
import com.android.systemui.statusbar.DisableFlagsLogger;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: QSFragmentDisableFlagsLogger.kt */
/* loaded from: classes.dex */
final class QSFragmentDisableFlagsLogger$logDisableFlagChange$2 extends Lambda implements Function1<LogMessage, String> {
    public final /* synthetic */ QSFragmentDisableFlagsLogger this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public QSFragmentDisableFlagsLogger$logDisableFlagChange$2(QSFragmentDisableFlagsLogger qSFragmentDisableFlagsLogger) {
        super(1);
        this.this$0 = qSFragmentDisableFlagsLogger;
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        return this.this$0.disableFlagsLogger.getDisableFlagsString(null, new DisableFlagsLogger.DisableState(logMessage2.getInt1(), logMessage2.getInt2()), new DisableFlagsLogger.DisableState((int) logMessage2.getLong1(), (int) logMessage2.getLong2()));
    }
}
