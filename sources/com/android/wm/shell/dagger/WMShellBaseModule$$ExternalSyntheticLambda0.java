package com.android.wm.shell.dagger;

import com.android.wm.shell.back.BackAnimationController;
import java.util.Objects;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WMShellBaseModule$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda0 INSTANCE = new WMShellBaseModule$$ExternalSyntheticLambda0();

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        BackAnimationController backAnimationController = (BackAnimationController) obj;
        Objects.requireNonNull(backAnimationController);
        return backAnimationController.mBackAnimation;
    }
}
