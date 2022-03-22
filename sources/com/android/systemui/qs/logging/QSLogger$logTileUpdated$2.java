package com.android.systemui.qs.logging;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.airbnb.lottie.parser.moshi.JsonReader$$ExternalSyntheticOutline0;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: QSLogger.kt */
/* loaded from: classes.dex */
public final class QSLogger$logTileUpdated$2 extends Lambda implements Function1<LogMessage, String> {
    public static final QSLogger$logTileUpdated$2 INSTANCE = new QSLogger$logTileUpdated$2();

    public QSLogger$logTileUpdated$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        String str;
        LogMessage logMessage2 = logMessage;
        StringBuilder m = JsonReader$$ExternalSyntheticOutline0.m('[');
        m.append((Object) logMessage2.getStr1());
        m.append("] Tile updated. Label=");
        m.append((Object) logMessage2.getStr2());
        m.append(". State=");
        m.append(logMessage2.getInt1());
        m.append(". Icon=");
        m.append((Object) logMessage2.getStr3());
        m.append('.');
        if (logMessage2.getBool1()) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m(" Activity in/out=");
            m2.append(logMessage2.getBool2());
            m2.append('/');
            m2.append(logMessage2.getBool3());
            str = m2.toString();
        } else {
            str = "";
        }
        m.append(str);
        return m.toString();
    }
}
