package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: GutsCoordinatorLogger.kt */
/* loaded from: classes.dex */
final class GutsCoordinatorLogger$logGutsClosed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final GutsCoordinatorLogger$logGutsClosed$2 INSTANCE = new GutsCoordinatorLogger$logGutsClosed$2();

    public GutsCoordinatorLogger$logGutsClosed$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("Guts closed for class ", logMessage.getStr1());
    }
}
