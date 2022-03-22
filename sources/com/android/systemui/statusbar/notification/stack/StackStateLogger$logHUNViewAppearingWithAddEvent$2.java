package com.android.systemui.statusbar.notification.stack;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: StackStateLogger.kt */
/* loaded from: classes.dex */
final class StackStateLogger$logHUNViewAppearingWithAddEvent$2 extends Lambda implements Function1<LogMessage, String> {
    public static final StackStateLogger$logHUNViewAppearingWithAddEvent$2 INSTANCE = new StackStateLogger$logHUNViewAppearingWithAddEvent$2();

    public StackStateLogger$logHUNViewAppearingWithAddEvent$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Heads up view disappearing ");
        m.append((Object) logMessage.getStr1());
        m.append(" for ANIMATION_TYPE_ADD");
        return m.toString();
    }
}
