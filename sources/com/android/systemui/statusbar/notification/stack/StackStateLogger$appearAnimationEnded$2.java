package com.android.systemui.statusbar.notification.stack;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: StackStateLogger.kt */
/* loaded from: classes.dex */
public final class StackStateLogger$appearAnimationEnded$2 extends Lambda implements Function1<LogMessage, String> {
    public static final StackStateLogger$appearAnimationEnded$2 INSTANCE = new StackStateLogger$appearAnimationEnded$2();

    public StackStateLogger$appearAnimationEnded$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Heads up notification appear animation ended ");
        m.append((Object) logMessage.getStr1());
        m.append(' ');
        return m.toString();
    }
}
