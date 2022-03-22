package com.android.systemui.statusbar.phone.fragment;

import com.android.systemui.log.LogMessage;
import com.android.systemui.statusbar.DisableFlagsLogger;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: CollapsedStatusBarFragmentLogger.kt */
/* loaded from: classes.dex */
final class CollapsedStatusBarFragmentLogger$logDisableFlagChange$2 extends Lambda implements Function1<LogMessage, String> {
    public final /* synthetic */ CollapsedStatusBarFragmentLogger this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CollapsedStatusBarFragmentLogger$logDisableFlagChange$2(CollapsedStatusBarFragmentLogger collapsedStatusBarFragmentLogger) {
        super(1);
        this.this$0 = collapsedStatusBarFragmentLogger;
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        return this.this$0.disableFlagsLogger.getDisableFlagsString(null, new DisableFlagsLogger.DisableState(logMessage2.getInt1(), logMessage2.getInt2()), new DisableFlagsLogger.DisableState((int) logMessage2.getLong1(), (int) logMessage2.getLong2()));
    }
}
