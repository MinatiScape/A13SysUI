package com.android.systemui.statusbar.gesture;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: SwipeStatusBarAwayGestureLogger.kt */
/* loaded from: classes.dex */
final class SwipeStatusBarAwayGestureLogger$logInputListeningStopped$2 extends Lambda implements Function1<LogMessage, String> {
    public static final SwipeStatusBarAwayGestureLogger$logInputListeningStopped$2 INSTANCE = new SwipeStatusBarAwayGestureLogger$logInputListeningStopped$2();

    public SwipeStatusBarAwayGestureLogger$logInputListeningStopped$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final /* bridge */ /* synthetic */ String invoke(LogMessage logMessage) {
        return "Input listening stopped ";
    }
}
