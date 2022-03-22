package com.android.systemui.qs.logging;

import com.airbnb.lottie.parser.moshi.JsonReader$$ExternalSyntheticOutline0;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: QSLogger.kt */
/* loaded from: classes.dex */
public final class QSLogger$logTileAdded$2 extends Lambda implements Function1<LogMessage, String> {
    public static final QSLogger$logTileAdded$2 INSTANCE = new QSLogger$logTileAdded$2();

    public QSLogger$logTileAdded$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        StringBuilder m = JsonReader$$ExternalSyntheticOutline0.m('[');
        m.append((Object) logMessage.getStr1());
        m.append("] Tile added");
        return m.toString();
    }
}
