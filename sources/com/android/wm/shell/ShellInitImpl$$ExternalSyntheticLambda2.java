package com.android.wm.shell;

import com.android.wm.shell.fullscreen.FullscreenUnfoldController;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ShellInitImpl$$ExternalSyntheticLambda2 implements Consumer {
    public static final /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda2 INSTANCE = new ShellInitImpl$$ExternalSyntheticLambda2();

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        FullscreenUnfoldController fullscreenUnfoldController = (FullscreenUnfoldController) obj;
        Objects.requireNonNull(fullscreenUnfoldController);
        fullscreenUnfoldController.mProgressProvider.addListener(fullscreenUnfoldController.mExecutor, fullscreenUnfoldController);
        fullscreenUnfoldController.mDisplayInsetsController.addInsetsChangedListener(0, fullscreenUnfoldController);
    }
}
