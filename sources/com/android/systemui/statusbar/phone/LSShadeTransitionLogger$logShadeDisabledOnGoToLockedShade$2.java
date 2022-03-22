package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: LSShadeTransitionLogger.kt */
/* loaded from: classes.dex */
public final class LSShadeTransitionLogger$logShadeDisabledOnGoToLockedShade$2 extends Lambda implements Function1<LogMessage, String> {
    public static final LSShadeTransitionLogger$logShadeDisabledOnGoToLockedShade$2 INSTANCE = new LSShadeTransitionLogger$logShadeDisabledOnGoToLockedShade$2();

    public LSShadeTransitionLogger$logShadeDisabledOnGoToLockedShade$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final /* bridge */ /* synthetic */ String invoke(LogMessage logMessage) {
        return "The shade was disabled when trying to go to the locked shade";
    }
}
