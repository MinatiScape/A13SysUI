package com.android.wm.shell.pip.tv;

import android.app.TaskInfo;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.SurfaceControl;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.transition.Transitions;
/* loaded from: classes.dex */
public final class TvPipTransition extends PipTransitionController {
    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public final WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        return null;
    }

    @Override // com.android.wm.shell.pip.PipTransitionController
    public final void onFinishResize(TaskInfo taskInfo, Rect rect, int i, SurfaceControl.Transaction transaction) {
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public final boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback) {
        return false;
    }

    public TvPipTransition(TvPipBoundsState tvPipBoundsState, TvPipMenuController tvPipMenuController, TvPipBoundsAlgorithm tvPipBoundsAlgorithm, PipAnimationController pipAnimationController, Transitions transitions) {
        super(tvPipBoundsState, tvPipMenuController, tvPipBoundsAlgorithm, pipAnimationController, transitions);
    }
}
