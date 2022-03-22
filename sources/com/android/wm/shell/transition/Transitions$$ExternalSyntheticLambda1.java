package com.android.wm.shell.transition;

import android.window.WindowContainerTransaction;
import com.android.wm.shell.transition.Transitions;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class Transitions$$ExternalSyntheticLambda1 implements Transitions.TransitionFinishCallback {
    public final /* synthetic */ Transitions f$0;
    public final /* synthetic */ Transitions.ActiveTransition f$1;

    public /* synthetic */ Transitions$$ExternalSyntheticLambda1(Transitions transitions, Transitions.ActiveTransition activeTransition) {
        this.f$0 = transitions;
        this.f$1 = activeTransition;
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionFinishCallback
    public final void onTransitionFinished(WindowContainerTransaction windowContainerTransaction) {
        Transitions transitions = this.f$0;
        Transitions.ActiveTransition activeTransition = this.f$1;
        Objects.requireNonNull(transitions);
        transitions.onFinish(activeTransition.mToken, windowContainerTransaction, false);
    }
}
