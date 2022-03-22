package com.android.systemui.statusbar.gesture;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: SwipeStatusBarAwayGestureLogger.kt */
/* loaded from: classes.dex */
final class SwipeStatusBarAwayGestureLogger$logGestureDetected$2 extends Lambda implements Function1<LogMessage, String> {
    public static final SwipeStatusBarAwayGestureLogger$logGestureDetected$2 INSTANCE = new SwipeStatusBarAwayGestureLogger$logGestureDetected$2();

    public SwipeStatusBarAwayGestureLogger$logGestureDetected$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("Gesture detected; notifying callbacks. y=", Integer.valueOf(logMessage.getInt1()));
    }
}
