package com.android.systemui.statusbar.notification.collection.listbuilder;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ShadeListBuilderLogger.kt */
/* loaded from: classes.dex */
public final class ShadeListBuilderLogger$logEndBuildList$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeListBuilderLogger$logEndBuildList$2 INSTANCE = new ShadeListBuilderLogger$logEndBuildList$2();

    public ShadeListBuilderLogger$logEndBuildList$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("(Build ");
        m.append(logMessage2.getLong1());
        m.append(") Build complete (");
        m.append(logMessage2.getInt1());
        m.append(" top-level entries, ");
        m.append(logMessage2.getInt2());
        m.append(" children)");
        return m.toString();
    }
}
