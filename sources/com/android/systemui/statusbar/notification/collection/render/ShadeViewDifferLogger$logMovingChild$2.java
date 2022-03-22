package com.android.systemui.statusbar.notification.collection.render;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ShadeViewDifferLogger.kt */
/* loaded from: classes.dex */
final class ShadeViewDifferLogger$logMovingChild$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeViewDifferLogger$logMovingChild$2 INSTANCE = new ShadeViewDifferLogger$logMovingChild$2();

    public ShadeViewDifferLogger$logMovingChild$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Moving child view ");
        m.append((Object) logMessage2.getStr1());
        m.append(" in ");
        m.append((Object) logMessage2.getStr2());
        m.append(" to index ");
        m.append(logMessage2.getInt1());
        return m.toString();
    }
}
