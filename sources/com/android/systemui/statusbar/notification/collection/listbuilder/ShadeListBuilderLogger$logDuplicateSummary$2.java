package com.android.systemui.statusbar.notification.collection.listbuilder;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ShadeListBuilderLogger.kt */
/* loaded from: classes.dex */
public final class ShadeListBuilderLogger$logDuplicateSummary$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeListBuilderLogger$logDuplicateSummary$2 INSTANCE = new ShadeListBuilderLogger$logDuplicateSummary$2();

    public ShadeListBuilderLogger$logDuplicateSummary$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("(Build ");
        m.append(logMessage2.getLong1());
        m.append(") Duplicate summary for group \"");
        m.append((Object) logMessage2.getStr1());
        m.append("\": \"");
        m.append((Object) logMessage2.getStr2());
        m.append("\" vs. \"");
        m.append((Object) logMessage2.getStr3());
        m.append('\"');
        return m.toString();
    }
}
