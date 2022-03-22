package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: LSShadeTransitionLogger.kt */
/* loaded from: classes.dex */
public final class LSShadeTransitionLogger$logGoingToLockedShade$2 extends Lambda implements Function1<LogMessage, String> {
    public final /* synthetic */ boolean $customAnimationHandler;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LSShadeTransitionLogger$logGoingToLockedShade$2(boolean z) {
        super(1);
        this.$customAnimationHandler = z;
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        String str;
        if (this.$customAnimationHandler) {
            str = "with";
        } else {
            str = "without a custom handler";
        }
        return Intrinsics.stringPlus("Going to locked shade ", str);
    }
}
