package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: LSShadeTransitionLogger.kt */
/* loaded from: classes.dex */
public final class LSShadeTransitionLogger$logDefaultGoToFullShadeAnimation$2 extends Lambda implements Function1<LogMessage, String> {
    public static final LSShadeTransitionLogger$logDefaultGoToFullShadeAnimation$2 INSTANCE = new LSShadeTransitionLogger$logDefaultGoToFullShadeAnimation$2();

    public LSShadeTransitionLogger$logDefaultGoToFullShadeAnimation$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("Default animation started to full shade with delay ", Long.valueOf(logMessage.getLong1()));
    }
}
