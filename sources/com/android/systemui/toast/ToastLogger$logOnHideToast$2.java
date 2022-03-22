package com.android.systemui.toast;

import com.airbnb.lottie.parser.moshi.JsonReader$$ExternalSyntheticOutline0;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ToastLogger.kt */
/* loaded from: classes.dex */
final class ToastLogger$logOnHideToast$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ToastLogger$logOnHideToast$2 INSTANCE = new ToastLogger$logOnHideToast$2();

    public ToastLogger$logOnHideToast$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = JsonReader$$ExternalSyntheticOutline0.m('[');
        m.append((Object) logMessage2.getStr2());
        m.append("] Hide toast for [");
        m.append((Object) logMessage2.getStr1());
        m.append(']');
        return m.toString();
    }
}
