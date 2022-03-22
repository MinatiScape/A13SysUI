package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: LSShadeTransitionLogger.kt */
/* loaded from: classes.dex */
public final class LSShadeTransitionLogger$logAnimationCancelled$4 extends Lambda implements Function1<LogMessage, String> {
    public static final LSShadeTransitionLogger$logAnimationCancelled$4 INSTANCE = new LSShadeTransitionLogger$logAnimationCancelled$4();

    public LSShadeTransitionLogger$logAnimationCancelled$4() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final /* bridge */ /* synthetic */ String invoke(LogMessage logMessage) {
        return "drag down animation cancelled";
    }
}
