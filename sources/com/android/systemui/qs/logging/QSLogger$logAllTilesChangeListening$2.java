package com.android.systemui.qs.logging;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: QSLogger.kt */
/* loaded from: classes.dex */
public final class QSLogger$logAllTilesChangeListening$2 extends Lambda implements Function1<LogMessage, String> {
    public static final QSLogger$logAllTilesChangeListening$2 INSTANCE = new QSLogger$logAllTilesChangeListening$2();

    public QSLogger$logAllTilesChangeListening$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Tiles listening=");
        m.append(logMessage2.getBool1());
        m.append(" in ");
        m.append((Object) logMessage2.getStr1());
        m.append(". ");
        m.append((Object) logMessage2.getStr2());
        return m.toString();
    }
}
