package com.android.systemui.statusbar.notification.collection.listbuilder;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ShadeListBuilderLogger.kt */
/* loaded from: classes.dex */
public final class ShadeListBuilderLogger$logSectionChanged$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeListBuilderLogger$logSectionChanged$2 INSTANCE = new ShadeListBuilderLogger$logSectionChanged$2();

    public ShadeListBuilderLogger$logSectionChanged$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        if (logMessage2.getStr1() == null) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("(Build ");
            m.append(logMessage2.getLong1());
            m.append(")     Section assigned: ");
            m.append((Object) logMessage2.getStr2());
            return m.toString();
        }
        StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("(Build ");
        m2.append(logMessage2.getLong1());
        m2.append(")     Section changed: ");
        m2.append((Object) logMessage2.getStr1());
        m2.append(" -> ");
        m2.append((Object) logMessage2.getStr2());
        return m2.toString();
    }
}
