package com.android.systemui.qs.logging;

import com.airbnb.lottie.parser.moshi.JsonReader$$ExternalSyntheticOutline0;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: QSLogger.kt */
/* loaded from: classes.dex */
public final class QSLogger$logTileSecondaryClick$2 extends Lambda implements Function1<LogMessage, String> {
    public static final QSLogger$logTileSecondaryClick$2 INSTANCE = new QSLogger$logTileSecondaryClick$2();

    public QSLogger$logTileSecondaryClick$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = JsonReader$$ExternalSyntheticOutline0.m('[');
        m.append((Object) logMessage2.getStr1());
        m.append("] Tile long clicked. StatusBarState=");
        m.append((Object) logMessage2.getStr2());
        m.append(". TileState=");
        m.append((Object) logMessage2.getStr3());
        return m.toString();
    }
}
