package com.android.systemui.statusbar.notification.stack;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: StackStateLogger.kt */
/* loaded from: classes.dex */
final class StackStateLogger$logHUNViewDisappearing$2 extends Lambda implements Function1<LogMessage, String> {
    public static final StackStateLogger$logHUNViewDisappearing$2 INSTANCE = new StackStateLogger$logHUNViewDisappearing$2();

    public StackStateLogger$logHUNViewDisappearing$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Heads up view disappearing ");
        m.append((Object) logMessage.getStr1());
        m.append(' ');
        return m.toString();
    }
}
