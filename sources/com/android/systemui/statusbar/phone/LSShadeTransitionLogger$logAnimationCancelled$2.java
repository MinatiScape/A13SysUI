package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: LSShadeTransitionLogger.kt */
/* loaded from: classes.dex */
public final class LSShadeTransitionLogger$logAnimationCancelled$2 extends Lambda implements Function1<LogMessage, String> {
    public static final LSShadeTransitionLogger$logAnimationCancelled$2 INSTANCE = new LSShadeTransitionLogger$logAnimationCancelled$2();

    public LSShadeTransitionLogger$logAnimationCancelled$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final /* bridge */ /* synthetic */ String invoke(LogMessage logMessage) {
        return "Pulse animation cancelled";
    }
}
