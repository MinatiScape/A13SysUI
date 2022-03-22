package com.android.systemui.statusbar.notification.collection.render;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ShadeViewDifferLogger.kt */
/* loaded from: classes.dex */
final class ShadeViewDifferLogger$logAttachingChild$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeViewDifferLogger$logAttachingChild$2 INSTANCE = new ShadeViewDifferLogger$logAttachingChild$2();

    public ShadeViewDifferLogger$logAttachingChild$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Attaching view ");
        m.append((Object) logMessage2.getStr1());
        m.append(" to ");
        m.append((Object) logMessage2.getStr2());
        return m.toString();
    }
}
