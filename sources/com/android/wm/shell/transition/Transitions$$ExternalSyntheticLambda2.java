package com.android.wm.shell.transition;

import com.android.wm.shell.transition.Transitions;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class Transitions$$ExternalSyntheticLambda2 implements Function {
    public static final /* synthetic */ Transitions$$ExternalSyntheticLambda2 INSTANCE = new Transitions$$ExternalSyntheticLambda2();

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        boolean z = Transitions.ENABLE_SHELL_TRANSITIONS;
        return ((Transitions.ActiveTransition) obj).mToken;
    }
}
