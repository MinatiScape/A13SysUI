package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.airbnb.lottie.parser.moshi.JsonReader$$ExternalSyntheticOutline0;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ShadeListBuilderLogger.kt */
/* loaded from: classes.dex */
public final class ShadeListBuilderLogger$logFinalList$4 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeListBuilderLogger$logFinalList$4 INSTANCE = new ShadeListBuilderLogger$logFinalList$4();

    public ShadeListBuilderLogger$logFinalList$4() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = JsonReader$$ExternalSyntheticOutline0.m('[');
        m.append(logMessage2.getInt1());
        m.append("] ");
        m.append((Object) logMessage2.getStr1());
        return m.toString();
    }
}
