package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ShadeViewDifferLogger.kt */
/* loaded from: classes.dex */
final class ShadeViewDifferLogger$logDuplicateNodeInTree$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeViewDifferLogger$logDuplicateNodeInTree$2 INSTANCE = new ShadeViewDifferLogger$logDuplicateNodeInTree$2();

    public ShadeViewDifferLogger$logDuplicateNodeInTree$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        return ((Object) logMessage2.getStr1()) + " when mapping tree: " + ((Object) logMessage2.getStr2());
    }
}
