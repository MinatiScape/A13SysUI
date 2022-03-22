package com.android.wm.shell.back;

import android.window.IOnBackInvokedCallback;
import com.android.wm.shell.back.BackAnimationController;
import java.lang.Thread;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BackAnimationController$IBackAnimationImpl$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ BackAnimationController$IBackAnimationImpl$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                BackAnimationController.IBackAnimationImpl iBackAnimationImpl = (BackAnimationController.IBackAnimationImpl) this.f$0;
                BackAnimationController backAnimationController = (BackAnimationController) obj;
                Objects.requireNonNull(iBackAnimationImpl);
                iBackAnimationImpl.mController.setBackToLauncherCallback((IOnBackInvokedCallback) this.f$1);
                return;
            default:
                ((Thread.UncaughtExceptionHandler) obj).uncaughtException((Thread) this.f$0, (Throwable) this.f$1);
                return;
        }
    }
}
