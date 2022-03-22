package com.android.systemui.qs.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: QSLogger.kt */
/* loaded from: classes.dex */
public final class QSLogger$logPanelExpanded$2 extends Lambda implements Function1<LogMessage, String> {
    public static final QSLogger$logPanelExpanded$2 INSTANCE = new QSLogger$logPanelExpanded$2();

    public QSLogger$logPanelExpanded$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        return ((Object) logMessage2.getStr1()) + " expanded=" + logMessage2.getBool1();
    }
}
