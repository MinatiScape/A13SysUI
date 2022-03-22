package com.android.systemui.statusbar.notification.collection.render;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ShadeViewDifferLogger.kt */
/* loaded from: classes.dex */
final class ShadeViewDifferLogger$logDetachingChild$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeViewDifferLogger$logDetachingChild$2 INSTANCE = new ShadeViewDifferLogger$logDetachingChild$2();

    public ShadeViewDifferLogger$logDetachingChild$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Detach ");
        m.append((Object) logMessage2.getStr1());
        m.append(" isTransfer=");
        m.append(logMessage2.getBool1());
        m.append(" isParentRemoved=");
        m.append(logMessage2.getBool2());
        m.append(" oldParent=");
        m.append((Object) logMessage2.getStr2());
        m.append(" newParent=");
        m.append((Object) logMessage2.getStr3());
        return m.toString();
    }
}
