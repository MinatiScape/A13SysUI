package com.android.systemui.biometrics;

import com.android.systemui.biometrics.AuthRippleController;
import com.android.systemui.statusbar.commandline.Command;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: AuthRippleController.kt */
/* loaded from: classes.dex */
public final class AuthRippleController$onViewAttached$1 extends Lambda implements Function0<Command> {
    public final /* synthetic */ AuthRippleController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AuthRippleController$onViewAttached$1(AuthRippleController authRippleController) {
        super(0);
        this.this$0 = authRippleController;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Command invoke() {
        return new AuthRippleController.AuthRippleCommand();
    }
}
