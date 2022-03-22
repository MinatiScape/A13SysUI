package com.android.wm.shell;

import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.apppairs.AppPairsPool;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ShellInitImpl$$ExternalSyntheticLambda1 implements Consumer {
    public static final /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda1 INSTANCE = new ShellInitImpl$$ExternalSyntheticLambda1();

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        AppPairsController appPairsController = (AppPairsController) obj;
        Objects.requireNonNull(appPairsController);
        if (appPairsController.mPairsPool == null) {
            appPairsController.setPairsPool(new AppPairsPool(appPairsController));
        }
    }
}
