package com.android.systemui.statusbar.notification.collection.listbuilder;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ShadeListBuilderLogger.kt */
/* loaded from: classes.dex */
public final class ShadeListBuilderLogger$logParentChanged$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeListBuilderLogger$logParentChanged$2 INSTANCE = new ShadeListBuilderLogger$logParentChanged$2();

    public ShadeListBuilderLogger$logParentChanged$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        if (logMessage2.getStr1() == null && logMessage2.getStr2() != null) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("(Build ");
            m.append(logMessage2.getLong1());
            m.append(")     Parent is {");
            m.append((Object) logMessage2.getStr2());
            m.append('}');
            return m.toString();
        } else if (logMessage2.getStr1() == null || logMessage2.getStr2() != null) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("(Build ");
            m2.append(logMessage2.getLong1());
            m2.append(")     Reparent: {");
            m2.append((Object) logMessage2.getStr1());
            m2.append("} -> {");
            m2.append((Object) logMessage2.getStr2());
            m2.append('}');
            return m2.toString();
        } else {
            StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("(Build ");
            m3.append(logMessage2.getLong1());
            m3.append(")     Parent was {");
            m3.append((Object) logMessage2.getStr1());
            m3.append('}');
            return m3.toString();
        }
    }
}
